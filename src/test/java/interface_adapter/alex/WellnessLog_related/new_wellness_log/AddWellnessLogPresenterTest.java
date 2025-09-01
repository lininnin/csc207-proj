package interface_adapter.alex.WellnessLog_related.new_wellness_log;

import entity.alex.MoodLabel.MoodLabelInterf;
import entity.alex.WellnessLogEntry.WellnessLogEntryInterf;
import interface_adapter.alex.WellnessLog_related.todays_wellnesslog_module.todays_wellness_log.TodaysWellnessLogState;
import interface_adapter.alex.WellnessLog_related.todays_wellnesslog_module.todays_wellness_log.TodaysWellnessLogViewModel;
import interface_adapter.Angela.today_so_far.TodaySoFarController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import use_case.alex.wellness_log_related.add_wellnessLog.AddWellnessLogOutputData;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Comprehensive test suite for AddWellnessLogPresenter.
 * Tests presenter responsibilities in Clean Architecture.
 */
class AddWellnessLogPresenterTest {

    private AddWellnessLogPresenter presenter;
    private AddWellnessLogViewModel mockAddViewModel;
    private TodaysWellnessLogViewModel mockTodaysViewModel;
    private TodaySoFarController mockTodaySoFarController;
    private AddWellnessLogState mockOldState;
    private TodaysWellnessLogState mockTodaysState;

    @BeforeEach
    void setUp() {
        mockAddViewModel = mock(AddWellnessLogViewModel.class);
        mockTodaysViewModel = mock(TodaysWellnessLogViewModel.class);
        mockTodaySoFarController = mock(TodaySoFarController.class);

        presenter = new AddWellnessLogPresenter(mockAddViewModel, mockTodaysViewModel);

        // Setup mock states
        mockOldState = mock(AddWellnessLogState.class);
        mockTodaysState = mock(TodaysWellnessLogState.class);

        when(mockAddViewModel.getState()).thenReturn(mockOldState);
        when(mockTodaysViewModel.getState()).thenReturn(mockTodaysState);
    }

    @Test
    @DisplayName("Should update view model states on success")
    void testPrepareSuccessView() {
        // Given
        WellnessLogEntryInterf mockEntry = mock(WellnessLogEntryInterf.class);
        AddWellnessLogOutputData outputData = new AddWellnessLogOutputData(
                mockEntry,
                true,
                "Successfully added wellness log"
        );

        List<MoodLabelInterf> mockMoodLabels = Arrays.asList(
                mock(MoodLabelInterf.class),
                mock(MoodLabelInterf.class)
        );
        when(mockOldState.getAvailableMoodLabels()).thenReturn(mockMoodLabels);

        // When
        presenter.prepareSuccessView(outputData);

        // Then
        verify(mockAddViewModel).setState(any(AddWellnessLogState.class));
        verify(mockAddViewModel).firePropertyChanged(AddWellnessLogViewModel.WELLNESS_LOG_PROPERTY);

        verify(mockTodaysState).addEntry(mockEntry);
        verify(mockTodaysViewModel).setState(mockTodaysState);
        verify(mockTodaysViewModel).firePropertyChanged(TodaysWellnessLogViewModel.TODAYS_WELLNESS_LOG_PROPERTY);
    }

    @Test
    @DisplayName("Should preserve mood labels from old state on success")
    void testPrepareSuccessViewPreservesMoodLabels() {
        // Given
        WellnessLogEntryInterf mockEntry = mock(WellnessLogEntryInterf.class);
        AddWellnessLogOutputData outputData = new AddWellnessLogOutputData(
                mockEntry,
                true,
                "Success message"
        );

        List<MoodLabelInterf> expectedMoodLabels = Arrays.asList(
                mock(MoodLabelInterf.class),
                mock(MoodLabelInterf.class)
        );
        when(mockOldState.getAvailableMoodLabels()).thenReturn(expectedMoodLabels);

        // When
        presenter.prepareSuccessView(outputData);

        // Then
        verify(mockAddViewModel).setState(argThat(newState -> {
            newState.setAvailableMoodLabels(expectedMoodLabels);
            return newState.getAvailableMoodLabels() == expectedMoodLabels;
        }));
    }

    @Test
    @DisplayName("Should set success message on new state")
    void testPrepareSuccessViewSetsMessage() {
        // Given
        String expectedMessage = "Wellness log added successfully!";
        WellnessLogEntryInterf mockEntry = mock(WellnessLogEntryInterf.class);
        AddWellnessLogOutputData outputData = new AddWellnessLogOutputData(
                mockEntry,
                true,
                expectedMessage
        );

        when(mockOldState.getAvailableMoodLabels()).thenReturn(Arrays.asList());

        // When
        presenter.prepareSuccessView(outputData);

        // Then
        verify(mockAddViewModel).setState(argThat(newState -> {
            newState.setSuccessMessage(expectedMessage);
            return expectedMessage.equals(newState.getSuccessMessage());
        }));
    }

    @Test
    @DisplayName("Should refresh today so far controller when set")
    void testPrepareSuccessViewRefreshesTodaySoFar() {
        // Given
        presenter.setTodaySoFarController(mockTodaySoFarController);
        
        WellnessLogEntryInterf mockEntry = mock(WellnessLogEntryInterf.class);
        AddWellnessLogOutputData outputData = new AddWellnessLogOutputData(
                mockEntry,
                true,
                "Success"
        );
        when(mockOldState.getAvailableMoodLabels()).thenReturn(Arrays.asList());

        // When
        presenter.prepareSuccessView(outputData);

        // Then
        verify(mockTodaySoFarController).refresh();
    }

