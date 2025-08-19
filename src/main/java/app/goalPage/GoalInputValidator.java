package app.goalPage;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class GoalInputValidator {

    /**
     * Validates that a string is not null or empty.
     *
     * @param input The string to validate.
     * @param fieldName The name of the field for error messages.
     * @return The validated string.
     * @throws IllegalArgumentException if the input is null or empty.
     */
    public static String validateString(String input, String fieldName) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty.");
        }
        return input;
    }

    /**
     * Validates that a string represents a valid positive integer.
     *
     * @param input The string to validate.
     * @param fieldName The name of the field for error messages.
     * @return The validated integer.
     * @throws IllegalArgumentException if the input is not a valid number or is not positive.
     */
    public static int validatePositiveInteger(String input, String fieldName) {
        try {
            final int value = Integer.parseInt(input);
            if (value < 0) {
                throw new IllegalArgumentException(fieldName + " must be a positive integer.");
            }
            return value;
        }
        catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid format for " + fieldName + ". Please enter an integer.");
        }
    }

    /**
     * Validates that a string represents a valid LocalDate.
     *
     * @param input The string to validate.
     * @param fieldName The name of the field for error messages.
     * @return The validated LocalDate object.
     * @throws IllegalArgumentException if the date format is invalid (must be YYYY-MM-DD).
     */
    public static LocalDate validateDate(String input, String fieldName) {
        try {
            return LocalDate.parse(input);
        }
        catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Invalid date format for " + fieldName + ". Please use YYYY-MM-DD.");
        }
    }

    /**
     * Validates that a start date is not after the end date.
     *
     * @param startDate The start date.
     * @param endDate The end date.
     * @throws IllegalArgumentException if the start date is after the end date.
     */
    public static void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start Date cannot be after End Date.");
        }
    }
}
