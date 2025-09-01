package interface_adapter.alex.WellnessLog_related.moodLabel_related.delete_moodLabel;

import interface_adapter.alex.WellnessLog_related.moodLabel_related.AvailableMoodLabelState;
import interface_adapter.alex.WellnessLog_related.moodLabel_related.AvailableMoodLabelState.MoodLabelEntry;
import interface_adapter.alex.WellnessLog_related.moodLabel_related.AvailableMoodLabelViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import use_case.alex.wellness_log_related.moodlabel_related.delete_moodLabel.DeleteMoodLabelOutputData;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

/**
 * Comprehensive test suite for DeleteMoodLabelPresenter.
 * Tests presenter responsibilities in Clean Architecture.
 */
class DeleteMoodLabelPresenterTest {

    private DeleteMoodLabelPresenter presenter;
    private DeleteMoodLabelViewModel mockDeleteViewModel;
    private AvailableMoodLabelViewModel mockAvailableViewModel;
    private AvailableMoodLabelState mockAvailableState;

    @BeforeEach
    void setUp() {
        mockDeleteViewModel = mock(DeleteMoodLabelViewModel.class);
        mockAvailableViewModel = mock(AvailableMoodLabelViewModel.class);
        mockAvailableState = mock(AvailableMoodLabelState.class);

        presenter = new DeleteMoodLabelPresenter(mockDeleteViewModel, mockAvailableViewModel);

        // Setup mock state
        when(mockAvailableViewModel.getState()).thenReturn(mockAvailableState);
    }

    @Test
    @DisplayName("Should update view model states on successful deletion")
    void testPrepareSuccessView() {
        // Given
        String moodLabelName = "Happy";
        DeleteMoodLabelOutputData outputData = new DeleteMoodLabelOutputData(moodLabelName);

        List<MoodLabelEntry> mockMoodLabels = new ArrayList<>();
        mockMoodLabels.add(new MoodLabelEntry("Happy", "Positive"));
        mockMoodLabels.add(new MoodLabelEntry("Sad", "Negative"));
        when(mockAvailableState.getMoodLabels()).thenReturn(mockMoodLabels);

        // When
        presenter.prepareSuccessView(outputData);

        // Then
        verify(mockDeleteViewModel).setState(any(DeleteMoodLabelState.class));
        verify(mockDeleteViewModel).firePropertyChanged(DeleteMoodLabelViewModel.DELETE_MOOD_LABEL_STATE_PROPERTY);
        verify(mockAvailableViewModel).setState(mockAvailableState);
        verify(mockAvailableViewModel).firePropertyChanged(AvailableMoodLabelViewModel.MOOD_LABEL_LIST_PROPERTY);
    }

    @Test
    @DisplayName("Should set success state with correct mood label name")
    void testPrepareSuccessViewSetsCorrectMoodLabelName() {
        // Given
        String expectedMoodLabelName = "Excited";
        DeleteMoodLabelOutputData outputData = new DeleteMoodLabelOutputData(expectedMoodLabelName);

        List<MoodLabelEntry> mockMoodLabels = new ArrayList<>();
        when(mockAvailableState.getMoodLabels()).thenReturn(mockMoodLabels);

        // When
        presenter.prepareSuccessView(outputData);

        // Then
        verify(mockDeleteViewModel).setState(argThat(state -> {
            state.setDeletedMoodLabelName(expectedMoodLabelName);
            state.setDeletedSuccessfully(true);
            state.setDeleteError(null);
            return expectedMoodLabelName.equals(state.getDeletedMoodLabelName()) &&
                   state.isDeletedSuccessfully() &&
                   state.getDeleteError() == null;
        }));
    }

