package spring.telegrambot.drinkWaterMeter.service.buttons;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import spring.telegrambot.drinkWaterMeter.data.model.user.User;
import spring.telegrambot.drinkWaterMeter.service.actions.Action;
import spring.telegrambot.drinkWaterMeter.service.repository.UserService;

public class WeightButtons implements Action {
        private final UserService userService;
        private final int kg;

        public WeightButtons(UserService userService, int kg) {
            this.userService = userService;
            this.kg = kg;
        }

        @Override
        public SendMessage generateRequest(Update update) {
            String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
            String username = update.getCallbackQuery().getFrom().getUserName();
            String text = "Ваш вес изменен на " + kg + " кг";
            User user = userService.findOrCreate(chatId, username);
            User userUpdate =  updateUser(user);
            text += "\n" + userUpdate;
            return SendMessage.builder().chatId(chatId).text(text).build();
        }

    private User updateUser(User user) {
        user.setWeight(kg);
        return userService.save(user);
    }
}