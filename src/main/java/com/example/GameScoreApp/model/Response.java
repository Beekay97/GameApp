package com.example.GameScoreApp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Response {
    @JsonProperty(value="gameScores")
    List<GameScore> gameScores;
}
