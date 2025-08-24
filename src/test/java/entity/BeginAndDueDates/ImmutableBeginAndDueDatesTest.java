package entity.BeginAndDueDates;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for ImmutableBeginAndDueDates.
 * Tests immutable wrapper functionality for BeginAndDueDates entity.
 * Ensures 100% coverage of all methods, constructors, and edge cases.
 */
class ImmutableBeginAndDueDatesTest {

    private LocalDate today;
    private LocalDate tomorrow;
    private LocalDate yesterday;
    private LocalDate nextWeek;
    private BeginAndDueDates mutableDates;

    @BeforeEach
    void setUp() {
        today = LocalDate.now();
        tomorrow = today.plusDays(1);
        yesterday = today.minusDays(1);
        nextWeek = today.plusWeeks(1);
        mutableDates = new BeginAndDueDates(today, tomorrow);
    }

    // Constructor Tests - BeginAndDueDates parameter

    @Test
    @DisplayName("Should create immutable wrapper from mutable BeginAndDueDates")
    void testConstructorFromMutableDates() {
        ImmutableBeginAndDueDates immutable = new ImmutableBeginAndDueDates(mutableDates);
        
        assertEquals(today, immutable.getBeginDate());
        assertEquals(tomorrow, immutable.getDueDate());
    }

