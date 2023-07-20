package at.fhv.jfe7727.ba.rest;

import java.util.Random;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import at.fhv.jfe7727.ba.restclient.MovieRestClient;
import at.fhv.jfe7727.ba.restclient.PersonRestClient;

@Path("/resiliance")
public class ResilianceResource {

    private static final Logger LOGGER = Logger.getLogger(ResilianceResource.class.getName());

    @Inject
    @RestClient
    MovieRestClient movieRestClient;

    @Inject
    @RestClient
    PersonRestClient personRestClient;

    @GET
    @Path("/retry")
    @Produces(MediaType.APPLICATION_JSON)
    @Retry(maxRetries = 2)
    @Fallback(fallbackMethod = "fallback")
    public Response getAllMovies() {
        maybeFail();
        LOGGER.info("GET All Movies accessed");
        return movieRestClient.getAllMovies();
    }

    @GET
    @Path("/timeout")
    @Produces(MediaType.APPLICATION_JSON)
    @Timeout(250)
    public Response getAllPersons() {
        
        try {
            maybeTimeOut();
            LOGGER.info("GET All Persons accessed");
            return personRestClient.getAllPersons();
        } catch (InterruptedException e) {
            LOGGER.warning("GET All Persons timed out");
            return null;
        }
    }

    public Response fallback() {
        LOGGER.info("Falling back");
        return Response.status(Status.SERVICE_UNAVAILABLE).build();
    }

    private void maybeTimeOut() throws InterruptedException {
        Thread.sleep(new Random().nextInt(1000)); 
    }

    private void maybeFail() {
        if (new Random().nextBoolean()) {
            LOGGER.warning("Failure occured");
            throw new RuntimeException("Failure");
        }
    }
}