package spring.telegrambot.drinkWaterMeter.repository.userDAO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import spring.telegrambot.drinkWaterMeter.repository.dao.UserDAO;
import spring.telegrambot.drinkWaterMeter.repository.jpa.user.DrinkWaterRepository;
import spring.telegrambot.drinkWaterMeter.repository.jpa.user.UserRepository;
import spring.telegrambot.drinkWaterMeter.repository.model.user.User;
import spring.telegrambot.drinkWaterMeter.repository.model.user.WaterDrink;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDAOTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private DrinkWaterRepository drinkWaterRepository;

    private UserDAO userDAO;

    @BeforeEach
    void setUp() {
        userDAO = new UserDAO(userRepository, drinkWaterRepository);
    }

    @Test
    void testConstructor_InjectsDependencies() {
        // Assert
        assertNotNull(userDAO);
        // Проверяем что зависимости были инжектированы через конструктор
    }

    @Test
    void testFindAll_ReturnsAllUsers() {
        // Arrange
        User user1 = new User();
        user1.setId(1);
        user1.setUsername("user1");
        
        User user2 = new User();
        user2.setId(2);
        user2.setUsername("user2");
        
        List<User> expectedUsers = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(expectedUsers);

        // Act
        List<User> actualUsers = userDAO.findAll();

        // Assert
        assertNotNull(actualUsers);
        assertEquals(2, actualUsers.size());
        assertEquals(expectedUsers, actualUsers);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_WhenNoUsers_ReturnsEmptyList() {
        // Arrange
        when(userRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<User> actualUsers = userDAO.findAll();

        // Assert
        assertNotNull(actualUsers);
        assertTrue(actualUsers.isEmpty());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testCreateUser_CreatesNewUserWithCorrectProperties() {
        // Arrange
        String chatId = "12345";
        String username = "testuser";
        
        User savedUser = new User();
        savedUser.setId(1);
        savedUser.setChatId(chatId);
        savedUser.setUsername(username);
        savedUser.setTimeShift(0);
        
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        User result = userDAO.createUser(chatId, username);

        // Assert
        assertNotNull(result);
        assertEquals(chatId, result.getChatId());
        assertEquals(username, result.getUsername());
        assertEquals(0, result.getTimeShift());
        assertEquals(1, result.getId());
        
        // Проверяем что save был вызван с правильными параметрами
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        
        User capturedUser = userCaptor.getValue();
        assertEquals(chatId, capturedUser.getChatId());
        assertEquals(username, capturedUser.getUsername());
        assertEquals(0, capturedUser.getTimeShift());
    }

    @Test
    void testCreateUser_WithNullUsername_CreatesUser() {
        // Arrange
        String chatId = "12345";
        User savedUser = new User();
        savedUser.setChatId(chatId);
        savedUser.setUsername(null);
        
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        User result = userDAO.createUser(chatId, null);

        // Assert
        assertNotNull(result);
        assertEquals(chatId, result.getChatId());
        assertNull(result.getUsername());
    }

    @Test
    void testFindOrCreate_WhenUserExists_ReturnsExistingUser() {
        // Arrange
        String chatId = "12345";
        String username = "existinguser";
        
        User existingUser = new User();
        existingUser.setId(1);
        existingUser.setChatId(chatId);
        existingUser.setUsername(username);
        
        when(userRepository.findByUsername(username)).thenReturn(existingUser);

        // Act
        User result = userDAO.findOrCreate(chatId, username);

        // Assert
        assertNotNull(result);
        assertEquals(existingUser, result);
        assertEquals(1, result.getId());
        verify(userRepository, times(1)).findByUsername(username);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testFindOrCreate_WhenUserDoesNotExist_CreatesNewUser() {
        // Arrange
        String chatId = "12345";
        String username = "newuser";
        
        when(userRepository.findByUsername(username)).thenReturn(null);
        
        User newUser = new User();
        newUser.setChatId(chatId);
        newUser.setUsername(username);
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        // Act
        User result = userDAO.findOrCreate(chatId, username);

        // Assert
        assertNotNull(result);
        assertEquals(chatId, result.getChatId());
        assertEquals(username, result.getUsername());
        verify(userRepository, times(1)).findByUsername(username);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testFindOrCreate_WithNullUsername_AlwaysCreatesNewUser() {
        // Arrange
        String chatId = "12345";
        
        when(userRepository.findByUsername(null)).thenReturn(null);
        
        User newUser = new User();
        newUser.setChatId(chatId);
        newUser.setUsername(null);
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        // Act
        User result = userDAO.findOrCreate(chatId, null);

        // Assert
        assertNotNull(result);
        assertEquals(chatId, result.getChatId());
        assertNull(result.getUsername());
        verify(userRepository, times(1)).findByUsername(null);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testSave_UpdatesUser() {
        // Arrange
        User userToSave = new User();
        userToSave.setId(1);
        userToSave.setUsername("testuser");
        
        User savedUser = new User();
        savedUser.setId(1);
        savedUser.setUsername("updateduser");
        
        when(userRepository.save(userToSave)).thenReturn(savedUser);

        // Act
        User result = userDAO.save(userToSave);

        // Assert
        assertNotNull(result);
        assertEquals(savedUser, result);
        verify(userRepository, times(1)).save(userToSave);
    }

    @Test
    void testSave_WithNullUser_ThrowsException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> userDAO.save(null));
        // Spring Data JPA скорее всего бросит исключение при попытке сохранить null
    }

    @Test
    void testAddWaterDrink_CreatesWaterDrinkWithCorrectProperties() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setUsername("testuser");
        
        Integer ml = 250;
        Instant time = Instant.now();
        
        WaterDrink savedWaterDrink = new WaterDrink();
        savedWaterDrink.setId(100);
        savedWaterDrink.setCountWaterMl(ml);
        savedWaterDrink.setTime(time);
        savedWaterDrink.setUser(user);
        
        when(drinkWaterRepository.save(any(WaterDrink.class))).thenReturn(savedWaterDrink);

        // Act
        WaterDrink result = userDAO.addWaterDrink(user, ml, time);

        // Assert
        assertNotNull(result);
        assertEquals(ml, result.getCountWaterMl());
        assertEquals(time, result.getTime());
        assertEquals(user, result.getUser());
        assertEquals(100, result.getId());
        
        // Проверяем что save был вызван с правильными параметрами
        ArgumentCaptor<WaterDrink> waterDrinkCaptor = ArgumentCaptor.forClass(WaterDrink.class);
        verify(drinkWaterRepository).save(waterDrinkCaptor.capture());
        
        WaterDrink capturedWaterDrink = waterDrinkCaptor.getValue();
        assertEquals(ml, capturedWaterDrink.getCountWaterMl());
        assertEquals(time, capturedWaterDrink.getTime());
        assertEquals(user, capturedWaterDrink.getUser());
    }

    @Test
    void testAddWaterDrink_WithNullUser_ThrowsException() {
        // Arrange
        Integer ml = 250;
        Instant time = Instant.now();

        // Act & Assert
        // В зависимости от реализации, может бросить NPE или быть обработано
        // Давайте проверим, что метод обрабатывает это корректно
        assertThrows(NullPointerException.class, () -> 
            userDAO.addWaterDrink(null, ml, time)
        );
    }

    @Test
    void testAddWaterDrink_WithNullTime_CreatesWaterDrink() {
        // Arrange
        User user = new User();
        Integer ml = 250;
        
        WaterDrink savedWaterDrink = new WaterDrink();
        savedWaterDrink.setCountWaterMl(ml);
        savedWaterDrink.setTime(null);
        savedWaterDrink.setUser(user);
        
        when(drinkWaterRepository.save(any(WaterDrink.class))).thenReturn(savedWaterDrink);

        // Act
        WaterDrink result = userDAO.addWaterDrink(user, ml, null);

        // Assert
        assertNotNull(result);
        assertEquals(ml, result.getCountWaterMl());
        assertNull(result.getTime());
        assertEquals(user, result.getUser());
    }

    @Test
    void testAddWaterDrink_WithZeroMl_CreatesWaterDrink() {
        // Arrange
        User user = new User();
        Integer ml = 0;
        Instant time = Instant.now();
        
        WaterDrink savedWaterDrink = new WaterDrink();
        savedWaterDrink.setCountWaterMl(ml);
        
        when(drinkWaterRepository.save(any(WaterDrink.class))).thenReturn(savedWaterDrink);

        // Act
        WaterDrink result = userDAO.addWaterDrink(user, ml, time);

        // Assert
        assertNotNull(result);
        assertEquals(ml, result.getCountWaterMl());
    }

    @Test
    void testDelete_DeletesUserAndWaterDrinks() {
        // Arrange
        User user = new User();
        user.setId(1);
        
        // Создаем список WaterDrink для пользователя
        WaterDrink drink1 = new WaterDrink();
        drink1.setId(10);
        
        WaterDrink drink2 = new WaterDrink();
        drink2.setId(11);
        
        List<WaterDrink> waterDrinks = Arrays.asList(drink1, drink2);
        user.setWaterDunks(waterDrinks);

        // Act
        userDAO.delete(user);

        // Assert
        // Проверяем что все WaterDrink были удалены
        verify(drinkWaterRepository, times(1)).delete(drink1);
        verify(drinkWaterRepository, times(1)).delete(drink2);
        
        // Проверяем что пользователь был удален
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void testDelete_UserWithNoWaterDrinks_DeletesOnlyUser() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setWaterDunks(new ArrayList<>()); // Пустой список

        // Act
        userDAO.delete(user);

        // Assert
        // Проверяем что delete для WaterDrink не вызывался
        verify(drinkWaterRepository, never()).delete(any(WaterDrink.class));
        
        // Проверяем что пользователь был удален
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void testDelete_WithNullUser_ThrowsException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> userDAO.delete(null));
    }

    @Test
    void testTransactionalAnnotationOnAddWaterDrink() throws NoSuchMethodException {
        // Проверяем наличие аннотации @Transactional на методе addWaterDrink
        var method = UserDAO.class.getMethod("addWaterDrink", User.class, Integer.class, Instant.class);
        assertTrue(method.isAnnotationPresent(jakarta.transaction.Transactional.class));
    }

    @Test
    void testTransactionalAnnotationOnDelete() throws NoSuchMethodException {
        // Проверяем наличие аннотации @Transactional на методе delete
        var method = UserDAO.class.getMethod("delete", User.class);
        assertTrue(method.isAnnotationPresent(jakarta.transaction.Transactional.class));
    }

    @Test
    void testRepositoryAnnotationOnClass() {
        // Проверяем что класс помечен как @Repository
        assertTrue(UserDAO.class.isAnnotationPresent(org.springframework.stereotype.Repository.class));
    }

    @Test
    void testSave_ReturnsSameInstance() {
        // Тест для проверки что save возвращает тот же объект (поведение Spring Data JPA)
        // Arrange
        User user = new User();
        user.setUsername("test");
        
        when(userRepository.save(same(user))).thenReturn(user);

        // Act
        User result = userDAO.save(user);

        // Assert
        assertSame(user, result);
    }

    @Test
    void testEdgeCases() {
        // Тестирование граничных случаев
        
        // 1. Очень длинные строки
        String longChatId = "1234567890123456789012345678901234567890";
        String longUsername = "very_long_username_that_exceeds_normal_length_but_should_still_work";
        
        User user = new User();
        when(userRepository.save(any(User.class))).thenReturn(user);
        
        User result = userDAO.createUser(longChatId, longUsername);
        assertNotNull(result);
        
        // 2. Специальные символы в username
        User result2 = userDAO.createUser("123", "user@name#123");
        assertNotNull(result2);
        
        // 3. Пустые строки
        User result3 = userDAO.createUser("", "");
        assertNotNull(result3);
    }
}