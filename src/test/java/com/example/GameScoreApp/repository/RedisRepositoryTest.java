package com.example.GameScoreApp.repository;

import com.example.GameScoreApp.model.GameInfo;
import com.example.GameScoreApp.model.GameScore;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import redis.embedded.RedisServer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Log4j2
@SpringBootTest
public class RedisRepositoryTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RedisRepository redisRepository;

    private RedisServer redisServer;

    @BeforeEach
    public void setUp() throws IOException {
        redisServer = new RedisServer(6379);
        redisServer.start();
    }

    @AfterEach
    public void tearDown(){
        redisServer.stop();
    }

    @Test
    public void testAddScores() {
        List<GameInfo> list = new ArrayList<>();
        list.add(new GameInfo().setGameId("gid6").setUserId("uid6").setGameScore(600).setGameTime(System.currentTimeMillis()));
        list.add(new GameInfo().setGameId("gid5").setUserId("uid5").setGameScore(500).setGameTime(System.currentTimeMillis() + 5000));
        list.add(new GameInfo().setGameId("gid4").setUserId("uid4").setGameScore(400).setGameTime(System.currentTimeMillis()));
        list.add(new GameInfo().setGameId("gid3").setUserId("uid3").setGameScore(300).setGameTime(System.currentTimeMillis() + 5000));
        list.add(new GameInfo().setGameId("gid2").setUserId("uid2").setGameScore(200).setGameTime(System.currentTimeMillis()));
        list.add(new GameInfo().setGameId("gid1").setUserId("uid1").setGameScore(100).setGameTime(System.currentTimeMillis() + 5000));
        list.stream().forEach(gameInfo -> redisRepository.addScore(gameInfo));

        Set<ZSetOperations.TypedTuple<String>> scores = redisTemplate.opsForZSet().reverseRangeWithScores("top_scores" , 0, -1);
        assertThat(scores).hasSize(5);
        Iterator<GameInfo> listIterator = list.iterator();
        Iterator<ZSetOperations.TypedTuple<String>> setIterator = scores.iterator();
        while(listIterator.hasNext() && setIterator.hasNext()){
            GameInfo gameInfo = listIterator.next();
            ZSetOperations.TypedTuple<String> score = setIterator.next();
            assertEquals(score.getValue().split(":")[0], gameInfo.getGameId());
            assertEquals(score.getValue().split(":")[1], gameInfo.getUserId());
            assertEquals(Math.floor(score.getScore()), gameInfo.getGameScore());
        }
    }

    @Test
    public void testGetTopFiveScores(){
        String gameId1 = "gid1";
        String gameId2 = "gid2";
        String gameId3 = "gid3";
        String gameId4 = "gid4";
        String gameId5 = "gid5";
        String gameId6 = "gid6";
        String userId1 = "uid1";
        String userId2 = "uid2";
        String userId3 = "uid3";
        String userId4 = "uid4";
        String userId5 = "uid5";
        String userId6 = "uid6";
        int score1 = 50;
        int score2 = 60;
        int score3 = 70;
        int score4 = 80;
        int score5 = 90;
        int score6 = 100;
        long timestamp1 = System.currentTimeMillis();
        long timestamp2 = System.currentTimeMillis() + 1000;
        long timestamp3 = System.currentTimeMillis() + 2000;
        long timestamp4 = System.currentTimeMillis();
        long timestamp5 = System.currentTimeMillis() + 1000;
        long timestamp6 = System.currentTimeMillis() + 2000;
        redisTemplate.opsForZSet().add("top_scores", gameId1+":"+userId1, score1 + (1 - (timestamp1 / 1e18)));
        redisTemplate.opsForZSet().add("top_scores", gameId2+":"+userId2, score2 + (1 - (timestamp2 / 1e18)));
        redisTemplate.opsForZSet().add("top_scores", gameId3+":"+userId3, score3 + (1 - (timestamp3 / 1e18)));
        redisTemplate.opsForZSet().add("top_scores", gameId4+":"+userId4, score4 + (1 - (timestamp4 / 1e18)));
        redisTemplate.opsForZSet().add("top_scores", gameId5+":"+userId5, score5 + (1 - (timestamp5 / 1e18)));
        redisTemplate.opsForZSet().add("top_scores", gameId6+":"+userId6, score6 + (1 - (timestamp6 / 1e18)));
        List<GameScore> gameScores = redisRepository.getTopScores();
        assertThat(gameScores.size()).isEqualTo(5);
        assertThat(gameScores.get(0).getUserId()).isEqualTo("uid6");
        assertThat(gameScores.get(0).getGameScore()).isEqualTo(100);
        assertThat(gameScores.get(1).getUserId()).isEqualTo("uid5");
        assertThat(gameScores.get(1).getGameScore()).isEqualTo(90);
        assertThat(gameScores.get(2).getUserId()).isEqualTo("uid4");
        assertThat(gameScores.get(2).getGameScore()).isEqualTo(80);
        assertThat(gameScores.get(3).getUserId()).isEqualTo("uid3");
        assertThat(gameScores.get(3).getGameScore()).isEqualTo(70);
        assertThat(gameScores.get(4).getUserId()).isEqualTo("uid2");
        assertThat(gameScores.get(4).getGameScore()).isEqualTo(60);
    }

    @Configuration
    static class TestConfig {

        @Bean
        public RedisConnectionFactory redisConnectionFactory() {
            return new LettuceConnectionFactory();
        }

        @Bean
        public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
            RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
            redisTemplate.setConnectionFactory(redisConnectionFactory);
            return redisTemplate;
        }

        @Bean
        public RedisRepository redisRepository(RedisTemplate<String, String> redisTemplate) {
            return new RedisRepository(redisTemplate);
        }
    }
}
