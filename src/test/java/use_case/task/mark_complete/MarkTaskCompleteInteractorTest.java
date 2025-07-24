package use_case.task.mark_complete;

import entity.AvailableTask;
import entity.BeginAndDueDates;
import entity.TodaysTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.task.TaskRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MarkTaskCompleteInteractor.
 */
public class MarkTaskCompleteInteractorTest {

    private MockTaskRepository mockRepository;
    private MockMarkTaskCompletePresenter mockPresenter;
    private MarkTaskCompleteInteractor interactor;
    private TodaysTask testTask;

    @BeforeEach
    public void setUp() {
        mockRepository = new MockTaskRepository();
        mockPresenter = new MockMarkTaskCompletePresenter();
        interactor = new MarkTaskCompleteInteractor(mockRepository, mockPresenter);

        // Create test task
        AvailableTask availableTask = AvailableTask.createRegular("task-1", "Test Task", null, "Work");
        BeginAndDueDates dates = BeginAndDueDates.startingToday(LocalDate.now().plusDays(2));
        testTask = new TodaysTask(availableTask, TodaysTask.Priority.MEDIUM, dates);
        mockRepository.todaysTasks.add(testTask);
    }

    @Test
    public void testMarkCompleteSuccess() {
        MarkTaskCompleteInputData inputData = new MarkTaskCompleteInputData("task-1");

        interactor.markComplete(inputData);

        assertTrue(mockPresenter.successCalled);
        assertFalse(mockPresenter.errorCalled);
        assertEquals("task-1", mockPresenter.lastSuccessData.getTaskId());
        assertEquals("Test Task", mockPresenter.lastSuccessData.getTaskName());
        assertTrue(mockPresenter.lastSuccessData.isCompleted());
        assertNotNull(mockPresenter.lastSuccessData.getCompletedDateTime());

        // Verify task was updated
        assertTrue(mockRepository.updateCalled);
        assertTrue(testTask.isCompleted());
    }

    @Test
    public void testMarkCompleteTaskNotFound() {
        MarkTaskCompleteInputData inputData = new MarkTaskCompleteInputData("non-existent");

        interactor.markComplete(inputData);

        assertFalse(mockPresenter.successCalled);
        assertTrue(mockPresenter.errorCalled);
        assertEquals("Task not found in today's list", mockPresenter.lastError);
    }

    @Test
    public void testMarkCompleteAlreadyCompleted() {
        testTask.markComplete();
        MarkTaskCompleteInputData inputData = new MarkTaskCompleteInputData("task-1");

        interactor.markComplete(inputData);

        assertFalse(mockPresenter.successCalled);
        assertTrue(mockPresenter.errorCalled);
        assertEquals("Task is already completed", mockPresenter.lastError);
    }

    @Test
    public void testMarkIncompleteSuccess() {
        testTask.markComplete();
        LocalDateTime completedTime = testTask.getCompletedDateTime();

        MarkTaskCompleteInputData inputData = new MarkTaskCompleteInputData("task-1");

        interactor.markIncomplete(inputData);

        assertTrue(mockPresenter.successCalled);
        assertFalse(mockPresenter.errorCalled);
        assertFalse(mockPresenter.lastSuccessData.isCompleted());
        assertNull(mockPresenter.lastSuccessData.getCompletedDateTime());

        // Verify task was updated
        assertTrue(mockRepository.updateCalled);
        assertFalse(testTask.isCompleted());
    }

    @Test
    public void testMarkIncompleteNotCompleted() {
        MarkTaskCompleteInputData inputData = new MarkTaskCompleteInputData("task-1");

        interactor.markIncomplete(inputData);

        assertFalse(mockPresenter.successCalled);
        assertTrue(mockPresenter.errorCalled);
        assertEquals("Task is not completed", mockPresenter.lastError);
    }

    @Test
    public void testMarkIncompleteTaskNotFound() {
        MarkTaskCompleteInputData inputData = new MarkTaskCompleteInputData("non-existent");

        interactor.markIncomplete(inputData);

        assertFalse(mockPresenter.successCalled);
        assertTrue(mockPresenter.errorCalled);
        assertEquals("Task not found in today's list", mockPresenter.lastError);
    }

    // Mock implementations

    private static class MockTaskRepository implements TaskRepository {
        List<TodaysTask> todaysTasks = new ArrayList<>();
        boolean updateCalled = false;

        @Override
        public List<TodaysTask> findAllTodaysTasks() {
            return todaysTasks;
        }

        @Override
        public void updateTodaysTask(TodaysTask task) {
            updateCalled = true;
        }

        // Other methods not used
        @Override
        public void saveAvailableTask(AvailableTask task) {}
        @Override
        public void updateAvailableTask(AvailableTask task) {}
        @Override
        public boolean deleteAvailableTask(String taskId) { return false; }
        @Override
        public AvailableTask findAvailableTaskById(String taskId) { return null; }
        @Override
        public List<AvailableTask> findAllAvailableTasks() { return new ArrayList<>(); }
        @Override
        public boolean availableTaskNameExists(String name) { return false; }
        @Override
        public void addTodaysTask(TodaysTask task) {}
        @Override
        public boolean removeTodaysTask(String taskId) { return false; }
        @Override
        public List<TodaysTask> findTodaysTasksByStatus(boolean completed) { return new ArrayList<>(); }
        @Override
        public List<TodaysTask> findOverdueTasks() { return new ArrayList<>(); }
        @Override
        public boolean isInTodaysList(String taskId) { return false; }
        @Override
        public void clearTodaysTasks() {}
        @Override
        public void removeOneTimeTasks(List<String> taskIds) {}
    }

    private static class MockMarkTaskCompletePresenter implements MarkTaskCompleteOutputBoundary {
        boolean successCalled = false;
        boolean errorCalled = false;
        MarkTaskCompleteOutputData lastSuccessData;
        String lastError;

        @Override
        public void presentSuccess(MarkTaskCompleteOutputData outputData) {
            successCalled = true;
            lastSuccessData = outputData;
        }

        @Override
        public void presentError(String error) {
            errorCalled = true;
            lastError = error;
        }
    }
}