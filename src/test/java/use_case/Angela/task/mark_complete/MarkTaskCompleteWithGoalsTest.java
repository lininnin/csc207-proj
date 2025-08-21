package use_case.Angela.task.mark_complete;

import data_access.GoalRepository;
import entity.Angela.Task.Task;
import entity.Angela.Task.TaskFactory;
import entity.BeginAndDueDates.BeginAndDueDates;
import entity.Sophia.Goal;
import entity.Sophia.GoalInfo;
import entity.Sophia.GoalInterface.TimePeriod;
import entity.info.Info;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for MarkTaskCompleteInteractor with goal integration.
 * Tests the goal progress update functionality.
 */
class MarkTaskCompleteWithGoalsTest {

    private TestMarkTaskCompleteDataAccess dataAccess;
    private TestMarkTaskCompletePresenter presenter;
    private TestGoalRepository goalRepository;
    private MarkTaskCompleteInteractor interactor;

    @BeforeEach
    void setUp() {
        dataAccess = new TestMarkTaskCompleteDataAccess();
        presenter = new TestMarkTaskCompletePresenter();
        goalRepository = new TestGoalRepository();
        interactor = new MarkTaskCompleteInteractor(dataAccess, presenter, goalRepository);
    }

    @Test
    void testMarkCompleteUpdatesGoalProgress() {
        // Create a task
        TaskFactory taskFactory = new TaskFactory();
        Info taskInfo = new Info.Builder("Exercise").description("Do 30 min workout").build();
        Task task = (Task) taskFactory.create("task1", "template1", taskInfo, Task.Priority.HIGH,
            new BeginAndDueDates(LocalDate.now(), LocalDate.now()), false, null, false);
        dataAccess.addTask(task);

        // Create a goal that targets this task
        Info goalInfo = new Info.Builder("Fitness Goal").build();
        Info targetTaskInfo = new Info.Builder("Exercise").build(); // Matches task name
        GoalInfo gInfo = new GoalInfo(goalInfo, targetTaskInfo);
        Goal goal = new Goal(gInfo, new BeginAndDueDates(LocalDate.now(), LocalDate.now().plusDays(7)),
            TimePeriod.WEEK, 5);
        assertEquals(0, goal.getCurrentProgress());
        goalRepository.addGoal(goal);

        // Mark task complete
        MarkTaskCompleteInputData inputData = new MarkTaskCompleteInputData("task1", true);
        interactor.execute(inputData);

        // Verify success
        assertNotNull(presenter.lastOutputData);
        assertNull(presenter.lastError);
        assertTrue(presenter.lastMessage.contains("marked as complete"));

        // Verify goal progress was updated
        Goal updatedGoal = goalRepository.getGoalById(goal.getGoalInfo().getInfo().getId());
        assertEquals(1, updatedGoal.getCurrentProgress());
    }

    @Test
    void testMarkIncompleteDecreasesGoalProgress() {
        // Create a task
        TaskFactory taskFactory = new TaskFactory();
        Info taskInfo = new Info.Builder("Study").description("Study for exam").build();
        Task task = (Task) taskFactory.create("task2", "template2", taskInfo, null,
            new BeginAndDueDates(LocalDate.now(), LocalDate.now()), true, LocalDateTime.now(), false);
        dataAccess.addTask(task);

        // Create a goal with existing progress
        Info goalInfo = new Info.Builder("Study Goal").build();
        Info targetTaskInfo = new Info.Builder("Study").build();
        GoalInfo gInfo = new GoalInfo(goalInfo, targetTaskInfo);
        Goal goal = new Goal(gInfo, new BeginAndDueDates(LocalDate.now(), LocalDate.now().plusDays(30)),
            TimePeriod.MONTH, 20);
        goal.setCurrentProgress(5); // Already has some progress
        goalRepository.addGoal(goal);

        // Mark task incomplete
        MarkTaskCompleteInputData inputData = new MarkTaskCompleteInputData("task2", false);
        interactor.execute(inputData);

        // Verify success
        assertNotNull(presenter.lastOutputData);
        assertNull(presenter.lastError);

        // Verify goal progress was decreased
        Goal updatedGoal = goalRepository.getGoalById(goal.getGoalInfo().getInfo().getId());
        assertEquals(4, updatedGoal.getCurrentProgress());
    }

