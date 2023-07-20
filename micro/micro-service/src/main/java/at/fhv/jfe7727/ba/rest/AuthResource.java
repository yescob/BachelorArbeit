package at.fhv.jfe7727.ba.rest;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import at.fhv.jfe7727.ba.restclient.AuthHeaderFactory;
import at.fhv.jfe7727.ba.restclient.MovieRestClient;
import at.fhv.jfe7727.ba.restclient.PersonRestClient;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {
    
    
    @Inject
    @RestClient
    MovieRestClient movieRestClient;

    @Inject
    @RestClient
    PersonRestClient personRestClient;

    @Inject
    AuthHeaderFactory authHeaderFactory;


    @DELETE
    @Path("/movie/{movieId}")
    public Response deleteMovie(@PathParam("movieId") Long movieId){
        return movieRestClient.deleteMovie(movieId);
    }

    @DELETE
    @Path("/person")
    public Response deleteAllPersons(){
        return personRestClient.deleteAllPersons();
    }

    @GET
    public String token(){
        return authHeaderFactory.getToken();
    }

}
