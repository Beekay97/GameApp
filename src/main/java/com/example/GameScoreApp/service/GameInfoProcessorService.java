package com.example.GameScoreApp.service;

import com.example.GameScoreApp.model.GameInfo;
import com.example.GameScoreApp.repository.RedisRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class GameInfoProcessorService {


    @Autowired
    RedisRepository redisRepository;

    @KafkaListener(id="game-info-processor", topics="${kafka.topic}")
    public void listen(GameInfo gameInfo){
        log.info("Received game info: {}", gameInfo.toString());
        try {
            redisRepository.addScore(gameInfo);
            log.info("successfully processed game info");
        }
        catch (Exception e) {
            log.error("Error processing game info " + e);
        }

    }
}
