package spring.telegrambot.drinkWaterMeter.service.logger;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import spring.telegrambot.drinkWaterMeter.client.contract.request.Request;
import spring.telegrambot.drinkWaterMeter.repository.dao.LogDAO;
import spring.telegrambot.drinkWaterMeter.repository.model.log.Log;
import spring.telegrambot.drinkWaterMeter.service.update.CallbackQuery;
import spring.telegrambot.drinkWaterMeter.service.update.Message;

@Service
public class LoggerService implements Logger{
    private final LogDAO logDAO;
    private final boolean isConsole;

    public LoggerService(LogDAO logDAO,
                         @Value("${logger.isConsole}") boolean isConsole) {
        this.logDAO = logDAO;
        this.isConsole = isConsole;
        print(new Log());
    }

    public void print(Log log){
        if (isConsole) {
            System.out.println("{logger.isConsole} " + isConsole);
        }
        else {
            System.out.println("{logger.isConsole} " + isConsole);
        }
    }

    @Override
    public void logMessage(Message message) {

    }

    @Override
    public void logCallbackQuery(CallbackQuery callbackQuery) {

    }

    @Override
    public void logRequest(Request request) {

    }

    @Override
    public void logException(String info, Exception e) {

    }

    @Override
    public void logResponse(String response) {

    }
}
