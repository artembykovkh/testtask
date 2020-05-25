package com.spintech.testtask.service;

import com.spintech.testtask.entity.User;
import com.spintech.testtask.pojo.Cast;

import java.util.List;

public interface UserService {
    User registerUser(String email, String password);
    User findUser(String email, String password);
    User addFavoriteActor(User user, String actorId);
    User removeFavoriteActor(User user, String actorId);
    User markTvShow(User user, String showId);
    User unMarkTvShow(User user, String showId);
    List<Cast> getUnwatchedTvShowsWithFavoriteActors(User currentUser, String actorId);
}

