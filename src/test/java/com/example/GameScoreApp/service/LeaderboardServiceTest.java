package com.example.GameScoreApp.service;

import com.example.GameScoreApp.model.GameScore;
import com.example.GameScoreApp.repository.RedisRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class LeaderboardServiceTest {
    @Mock
    private RedisRepository redisRepository;
    @InjectMocks
    private LeaderboardService leaderboardService;
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLeaderboardService(){
        GameScore gameScore1 = new GameScore().setGameScore(500).setUserId("uid1");
        GameScore gameScore2 = new GameScore().setGameScore(400).setUserId("uid2");
        GameScore gameScore3 = new GameScore().setGameScore(300).setUserId("uid3");
        GameScore gameScore4 = new GameScore().setGameScore(200).setUserId("uid4");
        GameScore gameScore5 = new GameScore().setGameScore(100).setUserId("uid5");
        List<GameScore> gameScores = Arrays.asList(gameScore1, gameScore2, gameScore3, gameScore4, gameScore5);

        when(redisRepository.getTopScores()).thenReturn(gameScores);

        List<GameScore> actual = leaderboardService.getTopScores();

        assertEquals(5, actual.size());
        IntStream.range(0, actual.size())
                .forEach(index -> {
                        GameScore expectedGameScore = gameScores.get(index);
                        GameScore actualGameScore = actual.get(index);
                        assertEquals(expectedGameScore.getGameScore(), actualGameScore.getGameScore());
                        assertEquals(expectedGameScore.getUserId(), actualGameScore.getUserId());
                    }
                );
        verify(redisRepository, times(1)).getTopScores();
    }

}
