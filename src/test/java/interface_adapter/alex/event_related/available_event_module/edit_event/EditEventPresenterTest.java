package interface_adapter.alex.event_related.available_event_module.edit_event;

import entity.info.InfoInterf;
import interface_adapter.alex.event_related.available_event_module.available_event.AvailableEventState;
import interface_adapter.alex.event_related.available_event_module.available_event.AvailableEventViewModel;
import interface_adapter.alex.event_related.todays_events_module.todays_events.TodaysEventsViewModel;
import interface_adapter.Angela.today_so_far.TodaySoFarController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import use_case.alex.event_related.avaliable_events_module.edit_event.EditEventOutputData;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EditEventPresenterTest {

    @Mock
    private EditedEventViewModel mockEditedEventViewModel;

    @Mock
    private AvailableEventViewModel mockAvailableEventViewModel;

    @Mock
    private TodaysEventsViewModel mockTodaysEventsViewModel;

    @Mock
    private TodaySoFarController mockTodaySoFarController;

    @Mock
    private InfoInterf mockInfo1;

    @Mock
    private InfoInterf mockInfo2;

    @Mock
    private InfoInterf mockInfo3;

    private EditEventPresenter presenter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        presenter = new EditEventPresenter(
                mockEditedEventViewModel,
                mockAvailableEventViewModel
        );
    }

    @Test
    void testConstructor() {
        assertNotNull(presenter);
    }

    @Test
    void testSetTodaysEventsViewModel() {
        presenter.setTodaysEventsViewModel(mockTodaysEventsViewModel);
        // Verify no exception is thrown
        assertDoesNotThrow(() -> presenter.setTodaysEventsViewModel(mockTodaysEventsViewModel));
    }

    @Test
    void testSetTodaysEventsViewModelWithNull() {
        presenter.setTodaysEventsViewModel(null);
        // Should not throw exception when set to null
        assertDoesNotThrow(() -> presenter.setTodaysEventsViewModel(null));
    }

    @Test
    void testSetTodaySoFarController() {
        presenter.setTodaySoFarController(mockTodaySoFarController);
        // Verify no exception is thrown
        assertDoesNotThrow(() -> presenter.setTodaySoFarController(mockTodaySoFarController));
    }

    @Test
    void testSetTodaySoFarControllerWithNull() {
        presenter.setTodaySoFarController(null);
        // Should not throw exception when set to null
        assertDoesNotThrow(() -> presenter.setTodaySoFarController(null));
    }

    @Test
    void testPrepareSuccessView() {
        // Arrange
        String eventId = "success-event-123";
        String name = "Updated Event Name";
        String category = "Work";
        String description = "Updated description";
        EditEventOutputData outputData = new EditEventOutputData(eventId, name, category, description, false);

        // Mock the events list in AvailableEventState
        when(mockInfo1.getId()).thenReturn("different-id-1");
        when(mockInfo2.getId()).thenReturn(eventId); // This event should be updated
        when(mockInfo3.getId()).thenReturn("different-id-3");

        List<InfoInterf> initialEvents = new ArrayList<>();
        initialEvents.add(mockInfo1);
        initialEvents.add(mockInfo2);
        initialEvents.add(mockInfo3);

        AvailableEventState mockCurrentState = mock(AvailableEventState.class);
        when(mockCurrentState.getAvailableEvents()).thenReturn(initialEvents);
        when(mockAvailableEventViewModel.getState()).thenReturn(mockCurrentState);

        presenter.setTodaysEventsViewModel(mockTodaysEventsViewModel);
        presenter.setTodaySoFarController(mockTodaySoFarController);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert - Verify EditedEventViewModel updates
        verify(mockEditedEventViewModel, times(1)).updateState(any(EditedEventState.class));

        // Assert - Verify the correct event's info was updated
        verify(mockInfo2, times(1)).setName(name);
        verify(mockInfo2, times(1)).setCategory(category);
        verify(mockInfo2, times(1)).setDescription(description);
        verify(mockInfo1, never()).setName(anyString());
        verify(mockInfo1, never()).setCategory(anyString());
        verify(mockInfo1, never()).setDescription(anyString());
        verify(mockInfo3, never()).setName(anyString());
        verify(mockInfo3, never()).setCategory(anyString());
        verify(mockInfo3, never()).setDescription(anyString());

        // Assert - Verify AvailableEventViewModel updates
        verify(mockAvailableEventViewModel, times(1)).getState();
        verify(mockAvailableEventViewModel, times(1)).setState(any(AvailableEventState.class));
        verify(mockAvailableEventViewModel, times(1)).firePropertyChanged(AvailableEventViewModel.AVAILABLE_EVENTS_PROPERTY);

        // Assert - Verify TodaysEventsViewModel refresh
        verify(mockTodaysEventsViewModel, times(1)).firePropertyChanged("state");

        // Assert - Verify TodaySoFarController refresh
        verify(mockTodaySoFarController, times(1)).refresh();
    }

    @Test
    void testPrepareSuccessViewWithoutOptionalDependencies() {
        // Arrange
        String eventId = "success-event-456";
        String name = "Event Name";
        String category = "Category";
        String description = "Description";
        EditEventOutputData outputData = new EditEventOutputData(eventId, name, category, description, false);

        // Mock empty events list
        List<InfoInterf> initialEvents = new ArrayList<>();
        AvailableEventState mockCurrentState = mock(AvailableEventState.class);
        when(mockCurrentState.getAvailableEvents()).thenReturn(initialEvents);
        when(mockAvailableEventViewModel.getState()).thenReturn(mockCurrentState);

        // Don't set optional dependencies (leave them null)

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert - Verify required ViewModels are updated
        verify(mockEditedEventViewModel, times(1)).updateState(any(EditedEventState.class));
        verify(mockAvailableEventViewModel, times(1)).setState(any(AvailableEventState.class));
        verify(mockAvailableEventViewModel, times(1)).firePropertyChanged(AvailableEventViewModel.AVAILABLE_EVENTS_PROPERTY);

        // Assert - Verify optional dependencies are not called (since they're null)
        verify(mockTodaysEventsViewModel, never()).firePropertyChanged(anyString());
        verify(mockTodaySoFarController, never()).refresh();
    }

    @Test
    void testPrepareSuccessViewUpdatesCorrectEvent() {
        // Arrange
        String targetEventId = "target-event-789";
        String newName = "New Event Name";
        String newCategory = "New Category";
        String newDescription = "New Description";
        EditEventOutputData outputData = new EditEventOutputData(targetEventId, newName, newCategory, newDescription, false);

        // Create mock events with different IDs
        when(mockInfo1.getId()).thenReturn("keep-event-1");
        when(mockInfo2.getId()).thenReturn(targetEventId); // This should be updated
        when(mockInfo3.getId()).thenReturn("keep-event-3");

        List<InfoInterf> initialEvents = new ArrayList<>();
        initialEvents.add(mockInfo1);
        initialEvents.add(mockInfo2);
        initialEvents.add(mockInfo3);

        AvailableEventState mockCurrentState = mock(AvailableEventState.class);
        when(mockCurrentState.getAvailableEvents()).thenReturn(initialEvents);
        when(mockAvailableEventViewModel.getState()).thenReturn(mockCurrentState);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert - Verify only the target event was updated
        verify(mockInfo2, times(1)).setName(newName);
        verify(mockInfo2, times(1)).setCategory(newCategory);
        verify(mockInfo2, times(1)).setDescription(newDescription);
        verify(mockInfo1, never()).setName(anyString());
        verify(mockInfo1, never()).setCategory(anyString());
        verify(mockInfo1, never()).setDescription(anyString());
        verify(mockInfo3, never()).setName(anyString());
        verify(mockInfo3, never()).setCategory(anyString());
        verify(mockInfo3, never()).setDescription(anyString());
    }

    @Test
    void testPrepareSuccessViewWithNonExistentEventId() {
        // Arrange
        String nonExistentEventId = "non-existent-event-id";
        String name = "Some Name";
        String category = "Some Category";
        String description = "Some Description";
        EditEventOutputData outputData = new EditEventOutputData(nonExistentEventId, name, category, description, false);

        // Mock events with different IDs
        when(mockInfo1.getId()).thenReturn("existing-id-1");
        when(mockInfo2.getId()).thenReturn("existing-id-2");

        List<InfoInterf> initialEvents = new ArrayList<>();
        initialEvents.add(mockInfo1);
        initialEvents.add(mockInfo2);

        AvailableEventState mockCurrentState = mock(AvailableEventState.class);
        when(mockCurrentState.getAvailableEvents()).thenReturn(initialEvents);
        when(mockAvailableEventViewModel.getState()).thenReturn(mockCurrentState);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert - No events should be updated since the ID doesn't match any event
        verify(mockInfo1, never()).setName(anyString());
        verify(mockInfo1, never()).setCategory(anyString());
        verify(mockInfo1, never()).setDescription(anyString());
        verify(mockInfo2, never()).setName(anyString());
        verify(mockInfo2, never()).setCategory(anyString());
        verify(mockInfo2, never()).setDescription(anyString());

        // Assert - ViewModels should still be updated
        verify(mockEditedEventViewModel, times(1)).updateState(any(EditedEventState.class));
        verify(mockAvailableEventViewModel, times(1)).setState(any(AvailableEventState.class));
    }

    @Test
    void testPrepareSuccessViewBreaksAfterFirstMatch() {
        // Arrange
        String targetEventId = "duplicate-event-id";
        String name = "Updated Name";
        String category = "Updated Category";
        String description = "Updated Description";
        EditEventOutputData outputData = new EditEventOutputData(targetEventId, name, category, description, false);

        // Create multiple events with the same ID to test that only the first one is updated (break statement)
        when(mockInfo1.getId()).thenReturn(targetEventId);
        when(mockInfo2.getId()).thenReturn(targetEventId); // Same ID - should not be updated due to break
        when(mockInfo3.getId()).thenReturn("different-id");

        List<InfoInterf> initialEvents = new ArrayList<>();
        initialEvents.add(mockInfo1);
        initialEvents.add(mockInfo2);
        initialEvents.add(mockInfo3);

        AvailableEventState mockCurrentState = mock(AvailableEventState.class);
        when(mockCurrentState.getAvailableEvents()).thenReturn(initialEvents);
        when(mockAvailableEventViewModel.getState()).thenReturn(mockCurrentState);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert - Only the first matching event should be updated (due to break statement)
        verify(mockInfo1, times(1)).setName(name);
        verify(mockInfo1, times(1)).setCategory(category);
        verify(mockInfo1, times(1)).setDescription(description);
        verify(mockInfo2, never()).setName(anyString()); // Should not be called due to break
        verify(mockInfo2, never()).setCategory(anyString());
        verify(mockInfo2, never()).setDescription(anyString());
        verify(mockInfo3, never()).setName(anyString());
        verify(mockInfo3, never()).setCategory(anyString());
        verify(mockInfo3, never()).setDescription(anyString());
    }

    @Test
    void testPrepareFailView() {
        // Arrange
        String eventId = "failed-event-123";
        String errorMessage = "Event edit validation failed";
        EditEventOutputData outputData = new EditEventOutputData(eventId, null, null, null, errorMessage, true);

        // Act
        presenter.prepareFailView(outputData);

        // Assert - Verify EditedEventViewModel is updated with failure state
        verify(mockEditedEventViewModel, times(1)).updateState(any(EditedEventState.class));
        verify(mockEditedEventViewModel, times(1)).firePropertyChanged("state");

        // Assert - Verify AvailableEventViewModel is NOT updated (only updated on success)
        verify(mockAvailableEventViewModel, never()).getState();
        verify(mockAvailableEventViewModel, never()).setState(any());
        verify(mockAvailableEventViewModel, never()).firePropertyChanged(anyString());

        // Assert - Verify optional dependencies are NOT called (only called on success)
        verify(mockTodaysEventsViewModel, never()).firePropertyChanged(anyString());
        verify(mockTodaySoFarController, never()).refresh();
    }

    @Test
    void testPrepareFailViewWithNullErrorMessage() {
        // Arrange
        String eventId = "failed-event-456";
        EditEventOutputData outputData = new EditEventOutputData(eventId, null, null, null, null, true);

        // Act
        presenter.prepareFailView(outputData);

        // Assert - Verify ViewModel is updated even with null error message
        verify(mockEditedEventViewModel, times(1)).updateState(any(EditedEventState.class));
        verify(mockEditedEventViewModel, times(1)).firePropertyChanged("state");
    }

    @Test
    void testPrepareSuccessViewStateValues() {
        // Arrange
        String eventId = "state-test-event";
        String name = "State Test Event";
        String category = "Test Category";
        String description = "Test Description";
        EditEventOutputData outputData = new EditEventOutputData(eventId, name, category, description, false);

        List<InfoInterf> initialEvents = new ArrayList<>();
        AvailableEventState mockCurrentState = mock(AvailableEventState.class);
        when(mockCurrentState.getAvailableEvents()).thenReturn(initialEvents);
        when(mockAvailableEventViewModel.getState()).thenReturn(mockCurrentState);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert - Capture the state that was updated and verify its values
        verify(mockEditedEventViewModel, times(1)).updateState(argThat(state -> {
            EditedEventState capturedState = (EditedEventState) state;
            return eventId.equals(capturedState.getEventId()) &&
                   name.equals(capturedState.getName()) &&
                   category.equals(capturedState.getCategory()) &&
                   description.equals(capturedState.getDescription()) &&
                   capturedState.getNameError() == null &&
                   capturedState.getCategoryError() == null &&
                   capturedState.getDescriptionError() == null &&
                   capturedState.getEditError() == null;
        }));
    }

    @Test
    void testPrepareFailViewStateValues() {
        // Arrange
        String eventId = "fail-state-test";
        String errorMessage = "Validation failed for event fields";
        EditEventOutputData outputData = new EditEventOutputData(eventId, null, null, null, errorMessage, true);

        // Act
        presenter.prepareFailView(outputData);

        // Assert - Capture the state that was updated and verify its values
        verify(mockEditedEventViewModel, times(1)).updateState(argThat(state -> {
            EditedEventState capturedState = (EditedEventState) state;
            return eventId.equals(capturedState.getEventId()) &&
                   capturedState.getName().equals("") && // Should be empty string (default)
                   capturedState.getCategory().equals("") && // Should be empty string (default)
                   capturedState.getDescription().equals("") && // Should be empty string (default)
                   capturedState.getNameError() == null &&
                   capturedState.getCategoryError() == null &&
                   capturedState.getDescriptionError() == null &&
                   errorMessage.equals(capturedState.getEditError());
        }));
    }

    @Test
    void testMultipleSuccessViewCalls() {
        // Arrange
        EditEventOutputData outputData1 = new EditEventOutputData("event-1", "Name 1", "Cat 1", "Desc 1", false);
        EditEventOutputData outputData2 = new EditEventOutputData("event-2", "Name 2", "Cat 2", "Desc 2", false);

        // Mock empty events list for both calls
        List<InfoInterf> initialEvents = new ArrayList<>();
        AvailableEventState mockCurrentState = mock(AvailableEventState.class);
        when(mockCurrentState.getAvailableEvents()).thenReturn(initialEvents);
        when(mockAvailableEventViewModel.getState()).thenReturn(mockCurrentState);

        presenter.setTodaysEventsViewModel(mockTodaysEventsViewModel);
        presenter.setTodaySoFarController(mockTodaySoFarController);

        // Act
        presenter.prepareSuccessView(outputData1);
        presenter.prepareSuccessView(outputData2);

        // Assert - Verify both calls result in ViewModel updates
        verify(mockEditedEventViewModel, times(2)).updateState(any(EditedEventState.class));
        verify(mockAvailableEventViewModel, times(2)).setState(any(AvailableEventState.class));
        verify(mockAvailableEventViewModel, times(2)).firePropertyChanged(AvailableEventViewModel.AVAILABLE_EVENTS_PROPERTY);
        verify(mockTodaysEventsViewModel, times(2)).firePropertyChanged("state");
        verify(mockTodaySoFarController, times(2)).refresh();
    }

    @Test
    void testMultipleFailViewCalls() {
        // Arrange
        EditEventOutputData outputData1 = new EditEventOutputData("failed-1", null, null, null, "Error 1", true);
        EditEventOutputData outputData2 = new EditEventOutputData("failed-2", null, null, null, "Error 2", true);

        // Act
        presenter.prepareFailView(outputData1);
        presenter.prepareFailView(outputData2);

        // Assert - Verify both calls result in ViewModel updates
        verify(mockEditedEventViewModel, times(2)).updateState(any(EditedEventState.class));
        verify(mockEditedEventViewModel, times(2)).firePropertyChanged("state");

        // Assert - Verify other ViewModels and controllers are never called for failures
        verify(mockAvailableEventViewModel, never()).getState();
        verify(mockAvailableEventViewModel, never()).setState(any());
        verify(mockAvailableEventViewModel, never()).firePropertyChanged(anyString());
        verify(mockTodaysEventsViewModel, never()).firePropertyChanged(anyString());
        verify(mockTodaySoFarController, never()).refresh();
    }

    @Test
    void testPrepareSuccessViewWithEmptyEventsList() {
        // Arrange
        String eventId = "non-matching-event";
        String name = "Non-matching Event";
        String category = "Category";
        String description = "Description";
        EditEventOutputData outputData = new EditEventOutputData(eventId, name, category, description, false);

        // Mock empty events list
        List<InfoInterf> initialEvents = new ArrayList<>();
        AvailableEventState mockCurrentState = mock(AvailableEventState.class);
        when(mockCurrentState.getAvailableEvents()).thenReturn(initialEvents);
        when(mockAvailableEventViewModel.getState()).thenReturn(mockCurrentState);

        presenter.setTodaysEventsViewModel(mockTodaysEventsViewModel);
        presenter.setTodaySoFarController(mockTodaySoFarController);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert - Verify ViewModels are updated even with empty events list
        verify(mockEditedEventViewModel, times(1)).updateState(any(EditedEventState.class));
        verify(mockAvailableEventViewModel, times(1)).setState(any(AvailableEventState.class));
        verify(mockAvailableEventViewModel, times(1)).firePropertyChanged(AvailableEventViewModel.AVAILABLE_EVENTS_PROPERTY);
        verify(mockTodaysEventsViewModel, times(1)).firePropertyChanged("state");
        verify(mockTodaySoFarController, times(1)).refresh();
    }

    @Test
    void testPrepareSuccessViewWithNullValues() {
        // Arrange
        String eventId = "null-values-test";
        String name = null;
        String category = null;
        String description = null;
        EditEventOutputData outputData = new EditEventOutputData(eventId, name, category, description, false);

        // Mock event that matches the ID
        when(mockInfo1.getId()).thenReturn(eventId);

        List<InfoInterf> initialEvents = new ArrayList<>();
        initialEvents.add(mockInfo1);

        AvailableEventState mockCurrentState = mock(AvailableEventState.class);
        when(mockCurrentState.getAvailableEvents()).thenReturn(initialEvents);
        when(mockAvailableEventViewModel.getState()).thenReturn(mockCurrentState);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert - Verify event is updated with null values
        verify(mockInfo1, times(1)).setName(null);
        verify(mockInfo1, times(1)).setCategory(null);
        verify(mockInfo1, times(1)).setDescription(null);

        // Assert - Verify ViewModels are still updated
        verify(mockEditedEventViewModel, times(1)).updateState(any(EditedEventState.class));
        verify(mockAvailableEventViewModel, times(1)).setState(any(AvailableEventState.class));
    }

    @Test
    void testPrepareSuccessViewWithPartiallySetOptionalDependencies() {
        // Arrange
        String eventId = "partial-deps-test";
        String name = "Partial Deps Event";
        String category = "Category";
        String description = "Description";
        EditEventOutputData outputData = new EditEventOutputData(eventId, name, category, description, false);

        List<InfoInterf> initialEvents = new ArrayList<>();
        AvailableEventState mockCurrentState = mock(AvailableEventState.class);
        when(mockCurrentState.getAvailableEvents()).thenReturn(initialEvents);
        when(mockAvailableEventViewModel.getState()).thenReturn(mockCurrentState);

        // Set only one optional dependency
        presenter.setTodaysEventsViewModel(mockTodaysEventsViewModel);
        // Don't set TodaySoFarController

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert - Verify set dependency is called, unset dependency is not
        verify(mockTodaysEventsViewModel, times(1)).firePropertyChanged("state");
        verify(mockTodaySoFarController, never()).refresh();
    }
}