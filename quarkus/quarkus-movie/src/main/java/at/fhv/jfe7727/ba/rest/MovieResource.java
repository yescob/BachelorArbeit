package at.fhv.jfe7727.ba.rest;

import java.util.concurrent.ExecutionException;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponseSchema;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

import at.fhv.jfe7727.ba.model.Movie;
import at.fhv.jfe7727.ba.service.MovieNotFoundException;
import at.fhv.jfe7727.ba.service.MovieService;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;

@Path("/movie")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Movie-Endpoints")
public class MovieResource {

    private static final Logger LOGGER = Logger.getLogger(MovieResource.class);

    @Inject
    private MovieService movieService;

    @Inject
    MeterRegistry meterRegistry;

    @ConfigProperty(name = "not.found.error.message")
    String notFoundErrorMessage;

    @GET
    @APIResponseSchema(value = Movie.class,
        responseDescription = "A list of all movies",
        responseCode = "200")
    @Operation(
        summary = "Get list of all movies",
        description = "Returns the list of all movies stored in the database")
    public Response allMovies(){
        LOGGER.info("Get all Movies");
        meterRegistry.counter("times.all.movies.accessed").increment();
        return Response.ok(movieService.getAllMovies()).build();
    }

    @GET
    @Path("/{movieId}")
    @APIResponse(
        responseCode = "404")
    @APIResponseSchema(value = Movie.class,
        responseDescription = "Movie found",
        responseCode = "200")
    @Operation(
        summary = "Find movie by id",
        description = "Returns the movie with the given id")
    public Response movieById(@PathParam("movieId") Long movieId){
        LOGGER.info("Get Movie with Id: " + movieId);
        try {
            return Response.ok(movieService.getMovieById(movieId)).build();
        } catch (MovieNotFoundException e) {
            return Response.status(Status.NOT_FOUND).entity(notFoundErrorMessage).build();
        }
    }

    @GET 
    @Path("search")
    @APIResponseSchema(value = Movie.class,
        responseDescription = "Movies found",
        responseCode = "200")
    @Operation(
        summary = "Find movie by title",
        description = "Return a list of movies with the given title")
    public Response movieByTitle(@QueryParam("title") String title){
        LOGGER.info("Search for Movie");
        return Response.ok(movieService.getMovieByTitle(title)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed(description = "time_to_add_movie")
    @Operation(
        summary = "Add new movie",
        description = "Creates and adds a new movie to the database")
    public Movie addMovie(Movie movie){
        LOGGER.info("Create new Movie");
        try {
            return movieService.addMovie(movie).toCompletableFuture().get();
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
    }

    @DELETE
    @RolesAllowed("Admin")
    @Path("/{movieId}")
    @APIResponse(
        responseCode = "404")
    @APIResponseSchema(value = Movie.class,
        responseDescription = "Movie sucessful deleted",
        responseCode = "200")
    @Operation(
        summary = "Deleted movie",
        description = "Delete movie with the given id")
    public Response deleteMovie(@PathParam("movieId") Long movieId){
        LOGGER.info("Delete Movie with Id: " + movieId);
        try {
            return Response.ok(movieService.deleteMovie(movieId).toCompletableFuture().get()).build();
        } catch (InterruptedException | ExecutionException | MovieNotFoundException e) {
            return Response.status(Status.NOT_FOUND).entity(notFoundErrorMessage).build();
        }
    }
    
}