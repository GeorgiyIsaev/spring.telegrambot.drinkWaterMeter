package spring.telegrambot.drinkWaterMeter.logger;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import spring.telegrambot.drinkWaterMeter.data.request.Request;


@Component
@ConditionalOnProperty(value="loggerEnable" ,havingValue="onlyConsole",matchIfMissing = false)
public class ToConsole implements Logger {

    private final LogFormater logFormater;

    public ToConsole(LogFormater logFormater) {
        this.logFormater = logFormater;
    }

    @Override
    public void logMessage(Update update){
        System.out.println(logFormater.logMessage(update));
    }

    @Override
    public void logCallbackQuery(Update update){
        System.out.println(logFormater.logCallbackQuery(update));
    }

    @Override
    public void logRequest(Request request){
        System.out.println(logFormater.logRequest(request));
    }

    @Override
    public void logException(String info, Exception e){
        System.out.println(logFormater.logException(info, e));
    }

    @Override
    public void logResponse(String response) {
        System.out.println(logFormater.logResponse(response));
    }
}
