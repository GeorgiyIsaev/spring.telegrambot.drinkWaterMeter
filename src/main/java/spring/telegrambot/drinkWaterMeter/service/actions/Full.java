package spring.telegrambot.drinkWaterMeter.service.actions;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import spring.telegrambot.drinkWaterMeter.data.model.user.User;
import spring.telegrambot.drinkWaterMeter.data.model.user.WaterDrunk;
import spring.telegrambot.drinkWaterMeter.data.model.user.WaterDrunksForDay;
import spring.telegrambot.drinkWaterMeter.service.repository.UserService;

public class Full implements Action {

    private final UserService userService;
    public Full(UserService userService) {
        this.userService = userService;
    }

    @Override
    public SendMessage generateRequest(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        String username = update.getMessage().getFrom().getUserName();


        User user = userService.findOrCreate(chatId, username);
        String message ="Все записи " + user.getUsername() + " !\n";
        message +=  "\n" + calendarWaterDrunk (user);


        return SendMessage.builder().chatId(chatId).text(message).build();
    }

    public String calendarWaterDrunk(User user) {
        if (user.getCalendarWaterDrunk().isEmpty() || user.getCalendarWaterDrunk() == null) {
            return "Записи о выпитой воде отсутствуют!";
        }
        StringBuilder text = new StringBuilder();
        for(  WaterDrunksForDay lastDay : user.getCalendarWaterDrunk()) {
            StringBuilder day = new StringBuilder();
            int allml = 0;
            for (WaterDrunk waterDrunk : lastDay.getWaterDunks()) {
                day.append("В ")
                        .append(getTime(waterDrunk))
                        .append(" выпито ")
                        .append(waterDrunk.getCountWaterMl())
                        .append(" мл воды\n");
                allml += waterDrunk.getCountWaterMl();
            }
            text.append("--- День:  ").append(lastDay.getDate());
            text.append(" Всего за день выпито ").append(allml).append(" мл жидкости.\n");
            text.append(day);
        }
        return text.toString();
    }

    public String getTime(WaterDrunk waterDrunk){
        return waterDrunk.getTime().getHour() + ":" +
                waterDrunk.getTime().getMinute() + ":" +
                waterDrunk.getTime().getSecond();
    }
}
