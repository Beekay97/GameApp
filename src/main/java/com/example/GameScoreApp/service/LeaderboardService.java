package com.example.GameScoreApp.service;

import com.example.GameScoreApp.model.GameScore;
import com.example.GameScoreApp.repository.RedisRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class LeaderboardService {

    @Autowired
    private RedisRepository redisRepository;
    public List<GameScore> getTopScores(){
        List<GameScore> topFiveScores = new ArrayList<>();
        log.info("Fetching top 5 scores");
        try{
            topFiveScores.addAll(redisRepository.getTopScores());
            log.info("Successfully retrieved the top 5 scores");
        }
        catch(Exception e){
            log.error("Failed to fetch the top 5 scores");
        }
        return topFiveScores;
    }
}
