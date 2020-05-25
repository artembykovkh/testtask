package com.spintech.testtask.service.tmdb.impl;

import com.spintech.testtask.pojo.Cast;
import com.spintech.testtask.pojo.Root;
import com.spintech.testtask.service.tmdb.TmdbApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class TmdbApiImpl implements TmdbApi {
    @Value("${tmdb.apikey}")
    private String tmdbApiKey;
    @Value("${tmdb.language}")
    private String tmdbLanguage;
    @Value("${tmdb.api.base.url}")
    private String tmdbApiBaseUrl;

    public String popularTVShows() throws IllegalArgumentException {
        try {
            String url = getTmdbUrl("/tv/popular");

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response
                    = restTemplate.getForEntity(url, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                return null;
            }

            return response.getBody();
        } catch (URISyntaxException e) {
            log.error("Couldn't get popular tv shows");
        }
        return null;
    }

    @Override
    public List<Cast> getShowsByActor(String actorId) {
        try {
            if(!isActorExist(actorId)){
                return Collections.emptyList();
            }

            String url = getTmdbUrl("/person/" + actorId + "/tv_credits");

            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<Root> response =
                    restTemplate.exchange(url,
                            HttpMethod.GET, null, new ParameterizedTypeReference<Root>() {
                            });

            if (!response.getStatusCode().is2xxSuccessful()) {
                return Collections.emptyList();
            }

            Root root = response.getBody();

            if (root == null) {
                return Collections.emptyList();
            }

            return root.getCast();

        } catch (URISyntaxException e) {
            log.error("Couldn't get tv shows");
        }
        return Collections.emptyList();
    }

    @Override
    public boolean isActorExist(String actorId) {

        try {
            String url = getTmdbUrl("/person/" + actorId);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response
                    = restTemplate.getForEntity(url, String.class);

            return response.getStatusCode().is2xxSuccessful();

        } catch (URISyntaxException e) {
            log.error("Couldn't check actor");
        }
        return false;
    }

    private String getTmdbUrl(String tmdbItem) throws URISyntaxException {
        StringBuilder builder = new StringBuilder(tmdbApiBaseUrl);
        builder.append(tmdbItem);
        URIBuilder uriBuilder = new URIBuilder(builder.toString());
        uriBuilder.addParameter("language", tmdbLanguage);
        uriBuilder.addParameter("api_key", tmdbApiKey);
        return uriBuilder.build().toString();
    }
}
