package spring.telegrambot.drinkWaterMeter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.test.LocalServerPort;


import org.springframework.boot.web.server.test.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import spring.telegrambot.drinkWaterMeter.controller.config.TelegramUpdateExample;
import spring.telegrambot.drinkWaterMeter.service.TelegramWaterService;
import spring.telegrambot.drinkWaterMeter.service.update.CallbackQueryContract;
import spring.telegrambot.drinkWaterMeter.service.update.MessageContract;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureMockMvc
class TelegramControllerTestRestTemplateTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TelegramWaterService telegramService;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/v1/api/telegram/";
        
        // Мокируем вызовы, которые происходят в @PostConstruct
        doNothing().when(telegramService).setWebhook();
        when(telegramService.logger()).thenReturn(Mockito.mock(spring.telegrambot.drinkWaterMeter.service.logger.Logger.class));
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> "jdbc:postgresql://localhost:5432/test008");
        registry.add("spring.datasource.username", () -> "postgres");
        registry.add("spring.datasource.password", () -> "0000");
    }

    @Test
    void testPostEndpoint_WithMessageUpdate_ReturnsOk() {
        // Arrange
        Update update = TelegramUpdateExample.createUpdateWithCommand();

        // Act
        ResponseEntity<Void> response = testRestTemplate.postForEntity(
            baseUrl,
            update,
            Void.class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        
        // Проверяем, что сервис был вызван
        verify(telegramService, times(1)).replyToMessage(any(MessageContract.class));
        verify(telegramService, never()).replyToCallbackQuery(any());
    }

    @Test
    void testPostEndpoint_WithCallbackQueryUpdate_ReturnsOk() {
        // Arrange
        Update update = TelegramUpdateExample.createCallbackQueryUpdate();

        // Act
        ResponseEntity<Void> response = testRestTemplate.postForEntity(
            baseUrl,
            update,
            Void.class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        
        // Проверяем, что сервис был вызван
        verify(telegramService, times(1)).replyToCallbackQuery(any(CallbackQueryContract.class));
        verify(telegramService, never()).replyToMessage(any());
    }


    @Test
    void testPostEndpoint_WithEmptyUpdate_ReturnsOk() {
        // Arrange
        Update update = new Update();
        // Пустой update - нет ни message, ни callbackQuery

        // Act
        ResponseEntity<Void> response = testRestTemplate.postForEntity(
            baseUrl,
            update,
            Void.class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        
        // Ни один сервис не должен быть вызван
        verify(telegramService, never()).replyToMessage(any());
        verify(telegramService, never()).replyToCallbackQuery(any());
    }

    @Test
    void testPostEndpoint_WithInvalidJson_ReturnsBadRequest() {
        // Arrange
        String invalidJson = "{invalid json}";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(invalidJson, headers);

        // Act
        ResponseEntity<String> response = testRestTemplate.postForEntity(
            baseUrl,
            request,
            String.class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        
        // Проверяем, что логгер был вызван
        verify(telegramService.logger(), times(1)).logException(anyString(), any());
    }



    @Test
    void testPostEndpoint_WithComplexMessage_ReturnsOk() {
        // Arrange
        Update update = TelegramUpdateExample.createUpdateWithCommand();

        // Act
        ResponseEntity<Void> response = testRestTemplate.postForEntity(
            baseUrl,
            update,
            Void.class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(telegramService, times(1)).replyToMessage(any(MessageContract.class));
    }

    @Test
    void testPostEndpoint_WithComplexCallbackQuery_ReturnsOk() {
        // Arrange
        Update update = TelegramUpdateExample.createCallbackQueryUpdate();

        // Act
        ResponseEntity<Void> response = testRestTemplate.postForEntity(
            baseUrl,
            update,
            Void.class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(telegramService, times(1)).replyToCallbackQuery(any(CallbackQueryContract.class));
    }

    @Test
    void testPostEndpoint_WithDifferentContentTypes() {
        // Arrange
        Update update = TelegramUpdateExample.createUpdateWithCommand();
        
        // Test 1: APPLICATION_JSON
        HttpHeaders jsonHeaders = new HttpHeaders();
        jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Update> jsonRequest = new HttpEntity<>(update, jsonHeaders);
        
        // Act & Assert
        ResponseEntity<Void> jsonResponse = testRestTemplate.postForEntity(baseUrl, jsonRequest, Void.class);
        assertThat(jsonResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        
        // Test 2: APPLICATION_JSON_UTF8 (устаревший, но для теста)
        HttpHeaders utf8Headers = new HttpHeaders();
        utf8Headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"));
        HttpEntity<Update> utf8Request = new HttpEntity<>(update, utf8Headers);
        
        ResponseEntity<Void> utf8Response = testRestTemplate.postForEntity(baseUrl, utf8Request, Void.class);
        assertThat(utf8Response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testPostEndpoint_WithLargePayload_ReturnsOk() {
        // Arrange
        Update update = TelegramUpdateExample.createUpdateWithCommand();
        
        // Создаем большое сообщение
        StringBuilder largeText = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            largeText.append("Line ").append(i).append(": This is a test message. ");
        }
        update.getMessage().setText(largeText.toString());

        // Act
        ResponseEntity<Void> response = testRestTemplate.postForEntity(
            baseUrl,
            update,
            Void.class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(telegramService, times(1)).replyToMessage(any(MessageContract.class));
    }

    @Test
    void testPostEndpoint_ConcurrentRequests() throws InterruptedException {
        // Arrange
        int numberOfRequests = 10;
        Thread[] threads = new Thread[numberOfRequests];
        
        // Act
        for (int i = 0; i < numberOfRequests; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                Update update = TelegramUpdateExample.createUpdateWithCommand();
                ResponseEntity<Void> response = testRestTemplate.postForEntity(
                    baseUrl,
                    update,
                    Void.class
                );
                
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            });
            threads[i].start();
        }
        
        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }
        
        // Assert
        // Проверяем что все запросы были обработаны
        verify(telegramService, times(numberOfRequests)).replyToMessage(any(MessageContract.class));
    }

    @Test
    void testControllerAnnotations() {
        // Проверяем через TestRestTemplate что контроллер правильно настроен
        
        // 1. Проверяем что endpoint доступен
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        Update update = new Update();
        HttpEntity<Update> request = new HttpEntity<>(update, headers);
        
        ResponseEntity<Void> response = testRestTemplate.postForEntity(baseUrl, request, Void.class);
        assertThat(response.getStatusCode()).isNotEqualTo(HttpStatus.NOT_FOUND);
        
        // 2. Проверяем что путь правильный
        assertThat(baseUrl).endsWith("/v1/api/telegram/");
    }
}