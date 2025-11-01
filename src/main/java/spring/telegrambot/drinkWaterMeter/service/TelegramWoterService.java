package spring.telegrambot.drinkWaterMeter.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import spring.telegrambot.drinkWaterMeter.client.TelegramFeignClient;
import spring.telegrambot.drinkWaterMeter.data.request.Request;
import spring.telegrambot.drinkWaterMeter.data.webhook.SetWebhookRequest;
import spring.telegrambot.drinkWaterMeter.logger.Logger;

@Service
public class TelegramWoterService {
    private final String urlServer;
    private final TelegramFeignClient telegramFeignClient;
    private final Logger logger;
    public TelegramWoterService(
            Logger logger,
            TelegramFeignClient telegramFeignClient,
            @Value("${telegrambot.urlHostTunnel}") String urlServer) {
        this.telegramFeignClient = telegramFeignClient;
        this.urlServer = urlServer;
        this.logger = logger;
    }

    @PostConstruct
    public void init(){
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



}
