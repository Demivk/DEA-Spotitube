package nl.han.dea.demi.ServiceLayer.Playlist;

import javax.inject.Inject;

public class PlaylistsService {

    private PlaylistsDAO playlistsDAO;

    @Inject
    public void setPlaylistsDAO(PlaylistsDAO playlistsDAO) {
        this.playlistsDAO = playlistsDAO;
    }

    public boolean isUserOwner(String token, int id) {
        return playlistsDAO.isUserOwner(token, id);
    }
}
