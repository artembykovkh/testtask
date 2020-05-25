package com.spintech.testtask.controller;

import com.spintech.testtask.entity.User;
import com.spintech.testtask.pojo.Cast;
import com.spintech.testtask.service.UserService;
import com.spintech.testtask.service.tmdb.TmdbApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/tv")
public class TVController {
    @Autowired
    private UserService userService;

    @Autowired
    private TmdbApi tmdbApi;

    @RequestMapping(value = "/popular", method = POST)
    public ResponseEntity popular(@RequestParam String email,
                                  @RequestParam String password) {
        if (userService.findUser(email, password) == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        String popularMovies = tmdbApi.popularTVShows();

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(popularMovies);
    }

    @RequestMapping(value = "/recommended", method = GET)
    public ResponseEntity getUnwatchedTvShowsWithFavoriteActors(@RequestParam String email,
                                                                @RequestParam String password, @RequestParam String actorId) {
        final User currentUser = userService.findUser(email, password);
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        final List<Cast> recommended = userService.getUnwatchedTvShowsWithFavoriteActors(currentUser, actorId);


        if (recommended.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(recommended);
    }
}
