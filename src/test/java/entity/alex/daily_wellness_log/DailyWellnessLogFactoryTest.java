package entity.alex.daily_wellness_log;

import entity.alex.DailyWellnessLog.DailyWellnessLog;
import entity.alex.DailyWellnessLog.DailyWellnessLogFactory;
import entity.alex.DailyWellnessLog.DailyWellnessLogFactoryInterf;
import entity.alex.DailyWellnessLog.DailyWellnessLogInterf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DailyWellnessLogFactory.
 */
public class DailyWellnessLogFactoryTest {

    private DailyWellnessLogFactoryInterf factory;

    @BeforeEach
    void setUp() {
        factory = new DailyWellnessLogFactory();
    }

    @Test
    void testCreateReturnsNonNull() {
        DailyWellnessLogInterf log = factory.create(LocalDate.now());
        assertNotNull(log, "Factory should return a non-null DailyWellnessLogInterf.");
    }

    @Test
    void testCreateReturnsCorrectType() {
        LocalDate date = LocalDate.of(2025, 8, 14);
        DailyWellnessLogInterf log = factory.create(date);
        assertTrue(log instanceof DailyWellnessLog, "Returned object should be instance of DailyWellnessLog.");
    }

    @Test
    void testLogHasCorrectDate() {
        LocalDate date = LocalDate.of(2023, 12, 1);
        DailyWellnessLogInterf log = factory.create(date);
        assertEquals(date, log.getDate(), "Log should retain the date passed to the factory.");
    }
}
