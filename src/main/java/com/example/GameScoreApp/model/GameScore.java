package com.example.GameScoreApp.model;

public class GameScore {
    private String userId;
    private long gameScore;

    public String getUserId() {
        return userId;
    }

    public GameScore setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public long getGameScore() {
        return gameScore;
    }

    public GameScore setGameScore(long gameScore) {
        this.gameScore = gameScore;
        return this;
    }
}
