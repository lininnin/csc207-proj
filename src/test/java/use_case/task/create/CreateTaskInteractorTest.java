package use_case.task.create;

import entity.AvailableTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.task.TaskRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CreateTaskInteractor.
 */
public class CreateTaskInteractorTest {

    private MockTaskRepository mockRepository;
    private MockCreateTaskPresenter mockPresenter;
    private CreateTaskInteractor interactor;

    @BeforeEach
    public void setUp() {
        mockRepository = new MockTaskRepository();
        mockPresenter = new MockCreateTaskPresenter();
        interactor = new CreateTaskInteractor(mockRepository, mockPresenter);
    }

    @Test
    public void testCreateTaskSuccess() {
        CreateTaskInputData inputData = new CreateTaskInputData(
                "Study",
                "Study for exam",
                "Academic",
                false
        );

        interactor.createTask(inputData);

        assertTrue(mockPresenter.successCalled);
        assertFalse(mockPresenter.errorCalled);
        assertEquals("Study", mockPresenter.lastSuccessData.getTaskName());
        assertTrue(mockPresenter.lastSuccessData.isSuccess());

        // Verify task was saved
        assertEquals(1, mockRepository.availableTasks.size());
        AvailableTask savedTask = mockRepository.availableTasks.get(0);
        assertEquals("Study", savedTask.getInfo().getName());
        assertEquals("Study for exam", savedTask.getInfo().getDescription());
        assertEquals("Academic", savedTask.getInfo().getCategory());
        assertFalse(savedTask.isOneTime());
    }

    @Test
    public void testCreateOneTimeTask() {
        CreateTaskInputData inputData = new CreateTaskInputData(
                "Doctor Visit",
                "Annual checkup",
                "Health",
                true
        );

        interactor.createTask(inputData);

        assertTrue(mockPresenter.successCalled);
        AvailableTask savedTask = mockRepository.availableTasks.get(0);
        assertTrue(savedTask.isOneTime());
    }

    @Test
    public void testCreateTaskDuplicateName() {
        // Add existing task
        mockRepository.existingNames.add("Study");

        CreateTaskInputData inputData = new CreateTaskInputData(
                "Study",
                "Different description",
                "Work",
                false
        );

        interactor.createTask(inputData);

        assertFalse(mockPresenter.successCalled);
        assertTrue(mockPresenter.errorCalled);
        assertEquals("Task name already exists", mockPresenter.lastError);
        assertEquals(0, mockRepository.availableTasks.size());
    }

    @Test
    public void testCreateTaskInvalidName() {
        CreateTaskInputData inputData = new CreateTaskInputData(
                "This task name is way too long and exceeds limit",
                "Description",
                "Category",
                false
        );

        interactor.createTask(inputData);

        assertFalse(mockPresenter.successCalled);
        assertTrue(mockPresenter.errorCalled);
        assertTrue(mockPresenter.lastError.contains("exceed 20 characters"));
    }

    @Test
    public void testCreateTaskNullValues() {
        CreateTaskInputData inputData = new CreateTaskInputData(
                "Task",
                null,
                null,
                false
        );

        interactor.createTask(inputData);

        assertTrue(mockPresenter.successCalled);
        AvailableTask savedTask = mockRepository.availableTasks.get(0);
        assertNull(savedTask.getInfo().getDescription());
        assertNull(savedTask.getInfo().getCategory());
    }

    // Mock implementations for testing

    private static class MockTaskRepository implements TaskRepository {
        final List<AvailableTask> availableTasks = new ArrayList<>();
        final List<String> existingNames = new ArrayList<>();

        @Override
        public void saveAvailableTask(AvailableTask task) {
            availableTasks.add(task);
        }

        @Override
        public boolean availableTaskNameExists(String name) {
            return existingNames.contains(name);
        }

        // Other methods not used in this test
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
        public AvailableTask findAvailableTaskById(String taskId) {
            // Not needed for this test
            return null;
        }

        @Override
        public List<AvailableTask> findAllAvailableTasks() {
            return new ArrayList<>(availableTasks);
        }

        @Override
        public void addTodaysTask(entity.TodaysTask task) {
            // Not needed for this test
        }

        @Override
        public void updateTodaysTask(entity.TodaysTask task) {
            // Not needed for this test
        }

        @Override
        public boolean removeTodaysTask(String taskId) {
            // Not needed for this test
            return false;
        }

        @Override
        public List<entity.TodaysTask> findAllTodaysTasks() {
            // Not needed for this test
            return new ArrayList<>();
        }

        @Override
        public List<entity.TodaysTask> findTodaysTasksByStatus(boolean completed) {
            // Not needed for this test
            return new ArrayList<>();
        }

        @Override
        public List<entity.TodaysTask> findOverdueTasks() {
            // Not needed for this test
            return new ArrayList<>();
        }

        @Override
        public boolean isInTodaysList(String taskId) {
            // Not needed for this test
            return false;
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

    private static class MockCreateTaskPresenter implements CreateTaskOutputBoundary {
        boolean successCalled = false;
        boolean errorCalled = false;
        CreateTaskOutputData lastSuccessData;
        String lastError;

        @Override
        public void presentSuccess(CreateTaskOutputData outputData) {
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