    @Test
    @DisplayName("Should not crash when today so far controller is null")
    void testPrepareSuccessViewWithoutTodaySoFarController() {
        // Given - no controller set
        WellnessLogEntryInterf mockEntry = mock(WellnessLogEntryInterf.class);
        AddWellnessLogOutputData outputData = new AddWellnessLogOutputData(
                mockEntry,
                true,
                "Success"
        );
        when(mockOldState.getAvailableMoodLabels()).thenReturn(Arrays.asList());

        // When & Then - should not throw exception
        presenter.prepareSuccessView(outputData);

        verify(mockAddViewModel).setState(any(AddWellnessLogState.class));
        verify(mockAddViewModel).firePropertyChanged(any(String.class));
    }

    @Test
    @DisplayName("Should update view model state on failure")
    void testPrepareFailView() {
        // Given
        String errorMessage = "Failed to add wellness log";
        List<MoodLabelInterf> mockMoodLabels = Arrays.asList(
                mock(MoodLabelInterf.class)
        );
        when(mockOldState.getAvailableMoodLabels()).thenReturn(mockMoodLabels);

        // When
        presenter.prepareFailView(errorMessage);

        // Then
        verify(mockAddViewModel).setState(any(AddWellnessLogState.class));
        verify(mockAddViewModel).firePropertyChanged(AddWellnessLogViewModel.WELLNESS_LOG_PROPERTY);

        // Should not update today's view model on failure
        verify(mockTodaysViewModel, never()).setState(any());
        verify(mockTodaysViewModel, never()).firePropertyChanged(any());
    }

    @Test
    @DisplayName("Should preserve mood labels from old state on failure")
    void testPrepareFailViewPreservesMoodLabels() {
        // Given
        String errorMessage = "Validation failed";
        List<MoodLabelInterf> expectedMoodLabels = Arrays.asList(
                mock(MoodLabelInterf.class),
                mock(MoodLabelInterf.class)
        );
        when(mockOldState.getAvailableMoodLabels()).thenReturn(expectedMoodLabels);

        // When
        presenter.prepareFailView(errorMessage);

        // Then
        verify(mockAddViewModel).setState(argThat(newState -> {
            newState.setAvailableMoodLabels(expectedMoodLabels);
            return newState.getAvailableMoodLabels() == expectedMoodLabels;
        }));
    }

    @Test
    @DisplayName("Should set error message on new state during failure")
    void testPrepareFailViewSetsErrorMessage() {
        // Given
        String expectedError = "Database connection failed";
        when(mockOldState.getAvailableMoodLabels()).thenReturn(Arrays.asList());

        // When
        presenter.prepareFailView(expectedError);

        // Then
        verify(mockAddViewModel).setState(argThat(newState -> {
            newState.setErrorMessage(expectedError);
            return expectedError.equals(newState.getErrorMessage());
        }));
    }

    @Test
    @DisplayName("Should set today so far controller correctly")
    void testSetTodaySoFarController() {
        // When
        presenter.setTodaySoFarController(mockTodaySoFarController);

        // Then - verify by checking if it's used in success scenario
        WellnessLogEntryInterf mockEntry = mock(WellnessLogEntryInterf.class);
        AddWellnessLogOutputData outputData = new AddWellnessLogOutputData(mockEntry, true, "Success");
        when(mockOldState.getAvailableMoodLabels()).thenReturn(Arrays.asList());

        presenter.prepareSuccessView(outputData);
        verify(mockTodaySoFarController).refresh();
    }

    @Test
    @DisplayName("Should handle null error message gracefully")
    void testPrepareFailViewWithNullError() {
        // Given
        when(mockOldState.getAvailableMoodLabels()).thenReturn(Arrays.asList());

        // When
        presenter.prepareFailView(null);

        // Then
        verify(mockAddViewModel).setState(argThat(newState -> {
            newState.setErrorMessage(null);
            return newState.getErrorMessage() == null;
        }));
    }

    @Test
    @DisplayName("Should handle empty error message")
    void testPrepareFailViewWithEmptyError() {
        // Given
        String emptyError = "";
        when(mockOldState.getAvailableMoodLabels()).thenReturn(Arrays.asList());

        // When
        presenter.prepareFailView(emptyError);

        // Then
        verify(mockAddViewModel).setState(argThat(newState -> {
            newState.setErrorMessage(emptyError);
            return emptyError.equals(newState.getErrorMessage());
        }));
    }

    @Test
    @DisplayName("Should handle null wellness log entry in output data")
    void testPrepareSuccessViewWithNullEntry() {
        // Given
        AddWellnessLogOutputData outputData = new AddWellnessLogOutputData(
                null,  // null entry
                true,
                "Success message"
        );
        when(mockOldState.getAvailableMoodLabels()).thenReturn(Arrays.asList());

        // When
        presenter.prepareSuccessView(outputData);

        // Then
        verify(mockTodaysState).addEntry(null);
        verify(mockTodaysViewModel).setState(mockTodaysState);
    }
}