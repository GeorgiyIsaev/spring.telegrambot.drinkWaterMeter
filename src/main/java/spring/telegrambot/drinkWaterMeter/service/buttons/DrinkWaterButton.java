package spring.telegrambot.drinkWaterMeter.service.buttons;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import spring.telegrambot.drinkWaterMeter.repository.dao.UserDAO;
import spring.telegrambot.drinkWaterMeter.repository.model.user.User;
import spring.telegrambot.drinkWaterMeter.repository.model.user.WaterDrink;
import spring.telegrambot.drinkWaterMeter.service.update.CallbackQueryContract;

import java.time.LocalTime;
import java.time.ZoneOffset;


public class DrinkWaterButton implements Button {
    private final UserDAO userDao;
    private final int ml;

    public DrinkWaterButton(UserDAO userDao, int ml) {
        this.userDao = userDao;
        this.ml = ml;
    }

    @Override
    public SendMessage generateRequest(CallbackQueryContract callbackQueryContract) {
        String chatId = callbackQueryContract.getChatId();
        String username = callbackQueryContract.getUsername();
        User user = userDao.findOrCreate(chatId, username);
        WaterDrink waterDrink = userDao.addWaterDrink(user,ml, callbackQueryContract.getTime());
        String text = "В " + getTime(callbackQueryContract, user) + " вы выпили " + waterDrink.getCountWaterMl() + " мл воды.";
        return SendMessage.builder().chatId(chatId).text(text).build();
    }

    public String getTime(CallbackQueryContract callbackQueryContract, User user){
        if( user.getTimeShift() == null){
            user.setTimeShift(0);
        }
        LocalTime localTime = LocalTime.from(callbackQueryContract.getTime().atZone(ZoneOffset.UTC));
        String text = "\"" + (localTime.getHour() + user.getTimeShift());
        text += ":" + localTime.getMinute() + "\"";
        text +="(UTC: ";
        text += user.getTimeShift()>=0 ? "+" : "";
        text += user.getTimeShift() + ")";
        return text;
    }
}
