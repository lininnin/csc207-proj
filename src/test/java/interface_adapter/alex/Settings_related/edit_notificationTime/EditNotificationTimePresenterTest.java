package interface_adapter.alex.Settings_related.edit_notificationTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import use_case.alex.Settings_related.edit_notificationTime.EditNotificationTimeOutputData;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

/**
 * Comprehensive test suite for EditNotificationTimePresenter.
 * Tests presenter responsibilities in Clean Architecture.
 */
class EditNotificationTimePresenterTest {

    private EditNotificationTimePresenter presenter;
    private EditNotificationTimeViewModel mockViewModel;

    @BeforeEach
    void setUp() {
        mockViewModel = mock(EditNotificationTimeViewModel.class);
        presenter = new EditNotificationTimePresenter(mockViewModel);
    }

    @Test
    @DisplayName("Should update view model state on success")
    void testPrepareSuccessView() {
        // Given
        EditNotificationTimeOutputData outputData = new EditNotificationTimeOutputData(
                "08:30", "13:15", "21:45", false
        );

        // When
        presenter.prepareSuccessView(outputData);

        // Then
        verify(mockViewModel).setState(any(EditNotificationTimeState.class));
        verify(mockViewModel).firePropertyChanged(EditNotificationTimeViewModel.EDIT_NOTIFICATION_TIME_STATE_PROPERTY);
    }

    @Test
    @DisplayName("Should set correct reminder times in success state")
    void testPrepareSuccessViewSetsReminderTimes() {
        // Given
        String expectedReminder1 = "07:00";
        String expectedReminder2 = "12:30";
        String expectedReminder3 = "19:15";
        EditNotificationTimeOutputData outputData = new EditNotificationTimeOutputData(
                expectedReminder1, expectedReminder2, expectedReminder3, false
        );

        // When
        presenter.prepareSuccessView(outputData);

        // Then
        verify(mockViewModel).setState(argThat(state -> {
            state.setReminder1(expectedReminder1);
            state.setReminder2(expectedReminder2);
            state.setReminder3(expectedReminder3);
            return expectedReminder1.equals(state.getReminder1()) &&
                   expectedReminder2.equals(state.getReminder2()) &&
                   expectedReminder3.equals(state.getReminder3());
        }));
    }

    @Test
    @DisplayName("Should set editing to false in success state")
    void testPrepareSuccessViewSetsEditingFalse() {
        // Given
        EditNotificationTimeOutputData outputData = new EditNotificationTimeOutputData(
                "08:00", "12:00", "20:00", false
        );

        // When
        presenter.prepareSuccessView(outputData);

        // Then
        verify(mockViewModel).setState(argThat(state -> {
            state.setEditing(false);
            return !state.isEditing();
        }));
    }

    @Test
    @DisplayName("Should clear error message in success state")
    void testPrepareSuccessViewClearsErrorMessage() {
        // Given
        EditNotificationTimeOutputData outputData = new EditNotificationTimeOutputData(
                "08:00", "12:00", "20:00", false
        );

        // When
        presenter.prepareSuccessView(outputData);

        // Then
        verify(mockViewModel).setState(argThat(state -> {
            state.setErrorMessage(null);
            return state.getErrorMessage() == null;
        }));
    }

    @Test
    @DisplayName("Should handle null reminder times in success view")
    void testPrepareSuccessViewWithNullTimes() {
        // Given
        EditNotificationTimeOutputData outputData = new EditNotificationTimeOutputData(
                null, null, null, false
        );

        // When
        presenter.prepareSuccessView(outputData);

        // Then
        verify(mockViewModel).setState(argThat(state -> {
            state.setReminder1(null);
            state.setReminder2(null);
            state.setReminder3(null);
            return state.getReminder1() == null &&
                   state.getReminder2() == null &&
                   state.getReminder3() == null;
        }));
    }

    @Test
    @DisplayName("Should handle empty reminder times in success view")
    void testPrepareSuccessViewWithEmptyTimes() {
        // Given
        EditNotificationTimeOutputData outputData = new EditNotificationTimeOutputData(
                "", "", "", false
        );

        // When
        presenter.prepareSuccessView(outputData);

        // Then
        verify(mockViewModel).setState(argThat(state -> {
            state.setReminder1("");
            state.setReminder2("");
            state.setReminder3("");
            return "".equals(state.getReminder1()) &&
                   "".equals(state.getReminder2()) &&
                   "".equals(state.getReminder3());
        }));
    }

