package at.fhv.jfe7727.ba.database;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.mongodb.reactivestreams.client.MongoCollection;
import static com.mongodb.client.model.Filters.eq;

import at.fhv.jfe7727.ba.model.Person;
import at.fhv.jfe7727.ba.model.PersonName;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

@ApplicationScoped
public class PersonRepository {

    @Inject
    DatabaseConnector databaseConnector;

    public Uni<List<Person>> listAll() {
        return Multi.createFrom().publisher(getCollection().find()).collect().asList();
    }

    public Uni<Person> getByName(PersonName personName){
        return Uni.createFrom().publisher(getCollection().find(eq("personName", personName)).first());
    }

    public Uni<Long> deleteAll(){
        return Uni.createFrom().publisher(getCollection().countDocuments())
        .onItem().call(
            i -> Uni.createFrom().publisher(getCollection().drop())
        );
    }

    public Uni<Void> update(Person person) {
        return Uni.createFrom().publisher(getCollection().replaceOne(eq("personName", person.getPersonName()), person)).replaceWithVoid();
    }

    public Uni<Void> add(Person person) {
        return Uni.createFrom().publisher(getCollection().insertOne(person)).replaceWithVoid();
    }

    private MongoCollection<Person> getCollection(){
        return databaseConnector.getDatabase().getCollection("person", Person.class);
    }

}
