package spring.telegrambot.drinkWaterMeter.service.actions;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import spring.telegrambot.drinkWaterMeter.data.model.user.User;
import spring.telegrambot.drinkWaterMeter.data.model.user.WaterDrunk;
import spring.telegrambot.drinkWaterMeter.data.model.user.WaterDrunksForDay;
import spring.telegrambot.drinkWaterMeter.service.repository.UserService;

import java.time.LocalDate;

public class TestDB implements Action {

    private final UserService userService;
    public TestDB(UserService userService) {
        this.userService = userService;
    }

    @Override
    public SendMessage generateRequest(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        String text = update.getMessage().getText();
        String updateID = update.getUpdateId().toString();
        String username = update.getMessage().getFrom().getUserName();
        Integer data = update.getMessage().getDate();
        System.out.println("data: " +  data);

//        System.out.println("Извелекаем из ДБ");
//        User user = userService.findOrCreate(chatId, username);
//        String message =user.name() + " добро пожаловать в чат id = " + user.chatId() + ".\n";
//        System.out.println(message);
//        message +=  weight(user);
//        System.out.println(message);
//        message +=  "\n" + calendarWaterDrunk (user);
//        System.out.println(message);

      return SendMessage.builder().chatId(chatId).text(text + " хахаха").build();
    }

//    public String weight(User user){
//        if(user.weight()==0){
//            return "Вы еще не указали свой вес!";
//        }
//        else {
//            return "Ваш вес составляет " + user.weight() + " кг.";
//        }
//    }
//
//    public String calendarWaterDrunk(User user) {
//        if(user.calendarWaterDrunk()==null){
//            return "Записи о выпитой воде отсутствуют";
//        }
//        else if (user.calendarWaterDrunk().isEmpty()){
//            return "Список пуст";
//        }
//        else{
//            WaterDrunksForDay lastDay= user.calendarWaterDrunk().getLast();
//
//            StringBuilder text = new StringBuilder("Последний день записей: " + lastDay.date());
//            for(WaterDrunk waterDrunk : lastDay.waterDunks()){
//                text.append("В ").append(waterDrunk.time().toLocalTime())
//                        .append(" выпито ").append(waterDrunk.countWaterMl())
//                        .append(" мл воды\n");
//            }
//            return text.toString();
//        }
//     }

}
