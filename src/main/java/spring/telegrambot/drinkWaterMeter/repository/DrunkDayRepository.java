package spring.telegrambot.drinkWaterMeter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.telegrambot.drinkWaterMeter.data.model.user.WaterDrunksForDay;

public interface DrunkDayRepository extends JpaRepository<WaterDrunksForDay, Integer> {
}
