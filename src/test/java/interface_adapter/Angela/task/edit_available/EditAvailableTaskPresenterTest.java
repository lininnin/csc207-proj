package interface_adapter.Angela.task.edit_available;

import interface_adapter.Angela.task.available.AvailableTasksState;
import interface_adapter.Angela.task.available.AvailableTasksViewModel;
import interface_adapter.Angela.task.today.TodayTasksState;
import interface_adapter.Angela.task.today.TodayTasksViewModel;
import interface_adapter.Angela.task.overdue.OverdueTasksController;
import interface_adapter.Angela.task.add_to_today.AddTaskToTodayViewModel;
import interface_adapter.Angela.task.add_to_today.AddTaskToTodayState;
import interface_adapter.Angela.today_so_far.TodaySoFarController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import use_case.Angela.task.edit_available.EditAvailableTaskOutputData;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for EditAvailableTaskPresenter.
 */
class EditAvailableTaskPresenterTest {

    @Mock
    private EditAvailableTaskViewModel mockEditViewModel;
    
    @Mock
    private AvailableTasksViewModel mockAvailableTasksViewModel;
    
    @Mock
    private TodayTasksViewModel mockTodayTasksViewModel;
    
    @Mock
    private OverdueTasksController mockOverdueTasksController;
    
    @Mock
    private AddTaskToTodayViewModel mockAddTaskToTodayViewModel;
    
    @Mock
    private TodaySoFarController mockTodaySoFarController;

