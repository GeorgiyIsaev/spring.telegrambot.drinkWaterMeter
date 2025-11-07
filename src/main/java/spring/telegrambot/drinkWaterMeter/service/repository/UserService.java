package spring.telegrambot.drinkWaterMeter.service.repository;

import org.springframework.stereotype.Service;
import spring.telegrambot.drinkWaterMeter.data.model.user.DayDrinks;
import spring.telegrambot.drinkWaterMeter.data.model.user.User;
import spring.telegrambot.drinkWaterMeter.data.model.user.WaterDrink;
import spring.telegrambot.drinkWaterMeter.repository.DrinkDayRepository;
import spring.telegrambot.drinkWaterMeter.repository.DrinkWaterRepository;
import spring.telegrambot.drinkWaterMeter.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final DrinkDayRepository drinkDayRepository;
    private final DrinkWaterRepository drinkWaterRepository;

    public UserService(UserRepository userRepository, DrinkDayRepository drinkDayRepository, DrinkWaterRepository drinkWaterRepository) {
        this.userRepository = userRepository;
        this.drinkDayRepository = drinkDayRepository;
        this.drinkWaterRepository = drinkWaterRepository;
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }


    public User findOrCreate(String chatId, String username){
        User user = userRepository.findByUsername(username);
        if(user==null){
            user = createUser(chatId,username);
        }
        return user;
    }

    public User save(User user){
        return userRepository.save(user);
    }
    public DayDrinks saveNow(User user){
        DayDrinks dayDrinks = new DayDrinks();
        dayDrinks.setDate(LocalDate.now());
        dayDrinks.setUserInfo(user);
        return drinkDayRepository.save(dayDrinks);
    }

    public WaterDrink addToDay(DayDrinks dayDrinks, Integer ml){
        WaterDrink waterDrink = new WaterDrink();
        waterDrink.setTime(LocalDateTime.now());
        waterDrink.setCountWaterMl(ml);
        waterDrink.setDayDrink(dayDrinks);
        return drinkWaterRepository.save(waterDrink);
    }
    public WaterDrink addToDay(DayDrinks dayDrinks, Integer ml, LocalDateTime localDateTime){
        WaterDrink waterDrink = new WaterDrink();
        waterDrink.setTime(localDateTime);
        waterDrink.setCountWaterMl(ml);
        waterDrink.setDayDrink(dayDrinks);
        return drinkWaterRepository.save(waterDrink);
    }


    public User createUser(String chatId, String username) {
        User user = new User(
                null,
                chatId,
                username,
                0,
                new ArrayList<>()
        );
        return save(user);
    }

    public void delete(User user){
        for(DayDrinks dayDrinks : user.getCalendarWaterDrunk()){
            for (WaterDrink waterDrink : dayDrinks.getWaterDunks()){
                drinkWaterRepository.delete(waterDrink);
            }
            drinkDayRepository.delete(dayDrinks);
        }
        userRepository.delete(user);
    }

}
