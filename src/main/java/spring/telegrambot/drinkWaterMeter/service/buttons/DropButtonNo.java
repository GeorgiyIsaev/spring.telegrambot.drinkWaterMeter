package spring.telegrambot.drinkWaterMeter.service.buttons;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import spring.telegrambot.drinkWaterMeter.service.actions.Action;
import spring.telegrambot.drinkWaterMeter.service.update.CallbackQuery;

public class DropButtonNo implements Button {

    @Override
    public SendMessage generateRequest(CallbackQuery callbackQuery) {
        String chatId = callbackQuery.getChatId();
        String username = callbackQuery.getUsername();
        String text = "Удаление отменено!";
        return SendMessage.builder().chatId(chatId).text(text).build();
    }


}