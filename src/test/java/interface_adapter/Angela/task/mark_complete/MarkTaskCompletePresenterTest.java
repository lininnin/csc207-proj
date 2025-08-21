package interface_adapter.Angela.task.mark_complete;

import interface_adapter.Angela.task.today.TodayTasksViewModel;
import interface_adapter.Angela.task.today.TodayTasksState;
import interface_adapter.Angela.task.overdue.OverdueTasksController;
import interface_adapter.Angela.today_so_far.TodaySoFarController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test_utils.TestDataResetUtil;
import use_case.Angela.task.mark_complete.MarkTaskCompleteOutputData;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

class MarkTaskCompletePresenterTest {

    private TodayTasksViewModel mockTodayTasksViewModel;
    private OverdueTasksController mockOverdueTasksController;
    private TodaySoFarController mockTodaySoFarController;
    private MarkTaskCompletePresenter presenter;
    private TodayTasksState mockTodayTasksState;

    @BeforeEach
    void setUp() {
        TestDataResetUtil.resetAllSharedData();
        mockTodayTasksViewModel = mock(TodayTasksViewModel.class);
        mockOverdueTasksController = mock(OverdueTasksController.class);
        mockTodaySoFarController = mock(TodaySoFarController.class);
        
        mockTodayTasksState = mock(TodayTasksState.class);
        when(mockTodayTasksViewModel.getState()).thenReturn(mockTodayTasksState);
        
        presenter = new MarkTaskCompletePresenter(mockTodayTasksViewModel);
        presenter.setOverdueTasksController(mockOverdueTasksController);
        presenter.setTodaySoFarController(mockTodaySoFarController);
    }

    @AfterEach
    void tearDown() {
        reset(mockTodayTasksViewModel);
        reset(mockOverdueTasksController);
        reset(mockTodaySoFarController);
        reset(mockTodayTasksState);
        presenter = null;
    }

    @Test
    void testPresentSuccess_validOutputData_updatesViewModels() {
        String taskId = "task123";
        boolean isCompleted = true;
        MarkTaskCompleteOutputData outputData = new MarkTaskCompleteOutputData(taskId, "Test Task", LocalDateTime.now(), 0.85);
        String message = "Task marked as complete";

        presenter.presentSuccess(outputData, message);

        // Verify today tasks view model update
        verify(mockTodayTasksState).setSuccessMessage(message);
        verify(mockTodayTasksState).setError(null);
        verify(mockTodayTasksState).setRefreshNeeded(true);
        verify(mockTodayTasksViewModel).setState(mockTodayTasksState);
        verify(mockTodayTasksViewModel).firePropertyChanged();

        // Verify controllers called
        verify(mockOverdueTasksController).execute(7);
        verify(mockTodaySoFarController).refresh();
    }

    @Test
    void testPresentSuccess_withNullTodayTasksState_createsNewState() {
        when(mockTodayTasksViewModel.getState()).thenReturn(null);
        String taskId = "task123";
        boolean isCompleted = false;
        MarkTaskCompleteOutputData outputData = new MarkTaskCompleteOutputData(taskId, "Test Task", LocalDateTime.now(), 0.85);
        String message = "Task marked as incomplete";

        presenter.presentSuccess(outputData, message);

        // Verify new TodayTasksState is created and used
        verify(mockTodayTasksViewModel).setState(any(TodayTasksState.class));
        verify(mockTodayTasksViewModel).firePropertyChanged();

        // Verify controllers still called
        verify(mockOverdueTasksController).execute(7);
        verify(mockTodaySoFarController).refresh();
    }

    @Test
    void testPresentSuccess_emptyMessage_updatesViewModels() {
        String taskId = "task123";
        boolean isCompleted = true;
        MarkTaskCompleteOutputData outputData = new MarkTaskCompleteOutputData(taskId, "Test Task", LocalDateTime.now(), 0.85);
        String message = "";

        presenter.presentSuccess(outputData, message);

        verify(mockTodayTasksState).setSuccessMessage(message);
        verify(mockTodayTasksState).setError(null);
        verify(mockTodayTasksState).setRefreshNeeded(true);
        verify(mockTodayTasksViewModel).setState(mockTodayTasksState);
        verify(mockTodayTasksViewModel).firePropertyChanged();
    }

    @Test
    void testPresentSuccess_nullMessage_updatesViewModels() {
        String taskId = "task123";
        boolean isCompleted = false;
        MarkTaskCompleteOutputData outputData = new MarkTaskCompleteOutputData(taskId, "Test Task", LocalDateTime.now(), 0.85);
        String message = null;

        presenter.presentSuccess(outputData, message);

        verify(mockTodayTasksState).setSuccessMessage(message);
        verify(mockTodayTasksState).setError(null);
        verify(mockTodayTasksState).setRefreshNeeded(true);
        verify(mockTodayTasksViewModel).setState(mockTodayTasksState);
        verify(mockTodayTasksViewModel).firePropertyChanged();
    }

