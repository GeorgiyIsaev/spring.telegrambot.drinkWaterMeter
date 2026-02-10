package spring.telegrambot.drinkWaterMeter.repository.dao;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Repository;

import spring.telegrambot.drinkWaterMeter.repository.model.user.User;

import spring.telegrambot.drinkWaterMeter.repository.jpa.user.DrinkWaterRepository;
import spring.telegrambot.drinkWaterMeter.repository.jpa.user.UserRepository;
import spring.telegrambot.drinkWaterMeter.repository.model.user.WaterDrink;

import java.time.Instant;
import java.util.List;

@Repository
public class UserDAO {
    private final UserRepository userRepository;
    private final DrinkWaterRepository drinkWaterRepository;

    public UserDAO(UserRepository userRepository, DrinkWaterRepository drinkWaterRepository) {
        this.userRepository = userRepository;
        this.drinkWaterRepository = drinkWaterRepository;
    }

    //СОЗДАНИЕ
    public List<User> findAll(){
        return userRepository.findAll();
    }
    public User createUser(String chatId, String username) {
        User user = new User();
        user.setChatId(chatId);
        user.setUsername(username);
        user.setTimeShift(0);
        return save(user);
    }
    public User findOrCreate(String chatId, String username){
        User user = userRepository.findByUsername(username);
        if(user==null){
            user = createUser(chatId, username);
        }
        return user;
    }

    //Изменение и добавление данных
    public User save(User user){
        if(user == null) throw new NullPointerException();
        return userRepository.save(user);
    }

    @Transactional
    public WaterDrink addWaterDrink(@NotNull User user, Integer ml, Instant time) {
        if(user == null) throw new NullPointerException();
        WaterDrink waterDrink = new WaterDrink();
        waterDrink.setTime(time);
        waterDrink.setCountWaterMl(ml);
        waterDrink.setUser(user);
        return drinkWaterRepository.save(waterDrink);
    }

    @Transactional
    public void delete(User user) {
        if(user.getWaterDunks() != null) {
            for (WaterDrink waterDrink : user.getWaterDunks()) {
                drinkWaterRepository.delete(waterDrink);
            }
        }
        userRepository.delete(user);
    }
}
