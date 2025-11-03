package spring.telegrambot.drinkWaterMeter.service.actions;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import spring.telegrambot.drinkWaterMeter.service.repository.UserService;

public class DrinkWaterButton implements Action {
    private final  UserService userService;
    private final int ml;

    public DrinkWaterButton(UserService userService, int ml) {
        this.userService = userService;
        this.ml = ml;
    }

    @Override
    public SendMessage generateRequest(Update update) {
        String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        String text =       "Выпил " + ml + " мл воды.";
        return SendMessage.builder().chatId(chatId).text(text).build();
    }
}
