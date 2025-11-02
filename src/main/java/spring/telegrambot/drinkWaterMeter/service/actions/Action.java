package spring.telegrambot.drinkWaterMeter.service.actions;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface Action {
    SendMessage generateRequest(String chatId);
}
