package app.goalPage;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class GoalInputValidatorTest {

    // -------- validateString --------
    @Test
    void validateString_returnsInput_whenNonEmpty() {
        String s = "  Learn Java  ";
        String out = GoalInputValidator.validateString(s, "Title");
        assertSame(s, out); // method returns original, not trimmed
    }

    @Test
    void validateString_throws_whenNull() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> GoalInputValidator.validateString(null, "Title")
        );
        assertEquals("Title cannot be empty.", ex.getMessage());
    }

    @Test
    void validateString_throws_whenBlank() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> GoalInputValidator.validateString("   ", "Title")
        );
        assertEquals("Title cannot be empty.", ex.getMessage());
    }

    // -------- validatePositiveInteger --------
    @Test
    void validatePositiveInteger_parsesValidNumber() {
        assertEquals(42, GoalInputValidator.validatePositiveInteger("42", "Count"));
    }

    @Test
    void validatePositiveInteger_allowsZero_perImplementation() {
        assertEquals(0, GoalInputValidator.validatePositiveInteger("0", "Count"));
    }

    @Test
    void validatePositiveInteger_throws_whenNegative() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> GoalInputValidator.validatePositiveInteger("-1", "Count")
        );
        assertEquals("Count must be a positive integer.", ex.getMessage());
    }

    @Test
    void validatePositiveInteger_throws_whenNotANumber() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> GoalInputValidator.validatePositiveInteger("abc", "Count")
        );
        assertEquals("Invalid format for Count. Please enter an integer.", ex.getMessage());
    }

    // -------- validateDate --------
    @Test
    void validateDate_parsesIsoDate() {
        LocalDate d = GoalInputValidator.validateDate("2025-08-21", "Start Date");
        assertEquals(LocalDate.of(2025, 8, 21), d);
    }

    @Test
    void validateDate_throws_whenInvalidFormat() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> GoalInputValidator.validateDate("21/08/2025", "Start Date")
        );
        assertEquals("Invalid date format for Start Date. Please use YYYY-MM-DD.", ex.getMessage());
    }

    // -------- validateDateRange --------
    @Test
    void validateDateRange_allowsEqualDates() {
        LocalDate d = LocalDate.of(2025, 8, 21);
        assertDoesNotThrow(() -> GoalInputValidator.validateDateRange(d, d));
    }

    @Test
    void validateDateRange_allowsStartBeforeEnd() {
        LocalDate start = LocalDate.of(2025, 8, 20);
        LocalDate end = LocalDate.of(2025, 8, 21);
        assertDoesNotThrow(() -> GoalInputValidator.validateDateRange(start, end));
    }

    @Test
    void validateDateRange_throws_whenStartAfterEnd() {
        LocalDate start = LocalDate.of(2025, 8, 22);
        LocalDate end = LocalDate.of(2025, 8, 21);
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> GoalInputValidator.validateDateRange(start, end)
        );
        assertEquals("Start Date cannot be after End Date.", ex.getMessage());
    }
}
