package spring.telegrambot.drinkWaterMeter.service.actions;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class Help implements Action{
    @Override
    public SendMessage generateRequest(String chatId) {
        String text =       "/help (хелп, помощь) - отобразить инструкцию для пользователя\n" +
                "Другие функции пока не добавлены";
        return SendMessage.builder().chatId(chatId).text(text).build();
    }
}
