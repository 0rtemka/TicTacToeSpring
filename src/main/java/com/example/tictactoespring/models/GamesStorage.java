package com.example.tictactoespring.models;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Component
public class GamesStorage implements Serializable {
    private Map<String, Game> games;

    private static GamesStorage instance;

    private GamesStorage() {
        games = new HashMap<>();
    }

    public static synchronized GamesStorage getInstance() {
        if (instance == null) {
            instance = new GamesStorage();
        }

        return instance;
    }

    public Map<String, Game> getGames() {
        return games;
    }

    public void setGame(Game game) {
        games.put(game.getGameId(), game);
    }
}
