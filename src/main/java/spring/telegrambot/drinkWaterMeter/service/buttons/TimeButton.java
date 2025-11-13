package spring.telegrambot.drinkWaterMeter.service.buttons;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import spring.telegrambot.drinkWaterMeter.repository.dao.UserDAO;
import spring.telegrambot.drinkWaterMeter.repository.model.user.User;
import spring.telegrambot.drinkWaterMeter.service.update.CallbackQuery;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class TimeButton implements Button {
        private final UserDAO userDao;
        private final int time;

        public TimeButton(UserDAO userDao, int time) {
            this.userDao = userDao;
            this.time = time;
        }

    @Override
    public SendMessage generateRequest(CallbackQuery callbackQuery) {
        String chatId = callbackQuery.getChatId();
        String username = callbackQuery.getUsername();


        User user = userDao.findOrCreate(chatId, username);
        User userUpdate = updateUser(user);
        String text = headerMessage();
        text += "Сейчас: " + getTime(callbackQuery, userUpdate);
        return SendMessage.builder().chatId(chatId).text(text).build();
    }

    public String headerMessage(){
        String text = "Часовой пояс установлен: \"";
        text += time>=0 ? "+" : "";
        text += time + ":00\"\n";
        return text;
    }

    public String getTime(CallbackQuery callbackQuery, User userUpdate){
            Instant instant = callbackQuery.getTime();
            System.out.println(instant);
        LocalTime localTime = LocalTime.from(callbackQuery.getTime().atZone(ZoneOffset.UTC));
        System.out.println(localTime);
        String text = "\"" + (localTime.getHour() + userUpdate.getTimeShift());
        text += ":" + localTime.getMinute() + "\"";
        return text;
    }

    private User updateUser(User user) {
        user.setTimeShift(time);
        return userDao.save(user);
    }
}