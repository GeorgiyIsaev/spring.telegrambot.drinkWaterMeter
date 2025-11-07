package spring.telegrambot.drinkWaterMeter.service.buttons;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import spring.telegrambot.drinkWaterMeter.data.model.user.User;
import spring.telegrambot.drinkWaterMeter.service.actions.Action;
import spring.telegrambot.drinkWaterMeter.service.repository.UserService;

public class HeightButton implements Action {
        private final UserService userService;
        private final int cm;

        public HeightButton(UserService userService, int cm) {
            this.userService = userService;
            this.cm = cm;
        }

        @Override
        public SendMessage generateRequest(Update update) {
            String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
            String username = update.getCallbackQuery().getFrom().getUserName();
            String text = "Ваш рост изменен на " + cm + " см";
            User user = userService.findOrCreate(chatId, username);
            User userUpdate =  updateUser(user);
            text += "\n" + userUpdate.getUsername() + ": " + userUpdate.getHeight() + " см.";
            return SendMessage.builder().chatId(chatId).text(text).build();
        }

    private User updateUser(User user) {
        user.setHeight(cm);
        return userService.save(user);
    }
}