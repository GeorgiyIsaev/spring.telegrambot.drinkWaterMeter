package spring.telegrambot.drinkWaterMeter.service.actions;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class Return implements Action{
    @Override
    public SendMessage generateRequest(String chatId) {
        String text ="C ВОЗВРАЩЕНИЕМ!" ;
        return SendMessage.builder().chatId(chatId).text(text).build();
    }
}
