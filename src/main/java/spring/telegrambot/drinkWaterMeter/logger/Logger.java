package spring.telegrambot.drinkWaterMeter.logger;

import org.telegram.telegrambots.meta.api.objects.Update;
import spring.telegrambot.drinkWaterMeter.client.contract.request.Request;


public interface Logger  {
     void logMessage(Update update);
     void logCallbackQuery(Update update);

     void logRequest(Request request);
     void logException(String info, Exception e);
     void logResponse(String response);
}
