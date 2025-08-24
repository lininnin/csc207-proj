package interface_adapter.Angela.today_so_far;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TodaySoFarViewModelTest {

    private TodaySoFarViewModel viewModel;
    private PropertyChangeListener mockPropertyChangeListener;
    private AtomicInteger propertyChangeCount;

    @BeforeEach
    void setUp() {
        viewModel = new TodaySoFarViewModel();
        mockPropertyChangeListener = mock(PropertyChangeListener.class);
        propertyChangeCount = new AtomicInteger(0);

        // Add real listener to count property changes
        viewModel.addPropertyChangeListener(evt -> propertyChangeCount.incrementAndGet());
        viewModel.addPropertyChangeListener(mockPropertyChangeListener);
    }

    @Test
    void testConstructor() {
        // Assert
        assertEquals("today_so_far", viewModel.getViewName());
        assertNotNull(viewModel.getState());
        assertInstanceOf(TodaySoFarState.class, viewModel.getState());
    }

    @Test
    void testConstants() {
        assertEquals("todaySoFarState", TodaySoFarViewModel.TODAY_SO_FAR_STATE_PROPERTY);
    }

    @Test
    void testInitialState() {
        // Act
        TodaySoFarState initialState = viewModel.getState();

        // Assert
        assertNotNull(initialState);
        assertNotNull(initialState.getGoals());
        assertNotNull(initialState.getCompletedItems());
        assertNotNull(initialState.getWellnessEntries());
        assertTrue(initialState.getGoals().isEmpty());
        assertTrue(initialState.getCompletedItems().isEmpty());
        assertTrue(initialState.getWellnessEntries().isEmpty());
        assertEquals(0, initialState.getCompletionRate());
        assertNull(initialState.getError());
    }

    @Test
    void testSetState() {
        // Arrange
        TodaySoFarState newState = new TodaySoFarState();
        newState.setCompletionRate(75);
        newState.setError("Test error");

        // Act
        viewModel.setState(newState);

        // Assert
        assertSame(newState, viewModel.getState());
        assertEquals(75, viewModel.getState().getCompletionRate());
        assertEquals("Test error", viewModel.getState().getError());
    }

    @Test
    void testSetStateWithNull() {
        // Act
        viewModel.setState(null);

        // Assert
        assertNull(viewModel.getState());
    }

    @Test
    void testFirePropertyChanged() {
        // Arrange
        int initialCount = propertyChangeCount.get();

        // Act
        viewModel.firePropertyChanged();

        // Assert
        assertEquals(initialCount + 1, propertyChangeCount.get());
        verify(mockPropertyChangeListener, times(1)).propertyChange(any(PropertyChangeEvent.class));
    }

    @Test
    void testFirePropertyChangedWithCurrentState() {
        // Arrange
        TodaySoFarState currentState = viewModel.getState();
        PropertyChangeListener detailListener = mock(PropertyChangeListener.class);
        viewModel.addPropertyChangeListener(detailListener);

        // Act
        viewModel.firePropertyChanged();

        // Assert
        ArgumentCaptor<PropertyChangeEvent> eventCaptor = ArgumentCaptor.forClass(PropertyChangeEvent.class);
        verify(detailListener).propertyChange(eventCaptor.capture());

        PropertyChangeEvent event = eventCaptor.getValue();
        assertEquals(TodaySoFarViewModel.TODAY_SO_FAR_STATE_PROPERTY, event.getPropertyName());
        assertNull(event.getOldValue());
        assertSame(currentState, event.getNewValue());
        assertSame(viewModel, event.getSource());
    }

    @Test
    void testFirePropertyChangedAfterStateChange() {
        // Arrange
        TodaySoFarState newState = new TodaySoFarState();
        newState.setCompletionRate(90);
        viewModel.setState(newState);
        PropertyChangeListener detailListener = mock(PropertyChangeListener.class);
        viewModel.addPropertyChangeListener(detailListener);

        // Act
        viewModel.firePropertyChanged();

        // Assert
        ArgumentCaptor<PropertyChangeEvent> eventCaptor = ArgumentCaptor.forClass(PropertyChangeEvent.class);
        verify(detailListener).propertyChange(eventCaptor.capture());

        PropertyChangeEvent event = eventCaptor.getValue();
        assertEquals(TodaySoFarViewModel.TODAY_SO_FAR_STATE_PROPERTY, event.getPropertyName());
        assertSame(newState, event.getNewValue());
    }

    @Test
    void testAddPropertyChangeListener() {
        // Arrange
        PropertyChangeListener additionalListener = mock(PropertyChangeListener.class);

        // Act
        viewModel.addPropertyChangeListener(additionalListener);
        viewModel.firePropertyChanged();

        // Assert
        verify(mockPropertyChangeListener, times(1)).propertyChange(any(PropertyChangeEvent.class));
        verify(additionalListener, times(1)).propertyChange(any(PropertyChangeEvent.class));
    }

    @Test
    void testMultiplePropertyChangeListeners() {
        // Arrange
        PropertyChangeListener listener1 = mock(PropertyChangeListener.class);
        PropertyChangeListener listener2 = mock(PropertyChangeListener.class);
        PropertyChangeListener listener3 = mock(PropertyChangeListener.class);

        viewModel.addPropertyChangeListener(listener1);
        viewModel.addPropertyChangeListener(listener2);
        viewModel.addPropertyChangeListener(listener3);

        // Act
        viewModel.firePropertyChanged();

        // Assert
        verify(listener1, times(1)).propertyChange(any(PropertyChangeEvent.class));
        verify(listener2, times(1)).propertyChange(any(PropertyChangeEvent.class));
        verify(listener3, times(1)).propertyChange(any(PropertyChangeEvent.class));
    }

    @Test
    void testMultipleFirePropertyChangedCalls() {
        // Arrange
        int initialCount = propertyChangeCount.get();

        // Act
        viewModel.firePropertyChanged();
        viewModel.firePropertyChanged();
        viewModel.firePropertyChanged();

        // Assert
        assertEquals(initialCount + 3, propertyChangeCount.get());
        verify(mockPropertyChangeListener, times(3)).propertyChange(any(PropertyChangeEvent.class));
    }

    @Test
    void testStateTransitions() {
        // Arrange
        TodaySoFarState state1 = new TodaySoFarState();
        state1.setCompletionRate(25);

        TodaySoFarState state2 = new TodaySoFarState();
        state2.setCompletionRate(50);

        TodaySoFarState state3 = new TodaySoFarState();
        state3.setCompletionRate(100);

        // Act
        viewModel.setState(state1);
        assertEquals(25, viewModel.getState().getCompletionRate());

        viewModel.setState(state2);
        assertEquals(50, viewModel.getState().getCompletionRate());

        viewModel.setState(state3);
        assertEquals(100, viewModel.getState().getCompletionRate());

        // Assert - Final state
        assertSame(state3, viewModel.getState());
    }

    @Test
    void testSetSameStateReference() {
        // Arrange
        TodaySoFarState state = new TodaySoFarState();
        state.setCompletionRate(60);

        // Act
        viewModel.setState(state);
        TodaySoFarState retrievedState1 = viewModel.getState();
        
        viewModel.setState(state); // Set same reference again
        TodaySoFarState retrievedState2 = viewModel.getState();

        // Assert
        assertSame(state, retrievedState1);
        assertSame(state, retrievedState2);
        assertSame(retrievedState1, retrievedState2);
    }

    @Test
    void testFirePropertyChangedWithNullState() {
        // Arrange
        viewModel.setState(null);
        PropertyChangeListener detailListener = mock(PropertyChangeListener.class);
        viewModel.addPropertyChangeListener(detailListener);

        // Act
        viewModel.firePropertyChanged();

        // Assert
        ArgumentCaptor<PropertyChangeEvent> eventCaptor = ArgumentCaptor.forClass(PropertyChangeEvent.class);
        verify(detailListener).propertyChange(eventCaptor.capture());

        PropertyChangeEvent event = eventCaptor.getValue();
        assertEquals(TodaySoFarViewModel.TODAY_SO_FAR_STATE_PROPERTY, event.getPropertyName());
        assertNull(event.getOldValue());
        assertNull(event.getNewValue());
        assertSame(viewModel, event.getSource());
    }

    @Test
    void testComplexStateWithFirePropertyChanged() {
        // Arrange
        TodaySoFarState complexState = new TodaySoFarState();
        complexState.setCompletionRate(85);
        complexState.setError("Complex test error");

        int initialCount = propertyChangeCount.get();

        // Act
        viewModel.setState(complexState);
        viewModel.firePropertyChanged();

        // Assert
        assertEquals(initialCount + 1, propertyChangeCount.get());
        assertSame(complexState, viewModel.getState());
        assertEquals(85, viewModel.getState().getCompletionRate());
        assertEquals("Complex test error", viewModel.getState().getError());
    }

    @Test
    void testViewModelInheritance() {
        // Assert that it properly extends ViewModel
        assertTrue(viewModel instanceof interface_adapter.ViewModel);
        assertEquals("today_so_far", viewModel.getViewName());
    }
}