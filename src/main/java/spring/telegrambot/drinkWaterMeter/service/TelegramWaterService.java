package spring.telegrambot.drinkWaterMeter.service;

import feign.FeignException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import spring.telegrambot.drinkWaterMeter.client.TelegramFeignClient;
import spring.telegrambot.drinkWaterMeter.client.contract.request.Request;
import spring.telegrambot.drinkWaterMeter.client.contract.webhook.SetWebhookRequest;

import spring.telegrambot.drinkWaterMeter.service.logger.Logger;
import spring.telegrambot.drinkWaterMeter.service.logger.LoggerService;
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
            LoggerService logger,
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
        logger.logMessage(message);
        SendMessage sendMessage = commandsService.generateRequest(message);
        Request request = telegramFeignClient.sendMessage(sendMessage);
        logger.logRequest(request);
    }

    public void dropButtons(CallbackQuery callbackQuery){
        Integer messageId = callbackQuery.getMessageId();
        String chatId = callbackQuery.getChatId();
        DeleteMessage deleteMessage = DeleteMessage.builder().chatId(chatId).messageId(messageId).build();
        try {
            String requestDelete = telegramFeignClient.deleteMessage(deleteMessage);
            logger.logDelete(requestDelete);
        }
        catch (FeignException e){
            logger.logException("Ошибка удаления кнопки!", e);
        }

    }


    public void replyToCallbackQuery(CallbackQuery callbackQuery) {
        logger.logCallbackQuery(callbackQuery);
        SendMessage sendMessage = buttonsService.generateRequest(callbackQuery);
        Request request = telegramFeignClient.sendMessage(sendMessage);
        logger.logRequest(request);
        dropButtons(callbackQuery);
    }


}
