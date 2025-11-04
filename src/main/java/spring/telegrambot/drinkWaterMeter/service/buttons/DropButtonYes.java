package spring.telegrambot.drinkWaterMeter.service.buttons;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import spring.telegrambot.drinkWaterMeter.data.model.user.User;
import spring.telegrambot.drinkWaterMeter.service.actions.Action;
import spring.telegrambot.drinkWaterMeter.service.repository.UserService;

public class DropButtonYes implements Action {
    private final UserService userService;

    public DropButtonYes(UserService userService) {
        this.userService = userService;
    }

    @Override
    public SendMessage generateRequest(Update update) {
        String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        String username = update.getCallbackQuery().getFrom().getUserName();

        User user = userService.findOrCreate(chatId, username);
        userService.delete(user);

        String text = "Все данные вашего профиля удалены!";
        return SendMessage.builder().chatId(chatId).text(text).build();
    }

    private void updateUser(User user) {
         userService.delete(user);
    }
}