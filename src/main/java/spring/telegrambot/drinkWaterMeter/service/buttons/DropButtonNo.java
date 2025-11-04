package spring.telegrambot.drinkWaterMeter.service.buttons;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import spring.telegrambot.drinkWaterMeter.service.actions.Action;

public class DropButtonNo implements Action {

    @Override
    public SendMessage generateRequest(Update update) {
        String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        String username = update.getCallbackQuery().getFrom().getUserName();
        String text = "Удаление отменено!";
        return SendMessage.builder().chatId(chatId).text(text).build();
    }


}