package nl.han.dea.demi.ServiceLayer.Track;

import nl.han.dea.demi.DTO.Tracks.TrackDTO;
import nl.han.dea.demi.ServiceLayer.Authentication.AuthenticationService;
import nl.han.dea.demi.ServiceLayer.Playlist.PlaylistsService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class TracksController {

    private TracksDAO tracksDAO;

    @Inject
    public void setTracksDAO(TracksDAO tracksDAO) {
        this.tracksDAO = tracksDAO;
    }

    @Inject
    private AuthenticationService authenticationService;

    @Inject
    private PlaylistsService playlistsService;

    @GET
    @Path("tracks")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAvailableTracks(@QueryParam("forPlaylist") int forPlaylist, @QueryParam("token") String token) {
        if(authenticationService.isTokenValid(token)) {
            return Response.ok(tracksDAO.getAllTracksAvailable(forPlaylist, token)).build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @GET
    @Path("playlists/{id}/tracks")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTracksInPlaylist(@PathParam("id") int playlistId, @QueryParam("token") String token) {
        if(authenticationService.isTokenValid(token)) {
            return Response.ok(tracksDAO.getAllTracksInPlaylist(playlistId, token)).build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @DELETE
    @Path("playlists/{pId}/tracks/{tId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteTrackFromPlaylist(@QueryParam("token") String token, @PathParam("pId") int playlistId, @PathParam("tId") int trackId) {
        if(authenticationService.isTokenValid(token) && playlistsService.isUserOwner(token, playlistId)) {
            return Response.ok(tracksDAO.deleteTrackFromPlaylist(playlistId, trackId, token)).build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @POST
    @Path("playlists/{id}/tracks")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addTrackToPlaylist(@QueryParam("token") String token, @PathParam("id") int playlistId, TrackDTO track) {
        if(authenticationService.isTokenValid(token) && playlistsService.isUserOwner(token, playlistId)) {
            return Response.ok(tracksDAO.addTrackToPlaylist(track, playlistId, token)).build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }
}