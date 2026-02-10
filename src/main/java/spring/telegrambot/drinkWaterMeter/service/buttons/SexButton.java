package spring.telegrambot.drinkWaterMeter.service.buttons;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import spring.telegrambot.drinkWaterMeter.repository.dao.UserDAO;
import spring.telegrambot.drinkWaterMeter.repository.model.user.User;
import spring.telegrambot.drinkWaterMeter.service.update.CallbackQueryContract;

public class SexButton implements Button {
        private final UserDAO userDao;
        private final Boolean isWoman ;

    public SexButton(UserDAO userDao, Boolean isWoman) {
        this.userDao = userDao;
        this.isWoman = isWoman;
    }

    @Override
    public SendMessage generateRequest(CallbackQueryContract callbackQueryContract) {
        String chatId = callbackQueryContract.getChatId();
        String username = callbackQueryContract.getUsername();
            User user = userDao.findOrCreate(chatId, username);
            User userUpdate =  updateUser(user);
            String sex = userUpdate.getSex() ? "женский" : "мужской";
            String text = "Ваш указали " + sex + " пол.";
            return SendMessage.builder().chatId(chatId).text(text).build();
        }

    private User updateUser(User user) {
        user.setSex(isWoman);
        return userDao.save(user);
    }
}