    @Test
    void testMarkIncompleteDoesNotGoBelowZero() {
        // Create a task
        TaskFactory taskFactory = new TaskFactory();
        Info taskInfo = new Info.Builder("Read").build();
        Task task = (Task) taskFactory.create("task3", "template3", taskInfo, null,
            new BeginAndDueDates(LocalDate.now(), LocalDate.now()), false, null, false);
        dataAccess.addTask(task);

        // Create a goal with zero progress
        Info goalInfo = new Info.Builder("Reading Goal").build();
        Info targetTaskInfo = new Info.Builder("Read").build();
        GoalInfo gInfo = new GoalInfo(goalInfo, targetTaskInfo);
        Goal goal = new Goal(gInfo, new BeginAndDueDates(LocalDate.now(), LocalDate.now().plusDays(7)),
            TimePeriod.WEEK, 7);
        // Progress is already 0
        assertEquals(0, goal.getCurrentProgress());
        goalRepository.addGoal(goal);

        // Mark task incomplete (should not decrease below 0)
        MarkTaskCompleteInputData inputData = new MarkTaskCompleteInputData("task3", false);
        interactor.execute(inputData);

        // Verify goal progress stays at 0
        Goal updatedGoal = goalRepository.getGoalById(goal.getGoalInfo().getInfo().getId());
        assertEquals(0, updatedGoal.getCurrentProgress());
    }

    @Test
    void testNoGoalUpdateWhenNamesDontMatch() {
        // Create a task
        TaskFactory taskFactory = new TaskFactory();
        Info taskInfo = new Info.Builder("Different Task").build();
        Task task = (Task) taskFactory.create("task4", "template4", taskInfo, null,
            new BeginAndDueDates(LocalDate.now(), LocalDate.now()), false, null, false);
        dataAccess.addTask(task);

        // Create a goal targeting a different task
        Info goalInfo = new Info.Builder("Unrelated Goal").build();
        Info targetTaskInfo = new Info.Builder("Other Task").build(); // Different name
        GoalInfo gInfo = new GoalInfo(goalInfo, targetTaskInfo);
        Goal goal = new Goal(gInfo, new BeginAndDueDates(LocalDate.now(), LocalDate.now().plusDays(7)),
            TimePeriod.WEEK, 3);
        goalRepository.addGoal(goal);

        // Mark task complete
        MarkTaskCompleteInputData inputData = new MarkTaskCompleteInputData("task4", true);
        interactor.execute(inputData);

        // Verify goal progress was NOT updated
        Goal unchangedGoal = goalRepository.getGoalById(goal.getGoalInfo().getInfo().getId());
        assertEquals(0, unchangedGoal.getCurrentProgress());
    }

    @Test
    void testGoalUpdateWithNullGoalInfo() {
        // Create a task
        TaskFactory taskFactory = new TaskFactory();
        Info taskInfo = new Info.Builder("Task").build();
        Task task = (Task) taskFactory.create("task5", "template5", taskInfo, null,
            new BeginAndDueDates(LocalDate.now(), LocalDate.now()), false, null, false);
        dataAccess.addTask(task);

        // Add a malformed goal with null GoalInfo (shouldn't happen but test defensive code)
        goalRepository.addNullInfoGoal();

        // Mark task complete - should handle null gracefully
        MarkTaskCompleteInputData inputData = new MarkTaskCompleteInputData("task5", true);
        interactor.execute(inputData);

        // Should succeed despite null goal info
        assertNotNull(presenter.lastOutputData);
        assertNull(presenter.lastError);
    }

    @Test
    void testGoalUpdateWithException() {
        // Create a task
        TaskFactory taskFactory = new TaskFactory();
        Info taskInfo = new Info.Builder("Task").build();
        Task task = (Task) taskFactory.create("task6", "template6", taskInfo, null,
            new BeginAndDueDates(LocalDate.now(), LocalDate.now()), false, null, false);
        dataAccess.addTask(task);

        // Set repository to throw exception
        goalRepository.setShouldThrowException(true);

        // Mark task complete - should handle exception gracefully
        MarkTaskCompleteInputData inputData = new MarkTaskCompleteInputData("task6", true);
        interactor.execute(inputData);

        // Task completion should still succeed even if goal update fails
        assertNotNull(presenter.lastOutputData);
        assertNull(presenter.lastError);
        assertTrue(dataAccess.getTask("task6").isCompleted());
    }

