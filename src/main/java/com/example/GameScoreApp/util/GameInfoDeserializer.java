package com.example.GameScoreApp.util;

import com.example.GameScoreApp.model.GameInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.common.serialization.Deserializer;
import java.io.IOException;

@Log4j2
public class GameInfoDeserializer implements Deserializer<GameInfo> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public GameInfo deserialize(String topic, byte[] data) {
        try{
            GameInfo gameInfo = objectMapper.readValue(data, GameInfo.class);
            log.info("Successfully deserialised kafka message into GameInfo object");
            return gameInfo;
        } catch (IOException e) {
            throw new RuntimeException("Error deserializing message", e);
        }
    }

}
