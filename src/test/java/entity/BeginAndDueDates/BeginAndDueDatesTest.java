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

    // Immutable Update Methods Tests
    @Test
    void testWithBeginDateValid() {
        BeginAndDueDates original = new BeginAndDueDates(today, nextWeek);
        
        BeginAndDueDates updated = original.withBeginDate(tomorrow);
        
        assertNotSame(original, updated);
        assertEquals(tomorrow, updated.getBeginDate());
        assertEquals(nextWeek, updated.getDueDate());
        assertEquals(today, original.getBeginDate()); // Original unchanged
    }

    @Test
    void testWithBeginDateToNull() {
        BeginAndDueDates original = new BeginAndDueDates(today, tomorrow);
        
        BeginAndDueDates updated = original.withBeginDate(null);
        
        assertNotSame(original, updated);
        assertNull(updated.getBeginDate());
        assertEquals(tomorrow, updated.getDueDate());
    }

    @Test
    void testWithBeginDateThrowsWhenAfterDue() {
        BeginAndDueDates dates = new BeginAndDueDates(today, tomorrow);
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> dates.withBeginDate(nextWeek)
        );
        
        assertEquals("Begin date cannot be after due date", exception.getMessage());
    }

    @Test
    void testWithBeginDateAllowsValidAfterDue() {
        BeginAndDueDates original = new BeginAndDueDates(yesterday, null);
        
        BeginAndDueDates updated = original.withBeginDate(nextWeek);
        
        assertEquals(nextWeek, updated.getBeginDate());
        assertNull(updated.getDueDate());
    }

    @Test
    void testWithDueDateValid() {
        BeginAndDueDates original = new BeginAndDueDates(today, tomorrow);
        
        BeginAndDueDates updated = original.withDueDate(nextWeek);
        
        assertNotSame(original, updated);
        assertEquals(today, updated.getBeginDate());
        assertEquals(nextWeek, updated.getDueDate());
        assertEquals(tomorrow, original.getDueDate()); // Original unchanged
    }

    @Test
    void testWithDueDateToNull() {
        BeginAndDueDates original = new BeginAndDueDates(today, tomorrow);
        
        BeginAndDueDates updated = original.withDueDate(null);
        
        assertNotSame(original, updated);
        assertEquals(today, updated.getBeginDate());
        assertNull(updated.getDueDate());
    }

    @Test
    void testWithDueDateThrowsWhenBeforeBegin() {
        BeginAndDueDates dates = new BeginAndDueDates(tomorrow, nextWeek);
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> dates.withDueDate(today)
        );
        
        assertEquals("Due date cannot be before begin date", exception.getMessage());
    }

    @Test
    void testWithDueDateAllowsValidBeforeBegin() {
        BeginAndDueDates original = new BeginAndDueDates(null, tomorrow);
        
        BeginAndDueDates updated = original.withDueDate(yesterday);
        
        assertNull(updated.getBeginDate());
        assertEquals(yesterday, updated.getDueDate());
    }

    @Test
    void testWithDatesValid() {
        BeginAndDueDates original = new BeginAndDueDates(yesterday, today);
        
        BeginAndDueDates updated = original.withDates(today, nextWeek);
        
        assertNotSame(original, updated);
        assertEquals(today, updated.getBeginDate());
        assertEquals(nextWeek, updated.getDueDate());
        assertEquals(yesterday, original.getBeginDate()); // Original unchanged
        assertEquals(today, original.getDueDate());
    }

    @Test
    void testWithDatesBothNull() {
        BeginAndDueDates original = new BeginAndDueDates(today, tomorrow);
        
        BeginAndDueDates updated = original.withDates(null, null);
        
        assertNotSame(original, updated);
        assertNull(updated.getBeginDate());
        assertNull(updated.getDueDate());
    }

    @Test
    void testWithDatesSameDates() {
        BeginAndDueDates original = new BeginAndDueDates(yesterday, yesterday);
        
        BeginAndDueDates updated = original.withDates(today, today);
        
        assertNotSame(original, updated);
        assertEquals(today, updated.getBeginDate());
        assertEquals(today, updated.getDueDate());
    }

    @Test
    void testWithDatesThrowsWhenBeginAfterDue() {
        BeginAndDueDates dates = new BeginAndDueDates(today, tomorrow);
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> dates.withDates(nextWeek, tomorrow)
        );
        
        assertEquals("Begin date cannot be after due date", exception.getMessage());
    }

    @Test
    void testWithDatesNullBeginValidDue() {
        BeginAndDueDates original = new BeginAndDueDates(today, tomorrow);
        
        BeginAndDueDates updated = original.withDates(null, nextWeek);
        
        assertNull(updated.getBeginDate());
        assertEquals(nextWeek, updated.getDueDate());
    }

    @Test
    void testWithDatesValidBeginNullDue() {
        BeginAndDueDates original = new BeginAndDueDates(today, tomorrow);
        
        BeginAndDueDates updated = original.withDates(yesterday, null);
        
        assertEquals(yesterday, updated.getBeginDate());
        assertNull(updated.getDueDate());
    }

    // Complex toString Tests
    @Test
    void testToStringComplexFormatting() {
        BeginAndDueDates dates = new BeginAndDueDates(
            LocalDate.of(2024, 12, 31),
            LocalDate.of(2025, 1, 1)
        );
        
        String result = dates.toString();
        assertTrue(result.contains("BeginAndDueDates{"));
        assertTrue(result.contains("beginDate=2024-12-31"));
        assertTrue(result.contains("dueDate=2025-01-01"));
        assertTrue(result.contains(", "));
    }

    @Test
    void testToStringOnlyBeginDateFormatting() {
        BeginAndDueDates dates = new BeginAndDueDates(
            LocalDate.of(2024, 6, 15),
            null
        );
        
        String result = dates.toString();
        assertEquals("BeginAndDueDates{beginDate=2024-06-15}", result);
        assertFalse(result.contains(", "));
        assertFalse(result.contains("dueDate"));
    }

    @Test
    void testToStringOnlyDueDateFormatting() {
        BeginAndDueDates dates = new BeginAndDueDates(
            null,
            LocalDate.of(2024, 6, 15)
        );
        
        String result = dates.toString();
        assertEquals("BeginAndDueDates{dueDate=2024-06-15}", result);
        assertFalse(result.contains(", "));
        assertFalse(result.contains("beginDate"));
    }

    @Test
    void testToStringEmptyFormatting() {
        BeginAndDueDates dates = new BeginAndDueDates(null, null);
        
        String result = dates.toString();
        assertEquals("BeginAndDueDates{}", result);
        assertFalse(result.contains("beginDate"));
        assertFalse(result.contains("dueDate"));
    }

    // Additional Edge Cases
    @Test
    void testImmutableUpdateChaining() {
        BeginAndDueDates original = new BeginAndDueDates(lastWeek, yesterday);
        
        BeginAndDueDates chained = original
            .withDueDate(nextWeek)  // First extend due date to allow begin date update
            .withBeginDate(today)   // Then update begin date
            .withDates(tomorrow, nextWeek.plusDays(1));
        
        // Original unchanged
        assertEquals(lastWeek, original.getBeginDate());
        assertEquals(yesterday, original.getDueDate());
        
        // Final result correct
        assertEquals(tomorrow, chained.getBeginDate());
        assertEquals(nextWeek.plusDays(1), chained.getDueDate());
    }

    @Test
    void testEqualsAndHashCodeConsistencyWithImmutableMethods() {
        BeginAndDueDates original = new BeginAndDueDates(today, tomorrow);
        BeginAndDueDates copy = new BeginAndDueDates(today, tomorrow);
        BeginAndDueDates fromWith = original.withDates(today, tomorrow);
        
        assertEquals(original, copy);
        assertEquals(original, fromWith);
        assertEquals(copy, fromWith);
        
        assertEquals(original.hashCode(), copy.hashCode());
        assertEquals(original.hashCode(), fromWith.hashCode());
    }

    @Test
    void testWithMethodsPreserveImmutability() {
        BeginAndDueDates original = new BeginAndDueDates(today, tomorrow);
        
        // Multiple operations on same object
        BeginAndDueDates result1 = original.withBeginDate(yesterday);
        BeginAndDueDates result2 = original.withDueDate(nextWeek);
        BeginAndDueDates result3 = original.withDates(null, null);
        
        // Original unchanged
        assertEquals(today, original.getBeginDate());
        assertEquals(tomorrow, original.getDueDate());
        
        // Each result different
        assertNotEquals(result1, result2);
        assertNotEquals(result2, result3);
        assertNotEquals(result1, result3);
    }
}