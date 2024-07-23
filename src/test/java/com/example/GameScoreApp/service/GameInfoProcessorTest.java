package com.example.GameScoreApp.service;

import com.example.GameScoreApp.model.GameInfo;
import com.example.GameScoreApp.repository.RedisRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class GameInfoProcessorTest {
    @Mock
    private RedisRepository redisRepository;

    @InjectMocks
    private GameInfoProcessorService gameInfoProcessorService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGameInfoProcessor(){
        GameInfo gameInfo = new GameInfo().setGameId("game1").setUserId("user1").setGameScore(100).setGameTime(System.currentTimeMillis());

        gameInfoProcessorService.listen(gameInfo);

        // Verify that addScore method of redisRepository is called once with any GameInfo object
        verify(redisRepository, times(1)).addScore(any(GameInfo.class));
    }
}
