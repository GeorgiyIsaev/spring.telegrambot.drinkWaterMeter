package spring.telegrambot.drinkWaterMeter.repository.dao;

import spring.telegrambot.drinkWaterMeter.repository.jpa.log.LogRepository;
import spring.telegrambot.drinkWaterMeter.repository.model.log.Logger;

import java.util.List;

public class LogDAO {
    private final LogRepository logRepository;

    public LogDAO(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public List<Logger> getFull(){
        return logRepository.findAll();
    }

    public Logger save(Logger logger){
        return logRepository.save(logger);
    }
}
