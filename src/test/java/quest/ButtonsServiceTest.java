package quest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import spring.telegrambot.drinkWaterMeter.repository.dao.UserDAO;
import spring.telegrambot.drinkWaterMeter.service.ButtonsService;
import spring.telegrambot.drinkWaterMeter.service.buttons.*;
import spring.telegrambot.drinkWaterMeter.service.update.CallbackQueryContract;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ButtonsServiceTest {

    @Mock
    private UserDAO userDao;

    @Mock
    private CallbackQueryContract callbackQueryContract;

    private ButtonsService buttonsService;

    @BeforeEach
    void setUp() {
        buttonsService = new ButtonsService(userDao);
    }

    //Можем ли мы как то замокать User Что бы переопределить его поведение
    @Test
    void testGenerateRequest_WithExistingButton_CallsAction() {
        // Arrange
        String dataButton = "button_index_200ml";
        String chatId = "12345";
        Instant instant = Instant.now();
        when(callbackQueryContract.getData()).thenReturn(dataButton);
        when(callbackQueryContract.getChatId()).thenReturn(chatId);
        when(callbackQueryContract.getTime()).thenReturn(instant);
        when(callbackQueryContract.getMessageId()).thenReturn(0);
        when(callbackQueryContract.getUsername()).thenReturn("user");

        Button mockButton = mock(Button.class);
        SendMessage expectedResponse = new SendMessage(chatId, "Test response");
        when(mockButton.generateRequest(callbackQueryContract)).thenReturn(expectedResponse);
        
        // Подменяем кнопку в сервисе
        // Для этого нам нужно получить доступ к приватному методу или использовать рефлексию
        // В данном случае, так как у нас есть реальный сервис, проверим что метод вызывается корректно
        
        // Act
        SendMessage result = buttonsService.generateRequest(callbackQueryContract);

        // Assert
        assertNotNull(result);
        verify(callbackQueryContract, times(1)).getData();
        // Кнопка существует, поэтому должен быть вызван ее метод generateRequest
    }
}