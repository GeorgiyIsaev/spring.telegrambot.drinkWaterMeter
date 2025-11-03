package spring.telegrambot.drinkWaterMeter.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import spring.telegrambot.drinkWaterMeter.client.TelegramFeignClient;
import spring.telegrambot.drinkWaterMeter.data.request.Request;
import spring.telegrambot.drinkWaterMeter.data.webhook.SetWebhookRequest;
import spring.telegrambot.drinkWaterMeter.logger.Logger;

@Service
public class TelegramWaterService {
    private final String urlServer;
    private final TelegramFeignClient telegramFeignClient;
    private final Logger logger;
    private final ActionsService actionsService;
    private final ButtonsService buttonsService;

    public TelegramWaterService(
            @Value("${telegrambot.urlHostTunnel}") String urlServer,
            TelegramFeignClient telegramFeignClient,
            Logger logger,
            ButtonsService buttonsService,
            ActionsService actionsService) {
        System.out.println("[" + urlServer + "]");
        this.urlServer = urlServer;
        this.telegramFeignClient = telegramFeignClient;
        this.logger = logger;
        this.actionsService = actionsService;
        this.buttonsService = buttonsService;
    }


    public void setWebhook(){
      SetWebhookRequest request = new SetWebhookRequest(urlServer);
        //SetWebhookRequest request = new SetWebhookRequest("");
        String response = telegramFeignClient.setWebhook(request);
        logger.logResponse(response);
    }

    public Logger logger() {
        return logger;
    }





    public void sendMessage(String chatId, String text){
        SendMessage sendMessage = SendMessage.builder().chatId(chatId).text(text).build();
        Request request = telegramFeignClient.sendMessage(sendMessage);
        System.out.println("sendMessage: " + request);
    }


    public void updateRouting(Update update) {
        if(update.hasMessage()){
            this.replyToMessage(update);

        } else if (update.hasCallbackQuery()) {
            this.replyToCallbackQuery(update);
        }
    }

    private void replyToCallbackQuery(Update update) {
        logger.logCallbackQuery(update);
        SendMessage sendMessage = buttonsService.generateRequest(update);
        Request request = telegramFeignClient.sendMessage(sendMessage);
        logger.logRequest(request);
    }

    private void replyToMessage(Update update) {
        logger.logMessage(update);
        SendMessage sendMessage = actionsService.generateRequest(update);
        Request request = telegramFeignClient.sendMessage(sendMessage);
        logger.logRequest(request);
    }
}
