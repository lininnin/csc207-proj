package entityTest.alex.daily_event_log;

import entity.Alex.DailyEventLog.DailyEventLog;
import entity.Alex.DailyEventLog.DailyEventLogFactory;
import entity.Alex.DailyEventLog.DailyEventLogInterf;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class DailyEventLogFactoryTest {

    @Test
    public void testCreateReturnsNonNullInstance() {
        DailyEventLogFactory factory = new DailyEventLogFactory();
        DailyEventLogInterf result = factory.create(LocalDate.of(2025, 8, 8));

        assertNotNull(result);
        assertTrue(result instanceof DailyEventLog);
    }

    @Test
    public void testCreatedInstanceHasCorrectDate() {
        DailyEventLogFactory factory = new DailyEventLogFactory();
        LocalDate date = LocalDate.of(2025, 8, 8);
        DailyEventLogInterf log = factory.create(date);

        assertEquals(date, log.getDate());
    }

    @Test
    public void testCreateReturnsDifferentInstances() {
        DailyEventLogFactory factory = new DailyEventLogFactory();
        DailyEventLogInterf first = factory.create(LocalDate.of(2025, 8, 8));
        DailyEventLogInterf second = factory.create(LocalDate.of(2025, 8, 9));

        assertNotSame(first, second);
        assertNotEquals(first.getDate(), second.getDate());
    }
}

