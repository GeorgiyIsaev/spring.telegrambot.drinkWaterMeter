package spring.telegrambot.drinkWaterMeter.service.actions;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import spring.telegrambot.drinkWaterMeter.repository.dao.UserDAO;
import spring.telegrambot.drinkWaterMeter.repository.model.user.User;
import spring.telegrambot.drinkWaterMeter.repository.model.user.WaterDrink;
import spring.telegrambot.drinkWaterMeter.service.update.Message;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Hello implements Action {

    private final UserDAO userDao;
    public Hello(UserDAO userDao) {
        this.userDao = userDao;
    }

    @Override
    public SendMessage generateRequest(Message message) {
        String chatId = message.getChatId();
        String username = message.getUsername();
        User user = userDao.findOrCreate(chatId, username);

        String text =user.getUsername() + " добро пожаловать в чат id = " + user.getChatId() + ".\n";
        text +=  height(user) +  "\n";
        text +=  weight(user) +  "\n";
        text +=  sex(user) +  "\n";
        text +=  timeZone(message, user) +  "\n";
      //  text +=  "\n" + lastDayInfo(user);
        return SendMessage.builder().chatId(chatId).text(text).build();
    }


    public String weight(User user){
        if(user.getWeight()==null){
            return "Вы не указали свой вес! /weight";
        }
        else {
            return "Вес: " + user.getWeight() + " кг. /weight";
        }
    }

    public String height(User user){
        if(user.getHeight()==null){
            return "Вы не указали свой рост! /height";
        }
        else {
            return "Рост: " + user.getHeight() + " см. /height";
        }
    }


    public String sex(User user){
        if(user.getSex()== null){
            return "Вы не указали свой пол! /sex";
        }
        else {
            String sex = user.getSex() ? "женский" : "мужской";
            return  "Пол: " + sex + ". /sex";
        }
    }

    private String timeZone(Message message, User user) {
        String time = "";
        if(user.getTimeShift() == null){
            user.setTimeShift(0);
        }
        if(user.getTimeShift() >=0){
            time = "+";
        }
        time += user.getTimeShift();

        return "Сейчас: !" + message.getTime() +  " - (GMT " + time + ") /time";
    }






}
