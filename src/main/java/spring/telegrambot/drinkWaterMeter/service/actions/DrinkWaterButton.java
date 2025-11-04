package spring.telegrambot.drinkWaterMeter.service.actions;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import spring.telegrambot.drinkWaterMeter.data.model.user.User;
import spring.telegrambot.drinkWaterMeter.data.model.user.WaterDrunk;
import spring.telegrambot.drinkWaterMeter.data.model.user.WaterDrunksForDay;
import spring.telegrambot.drinkWaterMeter.service.repository.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

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
        drink(user);
        return SendMessage.builder().chatId(chatId).text(text).build();
    }

    public void drink(User user){
        if(user.getCalendarWaterDrunk().isEmpty()){
            System.out.println("Список пуст");
            createWaterDrunksForDay(user);
        }
        else if(!user.getCalendarWaterDrunk().getLast().getDate().equals(LocalDate.now())){
            System.out.println("Сегодня нет");
            createWaterDrunksForDay(user);
        }

        WaterDrunksForDay waterDrunksForDay = user.getCalendarWaterDrunk().getLast();
        userService.addToDay(waterDrunksForDay, ml);
    }

    public void createWaterDrunksForDay(User user){
        WaterDrunksForDay waterDrunksForDay = userService.saveNow(user);
        user.getCalendarWaterDrunk().add(waterDrunksForDay);
    }
}
