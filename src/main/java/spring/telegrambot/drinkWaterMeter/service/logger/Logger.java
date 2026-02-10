package spring.telegrambot.drinkWaterMeter.service.logger;

import spring.telegrambot.drinkWaterMeter.client.contract.request.Request;
import spring.telegrambot.drinkWaterMeter.service.update.CallbackQueryContract;
import spring.telegrambot.drinkWaterMeter.service.update.MessageContract;


public interface Logger {
     void logMessage(MessageContract messageContract);
     void logCallbackQuery(CallbackQueryContract callbackQueryContract);

     void logRequest(Request request);
     void logException(String info, Exception e);
     void logResponse(String response);

     void logDelete(String requestDelete);
}
