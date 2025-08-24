package interface_adapter.alex.Settings_related.edit_notificationTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import use_case.alex.Settings_related.edit_notificationTime.EditNotificationTimeInputBoundary;
import use_case.alex.Settings_related.edit_notificationTime.EditNotificationTimeInputData;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

/**
 * Comprehensive test suite for EditNotificationTimeController.
 * Tests controller responsibilities in Clean Architecture.
 */
class EditNotificationTimeControllerTest {

    private EditNotificationTimeController controller;
    private EditNotificationTimeInputBoundary mockInteractor;

    @BeforeEach
    void setUp() {
        mockInteractor = mock(EditNotificationTimeInputBoundary.class);
        controller = new EditNotificationTimeController(mockInteractor);
    }

    @Test
    @DisplayName("Should create input data and call interactor with valid times")
    void testExecuteWithValidTimes() {
        // Given
        String reminder1 = "08:00";
        String reminder2 = "12:00";
        String reminder3 = "20:00";

        // When
        controller.execute(reminder1, reminder2, reminder3);

        // Then
        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getReminder1().equals(reminder1) &&
                inputData.getReminder2().equals(reminder2) &&
                inputData.getReminder3().equals(reminder3)
        ));
    }

    @Test
    @DisplayName("Should handle null reminder times")
    void testExecuteWithNullTimes() {
        // Given
        String reminder1 = null;
        String reminder2 = null;
        String reminder3 = null;

        // When
        controller.execute(reminder1, reminder2, reminder3);

        // Then
        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getReminder1() == null &&
                inputData.getReminder2() == null &&
                inputData.getReminder3() == null
        ));
    }

    @Test
    @DisplayName("Should handle empty string reminder times")
    void testExecuteWithEmptyTimes() {
        // Given
        String reminder1 = "";
        String reminder2 = "";
        String reminder3 = "";

        // When
        controller.execute(reminder1, reminder2, reminder3);

        // Then
        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getReminder1().equals("") &&
                inputData.getReminder2().equals("") &&
                inputData.getReminder3().equals("")
        ));
    }

    @Test
    @DisplayName("Should handle mixed null and valid times")
    void testExecuteWithMixedTimes() {
        // Given
        String reminder1 = "09:30";
        String reminder2 = null;
        String reminder3 = "18:45";

        // When
        controller.execute(reminder1, reminder2, reminder3);

        // Then
        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getReminder1().equals(reminder1) &&
                inputData.getReminder2() == null &&
                inputData.getReminder3().equals(reminder3)
        ));
    }

    @Test
    @DisplayName("Should handle invalid time format strings")
    void testExecuteWithInvalidTimeFormats() {
        // Given
        String reminder1 = "invalid";
        String reminder2 = "25:70";
        String reminder3 = "not-a-time";

        // When
        controller.execute(reminder1, reminder2, reminder3);

        // Then
        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getReminder1().equals(reminder1) &&
                inputData.getReminder2().equals(reminder2) &&
                inputData.getReminder3().equals(reminder3)
        ));
    }

    @Test
    @DisplayName("Should handle whitespace-only reminder times")
    void testExecuteWithWhitespaceOnlyTimes() {
        // Given
        String reminder1 = "   ";
        String reminder2 = "\t";
        String reminder3 = "\n";

        // When
        controller.execute(reminder1, reminder2, reminder3);

        // Then
        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getReminder1().equals(reminder1) &&
                inputData.getReminder2().equals(reminder2) &&
                inputData.getReminder3().equals(reminder3)
        ));
    }

    @Test
    @DisplayName("Should handle alternative time formats")
    void testExecuteWithAlternativeTimeFormats() {
        // Given
        String reminder1 = "8:00 AM";
        String reminder2 = "12:00 PM";
        String reminder3 = "8:00 PM";

        // When
        controller.execute(reminder1, reminder2, reminder3);

        // Then
        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getReminder1().equals(reminder1) &&
                inputData.getReminder2().equals(reminder2) &&
                inputData.getReminder3().equals(reminder3)
        ));
    }

    @Test
    @DisplayName("Should create new input data object for each call")
    void testCreatesNewInputDataForEachCall() {
        // When - Make multiple calls
        controller.execute("08:00", "12:00", "20:00");
        controller.execute("09:00", "13:00", "21:00");

        // Then - Verify interactor called twice with different data
        verify(mockInteractor, times(2)).execute(any(EditNotificationTimeInputData.class));
    }

    @Test
    @DisplayName("Should pass through all parameters unchanged")
    void testParameterPassthrough() {
        // Given
        String specificReminder1 = "07:30";
        String specificReminder2 = "14:15";
        String specificReminder3 = "22:45";

        // When
        controller.execute(specificReminder1, specificReminder2, specificReminder3);

        // Then - Verify exact parameter matching
        verify(mockInteractor).execute(argThat(inputData ->
                specificReminder1.equals(inputData.getReminder1()) &&
                specificReminder2.equals(inputData.getReminder2()) &&
                specificReminder3.equals(inputData.getReminder3())
        ));
    }

    @Test
    @DisplayName("Should handle edge case times correctly")
    void testExecuteWithEdgeCaseTimes() {
        // Given - Edge case times
        String reminder1 = "00:00"; // Midnight
        String reminder2 = "23:59"; // End of day
        String reminder3 = "12:00"; // Noon

        // When
        controller.execute(reminder1, reminder2, reminder3);

        // Then
        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getReminder1().equals(reminder1) &&
                inputData.getReminder2().equals(reminder2) &&
                inputData.getReminder3().equals(reminder3)
        ));
    }
}