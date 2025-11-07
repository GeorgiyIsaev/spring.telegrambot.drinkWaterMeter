package spring.telegrambot.drinkWaterMeter.service.actions;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import spring.telegrambot.drinkWaterMeter.data.model.user.DayDrinks;
import spring.telegrambot.drinkWaterMeter.data.model.user.User;
import spring.telegrambot.drinkWaterMeter.data.model.user.WaterDrink;
import spring.telegrambot.drinkWaterMeter.service.repository.UserService;

public class Hello implements Action {

    private final UserService userService;
    public Hello(UserService userService) {
        this.userService = userService;
    }

    @Override
    public SendMessage generateRequest(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        String username = update.getMessage().getFrom().getUserName();
        User user = userService.findOrCreate(chatId, username);

        String message =user.getUsername() + " добро пожаловать в чат id = " + user.getChatId() + ".\n";
        message +=  height(user) +  "\n";
        message +=  weight(user) +  "\n";
        message +=  sex(user) +  "\n";
        message +=  "\n" + calendarWaterDrunk (user);
        return SendMessage.builder().chatId(chatId).text(message).build();
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

    public String calendarWaterDrunk(User user) {
        if (user.getCalendarWaterDrunk().isEmpty() || user.getCalendarWaterDrunk() == null) {
            return "Записи о выпитой воде отсутствуют!";
        }

        DayDrinks lastDay = user.getCalendarWaterDrunk().getLast();

        StringBuilder text = new StringBuilder("Последний день записей: " + lastDay.getDate() + "\n");
        int allml = 0;
        for (WaterDrink waterDrink : lastDay.getWaterDunks()) {
            text.append("В ")
                    .append(getTime(waterDrink))
                    .append(" выпито ")
                    .append(waterDrink.getCountWaterMl())
                    .append(" мл воды\n");
            allml += waterDrink.getCountWaterMl();
        }
        text.append("Всего за день выпито ").append(allml).append(" мл жидкости.");
        return text.toString();
    }

    public String getTime(WaterDrink waterDrink){
        return waterDrink.getTime().getHour() + ":" +
                waterDrink.getTime().getMinute() + ":" +
                waterDrink.getTime().getSecond();
    }
}
