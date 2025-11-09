package spring.telegrambot.drinkWaterMeter.service.buttons;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import spring.telegrambot.drinkWaterMeter.repository.model.user.User;
import spring.telegrambot.drinkWaterMeter.service.actions.Action;
import spring.telegrambot.drinkWaterMeter.repository.dao.UserDAO;
import spring.telegrambot.drinkWaterMeter.service.update.CallbackQuery;

public class HeightButton implements Button {
        private final UserDAO userDao;
        private final int cm;

        public HeightButton(UserDAO userDao, int cm) {
            this.userDao = userDao;
            this.cm = cm;
        }

    @Override
    public SendMessage generateRequest(CallbackQuery callbackQuery) {
        String chatId = callbackQuery.getChatId();
        String username = callbackQuery.getUsername();
            String text = "Ваш рост изменен на " + cm + " см";
            User user = userDao.findOrCreate(chatId, username);
            User userUpdate =  updateUser(user);
            text += "\n" + userUpdate.getUsername() + ": " + userUpdate.getHeight() + " см.";
            return SendMessage.builder().chatId(chatId).text(text).build();
        }

    private User updateUser(User user) {
        user.setHeight(cm);
        return userDao.save(user);
    }
}