package spring.telegrambot.drinkWaterMeter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.objects.Update;
import spring.telegrambot.drinkWaterMeter.logger.Logger;
import spring.telegrambot.drinkWaterMeter.service.TelegramWoterService;


@RestController
@RequestMapping("/v1/api/telegram")
public class TelegramController {

    private final TelegramWoterService telegramService;
    public TelegramController(TelegramWoterService telegramService) {
        this.telegramService = telegramService;
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(HttpMessageNotReadableException e) {
        System.out.println("400 Bad Request from Telegram : "  + e.getMessage());
    }



    @PostMapping("/")
    public void postMethod(@RequestBody Update update) {
        if(update.hasMessage()){
            String chatId = update.getMessage().getChatId().toString();
            String text = update.getMessage().getText();

            System.out.println(chatId + " " + text);
            telegramService.sendMessage(chatId,text);
        }
    }
}