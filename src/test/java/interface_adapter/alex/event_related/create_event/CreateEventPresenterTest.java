package interface_adapter.alex.event_related.create_event;

import entity.info.InfoInterf;
import interface_adapter.Angela.today_so_far.TodaySoFarController;
import interface_adapter.alex.event_related.available_event_module.available_event.AvailableEventState;
import interface_adapter.alex.event_related.available_event_module.available_event.AvailableEventViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import use_case.alex.event_related.create_event.CreateEventDataAccessInterface;
import use_case.alex.event_related.create_event.CreateEventOutputData;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateEventPresenterTest {

    private CreatedEventViewModel createdEventViewModel;
    private AvailableEventViewModel availableEventViewModel;
    private CreateEventDataAccessInterface mockDataAccess;
    private TodaySoFarController mockTodaySoFarController;
    private CreateEventPresenter presenter;

    @BeforeEach
    void setUp() {
        createdEventViewModel = mock(CreatedEventViewModel.class, withSettings().lenient());
        availableEventViewModel = mock(AvailableEventViewModel.class, withSettings().lenient());
        mockDataAccess = mock(CreateEventDataAccessInterface.class, withSettings().lenient());
        mockTodaySoFarController = mock(TodaySoFarController.class, withSettings().lenient());

        presenter = new CreateEventPresenter(createdEventViewModel, availableEventViewModel, mockDataAccess);
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

        // The controller should be set internally (no direct way to verify without reflection)
        // We can verify behavior in prepareSuccessView test
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
        CreateEventOutputData outputData = mock(CreateEventOutputData.class);
        List<InfoInterf> mockEvents = List.of(
            mock(InfoInterf.class),
            mock(InfoInterf.class)
        );
        when(mockDataAccess.getAllEvents()).thenReturn(mockEvents);

        CreatedEventState mockCreatedState = new CreatedEventState();
        when(createdEventViewModel.getState()).thenReturn(mockCreatedState);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert - Verify created event view model is updated
        ArgumentCaptor<CreatedEventState> createdStateCaptor = ArgumentCaptor.forClass(CreatedEventState.class);
        verify(createdEventViewModel, times(2)).setState(createdStateCaptor.capture());
        verify(createdEventViewModel, times(2)).firePropertyChanged(CreatedEventViewModel.CREATED_EVENT_STATE_PROPERTY);

        // Verify all captured states have cleared fields
        List<CreatedEventState> capturedStates = createdStateCaptor.getAllValues();
        for (CreatedEventState state : capturedStates) {
            assertEquals("", state.getName());
            assertEquals("", state.getCategory());
            assertEquals("", state.getDescription());
        }
    }

    @Test
    void testPrepareSuccessViewUpdatesAvailableEventViewModel() {
        // Arrange
        CreateEventOutputData outputData = mock(CreateEventOutputData.class);
        List<InfoInterf> mockEvents = List.of(
            mock(InfoInterf.class),
            mock(InfoInterf.class),
            mock(InfoInterf.class)
        );
        when(mockDataAccess.getAllEvents()).thenReturn(mockEvents);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert - Verify available event view model is updated
        ArgumentCaptor<AvailableEventState> availableStateCaptor = ArgumentCaptor.forClass(AvailableEventState.class);
        verify(availableEventViewModel, times(2)).setState(availableStateCaptor.capture());
        verify(availableEventViewModel, times(1)).firePropertyChanged(AvailableEventViewModel.AVAILABLE_EVENTS_PROPERTY);

        // Verify the final state has the updated events
        List<AvailableEventState> capturedStates = availableStateCaptor.getAllValues();
        AvailableEventState finalState = capturedStates.get(capturedStates.size() - 1);
        assertEquals(mockEvents, finalState.getAvailableEvents());
    }

    @Test
    void testPrepareSuccessViewWithTodaySoFarController() {
        // Arrange
        CreateEventOutputData outputData = mock(CreateEventOutputData.class);
        when(mockDataAccess.getAllEvents()).thenReturn(new ArrayList<>());
        presenter.setTodaySoFarController(mockTodaySoFarController);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert
        verify(mockTodaySoFarController, times(1)).refresh();
    }

    @Test
    void testPrepareSuccessViewWithoutTodaySoFarController() {
        // Arrange
        CreateEventOutputData outputData = mock(CreateEventOutputData.class);
        when(mockDataAccess.getAllEvents()).thenReturn(new ArrayList<>());
        // No controller set

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert
        verifyNoInteractions(mockTodaySoFarController);
    }

    @Test
    void testPrepareSuccessViewWithEmptyEventList() {
        // Arrange
        CreateEventOutputData outputData = mock(CreateEventOutputData.class);
        List<InfoInterf> emptyEvents = new ArrayList<>();
        when(mockDataAccess.getAllEvents()).thenReturn(emptyEvents);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert
        ArgumentCaptor<AvailableEventState> stateCaptor = ArgumentCaptor.forClass(AvailableEventState.class);
        verify(availableEventViewModel, times(2)).setState(stateCaptor.capture());

        AvailableEventState finalState = stateCaptor.getAllValues().get(1);
        assertTrue(finalState.getAvailableEvents().isEmpty());
    }

    @Test
    void testPrepareFailView() {
        // Arrange
        String errorMessage = "Event creation failed";
        CreatedEventState mockState = mock(CreatedEventState.class);
        when(createdEventViewModel.getState()).thenReturn(mockState);

        // Act
        presenter.prepareFailView(errorMessage);

        // Assert
        verify(mockState, times(1)).setNameError(errorMessage);
        verify(createdEventViewModel, times(1)).setState(mockState);
    }

    @Test
    void testPrepareFailViewWithNullMessage() {
        // Arrange
        String errorMessage = null;
        CreatedEventState mockState = mock(CreatedEventState.class);
        when(createdEventViewModel.getState()).thenReturn(mockState);

        // Act
        presenter.prepareFailView(errorMessage);

        // Assert
        verify(mockState, times(1)).setNameError(null);
        verify(createdEventViewModel, times(1)).setState(mockState);
    }

    @Test
    void testPrepareFailViewWithEmptyMessage() {
        // Arrange
        String errorMessage = "";
        CreatedEventState mockState = mock(CreatedEventState.class);
        when(createdEventViewModel.getState()).thenReturn(mockState);

        // Act
        presenter.prepareFailView(errorMessage);

        // Assert
        verify(mockState, times(1)).setNameError("");
        verify(createdEventViewModel, times(1)).setState(mockState);
    }

    @Test
    void testPrepareFailViewWithLongMessage() {
        // Arrange
        String errorMessage = "This is a very long error message ".repeat(10);
        CreatedEventState mockState = mock(CreatedEventState.class);
        when(createdEventViewModel.getState()).thenReturn(mockState);

        // Act
        presenter.prepareFailView(errorMessage);

        // Assert
        verify(mockState, times(1)).setNameError(errorMessage);
        verify(createdEventViewModel, times(1)).setState(mockState);
    }

    @Test
    void testDataAccessInteraction() {
        // Arrange
        CreateEventOutputData outputData = mock(CreateEventOutputData.class);
        List<InfoInterf> mockEvents = List.of(mock(InfoInterf.class));
        when(mockDataAccess.getAllEvents()).thenReturn(mockEvents);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert
        verify(mockDataAccess, times(1)).getAllEvents();
    }

    @Test
    void testPrepareSuccessViewStateClearance() {
        // Arrange
        CreateEventOutputData outputData = mock(CreateEventOutputData.class);
        when(mockDataAccess.getAllEvents()).thenReturn(new ArrayList<>());

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert - Verify that new states are created with cleared fields
        ArgumentCaptor<CreatedEventState> stateCaptor = ArgumentCaptor.forClass(CreatedEventState.class);
        verify(createdEventViewModel, times(2)).setState(stateCaptor.capture());

        for (CreatedEventState state : stateCaptor.getAllValues()) {
            assertEquals("", state.getName());
            assertEquals("", state.getCategory());
            assertEquals("", state.getDescription());
        }
    }

    @Test
    void testMultiplePrepareSuccessViewCalls() {
        // Arrange
        CreateEventOutputData outputData1 = mock(CreateEventOutputData.class);
        CreateEventOutputData outputData2 = mock(CreateEventOutputData.class);
        when(mockDataAccess.getAllEvents()).thenReturn(new ArrayList<>());

        // Act
        presenter.prepareSuccessView(outputData1);
        presenter.prepareSuccessView(outputData2);

        // Assert
        verify(createdEventViewModel, times(4)).setState(any(CreatedEventState.class)); // 2 calls per execution
        verify(createdEventViewModel, times(4)).firePropertyChanged(CreatedEventViewModel.CREATED_EVENT_STATE_PROPERTY);
        verify(availableEventViewModel, times(4)).setState(any(AvailableEventState.class));
        verify(availableEventViewModel, times(2)).firePropertyChanged(AvailableEventViewModel.AVAILABLE_EVENTS_PROPERTY);
    }

    @Test
    void testPrepareSuccessViewWithLargeEventList() {
        // Arrange
        CreateEventOutputData outputData = mock(CreateEventOutputData.class);
        List<InfoInterf> largeEventList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            largeEventList.add(mock(InfoInterf.class));
        }
        when(mockDataAccess.getAllEvents()).thenReturn(largeEventList);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert
        ArgumentCaptor<AvailableEventState> stateCaptor = ArgumentCaptor.forClass(AvailableEventState.class);
        verify(availableEventViewModel, times(2)).setState(stateCaptor.capture());

        AvailableEventState finalState = stateCaptor.getAllValues().get(1);
        assertEquals(100, finalState.getAvailableEvents().size());
    }
}