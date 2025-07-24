package entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for BeginAndDueDates value object.
 */
public class BeginAndDueDatesTest {

    @Test
    public void testValidDateRange() {
        LocalDate begin = LocalDate.now();
        LocalDate due = LocalDate.now().plusDays(5);

        BeginAndDueDates dates = new BeginAndDueDates(begin, due);

        assertEquals(begin, dates.getBeginDate());
        assertEquals(due, dates.getDueDate());
        assertTrue(dates.hasDueDate());
    }

    @Test
    public void testNoDueDate() {
        LocalDate begin = LocalDate.now();

        BeginAndDueDates dates = new BeginAndDueDates(begin, null);

        assertEquals(begin, dates.getBeginDate());
        assertNull(dates.getDueDate());
        assertFalse(dates.hasDueDate());
    }

    @Test
    public void testNullBeginDateThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new BeginAndDueDates(null, LocalDate.now())
        );
    }

    @Test
    public void testDueDateBeforeBeginDateThrowsException() {
        LocalDate begin = LocalDate.now();
        LocalDate due = LocalDate.now().minusDays(1);

        assertThrows(IllegalArgumentException.class, () ->
                new BeginAndDueDates(begin, due)
        );
    }

    @Test
    public void testStartingTodayFactory() {
        LocalDate dueDate = LocalDate.now().plusDays(3);
        BeginAndDueDates dates = BeginAndDueDates.startingToday(dueDate);

        assertEquals(LocalDate.now(), dates.getBeginDate());
        assertEquals(dueDate, dates.getDueDate());
    }

    @Test
    public void testStartingTodayWithNullDueDate() {
        BeginAndDueDates dates = BeginAndDueDates.startingToday(null);

        assertEquals(LocalDate.now(), dates.getBeginDate());
        assertNull(dates.getDueDate());
    }

    @Test
    public void testIsPastDueTrue() {
        BeginAndDueDates dates = new BeginAndDueDates(
                LocalDate.now().minusDays(5),
                LocalDate.now().minusDays(1)
        );

        assertTrue(dates.isPastDue());
    }

    @Test
    public void testIsPastDueFalse() {
        BeginAndDueDates dates = new BeginAndDueDates(
                LocalDate.now(),
                LocalDate.now().plusDays(1)
        );

        assertFalse(dates.isPastDue());
    }

    @Test
    public void testIsPastDueToday() {
        BeginAndDueDates dates = new BeginAndDueDates(
                LocalDate.now().minusDays(1),
                LocalDate.now()
        );

        assertFalse(dates.isPastDue());
    }

    @Test
    public void testIsPastDueNoDueDate() {
        BeginAndDueDates dates = new BeginAndDueDates(LocalDate.now(), null);

        assertFalse(dates.isPastDue());
    }

    @Test
    public void testToString() {
        LocalDate begin = LocalDate.of(2024, 1, 1);
        LocalDate due = LocalDate.of(2024, 1, 5);

        BeginAndDueDates dates1 = new BeginAndDueDates(begin, due);
        BeginAndDueDates dates2 = new BeginAndDueDates(begin, null);

        assertTrue(dates1.toString().contains("2024-01-01"));
        assertTrue(dates1.toString().contains("2024-01-05"));
        assertTrue(dates2.toString().contains("none"));
    }
}