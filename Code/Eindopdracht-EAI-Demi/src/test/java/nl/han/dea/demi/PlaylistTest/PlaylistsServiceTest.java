package nl.han.dea.demi.PlaylistTest;

import nl.han.dea.demi.DTO.Authentication.AuthenticationDTO;
import nl.han.dea.demi.DTO.Playlists.PlaylistDTO;
import nl.han.dea.demi.DataAccessLayer.Databases.MSSQL.DatabaseConnectionMSSQL;
import nl.han.dea.demi.ServiceLayer.Playlist.PlaylistsDAO;
import nl.han.dea.demi.ServiceLayer.Playlist.PlaylistsService;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlaylistsServiceTest {

    @Mock
    private DatabaseConnectionMSSQL databaseMSSQL;

    @Test
    public void testIfUserOwnsPlaylistPass() {
        // Arrange.
        PlaylistsService sut = new PlaylistsService();
        PlaylistsDAO mockPlaylistsDAO = mock(PlaylistsDAO.class);
        sut.setPlaylistsDAO(mockPlaylistsDAO);

        boolean expected = true;

        int playlistId = 11;
        String playlistName = "TestList";
        boolean owner = true;

        String userName = "someUser";
        String token = "1234-5678";

        AuthenticationDTO testAuth = new AuthenticationDTO();
        testAuth.setUser(userName);
        testAuth.setToken(token);

        PlaylistDTO testPlaylist = new PlaylistDTO();
        testPlaylist.setId(playlistId);
        testPlaylist.setName(playlistName);
        testPlaylist.setOwner(owner);

        when(mockPlaylistsDAO.isUserOwner(token, playlistId)).thenReturn(true);

        // Act.
        boolean actual = sut.isUserOwner(testAuth.getToken(), testPlaylist.getId());

        // Assert.
        assertEquals(expected, actual);
    }

    @Test
    public void testIfUserOwnsPlaylistFail() {
        // Arrange.
        PlaylistsService sut = new PlaylistsService();
        PlaylistsDAO mockPlaylistsDAO = mock(PlaylistsDAO.class);
        sut.setPlaylistsDAO(mockPlaylistsDAO);

        boolean expected = false;

        int playlistId = 11;
        String playlistName = "TestList";
        boolean owner = false;

        String userName = "someUser";
        String token = "1234-5678";

        AuthenticationDTO testAuth = new AuthenticationDTO();
        testAuth.setUser(userName);
        testAuth.setToken(token);

        PlaylistDTO testPlaylist = new PlaylistDTO();
        testPlaylist.setId(playlistId);
        testPlaylist.setName(playlistName);
        testPlaylist.setOwner(owner);

        when(mockPlaylistsDAO.isUserOwner(token, playlistId)).thenReturn(false);

        // Act.
        boolean actual = sut.isUserOwner(testAuth.getToken(), testPlaylist.getId());

        // Assert.
        assertEquals(expected, actual);
    }
}
