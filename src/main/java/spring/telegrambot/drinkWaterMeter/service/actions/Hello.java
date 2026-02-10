package spring.telegrambot.drinkWaterMeter.service.actions;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import spring.telegrambot.drinkWaterMeter.repository.dao.UserDAO;
import spring.telegrambot.drinkWaterMeter.repository.model.user.User;
import spring.telegrambot.drinkWaterMeter.repository.model.user.WaterDrink;
import spring.telegrambot.drinkWaterMeter.service.update.MessageContract;
import spring.telegrambot.drinkWaterMeter.service.utils.CalendarDrink;

import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;

public class Hello implements Action {

    private final UserDAO userDao;
    public Hello(UserDAO userDao) {
        this.userDao = userDao;
    }

    @Override
    public SendMessage generateRequest(MessageContract messageContract) {
        String chatId = messageContract.getChatId();
        String username = messageContract.getUsername();
        User user = userDao.findOrCreate(chatId, username);

        String text =user.getUsername() + " добро пожаловать в чат id = " + user.getChatId() + ".\n";
        text +=  height(user) +  "\n";
        text +=  weight(user) +  "\n";
        text +=  sex(user) +  "\n";
        text +=  timeZone(messageContract, user) +  "\n";
        text +=  "\n" + lastDayInfo(user);
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

    private String timeZone(MessageContract messageContract, User user) {
        String time = "";
        if(user.getTimeShift() == null){
            user.setTimeShift(0);
        }
        if(user.getTimeShift() >=0){
            time = "+";
        }
        time += user.getTimeShift();

        return "Сейчас: !" + messageContract.getTime() +  " - (UTC " + time + ") /time";
    }


    private String lastDayInfo(User user) {
        List<List<WaterDrink>> days = CalendarDrink.make(user);
        if(days.isEmpty()){
            return "Список пуст!";
        }
        String text = "";
        int countAllDrink = 0;

        int timeShift = user.getTimeShift();
        for (WaterDrink drink : days.getLast()){
            LocalTime time = LocalTime.ofInstant(drink.getTime(), ZoneOffset.UTC);
            int hour = time.getHour() + timeShift;
            int minute = time.getMinute();
            text += "Выпито: " + drink.getCountWaterMl() + " мл в " +  hour + ":" + minute + ";\n";
            countAllDrink +=drink.getCountWaterMl();
        }
        text+="Всего за день выпито: " + countAllDrink + " мл воды.";
        return text;
    }




}
