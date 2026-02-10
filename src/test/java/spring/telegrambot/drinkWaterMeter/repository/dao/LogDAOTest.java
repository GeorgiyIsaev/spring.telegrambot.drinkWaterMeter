package spring.telegrambot.drinkWaterMeter.repository.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import spring.telegrambot.drinkWaterMeter.repository.jpa.log.LogRepository;
import spring.telegrambot.drinkWaterMeter.repository.model.log.Log;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogDAOTest {

    @Mock
    private LogRepository logRepository;

    @InjectMocks
    private LogDAO logDAO;

    private Log testLog;
    private Instant testTime;

    @BeforeEach
    void setUp() {
        testTime = Instant.parse("2024-01-15T10:00:00Z");
        testLog = new Log();
        testLog.setId(1);
        testLog.setTime(testTime);
        testLog.setEvent("TEST_EVENT");
        testLog.setInformation("Test information");
    }

    @Test
    @DisplayName("getFull - should return all logs from repository")
    void testGetFull_WhenRepositoryHasLogs_ReturnsAllLogs() {
        // Arrange
        List<Log> expectedLogs = Arrays.asList(testLog, new Log());
        when(logRepository.findAll()).thenReturn(expectedLogs);

        // Act
        List<Log> result = logDAO.getFull();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).isEqualTo(expectedLogs);
        verify(logRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("getFull - should return empty list when no logs")
    void testGetFull_WhenRepositoryEmpty_ReturnsEmptyList() {
        // Arrange
        when(logRepository.findAll()).thenReturn(List.of());

        // Act
        List<Log> result = logDAO.getFull();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(logRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("save with Log entity - should save and return saved log")
    void testSave_WithLogEntity_ShouldSaveAndReturnLog() {
        // Arrange
        when(logRepository.save(testLog)).thenReturn(testLog);

        // Act
        Log result = logDAO.save(testLog);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(testLog);
        verify(logRepository, times(1)).save(testLog);
    }

    @Test
    @DisplayName("save with Log entity - should return null when save fails")
    void testSave_WithLogEntity_WhenSaveFails_ReturnsNull() {
        // Arrange
        when(logRepository.save(testLog)).thenReturn(null);

        // Act
        Log result = logDAO.save(testLog);

        // Assert
        assertThat(result).isNull();
        verify(logRepository, times(1)).save(testLog);
    }

    @Test
    @DisplayName("save with parameters - should create and save log successfully")
    void testSave_WithTimeEventInformation_ShouldCreateAndSaveLog() {
        // Arrange
        String event = "USER_LOGIN";
        String information = "User logged in successfully";
        Log expectedLog = new Log();
        expectedLog.setTime(testTime);
        expectedLog.setEvent(event);
        expectedLog.setInformation(information);

        when(logRepository.save(any(Log.class))).thenReturn(expectedLog);

        // Act
        Log result = logDAO.save(testTime, event, information);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTime()).isEqualTo(testTime);
        assertThat(result.getEvent()).isEqualTo(event);
        assertThat(result.getInformation()).isEqualTo(information);
        verify(logRepository, times(1)).save(any(Log.class));
    }

    @Test
    @DisplayName("save with parameters - should truncate information when too long")
    void testSave_WithTimeEventInformation_WhenInformationTooLong_ShouldTruncate() {
        // Arrange
        String event = "LONG_MESSAGE";
        String longInformation = "A".repeat(1500); // 1500 символов
        String expectedTruncated = "A".repeat(1000); // Должно обрезаться до 1000

        when(logRepository.save(any(Log.class))).thenAnswer(invocation -> {
            Log savedLog = invocation.getArgument(0);
            return savedLog;
        });

        // Act
        Log result = logDAO.save(testTime, event, longInformation);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getInformation()).hasSize(1000);
        assertThat(result.getInformation()).isEqualTo(expectedTruncated);
        verify(logRepository, times(1)).save(any(Log.class));
    }

    @Test
    @DisplayName("save with parameters - should return null when information is null")
    void testSave_WithTimeEventInformation_WhenInformationIsNull_ReturnsNull() {
        // Arrange
        String event = "NULL_INFO_EVENT";
        String information = null;

        // Act
        Log result = logDAO.save(testTime, event, information);

        // Assert
        assertThat(result).isNull();
        verify(logRepository, never()).save(any(Log.class));
    }

    @Test
    @DisplayName("save with parameters - should handle empty information string")
    void testSave_WithTimeEventInformation_WhenInformationIsEmpty_ShouldSave() {
        // Arrange
        String event = "EMPTY_INFO_EVENT";
        String information = "";
        Log savedLog = new Log();
        savedLog.setTime(testTime);
        savedLog.setEvent(event);
        savedLog.setInformation(information);

        when(logRepository.save(any(Log.class))).thenReturn(savedLog);

        // Act
        Log result = logDAO.save(testTime, event, information);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getInformation()).isEmpty();
        verify(logRepository, times(1)).save(any(Log.class));
    }

    @Test
    @DisplayName("save with event and information - should use current time")
    void testSave_WithEventAndInformation_ShouldUseCurrentTime() {
        // Arrange
        String event = "AUTO_TIME_EVENT";
        String information = "Testing automatic time";
        
        // Захватываем время до вызова
        Instant beforeCall = Instant.now();
        
        when(logRepository.save(any(Log.class))).thenAnswer(invocation -> {
            Log log = invocation.getArgument(0);
            // Проверяем, что время установлено
            assertThat(log.getTime()).isNotNull();
            assertThat(log.getTime()).isAfterOrEqualTo(beforeCall);
            return log;
        });

        // Act
        Log result = logDAO.save(event, information);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getEvent()).isEqualTo(event);
        assertThat(result.getInformation()).isEqualTo(information);
        verify(logRepository, times(1)).save(any(Log.class));
    }

    @Test
    @DisplayName("save with event and information - should truncate long information")
    void testSave_WithEventAndInformation_WhenInformationExceedsLimit_ShouldTruncate() {
        // Arrange
        String event = "EVENT_WITH_LONG_INFO";
        String longInfo = "B".repeat(1200);
        String expectedTruncated = "B".repeat(1000);

        when(logRepository.save(any(Log.class))).thenAnswer(invocation -> 
            invocation.getArgument(0)
        );

        // Act
        Log result = logDAO.save(event, longInfo);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getInformation()).hasSize(1000);
        assertThat(result.getInformation()).isEqualTo(expectedTruncated);
        assertThat(result.getTime()).isNotNull();
    }

    @Test
    @DisplayName("save with event and information - should return null for null information")
    void testSave_WithEventAndInformation_WhenInformationNull_ReturnsNull() {
        // Arrange
        String event = "NULL_INFO";
        String information = null;

        // Act
        Log result = logDAO.save(event, information);

        // Assert
        assertThat(result).isNull();
        verify(logRepository, never()).save(any(Log.class));
    }

    @Test
    @DisplayName("Constructor - should inject LogRepository successfully")
    void testConstructor_ShouldInjectLogRepository() {
        // Arrange & Act
        LogDAO dao = new LogDAO(logRepository);

        // Assert
        assertThat(dao).isNotNull();
        // Можно добавить рефлексию для проверки поля, если нужно
    }

    @Test
    @DisplayName("save with exact MAX_COUNT_INFORMATION - should not truncate")
    void testSave_WithExactMaxCountInformation_ShouldNotTruncate() {
        // Arrange
        String event = "EXACT_LENGTH_EVENT";
        String exactLengthInfo = "C".repeat(1000); // Точная максимальная длина

        when(logRepository.save(any(Log.class))).thenAnswer(invocation -> 
            invocation.getArgument(0)
        );

        // Act
        Log result = logDAO.save(event, exactLengthInfo);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getInformation()).hasSize(1000);
        assertThat(result.getInformation()).isEqualTo(exactLengthInfo);
        verify(logRepository, times(1)).save(any(Log.class));
    }
}