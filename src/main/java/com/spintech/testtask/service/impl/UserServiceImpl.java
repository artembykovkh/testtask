package com.spintech.testtask.service.impl;

import com.spintech.testtask.entity.User;
import com.spintech.testtask.pojo.Cast;
import com.spintech.testtask.repository.UserRepository;
import com.spintech.testtask.service.UserService;
import com.spintech.testtask.service.tmdb.TmdbApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TmdbApi tmdbApi;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public User registerUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (Objects.nonNull(user)) {
            return null;
        }
        user = User.builder().email(email).password(passwordEncoder.encode(password)).build();
        return userRepository.save(user);
    }

    @Override
    public User findUser(String email, String password) {
        User foundUser = userRepository.findByEmail(email);
        if (Objects.nonNull(foundUser)) {
            if (passwordEncoder.matches(password, foundUser.getPassword())) {
                return foundUser;
            }
        }
        return null;
    }

    @Override
    public User addFavoriteActor(User user, String actorId) {
        if (!tmdbApi.isActorExist(actorId)) {
            return null;
        }

        final Set<String> favoriteActors = user.getFavoriteActors();
        favoriteActors.add(actorId);
        return userRepository.save(user);
    }

    @Override
    public User removeFavoriteActor(User user, String actorId) {
        if (!tmdbApi.isActorExist(actorId)) {
            return null;
        }

        final Set<String> favoriteActors = user.getFavoriteActors();
        favoriteActors.remove(actorId);
        return userRepository.save(user);
    }

    @Override
    public User markTvShow(User user, String showId) {
        final Set<String> watchedShows = user.getWatchedShows();
        watchedShows.add(showId);
        return userRepository.save(user);
    }

    @Override
    public User unMarkTvShow(User user, String showId) {
        final Set<String> watchedShows = user.getWatchedShows();
        watchedShows.remove(showId);
        return userRepository.save(user);
    }

    @Override
    public List<Cast> getUnwatchedTvShowsWithFavoriteActors(User currentUser, String actorId) {
        final Set<String> shows = currentUser.getWatchedShows();
        return tmdbApi.getShowsByActor(actorId)
                .stream()
                .filter(show -> !shows.contains(show.getId()))
                .collect(Collectors.toList());
    }
}