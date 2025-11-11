//package spring.telegrambot.drinkWaterMeter.service.actions;
//
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//import org.telegram.telegrambots.meta.api.objects.Update;
//import spring.telegrambot.drinkWaterMeter.repository.model.user.User;
//import spring.telegrambot.drinkWaterMeter.repository.model.user.WaterDrink;
//import spring.telegrambot.drinkWaterMeter.repository.model.user.DayDrinks;
//import spring.telegrambot.drinkWaterMeter.repository.dao.UserDAO;
//import spring.telegrambot.drinkWaterMeter.service.update.Message;
//
//public class Full implements Action {
//
//    private final UserDAO userDao;
//    public Full(UserDAO userDao) {
//        this.userDao = userDao;
//    }
//
//    @Override
//    public SendMessage generateRequest(Message message) {
//        String chatId = message.getChatId();
//        String username = message.getUsername();
//
//
//        User user = userDao.findOrCreate(chatId, username);
//        String text ="Все записи " + user.getUsername() + " !\n";
//       // text +=  "\n" + calendarWaterDrunk (user);
//
//
//        return SendMessage.builder().chatId(chatId).text(text).build();
//    }
//

import spring.telegrambot.drinkWaterMeter.repository.model.user.User;
import spring.telegrambot.drinkWaterMeter.repository.model.user.WaterDrink;

////    public String calendarWaterDrunk(User user) {
////        if (user.getCalendarWaterDrunk().isEmpty() || user.getCalendarWaterDrunk() == null) {
////            return "Записи о выпитой воде отсутствуют!";
////        }
////        StringBuilder text = new StringBuilder();
////        for(  DayDrinks lastDay : user.getCalendarWaterDrunk()) {
////            StringBuilder day = new StringBuilder();
////            int allml = 0;
////            for (WaterDrink waterDrink : lastDay.getWaterDunks()) {
////                day.append("В ")
////                        .append(getTime(waterDrink))
////                        .append(" выпито ")
////                        .append(waterDrink.getCountWaterMl())
////                        .append(" мл воды\n");
////                allml += waterDrink.getCountWaterMl();
////            }
////            text.append("--- День:  ").append(lastDay.getDate());
////            text.append(" Всего за день выпито ").append(allml).append(" мл жидкости.\n");
////            text.append(day);
////        }
////        return text.toString();
////    }
////
////    public String getTime(WaterDrink waterDrink){
////        return waterDrink.getTime().getHour() + ":" +
////                waterDrink.getTime().getMinute() + ":" +
////                waterDrink.getTime().getSecond();
////    }
//}

//private String lastDayInfo(User user) {
//    List<WaterDrink> waterDrinks = user.getWaterDunks();
//    List<WaterDrink> lastDay = new ArrayList<>();
//
//    long timezone = user.getTimeShift() + 3600;
//    Instant tempTime = waterDrinks.
//    for (WaterDrink waterDrink : waterDrinks){
//        Instant time = waterDrink.getTime().plusSeconds(timezone);
//
//        waterDrink.
//    }
//
//
//}