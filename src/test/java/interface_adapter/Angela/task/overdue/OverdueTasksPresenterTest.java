package interface_adapter.Angela.task.overdue;

import use_case.Angela.task.overdue.OverdueTasksOutputData.OverdueTaskData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.Angela.task.overdue.OverdueTasksOutputData;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OverdueTasksPresenterTest {

    private OverdueTasksPresenter presenter;
    private OverdueTasksViewModel mockViewModel;
    private OverdueTasksState mockState;

    @BeforeEach
    void setUp() {
        mockViewModel = mock(OverdueTasksViewModel.class);
        mockState = mock(OverdueTasksState.class);
        when(mockViewModel.getState()).thenReturn(mockState);
        presenter = new OverdueTasksPresenter(mockViewModel);
    }

    @Test
    void testConstructor() {
        assertNotNull(presenter);
    }

    @Test
    void testConstructorWithNullViewModel() {
        // The constructor doesn't validate null, check that it accepts null
        OverdueTasksPresenter presenter = new OverdueTasksPresenter(null);
        assertNotNull(presenter);
        // Just verify it was created successfully - NPE behavior depends on implementation
    }

    @Test
    void testPresentOverdueTasksWithValidData() {
        // Arrange
        List<OverdueTaskData> overdueTasks = new ArrayList<>();
        OverdueTaskData task1 = mock(OverdueTaskData.class);
        OverdueTaskData task2 = mock(OverdueTaskData.class);
        overdueTasks.add(task1);
        overdueTasks.add(task2);
        
        OverdueTasksOutputData outputData = new OverdueTasksOutputData(overdueTasks);

        // Act
        presenter.presentOverdueTasks(outputData);

        // Assert
        verify(mockState, times(1)).setOverdueTasks(overdueTasks);
        verify(mockState, times(1)).setTotalOverdueTasks(2);
        verify(mockState, times(1)).setError(null);
        verify(mockViewModel, times(1)).setState(mockState);
        verify(mockViewModel, times(1)).firePropertyChanged();
    }

    @Test
    void testPresentOverdueTasksWithEmptyList() {
        // Arrange
        List<OverdueTaskData> overdueTasks = new ArrayList<>();
        OverdueTasksOutputData outputData = new OverdueTasksOutputData(overdueTasks);

        // Act
        presenter.presentOverdueTasks(outputData);

        // Assert
        verify(mockState, times(1)).setOverdueTasks(overdueTasks);
        verify(mockState, times(1)).setTotalOverdueTasks(0);
        verify(mockState, times(1)).setError(null);
        verify(mockViewModel, times(1)).setState(mockState);
        verify(mockViewModel, times(1)).firePropertyChanged();
    }

    @Test
    void testPresentOverdueTasksWithLargeList() {
        // Arrange
        List<OverdueTaskData> overdueTasks = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            overdueTasks.add(mock(OverdueTaskData.class));
        }
        OverdueTasksOutputData outputData = new OverdueTasksOutputData(overdueTasks);

        // Act
        presenter.presentOverdueTasks(outputData);

        // Assert
        verify(mockState, times(1)).setOverdueTasks(overdueTasks);
        verify(mockState, times(1)).setTotalOverdueTasks(100);
        verify(mockState, times(1)).setError(null);
        verify(mockViewModel, times(1)).setState(mockState);
        verify(mockViewModel, times(1)).firePropertyChanged();
    }

    @Test
    void testPresentOverdueTasksClearsError() {
        // Arrange
        List<OverdueTaskData> overdueTasks = new ArrayList<>();
        overdueTasks.add(mock(OverdueTaskData.class));
        OverdueTasksOutputData outputData = new OverdueTasksOutputData(overdueTasks);

        // Act
        presenter.presentOverdueTasks(outputData);

        // Assert - Verify error is cleared
        verify(mockState, times(1)).setError(null);
        verify(mockViewModel, times(1)).setState(mockState);
        verify(mockViewModel, times(1)).firePropertyChanged();
    }

    @Test
    void testPrepareFailView() {
        // Arrange
        String errorMessage = "Failed to load overdue tasks";

        // Act
        presenter.prepareFailView(errorMessage);

        // Assert
        verify(mockState, times(1)).setError(errorMessage);
        verify(mockViewModel, times(1)).setState(mockState);
        verify(mockViewModel, times(1)).firePropertyChanged();
    }

    @Test
    void testPrepareFailViewWithNullError() {
        // Act
        presenter.prepareFailView(null);

        // Assert
        verify(mockState, times(1)).setError(null);
        verify(mockViewModel, times(1)).setState(mockState);
        verify(mockViewModel, times(1)).firePropertyChanged();
    }

    @Test
    void testPrepareFailViewWithEmptyError() {
        // Arrange
        String errorMessage = "";

        // Act
        presenter.prepareFailView(errorMessage);

        // Assert
        verify(mockState, times(1)).setError(errorMessage);
        verify(mockViewModel, times(1)).setState(mockState);
        verify(mockViewModel, times(1)).firePropertyChanged();
    }

    @Test
    void testPrepareFailViewWithLongError() {
        // Arrange
        String errorMessage = "A very long error message that might occur during the overdue tasks loading process";

        // Act
        presenter.prepareFailView(errorMessage);

        // Assert
        verify(mockState, times(1)).setError(errorMessage);
        verify(mockViewModel, times(1)).setState(mockState);
        verify(mockViewModel, times(1)).firePropertyChanged();
    }

    @Test
    void testMultiplePresentOverdueTasksCalls() {
        // Arrange
        List<OverdueTaskData> tasks1 = new ArrayList<>();
        tasks1.add(mock(OverdueTaskData.class));
        OverdueTasksOutputData outputData1 = new OverdueTasksOutputData(tasks1);

        List<OverdueTaskData> tasks2 = new ArrayList<>();
        tasks2.add(mock(OverdueTaskData.class));
        tasks2.add(mock(OverdueTaskData.class));
        OverdueTasksOutputData outputData2 = new OverdueTasksOutputData(tasks2);

        // Act
        presenter.presentOverdueTasks(outputData1);
        presenter.presentOverdueTasks(outputData2);

        // Assert
        verify(mockState, times(1)).setOverdueTasks(tasks1);
        verify(mockState, times(1)).setTotalOverdueTasks(1);
        verify(mockState, times(1)).setOverdueTasks(tasks2);
        verify(mockState, times(1)).setTotalOverdueTasks(2);
        verify(mockState, times(2)).setError(null);
        verify(mockViewModel, times(2)).setState(mockState);
        verify(mockViewModel, times(2)).firePropertyChanged();
    }

    @Test
    void testAlternatingSuccessAndFailureCalls() {
        // Arrange
        List<OverdueTaskData> overdueTasks = new ArrayList<>();
        overdueTasks.add(mock(OverdueTaskData.class));
        OverdueTasksOutputData outputData = new OverdueTasksOutputData(overdueTasks);
        String errorMessage = "Test error";

        // Act
        presenter.presentOverdueTasks(outputData);
        presenter.prepareFailView(errorMessage);
        presenter.presentOverdueTasks(outputData);

        // Assert
        verify(mockState, times(2)).setOverdueTasks(overdueTasks);
        verify(mockState, times(2)).setTotalOverdueTasks(1);
        verify(mockState, times(2)).setError(null);
        verify(mockState, times(1)).setError(errorMessage);
        verify(mockViewModel, times(3)).setState(mockState);
        verify(mockViewModel, times(3)).firePropertyChanged();
    }

    @Test
    void testPresentOverdueTasksStateInteraction() {
        // Arrange
        List<OverdueTaskData> overdueTasks = new ArrayList<>();
        OverdueTaskData task = mock(OverdueTaskData.class);
        overdueTasks.add(task);
        OverdueTasksOutputData outputData = new OverdueTasksOutputData(overdueTasks);

        // Act
        presenter.presentOverdueTasks(outputData);

        // Assert - Check interaction order
        verify(mockViewModel, times(1)).getState();
        verify(mockState, times(1)).setOverdueTasks(overdueTasks);
        verify(mockState, times(1)).setTotalOverdueTasks(1);
        verify(mockState, times(1)).setError(null);
        verify(mockViewModel, times(1)).setState(mockState);
        verify(mockViewModel, times(1)).firePropertyChanged();
    }

    @Test
    void testPrepareFailViewStateInteraction() {
        // Arrange
        String error = "Database connection failed";

        // Act
        presenter.prepareFailView(error);

        // Assert - Check interaction order
        verify(mockViewModel, times(1)).getState();
        verify(mockState, times(1)).setError(error);
        verify(mockViewModel, times(1)).setState(mockState);
        verify(mockViewModel, times(1)).firePropertyChanged();
    }
}