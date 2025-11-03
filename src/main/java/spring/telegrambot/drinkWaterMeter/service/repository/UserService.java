package spring.telegrambot.drinkWaterMeter.service.repository;

import org.springframework.stereotype.Service;
import spring.telegrambot.drinkWaterMeter.data.model.user.User;
import spring.telegrambot.drinkWaterMeter.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public User findByUser(String chatId){
        return userRepository.findByChatId(chatId);
    }

    public User findOrCreate(String chatId, String username){
        User user = userRepository.findByChatId(chatId);
        if(user==null){
            user = createUser(chatId,username);
        }
        return user;
    }

    public User createUser(User user){
        return userRepository.save(user);
    }
    public User createUser(String chatId, String username){
        User user = new User(
                null,
                chatId,
                username,
                0,
                new ArrayList<>()
        );
        System.out.println("user сохраняется: " + user);
       return createUser(user);
    }



}
