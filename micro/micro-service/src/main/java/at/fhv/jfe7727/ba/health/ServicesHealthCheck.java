package at.fhv.jfe7727.ba.health;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Readiness;
import org.eclipse.microprofile.health.HealthCheckResponse.Status;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import at.fhv.jfe7727.ba.restclient.MovieRestClient;

@Readiness
@ApplicationScoped
public class ServicesHealthCheck implements HealthCheck {

    @Inject
    @RestClient
    MovieRestClient movieRestClient;
    
    @Override
    public HealthCheckResponse call() {

        HealthCheckResponseBuilder healthCheckResponseBuilder = HealthCheckResponse.named("Movie Microservice connection health check");
        Status movieServiceStatus = Status.DOWN;

        try {
            getHealth();
            healthCheckResponseBuilder.up();
            movieServiceStatus = Status.UP;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            healthCheckResponseBuilder.down();
        }
        healthCheckResponseBuilder.withData("Movie-Service", movieServiceStatus.toString());
        return healthCheckResponseBuilder.build();  
      
    }

    @CircuitBreaker(successThreshold = 5, requestVolumeThreshold = 2, failureRatio=0.5, delay = 1000)
    private void getHealth(){
        movieRestClient.getHealResponse();
    }

}
