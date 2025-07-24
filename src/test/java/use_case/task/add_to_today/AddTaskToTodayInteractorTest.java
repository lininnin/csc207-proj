package use_case.task.add_to_today;

import entity.AvailableTask;
import entity.TodaysTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.task.TaskRepository;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AddTaskToTodayInteractor.
 */
public class AddTaskToTodayInteractorTest {

    private MockTaskRepository mockRepository;
    private MockAddTaskToTodayPresenter mockPresenter;
    private AddTaskToTodayInteractor interactor;
    private AvailableTask testTask;

    @BeforeEach
    public void setUp() {
        mockRepository = new MockTaskRepository();
        mockPresenter = new MockAddTaskToTodayPresenter();
        interactor = new AddTaskToTodayInteractor(mockRepository, mockPresenter);

        // Create a test available task
        testTask = AvailableTask.createRegular("task-1", "Test Task", "Description", "Work");
        mockRepository.availableTasks.put("task-1", testTask);
    }

    @Test
    public void testAddTaskToTodaySuccess() {
        LocalDate dueDate = LocalDate.now().plusDays(3);
        AddTaskToTodayInputData inputData = new AddTaskToTodayInputData(
                "task-1",
                TodaysTask.Priority.HIGH,
                dueDate
        );

        interactor.addTaskToToday(inputData);

        assertTrue(mockPresenter.successCalled);
        assertFalse(mockPresenter.errorCalled);
        assertEquals("task-1", mockPresenter.lastSuccessData.getTaskId());
        assertEquals("Test Task", mockPresenter.lastSuccessData.getTaskName());

        // Verify task was added to today's list
        assertEquals(1, mockRepository.todaysTasks.size());
        TodaysTask todaysTask = mockRepository.todaysTasks.get("task-1");
        assertEquals(TodaysTask.Priority.HIGH, todaysTask.getPriority());
        assertEquals(dueDate, todaysTask.getBeginAndDueDates().getDueDate());
    }

    @Test
    public void testAddTaskToTodayNullPriority() {
        AddTaskToTodayInputData inputData = new AddTaskToTodayInputData(
                "task-1",
                null,
                null
        );

        interactor.addTaskToToday(inputData);

        assertTrue(mockPresenter.successCalled);
        TodaysTask todaysTask = mockRepository.todaysTasks.get("task-1");
        assertEquals(TodaysTask.Priority.MEDIUM, todaysTask.getPriority());
    }

    @Test
    public void testAddTaskNotFound() {
        AddTaskToTodayInputData inputData = new AddTaskToTodayInputData(
                "non-existent",
                TodaysTask.Priority.LOW,
                null
        );

        interactor.addTaskToToday(inputData);

        assertFalse(mockPresenter.successCalled);
        assertTrue(mockPresenter.errorCalled);
        assertEquals("Task not found in available tasks", mockPresenter.lastError);
    }

    @Test
    public void testAddTaskAlreadyInToday() {
        // Add task to today's list
        mockRepository.todaysTasks.put("task-1",
                new TodaysTask(testTask, TodaysTask.Priority.LOW,
                        entity.BeginAndDueDates.startingToday(null)));

        AddTaskToTodayInputData inputData = new AddTaskToTodayInputData(
                "task-1",
                TodaysTask.Priority.HIGH,
                null
        );

        interactor.addTaskToToday(inputData);

        assertFalse(mockPresenter.successCalled);
        assertTrue(mockPresenter.errorCalled);
        assertEquals("Task is already in today's list", mockPresenter.lastError);
    }

    @Test
    public void testAddTaskPastDueDate() {
        LocalDate pastDate = LocalDate.now().minusDays(1);
        AddTaskToTodayInputData inputData = new AddTaskToTodayInputData(
                "task-1",
                TodaysTask.Priority.LOW,
                pastDate
        );

        interactor.addTaskToToday(inputData);

        assertFalse(mockPresenter.successCalled);
        assertTrue(mockPresenter.errorCalled);
        assertEquals("Due date cannot be in the past", mockPresenter.lastError);
    }

    // Mock implementations

    private static class MockTaskRepository implements TaskRepository {
        final Map<String, AvailableTask> availableTasks = new HashMap<>();
        final Map<String, TodaysTask> todaysTasks = new HashMap<>();

        @Override
        public AvailableTask findAvailableTaskById(String taskId) {
            return availableTasks.get(taskId);
        }

        @Override
        public boolean isInTodaysList(String taskId) {
            return todaysTasks.containsKey(taskId);
        }

        @Override
        public void addTodaysTask(TodaysTask task) {
            todaysTasks.put(task.getInfo().getId(), task);
        }

        // Other methods not used - adding comments to explain
        @Override
        public void saveAvailableTask(AvailableTask task) {
            // Not needed for this test
        }

        @Override
        public void updateAvailableTask(AvailableTask task) {
            // Not needed for this test
        }

        @Override
        public boolean deleteAvailableTask(String taskId) {
            // Not needed for this test
            return false;
        }

        @Override
        public List<AvailableTask> findAllAvailableTasks() {
            return new ArrayList<>(availableTasks.values());
        }

        @Override
        public boolean availableTaskNameExists(String name) {
            // Not needed for this test
            return false;
        }

        @Override
        public void updateTodaysTask(TodaysTask task) {
            // Not needed for this test
        }

        @Override
        public boolean removeTodaysTask(String taskId) {
            // Not needed for this test
            return false;
        }

        @Override
        public List<TodaysTask> findAllTodaysTasks() {
            return new ArrayList<>(todaysTasks.values());
        }

        @Override
        public List<TodaysTask> findTodaysTasksByStatus(boolean completed) {
            // Not needed for this test
            return new ArrayList<>();
        }

        @Override
        public List<TodaysTask> findOverdueTasks() {
            // Not needed for this test
            return new ArrayList<>();
        }

        @Override
        public void clearTodaysTasks() {
            // Not needed for this test
        }

        @Override
        public void removeOneTimeTasks(List<String> taskIds) {
            // Not needed for this test
        }
    }

    private static class MockAddTaskToTodayPresenter implements AddTaskToTodayOutputBoundary {
        boolean successCalled = false;
        boolean errorCalled = false;
        AddTaskToTodayOutputData lastSuccessData;
        String lastError;

        @Override
        public void presentSuccess(AddTaskToTodayOutputData outputData) {
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