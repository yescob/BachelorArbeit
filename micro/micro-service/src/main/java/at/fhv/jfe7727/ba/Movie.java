package at.fhv.jfe7727.ba;

import java.util.List;

public class Movie {

    private Long movieId;
    private String title;
    private List<PersonName> personNames;
    
    public Long getMovieId() {
        return movieId;
    }
    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public List<PersonName> getPersonNames() {
        return personNames;
    }
    public void setPersonNames(List<PersonName> personNames) {
        this.personNames = personNames;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((movieId == null) ? 0 : movieId.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Movie other = (Movie) obj;
        if (movieId == null) {
            if (other.movieId != null)
                return false;
        } else if (!movieId.equals(other.movieId))
            return false;
        return true;
    }

    

}
