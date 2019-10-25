package nl.han.dea.demi.DTO.Tracks;

import java.util.ArrayList;

public class TracksDTO {

    private ArrayList<TrackDTO> tracks;

    public TracksDTO() {
        tracks = new ArrayList<TrackDTO>();
    }

    public ArrayList<TrackDTO> getTracks() {
        return tracks;
    }

    public void setTracks(ArrayList<TrackDTO> tracks) {
        this.tracks = tracks;
    }
}
