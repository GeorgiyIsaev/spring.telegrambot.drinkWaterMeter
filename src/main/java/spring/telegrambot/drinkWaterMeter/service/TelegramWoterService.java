package spring.telegrambot.drinkWaterMeter.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import spring.telegrambot.drinkWaterMeter.client.TelegramFeignClient;
import spring.telegrambot.drinkWaterMeter.data.request.Request;
import spring.telegrambot.drinkWaterMeter.data.webhook.SetWebhookRequest;

@Service
public class TelegramWoterService {
    private final String urlServer;
    private final TelegramFeignClient telegramFeignClient;

    public TelegramWoterService(
            TelegramFeignClient telegramFeignClient,
            @Value("${telegrambot.urlHostTunnel}") String urlServer) {
        this.telegramFeignClient = telegramFeignClient;
        this.urlServer = urlServer;
    }

    @PostConstruct
    public void init(){
        SetWebhookRequest request = new SetWebhookRequest(urlServer);
        //SetWebhookRequest request = new SetWebhookRequest("");
        String response = telegramFeignClient.setWebhook(request);
        System.out.println(response);
    }


    public void sendMessage(String chatId, String text){
        SendMessage sendMessage = SendMessage.builder().chatId(chatId).text(text).build();
        Request request = telegramFeignClient.sendMessage(sendMessage);
        System.out.println("sendMessage: " + request);
    }

}
