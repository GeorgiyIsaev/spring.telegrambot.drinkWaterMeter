package spring.telegrambot.drinkWaterMeter.service.logger;

import spring.telegrambot.drinkWaterMeter.client.contract.request.Request;
import spring.telegrambot.drinkWaterMeter.service.update.CallbackQuery;
import spring.telegrambot.drinkWaterMeter.service.update.Message;


public interface Logger {
     void logMessage(Message message);
     void logCallbackQuery(CallbackQuery callbackQuery);

     void logRequest(Request request);
     void logException(String info, Exception e);
     void logResponse(String response);

     void logDelete(String requestDelete);
}
