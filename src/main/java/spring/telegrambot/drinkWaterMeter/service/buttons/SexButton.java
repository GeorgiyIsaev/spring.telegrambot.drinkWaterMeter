package spring.telegrambot.drinkWaterMeter.service.buttons;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import spring.telegrambot.drinkWaterMeter.data.model.user.User;
import spring.telegrambot.drinkWaterMeter.service.actions.Action;
import spring.telegrambot.drinkWaterMeter.service.repository.UserService;

public class SexButton implements Action {
        private final UserService userService;
        private final Boolean isWoman ;

    public SexButton(UserService userService, Boolean isWoman) {
        this.userService = userService;
        this.isWoman = isWoman;
    }

    @Override
        public SendMessage generateRequest(Update update) {
            String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
            String username = update.getCallbackQuery().getFrom().getUserName();
            User user = userService.findOrCreate(chatId, username);
            User userUpdate =  updateUser(user);
            String sex = userUpdate.getSex() ? "женский" : "мужской";
            String text = "Ваш указали " + sex + " пол.";
            return SendMessage.builder().chatId(chatId).text(text).build();
        }

    private User updateUser(User user) {
        user.setSex(isWoman);
        return userService.save(user);
    }
}