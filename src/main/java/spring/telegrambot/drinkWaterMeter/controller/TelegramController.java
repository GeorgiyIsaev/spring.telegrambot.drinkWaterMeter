package spring.telegrambot.drinkWaterMeter.controller;

import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.objects.Update;
import spring.telegrambot.drinkWaterMeter.service.TelegramWaterService;
import spring.telegrambot.drinkWaterMeter.service.update.CallbackQueryContract;
import spring.telegrambot.drinkWaterMeter.service.update.MessageContract;


@RestController
@RequestMapping("/v1/api/telegram")
public class TelegramController {

    private final TelegramWaterService telegramService;


    public TelegramController(TelegramWaterService telegramService) {
        this.telegramService = telegramService;
    }

    @PostConstruct
    private void init(){
        telegramService.setWebhook();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(HttpMessageNotReadableException e) {
        telegramService.logger().logException("400 Bad Request from Telegram : " , e);
    }



    @PostMapping("/")
    public void postMethod(@RequestBody Update update) {
        if(update == null){
            return;
        }
        updateRouting(update);
    }


    public void updateRouting(Update update) {
        if(update.hasMessage()){
            MessageContract messageContract = new MessageContract(update);
            this.telegramService.replyToMessage(messageContract);

        } else if (update.hasCallbackQuery()) {
            CallbackQueryContract callbackQueryContract = new CallbackQueryContract(update);
            this.telegramService.replyToCallbackQuery(callbackQueryContract);

        }
    }



}