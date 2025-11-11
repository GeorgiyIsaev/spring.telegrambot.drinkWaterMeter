//package spring.telegrambot.drinkWaterMeter.service.buttons;
//
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//import org.telegram.telegrambots.meta.api.objects.Update;
//import spring.telegrambot.drinkWaterMeter.repository.dao.UserDAO;
//import spring.telegrambot.drinkWaterMeter.repository.model.user.User;
//import spring.telegrambot.drinkWaterMeter.service.actions.Action;
//import spring.telegrambot.drinkWaterMeter.service.update.CallbackQuery;
//
//public class DropButtonYes implements Button {
//    private final UserDAO userDao;
//
//    public DropButtonYes(UserDAO userDao) {
//        this.userDao = userDao;
//    }
//
//    @Override
//    public SendMessage generateRequest(CallbackQuery callbackQuery) {
//        String chatId = callbackQuery.getChatId();
//        String username = callbackQuery.getUsername();
//
//        User user = userDao.findOrCreate(chatId, username);
//        userDao.delete(user);
//
//        String text = "Все данные вашего профиля удалены!";
//        return SendMessage.builder().chatId(chatId).text(text).build();
//    }
//
//    private void updateUser(User user) {
//         userDao.delete(user);
//    }
//}