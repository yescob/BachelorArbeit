package at.fhv.jfe7727.ba.rest;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


import at.fhv.jfe7727.ba.model.Person;
import at.fhv.jfe7727.ba.model.PersonName;
import io.smallrye.mutiny.Uni;

@Path("/person")
@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PersonResource {

    @GET
    public Uni<List<Person>> get() {
        return Person.listAll();
    }

    @DELETE
    @RolesAllowed("Admin")
    public Uni<Long> deleteAll(){
        return Person.deleteAll();
    }

    @GET
    @Path("{fname}/{lname}")
    public Uni<Person> getByPerson(@PathParam("fname") String fname, @PathParam("lname") String lname){
        PersonName personId = new PersonName();
        personId.setFirstName(fname);
        personId.setLastName(lname);
        return Person.findById(personId);
    }

}
