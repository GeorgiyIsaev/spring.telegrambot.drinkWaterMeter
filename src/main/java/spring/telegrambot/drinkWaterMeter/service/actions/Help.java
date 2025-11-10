package spring.telegrambot.drinkWaterMeter.service.actions;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import spring.telegrambot.drinkWaterMeter.service.update.Message;

public class Help implements Action {
    @Override
    public SendMessage generateRequest(Message message) {
        String chatId = message.getChatId();
        String text =       "/help (хелп, помощь) - отобразить инструкцию для пользователя\n" +
                "/hello" + " Отобразить информацию о пользователе\n" +
                "/recommended" + " Показать рекомендации\n" +
                "/full" + " Показать все записи\n" +
                "/drunk" + " Выпить воды\n\n" +

                "/weight" + " Изменить вес\n" +
                "/height" + " Изменить рост\n" +
                "/sex" + " Указать пол\n" +
                "/drop" + " Стереть все данные о себе\n" +
                "Другие функции пока не добавлены";

        return SendMessage.builder().chatId(chatId).text(text).build();
    }
}