    @Test
    void testPresentSuccess_longMessage_updatesViewModels() {
        String taskId = "task123";
        boolean isCompleted = true;
        MarkTaskCompleteOutputData outputData = new MarkTaskCompleteOutputData(taskId, "Test Task", LocalDateTime.now(), 0.85);
        String message = "This is a very long success message that contains detailed information";

        presenter.presentSuccess(outputData, message);

        verify(mockTodayTasksState).setSuccessMessage(message);
        verify(mockTodayTasksState).setError(null);
        verify(mockTodayTasksState).setRefreshNeeded(true);
        verify(mockTodayTasksViewModel).setState(mockTodayTasksState);
        verify(mockTodayTasksViewModel).firePropertyChanged();
    }

    @Test
    void testPresentSuccess_withoutControllers_stillUpdatesViewModel() {
        presenter = new MarkTaskCompletePresenter(mockTodayTasksViewModel);
        String taskId = "task123";
        boolean isCompleted = true;
        MarkTaskCompleteOutputData outputData = new MarkTaskCompleteOutputData(taskId, "Test Task", LocalDateTime.now(), 0.85);
        String message = "Task marked as complete";

        presenter.presentSuccess(outputData, message);

        // Verify today tasks view model is still updated
        verify(mockTodayTasksState).setSuccessMessage(message);
        verify(mockTodayTasksState).setError(null);
        verify(mockTodayTasksState).setRefreshNeeded(true);
        verify(mockTodayTasksViewModel).setState(mockTodayTasksState);
        verify(mockTodayTasksViewModel).firePropertyChanged();

        // Verify no interactions with controllers
        verifyNoInteractions(mockOverdueTasksController);
        verifyNoInteractions(mockTodaySoFarController);
    }

    @Test
    void testPresentError_errorMessage_updatesTodayTasksViewModel() {
        String error = "Task not found";

        presenter.presentError(error);

        // Verify today tasks view model update
        verify(mockTodayTasksState).setError(error);
        verify(mockTodayTasksState).setSuccessMessage(null);
        verify(mockTodayTasksViewModel).setState(mockTodayTasksState);
        verify(mockTodayTasksViewModel).firePropertyChanged();
    }

    @Test
    void testPresentError_withNullTodayTasksState_createsNewState() {
        when(mockTodayTasksViewModel.getState()).thenReturn(null);
        String error = "Task not found";

        presenter.presentError(error);

        // Verify new TodayTasksState is created and used
        verify(mockTodayTasksViewModel).setState(any(TodayTasksState.class));
        verify(mockTodayTasksViewModel).firePropertyChanged();
    }

    @Test
    void testPresentError_emptyErrorMessage_updatesTodayTasksViewModel() {
        String error = "";

        presenter.presentError(error);

        verify(mockTodayTasksState).setError(error);
        verify(mockTodayTasksState).setSuccessMessage(null);
        verify(mockTodayTasksViewModel).setState(mockTodayTasksState);
        verify(mockTodayTasksViewModel).firePropertyChanged();
    }

    @Test
    void testPresentError_nullErrorMessage_updatesTodayTasksViewModel() {
        String error = null;

        presenter.presentError(error);

        verify(mockTodayTasksState).setError(error);
        verify(mockTodayTasksState).setSuccessMessage(null);
        verify(mockTodayTasksViewModel).setState(mockTodayTasksState);
        verify(mockTodayTasksViewModel).firePropertyChanged();
    }

    @Test
    void testPresentError_longErrorMessage_updatesTodayTasksViewModel() {
        String error = "This is a very long error message that contains detailed information about what went wrong";

        presenter.presentError(error);

        verify(mockTodayTasksState).setError(error);
        verify(mockTodayTasksState).setSuccessMessage(null);
        verify(mockTodayTasksViewModel).setState(mockTodayTasksState);
        verify(mockTodayTasksViewModel).firePropertyChanged();
    }

    @Test
    void testSetOverdueTasksController_storesReference() {
        presenter = new MarkTaskCompletePresenter(mockTodayTasksViewModel);
        
        presenter.setOverdueTasksController(mockOverdueTasksController);

        // Test that dependency is properly set by calling presentSuccess
        String taskId = "task123";
        boolean isCompleted = true;
        MarkTaskCompleteOutputData outputData = new MarkTaskCompleteOutputData(taskId, "Test Task", LocalDateTime.now(), 0.85);
        String message = "Test message";
        presenter.presentSuccess(outputData, message);

        // If setter worked properly, this should be called
        verify(mockOverdueTasksController).execute(7);
    }

    @Test
    void testSetTodaySoFarController_storesReference() {
        presenter = new MarkTaskCompletePresenter(mockTodayTasksViewModel);
        
        presenter.setTodaySoFarController(mockTodaySoFarController);

        // Test that dependency is properly set by calling presentSuccess
        String taskId = "task123";
        boolean isCompleted = true;
        MarkTaskCompleteOutputData outputData = new MarkTaskCompleteOutputData(taskId, "Test Task", LocalDateTime.now(), 0.85);
        String message = "Test message";
        presenter.presentSuccess(outputData, message);

        // If setter worked properly, this should be called
        verify(mockTodaySoFarController).refresh();
    }
}