package entityTest.alex.daily_wellness_log;

import entity.Alex.DailyWellnessLog.DailyWellnessLog;
import entity.Alex.DailyWellnessLog.DailyWellnessLogFactory;
import entity.Alex.DailyWellnessLog.DailyWellnessLogInterf;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class DailyWellnessLogFactoryTest {

    @Test
    public void testCreateReturnsNonNullInstance() {
        DailyWellnessLogFactory factory = new DailyWellnessLogFactory();
        DailyWellnessLogInterf log = factory.create(LocalDate.of(2025, 8, 8));

        assertNotNull(log);
        assertTrue(log instanceof DailyWellnessLog);
    }

    @Test
    public void testCreatedLogHasCorrectDate() {
        LocalDate targetDate = LocalDate.of(2025, 8, 8);
        DailyWellnessLogFactory factory = new DailyWellnessLogFactory();
        DailyWellnessLogInterf log = factory.create(targetDate);

        assertEquals(targetDate, log.getDate());
    }

    @Test
    public void testCreateReturnsDifferentInstances() {
        DailyWellnessLogFactory factory = new DailyWellnessLogFactory();

        DailyWellnessLogInterf log1 = factory.create(LocalDate.of(2025, 8, 8));
        DailyWellnessLogInterf log2 = factory.create(LocalDate.of(2025, 8, 9));

        assertNotSame(log1, log2);
        assertNotEquals(log1.getDate(), log2.getDate());
    }
}

