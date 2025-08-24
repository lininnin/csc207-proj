package interface_adapter.alex.WellnessLog_related.moodLabel_related.edit_moodLabel;

import interface_adapter.alex.WellnessLog_related.moodLabel_related.AvailableMoodLabelState;
import interface_adapter.alex.WellnessLog_related.moodLabel_related.AvailableMoodLabelState.MoodLabelEntry;
import interface_adapter.alex.WellnessLog_related.moodLabel_related.AvailableMoodLabelViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import use_case.alex.wellness_log_related.moodlabel_related.edit_moodLabel.EditMoodLabelOutputData;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EditMoodLabelPresenterTest {

    private EditMoodLabelViewModel editMoodLabelViewModel;
    private AvailableMoodLabelViewModel availableMoodLabelViewModel;
    private EditMoodLabelPresenter presenter;

    @BeforeEach
    void setUp() {
        editMoodLabelViewModel = new EditMoodLabelViewModel();
        availableMoodLabelViewModel = new AvailableMoodLabelViewModel();
        presenter = new EditMoodLabelPresenter(editMoodLabelViewModel, availableMoodLabelViewModel);
    }

    @Test
    void testConstructor() {
        assertNotNull(presenter);
        // Constructor coverage is achieved by setUp() method
    }

    @Test
    void testPrepareSuccessViewUpdatesEditMoodLabelViewModel() {
        // Arrange
        EditMoodLabelOutputData outputData = new EditMoodLabelOutputData("Joyful", "Positive", false);
        
        // Act
        presenter.prepareSuccessView(outputData);
        
        // Assert
        EditMoodLabelState resultState = editMoodLabelViewModel.getState();
        assertEquals("Joyful", resultState.getName());
        assertEquals("Positive", resultState.getType());
        assertNull(resultState.getEditError()); // Should be null for success
    }

    @Test
    void testPrepareSuccessViewUpdatesAvailableMoodLabelList() {
        // Arrange
        List<MoodLabelEntry> initialLabels = Arrays.asList(
            new MoodLabelEntry("Happy", "Positive"),
            new MoodLabelEntry("Sad", "Negative"),
            new MoodLabelEntry("Joyful", "Positive")
        );
        
        AvailableMoodLabelState initialState = new AvailableMoodLabelState();
        initialState.setMoodLabels(initialLabels);
        availableMoodLabelViewModel.setState(initialState);

        EditMoodLabelOutputData outputData = new EditMoodLabelOutputData("Joyful", "Negative", false);
        
        PropertyChangeListener mockListener = mock(PropertyChangeListener.class);
        availableMoodLabelViewModel.addPropertyChangeListener(mockListener);
        
        // Act
        presenter.prepareSuccessView(outputData);
        
        // Assert
        AvailableMoodLabelState resultState = availableMoodLabelViewModel.getState();
        List<MoodLabelEntry> updatedLabels = resultState.getMoodLabels();
        
        // Verify the Joyful entry was updated to Negative
        MoodLabelEntry updatedEntry = updatedLabels.stream()
            .filter(entry -> "Joyful".equals(entry.getName()))
            .findFirst()
            .orElse(null);
        
        assertNotNull(updatedEntry);
        assertEquals("Negative", updatedEntry.getType());
        
        // Verify property change was fired
        verify(mockListener, atLeastOnce()).propertyChange(any());
    }

    @Test
    void testPrepareSuccessViewWithNonExistentLabel() {
        // Arrange
        List<MoodLabelEntry> initialLabels = Arrays.asList(
            new MoodLabelEntry("Happy", "Positive"),
            new MoodLabelEntry("Sad", "Negative")
        );
        
        AvailableMoodLabelState initialState = new AvailableMoodLabelState();
        initialState.setMoodLabels(initialLabels);
        availableMoodLabelViewModel.setState(initialState);

        EditMoodLabelOutputData outputData = new EditMoodLabelOutputData("NonExistent", "Positive", false);
        
        // Act
        presenter.prepareSuccessView(outputData);
        
        // Assert
        AvailableMoodLabelState resultState = availableMoodLabelViewModel.getState();
        List<MoodLabelEntry> updatedLabels = resultState.getMoodLabels();
        
        // Verify original labels remain unchanged
        assertEquals(2, updatedLabels.size());
        assertEquals("Happy", updatedLabels.get(0).getName());
        assertEquals("Positive", updatedLabels.get(0).getType());
        assertEquals("Sad", updatedLabels.get(1).getName());
        assertEquals("Negative", updatedLabels.get(1).getType());
    }

    @Test
    void testPrepareSuccessViewWithEmptyLabelList() {
        // Arrange
        AvailableMoodLabelState initialState = new AvailableMoodLabelState();
        initialState.setMoodLabels(new ArrayList<>());
        availableMoodLabelViewModel.setState(initialState);

        EditMoodLabelOutputData outputData = new EditMoodLabelOutputData("AnyLabel", "Positive", false);
        
        // Act
        presenter.prepareSuccessView(outputData);
        
        // Assert
        AvailableMoodLabelState resultState = availableMoodLabelViewModel.getState();
        List<MoodLabelEntry> updatedLabels = resultState.getMoodLabels();
        assertTrue(updatedLabels.isEmpty());
    }

    @Test
    void testPrepareFailView() {
        // Arrange
        EditMoodLabelOutputData outputData = new EditMoodLabelOutputData("InvalidLabel", "InvalidType", true);
        
        // Act
        presenter.prepareFailView(outputData);
        
        // Assert
        EditMoodLabelState resultState = editMoodLabelViewModel.getState();
        assertEquals("InvalidLabel", resultState.getName());
        assertEquals("InvalidType", resultState.getType());
        assertEquals("Edit failed: input may be invalid or label not found.", resultState.getEditError());
    }

    @Test
    void testPrepareFailViewWithNullValues() {
        // Arrange
        EditMoodLabelOutputData outputData = new EditMoodLabelOutputData(null, null, true);
        
        // Act
        presenter.prepareFailView(outputData);
        
        // Assert
        EditMoodLabelState resultState = editMoodLabelViewModel.getState();
        assertNull(resultState.getName());
        assertNull(resultState.getType());
        assertEquals("Edit failed: input may be invalid or label not found.", resultState.getEditError());
    }

    @Test
    void testPrepareSuccessViewFiresPropertyChange() {
        // Arrange
        PropertyChangeListener mockEditListener = mock(PropertyChangeListener.class);
        PropertyChangeListener mockAvailableListener = mock(PropertyChangeListener.class);
        
        editMoodLabelViewModel.addPropertyChangeListener(mockEditListener);
        availableMoodLabelViewModel.addPropertyChangeListener(mockAvailableListener);
        
        List<MoodLabelEntry> initialLabels = Arrays.asList(
            new MoodLabelEntry("TestLabel", "Positive")
        );
        
        AvailableMoodLabelState initialState = new AvailableMoodLabelState();
        initialState.setMoodLabels(initialLabels);
        availableMoodLabelViewModel.setState(initialState);

        EditMoodLabelOutputData outputData = new EditMoodLabelOutputData("TestLabel", "Negative", false);
        
        // Act
        presenter.prepareSuccessView(outputData);
        
        // Assert
        verify(mockEditListener, atLeastOnce()).propertyChange(any());
        verify(mockAvailableListener, atLeastOnce()).propertyChange(any());
    }

    @Test
    void testPrepareFailViewFiresPropertyChange() {
        // Arrange
        PropertyChangeListener mockEditListener = mock(PropertyChangeListener.class);
        editMoodLabelViewModel.addPropertyChangeListener(mockEditListener);
        
        EditMoodLabelOutputData outputData = new EditMoodLabelOutputData("FailLabel", "FailType", true);
        
        // Act
        presenter.prepareFailView(outputData);
        
        // Assert
        verify(mockEditListener, atLeastOnce()).propertyChange(any());
    }
}