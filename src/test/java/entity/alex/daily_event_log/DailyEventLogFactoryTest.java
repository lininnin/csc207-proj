package entity.alex.daily_event_log;

import entity.Alex.DailyEventLog.DailyEventLog;
import entity.Alex.DailyEventLog.DailyEventLogFactory;
import entity.Alex.DailyEventLog.DailyEventLogFactoryInterf;
import entity.Alex.DailyEventLog.DailyEventLogInterf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DailyEventLogFactory.
 */
public class DailyEventLogFactoryTest {

    private DailyEventLogFactoryInterf factory;

    @BeforeEach
    void setUp() {
        factory = new DailyEventLogFactory();
    }

    @Test
    void testCreateReturnsNonNull() {
        DailyEventLogInterf log = factory.create(LocalDate.now());
        assertNotNull(log, "Factory should return a non-null DailyEventLogInterf.");
    }

    @Test
    void testCreateReturnsCorrectType() {
        DailyEventLogInterf log = factory.create(LocalDate.of(2025, 8, 14));
        assertTrue(log instanceof DailyEventLog, "Returned object should be an instance of DailyEventLog.");
    }

    @Test
    void testLogHasCorrectDate() {
        LocalDate expectedDate = LocalDate.of(2023, 11, 20);
        DailyEventLogInterf log = factory.create(expectedDate);
        assertEquals(expectedDate, log.getDate(), "Date in the log should match the date provided.");
    }
}
