package spring.telegrambot.drinkWaterMeter.service.actions;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import spring.telegrambot.drinkWaterMeter.repository.model.user.User;
import spring.telegrambot.drinkWaterMeter.repository.model.user.WaterDrink;
import spring.telegrambot.drinkWaterMeter.repository.dao.UserDAO;
import spring.telegrambot.drinkWaterMeter.service.update.MessageContract;
import spring.telegrambot.drinkWaterMeter.service.utils.CalendarDrink;

import java.time.*;
import java.util.List;

public class Full implements Action {

    private final UserDAO userDao;
    public Full(UserDAO userDao) {
        this.userDao = userDao;
    }

    @Override
    public SendMessage generateRequest(MessageContract messageContract) {
        String chatId = messageContract.getChatId();
        String username = messageContract.getUsername();

        User user = userDao.findOrCreate(chatId, username);
        String text ="Все записи " + user.getUsername() + " !\n";
        text +=  "\n" + lastDayInfo(user);
        return SendMessage.builder().chatId(chatId).text(text).build();
    }

    private String lastDayInfo(User user) {
        List<List<WaterDrink>> days = CalendarDrink.make(user);
        String text = "";
        for (List<WaterDrink> day : days){
            if(!day.isEmpty()) {
                LocalDate date = LocalDate.ofInstant(day.getFirst().getTime(), ZoneOffset.UTC);
                text += "День: " + date + "\n";
            }
            for (WaterDrink drink : day){
                LocalTime time = LocalTime.ofInstant(drink.getTime(), ZoneOffset.UTC);
                int hour = time.getHour();
                int minute = time.getMinute();
                text += "Выпито: " + drink.getCountWaterMl() + " мл в " +  hour + ":" + minute + ";\n";

            }
        }
        return text;
    }
}

