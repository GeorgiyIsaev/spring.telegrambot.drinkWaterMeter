package spring.telegrambot.drinkWaterMeter.service.buttons;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import spring.telegrambot.drinkWaterMeter.service.update.CallbackQueryContract;

public class DropButtonNo implements Button {

    @Override
    public SendMessage generateRequest(CallbackQueryContract callbackQueryContract) {
        String chatId = callbackQueryContract.getChatId();
        String username = callbackQueryContract.getUsername();
        String text = "Удаление отменено!";
        return SendMessage.builder().chatId(chatId).text(text).build();
    }


}