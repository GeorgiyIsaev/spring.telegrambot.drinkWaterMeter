package spring.telegrambot.drinkWaterMeter.service.actions;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import spring.telegrambot.drinkWaterMeter.service.update.MessageContract;

public interface Action {
    SendMessage generateRequest(MessageContract messageContract);
}
