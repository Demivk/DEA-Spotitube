package nl.han.dea.demi.ServiceLayer.Playlist;

import nl.han.dea.demi.DTO.Playlists.PlaylistDTO;
import nl.han.dea.demi.DTO.Playlists.PlaylistsDTO;

import java.sql.ResultSet;
import java.util.ArrayList;

public interface PlaylistsDAO {
    PlaylistsDTO getAllPlaylists(String token);
    PlaylistsDTO editPlaylist(PlaylistDTO playlist, int id, String token);
    PlaylistsDTO deletePlaylist(int id, String token);
    PlaylistsDTO addPlaylist(PlaylistDTO playlist, String token);
    ArrayList<PlaylistDTO> addPlaylistFromResultSet(ResultSet rs);
    boolean isUserOwner(String token, int id);
}
