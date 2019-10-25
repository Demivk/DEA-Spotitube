package nl.han.dea.demi.DTO.Playlists;

import nl.han.dea.demi.DTO.Tracks.TrackDTO;

import java.util.List;

public class PlaylistDTO {
    private int id;
    private String name;
    private boolean owner;
    private List<TrackDTO> tracks;

    public int getId() {
        return this.id;
    }

    public void setId(int newId) {
        this.id = newId;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean newOwner) {
        this.owner = newOwner;
    }

    public List<TrackDTO> getTracks() {
        return this.tracks;
    }

    public void setTracks(List<TrackDTO> newTracks) {
        this.tracks = newTracks;
    }
}