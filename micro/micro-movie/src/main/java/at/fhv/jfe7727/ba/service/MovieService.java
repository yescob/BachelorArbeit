package at.fhv.jfe7727.ba.service;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import at.fhv.jfe7727.ba.model.Movie;
import at.fhv.jfe7727.ba.model.MovieRepository;
import at.fhv.jfe7727.ba.model.PersonName;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

import fish.payara.cloud.connectors.kafka.api.KafkaConnection;
import fish.payara.cloud.connectors.kafka.api.KafkaConnectionFactory;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.cache.annotation.CacheRemove;
import javax.cache.annotation.CacheRemoveAll;
import javax.cache.annotation.CacheResult;
import javax.resource.ConnectionFactoryDefinition;
import javax.resource.spi.TransactionSupport.TransactionSupportLevel;
import org.apache.kafka.clients.producer.ProducerRecord;

@ConnectionFactoryDefinition(name = "java:module/env/KafkaConnectionFactory",
        description = "Kafka Connection Factory",
        interfaceName = "fish.payara.cloud.connectors.kafka.KafkaConnectionFactory",
        resourceAdapter = "kafka-rar-0.7.0",
        minPoolSize = 2,
        maxPoolSize = 2,
        transactionSupport = TransactionSupportLevel.NoTransaction,
        properties = {
            "bootstrapServersConfig="+"${ENV=KAFKA_BOOTSTRAP}",
            "clientId=PayaraMicroMessenger"
        })
@Transactional
@ApplicationScoped
public class MovieService {

    @Resource(lookup = "java:module/env/KafkaConnectionFactory")
    KafkaConnectionFactory factory;

    @Inject
    MovieRepository movieRepository;

    private static final Logger LOGGER = Logger.getLogger(MovieService.class.getName());

    @CacheResult(cacheName ="movie")
    public List<Movie> getAllMovies() {
        return movieRepository.listAll();
    }

    @CacheResult(cacheName ="movie")
    public Movie getMovieById(Long movieId) throws MovieNotFoundException {
        Optional<Movie> optMovie = movieRepository.findByIdOptional(movieId);
        if(optMovie.isPresent()){
            return optMovie.get();
        }
        throw new MovieNotFoundException();
    }

    @CacheResult(cacheName ="movie")
    public List<Movie> getMovieByTitle(String title) {
        return movieRepository.getByTitle(title);
    }

    @CacheRemoveAll(cacheName ="movie")
    public Movie addMovie(Movie movie) {
        movieRepository.persist(movie);
        try (KafkaConnection conn = factory.createConnection()) {
            conn.send(new ProducerRecord<>("movie-added", 
            convertMovieToJsonString(movie)));
            LOGGER.info("MovieAdded Message sent");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return movie;
        
    }
    @CacheRemove(cacheName ="movie")
    public Movie deleteMovie(Long movieId) throws MovieNotFoundException {
        Optional<Movie> optMovie = movieRepository.findByIdOptional(movieId);
        if(optMovie.isPresent()){
            movieRepository.deleteById(movieId);
            try (KafkaConnection conn = factory.createConnection()) {
                conn.send(new ProducerRecord<>("movie-deleted", convertMovieToJsonString(optMovie.get())));
                LOGGER.info("MovieDeleted Message sent"); 
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
            return optMovie.get();
        }
        throw new MovieNotFoundException();
    }

    private String convertMovieToJsonString(Movie movie){
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder().add("movieId", movie.getMovieId().toString()).add("title", movie.getTitle());
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
                
        for(PersonName person : movie.getPersonNames()) {
            arrayBuilder.add(Json.createObjectBuilder().add("firstName", person.getFirstName())
            .add("lastName", person.getLastName()));
        }
                
        objectBuilder.add("personNames", arrayBuilder);
        return objectBuilder.build().toString();
    }

}
