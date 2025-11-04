package spring.telegrambot.drinkWaterMeter.service.repository;

import org.springframework.stereotype.Service;
import spring.telegrambot.drinkWaterMeter.data.model.user.User;
import spring.telegrambot.drinkWaterMeter.data.model.user.WaterDrunk;
import spring.telegrambot.drinkWaterMeter.data.model.user.WaterDrunksForDay;
import spring.telegrambot.drinkWaterMeter.repository.DrunkDayRepository;
import spring.telegrambot.drinkWaterMeter.repository.DrunkWaterRepository;
import spring.telegrambot.drinkWaterMeter.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final DrunkDayRepository drunkDayRepository;
    private final DrunkWaterRepository drunkWaterRepository;

    public UserService(UserRepository userRepository, DrunkDayRepository drunkDayRepository, DrunkWaterRepository drunkWaterRepository) {
        this.userRepository = userRepository;
        this.drunkDayRepository = drunkDayRepository;
        this.drunkWaterRepository = drunkWaterRepository;
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
    public WaterDrunksForDay saveNow(User user){
        WaterDrunksForDay waterDrunksForDay = new WaterDrunksForDay();
        waterDrunksForDay.setDate(LocalDate.now());
        waterDrunksForDay.setUserInfo(user);
        return drunkDayRepository.save(waterDrunksForDay);
    }

    public WaterDrunk addToDay(WaterDrunksForDay waterDrunksForDay, Integer ml){
        WaterDrunk waterDrunk = new WaterDrunk();
        waterDrunk.setTime(LocalDateTime.now());
        waterDrunk.setCountWaterMl(ml);
        waterDrunk.setDayDrink(waterDrunksForDay);
        return drunkWaterRepository.save(waterDrunk);
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
        for(WaterDrunksForDay waterDrunksForDay : user.getCalendarWaterDrunk()){
            for (WaterDrunk waterDrunk : waterDrunksForDay.getWaterDunks()){
                drunkWaterRepository.delete(waterDrunk);
            }
            drunkDayRepository.delete(waterDrunksForDay);
        }
        userRepository.delete(user);
    }

}
