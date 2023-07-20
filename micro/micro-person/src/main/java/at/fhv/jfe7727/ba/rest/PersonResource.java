package at.fhv.jfe7727.ba.rest;

import java.util.List;
import java.util.concurrent.CompletionStage;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import at.fhv.jfe7727.ba.database.PersonRepository;
import at.fhv.jfe7727.ba.model.Person;
import at.fhv.jfe7727.ba.model.PersonName;

@Path("/person")
@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PersonResource {

    @Inject
    PersonRepository personRepository;

    @GET
    @PermitAll
    public CompletionStage<List<Person>> get() {
        return personRepository.listAll().subscribeAsCompletionStage();
    }

    @DELETE
    @RolesAllowed("Admin")
    public CompletionStage<Long> deleteAll(){
        return personRepository.deleteAll().subscribeAsCompletionStage();
    }

    @GET
    @PermitAll
    @Path("{fname}/{lname}")
    public CompletionStage<Person> getByPerson(@PathParam("fname") String fname, @PathParam("lname") String lname){
        PersonName personName = new PersonName();
        personName.setFirstName(fname);
        personName.setLastName(lname);
        return personRepository.getByName(personName).subscribeAsCompletionStage();
    }

}
