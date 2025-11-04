package spring.telegrambot.drinkWaterMeter.service.actions;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import spring.telegrambot.drinkWaterMeter.data.model.user.User;
import spring.telegrambot.drinkWaterMeter.data.model.user.WaterDrunk;
import spring.telegrambot.drinkWaterMeter.data.model.user.WaterDrunksForDay;
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

        System.out.println("Извел екаем из ДБ");
        User user = userService.findOrCreate(chatId, username);
        String message =user.getName() + " добро пожаловать в чат id = " + user.getChatId() + ".\n";
        System.out.println(message);
        message +=  weight(user);
        System.out.println(message);
        message +=  "\n" + calendarWaterDrunk (user);
        System.out.println(message);

        return SendMessage.builder().chatId(chatId).text(message).build();
    }

    public String weight(User user){
        if(user.getWeight()==0){
            return "Вы еще не указали свой вес!";
        }
        else {
            return "Ваш вес составляет " + user.getWeight() + " кг.";
        }
    }

    public String calendarWaterDrunk(User user) {
        if(user.getCalendarWaterDrunk()==null){
            return "Записи о выпитой воде отсутствуют!";
        }
        else if (user.getCalendarWaterDrunk().isEmpty()){
            return "Вы еще не вносили записей о количестве выпитой воды!";
        }
        else{
            WaterDrunksForDay lastDay= user.getCalendarWaterDrunk().getLast();

            StringBuilder text = new StringBuilder("Последний день записей: " + lastDay.getDate()+ "\n");
            for(WaterDrunk waterDrunk : lastDay.getWaterDunks()){
                text.append("В ").append(waterDrunk.getTime().toLocalTime())
                        .append(" выпито ").append(waterDrunk.getCountWaterMl())
                        .append(" мл воды\n");
            }
            return text.toString();
        }
    }
}
