package com.example.GameScoreApp.controller;

import com.example.GameScoreApp.model.GameScore;
import com.example.GameScoreApp.service.LeaderboardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class GameInfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LeaderboardService leaderboardService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetTopFiveScores() throws Exception {
        GameScore gameScore1 = new GameScore().setGameScore(500).setUserId("uid1");
        GameScore gameScore2 = new GameScore().setGameScore(400).setUserId("uid2");
        GameScore gameScore3 = new GameScore().setGameScore(300).setUserId("uid3");
        GameScore gameScore4 = new GameScore().setGameScore(200).setUserId("uid4");
        GameScore gameScore5 = new GameScore().setGameScore(100).setUserId("uid5");
        List<GameScore> gameScores = Arrays.asList(gameScore1, gameScore2, gameScore3, gameScore4, gameScore5);

        when(leaderboardService.getTopScores()).thenReturn(gameScores);

        mockMvc.perform(get("/gameservice/topscores"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].gameScore").value(500))
                .andExpect(jsonPath("$[0].userId").value("uid1"))
                .andExpect(jsonPath("$[1].gameScore").value(400))
                .andExpect(jsonPath("$[1].userId").value("uid2"))
                .andExpect(jsonPath("$[2].gameScore").value(300))
                .andExpect(jsonPath("$[2].userId").value("uid3"))
                .andExpect(jsonPath("$[3].gameScore").value(200))
                .andExpect(jsonPath("$[3].userId").value("uid4"))
                .andExpect(jsonPath("$[4].gameScore").value(100))
                .andExpect(jsonPath("$[4].userId").value("uid5"));
    }
}
