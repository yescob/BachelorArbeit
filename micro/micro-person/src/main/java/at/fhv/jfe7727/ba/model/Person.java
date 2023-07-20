package at.fhv.jfe7727.ba.model;

import java.util.HashSet;
import java.util.Set;

//import org.bson.codecs.pojo.annotations.BsonId;



public class Person {

    //@BsonId
    private PersonName personName;
    private Set<Movie> movies = new HashSet<>();

    public PersonName getPersonName() {
        return personName;
    }
    public void setPersonName(PersonName personName) {
        this.personName = personName;
    }
    public Set<Movie> getMovies() {
        return movies;
    }
    public void setMovies(Set<Movie> movies) {
        this.movies = movies;
    }
    public void addMovie(Movie movie){
        movies.add(movie);
    }
    public void removeMovie(Movie movie) {
        movies.remove(movie);
    }   

}
