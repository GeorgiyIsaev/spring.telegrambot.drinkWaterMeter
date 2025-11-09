package spring.telegrambot.drinkWaterMeter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.telegrambot.drinkWaterMeter.repository.model.user.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByChatId(String chatId);
    User findByUsername(String username);
}



