package nl.han.dea.demi.DataAccessLayer.DAO;

import nl.han.dea.demi.DataAccessLayer.Databases.MSSQL.DatabaseConnectionMSSQL;
import nl.han.dea.demi.DTO.Playlists.PlaylistDTO;
import nl.han.dea.demi.DTO.Playlists.PlaylistsDTO;
import nl.han.dea.demi.ServiceLayer.Playlist.PlaylistsDAO;

import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PlaylistsDAOImpl implements PlaylistsDAO {

    @Inject
    private DatabaseConnectionMSSQL databaseMSSQL;

    /**
     * Gets all playlists from the databaseMSSQL from the user with the given token
     * @param token
     * @return all playlists
     */
    @Override
    public PlaylistsDTO getAllPlaylists(String token) {
        PlaylistsDTO playlistsDTO = new PlaylistsDTO();

        String selectQuery = "SELECT Playlist.id, Playlist.[name], Playlist.[owner] " +
                             "FROM Playlist INNER JOIN PlaylistUser ON Playlist.id = PlaylistUser.playlistId " +
                                           "INNER JOIN [Authentication] ON PlaylistUser.[user] = [Authentication].[user] " +
                             "WHERE [Authentication].token = '" + token + "'";

        PreparedStatement psSelect = databaseMSSQL.preparedStatement(selectQuery);
        ResultSet rsSelect = databaseMSSQL.getResult(psSelect);

        playlistsDTO.setPlaylists(addPlaylistFromResultSet(rsSelect));
        playlistsDTO.setLength(getTotalPlaylistsLength(token));

        return playlistsDTO;
    }

    /**
     * Updates the name of the playlist
     * @param playlist
     * @param id
     * @param token
     * @return getAllPlaylists(token)
     */
    @Override
    public PlaylistsDTO editPlaylist(PlaylistDTO playlist, int id, String token) {
        String name = playlist.getName();

        String updateQuery = "UPDATE Playlist SET [name] = '" + name + "' WHERE id = " + id;

        PreparedStatement ps = databaseMSSQL.preparedStatement(updateQuery);

        try {
            ps.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return getAllPlaylists(token);
    }

    /**
     * Deletes the playlist from all three tables it exists in
     * @param id
     * @param token
     * @return getAllPlaylists(token)
     */
    @Override
    public PlaylistsDTO deletePlaylist(int id, String token) {
        String deletePlaylistUserQuery = "DELETE FROM PlaylistUser WHERE playlistId = " + id;
        PreparedStatement psPU = databaseMSSQL.preparedStatement(deletePlaylistUserQuery);

        String deletePlaylistTrackQuery = "DELETE FROM PlaylistTrack WHERE playlistId = " + id;
        PreparedStatement psPT = databaseMSSQL.preparedStatement(deletePlaylistTrackQuery);

        String deletePlaylistQuery = "DELETE FROM Playlist WHERE id = " + id;
        PreparedStatement psP = databaseMSSQL.preparedStatement(deletePlaylistQuery);

        try {
            psPU.executeUpdate();
            psPT.executeUpdate();
            psP.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return getAllPlaylists(token);
    }

    /**
     * Adds a new playlist to the databaseMSSQL
     * @param playlist
     * @param token
     * @return getAllPlaylists(token)
     */
    @Override
    public PlaylistsDTO addPlaylist(PlaylistDTO playlist, String token) {
        String user = "";
        int id = generateNewPlaylistId(playlist.getId());
        String name = playlist.getName();
        int owner = 1;

        // Puts new playlist in table 'Playlist'
        String insertPlaylistQuery = "INSERT INTO Playlist VALUES (" + id + ", '" + name + "', " + owner + ")";

        PreparedStatement psP = databaseMSSQL.preparedStatement(insertPlaylistQuery);

        // Gets username using token
        String selectAuthenticationQuery = "SELECT [user] FROM [Authentication] WHERE token = '" + token + "'";

        PreparedStatement psA = databaseMSSQL.preparedStatement(selectAuthenticationQuery);
        ResultSet rsA = databaseMSSQL.getResult(psA);

        try {
            while(rsA != null && rsA.next()) {
                user = rsA.getString("user");
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        // Connects new playlist to user
        String insertPlaylistUserQuery = "INSERT INTO PlaylistUser VALUES ('" + user + "', " + id + ")";

        PreparedStatement psPU = databaseMSSQL.preparedStatement(insertPlaylistUserQuery);

        try {
            psP.executeUpdate();
            psPU.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return getAllPlaylists(token);
    }

    /**
     * Puts all values in a new Playlist, adds it to the ArrayList and returns the ArrayList
     * @param rs
     * @return ArrayList of Playlist
     */
    @Override
    public ArrayList<PlaylistDTO> addPlaylistFromResultSet(ResultSet rs) {
        ArrayList<PlaylistDTO> playlists = new ArrayList<PlaylistDTO>();

        try {
            while(rs != null && rs.next()) {
                PlaylistDTO playlist = new PlaylistDTO();
                playlist.setId(rs.getInt("id"));
                playlist.setName(rs.getString("name"));
                playlist.setOwner(rs.getBoolean("owner"));
                playlists.add(playlist);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return playlists;
    }

    /**
     * Calculates the total length of all tracks in all playlists together from the user with the given token
     * @param token
     * @return total length
     */
    private int getTotalPlaylistsLength(String token) {
        String getTotalLengthQuery = "SELECT SUM(Track.duration) AS totalDuration " +
                                     "FROM Track INNER JOIN PlaylistTrack ON Track.id = PlaylistTrack.trackId " +
                                                "INNER JOIN PlaylistUser ON PlaylistUser.playlistId = PlaylistTrack.playlistId " +
                                                "INNER JOIN [Authentication] ON [Authentication].[user] = PlaylistUser.[user] " +
                                     "WHERE [Authentication].token = '" + token + "'";

        PreparedStatement ps = databaseMSSQL.preparedStatement(getTotalLengthQuery);
        ResultSet rs = databaseMSSQL.getResult(ps);

        int length = 0;

        try {
            while(rs != null && rs.next()) {
                length = rs.getInt("totalDuration");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return length;
    }

    /**
     * Generates an id for a new playlist by getting the highest id + 1
     * @param playlistId
     * @return new id
     */
    private int generateNewPlaylistId(int playlistId) {
        String selectHighestIdQuery = "SELECT MAX(id) AS 'latestId' FROM Playlist";

        PreparedStatement psHighest = databaseMSSQL.preparedStatement(selectHighestIdQuery);
        ResultSet rsHighest = databaseMSSQL.getResult(psHighest);

        try {
            while(rsHighest != null && rsHighest.next()) {
                playlistId = rsHighest.getInt("latestId") + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return playlistId;
    }

    /**
     * Checks if the user owns the playlist with token 'token' and playlistId 'id'
     * @param token
     * @return owner (true if owner, false if not owner)
     */
    @Override
    public boolean isUserOwner(String token, int id) {
        boolean owner = false;
        String selectQuery = "SELECT Playlist.[owner] " +
                             "FROM Playlist INNER JOIN PlaylistUser ON Playlist.id = PlaylistUser.playlistId " +
                                           "INNER JOIN [Authentication] ON [Authentication].[user] = PlaylistUser.[user] " +
                             "WHERE [Authentication].token = '" + token + "' AND Playlist.id = " + id;

        PreparedStatement ps = databaseMSSQL.preparedStatement(selectQuery);
        ResultSet rs = databaseMSSQL.getResult(ps);

        try {
            while(rs != null && rs.next()) {
                owner = rs.getBoolean("owner");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return owner;
    }
}
