package spring.telegrambot.drinkWaterMeter.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import spring.telegrambot.drinkWaterMeter.client.contract.request.Request;
import spring.telegrambot.drinkWaterMeter.client.contract.webhook.SetWebhookRequest;


@FeignClient(name = "telegram", url = "${telegrambot.urlToken}")
public interface TelegramFeignClient {

        @PostMapping("/setWebhook")
        String setWebhook(SetWebhookRequest request);

        @PostMapping("/sendMessage")
        Request sendMessage(SendMessage sendMessage);

        @PostMapping("/deleteMessage")
        String deleteMessage(DeleteMessage deleteMessage);
}