    private EditAvailableTaskPresenter presenter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        presenter = new EditAvailableTaskPresenter(mockEditViewModel, mockAvailableTasksViewModel);
    }

    @Test
    void testConstructor() {
        assertNotNull(presenter);
    }

    @Test
    void testPrepareSuccessView() {
        // Arrange
        EditAvailableTaskOutputData outputData = new EditAvailableTaskOutputData("task-123", "Test Task", "Task updated successfully");
        AvailableTasksState mockAvailableState = new AvailableTasksState();
        when(mockAvailableTasksViewModel.getState()).thenReturn(mockAvailableState);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert
        verify(mockEditViewModel).setState(any(EditAvailableTaskState.class));
        verify(mockEditViewModel).firePropertyChanged();
        verify(mockAvailableTasksViewModel).setState(any(AvailableTasksState.class));
        verify(mockAvailableTasksViewModel).firePropertyChanged(AvailableTasksViewModel.AVAILABLE_TASKS_STATE_PROPERTY);
    }

    @Test
    void testPrepareSuccessViewWithNullAvailableState() {
        // Arrange
        EditAvailableTaskOutputData outputData = new EditAvailableTaskOutputData("task-123", "Test Task", "Task updated successfully");
        when(mockAvailableTasksViewModel.getState()).thenReturn(null);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert
        verify(mockEditViewModel).setState(any(EditAvailableTaskState.class));
        verify(mockEditViewModel).firePropertyChanged();
        verify(mockAvailableTasksViewModel).setState(any(AvailableTasksState.class));
        verify(mockAvailableTasksViewModel).firePropertyChanged(AvailableTasksViewModel.AVAILABLE_TASKS_STATE_PROPERTY);
    }

    @Test
    void testPrepareSuccessViewWithTodayTasksViewModel() {
        // Arrange
        EditAvailableTaskOutputData outputData = new EditAvailableTaskOutputData("task-123", "Test Task", "Task updated successfully");
        AvailableTasksState mockAvailableState = new AvailableTasksState();
        TodayTasksState mockTodayState = new TodayTasksState();
        
        when(mockAvailableTasksViewModel.getState()).thenReturn(mockAvailableState);
        when(mockTodayTasksViewModel.getState()).thenReturn(mockTodayState);
        
        presenter.setTodayTasksViewModel(mockTodayTasksViewModel);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert
        verify(mockTodayTasksViewModel).setState(any(TodayTasksState.class));
        verify(mockTodayTasksViewModel).firePropertyChanged();
    }

    @Test
    void testPrepareSuccessViewWithNullTodayState() {
        // Arrange
        EditAvailableTaskOutputData outputData = new EditAvailableTaskOutputData("task-123", "Test Task", "Task updated successfully");
        AvailableTasksState mockAvailableState = new AvailableTasksState();
        
        when(mockAvailableTasksViewModel.getState()).thenReturn(mockAvailableState);
        when(mockTodayTasksViewModel.getState()).thenReturn(null);
        
        presenter.setTodayTasksViewModel(mockTodayTasksViewModel);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert
        verify(mockTodayTasksViewModel).setState(any(TodayTasksState.class));
        verify(mockTodayTasksViewModel).firePropertyChanged();
    }

    @Test
    void testPrepareSuccessViewWithOverdueController() {
        // Arrange
        EditAvailableTaskOutputData outputData = new EditAvailableTaskOutputData("task-123", "Test Task", "Task updated successfully");
        AvailableTasksState mockAvailableState = new AvailableTasksState();
        when(mockAvailableTasksViewModel.getState()).thenReturn(mockAvailableState);
        
        presenter.setOverdueTasksController(mockOverdueTasksController);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert
        verify(mockOverdueTasksController).execute(7);
    }

    @Test
    void testPrepareSuccessViewWithAddToTodayViewModel() {
        // Arrange
        EditAvailableTaskOutputData outputData = new EditAvailableTaskOutputData("task-123", "Test Task", "Task updated successfully");
        AvailableTasksState mockAvailableState = new AvailableTasksState();
        AddTaskToTodayState mockAddToTodayState = new AddTaskToTodayState();
        
        when(mockAvailableTasksViewModel.getState()).thenReturn(mockAvailableState);
        when(mockAddTaskToTodayViewModel.getState()).thenReturn(mockAddToTodayState);
        
        presenter.setAddTaskToTodayViewModel(mockAddTaskToTodayViewModel);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert
        verify(mockAddTaskToTodayViewModel).setState(any(AddTaskToTodayState.class));
        verify(mockAddTaskToTodayViewModel).firePropertyChanged();
    }

    @Test
    void testPrepareSuccessViewWithNullAddToTodayState() {
        // Arrange
        EditAvailableTaskOutputData outputData = new EditAvailableTaskOutputData("task-123", "Test Task", "Task updated successfully");
        AvailableTasksState mockAvailableState = new AvailableTasksState();
        
        when(mockAvailableTasksViewModel.getState()).thenReturn(mockAvailableState);
        when(mockAddTaskToTodayViewModel.getState()).thenReturn(null);
        
        presenter.setAddTaskToTodayViewModel(mockAddTaskToTodayViewModel);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert
        verify(mockAddTaskToTodayViewModel).setState(any(AddTaskToTodayState.class));
        verify(mockAddTaskToTodayViewModel).firePropertyChanged();
    }

    @Test
    void testPrepareSuccessViewWithTodaySoFarController() {
        // Arrange
        EditAvailableTaskOutputData outputData = new EditAvailableTaskOutputData("task-123", "Test Task", "Task updated successfully");
        AvailableTasksState mockAvailableState = new AvailableTasksState();
        when(mockAvailableTasksViewModel.getState()).thenReturn(mockAvailableState);
        
        presenter.setTodaySoFarController(mockTodaySoFarController);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert
        verify(mockTodaySoFarController).refresh();
    }

    @Test
    void testPrepareFailView() {
        // Arrange
        String errorMessage = "Task update failed";

        // Act
        presenter.prepareFailView(errorMessage);

        // Assert
        verify(mockEditViewModel).setState(any(EditAvailableTaskState.class));
        verify(mockEditViewModel).firePropertyChanged();
        
        // Should not trigger refresh on error
        verify(mockAvailableTasksViewModel, never()).firePropertyChanged(anyString());
    }

    @Test
    void testSetTodayTasksViewModel() {
        // Act
        presenter.setTodayTasksViewModel(mockTodayTasksViewModel);

        // Assert - no exception thrown, and presenter should have the reference
        assertDoesNotThrow(() -> presenter.setTodayTasksViewModel(mockTodayTasksViewModel));
    }

    @Test
    void testSetAddTaskToTodayViewModel() {
        // Act
        presenter.setAddTaskToTodayViewModel(mockAddTaskToTodayViewModel);

        // Assert - no exception thrown, and presenter should have the reference
        assertDoesNotThrow(() -> presenter.setAddTaskToTodayViewModel(mockAddTaskToTodayViewModel));
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
    void testPrepareSuccessViewWithAllOptionalComponents() {
        // Arrange
        EditAvailableTaskOutputData outputData = new EditAvailableTaskOutputData("task-123", "Test Task", "Task updated successfully");
        AvailableTasksState mockAvailableState = new AvailableTasksState();
        TodayTasksState mockTodayState = new TodayTasksState();
        AddTaskToTodayState mockAddToTodayState = new AddTaskToTodayState();
        
        when(mockAvailableTasksViewModel.getState()).thenReturn(mockAvailableState);
        when(mockTodayTasksViewModel.getState()).thenReturn(mockTodayState);
        when(mockAddTaskToTodayViewModel.getState()).thenReturn(mockAddToTodayState);
        
        // Set all optional components
        presenter.setTodayTasksViewModel(mockTodayTasksViewModel);
        presenter.setOverdueTasksController(mockOverdueTasksController);
        presenter.setAddTaskToTodayViewModel(mockAddTaskToTodayViewModel);
        presenter.setTodaySoFarController(mockTodaySoFarController);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert all components are triggered
        verify(mockEditViewModel).setState(any(EditAvailableTaskState.class));
        verify(mockEditViewModel).firePropertyChanged();
        verify(mockAvailableTasksViewModel).setState(any(AvailableTasksState.class));
        verify(mockAvailableTasksViewModel).firePropertyChanged(AvailableTasksViewModel.AVAILABLE_TASKS_STATE_PROPERTY);
        verify(mockTodayTasksViewModel).setState(any(TodayTasksState.class));
        verify(mockTodayTasksViewModel).firePropertyChanged();
        verify(mockOverdueTasksController).execute(7);
        verify(mockAddTaskToTodayViewModel).setState(any(AddTaskToTodayState.class));
        verify(mockAddTaskToTodayViewModel).firePropertyChanged();
        verify(mockTodaySoFarController).refresh();
    }

    @Test
    void testConstructorWithNullViewModel() {
        EditAvailableTaskPresenter presenter = new EditAvailableTaskPresenter(null, mockAvailableTasksViewModel);
        assertNotNull(presenter);
    }

    @Test
    void testConstructorWithNullAvailableTasksViewModel() {
        EditAvailableTaskPresenter presenter = new EditAvailableTaskPresenter(mockEditViewModel, null);
        assertNotNull(presenter);
    }
}