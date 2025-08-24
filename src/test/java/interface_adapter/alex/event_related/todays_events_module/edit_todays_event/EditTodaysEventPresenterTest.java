package interface_adapter.alex.event_related.todays_events_module.edit_todays_event;

import entity.alex.Event.EventInterf;
import entity.info.Info;
import interface_adapter.alex.event_related.todays_events_module.todays_events.TodaysEventsState;
import interface_adapter.alex.event_related.todays_events_module.todays_events.TodaysEventsViewModel;
import interface_adapter.Angela.today_so_far.TodaySoFarController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import use_case.alex.event_related.todays_events_module.edit_todays_event.EditTodaysEventOutputData;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EditTodaysEventPresenterTest {

    @Mock
    private EditTodaysEventViewModel mockEditTodaysEventViewModel;

    @Mock
    private TodaysEventsViewModel mockTodaysEventsViewModel;

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

    private EditTodaysEventPresenter presenter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        presenter = new EditTodaysEventPresenter(
                mockEditTodaysEventViewModel,
                mockTodaysEventsViewModel
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
        String dueDate = "2024-12-25";
        EditTodaysEventOutputData outputData = new EditTodaysEventOutputData(eventId, dueDate, false);

        // Mock the events list in TodaysEventsState
        when(mockEvent1.getInfo()).thenReturn(mockInfo1);
        when(mockEvent2.getInfo()).thenReturn(mockInfo2);
        when(mockEvent3.getInfo()).thenReturn(mockInfo3);
        when(mockInfo1.getId()).thenReturn("different-id-1");
        when(mockInfo2.getId()).thenReturn(eventId); // This event should be updated
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

        // Assert - Verify EditTodaysEventViewModel updates
        verify(mockEditTodaysEventViewModel, times(1)).updateState(any(EditTodaysEventState.class));

        // Assert - Verify the correct event's due date was updated
        verify(mockEvent2, times(1)).editDueDate(LocalDate.parse(dueDate));
        verify(mockEvent1, never()).editDueDate(any());
        verify(mockEvent3, never()).editDueDate(any());

        // Assert - Verify TodaysEventsViewModel updates
        verify(mockTodaysEventsViewModel, times(1)).getState();
        verify(mockTodaysEventsViewModel, times(1)).setState(any(TodaysEventsState.class));
        verify(mockTodaysEventsViewModel, times(1)).firePropertyChanged(TodaysEventsViewModel.TODAYS_EVENTS_PROPERTY);

        // Assert - Verify TodaySoFarController refresh
        verify(mockTodaySoFarController, times(1)).refresh();
    }

    @Test
    void testPrepareSuccessViewWithoutTodaySoFarController() {
        // Arrange
        String eventId = "success-event-456";
        String dueDate = "2024-06-15";
        EditTodaysEventOutputData outputData = new EditTodaysEventOutputData(eventId, dueDate, false);

        // Mock empty events list
        List<EventInterf> initialEvents = new ArrayList<>();
        TodaysEventsState mockCurrentState = mock(TodaysEventsState.class);
        when(mockCurrentState.getTodaysEvents()).thenReturn(initialEvents);
        when(mockTodaysEventsViewModel.getState()).thenReturn(mockCurrentState);

        // Don't set TodaySoFarController (leave it null)

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert - Verify ViewModels are updated but controller is not called
        verify(mockEditTodaysEventViewModel, times(1)).updateState(any(EditTodaysEventState.class));
        verify(mockTodaysEventsViewModel, times(1)).setState(any(TodaysEventsState.class));
        verify(mockTodaysEventsViewModel, times(1)).firePropertyChanged(TodaysEventsViewModel.TODAYS_EVENTS_PROPERTY);

        // Assert - Verify TodaySoFarController is not called (since it's null)
        verify(mockTodaySoFarController, never()).refresh();
    }

    @Test
    void testPrepareSuccessViewUpdatesCorrectEvent() {
        // Arrange
        String targetEventId = "target-event-789";
        String newDueDate = "2024-03-20";
        EditTodaysEventOutputData outputData = new EditTodaysEventOutputData(targetEventId, newDueDate, false);

        // Create mock events with different IDs
        when(mockEvent1.getInfo()).thenReturn(mockInfo1);
        when(mockEvent2.getInfo()).thenReturn(mockInfo2);
        when(mockEvent3.getInfo()).thenReturn(mockInfo3);
        when(mockInfo1.getId()).thenReturn("keep-event-1");
        when(mockInfo2.getId()).thenReturn(targetEventId); // This should be updated
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

        // Assert - Verify only the target event was updated
        verify(mockEvent2, times(1)).editDueDate(LocalDate.parse(newDueDate));
        verify(mockEvent1, never()).editDueDate(any());
        verify(mockEvent3, never()).editDueDate(any());
    }

    @Test
    void testPrepareSuccessViewWithEventWithNullInfo() {
        // Arrange
        String eventId = "event-with-null-info";
        String dueDate = "2024-07-30";
        EditTodaysEventOutputData outputData = new EditTodaysEventOutputData(eventId, dueDate, false);

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

        // Assert - No events should be updated since none match the eventId (event with null info is ignored)
        verify(mockEvent1, never()).editDueDate(any());
        verify(mockEvent2, never()).editDueDate(any());
        verify(mockEvent3, never()).editDueDate(any());
    }

    @Test
    void testPrepareSuccessViewWithNonExistentEventId() {
        // Arrange
        String nonExistentEventId = "non-existent-event-id";
        String dueDate = "2024-09-15";
        EditTodaysEventOutputData outputData = new EditTodaysEventOutputData(nonExistentEventId, dueDate, false);

        // Mock events with different IDs
        when(mockEvent1.getInfo()).thenReturn(mockInfo1);
        when(mockEvent2.getInfo()).thenReturn(mockInfo2);
        when(mockInfo1.getId()).thenReturn("existing-id-1");
        when(mockInfo2.getId()).thenReturn("existing-id-2");

        List<EventInterf> initialEvents = new ArrayList<>();
        initialEvents.add(mockEvent1);
        initialEvents.add(mockEvent2);

        TodaysEventsState mockCurrentState = mock(TodaysEventsState.class);
        when(mockCurrentState.getTodaysEvents()).thenReturn(initialEvents);
        when(mockTodaysEventsViewModel.getState()).thenReturn(mockCurrentState);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert - No events should be updated since the ID doesn't match any event
        verify(mockEvent1, never()).editDueDate(any());
        verify(mockEvent2, never()).editDueDate(any());

        // Assert - ViewModels should still be updated
        verify(mockEditTodaysEventViewModel, times(1)).updateState(any(EditTodaysEventState.class));
        verify(mockTodaysEventsViewModel, times(1)).setState(any(TodaysEventsState.class));
    }

    @Test
    void testPrepareFailView() {
        // Arrange
        String eventId = "failed-event-123";
        String dueDate = "invalid-date";
        EditTodaysEventOutputData outputData = new EditTodaysEventOutputData(eventId, dueDate, true); // useCaseFailed = true

        // Act
        presenter.prepareFailView(outputData);

        // Assert - Verify EditTodaysEventViewModel is updated with failure state
        verify(mockEditTodaysEventViewModel, times(1)).updateState(any(EditTodaysEventState.class));

        // Assert - Verify TodaysEventsViewModel is NOT updated (only updated on success)
        verify(mockTodaysEventsViewModel, never()).getState();
        verify(mockTodaysEventsViewModel, never()).setState(any());
        verify(mockTodaysEventsViewModel, never()).firePropertyChanged(anyString());

        // Assert - Verify TodaySoFarController is NOT called (only called on success)
        verify(mockTodaySoFarController, never()).refresh();
    }

    @Test
    void testPrepareFailViewWithNullDueDate() {
        // Arrange
        String eventId = "failed-event-456";
        String dueDate = null;
        EditTodaysEventOutputData outputData = new EditTodaysEventOutputData(eventId, dueDate, true);

        // Act
        presenter.prepareFailView(outputData);

        // Assert - Verify ViewModel is updated even with null due date
        verify(mockEditTodaysEventViewModel, times(1)).updateState(any(EditTodaysEventState.class));
    }

    @Test
    void testPrepareSuccessViewStateValues() {
        // Arrange
        String eventId = "state-test-event";
        String dueDate = "2024-11-11";
        EditTodaysEventOutputData outputData = new EditTodaysEventOutputData(eventId, dueDate, false);

        List<EventInterf> initialEvents = new ArrayList<>();
        TodaysEventsState mockCurrentState = mock(TodaysEventsState.class);
        when(mockCurrentState.getTodaysEvents()).thenReturn(initialEvents);
        when(mockTodaysEventsViewModel.getState()).thenReturn(mockCurrentState);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert - Capture the state that was updated and verify its values
        verify(mockEditTodaysEventViewModel, times(1)).updateState(argThat(state -> {
            EditTodaysEventState capturedState = (EditTodaysEventState) state;
            return eventId.equals(capturedState.getEventId()) &&
                   dueDate.equals(capturedState.getDueDate()) &&
                   capturedState.getDueDateError() == null &&
                   capturedState.getEditError() == null;
        }));
    }

    @Test
    void testPrepareFailViewStateValues() {
        // Arrange
        String eventId = "fail-state-test";
        String dueDate = "invalid-date-format";
        EditTodaysEventOutputData outputData = new EditTodaysEventOutputData(eventId, dueDate, true);

        // Act
        presenter.prepareFailView(outputData);

        // Assert - Capture the state that was updated and verify its values
        verify(mockEditTodaysEventViewModel, times(1)).updateState(argThat(state -> {
            EditTodaysEventState capturedState = (EditTodaysEventState) state;
            return eventId.equals(capturedState.getEventId()) &&
                   capturedState.getDueDate().equals("") && // Due date should be empty in fail state
                   capturedState.getDueDateError() == null &&
                   "Edit failed: invalid due date or event not found.".equals(capturedState.getEditError());
        }));
    }

    @Test
    void testMultipleSuccessViewCalls() {
        // Arrange
        EditTodaysEventOutputData outputData1 = new EditTodaysEventOutputData("event-1", "2024-01-01", false);
        EditTodaysEventOutputData outputData2 = new EditTodaysEventOutputData("event-2", "2024-02-02", false);

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
        verify(mockEditTodaysEventViewModel, times(2)).updateState(any(EditTodaysEventState.class));
        verify(mockTodaysEventsViewModel, times(2)).setState(any(TodaysEventsState.class));
        verify(mockTodaysEventsViewModel, times(2)).firePropertyChanged(TodaysEventsViewModel.TODAYS_EVENTS_PROPERTY);
        verify(mockTodaySoFarController, times(2)).refresh();
    }

    @Test
    void testMultipleFailViewCalls() {
        // Arrange
        EditTodaysEventOutputData outputData1 = new EditTodaysEventOutputData("failed-1", "bad-date-1", true);
        EditTodaysEventOutputData outputData2 = new EditTodaysEventOutputData("failed-2", "bad-date-2", true);

        // Act
        presenter.prepareFailView(outputData1);
        presenter.prepareFailView(outputData2);

        // Assert - Verify both calls result in ViewModel updates
        verify(mockEditTodaysEventViewModel, times(2)).updateState(any(EditTodaysEventState.class));

        // Assert - Verify TodaysEventsViewModel and TodaySoFarController are never called for failures
        verify(mockTodaysEventsViewModel, never()).getState();
        verify(mockTodaysEventsViewModel, never()).setState(any());
        verify(mockTodaysEventsViewModel, never()).firePropertyChanged(anyString());
        verify(mockTodaySoFarController, never()).refresh();
    }

    @Test
    void testPrepareSuccessViewWithEmptyEventsList() {
        // Arrange
        String eventId = "non-matching-event";
        String dueDate = "2024-05-20";
        EditTodaysEventOutputData outputData = new EditTodaysEventOutputData(eventId, dueDate, false);

        // Mock empty events list
        List<EventInterf> initialEvents = new ArrayList<>();
        TodaysEventsState mockCurrentState = mock(TodaysEventsState.class);
        when(mockCurrentState.getTodaysEvents()).thenReturn(initialEvents);
        when(mockTodaysEventsViewModel.getState()).thenReturn(mockCurrentState);

        presenter.setTodaySoFarController(mockTodaySoFarController);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert - Verify ViewModels are updated even with empty events list
        verify(mockEditTodaysEventViewModel, times(1)).updateState(any(EditTodaysEventState.class));
        verify(mockTodaysEventsViewModel, times(1)).setState(any(TodaysEventsState.class));
        verify(mockTodaysEventsViewModel, times(1)).firePropertyChanged(TodaysEventsViewModel.TODAYS_EVENTS_PROPERTY);
        verify(mockTodaySoFarController, times(1)).refresh();
    }

    @Test
    void testPrepareSuccessViewBreaksAfterFirstMatch() {
        // Arrange
        String targetEventId = "duplicate-event-id";
        String dueDate = "2024-08-15";
        EditTodaysEventOutputData outputData = new EditTodaysEventOutputData(targetEventId, dueDate, false);

        // Create multiple events with the same ID to test that only the first one is updated (break statement)
        when(mockEvent1.getInfo()).thenReturn(mockInfo1);
        when(mockEvent2.getInfo()).thenReturn(mockInfo2);
        when(mockEvent3.getInfo()).thenReturn(mockInfo3);
        when(mockInfo1.getId()).thenReturn(targetEventId);
        when(mockInfo2.getId()).thenReturn(targetEventId); // Same ID - should not be updated due to break
        when(mockInfo3.getId()).thenReturn("different-id");

        List<EventInterf> initialEvents = new ArrayList<>();
        initialEvents.add(mockEvent1);
        initialEvents.add(mockEvent2);
        initialEvents.add(mockEvent3);

        TodaysEventsState mockCurrentState = mock(TodaysEventsState.class);
        when(mockCurrentState.getTodaysEvents()).thenReturn(initialEvents);
        when(mockTodaysEventsViewModel.getState()).thenReturn(mockCurrentState);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert - Only the first matching event should be updated (due to break statement)
        verify(mockEvent1, times(1)).editDueDate(LocalDate.parse(dueDate));
        verify(mockEvent2, never()).editDueDate(any()); // Should not be called due to break
        verify(mockEvent3, never()).editDueDate(any());
    }

    @Test
    void testPrepareSuccessViewWithInvalidDateFormat() {
        // Arrange
        String eventId = "event-with-invalid-date";
        String invalidDueDate = "2024-13-45"; // Invalid month and day
        EditTodaysEventOutputData outputData = new EditTodaysEventOutputData(eventId, invalidDueDate, false);

        // Mock event with matching ID
        when(mockEvent1.getInfo()).thenReturn(mockInfo1);
        when(mockInfo1.getId()).thenReturn(eventId);

        List<EventInterf> initialEvents = new ArrayList<>();
        initialEvents.add(mockEvent1);

        TodaysEventsState mockCurrentState = mock(TodaysEventsState.class);
        when(mockCurrentState.getTodaysEvents()).thenReturn(initialEvents);
        when(mockTodaysEventsViewModel.getState()).thenReturn(mockCurrentState);

        // Act & Assert - Should throw exception when trying to parse invalid date
        assertThrows(Exception.class, () -> {
            presenter.prepareSuccessView(outputData);
        });
    }
}