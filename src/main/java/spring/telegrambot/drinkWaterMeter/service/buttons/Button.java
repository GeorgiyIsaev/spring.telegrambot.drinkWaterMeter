package spring.telegrambot.drinkWaterMeter.service.buttons;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import spring.telegrambot.drinkWaterMeter.service.update.CallbackQueryContract;

public interface Button {
    SendMessage generateRequest(CallbackQueryContract callbackQueryContract);
}
