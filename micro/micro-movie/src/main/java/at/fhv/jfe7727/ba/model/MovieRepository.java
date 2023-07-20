package at.fhv.jfe7727.ba.model;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@ApplicationScoped
public class MovieRepository {

    @PersistenceContext(unitName = "movie")
    EntityManager entityManager;

    public List<Movie> listAll() {
        List<Movie> movies = entityManager.createQuery("SELECT m FROM Movie m", Movie.class).getResultList();
        return movies;
    }

    public Optional<Movie> findByIdOptional(Long movieId) {
        Movie movie = entityManager.find(Movie.class, movieId);
        return movie != null ? Optional.of(movie) : Optional.empty();
    }

    public List<Movie> getByTitle(String title) {
        TypedQuery<Movie> query = entityManager.createQuery("SELECT m FROM Movie m WHERE m.title =: title ", Movie.class);
        query.setParameter("title", title);
        return query.getResultList();
    }

    public void persist(Movie movie) {
        entityManager.persist(movie);
    }

    public void deleteById(Long movieId) {
        Movie movie = entityManager.find(Movie.class, movieId);
        entityManager.remove(movie);
    }
}
