package at.fhv.jfe7727.ba.service;

import java.util.concurrent.ExecutionException;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import at.fhv.jfe7727.ba.model.Movie;
import at.fhv.jfe7727.ba.model.Person;
import at.fhv.jfe7727.ba.model.PersonName;

@ApplicationScoped
public class MovieEventProcessor {

    private static final Logger LOGGER = Logger.getLogger(MovieEventProcessor.class);

    @Incoming("movie-added")      
    public void processMovieAddedEvent(Movie movie) throws InterruptedException, ExecutionException {
        LOGGER.info("Movie Added Message recived");
        for(PersonName personName : movie.getPersonNames()){   
            Person.findById(personName).onItem().transform(person -> {
                Person personToPersist = new Person();
                personToPersist.setPersonName(personName);
                if(person != null){
                    personToPersist= (Person) person;
                    personToPersist.addMovie(movie);
                }                
                personToPersist.addMovie(movie);
                return personToPersist.persistOrUpdate().subscribeAsCompletionStage();
            }).subscribe().asCompletionStage();
        }
        LOGGER.info("Movie Added Message processed");
    }

    @Incoming("movie-deleted")      
    public void processMovieDeletedEvent(Movie movie) throws InterruptedException, ExecutionException {
        LOGGER.info("Movie Deleted Message recived");
        for(PersonName personName : movie.getPersonNames()){   
            Person.findById(personName).onItem().transform(person -> {
                Person personToPersist = (Person) person;              
                personToPersist.removeMovie(movie);
                return personToPersist.update().subscribeAsCompletionStage();
            }).subscribe().asCompletionStage();
        }
        LOGGER.info("Movie Deleted Message processed");
    }
    
}
