package spring.telegrambot.drinkWaterMeter.repository.jpa.log;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.telegrambot.drinkWaterMeter.repository.model.log.Log;

public interface LogRepository extends JpaRepository<Log, Integer> {
}
