package spring.telegrambot.drinkWaterMeter.repository.userDAO;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import spring.telegrambot.drinkWaterMeter.repository.dao.UserDAO;
import spring.telegrambot.drinkWaterMeter.repository.model.user.User;
import spring.telegrambot.drinkWaterMeter.repository.model.user.WaterDrink;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(UserDAO.class)
class UserDAOIntegrationTest {

    @Autowired
    private UserDAO userDAO;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> "jdbc:postgresql://localhost:5432/test009");
        registry.add("spring.datasource.username", () -> "postgres");
        registry.add("spring.datasource.password", () -> "0000");
    }


    @Test
    void testCreateAndFindUser_Integration() {
        // Arrange
        String chatId = "integration_test_chat_id";
        String username = "integration_test_user";

        // Act - создаем пользователя
        User createdUser = userDAO.createUser(chatId, username);
        
        // Assert
        assertNotNull(createdUser);
        assertNotNull(createdUser.getId());
        assertEquals(chatId, createdUser.getChatId());
        assertEquals(username, createdUser.getUsername());
        assertEquals(0, createdUser.getTimeShift());

        // Act - ищем всех пользователей
        List<User> allUsers = userDAO.findAll();
        
        // Assert
        assertFalse(allUsers.isEmpty());
        assertTrue(allUsers.stream().anyMatch(u -> u.getUsername().equals(username)));
    }

    @Test
    void testAddWaterDrink_Integration() {
        // Arrange
        User user = userDAO.createUser("chat123", "testuser");
        Integer ml = 300;
        Instant time = Instant.now();

        // Act
        WaterDrink waterDrink = userDAO.addWaterDrink(user, ml, time);

        // Assert
        assertNotNull(waterDrink);
        assertNotNull(waterDrink.getId());
        assertEquals(ml, waterDrink.getCountWaterMl());
        assertEquals(time, waterDrink.getTime());
        assertEquals(user, waterDrink.getUser());
    }

    @Test
    void testFindOrCreate_WhenNotExists_Integration() {
        // Act
        User user = userDAO.findOrCreate("new_chat", "new_user");

        // Assert
        assertNotNull(user);
        assertEquals("new_chat", user.getChatId());
        assertEquals("new_user", user.getUsername());
    }

    @Test
    void testDeleteUser_Integration() {
        // Arrange
        User user = userDAO.createUser("to_delete", "delete_me");
        userDAO.addWaterDrink(user, 250, Instant.now());
        
        // Предварительная проверка
        List<User> usersBefore = userDAO.findAll();
        assertTrue(usersBefore.contains(user));

        // Act
        User userFind = userDAO.findOrCreate("to_delete", "delete_me");
        userDAO.delete(userFind);

        // Assert - пользователь должен быть удален
        List<User> usersAfter = userDAO.findAll();
        assertFalse(usersAfter.contains(userFind));
    }
}