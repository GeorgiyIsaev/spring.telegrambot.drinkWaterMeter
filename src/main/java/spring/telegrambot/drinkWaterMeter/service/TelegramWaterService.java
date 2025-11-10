package spring.telegrambot.drinkWaterMeter.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import spring.telegrambot.drinkWaterMeter.client.TelegramFeignClient;
import spring.telegrambot.drinkWaterMeter.client.contract.request.Request;
import spring.telegrambot.drinkWaterMeter.client.contract.webhook.SetWebhookRequest;
import spring.telegrambot.drinkWaterMeter.logger.Logger;
import spring.telegrambot.drinkWaterMeter.service.update.CallbackQuery;
import spring.telegrambot.drinkWaterMeter.service.update.Message;

@Service
public class TelegramWaterService {
    private final String urlServer;
    private final TelegramFeignClient telegramFeignClient;
    private final Logger logger;
    private final CommandsService commandsService;
    private final ButtonsService buttonsService;

    public TelegramWaterService(
            @Value("${telegrambot.urlHostTunnel}") String urlServer,
            TelegramFeignClient telegramFeignClient,
            Logger logger,
            ButtonsService buttonsService,
            CommandsService commandsService) {
        System.out.println("[" + urlServer + "]");
        this.urlServer = urlServer;
        this.telegramFeignClient = telegramFeignClient;
        this.logger = logger;
        this.commandsService = commandsService;
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


    public void replyToMessage(Message message) {
        //logger.logMessage(update);
        SendMessage sendMessage = commandsService.generateRequest(message);
        Request request = telegramFeignClient.sendMessage(sendMessage);
        logger.logRequest(request);
    }

    public void dropButtons(CallbackQuery callbackQuery){
        Integer messageId = callbackQuery.getMessageId();
        String chatId = callbackQuery.getChatId();
        DeleteMessage deleteMessage = DeleteMessage.builder().chatId(chatId).messageId(messageId).build();
        String requestDelete = telegramFeignClient.deleteMessage(deleteMessage);
        System.out.println(requestDelete);
    }


    public void replyToCallbackQuery(CallbackQuery callbackQuery) {
       // logger.logCallbackQuery(update);
        SendMessage sendMessage = buttonsService.generateRequest(callbackQuery);
        Request request = telegramFeignClient.sendMessage(sendMessage);
        logger.logRequest(request);
        dropButtons(callbackQuery);
    }


}
