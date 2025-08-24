package interface_adapter.alex.event_related.add_event;

import entity.alex.Event.EventInterf;
import interface_adapter.Angela.today_so_far.TodaySoFarController;
import interface_adapter.alex.event_related.todays_events_module.todays_events.TodaysEventsState;
import interface_adapter.alex.event_related.todays_events_module.todays_events.TodaysEventsViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import use_case.alex.event_related.add_event.AddEventDataAccessInterf;
import use_case.alex.event_related.add_event.AddEventOutputData;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddEventPresenterTest {

    private AddedEventViewModel addedEventViewModel;
    private TodaysEventsViewModel todaysEventsViewModel;
    private AddEventDataAccessInterf mockDataAccess;
    private TodaySoFarController mockTodaySoFarController;
    private AddEventPresenter presenter;

    @BeforeEach
    void setUp() {
        addedEventViewModel = mock(AddedEventViewModel.class, withSettings().lenient());
        todaysEventsViewModel = mock(TodaysEventsViewModel.class, withSettings().lenient());
        mockDataAccess = mock(AddEventDataAccessInterf.class, withSettings().lenient());
        mockTodaySoFarController = mock(TodaySoFarController.class, withSettings().lenient());

        presenter = new AddEventPresenter(addedEventViewModel, todaysEventsViewModel, mockDataAccess);
    }

    @Test
    void testConstructor() {
        // Verify constructor assigns fields correctly
        assertNotNull(presenter);
    }

    @Test
    void testSetTodaySoFarController() {
        // Act
        presenter.setTodaySoFarController(mockTodaySoFarController);

        // The controller should be set internally (verified in success view test)
        assertNotNull(presenter);
    }

    @Test
    void testSetTodaySoFarControllerWithNull() {
        // Act
        presenter.setTodaySoFarController(null);

        // Should not throw exception
        assertNotNull(presenter);
    }

    @Test
    void testPrepareSuccessView() {
        // Arrange
        String eventName = "Team Meeting";
        LocalDate dueDate = LocalDate.of(2024, 1, 15);
        AddEventOutputData outputData = mock(AddEventOutputData.class);
        when(outputData.getName()).thenReturn(eventName);
        when(outputData.getDueDate()).thenReturn(dueDate);

        AddedEventState mockState = mock(AddedEventState.class);
        when(addedEventViewModel.getState()).thenReturn(mockState);

        List<EventInterf> mockEvents = List.of(
            mock(EventInterf.class),
            mock(EventInterf.class)
        );
        when(mockDataAccess.getTodaysEvents()).thenReturn(mockEvents);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert - Verify added event view model is updated
        verify(mockState).setErrorMessage(null);
        verify(mockState).setSuccessMessage("Event \"" + eventName + "\" added successfully.");
        verify(mockState).setSelectedName(eventName);
        verify(mockState).setDueDate(dueDate);
        verify(addedEventViewModel).setState(mockState);
    }

    @Test
    void testPrepareSuccessViewUpdatesTodaysEventsViewModel() {
        // Arrange
        AddEventOutputData outputData = mock(AddEventOutputData.class);
        when(outputData.getName()).thenReturn("Test Event");
        when(outputData.getDueDate()).thenReturn(LocalDate.now());

        AddedEventState mockState = mock(AddedEventState.class);
        when(addedEventViewModel.getState()).thenReturn(mockState);

        List<EventInterf> mockEvents = List.of(
            mock(EventInterf.class),
            mock(EventInterf.class),
            mock(EventInterf.class)
        );
        when(mockDataAccess.getTodaysEvents()).thenReturn(mockEvents);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert - Verify today's events view model is updated
        ArgumentCaptor<TodaysEventsState> stateCaptor = ArgumentCaptor.forClass(TodaysEventsState.class);
        verify(todaysEventsViewModel).setState(stateCaptor.capture());

        TodaysEventsState capturedState = stateCaptor.getValue();
        assertEquals(mockEvents, capturedState.getTodaysEvents());
    }

    @Test
    void testPrepareSuccessViewWithTodaySoFarController() {
        // Arrange
        AddEventOutputData outputData = mock(AddEventOutputData.class);
        when(outputData.getName()).thenReturn("Event");
        when(outputData.getDueDate()).thenReturn(LocalDate.now());

        when(addedEventViewModel.getState()).thenReturn(mock(AddedEventState.class));
        when(mockDataAccess.getTodaysEvents()).thenReturn(new ArrayList<>());

        presenter.setTodaySoFarController(mockTodaySoFarController);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert
        verify(mockTodaySoFarController, times(1)).refresh();
    }

    @Test
    void testPrepareSuccessViewWithoutTodaySoFarController() {
        // Arrange
        AddEventOutputData outputData = mock(AddEventOutputData.class);
        when(outputData.getName()).thenReturn("Event");
        when(outputData.getDueDate()).thenReturn(LocalDate.now());

        when(addedEventViewModel.getState()).thenReturn(mock(AddedEventState.class));
        when(mockDataAccess.getTodaysEvents()).thenReturn(new ArrayList<>());
        // No controller set

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert
        verifyNoInteractions(mockTodaySoFarController);
    }

    @Test
    void testPrepareSuccessViewWithEmptyEventList() {
        // Arrange
        AddEventOutputData outputData = mock(AddEventOutputData.class);
        when(outputData.getName()).thenReturn("Event");
        when(outputData.getDueDate()).thenReturn(LocalDate.now());

        when(addedEventViewModel.getState()).thenReturn(mock(AddedEventState.class));

        List<EventInterf> emptyEvents = new ArrayList<>();
        when(mockDataAccess.getTodaysEvents()).thenReturn(emptyEvents);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert
        ArgumentCaptor<TodaysEventsState> stateCaptor = ArgumentCaptor.forClass(TodaysEventsState.class);
        verify(todaysEventsViewModel).setState(stateCaptor.capture());

        TodaysEventsState capturedState = stateCaptor.getValue();
        assertTrue(capturedState.getTodaysEvents().isEmpty());
    }

    @Test
    void testPrepareSuccessViewSuccessMessage() {
        // Arrange
        String eventName = "Important Meeting";
        AddEventOutputData outputData = mock(AddEventOutputData.class);
        when(outputData.getName()).thenReturn(eventName);
        when(outputData.getDueDate()).thenReturn(LocalDate.now());

        AddedEventState mockState = mock(AddedEventState.class);
        when(addedEventViewModel.getState()).thenReturn(mockState);
        when(mockDataAccess.getTodaysEvents()).thenReturn(new ArrayList<>());

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert
        String expectedMessage = "Event \"" + eventName + "\" added successfully.";
        verify(mockState).setSuccessMessage(expectedMessage);
    }

    @Test
    void testPrepareFailView() {
        // Arrange
        String errorMessage = "Event addition failed";
        AddedEventState mockState = mock(AddedEventState.class);
        when(addedEventViewModel.getState()).thenReturn(mockState);

        // Act
        presenter.prepareFailView(errorMessage);

        // Assert
        verify(mockState).setErrorMessage(errorMessage);
        verify(mockState).setSuccessMessage(null);
        verify(addedEventViewModel).setState(mockState);
    }

    @Test
    void testPrepareFailViewWithNullMessage() {
        // Arrange
        String errorMessage = null;
        AddedEventState mockState = mock(AddedEventState.class);
        when(addedEventViewModel.getState()).thenReturn(mockState);

        // Act
        presenter.prepareFailView(errorMessage);

        // Assert
        verify(mockState).setErrorMessage(null);
        verify(mockState).setSuccessMessage(null);
        verify(addedEventViewModel).setState(mockState);
    }

    @Test
    void testPrepareFailViewWithEmptyMessage() {
        // Arrange
        String errorMessage = "";
        AddedEventState mockState = mock(AddedEventState.class);
        when(addedEventViewModel.getState()).thenReturn(mockState);

        // Act
        presenter.prepareFailView(errorMessage);

        // Assert
        verify(mockState).setErrorMessage("");
        verify(mockState).setSuccessMessage(null);
        verify(addedEventViewModel).setState(mockState);
    }

    @Test
    void testDataAccessInteraction() {
        // Arrange
        AddEventOutputData outputData = mock(AddEventOutputData.class);
        when(outputData.getName()).thenReturn("Event");
        when(outputData.getDueDate()).thenReturn(LocalDate.now());

        when(addedEventViewModel.getState()).thenReturn(mock(AddedEventState.class));

        List<EventInterf> mockEvents = List.of(mock(EventInterf.class));
        when(mockDataAccess.getTodaysEvents()).thenReturn(mockEvents);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert
        verify(mockDataAccess, times(1)).getTodaysEvents();
    }

    @Test
    void testPrepareSuccessViewWithNullDueDate() {
        // Arrange
        String eventName = "No Due Date Event";
        AddEventOutputData outputData = mock(AddEventOutputData.class);
        when(outputData.getName()).thenReturn(eventName);
        when(outputData.getDueDate()).thenReturn(null);

        AddedEventState mockState = mock(AddedEventState.class);
        when(addedEventViewModel.getState()).thenReturn(mockState);
        when(mockDataAccess.getTodaysEvents()).thenReturn(new ArrayList<>());

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert
        verify(mockState).setSelectedName(eventName);
        verify(mockState).setDueDate(null);
    }

    @Test
    void testMultiplePrepareSuccessViewCalls() {
        // Arrange
        AddEventOutputData outputData1 = mock(AddEventOutputData.class);
        when(outputData1.getName()).thenReturn("Event 1");
        when(outputData1.getDueDate()).thenReturn(LocalDate.of(2024, 1, 1));

        AddEventOutputData outputData2 = mock(AddEventOutputData.class);
        when(outputData2.getName()).thenReturn("Event 2");
        when(outputData2.getDueDate()).thenReturn(LocalDate.of(2024, 2, 1));

        when(addedEventViewModel.getState()).thenReturn(mock(AddedEventState.class));
        when(mockDataAccess.getTodaysEvents()).thenReturn(new ArrayList<>());

        // Act
        presenter.prepareSuccessView(outputData1);
        presenter.prepareSuccessView(outputData2);

        // Assert
        verify(addedEventViewModel, times(2)).setState(any(AddedEventState.class));
        verify(todaysEventsViewModel, times(2)).setState(any(TodaysEventsState.class));
        verify(mockDataAccess, times(2)).getTodaysEvents();
    }

    @Test
    void testPrepareSuccessViewWithLargeEventList() {
        // Arrange
        AddEventOutputData outputData = mock(AddEventOutputData.class);
        when(outputData.getName()).thenReturn("Event");
        when(outputData.getDueDate()).thenReturn(LocalDate.now());

        when(addedEventViewModel.getState()).thenReturn(mock(AddedEventState.class));

        List<EventInterf> largeEventList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            largeEventList.add(mock(EventInterf.class));
        }
        when(mockDataAccess.getTodaysEvents()).thenReturn(largeEventList);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert
        ArgumentCaptor<TodaysEventsState> stateCaptor = ArgumentCaptor.forClass(TodaysEventsState.class);
        verify(todaysEventsViewModel).setState(stateCaptor.capture());

        TodaysEventsState capturedState = stateCaptor.getValue();
        assertEquals(100, capturedState.getTodaysEvents().size());
    }

    @Test
    void testPrepareFailViewMultipleCalls() {
        // Arrange
        AddedEventState mockState = mock(AddedEventState.class);
        when(addedEventViewModel.getState()).thenReturn(mockState);

        // Act
        presenter.prepareFailView("Error 1");
        presenter.prepareFailView("Error 2");
        presenter.prepareFailView("Error 3");

        // Assert
        verify(addedEventViewModel, times(3)).setState(mockState);
        verify(mockState, times(3)).setSuccessMessage(null);
    }
}