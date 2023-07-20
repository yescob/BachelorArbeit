package at.fhv.jfe7727.ba.database;

import javax.enterprise.context.ApplicationScoped;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoDatabase;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@ApplicationScoped
public class DatabaseConnector {

    MongoClient mongoClient = null;

    public MongoDatabase getDatabase(){

        if(mongoClient == null){
            mongoClient = MongoClients.create("mongodb://mongodb-micro:27017");
        }

        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
        fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        return mongoClient.getDatabase("micro-person").withCodecRegistry(pojoCodecRegistry);
    }
    
}
