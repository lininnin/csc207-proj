package interface_adapter.alex.event_related.todays_events_module.delete_todays_event;

import entity.alex.Event.EventInterf;
import interface_adapter.alex.event_related.add_event.AddedEventViewModel;
import interface_adapter.alex.event_related.todays_events_module.todays_events.TodaysEventsState;
import interface_adapter.alex.event_related.todays_events_module.todays_events.TodaysEventsViewModel;
import interface_adapter.Angela.today_so_far.TodaySoFarController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import use_case.alex.event_related.todays_events_module.delete_todays_event.DeleteTodaysEventOutputData;
import entity.info.Info;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeleteTodaysEventPresenterTest {

    @Mock
    private DeleteTodaysEventViewModel mockDeleteTodaysEventViewModel;

    @Mock
    private TodaysEventsViewModel mockTodaysEventsViewModel;

    @Mock
    private AddedEventViewModel mockAddedEventViewModel;

    @Mock
    private TodaySoFarController mockTodaySoFarController;

    @Mock
    private EventInterf mockEvent1;

    @Mock
    private EventInterf mockEvent2;

    @Mock
    private EventInterf mockEvent3;

    @Mock
    private Info mockInfo1;

    @Mock
    private Info mockInfo2;

    @Mock
    private Info mockInfo3;

    private DeleteTodaysEventPresenter presenter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        presenter = new DeleteTodaysEventPresenter(
                mockDeleteTodaysEventViewModel,
                mockTodaysEventsViewModel,
                mockAddedEventViewModel
        );
    }

    @Test
    void testConstructor() {
        assertNotNull(presenter);
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
        String eventName = "Successful Event";
        DeleteTodaysEventOutputData outputData = new DeleteTodaysEventOutputData(eventId, eventName);

        // Mock the events list in TodaysEventsState
        when(mockEvent1.getInfo()).thenReturn(mockInfo1);
        when(mockEvent2.getInfo()).thenReturn(mockInfo2);
        when(mockEvent3.getInfo()).thenReturn(mockInfo3);
        when(mockInfo1.getId()).thenReturn("different-id-1");
        when(mockInfo2.getId()).thenReturn(eventId); // This event should be removed
        when(mockInfo3.getId()).thenReturn("different-id-3");

        List<EventInterf> initialEvents = new ArrayList<>();
        initialEvents.add(mockEvent1);
        initialEvents.add(mockEvent2);
        initialEvents.add(mockEvent3);

        TodaysEventsState mockCurrentState = mock(TodaysEventsState.class);
        when(mockCurrentState.getTodaysEvents()).thenReturn(initialEvents);
        when(mockTodaysEventsViewModel.getState()).thenReturn(mockCurrentState);

        presenter.setTodaySoFarController(mockTodaySoFarController);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert - Verify DeleteTodaysEventViewModel updates
        verify(mockDeleteTodaysEventViewModel, times(1)).setState(any(DeleteTodaysEventState.class));
        verify(mockDeleteTodaysEventViewModel, times(1)).firePropertyChanged(DeleteTodaysEventViewModel.DELETE_TODAYS_EVENT_STATE_PROPERTY);

        // Assert - Verify TodaysEventsViewModel updates
        verify(mockTodaysEventsViewModel, times(1)).getState();
        verify(mockTodaysEventsViewModel, times(1)).setState(mockCurrentState);
        verify(mockTodaysEventsViewModel, times(1)).firePropertyChanged("state");

        // Assert - Verify TodaySoFarController refresh
        verify(mockTodaySoFarController, times(1)).refresh();
    }

    @Test
    void testPrepareSuccessViewWithoutTodaySoFarController() {
        // Arrange
        String eventId = "success-event-456";
        String eventName = "Event Without Controller";
        DeleteTodaysEventOutputData outputData = new DeleteTodaysEventOutputData(eventId, eventName);

        // Mock empty events list
        List<EventInterf> initialEvents = new ArrayList<>();
        TodaysEventsState mockCurrentState = mock(TodaysEventsState.class);
        when(mockCurrentState.getTodaysEvents()).thenReturn(initialEvents);
        when(mockTodaysEventsViewModel.getState()).thenReturn(mockCurrentState);

        // Don't set TodaySoFarController (leave it null)

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert - Verify ViewModels are updated but controller is not called
        verify(mockDeleteTodaysEventViewModel, times(1)).setState(any(DeleteTodaysEventState.class));
        verify(mockDeleteTodaysEventViewModel, times(1)).firePropertyChanged(DeleteTodaysEventViewModel.DELETE_TODAYS_EVENT_STATE_PROPERTY);
        verify(mockTodaysEventsViewModel, times(1)).setState(mockCurrentState);
        verify(mockTodaysEventsViewModel, times(1)).firePropertyChanged("state");

        // Assert - Verify TodaySoFarController is not called (since it's null)
        verify(mockTodaySoFarController, never()).refresh();
    }

    @Test
    void testPrepareSuccessViewRemovesCorrectEvent() {
        // Arrange
        String targetEventId = "target-event-789";
        String eventName = "Target Event";
        DeleteTodaysEventOutputData outputData = new DeleteTodaysEventOutputData(targetEventId, eventName);

        // Create mock events with different IDs
        when(mockEvent1.getInfo()).thenReturn(mockInfo1);
        when(mockEvent2.getInfo()).thenReturn(mockInfo2);
        when(mockEvent3.getInfo()).thenReturn(mockInfo3);
        when(mockInfo1.getId()).thenReturn("keep-event-1");
        when(mockInfo2.getId()).thenReturn(targetEventId); // This should be removed
        when(mockInfo3.getId()).thenReturn("keep-event-3");

        List<EventInterf> initialEvents = new ArrayList<>();
        initialEvents.add(mockEvent1);
        initialEvents.add(mockEvent2);
        initialEvents.add(mockEvent3);

        TodaysEventsState mockCurrentState = mock(TodaysEventsState.class);
        when(mockCurrentState.getTodaysEvents()).thenReturn(initialEvents);
        when(mockTodaysEventsViewModel.getState()).thenReturn(mockCurrentState);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert - Verify the correct event was removed from the list
        verify(mockCurrentState, times(1)).setTodaysEvents(argThat(list -> {
            // The updated list should have 2 events (original 3 minus the deleted one)
            return list.size() == 2 && 
                   list.contains(mockEvent1) && 
                   list.contains(mockEvent3) && 
                   !list.contains(mockEvent2);
        }));
    }

    @Test
    void testPrepareSuccessViewWithEventWithNullInfo() {
        // Arrange
        String eventId = "event-with-null-info";
        String eventName = "Event Name";
        DeleteTodaysEventOutputData outputData = new DeleteTodaysEventOutputData(eventId, eventName);

        // Create events where one has null info
        when(mockEvent1.getInfo()).thenReturn(mockInfo1);
        when(mockEvent2.getInfo()).thenReturn(null); // This event has null info
        when(mockEvent3.getInfo()).thenReturn(mockInfo3);
        when(mockInfo1.getId()).thenReturn("valid-id-1");
        when(mockInfo3.getId()).thenReturn("valid-id-3");

        List<EventInterf> initialEvents = new ArrayList<>();
        initialEvents.add(mockEvent1);
        initialEvents.add(mockEvent2);
        initialEvents.add(mockEvent3);

        TodaysEventsState mockCurrentState = mock(TodaysEventsState.class);
        when(mockCurrentState.getTodaysEvents()).thenReturn(initialEvents);
        when(mockTodaysEventsViewModel.getState()).thenReturn(mockCurrentState);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert - All events should remain since none match the eventId (event with null info is ignored)
        verify(mockCurrentState, times(1)).setTodaysEvents(argThat(list -> 
            list.size() == 3 && 
            list.contains(mockEvent1) && 
            list.contains(mockEvent2) && 
            list.contains(mockEvent3)
        ));
    }

    @Test
    void testPrepareFailView() {
        // Arrange
        String eventId = "failed-event-123";
        String eventName = "Failed Event";
        String errorMessage = "Event deletion failed";
        DeleteTodaysEventOutputData outputData = new DeleteTodaysEventOutputData(eventId, errorMessage, false);

        // Act
        presenter.prepareFailView(outputData);

        // Assert - Verify DeleteTodaysEventViewModel is updated with failure state
        verify(mockDeleteTodaysEventViewModel, times(1)).setState(any(DeleteTodaysEventState.class));
        verify(mockDeleteTodaysEventViewModel, times(1)).firePropertyChanged(DeleteTodaysEventViewModel.DELETE_TODAYS_EVENT_STATE_PROPERTY);

        // Assert - Verify TodaysEventsViewModel is NOT updated (only updated on success)
        verify(mockTodaysEventsViewModel, never()).setState(any());
        verify(mockTodaysEventsViewModel, never()).firePropertyChanged(anyString());

        // Assert - Verify TodaySoFarController is NOT called (only called on success)
        verify(mockTodaySoFarController, never()).refresh();
    }

    @Test
    void testPrepareFailViewWithNullErrorMessage() {
        // Arrange
        String eventId = "failed-event-456";
        String eventName = "Event Name";
        DeleteTodaysEventOutputData outputData = new DeleteTodaysEventOutputData(eventId, null, false);

        // Act
        presenter.prepareFailView(outputData);

        // Assert - Verify ViewModel is updated even with null error message
        verify(mockDeleteTodaysEventViewModel, times(1)).setState(any(DeleteTodaysEventState.class));
        verify(mockDeleteTodaysEventViewModel, times(1)).firePropertyChanged(DeleteTodaysEventViewModel.DELETE_TODAYS_EVENT_STATE_PROPERTY);
    }

    @Test
    void testPrepareFailViewWithEmptyErrorMessage() {
        // Arrange
        String eventId = "failed-event-789";
        String eventName = "Event Name";
        String errorMessage = "";
        DeleteTodaysEventOutputData outputData = new DeleteTodaysEventOutputData(eventId, errorMessage, false);

        // Act
        presenter.prepareFailView(outputData);

        // Assert - Verify ViewModel is updated with empty error message
        verify(mockDeleteTodaysEventViewModel, times(1)).setState(any(DeleteTodaysEventState.class));
        verify(mockDeleteTodaysEventViewModel, times(1)).firePropertyChanged(DeleteTodaysEventViewModel.DELETE_TODAYS_EVENT_STATE_PROPERTY);
    }

    @Test
    void testMultipleSuccessViewCalls() {
        // Arrange
        DeleteTodaysEventOutputData outputData1 = new DeleteTodaysEventOutputData("event-1", "Event One");
        DeleteTodaysEventOutputData outputData2 = new DeleteTodaysEventOutputData("event-2", "Event Two");

        // Mock empty events list for both calls
        List<EventInterf> initialEvents = new ArrayList<>();
        TodaysEventsState mockCurrentState = mock(TodaysEventsState.class);
        when(mockCurrentState.getTodaysEvents()).thenReturn(initialEvents);
        when(mockTodaysEventsViewModel.getState()).thenReturn(mockCurrentState);

        presenter.setTodaySoFarController(mockTodaySoFarController);

        // Act
        presenter.prepareSuccessView(outputData1);
        presenter.prepareSuccessView(outputData2);

        // Assert - Verify both calls result in ViewModel updates
        verify(mockDeleteTodaysEventViewModel, times(2)).setState(any(DeleteTodaysEventState.class));
        verify(mockDeleteTodaysEventViewModel, times(2)).firePropertyChanged(DeleteTodaysEventViewModel.DELETE_TODAYS_EVENT_STATE_PROPERTY);
        verify(mockTodaysEventsViewModel, times(2)).setState(mockCurrentState);
        verify(mockTodaysEventsViewModel, times(2)).firePropertyChanged("state");
        verify(mockTodaySoFarController, times(2)).refresh();
    }

    @Test
    void testMultipleFailViewCalls() {
        // Arrange
        DeleteTodaysEventOutputData outputData1 = new DeleteTodaysEventOutputData("failed-1", "Error 1", false);
        DeleteTodaysEventOutputData outputData2 = new DeleteTodaysEventOutputData("failed-2", "Error 2", false);

        // Act
        presenter.prepareFailView(outputData1);
        presenter.prepareFailView(outputData2);

        // Assert - Verify both calls result in ViewModel updates
        verify(mockDeleteTodaysEventViewModel, times(2)).setState(any(DeleteTodaysEventState.class));
        verify(mockDeleteTodaysEventViewModel, times(2)).firePropertyChanged(DeleteTodaysEventViewModel.DELETE_TODAYS_EVENT_STATE_PROPERTY);

        // Assert - Verify TodaysEventsViewModel and TodaySoFarController are never called for failures
        verify(mockTodaysEventsViewModel, never()).setState(any());
        verify(mockTodaysEventsViewModel, never()).firePropertyChanged(anyString());
        verify(mockTodaySoFarController, never()).refresh();
    }

    @Test
    void testPrepareSuccessViewWithEmptyEventsList() {
        // Arrange
        String eventId = "non-existent-event";
        String eventName = "Non-existent Event";
        DeleteTodaysEventOutputData outputData = new DeleteTodaysEventOutputData(eventId, eventName);

        // Mock empty events list
        List<EventInterf> initialEvents = new ArrayList<>();
        TodaysEventsState mockCurrentState = mock(TodaysEventsState.class);
        when(mockCurrentState.getTodaysEvents()).thenReturn(initialEvents);
        when(mockTodaysEventsViewModel.getState()).thenReturn(mockCurrentState);

        presenter.setTodaySoFarController(mockTodaySoFarController);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert - Verify list remains empty (no events to remove)
        verify(mockCurrentState, times(1)).setTodaysEvents(argThat(list -> list.isEmpty()));
        verify(mockDeleteTodaysEventViewModel, times(1)).setState(any(DeleteTodaysEventState.class));
        verify(mockDeleteTodaysEventViewModel, times(1)).firePropertyChanged(DeleteTodaysEventViewModel.DELETE_TODAYS_EVENT_STATE_PROPERTY);
        verify(mockTodaySoFarController, times(1)).refresh();
    }

    @Test
    void testPrepareSuccessViewStateValues() {
        // Arrange
        String eventId = "state-test-event";
        String eventName = "State Test Event";
        DeleteTodaysEventOutputData outputData = new DeleteTodaysEventOutputData(eventId, eventName);

        List<EventInterf> initialEvents = new ArrayList<>();
        TodaysEventsState mockCurrentState = mock(TodaysEventsState.class);
        when(mockCurrentState.getTodaysEvents()).thenReturn(initialEvents);
        when(mockTodaysEventsViewModel.getState()).thenReturn(mockCurrentState);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert - Capture the state that was set and verify its values
        verify(mockDeleteTodaysEventViewModel, times(1)).setState(argThat(state -> {
            DeleteTodaysEventState capturedState = (DeleteTodaysEventState) state;
            return eventId.equals(capturedState.getDeletedEventId()) &&
                   eventName.equals(capturedState.getDeletedEventName()) &&
                   capturedState.isDeletedSuccessfully() &&
                   capturedState.getDeleteError() == null;
        }));
    }

    @Test
    void testPrepareFailViewStateValues() {
        // Arrange
        String eventId = "fail-state-test";
        String errorMessage = "Test error message";
        DeleteTodaysEventOutputData outputData = new DeleteTodaysEventOutputData(eventId, errorMessage, true);

        // Act
        presenter.prepareFailView(outputData);

        // Assert - Capture the state that was set and verify its values
        verify(mockDeleteTodaysEventViewModel, times(1)).setState(argThat(state -> {
            DeleteTodaysEventState capturedState = (DeleteTodaysEventState) state;
            return eventId.equals(capturedState.getDeletedEventId()) &&
                   capturedState.getDeletedEventName() == null && // eventName is null for failed deletion
                   !capturedState.isDeletedSuccessfully() &&
                   errorMessage.equals(capturedState.getDeleteError());
        }));
    }
}