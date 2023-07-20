package at.fhv.jfe7727.ba.restclient;

import java.util.Arrays;
import java.util.HashSet;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory;

import io.smallrye.jwt.build.Jwt;


@ApplicationScoped
public class AuthHeaderFactory implements ClientHeadersFactory {

    @Override
    public MultivaluedMap<String, String> update(MultivaluedMap<String, String> incomingHeaders,
            MultivaluedMap<String, String> clientOutgoingHeaders) {
                
        MultivaluedMap<String, String> result = new MultivaluedHashMap<>();
        result.add("Authorization", "Bearer " + tokenGeneration());
        return result;
    }

    
    
    public String tokenGeneration(){
        return Jwt.issuer("Jakob") 
          .upn("Jakob") 
          .groups(new HashSet<>(Arrays.asList("User", "Admin")))
        .sign();
    }
    
}
