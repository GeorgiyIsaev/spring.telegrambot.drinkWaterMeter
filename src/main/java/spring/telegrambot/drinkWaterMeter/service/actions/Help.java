package spring.telegrambot.drinkWaterMeter.service.actions;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import spring.telegrambot.drinkWaterMeter.service.update.MessageContract;

public class Help implements Action {
    @Override
    public SendMessage generateRequest(MessageContract messageContract) {
        String chatId = messageContract.getChatId();
        String text =       "/help (хелп, помощь) - отобразить инструкцию для пользователя\n" +
                "/hello" + " Отобразить информацию о пользователе\n" +
                "/recommended" + " Показать рекомендации\n" +
                "/full" + " Показать все записи\n" +
                "/drink" + " Выпить воды\n\n" +

                "/weight" + " Изменить вес\n" +
                "/height" + " Изменить рост\n" +
                "/sex" + " Указать пол\n" +
                "/drop" + " Стереть все данные о себе\n" +
                "Другие функции пока не добавлены";

        return SendMessage.builder().chatId(chatId).text(text).build();
    }
}
