package spring.telegrambot.drinkWaterMeter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.telegrambot.drinkWaterMeter.data.model.user.DayDrinks;

public interface DrinkDayRepository extends JpaRepository<DayDrinks, Integer> {
}