    @Test
    @DisplayName("Should update view model state on failure")
    void testPrepareFailView() {
        // Given
        String errorMessage = "Invalid time format";

        // When
        presenter.prepareFailView(errorMessage);

        // Then
        verify(mockViewModel).setState(any(EditNotificationTimeState.class));
        verify(mockViewModel).firePropertyChanged(EditNotificationTimeViewModel.EDIT_NOTIFICATION_TIME_STATE_PROPERTY);
    }

    @Test
    @DisplayName("Should set error message in fail state")
    void testPrepareFailViewSetsErrorMessage() {
        // Given
        String expectedError = "Time validation failed";

        // When
        presenter.prepareFailView(expectedError);

        // Then
        verify(mockViewModel).setState(argThat(state -> {
            state.setErrorMessage(expectedError);
            return expectedError.equals(state.getErrorMessage());
        }));
    }

    @Test
    @DisplayName("Should set editing to false in fail state")
    void testPrepareFailViewSetsEditingFalse() {
        // Given
        String errorMessage = "Error occurred";

        // When
        presenter.prepareFailView(errorMessage);

        // Then
        verify(mockViewModel).setState(argThat(state -> {
            state.setEditing(false);
            return !state.isEditing();
        }));
    }

    @Test
    @DisplayName("Should handle null error message gracefully")
    void testPrepareFailViewWithNullError() {
        // When
        presenter.prepareFailView(null);

        // Then
        verify(mockViewModel).setState(argThat(state -> {
            state.setErrorMessage(null);
            return state.getErrorMessage() == null;
        }));
    }

    @Test
    @DisplayName("Should handle empty error message")
    void testPrepareFailViewWithEmptyError() {
        // Given
        String emptyError = "";

        // When
        presenter.prepareFailView(emptyError);

        // Then
        verify(mockViewModel).setState(argThat(state -> {
            state.setErrorMessage(emptyError);
            return "".equals(state.getErrorMessage());
        }));
    }

    @Test
    @DisplayName("Should fire property change with correct property name")
    void testFiresCorrectPropertyName() {
        // Given
        EditNotificationTimeOutputData outputData = new EditNotificationTimeOutputData(
                "08:00", "12:00", "20:00", false
        );

        // When
        presenter.prepareSuccessView(outputData);

        // Then
        verify(mockViewModel).firePropertyChanged(
                EditNotificationTimeViewModel.EDIT_NOTIFICATION_TIME_STATE_PROPERTY
        );
    }

    @Test
    @DisplayName("Should create new state object for each call")
    void testCreatesNewStateForEachCall() {
        // Given
        EditNotificationTimeOutputData outputData1 = new EditNotificationTimeOutputData(
                "08:00", "12:00", "20:00", false
        );
        EditNotificationTimeOutputData outputData2 = new EditNotificationTimeOutputData(
                "09:00", "13:00", "21:00", false
        );

        // When
        presenter.prepareSuccessView(outputData1);
        presenter.prepareSuccessView(outputData2);

        // Then
        verify(mockViewModel, times(2)).setState(any(EditNotificationTimeState.class));
        verify(mockViewModel, times(2)).firePropertyChanged(any(String.class));
    }

    @Test
    @DisplayName("Should handle mixed success and failure calls")
    void testMixedSuccessAndFailureCalls() {
        // Given
        EditNotificationTimeOutputData outputData = new EditNotificationTimeOutputData(
                "08:00", "12:00", "20:00", false
        );
        String errorMessage = "Validation error";

        // When
        presenter.prepareSuccessView(outputData);
        presenter.prepareFailView(errorMessage);

        // Then
        verify(mockViewModel, times(2)).setState(any(EditNotificationTimeState.class));
        verify(mockViewModel, times(2)).firePropertyChanged(
                EditNotificationTimeViewModel.EDIT_NOTIFICATION_TIME_STATE_PROPERTY
        );
    }

    @Test
    @DisplayName("Should handle edge case reminder times")
    void testPrepareSuccessViewWithEdgeCaseTimes() {
        // Given - Edge case times
        EditNotificationTimeOutputData outputData = new EditNotificationTimeOutputData(
                "00:00", "23:59", "12:00", false
        );

        // When
        presenter.prepareSuccessView(outputData);

        // Then
        verify(mockViewModel).setState(argThat(state -> {
            state.setReminder1("00:00");
            state.setReminder2("23:59");
            state.setReminder3("12:00");
            return "00:00".equals(state.getReminder1()) &&
                   "23:59".equals(state.getReminder2()) &&
                   "12:00".equals(state.getReminder3());
        }));
    }
}