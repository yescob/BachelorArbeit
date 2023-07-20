package at.fhv.jfe7727.ba.service;


import fish.payara.cloud.connectors.kafka.api.KafkaListener;
import fish.payara.cloud.connectors.kafka.api.OnRecord;

import java.util.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.json.bind.JsonbBuilder;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import at.fhv.jfe7727.ba.database.PersonRepository;
import at.fhv.jfe7727.ba.model.Movie;
import at.fhv.jfe7727.ba.model.Person;
import at.fhv.jfe7727.ba.model.PersonName;

@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "clientId", propertyValue = "PayaraMicroListener"),
    @ActivationConfigProperty(propertyName = "groupIdConfig", propertyValue = "PayaraMicro"),
    @ActivationConfigProperty(propertyName = "topics", propertyValue = "movie-added,movie-deleted"),
    @ActivationConfigProperty(propertyName = "bootstrapServersConfig", propertyValue = "kafka:9092"),    
    @ActivationConfigProperty(propertyName = "enableAutoCommit", propertyValue = "true"),    
    @ActivationConfigProperty(propertyName = "autoCommitInterval", propertyValue = "100"),    
    @ActivationConfigProperty(propertyName = "retryBackoff", propertyValue = "1000"),    
    @ActivationConfigProperty(propertyName = "keyDeserializer", propertyValue = "org.apache.kafka.common.serialization.StringDeserializer"),    
    @ActivationConfigProperty(propertyName = "valueDeserializer", propertyValue = "org.apache.kafka.common.serialization.StringDeserializer"),    
    @ActivationConfigProperty(propertyName = "pollInterval", propertyValue = "3000"), 
})
public class KafkaMessageListener implements KafkaListener {
     
    @Inject
    PersonRepository personRepository;

    private static final Logger LOGGER = Logger.getLogger(KafkaMessageListener.class.getName());

    @OnRecord( topics={"movie-added"})
    public void processMovieAddedEvent(ConsumerRecord record) {
        LOGGER.info("Movie Added Message recived");
        Movie movie = JsonbBuilder.create().fromJson(record.value().toString(), Movie.class);
        for(PersonName personName : movie.getPersonNames()){   
            personRepository.getByName(personName).onItem().transform(person -> {
                Person personToPersist = new Person();
                personToPersist.setPersonName(personName);
                if(person != null){
                    personToPersist= (Person) person;
                    personToPersist.addMovie(movie);
                    return personRepository.update(personToPersist).subscribe().asCompletionStage();
                }                
                personToPersist.addMovie(movie);
                return personRepository.add(personToPersist).subscribe().asCompletionStage();
            }).subscribe().asCompletionStage();
        }
        LOGGER.info("Movie added message processed");
    }

    @OnRecord( topics={"movie-deleted"})
    public void processMovieDeltedEvent(ConsumerRecord record) {
        LOGGER.info("Movie Deleted Message recived");
        Movie movie = JsonbBuilder.create().fromJson(record.value().toString(), Movie.class);
        for(PersonName personName : movie.getPersonNames()){   
            personRepository.getByName(personName).onItem().transform(person -> {
                Person personToPersist = (Person) person;
                System.out.println(personToPersist.getMovies().size());              
                personToPersist.removeMovie(movie);
                System.out.println(personToPersist.getMovies().size());  
                return personRepository.update(personToPersist).subscribeAsCompletionStage();
            }).subscribe().asCompletionStage();
        }
        LOGGER.info("Movie deleted message processed");
    }
}


