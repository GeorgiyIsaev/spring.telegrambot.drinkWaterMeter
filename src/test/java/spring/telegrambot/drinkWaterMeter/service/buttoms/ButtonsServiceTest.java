package spring.telegrambot.drinkWaterMeter.service.buttoms;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import spring.telegrambot.drinkWaterMeter.repository.dao.UserDAO;
import spring.telegrambot.drinkWaterMeter.service.ButtonsService;
import spring.telegrambot.drinkWaterMeter.service.buttons.*;
import spring.telegrambot.drinkWaterMeter.service.update.CallbackQueryContract;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

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

    @Test
    void testConstructor_InitializesButtons() {
        // Проверяем, что после создания сервиса кнопки созданы
        assertNotNull(buttonsService);
        
        // Проверяем наличие кнопок из разных категорий
        assertNotNull(buttonsService.getAction("button_index_200ml"));
        assertNotNull(buttonsService.getAction("button_weight_50"));
        assertNotNull(buttonsService.getAction("button_height_180"));
        assertNotNull(buttonsService.getAction("button_drop_yes"));
        assertNotNull(buttonsService.getAction("button_drop_no"));
        assertNotNull(buttonsService.getAction("button_woman"));
        assertNotNull(buttonsService.getAction("button_man"));
        assertNotNull(buttonsService.getAction("button_time_0"));
    }

    @Test
    void testGetAction_WithExistingButton_ReturnsButton() {
        // Act
        Button result = buttonsService.getAction("button_index_200ml");

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof DrinkWaterButton);
    }

    @Test
    void testGetAction_WithNonExistingButton_ReturnsNull() {
        // Act
        Button result = buttonsService.getAction("non_existing_button");

        // Assert
        assertNull(result);
    }

    @Test
    void testGetAction_WithNullInput_ReturnsNull() {
        // Act
        Button result = buttonsService.getAction(null);

        // Assert
        assertNull(result);
    }

    @Test
    void testGetAction_CaseInsensitive() {
        // Act
        Button lowerCase = buttonsService.getAction("button_index_200ml");
        Button upperCase = buttonsService.getAction("BUTTON_INDEX_200ML");
        Button mixedCase = buttonsService.getAction("Button_Index_200Ml");

        // Assert
        assertNotNull(lowerCase);
        assertNotNull(upperCase);
        assertNotNull(mixedCase);
        assertEquals(lowerCase, upperCase);
        assertEquals(lowerCase, mixedCase);
    }
