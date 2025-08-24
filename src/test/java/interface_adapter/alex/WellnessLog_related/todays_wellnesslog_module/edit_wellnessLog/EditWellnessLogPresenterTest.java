package interface_adapter.alex.WellnessLog_related.todays_wellnesslog_module.edit_wellnessLog;

import entity.alex.WellnessLogEntry.WellnessLogEntryInterf;
import interface_adapter.alex.WellnessLog_related.todays_wellnesslog_module.todays_wellness_log.TodaysWellnessLogState;
import interface_adapter.alex.WellnessLog_related.todays_wellnesslog_module.todays_wellness_log.TodaysWellnessLogViewModel;
import interface_adapter.Angela.today_so_far.TodaySoFarController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import use_case.alex.wellness_log_related.todays_wellness_log_module.edit_wellnesslog.EditWellnessLogDataAccessInterf;
import use_case.alex.wellness_log_related.todays_wellness_log_module.edit_wellnesslog.EditWellnessLogOutputData;

import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EditWellnessLogPresenterTest {

    @Mock
    private EditWellnessLogDataAccessInterf mockDataAccess;
    @Mock
    private TodaySoFarController mockTodaySoFarController;
    @Mock
    private WellnessLogEntryInterf mockEntry1;
    @Mock
    private WellnessLogEntryInterf mockEntry2;

    private EditWellnessLogViewModel editViewModel;
    private TodaysWellnessLogViewModel todaysViewModel;
    private EditWellnessLogPresenter presenter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        editViewModel = new EditWellnessLogViewModel();
        todaysViewModel = new TodaysWellnessLogViewModel();
        presenter = new EditWellnessLogPresenter(editViewModel, todaysViewModel, mockDataAccess);
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
        EditWellnessLogOutputData outputData = new EditWellnessLogOutputData("log-123", true);
        List<WellnessLogEntryInterf> mockEntries = Arrays.asList(mockEntry1, mockEntry2);
        when(mockDataAccess.getTodaysWellnessLogEntries()).thenReturn(mockEntries);
        
        presenter.prepareSuccessView(outputData);
        
        verify(mockTodaySoFarController, times(1)).refresh();
    }

    @Test
    void testPrepareSuccessViewUpdatesEditViewModel() {
        // Arrange
        EditWellnessLogOutputData outputData = new EditWellnessLogOutputData("log-456", true);
        List<WellnessLogEntryInterf> mockEntries = Arrays.asList(mockEntry1);
        when(mockDataAccess.getTodaysWellnessLogEntries()).thenReturn(mockEntries);
        
        PropertyChangeListener editListener = mock(PropertyChangeListener.class);
        editViewModel.addPropertyChangeListener(editListener);
        
        // Act
        presenter.prepareSuccessView(outputData);
        
        // Assert
        EditWellnessLogState editState = editViewModel.getState();
        assertEquals("log-456", editState.getLogId());
        assertEquals("", editState.getErrorMessage());
        verify(editListener, atLeastOnce()).propertyChange(any());
    }

    @Test
    void testPrepareSuccessViewUpdatesTodaysViewModel() {
        // Arrange
        EditWellnessLogOutputData outputData = new EditWellnessLogOutputData("log-789", false);
        List<WellnessLogEntryInterf> mockEntries = Arrays.asList(mockEntry1, mockEntry2);
        when(mockDataAccess.getTodaysWellnessLogEntries()).thenReturn(mockEntries);
        
        PropertyChangeListener todaysListener = mock(PropertyChangeListener.class);
        todaysViewModel.addPropertyChangeListener(todaysListener);
        
        // Act
        presenter.prepareSuccessView(outputData);
        
        // Assert
        TodaysWellnessLogState todaysState = todaysViewModel.getState();
        assertEquals(mockEntries, todaysState.getEntries());
        verify(todaysListener, atLeastOnce()).propertyChange(any());
        verify(mockDataAccess, times(1)).getTodaysWellnessLogEntries();
    }

    @Test
    void testPrepareSuccessViewWithoutTodaySoFarController() {
        // Arrange
        EditWellnessLogOutputData outputData = new EditWellnessLogOutputData("log-001", true);
        List<WellnessLogEntryInterf> mockEntries = Arrays.asList(mockEntry1);
        when(mockDataAccess.getTodaysWellnessLogEntries()).thenReturn(mockEntries);
        
        // Note: not setting todaySoFarController (it should remain null)
        
        // Act
        presenter.prepareSuccessView(outputData);
        
        // Assert
        // Should not throw exception and should still update ViewModels
        assertEquals("log-001", editViewModel.getState().getLogId());
        assertEquals(mockEntries, todaysViewModel.getState().getEntries());
    }

    @Test
    void testPrepareSuccessViewWithNullLogId() {
        // Arrange
        EditWellnessLogOutputData outputData = new EditWellnessLogOutputData(null, false);
        List<WellnessLogEntryInterf> mockEntries = Arrays.asList(mockEntry1);
        when(mockDataAccess.getTodaysWellnessLogEntries()).thenReturn(mockEntries);
        
        // Act
        presenter.prepareSuccessView(outputData);
        
        // Assert
        EditWellnessLogState editState = editViewModel.getState();
        assertEquals("", editState.getLogId());
        assertEquals("", editState.getErrorMessage());
    }

    @Test
    void testPrepareSuccessViewWithEmptyEntryList() {
        // Arrange
        EditWellnessLogOutputData outputData = new EditWellnessLogOutputData("log-empty", true);
        List<WellnessLogEntryInterf> emptyEntries = Arrays.asList();
        when(mockDataAccess.getTodaysWellnessLogEntries()).thenReturn(emptyEntries);
        
        // Act
        presenter.prepareSuccessView(outputData);
        
        // Assert
        TodaysWellnessLogState todaysState = todaysViewModel.getState();
        assertEquals(emptyEntries, todaysState.getEntries());
        assertTrue(todaysState.getEntries().isEmpty());
    }

    @Test
    void testPrepareFailView() {
        // Arrange
        String errorMessage = "Failed to edit wellness log: validation error";
        PropertyChangeListener editListener = mock(PropertyChangeListener.class);
        editViewModel.addPropertyChangeListener(editListener);
        
        // Act
        presenter.prepareFailView(errorMessage);
        
        // Assert
        EditWellnessLogState errorState = editViewModel.getState();
        assertEquals(errorMessage, errorState.getErrorMessage());
        assertEquals("", errorState.getLogId()); // Should be empty/default
        verify(editListener, atLeastOnce()).propertyChange(any());
    }

    @Test
    void testPrepareFailViewWithNullError() {
        // Act
        presenter.prepareFailView(null);
        
        // Assert
        EditWellnessLogState errorState = editViewModel.getState();
        assertEquals("", errorState.getErrorMessage());
    }

    @Test
    void testPrepareFailViewWithEmptyError() {
        // Act
        presenter.prepareFailView("");
        
        // Assert
        EditWellnessLogState errorState = editViewModel.getState();
        assertEquals("", errorState.getErrorMessage());
    }

    @Test
    void testPrepareSuccessViewFiresCorrectViewModelMethods() {
        // Arrange
        EditWellnessLogOutputData outputData = new EditWellnessLogOutputData("log-test", true);
        List<WellnessLogEntryInterf> mockEntries = Arrays.asList(mockEntry1);
        when(mockDataAccess.getTodaysWellnessLogEntries()).thenReturn(mockEntries);
        
        PropertyChangeListener editListener = mock(PropertyChangeListener.class);
        PropertyChangeListener todaysListener = mock(PropertyChangeListener.class);
        editViewModel.addPropertyChangeListener(editListener);
        todaysViewModel.addPropertyChangeListener(todaysListener);
        
        // Act
        presenter.prepareSuccessView(outputData);
        
        // Assert - verify both ViewModels fire property changes
        verify(editListener, atLeastOnce()).propertyChange(any());
        verify(todaysListener, atLeastOnce()).propertyChange(any());
    }

    @Test
    void testPrepareFailViewDoesNotAffectTodaysViewModel() {
        // Arrange
        String errorMessage = "Edit failed";
        PropertyChangeListener todaysListener = mock(PropertyChangeListener.class);
        todaysViewModel.addPropertyChangeListener(todaysListener);
        
        // Store initial state
        TodaysWellnessLogState initialTodaysState = todaysViewModel.getState();
        
        // Act
        presenter.prepareFailView(errorMessage);
        
        // Assert - todaysViewModel should not be updated during failure
        assertEquals(initialTodaysState, todaysViewModel.getState());
        verify(mockDataAccess, never()).getTodaysWellnessLogEntries();
        // todaysListener should not be called since no state change occurred
        verifyNoInteractions(todaysListener);
    }

    @Test
    void testPrepareSuccessViewWithLargeEntryList() {
        // Arrange
        EditWellnessLogOutputData outputData = new EditWellnessLogOutputData("large-list-test", false);
        
        // Create a large list of mock entries
        WellnessLogEntryInterf[] mockEntries = new WellnessLogEntryInterf[100];
        for (int i = 0; i < 100; i++) {
            mockEntries[i] = mock(WellnessLogEntryInterf.class);
        }
        List<WellnessLogEntryInterf> largeEntryList = Arrays.asList(mockEntries);
        when(mockDataAccess.getTodaysWellnessLogEntries()).thenReturn(largeEntryList);
        
        // Act
        presenter.prepareSuccessView(outputData);
        
        // Assert
        TodaysWellnessLogState todaysState = todaysViewModel.getState();
        assertEquals(largeEntryList, todaysState.getEntries());
        assertEquals(100, todaysState.getEntries().size());
    }

    @Test
    void testMultipleSuccessViewCallsUpdateState() {
        // Arrange
        List<WellnessLogEntryInterf> mockEntries = Arrays.asList(mockEntry1);
        when(mockDataAccess.getTodaysWellnessLogEntries()).thenReturn(mockEntries);
        
        EditWellnessLogOutputData outputData1 = new EditWellnessLogOutputData("log-001", true);
        EditWellnessLogOutputData outputData2 = new EditWellnessLogOutputData("log-002", false);
        
        // Act
        presenter.prepareSuccessView(outputData1);
        presenter.prepareSuccessView(outputData2);
        
        // Assert - final state should reflect the last call
        EditWellnessLogState finalEditState = editViewModel.getState();
        assertEquals("log-002", finalEditState.getLogId());
        assertEquals("", finalEditState.getErrorMessage());
        
        // Data access should have been called twice
        verify(mockDataAccess, times(2)).getTodaysWellnessLogEntries();
    }
}