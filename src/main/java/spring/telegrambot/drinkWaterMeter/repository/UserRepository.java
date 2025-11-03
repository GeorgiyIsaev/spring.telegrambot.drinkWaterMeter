package spring.telegrambot.drinkWaterMeter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.telegrambot.drinkWaterMeter.data.model.user.User;

public interface UserRepository extends JpaRepository<User, Integer>{
}



