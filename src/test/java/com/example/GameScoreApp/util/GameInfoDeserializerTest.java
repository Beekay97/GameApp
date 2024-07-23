package com.example.GameScoreApp.util;

import com.example.GameScoreApp.model.GameInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class GameInfoDeserializerTest {
    private Deserializer<GameInfo> deserializer;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp(){
        deserializer = new GameInfoDeserializer();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGameInfoDeserializerSuccess() throws Exception {
        GameInfo gameInfo = new GameInfo().setGameId("gid1").setUserId("uid1").setGameScore(100).setGameTime(123456);

        byte[] data = objectMapper.writeValueAsBytes(gameInfo);

        GameInfo deserializedGameInfo = deserializer.deserialize("game-info", data);
        assertNotNull(deserializedGameInfo);
        assertEquals(gameInfo.getGameId(), deserializedGameInfo.getGameId());
        assertEquals(gameInfo.getUserId(), deserializedGameInfo.getUserId());
        assertEquals(gameInfo.getGameScore(), deserializedGameInfo.getGameScore());
        assertEquals(gameInfo.getGameTime(), deserializedGameInfo.getGameTime());
    }
    @Test
    public void testGameInfoDeserializerFailure() throws Exception {
        String gameInfo =" new GameInfo().setGameId(\"gid1\").setUserId(\"uid1\").setGameScore(100).setGameTime(123456);";

        byte[] data = objectMapper.writeValueAsBytes(gameInfo);

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> {deserializer.deserialize("game-info",data);});

        assertEquals("Error deserializing message", runtimeException.getMessage());
    }
}
