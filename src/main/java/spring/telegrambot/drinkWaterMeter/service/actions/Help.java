package spring.telegrambot.drinkWaterMeter.service.actions;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Help implements Action {
    @Override
    public SendMessage generateRequest(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        String text =       "/help (хелп, помощь) - отобразить инструкцию для пользователя\n" +
                "/weight" + " Изменить вес\n" +
                "/height" + " Изменить рост\n" +
                "/sex" + " Указать пол\n" +
                "/hello" + " Отобразить информацию о пользователе\n" +
                "/drunk" + " Выпить воды\n" +
                "/recommended" + " Показать рекомендации\n" +
                "Другие функции пока не добавлены";

        return SendMessage.builder().chatId(chatId).text(text).build();
    }
}
