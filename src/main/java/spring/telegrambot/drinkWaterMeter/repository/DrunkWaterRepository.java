package spring.telegrambot.drinkWaterMeter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.telegrambot.drinkWaterMeter.data.model.user.WaterDrunk;

public interface DrunkWaterRepository extends JpaRepository<WaterDrunk, Integer> {
}
