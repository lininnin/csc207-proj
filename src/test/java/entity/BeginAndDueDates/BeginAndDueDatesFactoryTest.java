package entity.BeginAndDueDates;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for BeginAndDueDatesFactory following Clean Architecture principles.
 * Tests factory methods for creating BeginAndDueDates instances.
 */
class BeginAndDueDatesFactoryTest {

    private BeginAndDueDatesFactory factory;
    private LocalDate today;
    private LocalDate tomorrow;
    private LocalDate yesterday;
    private LocalDate nextWeek;

    @BeforeEach
    void setUp() {
        factory = new BeginAndDueDatesFactory();
        today = LocalDate.now();
        tomorrow = today.plusDays(1);
        yesterday = today.minusDays(1);
        nextWeek = today.plusWeeks(1);
    }

    // Basic create method tests
    @Test
    void testCreateWithBothDates() {
        BeginAndDueDatesInterf dates = factory.create(today, tomorrow);
        
        assertNotNull(dates);
        assertEquals(today, dates.getBeginDate());
        assertEquals(tomorrow, dates.getDueDate());
        assertTrue(dates.hasDueDate());
    }

    @Test
    void testCreateWithNullDueDate() {
        BeginAndDueDatesInterf dates = factory.create(today, null);
        
        assertNotNull(dates);
        assertEquals(today, dates.getBeginDate());
        assertNull(dates.getDueDate());
        assertFalse(dates.hasDueDate());
    }

    @Test
    void testCreateWithNullBeginDate() {
        BeginAndDueDatesInterf dates = factory.create(null, tomorrow);
        
        assertNotNull(dates);
        assertNull(dates.getBeginDate());
        assertEquals(tomorrow, dates.getDueDate());
        assertTrue(dates.hasDueDate());
    }

    @Test
    void testCreateWithBothNull() {
        BeginAndDueDatesInterf dates = factory.create(null, null);
        
        assertNotNull(dates);
        assertNull(dates.getBeginDate());
        assertNull(dates.getDueDate());
        assertFalse(dates.hasDueDate());
    }

    @Test
    void testCreateThrowsWhenBeginAfterDue() {
        assertThrows(
            IllegalArgumentException.class,
            () -> factory.create(tomorrow, today)
        );
    }

    // createOpenEnded tests
    @Test
    void testCreateOpenEndedWithValidDate() {
        BeginAndDueDatesInterf dates = factory.createOpenEnded(today);
        
        assertNotNull(dates);
        assertEquals(today, dates.getBeginDate());
        assertNull(dates.getDueDate());
        assertFalse(dates.hasDueDate());
    }

    @Test
    void testCreateOpenEndedWithNull() {
        BeginAndDueDatesInterf dates = factory.createOpenEnded(null);
        
        assertNotNull(dates);
        assertNull(dates.getBeginDate());
        assertNull(dates.getDueDate());
        assertFalse(dates.hasDueDate());
    }

    @Test
    void testCreateOpenEndedWithPastDate() {
        BeginAndDueDatesInterf dates = factory.createOpenEnded(yesterday);
        
        assertNotNull(dates);
        assertEquals(yesterday, dates.getBeginDate());
        assertNull(dates.getDueDate());
        assertFalse(dates.hasDueDate());
    }

    @Test
    void testCreateOpenEndedWithFutureDate() {
        BeginAndDueDatesInterf dates = factory.createOpenEnded(nextWeek);
        
        assertNotNull(dates);
        assertEquals(nextWeek, dates.getBeginDate());
        assertNull(dates.getDueDate());
        assertFalse(dates.hasDueDate());
    }

    // createEmpty tests
    @Test
    void testCreateEmpty() {
        BeginAndDueDatesInterf dates = factory.createEmpty();
        
        assertNotNull(dates);
        assertNull(dates.getBeginDate());
        assertNull(dates.getDueDate());
        assertFalse(dates.hasDueDate());
    }

    @Test
    void testCreateEmptyIsIndependent() {
        BeginAndDueDatesInterf dates1 = factory.createEmpty();
        BeginAndDueDatesInterf dates2 = factory.createEmpty();
        
        assertNotSame(dates1, dates2);
        assertEquals(dates1, dates2); // Equal but not same instance
    }

    // createStartingToday tests
    @Test
    void testCreateStartingTodayWithDueDate() {
        BeginAndDueDatesInterf dates = factory.createStartingToday(7);
        
        assertNotNull(dates);
        assertEquals(today, dates.getBeginDate());
        assertEquals(today.plusDays(7), dates.getDueDate());
        assertTrue(dates.hasDueDate());
        assertFalse(dates.isOverdue());
        assertTrue(dates.isDueTodayOrFuture());
    }

    @Test
    void testCreateStartingTodayWithNullDays() {
        BeginAndDueDatesInterf dates = factory.createStartingToday(null);
        
        assertNotNull(dates);
        assertEquals(today, dates.getBeginDate());
        assertNull(dates.getDueDate());
        assertFalse(dates.hasDueDate());
    }

    @Test
    void testCreateStartingTodayWithZeroDays() {
        BeginAndDueDatesInterf dates = factory.createStartingToday(0);
        
        assertNotNull(dates);
        assertEquals(today, dates.getBeginDate());
        assertEquals(today, dates.getDueDate());
        assertTrue(dates.hasDueDate());
        assertFalse(dates.isOverdue());
        assertTrue(dates.isDueTodayOrFuture());
    }

