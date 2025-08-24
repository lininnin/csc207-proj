package interface_adapter.alex.WellnessLog_related.todays_wellnesslog_module.todays_wellness_log;

import entity.alex.WellnessLogEntry.WellnessLogEntryInterf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Comprehensive test suite for TodaysWellnessLogViewModel.
 * Tests view model responsibilities in Clean Architecture.
 */
class TodaysWellnessLogViewModelTest {

    private TodaysWellnessLogViewModel viewModel;
    private PropertyChangeListener mockListener;

    @BeforeEach
    void setUp() {
        viewModel = new TodaysWellnessLogViewModel();
        mockListener = mock(PropertyChangeListener.class);
        viewModel.addPropertyChangeListener(mockListener);
    }

    @Test
    @DisplayName("Should initialize with correct view name")
    void testInitialization() {
        assertEquals("TodaysWellnessLogView", viewModel.getViewName());
        assertNotNull(viewModel.getState());
        assertTrue(viewModel.getState() instanceof TodaysWellnessLogState);
    }

    @Test
    @DisplayName("Should initialize with empty wellness log state")
    void testInitialStateIsEmpty() {
        TodaysWellnessLogState state = viewModel.getState();
        
        assertTrue(state.isEmpty());
        assertTrue(state.getEntries().isEmpty());
    }

    @Test
    @DisplayName("Should fire property change when setState is called")
    void testSetStateFiresPropertyChange() {
        // Given
        TodaysWellnessLogState newState = new TodaysWellnessLogState();
        WellnessLogEntryInterf mockEntry = mock(WellnessLogEntryInterf.class);
        newState.addEntry(mockEntry);

        // When
        viewModel.setState(newState);

        // Then
        verify(mockListener).propertyChange(any(PropertyChangeEvent.class));
        assertEquals(newState, viewModel.getState());
    }

    @Test
    @DisplayName("Should preserve state data when setting new state")
    void testSetStatePreservesData() {
        // Given
        TodaysWellnessLogState newState = new TodaysWellnessLogState();
        WellnessLogEntryInterf mockEntry1 = mock(WellnessLogEntryInterf.class);
        WellnessLogEntryInterf mockEntry2 = mock(WellnessLogEntryInterf.class);
        newState.addEntry(mockEntry1);
        newState.addEntry(mockEntry2);

        // When
        viewModel.setState(newState);

        // Then
        TodaysWellnessLogState retrievedState = viewModel.getState();
        assertEquals(2, retrievedState.getEntries().size());
        assertFalse(retrievedState.isEmpty());
    }

    @Test
    @DisplayName("Should fire state changed with correct property name")
    void testFireStateChangedUsesCorrectProperty() {
        // When
        viewModel.fireStateChanged();

        // Then
        verify(mockListener).propertyChange(argThat(event -> 
            TodaysWellnessLogViewModel.TODAYS_WELLNESS_LOG_PROPERTY.equals(event.getPropertyName())
        ));
    }

    @Test
    @DisplayName("Should fire state changed with current state as new value")
    void testFireStateChangedIncludesCurrentState() {
        // Given
        TodaysWellnessLogState currentState = viewModel.getState();

        // When
        viewModel.fireStateChanged();

        // Then
        verify(mockListener).propertyChange(argThat(event -> 
            event.getNewValue() == currentState
        ));
    }

    @Test
    @DisplayName("Should support multiple property change listeners")
    void testMultiplePropertyChangeListeners() {
        // Given
        PropertyChangeListener mockListener2 = mock(PropertyChangeListener.class);
        viewModel.addPropertyChangeListener(mockListener2);

        // When
        viewModel.fireStateChanged();

        // Then
        verify(mockListener).propertyChange(any(PropertyChangeEvent.class));
        verify(mockListener2).propertyChange(any(PropertyChangeEvent.class));
    }

    @Test
    @DisplayName("Should remove property change listeners correctly")
    void testRemovePropertyChangeListener() {
        // Given
        viewModel.removePropertyChangeListener(mockListener);

        // When
        viewModel.fireStateChanged();

        // Then
        verify(mockListener, never()).propertyChange(any(PropertyChangeEvent.class));
    }

    @Test
    @DisplayName("Should handle null state gracefully")
    void testSetNullState() {
        // When
        viewModel.setState(null);

        // Then
        assertNull(viewModel.getState());
        verify(mockListener).propertyChange(any(PropertyChangeEvent.class));
    }

    @Test
    @DisplayName("Should maintain property constant value")
    void testPropertyConstant() {
        assertEquals("todaysWellnessLog", TodaysWellnessLogViewModel.TODAYS_WELLNESS_LOG_PROPERTY);
    }

    @Test
    @DisplayName("Should fire property change when firePropertyChanged is called")
    void testFirePropertyChangedMethod() {
        // When
        viewModel.firePropertyChanged();

        // Then
        verify(mockListener).propertyChange(argThat(event -> 
            "state".equals(event.getPropertyName())
        ));
    }

    @Test
    @DisplayName("Should fire property change with custom property name")
    void testFirePropertyChangedWithCustomName() {
        // Given
        String customProperty = "customProperty";

        // When
        viewModel.firePropertyChanged(customProperty);

        // Then
        verify(mockListener).propertyChange(argThat(event -> 
            customProperty.equals(event.getPropertyName())
        ));
    }

    @Test
    @DisplayName("Should handle state changes without listeners gracefully")
    void testStateChangeWithoutListeners() {
        // Given
        TodaysWellnessLogViewModel viewModelNoListeners = new TodaysWellnessLogViewModel();
        TodaysWellnessLogState newState = new TodaysWellnessLogState();

        // When & Then - should not throw exception
        assertDoesNotThrow(() -> {
            viewModelNoListeners.setState(newState);
            viewModelNoListeners.fireStateChanged();
        });
    }

    @Test
    @DisplayName("Should maintain reference integrity with state")
    void testStateReferenceIntegrity() {
        // Given
        TodaysWellnessLogState state1 = new TodaysWellnessLogState();
        TodaysWellnessLogState state2 = new TodaysWellnessLogState();

        // When
        viewModel.setState(state1);
        TodaysWellnessLogState retrieved1 = viewModel.getState();
        
        viewModel.setState(state2);
        TodaysWellnessLogState retrieved2 = viewModel.getState();

        // Then
        assertSame(state1, retrieved1);
        assertSame(state2, retrieved2);
        assertNotSame(retrieved1, retrieved2);
    }
}