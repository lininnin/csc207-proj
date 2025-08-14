package entity.BeginAndDueDates;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for BeginAndDueDates entity following Clean Architecture principles.
 * Tests date validation, business rules, and edge cases.
 */
class BeginAndDueDatesTest {

    private LocalDate today;
    private LocalDate tomorrow;
    private LocalDate yesterday;
    private LocalDate nextWeek;
    private LocalDate lastWeek;

    @BeforeEach
    void setUp() {
        today = LocalDate.now();
        tomorrow = today.plusDays(1);
        yesterday = today.minusDays(1);
        nextWeek = today.plusWeeks(1);
        lastWeek = today.minusWeeks(1);
    }

    // Constructor Tests
    @Test
    void testConstructorWithValidDates() {
        BeginAndDueDates dates = new BeginAndDueDates(today, tomorrow);
        
        assertNotNull(dates);
        assertEquals(today, dates.getBeginDate());
        assertEquals(tomorrow, dates.getDueDate());
    }

    @Test
    void testConstructorWithNullBeginDate() {
        BeginAndDueDates dates = new BeginAndDueDates(null, tomorrow);
        
        assertNotNull(dates);
        assertNull(dates.getBeginDate());
        assertEquals(tomorrow, dates.getDueDate());
    }

    @Test
    void testConstructorWithNullDueDate() {
        BeginAndDueDates dates = new BeginAndDueDates(today, null);
        
        assertNotNull(dates);
        assertEquals(today, dates.getBeginDate());
        assertNull(dates.getDueDate());
    }

    @Test
    void testConstructorWithBothNull() {
        BeginAndDueDates dates = new BeginAndDueDates(null, null);
        
        assertNotNull(dates);
        assertNull(dates.getBeginDate());
        assertNull(dates.getDueDate());
    }

    @Test
    void testConstructorWithSameDates() {
        BeginAndDueDates dates = new BeginAndDueDates(today, today);
        
        assertNotNull(dates);
        assertEquals(today, dates.getBeginDate());
        assertEquals(today, dates.getDueDate());
    }

