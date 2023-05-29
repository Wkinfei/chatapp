package nus.iss.chatapp.com.server.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import nus.iss.chatapp.com.server.models.TenorGif;
import nus.iss.chatapp.com.server.utils.Utils;

@Service
public class GifService {

    @Value("${api.key.tenor}")
    private String API_PUB_KEY;

    private final String URL = "https://tenor.googleapis.com/v2/search";

    public List<TenorGif> getGif(String search,Integer limit){
        String uri = UriComponentsBuilder
        .fromUriString(URL)
        .queryParam("q",search)
        .queryParam("key",API_PUB_KEY)

        .queryParam("limit",limit)
 
        .build()
        .toUriString();

        // System.out.println(uri);

        RequestEntity<Void> req = RequestEntity
                                    .get(uri)
                                    .accept(MediaType.APPLICATION_JSON)
                                    .build();

        RestTemplate template = new RestTemplate();
        ResponseEntity<String> response = null;



            response = template.exchange(req, String.class);
            System.out.println(response.getBody());
            JsonObject json = Utils.toJson(response.getBody());
            JsonArray results = json.getJsonArray("results");
            List<TenorGif> reviews = results.stream()
                                    .map(x -> (JsonObject) x)
                                    .map(x -> Utils.toGif(x))
                                    .toList();
 
            return reviews;
        
    }
   
}