    @Test
    @DisplayName("Should throw exception when wrapping null BeginAndDueDates")
    void testConstructorWithNullDates() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new ImmutableBeginAndDueDates((BeginAndDueDates) null)
        );
        assertEquals("BeginAndDueDates cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("Should wrap mutable dates with null begin date")
    void testConstructorFromMutableWithNullBeginDate() {
        BeginAndDueDates datesWithNullBegin = new BeginAndDueDates(null, tomorrow);
        ImmutableBeginAndDueDates immutable = new ImmutableBeginAndDueDates(datesWithNullBegin);
        
        assertNull(immutable.getBeginDate());
        assertEquals(tomorrow, immutable.getDueDate());
    }

    @Test
    @DisplayName("Should wrap mutable dates with null due date")
    void testConstructorFromMutableWithNullDueDate() {
        BeginAndDueDates datesWithNullDue = new BeginAndDueDates(today, null);
        ImmutableBeginAndDueDates immutable = new ImmutableBeginAndDueDates(datesWithNullDue);
        
        assertEquals(today, immutable.getBeginDate());
        assertNull(immutable.getDueDate());
    }

    @Test
    @DisplayName("Should wrap mutable dates with both null dates")
    void testConstructorFromMutableWithBothNull() {
        BeginAndDueDates datesWithBothNull = new BeginAndDueDates(null, null);
        ImmutableBeginAndDueDates immutable = new ImmutableBeginAndDueDates(datesWithBothNull);
        
        assertNull(immutable.getBeginDate());
        assertNull(immutable.getDueDate());
    }

    // Constructor Tests - Direct LocalDate parameters

    @Test
    @DisplayName("Should create immutable with valid begin and due dates")
    void testConstructorWithValidDates() {
        ImmutableBeginAndDueDates immutable = new ImmutableBeginAndDueDates(today, tomorrow);
        
        assertEquals(today, immutable.getBeginDate());
        assertEquals(tomorrow, immutable.getDueDate());
    }

    @Test
    @DisplayName("Should create immutable with same begin and due date")
    void testConstructorWithSameDates() {
        ImmutableBeginAndDueDates immutable = new ImmutableBeginAndDueDates(today, today);
        
        assertEquals(today, immutable.getBeginDate());
        assertEquals(today, immutable.getDueDate());
    }

    @Test
    @DisplayName("Should create immutable with null begin date")
    void testConstructorWithNullBeginDate() {
        ImmutableBeginAndDueDates immutable = new ImmutableBeginAndDueDates(null, tomorrow);
        
        assertNull(immutable.getBeginDate());
        assertEquals(tomorrow, immutable.getDueDate());
    }

    @Test
    @DisplayName("Should create immutable with null due date")
    void testConstructorWithNullDueDate() {
        ImmutableBeginAndDueDates immutable = new ImmutableBeginAndDueDates(today, null);
        
        assertEquals(today, immutable.getBeginDate());
        assertNull(immutable.getDueDate());
    }

    @Test
    @DisplayName("Should create immutable with both dates null")
    void testConstructorWithBothDatesNull() {
        ImmutableBeginAndDueDates immutable = new ImmutableBeginAndDueDates(null, null);
        
        assertNull(immutable.getBeginDate());
        assertNull(immutable.getDueDate());
    }

    @Test
    @DisplayName("Should throw exception when begin date is after due date")
    void testConstructorWithBeginAfterDue() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new ImmutableBeginAndDueDates(tomorrow, today)
        );
        assertEquals("Begin date cannot be after due date", exception.getMessage());
    }

    // Getter Tests

    @Test
    @DisplayName("Should return correct begin date")
    void testGetBeginDate() {
        ImmutableBeginAndDueDates immutable = new ImmutableBeginAndDueDates(today, tomorrow);
        assertEquals(today, immutable.getBeginDate());
    }

    @Test
    @DisplayName("Should return correct due date")
    void testGetDueDate() {
        ImmutableBeginAndDueDates immutable = new ImmutableBeginAndDueDates(today, tomorrow);
        assertEquals(tomorrow, immutable.getDueDate());
    }

    @Test
    @DisplayName("Should return null begin date when null")
    void testGetBeginDateNull() {
        ImmutableBeginAndDueDates immutable = new ImmutableBeginAndDueDates(null, tomorrow);
        assertNull(immutable.getBeginDate());
    }

    @Test
    @DisplayName("Should return null due date when null")
    void testGetDueDateNull() {
        ImmutableBeginAndDueDates immutable = new ImmutableBeginAndDueDates(today, null);
        assertNull(immutable.getDueDate());
    }

    // Setter Tests (No-op methods)

    @Test
    @DisplayName("Should not modify begin date when setBeginDate called")
    void testSetBeginDateNoOp() {
        ImmutableBeginAndDueDates immutable = new ImmutableBeginAndDueDates(today, tomorrow);
        LocalDate originalBegin = immutable.getBeginDate();
        
        immutable.setBeginDate(nextWeek);
        
        assertEquals(originalBegin, immutable.getBeginDate());
    }

    @Test
    @DisplayName("Should not modify due date when setDueDate called")
    void testSetDueDateNoOp() {
        ImmutableBeginAndDueDates immutable = new ImmutableBeginAndDueDates(today, tomorrow);
        LocalDate originalDue = immutable.getDueDate();
        
        immutable.setDueDate(nextWeek);
        
        assertEquals(originalDue, immutable.getDueDate());
    }

    // hasDueDate Tests

    @Test
    @DisplayName("Should return true when due date exists")
    void testHasDueDateTrue() {
        ImmutableBeginAndDueDates immutable = new ImmutableBeginAndDueDates(today, tomorrow);
        assertTrue(immutable.hasDueDate());
    }

    @Test
    @DisplayName("Should return false when due date is null")
    void testHasDueDateFalse() {
        ImmutableBeginAndDueDates immutable = new ImmutableBeginAndDueDates(today, null);
        assertFalse(immutable.hasDueDate());
    }

    // isOverdue Tests

    @Test
    @DisplayName("Should return false when due date is null")
    void testIsOverdueWithNullDueDate() {
        ImmutableBeginAndDueDates immutable = new ImmutableBeginAndDueDates(today, null);
        assertFalse(immutable.isOverdue());
    }

    @Test
    @DisplayName("Should return true when due date is in the past")
    void testIsOverdueWithPastDueDate() {
        ImmutableBeginAndDueDates immutable = new ImmutableBeginAndDueDates(yesterday.minusDays(1), yesterday);
        assertTrue(immutable.isOverdue());
    }

    @Test
    @DisplayName("Should return false when due date is today")
    void testIsOverdueWithTodayDueDate() {
        ImmutableBeginAndDueDates immutable = new ImmutableBeginAndDueDates(today, today);
        assertFalse(immutable.isOverdue());
    }

    @Test
    @DisplayName("Should return false when due date is in the future")
    void testIsOverdueWithFutureDueDate() {
        ImmutableBeginAndDueDates immutable = new ImmutableBeginAndDueDates(today, tomorrow);
        assertFalse(immutable.isOverdue());
    }

    // isDueTodayOrFuture Tests

    @Test
    @DisplayName("Should return false when due date is null")
    void testIsDueTodayOrFutureWithNullDueDate() {
        ImmutableBeginAndDueDates immutable = new ImmutableBeginAndDueDates(today, null);
        assertFalse(immutable.isDueTodayOrFuture());
    }

    @Test
    @DisplayName("Should return false when due date is in the past")
    void testIsDueTodayOrFutureWithPastDueDate() {
        ImmutableBeginAndDueDates immutable = new ImmutableBeginAndDueDates(yesterday.minusDays(1), yesterday);
        assertFalse(immutable.isDueTodayOrFuture());
    }

    @Test
    @DisplayName("Should return true when due date is today")
    void testIsDueTodayOrFutureWithTodayDueDate() {
        ImmutableBeginAndDueDates immutable = new ImmutableBeginAndDueDates(today, today);
        assertTrue(immutable.isDueTodayOrFuture());
    }

    @Test
    @DisplayName("Should return true when due date is in the future")
    void testIsDueTodayOrFutureWithFutureDueDate() {
        ImmutableBeginAndDueDates immutable = new ImmutableBeginAndDueDates(today, tomorrow);
        assertTrue(immutable.isDueTodayOrFuture());
    }

    // withBeginDate Tests

    @Test
    @DisplayName("Should create new instance with updated begin date")
    void testWithBeginDate() {
        ImmutableBeginAndDueDates original = new ImmutableBeginAndDueDates(today, tomorrow);
        ImmutableBeginAndDueDates updated = original.withBeginDate(yesterday);
        
        assertEquals(yesterday, updated.getBeginDate());
        assertEquals(tomorrow, updated.getDueDate());
        assertEquals(today, original.getBeginDate()); // Original unchanged
    }

    @Test
    @DisplayName("Should create new instance with null begin date")
    void testWithBeginDateNull() {
        ImmutableBeginAndDueDates original = new ImmutableBeginAndDueDates(today, tomorrow);
        ImmutableBeginAndDueDates updated = original.withBeginDate(null);
        
        assertNull(updated.getBeginDate());
        assertEquals(tomorrow, updated.getDueDate());
    }

    @Test
    @DisplayName("Should throw exception when new begin date is after due date")
    void testWithBeginDateAfterDue() {
        ImmutableBeginAndDueDates original = new ImmutableBeginAndDueDates(today, tomorrow);
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> original.withBeginDate(nextWeek)
        );
        assertEquals("Begin date cannot be after due date", exception.getMessage());
    }

    @Test
    @DisplayName("Should allow begin date update when due date is null")
    void testWithBeginDateWhenDueDateNull() {
        ImmutableBeginAndDueDates original = new ImmutableBeginAndDueDates(today, null);
        ImmutableBeginAndDueDates updated = original.withBeginDate(tomorrow);
        
        assertEquals(tomorrow, updated.getBeginDate());
        assertNull(updated.getDueDate());
    }

    // withDueDate Tests

    @Test
    @DisplayName("Should create new instance with updated due date")
    void testWithDueDate() {
        ImmutableBeginAndDueDates original = new ImmutableBeginAndDueDates(today, tomorrow);
        ImmutableBeginAndDueDates updated = original.withDueDate(nextWeek);
        
        assertEquals(today, updated.getBeginDate());
        assertEquals(nextWeek, updated.getDueDate());
        assertEquals(tomorrow, original.getDueDate()); // Original unchanged
    }

    @Test
    @DisplayName("Should create new instance with null due date")
    void testWithDueDateNull() {
        ImmutableBeginAndDueDates original = new ImmutableBeginAndDueDates(today, tomorrow);
        ImmutableBeginAndDueDates updated = original.withDueDate(null);
        
        assertEquals(today, updated.getBeginDate());
        assertNull(updated.getDueDate());
    }

    @Test
    @DisplayName("Should throw exception when new due date is before begin date")
    void testWithDueDateBeforeBegin() {
        ImmutableBeginAndDueDates original = new ImmutableBeginAndDueDates(today, tomorrow);
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> original.withDueDate(yesterday)
        );
        assertEquals("Due date cannot be before begin date", exception.getMessage());
    }

    @Test
    @DisplayName("Should allow due date update when begin date is null")
    void testWithDueDateWhenBeginDateNull() {
        ImmutableBeginAndDueDates original = new ImmutableBeginAndDueDates(null, tomorrow);
        ImmutableBeginAndDueDates updated = original.withDueDate(today);
        
        assertNull(updated.getBeginDate());
        assertEquals(today, updated.getDueDate());
    }

    // withDates Tests

    @Test
    @DisplayName("Should create new instance with both dates updated")
    void testWithDates() {
        ImmutableBeginAndDueDates original = new ImmutableBeginAndDueDates(today, tomorrow);
        ImmutableBeginAndDueDates updated = original.withDates(yesterday, nextWeek);
        
        assertEquals(yesterday, updated.getBeginDate());
        assertEquals(nextWeek, updated.getDueDate());
        assertEquals(today, original.getBeginDate()); // Original unchanged
        assertEquals(tomorrow, original.getDueDate()); // Original unchanged
    }

    @Test
    @DisplayName("Should create new instance with same dates")
    void testWithDatesSameDates() {
        ImmutableBeginAndDueDates original = new ImmutableBeginAndDueDates(today, tomorrow);
        ImmutableBeginAndDueDates updated = original.withDates(today, today);
        
        assertEquals(today, updated.getBeginDate());
        assertEquals(today, updated.getDueDate());
    }

    @Test
    @DisplayName("Should create new instance with both dates null")
    void testWithDatesBothNull() {
        ImmutableBeginAndDueDates original = new ImmutableBeginAndDueDates(today, tomorrow);
        ImmutableBeginAndDueDates updated = original.withDates(null, null);
        
        assertNull(updated.getBeginDate());
        assertNull(updated.getDueDate());
    }

    @Test
    @DisplayName("Should throw exception when begin date is after due date in withDates")
    void testWithDatesBeginAfterDue() {
        ImmutableBeginAndDueDates original = new ImmutableBeginAndDueDates(today, tomorrow);
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> original.withDates(nextWeek, today)
        );
        assertEquals("Begin date cannot be after due date", exception.getMessage());
    }

    // toMutableBeginAndDueDates Tests

    @Test
    @DisplayName("Should convert to mutable BeginAndDueDates with same data")
    void testToMutableBeginAndDueDates() {
        ImmutableBeginAndDueDates immutable = new ImmutableBeginAndDueDates(today, tomorrow);
        BeginAndDueDates mutable = immutable.toMutableBeginAndDueDates();
        
        assertEquals(today, mutable.getBeginDate());
        assertEquals(tomorrow, mutable.getDueDate());
        assertNotSame(immutable, mutable);
    }

    @Test
    @DisplayName("Should convert to mutable with null dates")
    void testToMutableBeginAndDueDatesWithNulls() {
        ImmutableBeginAndDueDates immutable = new ImmutableBeginAndDueDates(null, null);
        BeginAndDueDates mutable = immutable.toMutableBeginAndDueDates();
        
        assertNull(mutable.getBeginDate());
        assertNull(mutable.getDueDate());
    }

    // equals Tests

    @Test
    @DisplayName("Should be equal to itself")
    void testEqualsIdentity() {
        ImmutableBeginAndDueDates immutable = new ImmutableBeginAndDueDates(today, tomorrow);
        assertEquals(immutable, immutable);
    }

    @Test
    @DisplayName("Should be equal to another instance with same dates")
    void testEqualsWithSameDates() {
        ImmutableBeginAndDueDates immutable1 = new ImmutableBeginAndDueDates(today, tomorrow);
        ImmutableBeginAndDueDates immutable2 = new ImmutableBeginAndDueDates(today, tomorrow);
        
        assertEquals(immutable1, immutable2);
        assertEquals(immutable2, immutable1);
    }

    @Test
    @DisplayName("Should not be equal to null")
    void testEqualsWithNull() {
        ImmutableBeginAndDueDates immutable = new ImmutableBeginAndDueDates(today, tomorrow);
        assertNotEquals(immutable, null);
    }

    @Test
    @DisplayName("Should not be equal to different class")
    void testEqualsWithDifferentClass() {
        ImmutableBeginAndDueDates immutable = new ImmutableBeginAndDueDates(today, tomorrow);
        BeginAndDueDates mutable = new BeginAndDueDates(today, tomorrow);
        
        assertNotEquals(immutable, mutable);
    }

    @Test
    @DisplayName("Should not be equal with different begin date")
    void testEqualsWithDifferentBeginDate() {
        ImmutableBeginAndDueDates immutable1 = new ImmutableBeginAndDueDates(today, tomorrow);
        ImmutableBeginAndDueDates immutable2 = new ImmutableBeginAndDueDates(yesterday, tomorrow);
        
        assertNotEquals(immutable1, immutable2);
    }

    @Test
    @DisplayName("Should not be equal with different due date")
    void testEqualsWithDifferentDueDate() {
        ImmutableBeginAndDueDates immutable1 = new ImmutableBeginAndDueDates(today, tomorrow);
        ImmutableBeginAndDueDates immutable2 = new ImmutableBeginAndDueDates(today, nextWeek);
        
        assertNotEquals(immutable1, immutable2);
    }

    @Test
    @DisplayName("Should be equal with both dates null")
    void testEqualsWithBothNull() {
        ImmutableBeginAndDueDates immutable1 = new ImmutableBeginAndDueDates(null, null);
        ImmutableBeginAndDueDates immutable2 = new ImmutableBeginAndDueDates(null, null);
        
        assertEquals(immutable1, immutable2);
    }

    @Test
    @DisplayName("Should be equal with same null begin date")
    void testEqualsWithNullBeginDate() {
        ImmutableBeginAndDueDates immutable1 = new ImmutableBeginAndDueDates(null, tomorrow);
        ImmutableBeginAndDueDates immutable2 = new ImmutableBeginAndDueDates(null, tomorrow);
        
        assertEquals(immutable1, immutable2);
    }

    @Test
    @DisplayName("Should be equal with same null due date")
    void testEqualsWithNullDueDate() {
        ImmutableBeginAndDueDates immutable1 = new ImmutableBeginAndDueDates(today, null);
        ImmutableBeginAndDueDates immutable2 = new ImmutableBeginAndDueDates(today, null);
        
        assertEquals(immutable1, immutable2);
    }

    // hashCode Tests

    @Test
    @DisplayName("Should have same hash code for equal objects")
    void testHashCodeConsistency() {
        ImmutableBeginAndDueDates immutable1 = new ImmutableBeginAndDueDates(today, tomorrow);
        ImmutableBeginAndDueDates immutable2 = new ImmutableBeginAndDueDates(today, tomorrow);
        
        assertEquals(immutable1.hashCode(), immutable2.hashCode());
    }

    @Test
    @DisplayName("Should have consistent hash code")
    void testHashCodeStability() {
        ImmutableBeginAndDueDates immutable = new ImmutableBeginAndDueDates(today, tomorrow);
        int hash1 = immutable.hashCode();
        int hash2 = immutable.hashCode();
        
        assertEquals(hash1, hash2);
    }

    @Test
    @DisplayName("Should handle hash code with null dates")
    void testHashCodeWithNulls() {
        ImmutableBeginAndDueDates immutable = new ImmutableBeginAndDueDates(null, null);
        assertDoesNotThrow(() -> immutable.hashCode());
    }

    // toString Tests

    @Test
    @DisplayName("Should format toString with both dates")
    void testToStringWithBothDates() {
        ImmutableBeginAndDueDates immutable = new ImmutableBeginAndDueDates(today, tomorrow);
        String result = immutable.toString();
        
        assertTrue(result.contains("ImmutableBeginAndDueDates{"));
        assertTrue(result.contains("beginDate=" + today));
        assertTrue(result.contains("dueDate=" + tomorrow));
        assertTrue(result.contains("}"));
    }

    @Test
    @DisplayName("Should format toString with only begin date")
    void testToStringWithOnlyBeginDate() {
        ImmutableBeginAndDueDates immutable = new ImmutableBeginAndDueDates(today, null);
        String result = immutable.toString();
        
        assertTrue(result.contains("ImmutableBeginAndDueDates{"));
        assertTrue(result.contains("beginDate=" + today));
        assertFalse(result.contains("dueDate="));
        assertTrue(result.contains("}"));
    }

    @Test
    @DisplayName("Should format toString with only due date")
    void testToStringWithOnlyDueDate() {
        ImmutableBeginAndDueDates immutable = new ImmutableBeginAndDueDates(null, tomorrow);
        String result = immutable.toString();
        
        assertTrue(result.contains("ImmutableBeginAndDueDates{"));
        assertFalse(result.contains("beginDate="));
        assertTrue(result.contains("dueDate=" + tomorrow));
        assertTrue(result.contains("}"));
    }

    @Test
    @DisplayName("Should format toString with both dates null")
    void testToStringWithBothNull() {
        ImmutableBeginAndDueDates immutable = new ImmutableBeginAndDueDates(null, null);
        String result = immutable.toString();
        
        assertEquals("ImmutableBeginAndDueDates{}", result);
    }

    @Test
    @DisplayName("Should include comma separator when both dates present")
    void testToStringCommaSeparator() {
        ImmutableBeginAndDueDates immutable = new ImmutableBeginAndDueDates(today, tomorrow);
        String result = immutable.toString();
        
        assertTrue(result.contains(", "));
        assertTrue(result.matches(".*beginDate=.*,\\s*dueDate=.*"));
    }
}