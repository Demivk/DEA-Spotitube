package nl.han.dea.demi.DTO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/spotitube")
public class HelloWorld {

    @GET
    @Path("/helloworld")
    @Produces(MediaType.TEXT_PLAIN)
    public String sendHelloWorld() {
        return "Hello world";
    }
}
