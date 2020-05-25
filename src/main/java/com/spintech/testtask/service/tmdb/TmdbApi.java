package com.spintech.testtask.service.tmdb;

import com.spintech.testtask.pojo.Cast;

import java.util.List;

public interface TmdbApi {
    String popularTVShows();
    List<Cast> getShowsByActor(String actorId);
    boolean isActorExist(String actorId);
}
