package spring.telegrambot.drinkWaterMeter.service.actions;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import spring.telegrambot.drinkWaterMeter.repository.model.user.User;
import spring.telegrambot.drinkWaterMeter.repository.model.user.WaterDrink;
import spring.telegrambot.drinkWaterMeter.repository.model.user.DayDrinks;
import spring.telegrambot.drinkWaterMeter.repository.dao.UserDAO;

public class Full implements Action {

    private final UserDAO userDao;
    public Full(UserDAO userDao) {
        this.userDao = userDao;
    }

    @Override
    public SendMessage generateRequest(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        String username = update.getMessage().getFrom().getUserName();


        User user = userDao.findOrCreate(chatId, username);
        String message ="Все записи " + user.getUsername() + " !\n";
        message +=  "\n" + calendarWaterDrunk (user);


        return SendMessage.builder().chatId(chatId).text(message).build();
    }

    public String calendarWaterDrunk(User user) {
        if (user.getCalendarWaterDrunk().isEmpty() || user.getCalendarWaterDrunk() == null) {
            return "Записи о выпитой воде отсутствуют!";
        }
        StringBuilder text = new StringBuilder();
        for(  DayDrinks lastDay : user.getCalendarWaterDrunk()) {
            StringBuilder day = new StringBuilder();
            int allml = 0;
            for (WaterDrink waterDrink : lastDay.getWaterDunks()) {
                day.append("В ")
                        .append(getTime(waterDrink))
                        .append(" выпито ")
                        .append(waterDrink.getCountWaterMl())
                        .append(" мл воды\n");
                allml += waterDrink.getCountWaterMl();
            }
            text.append("--- День:  ").append(lastDay.getDate());
            text.append(" Всего за день выпито ").append(allml).append(" мл жидкости.\n");
            text.append(day);
        }
        return text.toString();
    }

    public String getTime(WaterDrink waterDrink){
        return waterDrink.getTime().getHour() + ":" +
                waterDrink.getTime().getMinute() + ":" +
                waterDrink.getTime().getSecond();
    }
}