    @Test
    @DisplayName("Should remove deleted mood label from available list")
    void testPrepareSuccessViewRemovesMoodLabelFromList() {
        // Given
        String moodLabelToDelete = "Anxious";
        DeleteMoodLabelOutputData outputData = new DeleteMoodLabelOutputData(moodLabelToDelete);

        List<MoodLabelEntry> mockMoodLabels = new ArrayList<>();
        mockMoodLabels.add(new MoodLabelEntry("Happy", "Positive"));
        mockMoodLabels.add(new MoodLabelEntry("Anxious", "Negative"));
        mockMoodLabels.add(new MoodLabelEntry("Calm", "Positive"));
        when(mockAvailableState.getMoodLabels()).thenReturn(mockMoodLabels);

        // When
        presenter.prepareSuccessView(outputData);

        // Then
        // Verify setMoodLabels was called with a list not containing "Anxious"
        verify(mockAvailableState).setMoodLabels(argThat(updatedList -> {
            return updatedList.size() == 2 &&
                   updatedList.stream().noneMatch(entry -> entry.getName().equals("Anxious")) &&
                   updatedList.stream().anyMatch(entry -> entry.getName().equals("Happy")) &&
                   updatedList.stream().anyMatch(entry -> entry.getName().equals("Calm"));
        }));
    }

    @Test
    @DisplayName("Should handle deletion when mood label not found in list")
    void testPrepareSuccessViewWithNonExistentMoodLabel() {
        // Given
        String nonExistentMoodLabel = "NonExistent";
        DeleteMoodLabelOutputData outputData = new DeleteMoodLabelOutputData(nonExistentMoodLabel);

        List<MoodLabelEntry> mockMoodLabels = new ArrayList<>();
        mockMoodLabels.add(new MoodLabelEntry("Happy", "Positive"));
        mockMoodLabels.add(new MoodLabelEntry("Sad", "Negative"));
        when(mockAvailableState.getMoodLabels()).thenReturn(mockMoodLabels);

        // When
        presenter.prepareSuccessView(outputData);

        // Then - List should remain unchanged
        verify(mockAvailableState).setMoodLabels(argThat(updatedList ->
            updatedList.size() == 2 &&
            updatedList.stream().anyMatch(entry -> entry.getName().equals("Happy")) &&
            updatedList.stream().anyMatch(entry -> entry.getName().equals("Sad"))
        ));
    }

    @Test
    @DisplayName("Should update view model state on deletion failure")
    void testPrepareFailView() {
        // Given
        String moodLabelName = "Protected";
        String errorMessage = "Cannot delete protected mood label";
        DeleteMoodLabelOutputData outputData = new DeleteMoodLabelOutputData(moodLabelName, errorMessage);

        // When
        presenter.prepareFailView(outputData);

        // Then
        verify(mockDeleteViewModel).setState(any(DeleteMoodLabelState.class));
        verify(mockDeleteViewModel).firePropertyChanged(DeleteMoodLabelViewModel.DELETE_MOOD_LABEL_STATE_PROPERTY);

        // Should not update available view model on failure
        verify(mockAvailableViewModel, never()).setState(any());
        verify(mockAvailableViewModel, never()).firePropertyChanged(any());
    }

    @Test
    @DisplayName("Should set error state with correct details on failure")
    void testPrepareFailViewSetsErrorDetails() {
        // Given
        String expectedMoodLabelName = "SystemLabel";
        String expectedErrorMessage = "System labels cannot be deleted";
        DeleteMoodLabelOutputData outputData = new DeleteMoodLabelOutputData(expectedMoodLabelName, expectedErrorMessage);

        // When
        presenter.prepareFailView(outputData);

        // Then
        verify(mockDeleteViewModel).setState(argThat(state -> {
            state.setDeletedMoodLabelName(expectedMoodLabelName);
            state.setDeletedSuccessfully(false);
            state.setDeleteError(expectedErrorMessage);
            return expectedMoodLabelName.equals(state.getDeletedMoodLabelName()) &&
                   !state.isDeletedSuccessfully() &&
                   expectedErrorMessage.equals(state.getDeleteError());
        }));
    }