    @Test
    void testConstructorThrowsWhenBeginAfterDue() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new BeginAndDueDates(tomorrow, today)
        );
        
        assertEquals("Begin date cannot be after due date", exception.getMessage());
    }

    // Setter Tests
    @Test
    void testSetBeginDateValid() {
        BeginAndDueDates dates = new BeginAndDueDates(today, nextWeek);
        
        dates.setBeginDate(tomorrow);
        assertEquals(tomorrow, dates.getBeginDate());
        assertEquals(nextWeek, dates.getDueDate());
    }

    @Test
    void testSetBeginDateToNull() {
        BeginAndDueDates dates = new BeginAndDueDates(today, tomorrow);
        
        dates.setBeginDate(null);
        assertNull(dates.getBeginDate());
        assertEquals(tomorrow, dates.getDueDate());
    }

    @Test
    void testSetBeginDateThrowsWhenAfterDue() {
        BeginAndDueDates dates = new BeginAndDueDates(today, tomorrow);
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> dates.setBeginDate(nextWeek)
        );
        
        assertEquals("Begin date cannot be after due date", exception.getMessage());
        // Verify date wasn't changed
        assertEquals(today, dates.getBeginDate());
    }

    @Test
    void testSetDueDateValid() {
        BeginAndDueDates dates = new BeginAndDueDates(today, tomorrow);
        
        dates.setDueDate(nextWeek);
        assertEquals(today, dates.getBeginDate());
        assertEquals(nextWeek, dates.getDueDate());
    }

    @Test
    void testSetDueDateToNull() {
        BeginAndDueDates dates = new BeginAndDueDates(today, tomorrow);
        
        dates.setDueDate(null);
        assertEquals(today, dates.getBeginDate());
        assertNull(dates.getDueDate());
    }

    @Test
    void testSetDueDateThrowsWhenBeforeBegin() {
        BeginAndDueDates dates = new BeginAndDueDates(today, tomorrow);
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> dates.setDueDate(yesterday)
        );
        
        assertEquals("Due date cannot be before begin date", exception.getMessage());
        // Verify date wasn't changed
        assertEquals(tomorrow, dates.getDueDate());
    }

    // hasDueDate Tests
    @Test
    void testHasDueDateWhenSet() {
        BeginAndDueDates dates = new BeginAndDueDates(today, tomorrow);
        assertTrue(dates.hasDueDate());
    }

    @Test
    void testHasDueDateWhenNull() {
        BeginAndDueDates dates = new BeginAndDueDates(today, null);
        assertFalse(dates.hasDueDate());
    }

    @Test
    void testHasDueDateAfterSettingToNull() {
        BeginAndDueDates dates = new BeginAndDueDates(today, tomorrow);
        assertTrue(dates.hasDueDate());
        
        dates.setDueDate(null);
        assertFalse(dates.hasDueDate());
    }

    // isOverdue Tests
    @Test
    void testIsOverdueWithPastDueDate() {
        BeginAndDueDates dates = new BeginAndDueDates(lastWeek, yesterday);
        assertTrue(dates.isOverdue());
    }

    @Test
    void testIsOverdueWithTodayDueDate() {
        BeginAndDueDates dates = new BeginAndDueDates(yesterday, today);
        assertFalse(dates.isOverdue());
    }

    @Test
    void testIsOverdueWithFutureDueDate() {
        BeginAndDueDates dates = new BeginAndDueDates(today, tomorrow);
        assertFalse(dates.isOverdue());
    }

    @Test
    void testIsOverdueWithNullDueDate() {
        BeginAndDueDates dates = new BeginAndDueDates(today, null);
        assertFalse(dates.isOverdue());
    }

    // isDueTodayOrFuture Tests
    @Test
    void testIsDueTodayOrFutureWithToday() {
        BeginAndDueDates dates = new BeginAndDueDates(yesterday, today);
        assertTrue(dates.isDueTodayOrFuture());
    }

    @Test
    void testIsDueTodayOrFutureWithFuture() {
        BeginAndDueDates dates = new BeginAndDueDates(today, tomorrow);
        assertTrue(dates.isDueTodayOrFuture());
    }

    @Test
    void testIsDueTodayOrFutureWithPast() {
        BeginAndDueDates dates = new BeginAndDueDates(lastWeek, yesterday);
        assertFalse(dates.isDueTodayOrFuture());
    }

    @Test
    void testIsDueTodayOrFutureWithNull() {
        BeginAndDueDates dates = new BeginAndDueDates(today, null);
        assertFalse(dates.isDueTodayOrFuture());
    }

    // toString Tests
    @Test
    void testToStringWithBothDates() {
        BeginAndDueDates dates = new BeginAndDueDates(
            LocalDate.of(2024, 1, 1),
            LocalDate.of(2024, 1, 15)
        );
        
        String expected = "BeginAndDueDates{beginDate=2024-01-01, dueDate=2024-01-15}";
        assertEquals(expected, dates.toString());
    }

    @Test
    void testToStringWithOnlyBeginDate() {
        BeginAndDueDates dates = new BeginAndDueDates(
            LocalDate.of(2024, 1, 1),
            null
        );
        
        String expected = "BeginAndDueDates{beginDate=2024-01-01}";
        assertEquals(expected, dates.toString());
    }

    @Test
    void testToStringWithOnlyDueDate() {
        BeginAndDueDates dates = new BeginAndDueDates(
            null,
            LocalDate.of(2024, 1, 15)
        );
        
        String expected = "BeginAndDueDates{dueDate=2024-01-15}";
        assertEquals(expected, dates.toString());
    }

    @Test
    void testToStringWithNoDates() {
        BeginAndDueDates dates = new BeginAndDueDates(null, null);
        
        String expected = "BeginAndDueDates{}";
        assertEquals(expected, dates.toString());
    }

    // equals and hashCode Tests
    @Test
    void testEqualsWithSameObject() {
        BeginAndDueDates dates = new BeginAndDueDates(today, tomorrow);
        assertEquals(dates, dates);
    }

    @Test
    void testEqualsWithEqualObjects() {
        BeginAndDueDates dates1 = new BeginAndDueDates(today, tomorrow);
        BeginAndDueDates dates2 = new BeginAndDueDates(today, tomorrow);
        
        assertEquals(dates1, dates2);
        assertEquals(dates1.hashCode(), dates2.hashCode());
    }

    @Test
    void testEqualsWithDifferentBeginDates() {
        BeginAndDueDates dates1 = new BeginAndDueDates(today, tomorrow);
        BeginAndDueDates dates2 = new BeginAndDueDates(yesterday, tomorrow);
        
        assertNotEquals(dates1, dates2);
    }

    @Test
    void testEqualsWithDifferentDueDates() {
        BeginAndDueDates dates1 = new BeginAndDueDates(today, tomorrow);
        BeginAndDueDates dates2 = new BeginAndDueDates(today, nextWeek);
        
        assertNotEquals(dates1, dates2);
    }

    @Test
    void testEqualsWithNullObject() {
        BeginAndDueDates dates = new BeginAndDueDates(today, tomorrow);
        assertNotEquals(dates, null);
    }

    @Test
    void testEqualsWithDifferentClass() {
        BeginAndDueDates dates = new BeginAndDueDates(today, tomorrow);
        assertNotEquals(dates, "not a BeginAndDueDates");
    }

    @Test
    void testEqualsWithNullDates() {
        BeginAndDueDates dates1 = new BeginAndDueDates(null, null);
        BeginAndDueDates dates2 = new BeginAndDueDates(null, null);
        
        assertEquals(dates1, dates2);
        assertEquals(dates1.hashCode(), dates2.hashCode());
    }

    // Edge Cases and Boundary Tests
    @Test
    void testWithVeryDistantDates() {
        LocalDate farPast = LocalDate.of(1900, 1, 1);
        LocalDate farFuture = LocalDate.of(2100, 12, 31);
        
        BeginAndDueDates dates = new BeginAndDueDates(farPast, farFuture);
        
        assertEquals(farPast, dates.getBeginDate());
        assertEquals(farFuture, dates.getDueDate());
        assertFalse(dates.isOverdue());
        assertTrue(dates.isDueTodayOrFuture());
    }

    @Test
    void testDateModificationSequence() {
        BeginAndDueDates dates = new BeginAndDueDates(yesterday, tomorrow);
        
        // Move due date to next week
        dates.setDueDate(nextWeek);
        assertEquals(nextWeek, dates.getDueDate());
        
        // Now can move begin date to today
        dates.setBeginDate(today);
        assertEquals(today, dates.getBeginDate());
        
        // Set due date to null
        dates.setDueDate(null);
        assertNull(dates.getDueDate());
        
        // Now can set begin date to any future date
        dates.setBeginDate(nextWeek);
        assertEquals(nextWeek, dates.getBeginDate());
    }

    @Test
    void testLeapYearDates() {
        LocalDate leapDay2024 = LocalDate.of(2024, 2, 29);
        LocalDate march1st2024 = LocalDate.of(2024, 3, 1);
        
        BeginAndDueDates dates = new BeginAndDueDates(leapDay2024, march1st2024);
        
        assertEquals(leapDay2024, dates.getBeginDate());
        assertEquals(march1st2024, dates.getDueDate());
    }
}