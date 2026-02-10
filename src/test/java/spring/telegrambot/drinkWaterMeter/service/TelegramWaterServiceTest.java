package spring.telegrambot.drinkWaterMeter.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import spring.telegrambot.drinkWaterMeter.client.TelegramFeignClient;
import spring.telegrambot.drinkWaterMeter.service.logger.LoggerService;
import spring.telegrambot.drinkWaterMeter.service.update.MessageContract;

import java.time.Instant;

@SpringBootTest
class TelegramWaterServiceTest {
    @Autowired
    private TelegramWaterService telegramWaterService;

    @MockitoBean
    private TelegramFeignClient telegramFeignClient;
    @MockitoBean
    private LoggerService logger;


    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> "jdbc:postgresql://localhost:5432/test003");
        registry.add("spring.datasource.username", () -> "postgres");
        registry.add("spring.datasource.password", () -> "0000");
    }

    @Test
    void setWebhook() {

    }

    @Test
    void logger() {
    }

    @Test
    void replyToMessage() {
        MessageContract messageContract = new MessageContract();
        messageContract.setChatId("1");
        messageContract.setText("/help");
        messageContract.setMessageId(1);
        messageContract.setTime(Instant.now());
        messageContract.setUsername("fwe3f");

        telegramWaterService.replyToMessage(messageContract);
        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramFeignClient).sendMessage(captor.capture());

        SendMessage sendMessageCaptor = captor.getValue();
        Assertions.assertEquals("2",sendMessageCaptor.getChatId());

    }

    @Test
    void dropButtons() {
    }

    @Test
    void replyToCallbackQuery() {
    }
}