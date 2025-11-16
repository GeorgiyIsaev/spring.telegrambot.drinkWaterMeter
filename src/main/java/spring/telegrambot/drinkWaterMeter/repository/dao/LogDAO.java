package spring.telegrambot.drinkWaterMeter.repository.dao;

import org.springframework.stereotype.Repository;
import spring.telegrambot.drinkWaterMeter.repository.jpa.log.LogRepository;
import spring.telegrambot.drinkWaterMeter.repository.model.log.Log;

import java.time.Instant;
import java.util.List;

@Repository
public class LogDAO {
    private final LogRepository logRepository;

    public LogDAO(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public List<Log> getFull(){
        return logRepository.findAll();
    }

    public Log save(Log log){
        return logRepository.save(log);
    }


    public Log save(Instant time, String event, String information){
        Log log = new Log();
        log.setTime(time);
        log.setEvent(event);
        log.setInformation(information);
        return this.save(log);
    }
    public Log save(String event, String information){
        Log log = new Log();
        log.setTime(Instant.now());
        log.setEvent(event);
        log.setInformation(information);
        return this.save(log);
    }
}
