package com.example.GameScoreApp.repository;

import com.example.GameScoreApp.model.GameInfo;
import com.example.GameScoreApp.model.GameScore;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Repository
public class RedisRepository {
    private static final String TOP_SCORES_KEY = "top_scores";

    private RedisTemplate<String, String> redisTemplate;

    @Value("${service.leaderboardK:5}")
    private int leaderboardK;

    @Autowired
    public RedisRepository(RedisTemplate<String, String> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    public void addScore(GameInfo gameInfo){
        String member = gameInfo.getGameId()+":"+gameInfo.getUserId();
        log.info("Using composite score which is a combination of game score and timestamp, for redis sorted set");
        double compositeScore = gameInfo.getGameScore() + (1 - (gameInfo.getGameTime() / 1e18));
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        zSetOps.add(TOP_SCORES_KEY, member, compositeScore);
        if(zSetOps.size(TOP_SCORES_KEY) > leaderboardK ){
            log.info("Restricting the redis database to store only the top 5 scores");
            zSetOps.removeRange(TOP_SCORES_KEY, 0, 0);
        }
    }

    public List<GameScore> getTopScores() {
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        log.info("Fetching top 5 scores from Redis and compiling it into a list of Game Scores");
        return zSetOps.reverseRangeWithScores(TOP_SCORES_KEY, 0 , leaderboardK - 1)
                .stream()
                .map(tuple -> {
                    String userId = tuple.getValue().split(":")[1];
                    long score = (int) Math.floor(tuple.getScore());
                    return new GameScore().setGameScore(score).setUserId(userId);
                })
                .collect(Collectors.toList());
    }
}
