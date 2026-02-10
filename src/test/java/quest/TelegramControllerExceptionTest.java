package quest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.telegram.telegrambots.meta.api.objects.*;
import spring.telegrambot.drinkWaterMeter.controller.TelegramController;
import spring.telegrambot.drinkWaterMeter.controller.config.TelegramUpdateExample;
import spring.telegrambot.drinkWaterMeter.service.TelegramWaterService;
import spring.telegrambot.drinkWaterMeter.service.update.CallbackQueryContract;
import spring.telegrambot.drinkWaterMeter.service.update.MessageContract;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TelegramControllerExceptionTest {

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


    //Не работает
//    @Test
//    void testHandleException_WithHttpMessageNotReadableException_CallsLogger() {
//        // Arrange
//        HttpMessageNotReadableException exception = new HttpMessageNotReadableException(
//            "Invalid JSON",
//               new RuntimeException("Parse error")
//        );
//
//        // Act
//        telegramController.handle(exception);
//
//        // Assert
//        verify(telegramService.logger(), times(1)).logException(
//            eq("400 Bad Request from Telegram : "),
//            eq(exception)
//        );
//    }


    //Как осуществлять проверку на ошибку в методе .handle()
    @Test
    void testHandleException_WithNullException_DoesNotThrow() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> telegramController.handle(null));
        // Метод должен обработать null без исключения
    }
}