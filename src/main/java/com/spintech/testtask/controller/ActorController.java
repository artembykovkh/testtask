package com.spintech.testtask.controller;

import com.spintech.testtask.entity.User;
import com.spintech.testtask.service.ActorService;
import com.spintech.testtask.service.UserService;
import com.spintech.testtask.service.tmdb.TmdbApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/actor")
public class ActorController {

    @Autowired
    private UserService userService;

    @Autowired
    private ActorService actorService;

    @Autowired
    private TmdbApi tmdbApi;

    @RequestMapping(value = "/favorite/add", method = POST)
    public ResponseEntity addFavorite(@RequestParam String email,
                                      @RequestParam String password, @RequestParam String actorId) {
        User currentUser = userService.findUser(email, password);
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        User user = userService.addFavoriteActor(currentUser, actorId);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(user);
    }

    @RequestMapping(value = "/favorite/delete", method = DELETE)
    public ResponseEntity deleteFavorite(@RequestParam String email,
                                         @RequestParam String password, @RequestParam String actorId) {
        User currentUser = userService.findUser(email, password);
        if (userService.findUser(email, password) == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        User user = userService.removeFavoriteActor(currentUser, actorId);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(user);
    }

}
