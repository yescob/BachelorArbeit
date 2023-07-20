package at.fhv.jfe7727.ba.restclient;

import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import at.fhv.jfe7727.ba.Movie;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@RegisterRestClient(configKey="movie-api")
@RegisterClientHeaders(AuthHeaderFactory.class)
public interface MovieRestClient {

    @GET
    @Path("/movie")
    public Response getAllMovies();

    @POST
    @Path("/movie")
    public Response addMovie(Movie movie);

    @DELETE
    @Path("/movie/{movieId}")
    public Response deleteMovie(@PathParam("movieId") Long movieId);

    @GET
    @Path("/health")
    public HealthCheckResponse getHealResponse();
}
