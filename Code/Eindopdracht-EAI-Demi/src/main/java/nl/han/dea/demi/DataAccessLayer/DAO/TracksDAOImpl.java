package nl.han.dea.demi.DataAccessLayer.DAO;

import nl.han.dea.demi.DataAccessLayer.Databases.MSSQL.DatabaseConnectionMSSQL;
import nl.han.dea.demi.DTO.Tracks.TrackDTO;
import nl.han.dea.demi.DTO.Tracks.TracksDTO;
import nl.han.dea.demi.ServiceLayer.Track.TracksDAO;

import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TracksDAOImpl implements TracksDAO {

    @Inject
    private DatabaseConnectionMSSQL databaseMSSQL;

    /**
     * Gets all tracks from the current playlist
     * @param playlistId
     * @param token
     * @return all tracks
     */
    @Override
    public TracksDTO getAllTracksInPlaylist(int playlistId, String token) {
        TracksDTO tracksDTO = new TracksDTO();

        String selectQuery = "SELECT Track.id, Track.title, Track.performer, Track.duration, Track.album, Track.playcount, Track.publicationdate, Track.[description], Track.offlineAvailable " +
                             "FROM Track INNER JOIN PlaylistTrack ON Track.id = PlaylistTrack.trackId " +
                                        "INNER JOIN PlaylistUser ON PlaylistUser.playlistId = PlaylistTrack.playlistId " +
                                        "INNER JOIN [Authentication] ON [Authentication].[user] = PlaylistUser.[user] " +
                             "WHERE PlaylistTrack.playlistId = " + playlistId + " AND [Authentication].token = '" + token + "'";

        PreparedStatement ps = databaseMSSQL.preparedStatement(selectQuery);
        ResultSet rs = databaseMSSQL.getResult(ps);

        tracksDTO.setTracks(addTrackFromResultSet(rs));

        return tracksDTO;
    }

    /**
     * Gets all tracks that are not in the current playlist
     * @param forPlaylist
     * @param token
     * @return tracks that are not in the current playlist
     */
    @Override
    public TracksDTO getAllTracksAvailable(int forPlaylist, String token) {
        TracksDTO tracksDTO = new TracksDTO();

        String selectQuery = "SELECT * FROM Track WHERE NOT EXISTS" +
                            "(SELECT * FROM PlaylistTrack WHERE PlaylistTrack.trackId = Track.id AND PlaylistTrack.playlistId = " + forPlaylist + ")";

        PreparedStatement ps = databaseMSSQL.preparedStatement(selectQuery);
        ResultSet rs = databaseMSSQL.getResult(ps);

        tracksDTO.setTracks(addTrackFromResultSet(rs));

        return tracksDTO;
    }

    /**
     * Deletes the track from the current playlist
     * @param playlistId
     * @param trackId
     * @param token
     * @return getAllTracksInPlaylist(playlistId, token)
     */
    @Override
    public TracksDTO deleteTrackFromPlaylist(int playlistId, int trackId, String token) {
        String deletePlaylistTrackQuery = "DELETE FROM PlaylistTrack WHERE playlistId = " + playlistId + " AND trackId = " + trackId;

        PreparedStatement ps = databaseMSSQL.preparedStatement(deletePlaylistTrackQuery);

        try {
            ps.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return getAllTracksInPlaylist(playlistId, token);
    }

    /**
     * Adds an existing track to the current playlist
     * @param track
     * @param playlistId
     * @param token
     * @return getAllTracksInPlaylist(playlistId, token)
     */
    @Override
    public TracksDTO addTrackToPlaylist(TrackDTO track, int playlistId, String token) {
        int trackId = track.getId();
        boolean trackOfflineAvailable = track.isOfflineAvailable();
        int newOfflineAvailable = 0;

        // Connects track to playlist
        String insertQuery = "INSERT INTO PlaylistTrack VALUES (" + playlistId + ", " + trackId + ")";

        PreparedStatement psInsert = databaseMSSQL.preparedStatement(insertQuery);

        // Updates the offlineAvailable state
        if(trackOfflineAvailable) {
            newOfflineAvailable = 1;
        } else {
            newOfflineAvailable = 0;
        }
        String updateQuery = "UPDATE Track SET offlineAvailable = " + newOfflineAvailable + " WHERE id = " + trackId;
        PreparedStatement psUpdate = databaseMSSQL.preparedStatement(updateQuery);

        try {
            psInsert.executeUpdate();
            psUpdate.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return getAllTracksInPlaylist(playlistId, token);
    }

    /**
     * Puts all values in a new Track, adds it to the ArrayList and returns the ArrayList
     * @param rs
     * @return Arraylist of Track
     */
    @Override
    public ArrayList<TrackDTO> addTrackFromResultSet(ResultSet rs) {
        ArrayList<TrackDTO> tracks = new ArrayList<TrackDTO>();

        try {
            while(rs != null && rs.next()) {
                TrackDTO track = new TrackDTO();
                track.setId(rs.getInt("id"));
                track.setTitle(rs.getString("title"));
                track.setPerformer(rs.getString("performer"));
                track.setDuration(rs.getInt("duration"));
                track.setAlbum(rs.getString("album"));
                track.setPlaycount(rs.getInt("playcount"));
                track.setPublicationDate(rs.getString("publicationdate"));
                track.setDescription(rs.getString("description"));
                track.setOfflineAvailable(rs.getBoolean("offlineAvailable"));
                tracks.add(track);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return tracks;
    }
}