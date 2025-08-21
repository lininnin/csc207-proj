package interface_adapter.Angela.task.edit_today;

import interface_adapter.Angela.task.today.TodayTasksState;
import interface_adapter.Angela.task.today.TodayTasksViewModel;
import interface_adapter.Angela.task.overdue.OverdueTasksController;
import interface_adapter.Angela.today_so_far.TodaySoFarController;
import entity.Angela.Task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import use_case.Angela.task.edit_today.EditTodayTaskOutputData;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for EditTodayTaskPresenter.
 */
class EditTodayTaskPresenterTest {

    @Mock
    private EditTodayTaskViewModel mockEditViewModel;
    
    @Mock
    private TodayTasksViewModel mockTodayTasksViewModel;
    
    @Mock
    private OverdueTasksController mockOverdueTasksController;
    
    @Mock
    private TodaySoFarController mockTodaySoFarController;

    private EditTodayTaskPresenter presenter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        presenter = new EditTodayTaskPresenter(mockEditViewModel, mockTodayTasksViewModel);
    }

    @Test
    void testConstructor() {
        assertNotNull(presenter);
    }

    @Test
    void testPrepareSuccessView() {
        // Arrange
        EditTodayTaskOutputData outputData = new EditTodayTaskOutputData("task-123", "Test Task", Task.Priority.HIGH, LocalDate.now(), "Task updated successfully");
        TodayTasksState mockTodayState = new TodayTasksState();
        when(mockTodayTasksViewModel.getState()).thenReturn(mockTodayState);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert
        verify(mockEditViewModel).setState(any(EditTodayTaskState.class));
        verify(mockEditViewModel).firePropertyChanged();
        verify(mockTodayTasksViewModel).setState(any(TodayTasksState.class));
        verify(mockTodayTasksViewModel).firePropertyChanged();
    }

    @Test
    void testPrepareSuccessViewWithNullTodayState() {
        // Arrange
        EditTodayTaskOutputData outputData = new EditTodayTaskOutputData("task-456", "Test Task", Task.Priority.MEDIUM, LocalDate.now(), "Task updated successfully");
        when(mockTodayTasksViewModel.getState()).thenReturn(null);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert
        verify(mockEditViewModel).setState(any(EditTodayTaskState.class));
        verify(mockEditViewModel).firePropertyChanged();
        verify(mockTodayTasksViewModel).setState(any(TodayTasksState.class));
        verify(mockTodayTasksViewModel).firePropertyChanged();
    }

    @Test
    void testPrepareSuccessViewWithOverdueController() {
        // Arrange
        EditTodayTaskOutputData outputData = new EditTodayTaskOutputData("task-789", "Test Task", Task.Priority.LOW, LocalDate.now(), "Task updated successfully");
        TodayTasksState mockTodayState = new TodayTasksState();
        when(mockTodayTasksViewModel.getState()).thenReturn(mockTodayState);
        
        presenter.setOverdueTasksController(mockOverdueTasksController);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert
        verify(mockOverdueTasksController).execute(7);
    }

    @Test
    void testPrepareSuccessViewWithTodaySoFarController() {
        // Arrange
        EditTodayTaskOutputData outputData = new EditTodayTaskOutputData("task-000", "Test Task", Task.Priority.HIGH, LocalDate.now(), "Task updated successfully");
        TodayTasksState mockTodayState = new TodayTasksState();
        when(mockTodayTasksViewModel.getState()).thenReturn(mockTodayState);
        
        presenter.setTodaySoFarController(mockTodaySoFarController);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert
        verify(mockTodaySoFarController).refresh();
    }

    @Test
    void testPrepareSuccessViewWithAllControllers() {
        // Arrange
        EditTodayTaskOutputData outputData = new EditTodayTaskOutputData("task-all", "Test Task", Task.Priority.MEDIUM, LocalDate.now(), "Task updated successfully");
        TodayTasksState mockTodayState = new TodayTasksState();
        when(mockTodayTasksViewModel.getState()).thenReturn(mockTodayState);
        
        presenter.setOverdueTasksController(mockOverdueTasksController);
        presenter.setTodaySoFarController(mockTodaySoFarController);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert
        verify(mockEditViewModel).setState(any(EditTodayTaskState.class));
        verify(mockEditViewModel).firePropertyChanged();
        verify(mockTodayTasksViewModel).setState(any(TodayTasksState.class));
        verify(mockTodayTasksViewModel).firePropertyChanged();
        verify(mockOverdueTasksController).execute(7);
        verify(mockTodaySoFarController).refresh();
    }

    @Test
    void testPrepareFailView() {
        // Arrange
        String errorMessage = "Task not found";

        // Act
        presenter.prepareFailView(errorMessage);

        // Assert
        verify(mockEditViewModel).setState(any(EditTodayTaskState.class));
        verify(mockEditViewModel).firePropertyChanged();
        
        // Should not trigger refresh on error
        verify(mockTodayTasksViewModel, never()).firePropertyChanged();
        verify(mockOverdueTasksController, never()).execute(anyInt());
        verify(mockTodaySoFarController, never()).refresh();
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
        EditTodayTaskOutputData outputData = new EditTodayTaskOutputData("task-null", "Test Task", null, null, null);
        TodayTasksState mockTodayState = new TodayTasksState();
        when(mockTodayTasksViewModel.getState()).thenReturn(mockTodayState);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert
        verify(mockEditViewModel).setState(any(EditTodayTaskState.class));
        verify(mockEditViewModel).firePropertyChanged();
        verify(mockTodayTasksViewModel).setState(any(TodayTasksState.class));
        verify(mockTodayTasksViewModel).firePropertyChanged();
    }

    @Test
    void testPrepareFailViewWithNullError() {
        // Act
        presenter.prepareFailView(null);

        // Assert
        verify(mockEditViewModel).setState(any(EditTodayTaskState.class));
        verify(mockEditViewModel).firePropertyChanged();
    }

    @Test
    void testPrepareFailViewWithEmptyError() {
        // Act
        presenter.prepareFailView("");

        // Assert
        verify(mockEditViewModel).setState(any(EditTodayTaskState.class));
        verify(mockEditViewModel).firePropertyChanged();
    }

    @Test
    void testMultipleSuccessViews() {
        // Arrange
        EditTodayTaskOutputData outputData1 = new EditTodayTaskOutputData("task-1", "Test Task 1", Task.Priority.HIGH, LocalDate.now(), "First update");
        EditTodayTaskOutputData outputData2 = new EditTodayTaskOutputData("task-2", "Test Task 2", Task.Priority.LOW, LocalDate.now(), "Second update");
        TodayTasksState mockTodayState = new TodayTasksState();
        when(mockTodayTasksViewModel.getState()).thenReturn(mockTodayState);

        // Act
        presenter.prepareSuccessView(outputData1);
        presenter.prepareSuccessView(outputData2);

        // Assert
        verify(mockEditViewModel, times(2)).setState(any(EditTodayTaskState.class));
        verify(mockEditViewModel, times(2)).firePropertyChanged();
        verify(mockTodayTasksViewModel, times(2)).setState(any(TodayTasksState.class));
        verify(mockTodayTasksViewModel, times(2)).firePropertyChanged();
    }

    @Test
    void testConstructorWithNullEditViewModel() {
        EditTodayTaskPresenter presenter = new EditTodayTaskPresenter(null, mockTodayTasksViewModel);
        assertNotNull(presenter);
    }

    @Test
    void testConstructorWithNullTodayTasksViewModel() {
        EditTodayTaskPresenter presenter = new EditTodayTaskPresenter(mockEditViewModel, null);
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
        EditTodayTaskOutputData outputData = new EditTodayTaskOutputData("task-123", "Test Task", Task.Priority.HIGH, LocalDate.now(), "Task updated successfully");
        TodayTasksState mockTodayState = new TodayTasksState();
        when(mockTodayTasksViewModel.getState()).thenReturn(mockTodayState);
        
        // Explicitly set controllers to null
        presenter.setOverdueTasksController(null);
        presenter.setTodaySoFarController(null);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert - should not cause NullPointerException
        verify(mockEditViewModel).setState(any(EditTodayTaskState.class));
        verify(mockEditViewModel).firePropertyChanged();
        verify(mockTodayTasksViewModel).setState(any(TodayTasksState.class));
        verify(mockTodayTasksViewModel).firePropertyChanged();
        // Controllers should not be called since they are null
    }
}