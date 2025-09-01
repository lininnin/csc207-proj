package interface_adapter.alex.event_related.add_event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeListener;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddedEventViewModelTest {

    private AddedEventViewModel viewModel;
    private PropertyChangeListener mockPropertyChangeListener;
    private AtomicInteger propertyChangeCount;

    @BeforeEach
    void setUp() {
        viewModel = new AddedEventViewModel();
        mockPropertyChangeListener = mock(PropertyChangeListener.class);
        propertyChangeCount = new AtomicInteger(0);

        // Add real listener to count property changes
        viewModel.addPropertyChangeListener(evt -> propertyChangeCount.incrementAndGet());
        viewModel.addPropertyChangeListener(mockPropertyChangeListener);
    }

    @Test
    void testConstructor() {
        // Assert
        assertEquals("add event", viewModel.getViewName());
        assertNotNull(viewModel.getState());
        assertInstanceOf(AddedEventState.class, viewModel.getState());
    }

    @Test
    void testConstants() {
        assertEquals("addEventState", AddedEventViewModel.ADD_EVENT_STATE_PROPERTY);
        assertEquals("Add Today's Event", AddedEventViewModel.TITLE_LABEL);
        assertEquals("Select Event Name:", AddedEventViewModel.NAME_LABEL);
        assertEquals("Due Date (optional, yyyy-mm-dd):", AddedEventViewModel.DUE_DATE_LABEL);
        assertEquals("Add", AddedEventViewModel.ADD_BUTTON_LABEL);
    }

    @Test
    void testSetState() {
        // Arrange
        AddedEventState newState = new AddedEventState();
        newState.setSelectedName("Test Event");

        // Act
        viewModel.setState(newState);

        // Assert
        assertSame(newState, viewModel.getState());
        assertEquals("Test Event", viewModel.getState().getSelectedName());
    }

    @Test
    void testSetStateTriggersPropertyChange() {
        // Arrange
        AddedEventState newState = new AddedEventState();
        newState.setSelectedName("Property Change Test");
        int initialCount = propertyChangeCount.get();

        // Act
        viewModel.setState(newState);

        // Assert
        assertEquals(initialCount + 2, propertyChangeCount.get()); // setState fires 2 property changes
        verify(mockPropertyChangeListener, times(2)).propertyChange(any());
    }

    @Test
    void testSetStateWithNull() {
        // Act
        viewModel.setState(null);

        // Assert
        assertNull(viewModel.getState());
    }

    @Test
    void testMultipleSetStateCalls() {
        // Arrange
        AddedEventState state1 = new AddedEventState();
        state1.setSelectedName("Event 1");
        AddedEventState state2 = new AddedEventState();
        state2.setSelectedName("Event 2");
        int initialCount = propertyChangeCount.get();

        // Act
        viewModel.setState(state1);
        viewModel.setState(state2);

        // Assert
        assertEquals(initialCount + 4, propertyChangeCount.get()); // 2 setState calls × 2 property changes each
        assertSame(state2, viewModel.getState());
        assertEquals("Event 2", viewModel.getState().getSelectedName());
    }

    @Test
    void testFirePropertyChangedDirectly() {
        // Arrange
        int initialCount = propertyChangeCount.get();

        // Act
        viewModel.firePropertyChanged(AddedEventViewModel.ADD_EVENT_STATE_PROPERTY);

        // Assert
        assertEquals(initialCount + 1, propertyChangeCount.get());
    }

    @Test
    void testInitialStateProperties() {
        // Act - Get the initial state set in constructor
        AddedEventState initialState = viewModel.getState();

        // Assert
        assertEquals("", initialState.getSelectedName());
        assertNull(initialState.getDueDate());
        assertTrue(initialState.getAvailableNames().isEmpty());
        assertNull(initialState.getErrorMessage());
        assertNull(initialState.getSuccessMessage());
    }

    @Test
    void testComplexStateTransition() {
        // Arrange
        AddedEventState state = new AddedEventState();
        state.setSelectedName("Complex Event");
        state.setErrorMessage("Some error");
        state.setSuccessMessage("Success!");

        int initialCount = propertyChangeCount.get();

        // Act
        viewModel.setState(state);

        // Assert
        assertEquals(initialCount + 2, propertyChangeCount.get()); // setState fires 2 property changes
        assertEquals("Complex Event", viewModel.getState().getSelectedName());
        assertEquals("Some error", viewModel.getState().getErrorMessage());
        assertEquals("Success!", viewModel.getState().getSuccessMessage());
    }

    @Test
    void testSetSameStateReference() {
        // Arrange
        AddedEventState state = new AddedEventState();
        state.setSelectedName("Same State");
        viewModel.setState(state);
        int countAfterFirst = propertyChangeCount.get();

        // Act - Set the same state reference again
        viewModel.setState(state);

        // Assert
        assertEquals(countAfterFirst + 1, propertyChangeCount.get()); // Still fires property change
        assertSame(state, viewModel.getState());
    }
}