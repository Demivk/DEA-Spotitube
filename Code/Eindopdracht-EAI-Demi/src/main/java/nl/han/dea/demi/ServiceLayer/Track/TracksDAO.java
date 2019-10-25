package nl.han.dea.demi.ServiceLayer.Track;

import nl.han.dea.demi.DTO.Tracks.TrackDTO;
import nl.han.dea.demi.DTO.Tracks.TracksDTO;

import java.sql.ResultSet;
import java.util.ArrayList;

public interface TracksDAO {
    TracksDTO getAllTracksInPlaylist(int playlistId, String token);
    TracksDTO getAllTracksAvailable(int forPlaylist, String token);
    TracksDTO deleteTrackFromPlaylist(int playlistId, int trackId, String token);
    TracksDTO addTrackToPlaylist(TrackDTO track, int playlistId, String token);
    ArrayList<TrackDTO> addTrackFromResultSet(ResultSet rs);
}
