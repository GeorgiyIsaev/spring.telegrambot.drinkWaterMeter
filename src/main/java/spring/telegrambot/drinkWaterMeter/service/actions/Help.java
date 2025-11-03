package spring.telegrambot.drinkWaterMeter.service.actions;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Help implements Action {
    @Override
    public SendMessage generateRequest(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        String text =       "/help (хелп, помощь) - отобразить инструкцию для пользователя\n" +
                "Другие функции пока не добавлены";
        return SendMessage.builder().chatId(chatId).text(text).build();
    }
}
