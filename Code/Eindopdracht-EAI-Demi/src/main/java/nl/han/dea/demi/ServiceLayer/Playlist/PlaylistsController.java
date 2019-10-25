package nl.han.dea.demi.ServiceLayer.Playlist;

import nl.han.dea.demi.DTO.Playlists.PlaylistDTO;
import nl.han.dea.demi.ServiceLayer.Authentication.AuthenticationService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class PlaylistsController {

    private PlaylistsDAO playlistsDAO;

    @Inject
    public void setPlaylistsDAO(PlaylistsDAO playlistsDAO) {
        this.playlistsDAO = playlistsDAO;
    }

    @Inject
    private AuthenticationService authenticationService;

    @Inject
    private PlaylistsService playlistsService;

    @GET
    @Path("playlists")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlaylists(@QueryParam("token") String token) {
        if(authenticationService.isTokenValid(token)) {
            return Response.ok(playlistsDAO.getAllPlaylists(token)).build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @PUT
    @Path("playlists/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editPlaylist(@QueryParam("token") String token, @PathParam("id") int id, PlaylistDTO playlist) {
        if(authenticationService.isTokenValid(token) && playlistsService.isUserOwner(token, id)) {
            return Response.ok(playlistsDAO.editPlaylist(playlist, id, token)).build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @DELETE
    @Path("playlists/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deletePlaylist(@QueryParam("token") String token, @PathParam("id") int id) {
        if(authenticationService.isTokenValid(token) && playlistsService.isUserOwner(token, id)) {
            return Response.ok(playlistsDAO.deletePlaylist(id, token)).build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @POST
    @Path("playlists")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPlaylist(@QueryParam("token") String token, PlaylistDTO playlist) {
        if(authenticationService.isTokenValid(token)) {
            return Response.ok(playlistsDAO.addPlaylist(playlist, token)).build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }
}
