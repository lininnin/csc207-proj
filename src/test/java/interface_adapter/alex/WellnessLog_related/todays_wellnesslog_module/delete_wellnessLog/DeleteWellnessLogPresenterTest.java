package interface_adapter.alex.WellnessLog_related.todays_wellnesslog_module.delete_wellnessLog;

import interface_adapter.alex.WellnessLog_related.todays_wellnesslog_module.todays_wellness_log.TodaysWellnessLogState;
import interface_adapter.alex.WellnessLog_related.todays_wellnesslog_module.todays_wellness_log.TodaysWellnessLogViewModel;
import interface_adapter.Angela.today_so_far.TodaySoFarController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import use_case.alex.wellness_log_related.todays_wellness_log_module.delete_wellnesslog.DeleteWellnessLogOutputData;
import use_case.alex.wellness_log_related.todays_wellness_log_module.delete_wellnesslog.DeleteWellnessLogDataAccessInterf;
import entity.alex.WellnessLogEntry.WellnessLogEntryInterf;

import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeleteWellnessLogPresenterTest {

    @Mock
    private DeleteWellnessLogDataAccessInterf mockDataAccess;
    @Mock
    private TodaySoFarController mockTodaySoFarController;
    @Mock
    private WellnessLogEntryInterf mockEntry1;
    @Mock
    private WellnessLogEntryInterf mockEntry2;

    private DeleteWellnessLogViewModel deleteViewModel;
    private TodaysWellnessLogViewModel todaysViewModel;
    private DeleteWellnessLogPresenter presenter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        deleteViewModel = new DeleteWellnessLogViewModel();
        todaysViewModel = new TodaysWellnessLogViewModel();
        presenter = new DeleteWellnessLogPresenter(deleteViewModel, todaysViewModel, mockDataAccess);
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
        DeleteWellnessLogOutputData outputData = new DeleteWellnessLogOutputData("log-123", true);
        List<WellnessLogEntryInterf> mockEntries = Arrays.asList(mockEntry1, mockEntry2);
        when(mockDataAccess.getTodaysWellnessLogEntries()).thenReturn(mockEntries);
        
        presenter.prepareSuccessView(outputData);
        
        verify(mockTodaySoFarController, times(1)).refresh();
    }

    @Test
    void testPrepareSuccessViewUpdatesDeleteViewModel() {
        // Arrange
        DeleteWellnessLogOutputData outputData = new DeleteWellnessLogOutputData("log-456", true);
        List<WellnessLogEntryInterf> mockEntries = Arrays.asList(mockEntry1);
        when(mockDataAccess.getTodaysWellnessLogEntries()).thenReturn(mockEntries);
        
        PropertyChangeListener deleteListener = mock(PropertyChangeListener.class);
        deleteViewModel.addPropertyChangeListener(deleteListener);
        
        // Act
        presenter.prepareSuccessView(outputData);
        
        // Assert
        DeleteWellnessLogState deleteState = deleteViewModel.getState();
        assertEquals("log-456", deleteState.getDeletedLogId());
        assertEquals("", deleteState.getDeleteError());
        verify(deleteListener, atLeastOnce()).propertyChange(any());
    }

    @Test
    void testPrepareSuccessViewUpdatesTodaysViewModel() {
        // Arrange
        DeleteWellnessLogOutputData outputData = new DeleteWellnessLogOutputData("log-789", true);
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
        DeleteWellnessLogOutputData outputData = new DeleteWellnessLogOutputData("log-001", true);
        List<WellnessLogEntryInterf> mockEntries = Arrays.asList(mockEntry1);
        when(mockDataAccess.getTodaysWellnessLogEntries()).thenReturn(mockEntries);
        
        // Note: not setting todaySoFarController (it should remain null)
        
        // Act
        presenter.prepareSuccessView(outputData);
        
        // Assert
        // Should not throw exception and should still update ViewModels
        assertEquals("log-001", deleteViewModel.getState().getDeletedLogId());
        assertEquals(mockEntries, todaysViewModel.getState().getEntries());
    }

    @Test
    void testPrepareSuccessViewWithNullLogId() {
        // Arrange
        DeleteWellnessLogOutputData outputData = new DeleteWellnessLogOutputData(null, true);
        List<WellnessLogEntryInterf> mockEntries = Arrays.asList(mockEntry1);
        when(mockDataAccess.getTodaysWellnessLogEntries()).thenReturn(mockEntries);
        
        // Act
        presenter.prepareSuccessView(outputData);
        
        // Assert
        DeleteWellnessLogState deleteState = deleteViewModel.getState();
        assertNull(deleteState.getDeletedLogId());
        assertEquals("", deleteState.getDeleteError());
    }

    @Test
    void testPrepareFailView() {
        // Arrange
        String errorMessage = "Failed to delete wellness log: entry not found";
        PropertyChangeListener deleteListener = mock(PropertyChangeListener.class);
        deleteViewModel.addPropertyChangeListener(deleteListener);
        
        // Act
        presenter.prepareFailView(errorMessage);
        
        // Assert
        DeleteWellnessLogState errorState = deleteViewModel.getState();
        assertEquals(errorMessage, errorState.getDeleteError());
        assertEquals("", errorState.getDeletedLogId()); // Should be empty/default
        verify(deleteListener, atLeastOnce()).propertyChange(any());
    }

    @Test
    void testPrepareFailViewWithNullError() {
        // Act
        presenter.prepareFailView(null);
        
        // Assert
        DeleteWellnessLogState errorState = deleteViewModel.getState();
        assertNull(errorState.getDeleteError());
    }

    @Test
    void testPrepareFailViewWithEmptyError() {
        // Act
        presenter.prepareFailView("");
        
        // Assert
        DeleteWellnessLogState errorState = deleteViewModel.getState();
        assertEquals("", errorState.getDeleteError());
    }

    @Test
    void testPrepareSuccessViewFiresCorrectPropertyNames() {
        // Arrange
        DeleteWellnessLogOutputData outputData = new DeleteWellnessLogOutputData("log-test", true);
        List<WellnessLogEntryInterf> mockEntries = Arrays.asList(mockEntry1);
        when(mockDataAccess.getTodaysWellnessLogEntries()).thenReturn(mockEntries);
        
        PropertyChangeListener deleteListener = mock(PropertyChangeListener.class);
        PropertyChangeListener todaysListener = mock(PropertyChangeListener.class);
        deleteViewModel.addPropertyChangeListener(deleteListener);
        todaysViewModel.addPropertyChangeListener(todaysListener);
        
        // Act
        presenter.prepareSuccessView(outputData);
        
        // Assert - verify both ViewModels fire property changes
        verify(deleteListener, atLeastOnce()).propertyChange(argThat(event -> 
            DeleteWellnessLogViewModel.DELETE_WELLNESS_LOG_PROPERTY.equals(event.getPropertyName()) ||
            "state".equals(event.getPropertyName()))); // Base ViewModel also fires "state"
        verify(todaysListener, atLeastOnce()).propertyChange(any());
    }
}