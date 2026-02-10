package spring.telegrambot.drinkWaterMeter.controller.config;

import org.telegram.telegrambots.meta.api.objects.*;

import java.util.ArrayList;
import java.util.List;

public class TelegramUpdateExample {

    public static Update createUpdateWithCommand() {
        Update update = new Update();
        
        User user = new User();
        user.setId(987654321L);
        user.setFirstName("Анна");
        user.setUserName("anna_telegram");
        user.setIsBot(false);
        
        Chat chat = new Chat();
        chat.setId(987654321L);
        chat.setType("private");
        chat.setFirstName("Анна");
        
        Message message = new Message();
        message.setMessageId(2);
        message.setFrom(user);
        message.setChat(chat);
        message.setDate((int) (System.currentTimeMillis() / 1000));
        message.setText("/start"); // Команда для бота
        
        update.setMessage(message);
        update.setUpdateId(123457);
        
        return update;
    }
    
    public static Update createCallbackQueryUpdate() {
        Update update = new Update();
        
        User user = new User();
        user.setId(111222333L);
        user.setFirstName("Мария");
        user.setUserName("maria_user");
        
        // Создаем CallbackQuery (нажатие на inline-кнопку)
        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setId("callback_123");
        callbackQuery.setFrom(user);
        callbackQuery.setData("button_clicked"); // Данные callback

        Message message = new Message();
        message.setMessageId(100);
        message.setDate((int) (System.currentTimeMillis() / 1000));
        
        Chat chat = new Chat();
        chat.setId(111222333L);
        chat.setType("private");
        message.setChat(chat);
        
        callbackQuery.setMessage(message);
        
        update.setCallbackQuery(callbackQuery);
        update.setUpdateId(123459);
        
        return update;
    }
}