    @Test
    void testMarkCompleteWithNullGoalRepository() {
        // Create interactor without goal repository
        MarkTaskCompleteInteractor interactorNoGoals = 
            new MarkTaskCompleteInteractor(dataAccess, presenter);

        // Create a task
        TaskFactory taskFactory = new TaskFactory();
        Info taskInfo = new Info.Builder("Task").build();
        Task task = (Task) taskFactory.create("task7", "template7", taskInfo, null,
            new BeginAndDueDates(LocalDate.now(), LocalDate.now()), false, null, false);
        dataAccess.addTask(task);

        // Mark task complete
        MarkTaskCompleteInputData inputData = new MarkTaskCompleteInputData("task7", true);
        interactorNoGoals.execute(inputData);

        // Should succeed without goal updates
        assertNotNull(presenter.lastOutputData);
        assertNull(presenter.lastError);
    }

    @Test
    void testUpdateFailureDoesNotAffectSuccess() {
        // Create a task
        TaskFactory taskFactory = new TaskFactory();
        Info taskInfo = new Info.Builder("Task").build();
        Task task = (Task) taskFactory.create("task8", "template8", taskInfo, null,
            new BeginAndDueDates(LocalDate.now(), LocalDate.now()), false, null, false);
        dataAccess.addTask(task);

        // Set data access to fail update
        dataAccess.setShouldFailUpdate(true);

        // Try to mark task complete
        MarkTaskCompleteInputData inputData = new MarkTaskCompleteInputData("task8", true);
        interactor.execute(inputData);

        // Should report failure
        assertNull(presenter.lastOutputData);
        assertNotNull(presenter.lastError);
        assertEquals("Failed to update task completion status", presenter.lastError);
    }

    /**
     * Test data access implementation
     */
    private static class TestMarkTaskCompleteDataAccess implements MarkTaskCompleteDataAccessInterface {
        private final Map<String, Task> tasks = new HashMap<>();
        private boolean shouldFailUpdate = false;

        void addTask(Task task) {
            tasks.put(task.getId(), task);
        }

        Task getTask(String id) {
            return tasks.get(id);
        }

        void setShouldFailUpdate(boolean shouldFail) {
            this.shouldFailUpdate = shouldFail;
        }

        @Override
        public Task getTodayTaskById(String taskId) {
            return tasks.get(taskId);
        }

        @Override
        public boolean updateTaskCompletionStatus(String taskId, boolean isCompleted) {
            if (shouldFailUpdate) {
                return false;
            }
            Task task = tasks.get(taskId);
            if (task != null) {
                if (isCompleted) {
                    task.markComplete();
                } else {
                    task.unmarkComplete();
                }
                return true;
            }
            return false;
        }
    }

    /**
     * Test presenter implementation
     */
    private static class TestMarkTaskCompletePresenter implements MarkTaskCompleteOutputBoundary {
        MarkTaskCompleteOutputData lastOutputData;
        String lastError;
        String lastMessage;

        @Override
        public void presentSuccess(MarkTaskCompleteOutputData outputData, String message) {
            this.lastOutputData = outputData;
            this.lastMessage = message;
            this.lastError = null;
        }

        @Override
        public void presentError(String error) {
            this.lastError = error;
            this.lastOutputData = null;
            this.lastMessage = null;
        }
    }

    /**
     * Test goal repository implementation
     */
    private static class TestGoalRepository implements GoalRepository {
        private final List<Goal> goals = new ArrayList<>();
        private final List<Goal> currentGoals = new ArrayList<>();
        private Goal todayGoal = null;
        private boolean shouldThrowException = false;
        private Goal nullInfoGoal = null;

        void addGoal(Goal goal) {
            goals.add(goal);
        }

        Goal getGoalById(String id) {
            return goals.stream()
                .filter(g -> g.getGoalInfo().getInfo().getId().equals(id))
                .findFirst()
                .orElse(null);
        }

