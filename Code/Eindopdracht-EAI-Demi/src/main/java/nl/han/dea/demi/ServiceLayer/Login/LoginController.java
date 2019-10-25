package nl.han.dea.demi.ServiceLayer.Login;

import nl.han.dea.demi.DTO.Login.UserDTO;
import nl.han.dea.demi.ServiceLayer.Authentication.AuthenticationDAO;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class LoginController {

    private AuthenticationDAO authenticationDAO;

    @Inject
    public void setAuthenticationDAO(AuthenticationDAO authenticationDAO) {
        this.authenticationDAO = authenticationDAO;
    }

    @Inject
    private LoginService loginService;

    @POST
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response checkLoginValues(UserDTO user) {
        if(loginService.checkLoginValues(user)) {
            System.out.println("Authentication successful");
            return Response.accepted(authenticationDAO.getToken(user.getUser())).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}