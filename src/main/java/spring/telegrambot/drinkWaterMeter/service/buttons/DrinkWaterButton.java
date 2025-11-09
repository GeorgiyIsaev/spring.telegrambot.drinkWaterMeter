package spring.telegrambot.drinkWaterMeter.service.buttons;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import spring.telegrambot.drinkWaterMeter.repository.dao.UserDAO;
import spring.telegrambot.drinkWaterMeter.repository.model.user.DayDrinks;
import spring.telegrambot.drinkWaterMeter.repository.model.user.User;
import spring.telegrambot.drinkWaterMeter.service.actions.Action;
import spring.telegrambot.drinkWaterMeter.service.update.CallbackQuery;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DrinkWaterButton implements Button {
    private final UserDAO userDao;
    private final int ml;

    public DrinkWaterButton(UserDAO userDao, int ml) {
        this.userDao = userDao;
        this.ml = ml;
    }

    @Override
    public SendMessage generateRequest(CallbackQuery callbackQuery) {
        String chatId = callbackQuery.getChatId();
        String username = callbackQuery.getUsername();
        String text = "Выпил " + ml + " мл воды.";
        User user = userDao.findOrCreate(chatId, username);

        drink(user, callbackQuery.getTime());
        return SendMessage.builder().chatId(chatId).text(text).build();
    }

//    @Override
//    public SendMessage generateRequest(Update update) {
//        String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
//        String username = update.getCallbackQuery().getFrom().getUserName();
//        String text = "Выпил " + ml + " мл воды.";
//        User user = userDao.findOrCreate(chatId, username);
//        LocalDateTime time = toLocalDataTime(update);
//        drink(user, time);
//        return SendMessage.builder().chatId(chatId).text(text).build();
//    }

    public void drink(User user, Instant time){
        if(user.getCalendarWaterDrunk().isEmpty()){
            createWaterDrunksForDay(user);
        }
        else if(!user.getCalendarWaterDrunk().getLast().getDate().equals(LocalDate.now())){
            createWaterDrunksForDay(user);
        }
        DayDrinks dayDrinks = user.getCalendarWaterDrunk().getLast();
        userDao.addToDay(dayDrinks, ml);
       // userDao.addToDay(dayDrinks, ml, time);
    }

    public void createWaterDrunksForDay(User user){
        DayDrinks dayDrinks = userDao.saveNow(user);
        user.getCalendarWaterDrunk().add(dayDrinks);
    }

    public LocalDateTime toLocalDataTime(Update update){
        Integer integer = update.getCallbackQuery().getMessage().getDate();
        Instant instantS = Instant.ofEpochSecond(integer);
        System.out.println("instantS: " + instantS);
        Instant instantMl = Instant.ofEpochMilli(integer);
        System.out.println("instantMl: " + instantMl);



        long long2 = update.getCallbackQuery().getMessage().getDate() * 1000;
        System.out.println("integer: " + integer);
        Date time = new Date(long2);

        System.out.println("time: " + time);
        LocalDateTime ldt = time.toInstant().atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        System.out.println("LocalDateTime: " + ldt);
        return ldt;

    }

}