    @Test
    @DisplayName("Should handle null mood label name in success case")
    void testPrepareSuccessViewWithNullMoodLabelName() {
        // Given
        DeleteMoodLabelOutputData outputData = new DeleteMoodLabelOutputData(null);

        List<MoodLabelEntry> mockMoodLabels = new ArrayList<>();
        when(mockAvailableState.getMoodLabels()).thenReturn(mockMoodLabels);

        // When
        presenter.prepareSuccessView(outputData);

        // Then
        verify(mockDeleteViewModel).setState(argThat(state -> {
            state.setDeletedMoodLabelName(null);
            return state.getDeletedMoodLabelName() == null;
        }));
    }

    @Test
    @DisplayName("Should handle null error message in fail case")
    void testPrepareFailViewWithNullErrorMessage() {
        // Given
        String moodLabelName = "TestLabel";
        DeleteMoodLabelOutputData outputData = new DeleteMoodLabelOutputData(moodLabelName, null);

        // When
        presenter.prepareFailView(outputData);

        // Then
        verify(mockDeleteViewModel).setState(argThat(state -> {
            state.setDeleteError(null);
            return state.getDeleteError() == null;
        }));
    }

    @Test
    @DisplayName("Should handle empty mood labels list")
    void testPrepareSuccessViewWithEmptyList() {
        // Given
        String moodLabelName = "AnyLabel";
        DeleteMoodLabelOutputData outputData = new DeleteMoodLabelOutputData(moodLabelName);

        List<MoodLabelEntry> emptyMoodLabels = new ArrayList<>();
        when(mockAvailableState.getMoodLabels()).thenReturn(emptyMoodLabels);

        // When
        presenter.prepareSuccessView(outputData);

        // Then
        verify(mockAvailableState).setMoodLabels(argThat(updatedList ->
            updatedList.isEmpty()
        ));
        verify(mockDeleteViewModel).setState(any(DeleteMoodLabelState.class));
        verify(mockAvailableViewModel).setState(mockAvailableState);
    }

    @Test
    @DisplayName("Should handle case-sensitive mood label deletion")
    void testPrepareSuccessViewCaseSensitive() {
        // Given
        String moodLabelToDelete = "Happy";
        DeleteMoodLabelOutputData outputData = new DeleteMoodLabelOutputData(moodLabelToDelete);

        List<MoodLabelEntry> mockMoodLabels = new ArrayList<>();
        mockMoodLabels.add(new MoodLabelEntry("happy", "Positive")); // lowercase
        mockMoodLabels.add(new MoodLabelEntry("Happy", "Positive")); // exact match
        mockMoodLabels.add(new MoodLabelEntry("HAPPY", "Positive")); // uppercase
        when(mockAvailableState.getMoodLabels()).thenReturn(mockMoodLabels);

        // When
        presenter.prepareSuccessView(outputData);

        // Then - Only exact case match should be removed
        verify(mockAvailableState).setMoodLabels(argThat(updatedList -> {
            return updatedList.size() == 2 &&
                   updatedList.stream().anyMatch(entry -> entry.getName().equals("happy")) &&
                   updatedList.stream().anyMatch(entry -> entry.getName().equals("HAPPY")) &&
                   updatedList.stream().noneMatch(entry -> entry.getName().equals("Happy"));
        }));
    }

    @Test
    @DisplayName("Should fire correct property change events")
    void testFiresCorrectPropertyChangeEvents() {
        // Given
        String moodLabelName = "TestLabel";
        DeleteMoodLabelOutputData successData = new DeleteMoodLabelOutputData(moodLabelName);
        DeleteMoodLabelOutputData failData = new DeleteMoodLabelOutputData(moodLabelName, "Error");

        List<MoodLabelEntry> mockMoodLabels = new ArrayList<>();
        when(mockAvailableState.getMoodLabels()).thenReturn(mockMoodLabels);

        // When
        presenter.prepareSuccessView(successData);
        presenter.prepareFailView(failData);

        // Then
        verify(mockDeleteViewModel, times(2)).firePropertyChanged(
            DeleteMoodLabelViewModel.DELETE_MOOD_LABEL_STATE_PROPERTY
        );
        verify(mockAvailableViewModel).firePropertyChanged(
            AvailableMoodLabelViewModel.MOOD_LABEL_LIST_PROPERTY
        );
    }
}