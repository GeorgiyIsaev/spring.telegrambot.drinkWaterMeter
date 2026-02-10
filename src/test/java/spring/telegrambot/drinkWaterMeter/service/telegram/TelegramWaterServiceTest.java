package spring.telegrambot.drinkWaterMeter.service.telegram;

import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import spring.telegrambot.drinkWaterMeter.client.TelegramFeignClient;
import spring.telegrambot.drinkWaterMeter.client.contract.request.Chat;
import spring.telegrambot.drinkWaterMeter.client.contract.request.Request;
import spring.telegrambot.drinkWaterMeter.client.contract.request.Result;
import spring.telegrambot.drinkWaterMeter.client.contract.webhook.SetWebhookRequest;
import spring.telegrambot.drinkWaterMeter.service.ButtonsService;
import spring.telegrambot.drinkWaterMeter.service.CommandsService;
import spring.telegrambot.drinkWaterMeter.service.TelegramWaterService;
import spring.telegrambot.drinkWaterMeter.service.logger.Logger;
import spring.telegrambot.drinkWaterMeter.service.logger.LoggerService;
import spring.telegrambot.drinkWaterMeter.service.update.CallbackQueryContract;
import spring.telegrambot.drinkWaterMeter.service.update.MessageContract;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TelegramWaterServiceTest {

    @Mock
    private TelegramFeignClient telegramFeignClient;

    @Mock
    private LoggerService logger;

    @Mock
    private CommandsService commandsService;

    @Mock
    private ButtonsService buttonsService;

    @Mock
    private MessageContract messageContract;
    @Mock
    private CallbackQueryContract callbackQueryContract;


    private TelegramWaterService telegramWaterService;
    private final String testUrlServer = "https://test-server.com/webhook";

    @BeforeEach
    void setUp() {
        telegramWaterService = new TelegramWaterService(
                testUrlServer,
                telegramFeignClient,
                logger,
                buttonsService,
                commandsService
        );
    }

    @Test
    void testConstructor_InjectsDependenciesAndPrintsUrl() {
        // Assert
        assertNotNull(telegramWaterService);
        
        // Проверяем что URL был выведен в консоль (можно проверить через System.out если нужно)
        // Для простоты проверяем что объект создан
        String actualUrl = (String) ReflectionTestUtils.getField(telegramWaterService, "urlServer");
        assertEquals(testUrlServer, actualUrl);
    }

    @Test
    void testConstructor_WithEmptyUrl_InitializesService() {
        // Arrange & Act
        TelegramWaterService service = new TelegramWaterService(
                "",
                telegramFeignClient,
                logger,
                buttonsService,
                commandsService
        );

        // Assert
        assertNotNull(service);
        /// /// Безсмсленный тест
    }

    @Test
    void testSetWebhook_Successful() {
        // Arrange
        String expectedResponse = "Webhook set successfully";
        when(telegramFeignClient.setWebhook(any(SetWebhookRequest.class))).thenReturn(expectedResponse);

        // Act
        telegramWaterService.setWebhook();

        // Assert
        verify(telegramFeignClient, times(1)).setWebhook(any(SetWebhookRequest.class));
        verify(logger, times(1)).logResponse(expectedResponse);
        
        // Проверяем что SetWebhookRequest создан с правильным URL
        ArgumentCaptor<SetWebhookRequest> requestCaptor = ArgumentCaptor.forClass(SetWebhookRequest.class);
        verify(telegramFeignClient).setWebhook(requestCaptor.capture());
        
        SetWebhookRequest capturedRequest = requestCaptor.getValue();
        assertNotNull(capturedRequest);
        assertEquals(testUrlServer, capturedRequest.url());
    }

    /// /// Какая то ошибка с игнорированием тест, что это значит
    @Test
    void testSetWebhook_WithFeignException_LogsError() {
        // Arrange
        FeignException feignException = mock(FeignException.class);
        when(telegramFeignClient.setWebhook(any(SetWebhookRequest.class))).thenThrow(feignException);

        // Act
        telegramWaterService.setWebhook();

        // Assert
        verify(telegramFeignClient, times(1)).setWebhook(any(SetWebhookRequest.class));
        // Проверяем что исключение не пробрасывается дальше
        // Метод должен обработать исключение внутри Feign клиента
    }

    @Test
    void testLogger_ReturnsLoggerInstance() {
        // Act
        Logger returnedLogger = telegramWaterService.logger();

        // Assert
        assertNotNull(returnedLogger);
        assertSame(logger, returnedLogger);
    }

    @Test
    void testReplyToMessage_Successful() {
        // Arrange
        SendMessage sendMessage = new SendMessage("12345", "Test message");
        Request expectedRequest = new Request("true", new Result("Message sent",new Chat("12345", "test")));
        
        when(commandsService.generateRequest(messageContract)).thenReturn(sendMessage);
        when(telegramFeignClient.sendMessage(sendMessage)).thenReturn(expectedRequest);

        // Act
        telegramWaterService.replyToMessage(messageContract);

        // Assert
        verify(logger, times(1)).logMessage(messageContract);
        verify(commandsService, times(1)).generateRequest(messageContract);
        verify(telegramFeignClient, times(1)).sendMessage(sendMessage);
        verify(logger, times(1)).logRequest(expectedRequest);
    }

//    @Test
//    void testReplyToMessage_WithNullMessage_ThrowsException() {
//        // Act & Assert
//        assertThrows(NullPointerException.class, () -> telegramWaterService.replyToMessage(null));
//    }

    @Test
    void testReplyToMessage_WhenGenerateRequestReturnsNull() {
        // Arrange
        when(commandsService.generateRequest(messageContract)).thenReturn(null);

        // Act
        telegramWaterService.replyToMessage(messageContract);

        // Assert
        verify(logger, times(1)).logMessage(messageContract);
        verify(commandsService, times(1)).generateRequest(messageContract);
        verify(telegramFeignClient, never()).sendMessage(any(SendMessage.class));
        verify(logger, never()).logRequest(any(Request.class));
    }

    @Test
    void testReplyToMessage_FeignClientThrowsException() {
        // Arrange
        SendMessage sendMessage = new SendMessage("12345", "Test message");
        FeignException feignException = mock(FeignException.class);
        
        when(commandsService.generateRequest(messageContract)).thenReturn(sendMessage);
        when(telegramFeignClient.sendMessage(sendMessage)).thenThrow(feignException);

        // Act
        telegramWaterService.replyToMessage(messageContract);

        // Assert
        verify(logger, times(1)).logMessage(messageContract);
        verify(commandsService, times(1)).generateRequest(messageContract);
        verify(telegramFeignClient, times(1)).sendMessage(sendMessage);
        // Исключение должно быть обработано внутри Feign клиента
    }

    @Test
    void testReplyToCallbackQuery_Successful() {
        // Arrange
        SendMessage sendMessage = new SendMessage("12345", "Button pressed");
        Request expectedRequest = new Request("true", new Result("Callback processed",new Chat("12345", "test")));

        
        when(buttonsService.generateRequest(callbackQueryContract)).thenReturn(sendMessage);
        when(telegramFeignClient.sendMessage(sendMessage)).thenReturn(expectedRequest);
        when(callbackQueryContract.getMessageId()).thenReturn(100);
        when(callbackQueryContract.getChatId()).thenReturn("12345");

        // Act
        telegramWaterService.replyToCallbackQuery(callbackQueryContract);

        // Assert
        verify(logger, times(1)).logCallbackQuery(callbackQueryContract);
        verify(buttonsService, times(1)).generateRequest(callbackQueryContract);
        verify(telegramFeignClient, times(1)).sendMessage(sendMessage);
        verify(logger, times(1)).logRequest(expectedRequest);
        // Проверяем что dropButtons также вызывается
        verify(telegramFeignClient, times(1)).deleteMessage(any(DeleteMessage.class));
    }

    @Test
    void testReplyToCallbackQuery_WithNullCallbackQuery_ThrowsException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> telegramWaterService.replyToCallbackQuery(null));
    }

    @Test
    void testReplyToCallbackQuery_WhenGenerateRequestReturnsNull() {
        // Arrange
        when(buttonsService.generateRequest(callbackQueryContract)).thenReturn(null);

        // Act
        telegramWaterService.replyToCallbackQuery(callbackQueryContract);

        // Assert
        verify(logger, times(1)).logCallbackQuery(callbackQueryContract);
        verify(buttonsService, times(1)).generateRequest(callbackQueryContract);
        verify(telegramFeignClient, never()).sendMessage(any(SendMessage.class));
        verify(logger, never()).logRequest(any(Request.class));
        // dropButtons все равно должен вызваться
        verify(telegramFeignClient, times(1)).deleteMessage(any(DeleteMessage.class));
    }

    @Test
    void testDropButtons_Successful() {
        // Arrange
        Integer messageId = 100;
        String chatId = "12345";
        String deleteResponse = "Message deleted";
        
        when(callbackQueryContract.getMessageId()).thenReturn(messageId);
        when(callbackQueryContract.getChatId()).thenReturn(chatId);
        when(telegramFeignClient.deleteMessage(any(DeleteMessage.class))).thenReturn(deleteResponse);

        // Act
        telegramWaterService.dropButtons(callbackQueryContract);

        // Assert
        verify(telegramFeignClient, times(1)).deleteMessage(any(DeleteMessage.class));
        verify(logger, times(1)).logDelete(deleteResponse);
        
        // Проверяем что DeleteMessage создан с правильными параметрами
        ArgumentCaptor<DeleteMessage> deleteMessageCaptor = ArgumentCaptor.forClass(DeleteMessage.class);
        verify(telegramFeignClient).deleteMessage(deleteMessageCaptor.capture());
        
        DeleteMessage capturedDeleteMessage = deleteMessageCaptor.getValue();
        assertEquals(chatId, capturedDeleteMessage.getChatId());
        assertEquals(messageId, capturedDeleteMessage.getMessageId());
    }

    @Test
    void testDropButtons_WithFeignException_LogsError() {
        // Arrange
        Integer messageId = 100;
        String chatId = "12345";
        FeignException feignException = mock(FeignException.class);
        
        when(callbackQueryContract.getMessageId()).thenReturn(messageId);
        when(callbackQueryContract.getChatId()).thenReturn(chatId);
        when(telegramFeignClient.deleteMessage(any(DeleteMessage.class))).thenThrow(feignException);

        // Act
        telegramWaterService.dropButtons(callbackQueryContract);

        // Assert
        verify(telegramFeignClient, times(1)).deleteMessage(any(DeleteMessage.class));
        verify(logger, times(1)).logException(eq("Ошибка удаления кнопки!"), eq(feignException));
        verify(logger, never()).logDelete(anyString());
    }

    @Test
    void testDropButtons_WithNullCallbackQuery_ThrowsException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> telegramWaterService.dropButtons(null));
    }

    @Test
    void testDropButtons_WithNullMessageId() {
        // Arrange
        when(callbackQueryContract.getMessageId()).thenReturn(null);
        when(callbackQueryContract.getChatId()).thenReturn("12345");

        // Act
        telegramWaterService.dropButtons(callbackQueryContract);

        // Assert
        // DeleteMessage должен быть создан даже с null messageId
        verify(telegramFeignClient, times(1)).deleteMessage(any(DeleteMessage.class));
    }

    @Test
    void testDropButtons_WithNullChatId() {
        // Arrange
        when(callbackQueryContract.getMessageId()).thenReturn(100);
        when(callbackQueryContract.getChatId()).thenReturn(null);

        // Act
        telegramWaterService.dropButtons(callbackQueryContract);

        // Assert
        // DeleteMessage должен быть создан даже с null chatId
        verify(telegramFeignClient, times(1)).deleteMessage(any(DeleteMessage.class));
    }

    @Test
    void testServiceAnnotation() {
        // Проверяем что класс помечен как @Service
        assertTrue(TelegramWaterService.class.isAnnotationPresent(org.springframework.stereotype.Service.class));
    }

    @Test
    void testIntegrationFlow_MessageAndCallback() {
        // Комплексный тест полного потока
        
        // 1. Настройка webhook
        when(telegramFeignClient.setWebhook(any(SetWebhookRequest.class))).thenReturn("OK");
        telegramWaterService.setWebhook();
        
        // 2. Обработка сообщения
        SendMessage messageResponse = new SendMessage("123", "Hello");

        Request messageRequest = new Request("true", new Result("sent",new Chat("12345", "test")));
        when(commandsService.generateRequest(messageContract)).thenReturn(messageResponse);
        when(telegramFeignClient.sendMessage(messageResponse)).thenReturn(messageRequest);
        
        telegramWaterService.replyToMessage(messageContract);
        
        // 3. Обработка callback query
        SendMessage callbackResponse = new SendMessage("123", "Button clicked");
        Request callbackRequest = new Request("true", new Result("Callback sent",new Chat("12345", "test")));


        when(buttonsService.generateRequest(callbackQueryContract)).thenReturn(callbackResponse);
        when(telegramFeignClient.sendMessage(callbackResponse)).thenReturn(callbackRequest);
        when(callbackQueryContract.getMessageId()).thenReturn(100);
        when(callbackQueryContract.getChatId()).thenReturn("123");
        when(telegramFeignClient.deleteMessage(any(DeleteMessage.class))).thenReturn("Deleted");
        
        telegramWaterService.replyToCallbackQuery(callbackQueryContract);
        
        // Проверка вызовов
        verify(telegramFeignClient, times(1)).setWebhook(any(SetWebhookRequest.class));
        verify(commandsService, times(1)).generateRequest(messageContract);
        verify(buttonsService, times(1)).generateRequest(callbackQueryContract);
        verify(telegramFeignClient, times(2)).sendMessage(any(SendMessage.class));
        verify(telegramFeignClient, times(1)).deleteMessage(any(DeleteMessage.class));
        verify(logger, times(1)).logResponse("OK");
        verify(logger, times(1)).logMessage(messageContract);
        verify(logger, times(1)).logCallbackQuery(callbackQueryContract);
        verify(logger, times(2)).logRequest(any(Request.class));
        verify(logger, times(1)).logDelete("Deleted");
    }

    @Test
    void testEdgeCases() {
        // Тестирование граничных случаев
        
        // 1. Очень длинный URL
        String longUrl = "https://" + "a".repeat(1000) + ".com/webhook";
        TelegramWaterService serviceWithLongUrl = new TelegramWaterService(
                longUrl,
                telegramFeignClient,
                logger,
                buttonsService,
                commandsService
        );
        assertNotNull(serviceWithLongUrl);
        
        // 2. Специальные символы в URL
        TelegramWaterService serviceWithSpecialChars = new TelegramWaterService(
                "https://test.com:8080/webhook?token=abc123&param=test%20value",
                telegramFeignClient,
                logger,
                buttonsService,
                commandsService
        );
        assertNotNull(serviceWithSpecialChars);
    }

    @Test
    void testConcurrentAccess() throws InterruptedException {
        // Тест на потокобезопасность (если требуется)
        // Arrange
        SendMessage sendMessage = new SendMessage("123", "Test");
        Request request = new Request("true", new Result("OK",new Chat("12345", "test")));
        when(commandsService.generateRequest(any(MessageContract.class))).thenReturn(sendMessage);
        when(telegramFeignClient.sendMessage(sendMessage)).thenReturn(request);
        
        // Act - запускаем несколько потоков
        int threadCount = 10;
        Thread[] threads = new Thread[threadCount];
        
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                MessageContract msg = mock(MessageContract.class);
                telegramWaterService.replyToMessage(msg);
            });
            threads[i].start();
        }
        
        // Ждем завершения всех потоков
        for (Thread thread : threads) {
            thread.join();
        }
        
        // Assert
        verify(telegramFeignClient, times(threadCount)).sendMessage(sendMessage);
        verify(logger, times(threadCount)).logMessage(any(MessageContract.class));
    }
}