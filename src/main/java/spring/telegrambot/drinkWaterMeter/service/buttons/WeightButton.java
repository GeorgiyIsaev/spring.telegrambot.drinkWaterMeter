package spring.telegrambot.drinkWaterMeter.service.buttons;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import spring.telegrambot.drinkWaterMeter.repository.dao.UserDAO;
import spring.telegrambot.drinkWaterMeter.repository.model.user.User;
import spring.telegrambot.drinkWaterMeter.service.actions.Action;

public class WeightButton implements Action {
        private final UserDAO userDao;
        private final int kg;

        public WeightButton(UserDAO userDao, int kg) {
            this.userDao = userDao;
            this.kg = kg;
        }

        @Override
        public SendMessage generateRequest(Update update) {
            String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
            String username = update.getCallbackQuery().getFrom().getUserName();
            String text = "Ваш вес изменен на " + kg + " кг";
            User user = userDao.findOrCreate(chatId, username);
            User userUpdate =  updateUser(user);
            text += "\n" + userUpdate;
            return SendMessage.builder().chatId(chatId).text(text).build();
        }

    private User updateUser(User user) {
        user.setWeight(kg);
        return userDao.save(user);
    }
}