        void setShouldThrowException(boolean shouldThrow) {
            this.shouldThrowException = shouldThrow;
        }

        void addNullInfoGoal() {
            // Add a goal that will return null for getGoalInfo()
            nullInfoGoal = new Goal(
                new GoalInfo(new Info.Builder("Null Test").build(), new Info.Builder("Target").build()),
                new BeginAndDueDates(LocalDate.now(), LocalDate.now().plusDays(7)),
                TimePeriod.WEEK, 3) {
                @Override
                public GoalInfo getGoalInfo() {
                    return null;
                }
            };
        }

        @Override
        public void save(Goal goal) {
            if (shouldThrowException) {
                throw new RuntimeException("Save failed");
            }
            // Update existing goal or add new one
            goals.removeIf(g -> g.getGoalInfo() != null && 
                          g.getGoalInfo().getInfo().getId().equals(goal.getGoalInfo().getInfo().getId()));
            goals.add(goal);
        }

        @Override
        public java.util.Optional<Goal> findByName(String name) {
            return goals.stream()
                .filter(g -> g.getGoalInfo() != null && 
                       g.getGoalInfo().getInfo().getName().equals(name))
                .findFirst();
        }

        @Override
        public void deleteByName(String name) {
            goals.removeIf(g -> g.getGoalInfo() != null && 
                          g.getGoalInfo().getInfo().getName().equals(name));
        }

        @Override
        public List<Goal> getAllGoals() {
            return new ArrayList<>(goals);
        }

        @Override
        public List<Goal> getCurrentGoals() {
            return new ArrayList<>(currentGoals);
        }

        @Override
        public void addToCurrentGoals(Goal goal) {
            currentGoals.add(goal);
        }

        @Override
        public void removeFromCurrentGoals(Goal goal) {
            currentGoals.remove(goal);
        }

        @Override
        public boolean isInCurrentGoals(Goal goal) {
            return currentGoals.contains(goal);
        }

        @Override
        public boolean existsByName(String name) {
            return goals.stream()
                .anyMatch(g -> g.getGoalInfo() != null && 
                         g.getGoalInfo().getInfo().getName().equals(name));
        }

        @Override
        public List<Goal> findByTargetAmountRange(double minAmount, double maxAmount) {
            // Not relevant for our test, return empty list
            return new ArrayList<>();
        }

        @Override
        public List<Goal> findAvailableGoals() {
            return new ArrayList<>(goals);
        }

        @Override
        public Goal getTodayGoal() {
            return todayGoal;
        }

        @Override
        public void saveTodayGoal(Goal goal) {
            this.todayGoal = goal;
        }

        @Override
        public void removeGoal(String goalName) {
            goals.removeIf(g -> g.getGoalInfo() != null && 
                          g.getGoalInfo().getInfo().getName().equals(goalName));
        }

        @Override
        public void addGoalToToday(String goalName) {
            findByName(goalName).ifPresent(goal -> todayGoal = goal);
        }

        @Override
        public void removeGoalFromToday(String goalName) {
            if (todayGoal != null && todayGoal.getGoalInfo() != null &&
                todayGoal.getGoalInfo().getInfo().getName().equals(goalName)) {
                todayGoal = null;
            }
        }

        @Override
        public void updateTodayGoalProgress(String goalName, int newProgress) {
            if (todayGoal != null && todayGoal.getGoalInfo() != null &&
                todayGoal.getGoalInfo().getInfo().getName().equals(goalName)) {
                todayGoal.setCurrentProgress(newProgress);
            }
        }

        @Override
        public List<Goal> getTodayGoals() {
            if (shouldThrowException) {
                throw new RuntimeException("Database error");
            }
            List<Goal> todayGoals = new ArrayList<>(goals);
            if (nullInfoGoal != null) {
                todayGoals.add(nullInfoGoal);
            }
            return todayGoals;
        }

        @Override
        public List<Goal> loadGoals() {
            return new ArrayList<>(goals);
        }

        @Override
        public List<Goal> loadCurrentGoals() {
            return new ArrayList<>(currentGoals);
        }

        @Override
        public void saveGoals() {
            // Test implementation - no-op
        }

        @Override
        public void saveCurrentGoals() {
            // Test implementation - no-op
        }
    }
}