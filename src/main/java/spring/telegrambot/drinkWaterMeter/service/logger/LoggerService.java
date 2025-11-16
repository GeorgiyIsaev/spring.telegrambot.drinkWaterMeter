package spring.telegrambot.drinkWaterMeter.service.logger;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import spring.telegrambot.drinkWaterMeter.client.contract.request.Request;
import spring.telegrambot.drinkWaterMeter.repository.dao.LogDAO;
import spring.telegrambot.drinkWaterMeter.repository.model.log.Log;
import spring.telegrambot.drinkWaterMeter.service.update.CallbackQuery;
import spring.telegrambot.drinkWaterMeter.service.update.Message;

import java.time.Instant;

@Service
public class LoggerService implements Logger{
    private final LogDAO logDAO;
    private final boolean isConsole;

    public LoggerService(LogDAO logDAO,
                         @Value("${logger.isConsole}") boolean isConsole) {
        this.logDAO = logDAO;
        this.isConsole = isConsole;
    }

    public void print(Log log){
        if (isConsole) {
            PrintLog.print(log);
        }
    }

    @Override
    public void logMessage(Message message) {
        Log log = logDAO.save(message.getTime(),"Message", message.toString());
        print(log);
    }

    @Override
    public void logCallbackQuery(CallbackQuery callbackQuery) {
        Log log = logDAO.save(callbackQuery.getTime(),"CallbackQuery", callbackQuery.toString());
        print(log);
    }

    @Override
    public void logRequest(Request request) {
        Log log = logDAO.save(Instant.now(),"Request", request.toString());
        print(log);
    }

    @Override
    public void logException(String info, Exception e) {
        Log log = logDAO.save(Instant.now(),"Request", e.getMessage());
        print(log);
    }

    @Override
    public void logResponse(String response) {
        Log log = logDAO.save(Instant.now(),"Request", response);
        print(log);
    }

    @Override
    public void logDelete(String request) {
        Log log = logDAO.save(Instant.now(),"Delete", request);
        print(log);
    }
}
