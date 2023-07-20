package at.fhv.jfe7727.ba.restclient;

import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@RegisterRestClient(configKey="person-api")
@RegisterClientHeaders(AuthHeaderFactory.class)
public interface PersonRestClient {

    @GET
    @Path("/person")
    public Response getAllPersons();

    @DELETE
    @Path("/person")
    public Response deleteAllPersons();

}
