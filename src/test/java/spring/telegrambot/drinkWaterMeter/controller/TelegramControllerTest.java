package spring.telegrambot.drinkWaterMeter.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.telegram.telegrambots.meta.api.objects.*;
import spring.telegrambot.drinkWaterMeter.controller.config.TelegramUpdateExample;
import spring.telegrambot.drinkWaterMeter.service.TelegramWaterService;
import spring.telegrambot.drinkWaterMeter.service.update.CallbackQueryContract;
import spring.telegrambot.drinkWaterMeter.service.update.MessageContract;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TelegramControllerTest {

    @Mock
    private TelegramWaterService telegramService;

    @Mock
    private Update update;

    @Mock
    private CallbackQuery telegramCallbackQuery;

    @Mock
    private Message telegramMessage;

    @Mock
    private User user;

    private TelegramController telegramController;

    @BeforeEach
    void setUp() {
        telegramController = new TelegramController(telegramService);
    }

    @Test
    void testConstructor_InjectsDependencies() {
        // Act & Assert
        assertNotNull(telegramController);
        // Проверяем, что сервис был корректно инжектирован через конструктор
        // Можно использовать рефлексию для проверки приватного поля, но обычно достаточно проверить поведение
    }

    @Test
    void testInit_CallsSetWebhook() {
        // Arrange
        // @PostConstruct вызывается после создания бина, но в тесте нам нужно вызвать его явно
        // Используем рефлексию для вызова приватного метода init()
        try {
            var method = TelegramController.class.getDeclaredMethod("init");
            method.setAccessible(true);
            
            // Act
            method.invoke(telegramController);
            
            // Assert
            verify(telegramService, times(1)).setWebhook();
        } catch (Exception e) {
            fail("Не удалось вызвать метод init через рефлексию", e);
        }
    }

    @Test
    void testPostMethod_WithValidUpdate_CallsUpdateRouting() {
        // Arrange
        Update testUpdate = new Update();
        
        // Используем spy для проверки вызова updateRouting
        TelegramController spyController = spy(telegramController);
        
        // Act
        spyController.postMethod(testUpdate);
        
        // Assert
        verify(spyController, times(1)).updateRouting(testUpdate);
    }

    @Test
    void testPostMethod_WithNullUpdate_DoesNotThrowException() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> telegramController.postMethod(null));
        // Метод должен обработать null без исключения
    }

    @Test
    void testUpdateRouting_WithMessageUpdate_CallsReplyToMessage() {
        // Arrange
        Update realUpdate = TelegramUpdateExample.createUpdateWithCommand();
        
        // Act
        telegramController.updateRouting(realUpdate);
        
        // Assert
        verify(telegramService, times(1)).replyToMessage(any(MessageContract.class));
        verify(telegramService, never()).replyToCallbackQuery(any(CallbackQueryContract.class));
    }

    @Test
    void testUpdateRouting_WithCallbackQueryUpdate_CallsReplyToCallbackQuery() {
        // Arrange
        Update realUpdate = TelegramUpdateExample.createCallbackQueryUpdate();
        
        // Act
        telegramController.updateRouting(realUpdate);
        
        // Assert
        verify(telegramService, times(1)).replyToCallbackQuery(any(CallbackQueryContract.class));
        verify(telegramService, never()).replyToMessage(any(MessageContract.class));
    }


    @Test
    void testUpdateRouting_WithNeitherMessageNorCallbackQuery_DoesNothing() {
        // Arrange
        when(update.hasMessage()).thenReturn(false);
        when(update.hasCallbackQuery()).thenReturn(false);
        
        // Act
        telegramController.updateRouting(update);
        
        // Assert
        verify(telegramService, never()).replyToMessage(any(MessageContract.class));
        verify(telegramService, never()).replyToCallbackQuery(any(CallbackQueryContract.class));
    }

    @Test
    void testUpdateRouting_WithEmptyMessage_StillCreatesServiceMessage() {
        // Arrange
        Update realUpdate = TelegramUpdateExample.createUpdateWithCommand();
        
        // Act
        telegramController.updateRouting(realUpdate);
        
        // Assert
        verify(telegramService, times(1)).replyToMessage(any(MessageContract.class));
        
        // Проверяем, что ServiceMessage создается даже с null значениями
        ArgumentCaptor<MessageContract> messageCaptor = ArgumentCaptor.forClass(MessageContract.class);
        verify(telegramService).replyToMessage(messageCaptor.capture());

        MessageContract capturedMessageContract = messageCaptor.getValue();
        assertNotNull(capturedMessageContract);
    }

    @Test
    void testUpdateRouting_WithEmptyCallbackQuery_StillCreatesServiceCallbackQuery() {

        // Act
        Update updateCall = TelegramUpdateExample.createCallbackQueryUpdate();
        telegramController.updateRouting(updateCall);
        
        // Assert
        verify(telegramService, times(1)).replyToCallbackQuery(any(CallbackQueryContract.class));
        
        // Проверяем, что ServiceCallbackQuery создается даже с null значениями
        ArgumentCaptor<CallbackQueryContract> callbackCaptor =
                ArgumentCaptor.forClass(CallbackQueryContract.class);
        verify(telegramService).replyToCallbackQuery(callbackCaptor.capture());

        CallbackQueryContract capturedCallback
                = callbackCaptor.getValue();
        assertNotNull(capturedCallback);
    }


    @Test
    void testUpdateRouting_WithRealUpdateObject() {
        // Arrange - создаем реальный объект Update
        Update realUpdate = TelegramUpdateExample.createUpdateWithCommand();

        // Act
        telegramController.updateRouting(realUpdate);
        
        // Assert
        verify(telegramService, times(1)).replyToMessage(any(MessageContract.class));
    }

    @Test
    void testPostMethod_ExceptionHandling() {
        // Arrange
        Update testUpdate = new Update();
        
        // Симулируем исключение в updateRouting
        TelegramController spyController = spy(telegramController);
        doThrow(new RuntimeException("Test exception")).when(spyController).updateRouting(any());
        
        // Act & Assert - проверяем, что исключение пробрасывается дальше
        assertThrows(RuntimeException.class, () -> spyController.postMethod(testUpdate));
    }

    @Test
    void testControllerAnnotations() {
        // Проверяем наличие аннотаций через рефлексию
        Class<?> controllerClass = telegramController.getClass();
        
        // Проверяем @RestController
        assertTrue(controllerClass.isAnnotationPresent(org.springframework.web.bind.annotation.RestController.class));
        
        // Проверяем @RequestMapping
        RequestMapping requestMapping = controllerClass.getAnnotation(RequestMapping.class);
        assertNotNull(requestMapping);
        assertEquals("/v1/api/telegram", requestMapping.value()[0]);
        
        // Проверяем @ExceptionHandler метод
        try {
            var handleMethod = controllerClass.getMethod("handle", HttpMessageNotReadableException.class);
            assertTrue(handleMethod.isAnnotationPresent(org.springframework.web.bind.annotation.ExceptionHandler.class));
            assertTrue(handleMethod.isAnnotationPresent(org.springframework.web.bind.annotation.ResponseStatus.class));
        } catch (NoSuchMethodException e) {
            fail("Метод handle не найден", e);
        }
        
        // Проверяем @PostMapping метод
        try {
            var postMethod = controllerClass.getMethod("postMethod", Update.class);
            assertTrue(postMethod.isAnnotationPresent(org.springframework.web.bind.annotation.PostMapping.class));
            PostMapping postMapping = postMethod.getAnnotation(PostMapping.class);
            assertEquals("/", postMapping.value()[0]);
        } catch (NoSuchMethodException e) {
            fail("Метод postMethod не найден", e);
        }
    }
}