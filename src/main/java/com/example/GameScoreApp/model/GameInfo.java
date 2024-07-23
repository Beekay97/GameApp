package com.example.GameScoreApp.model;

public class GameInfo {
    private String gameId;
    private String userId;
    private long gameScore;
    private long gameTime;

    public String getGameId() {
        return gameId;
    }

    public GameInfo setGameId(String gameId) {
        this.gameId = gameId;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public GameInfo setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public long getGameScore() {
        return gameScore;
    }

    public GameInfo setGameScore(long gameScore) {
        this.gameScore = gameScore;
        return this;
    }

    public long getGameTime() {
        return gameTime;
    }

    public GameInfo setGameTime(long gameTime) {
        this.gameTime = gameTime;
        return this;
    }

    @Override
    public String toString() {
        return "GameInfo{" +
                "gameId='" + gameId + '\'' +
                ", userId='" + userId + '\'' +
                ", gameScore=" + gameScore +
                ", gameTime=" + gameTime +
                '}';
    }
}