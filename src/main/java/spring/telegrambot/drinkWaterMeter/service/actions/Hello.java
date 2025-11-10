package spring.telegrambot.drinkWaterMeter.service.actions;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import spring.telegrambot.drinkWaterMeter.repository.dao.UserDAO;
import spring.telegrambot.drinkWaterMeter.repository.model.user.DayDrinks;
import spring.telegrambot.drinkWaterMeter.repository.model.user.User;
import spring.telegrambot.drinkWaterMeter.repository.model.user.WaterDrink;
import spring.telegrambot.drinkWaterMeter.service.update.Message;

public class Hello implements Action {

    private final UserDAO userDao;
    public Hello(UserDAO userDao) {
        this.userDao = userDao;
    }

    @Override
    public SendMessage generateRequest(Message message) {
        String chatId = message.getChatId();
        String username = message.getUsername();
        User user = userDao.findOrCreate(chatId, username);

        String text =user.getUsername() + " добро пожаловать в чат id = " + user.getChatId() + ".\n";
        text +=  height(user) +  "\n";
        text +=  weight(user) +  "\n";
        text +=  sex(user) +  "\n";
        text +=  "\n" + calendarWaterDrunk (user);
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