//
//    @Test
//    void testGenerateRequest_WithExistingButton_CallsAction() {
//        // Arrange
//        String dataButton = "button_index_200ml";
//        String chatId = "12345";
//        Instant instant = Instant.now();
//        when(callbackQueryContract.getData()).thenReturn(dataButton);
//        when(callbackQueryContract.getChatId()).thenReturn(chatId);
//        when(callbackQueryContract.getTime()).thenReturn(instant);
//        when(callbackQueryContract.getMessageId()).thenReturn(0);
//        when(callbackQueryContract.getUsername()).thenReturn("user");
//
//        Button mockButton = mock(Button.class);
//        SendMessage expectedResponse = new SendMessage(chatId, "Test response");
//        when(mockButton.generateRequest(callbackQueryContract)).thenReturn(expectedResponse);
//
//        // Подменяем кнопку в сервисе
//        // Для этого нам нужно получить доступ к приватному методу или использовать рефлексию
//        // В данном случае, так как у нас есть реальный сервис, проверим что метод вызывается корректно
//
//        // Act
//        SendMessage result = buttonsService.generateRequest(callbackQueryContract);
//
//        // Assert
//        assertNotNull(result);
//        verify(callbackQueryContract, times(1)).getData();
//        // Кнопка существует, поэтому должен быть вызван ее метод generateRequest
//    }

    @Test
    void testGenerateRequest_WithNonExistingButton_ReturnsErrorMessage() {
        // Arrange
        String nonExistingButton = "non_existing_button";
        String chatId = "12345";
        
        when(callbackQueryContract.getData()).thenReturn(nonExistingButton);
        when(callbackQueryContract.getChatId()).thenReturn(chatId);

        // Act
        SendMessage result = buttonsService.generateRequest(callbackQueryContract);

        // Assert
        assertNotNull(result);
        assertEquals(chatId, result.getChatId());
        assertTrue(result.getText().contains("не реализованна"));
        assertTrue(result.getText().contains(nonExistingButton));
    }

    @Test
    void testGenerateRequest_WithNullCallbackQuery_ThrowsException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            buttonsService.generateRequest(null);
        });
    }

    @Test
    void testAddButtonsDrinkWater_CreatesCorrectRange() {
        // Проверяем, что создаются кнопки от 200 до 600 мл с шагом 50
        for (int ml = 200; ml <= 600; ml += 50) {
            String key = "button_index_" + ml + "ml";
            Button button = buttonsService.getAction(key);
            assertNotNull(button, "Кнопка для " + ml + " мл должна существовать");
            assertTrue(button instanceof DrinkWaterButton);
        }
        
        // Проверяем граничные значения
        assertNull(buttonsService.getAction("button_index_199ml"));
        assertNull(buttonsService.getAction("button_index_601ml"));
    }

    @Test
    void testAddButtonsWeight_CreatesCorrectRanges() {
        // Проверяем вес от 40 до 190 кг с шагом 5
        for (int kg = 40; kg <= 190; kg += 5) {
            String key = "button_weight_" + kg;
            Button button = buttonsService.getAction(key);
            assertNotNull(button, "Кнопка для веса " + kg + " кг должна существовать");
            assertTrue(button instanceof WeightButton);
        }
        
        // Проверяем рост от 80 до 250 см с шагом 5
        for (int cm = 80; cm <= 250; cm += 5) {
            String key = "button_height_" + cm;
            Button button = buttonsService.getAction(key);
            assertNotNull(button, "Кнопка для роста " + cm + " см должна существовать");
            assertTrue(button instanceof HeightButton);
        }
    }

    @Test
    void testAddButtonsTime_CreatesCorrectRange() {
        // Проверяем время от -12 до 14 с шагом 1
        for (int time = -12; time <= 14; time += 1) {
            String key = "button_time_" + time;
            Button button = buttonsService.getAction(key);
            assertNotNull(button, "Кнопка для времени " + time + " должна существовать");
            assertTrue(button instanceof TimeButton);
        }
    }

    @Test
    void testDropButtons_CreatedCorrectly() {
        // Проверяем кнопки для удаления
        Button dropNo = buttonsService.getAction("button_drop_no");
        Button dropYes = buttonsService.getAction("button_drop_yes");
        
        assertNotNull(dropNo);
        assertNotNull(dropYes);
        assertTrue(dropNo instanceof DropButtonNo);
        assertTrue(dropYes instanceof DropButtonYes);
    }

    @Test
    void testSexButtons_CreatedCorrectly() {
        // Проверяем кнопки выбора пола
        Button womanButton = buttonsService.getAction("button_woman");
        Button manButton = buttonsService.getAction("button_man");
        
        assertNotNull(womanButton);
        assertNotNull(manButton);
        assertTrue(womanButton instanceof SexButton);
        assertTrue(manButton instanceof SexButton);
    }

    @Test
    void testPrivateFieldButton_InitializedCorrectly() throws Exception {
        // Используем рефлексию для проверки приватного поля
        Field buttonField = ButtonsService.class.getDeclaredField("button");
        buttonField.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<String, Button> buttonMap = (Map<String, Button>) buttonField.get(buttonsService);

        assertNotNull(buttonMap);
        assertTrue(buttonMap instanceof TreeMap);

        // Проверяем, что карта не пустая
        assertFalse(buttonMap.isEmpty());

        // Проверяем чувствительность к регистру
        Button lower = buttonMap.get("button_index_200ml");
        Button upper = buttonMap.get("BUTTON_INDEX_200ML");
        assertNotNull(lower);
        assertNotNull(upper);
        assertEquals(lower, upper);
    }
}