    @Test
    void testCreateStartingTodayWithNegativeDays() {
        // Negative days would create a due date before today (begin date)
        // This should throw an exception because begin > due
        assertThrows(
            IllegalArgumentException.class,
            () -> factory.createStartingToday(-1)
        );
    }

    @Test
    void testCreateStartingTodayWithLargeDuration() {
        BeginAndDueDatesInterf dates = factory.createStartingToday(365);
        
        assertNotNull(dates);
        assertEquals(today, dates.getBeginDate());
        assertEquals(today.plusDays(365), dates.getDueDate());
        assertTrue(dates.hasDueDate());
        assertFalse(dates.isOverdue());
        assertTrue(dates.isDueTodayOrFuture());
    }

    // createWithDuration tests
    @Test
    void testCreateWithDurationValidInputs() {
        BeginAndDueDatesInterf dates = factory.createWithDuration(today, 10);
        
        assertNotNull(dates);
        assertEquals(today, dates.getBeginDate());
        assertEquals(today.plusDays(10), dates.getDueDate());
        assertTrue(dates.hasDueDate());
    }

    @Test
    void testCreateWithDurationZeroDays() {
        BeginAndDueDatesInterf dates = factory.createWithDuration(today, 0);
        
        assertNotNull(dates);
        assertEquals(today, dates.getBeginDate());
        assertEquals(today, dates.getDueDate());
        assertTrue(dates.hasDueDate());
    }

    @Test
    void testCreateWithDurationNegativeDays() {
        // Negative duration would create due date before begin date
        // This should throw an exception because begin > due
        assertThrows(
            IllegalArgumentException.class,
            () -> factory.createWithDuration(today, -1)
        );
    }

    @Test
    void testCreateWithDurationNullBeginDate() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> factory.createWithDuration(null, 10)
        );
        
        assertEquals("Begin date cannot be null when creating with duration", 
                    exception.getMessage());
    }

    @Test
    void testCreateWithDurationPastBeginDate() {
        BeginAndDueDatesInterf dates = factory.createWithDuration(yesterday, 5);
        
        assertNotNull(dates);
        assertEquals(yesterday, dates.getBeginDate());
        assertEquals(yesterday.plusDays(5), dates.getDueDate());
        assertTrue(dates.hasDueDate());
    }

    @Test
    void testCreateWithDurationFutureBeginDate() {
        BeginAndDueDatesInterf dates = factory.createWithDuration(nextWeek, 30);
        
        assertNotNull(dates);
        assertEquals(nextWeek, dates.getBeginDate());
        assertEquals(nextWeek.plusDays(30), dates.getDueDate());
        assertTrue(dates.hasDueDate());
        assertFalse(dates.isOverdue());
        assertTrue(dates.isDueTodayOrFuture());
    }

    // Test factory creates independent instances
    @Test
    void testFactoryCreatesIndependentInstances() {
        BeginAndDueDatesInterf dates1 = factory.create(today, tomorrow);
        BeginAndDueDatesInterf dates2 = factory.create(today, tomorrow);
        
        assertNotSame(dates1, dates2);
        assertEquals(dates1, dates2);
        
        // Modifying one shouldn't affect the other
        dates1.setDueDate(nextWeek);
        assertNotEquals(dates1.getDueDate(), dates2.getDueDate());
        assertEquals(tomorrow, dates2.getDueDate());
    }

    // Test factory method combinations
    @Test
    void testFactoryMethodVariations() {
        // Test all factory methods produce valid objects
        BeginAndDueDatesInterf dates1 = factory.create(today, tomorrow);
        BeginAndDueDatesInterf dates2 = factory.createOpenEnded(today);
        BeginAndDueDatesInterf dates3 = factory.createEmpty();
        BeginAndDueDatesInterf dates4 = factory.createStartingToday(5);
        BeginAndDueDatesInterf dates5 = factory.createWithDuration(today, 5);
        
        assertNotNull(dates1);
        assertNotNull(dates2);
        assertNotNull(dates3);
        assertNotNull(dates4);
        assertNotNull(dates5);
        
        // Verify dates4 and dates5 are equal (both start today with 5 day duration)
        assertEquals(dates4, dates5);
    }

    // Edge case tests
    @Test
    void testCreateWithMaxDuration() {
        // Test with very large duration
        int maxDays = 10000;
        BeginAndDueDatesInterf dates = factory.createWithDuration(today, maxDays);
        
        assertNotNull(dates);
        assertEquals(today, dates.getBeginDate());
        assertEquals(today.plusDays(maxDays), dates.getDueDate());
    }

    @Test
    void testCreateWithLeapYear() {
        LocalDate leapDay = LocalDate.of(2024, 2, 29);
        
        BeginAndDueDatesInterf dates1 = factory.create(leapDay, leapDay.plusDays(1));
        assertEquals(LocalDate.of(2024, 3, 1), dates1.getDueDate());
        
        BeginAndDueDatesInterf dates2 = factory.createWithDuration(leapDay, 365);
        assertEquals(LocalDate.of(2025, 2, 28), dates2.getDueDate());
    }

    @Test
    void testFactoryWithSpecialDates() {
        LocalDate minDate = LocalDate.MIN;
        LocalDate maxDate = LocalDate.MAX;
        
        // Should handle extreme dates
        BeginAndDueDatesInterf dates = factory.create(minDate, maxDate);
        
        assertNotNull(dates);
        assertEquals(minDate, dates.getBeginDate());
        assertEquals(maxDate, dates.getDueDate());
    }
}