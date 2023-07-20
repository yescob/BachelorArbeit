package at.fhv.jfe7727.ba.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;

import at.fhv.jfe7727.ba.model.Movie;
import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;
import io.smallrye.reactive.messaging.annotations.Broadcast;

//
@Transactional
@ApplicationScoped
public class MovieService {

    private static final Logger LOGGER = Logger.getLogger(MovieService.class);

    @Inject
    @Channel("movie-added")
    @Broadcast
    Emitter<Movie> movieAddedEmitter;

    @Inject
    @Channel("movie-deleted")
    @Broadcast
    Emitter<Movie> movieDeletedEmitter;

    @CacheResult(cacheName = "movie")
    public List<Movie> getAllMovies() {
        return Movie.listAll();
    }

    @CacheResult(cacheName = "movie")
    public Movie getMovieById(Long movieId) throws MovieNotFoundException {
        Optional<Movie> optMovie = Movie.findByIdOptional(movieId);
        if(optMovie.isPresent()){
            return optMovie.get();
        }
        throw new MovieNotFoundException();
    }

    @CacheResult(cacheName = "movie")
    public List<Movie> getMovieByTitle(String title) {
        return Movie.list("title", title);
    }

    @CacheInvalidateAll(cacheName = "movie")
    public CompletionStage<Movie> addMovie(Movie movie) {
        movie.setMovieId(null);
        Movie.persist(movie);
        LOGGER.info("MovieAdded Message sent");
        return movieAddedEmitter.send(movie).thenApply(result -> movie);
        
    }

    @CacheInvalidate(cacheName = "movie")
    public CompletionStage<Movie> deleteMovie(Long movieId) throws MovieNotFoundException {
        Optional<Movie> optMovie = Movie.findByIdOptional(movieId);
        if(optMovie.isPresent()){
            Movie.deleteById(movieId);
            LOGGER.info("MovieDeleted Message sent"); 
            return movieDeletedEmitter.send(optMovie.get()).thenApply(result -> optMovie.get());
        }
        throw new MovieNotFoundException();
    }

}
