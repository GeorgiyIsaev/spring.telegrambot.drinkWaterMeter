package spring.telegrambot.drinkWaterMeter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.objects.Update;
import spring.telegrambot.drinkWaterMeter.service.TelegramWaterService;


@RestController
@RequestMapping("/v1/api/telegram")
public class TelegramController {

    private final TelegramWaterService telegramService;


    public TelegramController(TelegramWaterService telegramService) {
        this.telegramService = telegramService;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(HttpMessageNotReadableException e) {
        telegramService.logger().logException("400 Bad Request from Telegram : " , e);
    }



    @PostMapping("/")
    public void postMethod(@RequestBody Update update) {
        telegramService.updateRouting(update);
    }
}