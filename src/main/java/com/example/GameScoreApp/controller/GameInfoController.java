package com.example.GameScoreApp.controller;

import com.example.GameScoreApp.model.GameScore;
import com.example.GameScoreApp.service.LeaderboardService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Log4j2
@RestController
@RequestMapping(value = "gameservice", produces = MediaType.APPLICATION_JSON_VALUE)
public class GameInfoController {

    @Autowired
    private LeaderboardService leaderboardService;

    @GetMapping("/topscores")

    public ResponseEntity<List<GameScore>> getTopScores() {
        log.info("Compiling and returning the top 5 game scores");
        return new ResponseEntity<>(leaderboardService.getTopScores(), HttpStatus.OK );
    }
}
