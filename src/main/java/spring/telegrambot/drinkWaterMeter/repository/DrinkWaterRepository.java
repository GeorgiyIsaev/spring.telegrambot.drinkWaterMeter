package spring.telegrambot.drinkWaterMeter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.telegrambot.drinkWaterMeter.repository.model.user.WaterDrink;

public interface DrinkWaterRepository extends JpaRepository<WaterDrink, Integer> {
}
