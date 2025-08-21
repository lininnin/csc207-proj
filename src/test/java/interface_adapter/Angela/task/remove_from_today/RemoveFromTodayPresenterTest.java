package interface_adapter.Angela.task.remove_from_today;

import interface_adapter.Angela.task.today.TodayTasksState;
import interface_adapter.Angela.task.today.TodayTasksViewModel;
import interface_adapter.Angela.task.overdue.OverdueTasksController;
import interface_adapter.Angela.today_so_far.TodaySoFarController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import use_case.Angela.task.remove_from_today.RemoveFromTodayOutputData;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for RemoveFromTodayPresenter.
 */
class RemoveFromTodayPresenterTest {

    @Mock
    private TodayTasksViewModel mockTodayTasksViewModel;
    
    @Mock
    private OverdueTasksController mockOverdueTasksController;
    
    @Mock
    private TodaySoFarController mockTodaySoFarController;

    private RemoveFromTodayPresenter presenter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        presenter = new RemoveFromTodayPresenter(mockTodayTasksViewModel);
    }

    @Test
    void testConstructor() {
        assertNotNull(presenter);
    }

    @Test
    void testPrepareSuccessView() {
        // Arrange
        RemoveFromTodayOutputData outputData = new RemoveFromTodayOutputData("task-123", "Test Task", "Task removed successfully");
        TodayTasksState mockState = new TodayTasksState();
        when(mockTodayTasksViewModel.getState()).thenReturn(mockState);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert
        verify(mockTodayTasksViewModel).setState(any(TodayTasksState.class));
        verify(mockTodayTasksViewModel).firePropertyChanged();
    }

    @Test
    void testPrepareSuccessViewWithNullState() {
        // Arrange
        RemoveFromTodayOutputData outputData = new RemoveFromTodayOutputData("task-456", "Test Task", "Task removed successfully");
        when(mockTodayTasksViewModel.getState()).thenReturn(null);

        // Act & Assert - should not throw NullPointerException
        assertThrows(NullPointerException.class, () -> {
            presenter.prepareSuccessView(outputData);
        });
    }

    @Test
    void testPrepareSuccessViewWithOverdueController() {
        // Arrange
        RemoveFromTodayOutputData outputData = new RemoveFromTodayOutputData("task-789", "Test Task", "Task removed successfully");
        TodayTasksState mockState = new TodayTasksState();
        when(mockTodayTasksViewModel.getState()).thenReturn(mockState);
        
        presenter.setOverdueTasksController(mockOverdueTasksController);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert
        verify(mockOverdueTasksController).execute(7);
    }

    @Test
    void testPrepareSuccessViewWithTodaySoFarController() {
        // Arrange
        RemoveFromTodayOutputData outputData = new RemoveFromTodayOutputData("task-000", "Test Task", "Task removed successfully");
        TodayTasksState mockState = new TodayTasksState();
        when(mockTodayTasksViewModel.getState()).thenReturn(mockState);
        
        presenter.setTodaySoFarController(mockTodaySoFarController);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert
        verify(mockTodaySoFarController).refresh();
    }

    @Test
    void testPrepareSuccessViewWithAllControllers() {
        // Arrange
        RemoveFromTodayOutputData outputData = new RemoveFromTodayOutputData("task-all", "Test Task", "Task removed successfully");
        TodayTasksState mockState = new TodayTasksState();
        when(mockTodayTasksViewModel.getState()).thenReturn(mockState);
        
        presenter.setOverdueTasksController(mockOverdueTasksController);
        presenter.setTodaySoFarController(mockTodaySoFarController);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert
        verify(mockTodayTasksViewModel).setState(any(TodayTasksState.class));
        verify(mockTodayTasksViewModel).firePropertyChanged();
        verify(mockOverdueTasksController).execute(7);
        verify(mockTodaySoFarController).refresh();
    }

    @Test
    void testPrepareFailView() {
        // Arrange
        String errorMessage = "Task not found";
        TodayTasksState mockState = new TodayTasksState();
        when(mockTodayTasksViewModel.getState()).thenReturn(mockState);

        // Act
        presenter.prepareFailView(errorMessage);

        // Assert
        verify(mockTodayTasksViewModel).setState(any(TodayTasksState.class));
        verify(mockTodayTasksViewModel).firePropertyChanged();
        
        // Should not trigger refresh on error
        verify(mockOverdueTasksController, never()).execute(anyInt());
        verify(mockTodaySoFarController, never()).refresh();
    }

    @Test
    void testPrepareFailViewWithNullState() {
        // Arrange
        String errorMessage = "Task not found";
        when(mockTodayTasksViewModel.getState()).thenReturn(null);

        // Act & Assert - should not throw NullPointerException
        assertThrows(NullPointerException.class, () -> {
            presenter.prepareFailView(errorMessage);
        });
    }

    @Test
    void testSetOverdueTasksController() {
        // Act
        presenter.setOverdueTasksController(mockOverdueTasksController);

        // Assert - no exception thrown
        assertDoesNotThrow(() -> presenter.setOverdueTasksController(mockOverdueTasksController));
    }

    @Test
    void testSetTodaySoFarController() {
        // Act
        presenter.setTodaySoFarController(mockTodaySoFarController);

        // Assert - no exception thrown
        assertDoesNotThrow(() -> presenter.setTodaySoFarController(mockTodaySoFarController));
    }

    @Test
    void testPrepareSuccessViewWithNullMessage() {
        // Arrange
        RemoveFromTodayOutputData outputData = new RemoveFromTodayOutputData("task-null", "Test Task", null);
        TodayTasksState mockState = new TodayTasksState();
        when(mockTodayTasksViewModel.getState()).thenReturn(mockState);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert
        verify(mockTodayTasksViewModel).setState(any(TodayTasksState.class));
        verify(mockTodayTasksViewModel).firePropertyChanged();
    }

    @Test
    void testPrepareFailViewWithNullError() {
        // Arrange
        TodayTasksState mockState = new TodayTasksState();
        when(mockTodayTasksViewModel.getState()).thenReturn(mockState);

        // Act
        presenter.prepareFailView(null);

        // Assert
        verify(mockTodayTasksViewModel).setState(any(TodayTasksState.class));
        verify(mockTodayTasksViewModel).firePropertyChanged();
    }

    @Test
    void testPrepareFailViewWithEmptyError() {
        // Arrange
        TodayTasksState mockState = new TodayTasksState();
        when(mockTodayTasksViewModel.getState()).thenReturn(mockState);

        // Act
        presenter.prepareFailView("");

        // Assert
        verify(mockTodayTasksViewModel).setState(any(TodayTasksState.class));
        verify(mockTodayTasksViewModel).firePropertyChanged();
    }

    @Test
    void testMultipleSuccessViews() {
        // Arrange
        RemoveFromTodayOutputData outputData1 = new RemoveFromTodayOutputData("task-1", "Test Task 1", "First removal");
        RemoveFromTodayOutputData outputData2 = new RemoveFromTodayOutputData("task-2", "Test Task 2", "Second removal");
        TodayTasksState mockState = new TodayTasksState();
        when(mockTodayTasksViewModel.getState()).thenReturn(mockState);

        // Act
        presenter.prepareSuccessView(outputData1);
        presenter.prepareSuccessView(outputData2);

        // Assert
        verify(mockTodayTasksViewModel, times(2)).setState(any(TodayTasksState.class));
        verify(mockTodayTasksViewModel, times(2)).firePropertyChanged();
    }

    @Test
    void testConstructorWithNullTodayTasksViewModel() {
        RemoveFromTodayPresenter presenter = new RemoveFromTodayPresenter(null);
        assertNotNull(presenter);
    }

    @Test
    void testSetNullControllers() {
        // Act & Assert - should not throw exceptions
        assertDoesNotThrow(() -> presenter.setOverdueTasksController(null));
        assertDoesNotThrow(() -> presenter.setTodaySoFarController(null));
    }

    @Test
    void testPrepareSuccessViewWithNullControllers() {
        // Arrange
        RemoveFromTodayOutputData outputData = new RemoveFromTodayOutputData("task-123", "Test Task", "Task removed successfully");
        TodayTasksState mockState = new TodayTasksState();
        when(mockTodayTasksViewModel.getState()).thenReturn(mockState);
        
        // Explicitly set controllers to null
        presenter.setOverdueTasksController(null);
        presenter.setTodaySoFarController(null);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert - should not cause NullPointerException
        verify(mockTodayTasksViewModel).setState(any(TodayTasksState.class));
        verify(mockTodayTasksViewModel).firePropertyChanged();
        // Controllers should not be called since they are null
    }
}