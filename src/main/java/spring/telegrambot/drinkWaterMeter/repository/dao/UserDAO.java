package spring.telegrambot.drinkWaterMeter.repository.dao;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import spring.telegrambot.drinkWaterMeter.repository.model.user.User;

import spring.telegrambot.drinkWaterMeter.repository.DrinkWaterRepository;
import spring.telegrambot.drinkWaterMeter.repository.UserRepository;

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
        return userRepository.save(user);
    }

//
//    public WaterDrink addToDay(DayDrinks dayDrinks, Integer ml){
//        WaterDrink waterDrink = new WaterDrink();
//        waterDrink.setTime(LocalDateTime.now());
//        waterDrink.setCountWaterMl(ml);
//        waterDrink.setDayDrink(dayDrinks);
//        return drinkWaterRepository.save(waterDrink);
//    }
//    public WaterDrink addToDay(DayDrinks dayDrinks, Integer ml, LocalDateTime localDateTime){
//        WaterDrink waterDrink = new WaterDrink();
//        waterDrink.setTime(localDateTime);
//        waterDrink.setCountWaterMl(ml);
//        waterDrink.setDayDrink(dayDrinks);
//        return drinkWaterRepository.save(waterDrink);
//    }
//
//
//    @Transactional
//    public void delete(User user){
//        for(DayDrinks dayDrinks : user.getCalendarWaterDrunk()){
//            for (WaterDrink waterDrink : dayDrinks.getWaterDunks()){
//                drinkWaterRepository.delete(waterDrink);
//            }
//            drinkDayRepository.delete(dayDrinks);
//        }
//        userRepository.delete(user);
//    }
}
