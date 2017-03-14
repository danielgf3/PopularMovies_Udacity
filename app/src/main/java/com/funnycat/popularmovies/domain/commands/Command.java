package com.funnycat.popularmovies.domain.commands;

/**
 * Created by daniel on 18/01/2017.
 */

public interface Command <T>{
    T execute();
}
