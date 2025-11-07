package spring.telegrambot.drinkWaterMeter.service.buttons;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import spring.telegrambot.drinkWaterMeter.data.model.user.DayDrinks;
import spring.telegrambot.drinkWaterMeter.data.model.user.User;
import spring.telegrambot.drinkWaterMeter.service.actions.Action;
import spring.telegrambot.drinkWaterMeter.service.repository.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DrinkWaterButton implements Action {
    private final  UserService userService;
    private final int ml;

    public DrinkWaterButton(UserService userService, int ml) {
        this.userService = userService;
        this.ml = ml;
    }

    @Override
    public SendMessage generateRequest(Update update) {
        String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        String username = update.getCallbackQuery().getFrom().getUserName();
        String text = "Выпил " + ml + " мл воды.";
        User user = userService.findOrCreate(chatId, username);
        LocalDateTime time = toLocalDataTime(update);
        drink(user, time);
        return SendMessage.builder().chatId(chatId).text(text).build();
    }

    public void drink(User user, LocalDateTime time){
        if(user.getCalendarWaterDrunk().isEmpty()){
            createWaterDrunksForDay(user);
        }
        else if(!user.getCalendarWaterDrunk().getLast().getDate().equals(LocalDate.now())){
            createWaterDrunksForDay(user);
        }
        DayDrinks dayDrinks = user.getCalendarWaterDrunk().getLast();
        userService.addToDay(dayDrinks, ml);
       // userService.addToDay(dayDrinks, ml, time);
    }

    public void createWaterDrunksForDay(User user){
        DayDrinks dayDrinks = userService.saveNow(user);
        user.getCalendarWaterDrunk().add(dayDrinks);
    }

    public LocalDateTime toLocalDataTime(Update update){
        Integer integer = update.getCallbackQuery().getMessage().getDate();
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
