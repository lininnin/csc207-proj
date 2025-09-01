package interface_adapter.alex.event_related.available_event_module.delete_event;

import entity.info.InfoInterf;
import interface_adapter.alex.event_related.available_event_module.available_event.AvailableEventState;
import interface_adapter.alex.event_related.available_event_module.available_event.AvailableEventViewModel;
import interface_adapter.alex.event_related.add_event.AddedEventState;
import interface_adapter.alex.event_related.add_event.AddedEventViewModel;
import interface_adapter.Angela.today_so_far.TodaySoFarController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import use_case.alex.event_related.avaliable_events_module.delete_event.DeleteEventOutputData;

import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeleteEventPresenterTest {

    @Mock
    private TodaySoFarController mockTodaySoFarController;
    @Mock
    private InfoInterf mockInfo1;
    @Mock
    private InfoInterf mockInfo2;
    @Mock
    private InfoInterf mockInfo3;

    private DeletedEventViewModel deletedEventViewModel;
    private AvailableEventViewModel availableEventViewModel;
    private AddedEventViewModel addedEventViewModel;
    private DeleteEventPresenter presenter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        deletedEventViewModel = new DeletedEventViewModel();
        availableEventViewModel = new AvailableEventViewModel();
        addedEventViewModel = new AddedEventViewModel();
        presenter = new DeleteEventPresenter(deletedEventViewModel, availableEventViewModel, addedEventViewModel);
    }

    @Test
    void testConstructor() {
        assertNotNull(presenter);
        // Constructor coverage is achieved by setUp() method
    }

    @Test
    void testSetTodaySoFarController() {
        // Act
        presenter.setTodaySoFarController(mockTodaySoFarController);
        
        // Test by calling prepareSuccessView and verifying controller is called
        DeleteEventOutputData successData = new DeleteEventOutputData("event-123", "Test Event");
        setupMockInfos();
        
        presenter.prepareSuccessView(successData);
        
        verify(mockTodaySoFarController, times(1)).refresh();
    }

    @Test
    void testPrepareSuccessViewUpdatesDeletedEventViewModel() {
        // Arrange
        DeleteEventOutputData successData = new DeleteEventOutputData("event-456", "Meeting Event");
        PropertyChangeListener deletedListener = mock(PropertyChangeListener.class);
        deletedEventViewModel.addPropertyChangeListener(deletedListener);
        setupMockInfos();

        // Act
        presenter.prepareSuccessView(successData);

        // Assert
        DeletedEventState state = deletedEventViewModel.getState();
        assertEquals("event-456", state.getDeletedEventId());
        assertEquals("Meeting Event", state.getDeletedEventName());
        assertTrue(state.isDeletedSuccessfully());
        assertNull(state.getDeleteError());
    }

    @Test
    void testPrepareSuccessViewUpdatesAvailableEventViewModel() {
        // Arrange
        DeleteEventOutputData successData = new DeleteEventOutputData("event-to-delete", "Delete Me");
        setupMockInfosWithTargetEvent("event-to-delete");
        
        PropertyChangeListener availableListener = mock(PropertyChangeListener.class);
        availableEventViewModel.addPropertyChangeListener(availableListener);

        // Act
        presenter.prepareSuccessView(successData);

        // Assert
        AvailableEventState availableState = availableEventViewModel.getState();
        List<InfoInterf> updatedEvents = availableState.getAvailableEvents();
        assertEquals(2, updatedEvents.size()); // Should have removed the target event
        assertFalse(updatedEvents.stream().anyMatch(info -> "event-to-delete".equals(info.getId())));
        verify(availableListener, atLeastOnce()).propertyChange(any());
    }

    @Test
    void testPrepareSuccessViewUpdatesAddedEventViewModel() {
        // Arrange
        DeleteEventOutputData successData = new DeleteEventOutputData("event-789", "Workshop Event");
        setupMockInfos();
        
        PropertyChangeListener addedListener = mock(PropertyChangeListener.class);
        addedEventViewModel.addPropertyChangeListener(addedListener);

        // Act
        presenter.prepareSuccessView(successData);

        // Assert
        AddedEventState addedState = addedEventViewModel.getState();
        List<String> availableNames = addedState.getAvailableNames();
        assertEquals(3, availableNames.size());
        assertEquals(Arrays.asList("Event 1", "Event 2", "Event 3"), availableNames);
        verify(addedListener, atLeastOnce()).propertyChange(any());
    }

    @Test
    void testPrepareSuccessViewWithoutTodaySoFarController() {
        // Arrange
        DeleteEventOutputData successData = new DeleteEventOutputData("event-no-controller", "No Controller Event");
        setupMockInfos();
        
        // Note: not setting todaySoFarController (it should remain null)

        // Act
        presenter.prepareSuccessView(successData);

        // Assert
        // Should not throw exception and should still update ViewModels
        assertEquals("event-no-controller", deletedEventViewModel.getState().getDeletedEventId());
        assertEquals("No Controller Event", deletedEventViewModel.getState().getDeletedEventName());
        assertTrue(deletedEventViewModel.getState().isDeletedSuccessfully());
    }

    @Test
    void testPrepareSuccessViewWithEmptyAvailableEvents() {
        // Arrange
        DeleteEventOutputData successData = new DeleteEventOutputData("event-empty", "Empty List Event");
        
        // Setup empty available events list
        AvailableEventState emptyState = availableEventViewModel.getState();
        emptyState.setAvailableEvents(Arrays.asList());
        availableEventViewModel.setState(emptyState);

        // Act
        presenter.prepareSuccessView(successData);

        // Assert
        AddedEventState addedState = addedEventViewModel.getState();
        List<String> availableNames = addedState.getAvailableNames();
        assertEquals(0, availableNames.size());
        assertTrue(availableNames.isEmpty());
    }

    @Test
    void testPrepareFailView() {
        // Arrange
        String errorMessage = "Event not found or deletion failed";
        DeleteEventOutputData failData = new DeleteEventOutputData("event-fail", errorMessage, false);
        PropertyChangeListener deletedListener = mock(PropertyChangeListener.class);
        deletedEventViewModel.addPropertyChangeListener(deletedListener);

        // Act
        presenter.prepareFailView(failData);

        // Assert
        DeletedEventState errorState = deletedEventViewModel.getState();
        assertEquals("event-fail", errorState.getDeletedEventId());
        assertNull(errorState.getDeletedEventName()); // Should be null for failed deletion
        assertFalse(errorState.isDeletedSuccessfully());
        assertEquals(errorMessage, errorState.getDeleteError());
    }

    @Test
    void testPrepareFailViewWithNullError() {
        // Arrange
        DeleteEventOutputData failData = new DeleteEventOutputData("event-null-error", null, false);

        // Act
        presenter.prepareFailView(failData);

        // Assert
        DeletedEventState errorState = deletedEventViewModel.getState();
        assertEquals("event-null-error", errorState.getDeletedEventId());
        assertFalse(errorState.isDeletedSuccessfully());
        assertNull(errorState.getDeleteError());
    }

    @Test
    void testPrepareFailViewDoesNotAffectOtherViewModels() {
        // Arrange
        String errorMessage = "Database connection failed";
        DeleteEventOutputData failData = new DeleteEventOutputData("event-db-fail", errorMessage, false);
        setupMockInfos();
        
        // Store initial states
        AvailableEventState initialAvailableState = availableEventViewModel.getState();
        AddedEventState initialAddedState = addedEventViewModel.getState();
        
        PropertyChangeListener availableListener = mock(PropertyChangeListener.class);
        PropertyChangeListener addedListener = mock(PropertyChangeListener.class);
        availableEventViewModel.addPropertyChangeListener(availableListener);
        addedEventViewModel.addPropertyChangeListener(addedListener);

        // Act
        presenter.prepareFailView(failData);

        // Assert
        // Available and Added ViewModels should not be affected during failure
        assertEquals(initialAvailableState, availableEventViewModel.getState());
        assertEquals(initialAddedState, addedEventViewModel.getState());
        verifyNoInteractions(availableListener, addedListener);
    }

    @Test
    void testPrepareSuccessViewFiresCorrectViewModelMethods() {
        // Arrange
        DeleteEventOutputData successData = new DeleteEventOutputData("event-test", "Test Fire Methods");
        setupMockInfos();
        
        PropertyChangeListener deletedListener = mock(PropertyChangeListener.class);
        PropertyChangeListener availableListener = mock(PropertyChangeListener.class);
        PropertyChangeListener addedListener = mock(PropertyChangeListener.class);
        deletedEventViewModel.addPropertyChangeListener(deletedListener);
        availableEventViewModel.addPropertyChangeListener(availableListener);
        addedEventViewModel.addPropertyChangeListener(addedListener);

        // Act
        presenter.prepareSuccessView(successData);

        // Assert - verify all ViewModels fire property changes
        verify(deletedListener, atLeastOnce()).propertyChange(any());
        verify(availableListener, atLeastOnce()).propertyChange(any());
        verify(addedListener, atLeastOnce()).propertyChange(any());
    }

    @Test
    void testPrepareSuccessViewWithSpecificEventRemoval() {
        // Arrange
        String targetEventId = "event-target-removal";
        DeleteEventOutputData successData = new DeleteEventOutputData(targetEventId, "Target Event");
        
        // Setup mock infos where one matches the target ID
        when(mockInfo1.getId()).thenReturn("event-1");
        when(mockInfo1.getName()).thenReturn("Event 1");
        when(mockInfo2.getId()).thenReturn(targetEventId);
        when(mockInfo2.getName()).thenReturn("Target Event");
        when(mockInfo3.getId()).thenReturn("event-3");
        when(mockInfo3.getName()).thenReturn("Event 3");
        
        AvailableEventState currentState = availableEventViewModel.getState();
        currentState.setAvailableEvents(Arrays.asList(mockInfo1, mockInfo2, mockInfo3));
        availableEventViewModel.setState(currentState);

        // Act
        presenter.prepareSuccessView(successData);

        // Assert
        AvailableEventState updatedState = availableEventViewModel.getState();
        List<InfoInterf> remainingEvents = updatedState.getAvailableEvents();
        assertEquals(2, remainingEvents.size());
        
        // Verify the specific event was removed
        assertFalse(remainingEvents.stream().anyMatch(info -> targetEventId.equals(info.getId())));
        assertTrue(remainingEvents.stream().anyMatch(info -> "event-1".equals(info.getId())));
        assertTrue(remainingEvents.stream().anyMatch(info -> "event-3".equals(info.getId())));
        
        // Verify names were updated correctly
        AddedEventState addedState = addedEventViewModel.getState();
        List<String> names = addedState.getAvailableNames();
        assertEquals(Arrays.asList("Event 1", "Event 3"), names);
    }

    @Test
    void testMultipleSuccessCallsUpdateState() {
        // Arrange
        setupMockInfos();
        
        DeleteEventOutputData firstData = new DeleteEventOutputData("event-first", "First Event");
        DeleteEventOutputData secondData = new DeleteEventOutputData("event-second", "Second Event");

        // Act
        presenter.prepareSuccessView(firstData);
        presenter.prepareSuccessView(secondData);

        // Assert - final state should reflect the last call
        DeletedEventState finalState = deletedEventViewModel.getState();
        assertEquals("event-second", finalState.getDeletedEventId());
        assertEquals("Second Event", finalState.getDeletedEventName());
        assertTrue(finalState.isDeletedSuccessfully());
        assertNull(finalState.getDeleteError());
    }

    // Helper methods
    private void setupMockInfos() {
        when(mockInfo1.getId()).thenReturn("event-1");
        when(mockInfo1.getName()).thenReturn("Event 1");
        when(mockInfo2.getId()).thenReturn("event-2");
        when(mockInfo2.getName()).thenReturn("Event 2");
        when(mockInfo3.getId()).thenReturn("event-3");
        when(mockInfo3.getName()).thenReturn("Event 3");
        
        AvailableEventState currentState = availableEventViewModel.getState();
        currentState.setAvailableEvents(Arrays.asList(mockInfo1, mockInfo2, mockInfo3));
        availableEventViewModel.setState(currentState);
    }
    
    private void setupMockInfosWithTargetEvent(String targetId) {
        when(mockInfo1.getId()).thenReturn("event-1");
        when(mockInfo1.getName()).thenReturn("Event 1");
        when(mockInfo2.getId()).thenReturn(targetId);
        when(mockInfo2.getName()).thenReturn("Target Event");
        when(mockInfo3.getId()).thenReturn("event-3");
        when(mockInfo3.getName()).thenReturn("Event 3");
        
        AvailableEventState currentState = availableEventViewModel.getState();
        currentState.setAvailableEvents(Arrays.asList(mockInfo1, mockInfo2, mockInfo3));
        availableEventViewModel.setState(currentState);
    }
}