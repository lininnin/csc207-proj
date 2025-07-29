package data_access;

import entity.Angela.Task.Task;
import entity.BeginAndDueDates.BeginAndDueDates;
import entity.info.Info;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

/**
 * Unit tests for FileTaskRepository.
 */
class FileTaskRepositoryTest {
    private static final String TEST_DATA_DIR = "test-data";
    private static final String TEST_TASKS_FILE = "test-data/tasks.json";
    private FileTaskRepository repository;

    @BeforeEach
    void setUp() throws IOException {
        // Create test data directory
        Files.createDirectories(Paths.get(TEST_DATA_DIR));

        // Create repository with test file location
        System.setProperty("tasks.file", TEST_TASKS_FILE);
        repository = new TestFileTaskRepository();
    }

    @AfterEach
    void tearDown() throws IOException {
        // Clean up test files
        Path testDir = Paths.get(TEST_DATA_DIR);
        if (Files.exists(testDir)) {
            Files.walk(testDir)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
        System.clearProperty("tasks.file");
    }

    @Test
    @DisplayName("Should save and retrieve task")
    void testSaveAndRetrieveTask() {
        // Create a task
        Info info = new Info("task-1", "Test Task", "Description", "Work", LocalDate.now());
        BeginAndDueDates dates = new BeginAndDueDates(LocalDate.now(), LocalDate.now().plusDays(7));
        Task task = new Task(info, dates);

        // Save task
        repository.save(task);

        // Retrieve task
        Task retrieved = repository.findById("task-1");
        assertNotNull(retrieved);
        assertEquals("Test Task", retrieved.getInfo().getName());
        assertEquals("Description", retrieved.getInfo().getDescription());
        assertEquals("Work", retrieved.getInfo().getCategory());
    }

    @Test
    @DisplayName("Should update existing task")
    void testUpdateTask() {
        // Create and save task
        Info info = new Info("task-1", "Original", "Desc", "Work", LocalDate.now());
        BeginAndDueDates dates = new BeginAndDueDates(LocalDate.now(), null);
        Task task = new Task(info, dates);
        repository.save(task);

        // Update task
        task.setPriority(Task.Priority.HIGH);
        task.editStatus(true);
        repository.update(task);

        // Verify update
        Task retrieved = repository.findById("task-1");
        assertEquals(Task.Priority.HIGH, retrieved.getPriority());
        assertTrue(retrieved.isComplete());
    }

    @Test
    @DisplayName("Should find all tasks")
    void testFindAllTasks() {
        // Save multiple tasks
        for (int i = 1; i <= 3; i++) {
            Info info = new Info("task-" + i, "Task " + i, "Desc", "Work", LocalDate.now());
            BeginAndDueDates dates = new BeginAndDueDates(LocalDate.now(), null);
            Task task = new Task(info, dates);
            repository.save(task);
        }

        List<Task> allTasks = repository.findAll();
        assertEquals(3, allTasks.size());
    }

    @Test
    @DisplayName("Should handle today's tasks correctly")
    void testTodaysTasks() {
        // Create task and add to today
        Info info = new Info("task-1", "Today Task", "Desc", "Work", LocalDate.now());
        BeginAndDueDates dates = new BeginAndDueDates(LocalDate.now(), null);
        Task task = new Task(info, dates);
        task.setPriority(Task.Priority.MEDIUM);

        repository.save(task);
        repository.addToTodaysTasks(task);

        List<Task> todaysTasks = repository.findTodaysTasks();
        assertEquals(1, todaysTasks.size());
        assertEquals("Today Task", todaysTasks.get(0).getInfo().getName());
    }

    @Test
    @DisplayName("Should include tasks with due dates in today's tasks")
    void testDueDateTasksInToday() {
        // Task with due date (should appear in today automatically)
        Info info1 = new Info("task-1", "Due Today", "Desc", "Work", LocalDate.now());
        BeginAndDueDates dates1 = new BeginAndDueDates(
                LocalDate.now().minusDays(2),
                LocalDate.now().plusDays(3)
        );
        Task dueTask = new Task(info1, dates1);
        repository.save(dueTask);

        // Task without due date (not in today unless explicitly added)
        Info info2 = new Info("task-2", "No Due", "Desc", "Personal", LocalDate.now());
        BeginAndDueDates dates2 = new BeginAndDueDates(LocalDate.now(), null);
        Task noDueTask = new Task(info2, dates2);
        repository.save(noDueTask);

        List<Task> todaysTasks = repository.findTodaysTasks();
        assertEquals(1, todaysTasks.size());
        assertEquals("Due Today", todaysTasks.get(0).getInfo().getName());
    }

    @Test
    @DisplayName("Should find overdue tasks")
    void testFindOverdueTasks() {
        // Overdue task
        Info info1 = new Info("task-1", "Overdue", "Desc", "Work", LocalDate.now());
        BeginAndDueDates dates1 = new BeginAndDueDates(
                LocalDate.now().minusDays(5),
                LocalDate.now().minusDays(2) // Due 2 days ago
        );
        Task overdueTask = new Task(info1, dates1);
        repository.save(overdueTask);

        // Not overdue task
        Info info2 = new Info("task-2", "Future", "Desc", "Work", LocalDate.now());
        BeginAndDueDates dates2 = new BeginAndDueDates(
                LocalDate.now(),
                LocalDate.now().plusDays(5)
        );
        Task futureTask = new Task(info2, dates2);
        repository.save(futureTask);

        List<Task> overdueTasks = repository.findOverdueTasks();
        assertEquals(1, overdueTasks.size());
        assertEquals("Overdue", overdueTasks.get(0).getInfo().getName());
    }

    @Test
    @DisplayName("Should find tasks by category")
    void testFindByCategory() {
        // Save tasks with different categories
        String[] categories = {"Work", "Personal", "Work"};
        for (int i = 0; i < categories.length; i++) {
            Info info = new Info("task-" + i, "Task " + i, "Desc", categories[i], LocalDate.now());
            BeginAndDueDates dates = new BeginAndDueDates(LocalDate.now(), null);
            Task task = new Task(info, dates);
            repository.save(task);
        }

        List<Task> workTasks = repository.findByCategory("Work");
        assertEquals(2, workTasks.size());

        List<Task> personalTasks = repository.findByCategory("Personal");
        assertEquals(1, personalTasks.size());
    }

    @Test
    @DisplayName("Should find tasks by priority")
    void testFindByPriority() {
        // Save tasks with different priorities
        Task.Priority[] priorities = {Task.Priority.HIGH, Task.Priority.LOW, Task.Priority.HIGH};
        for (int i = 0; i < priorities.length; i++) {
            Info info = new Info("task-" + i, "Task " + i, "Desc", "Work", LocalDate.now());
            BeginAndDueDates dates = new BeginAndDueDates(LocalDate.now(), null);
            Task task = new Task(info, dates);
            task.setPriority(priorities[i]);
            repository.save(task);
            repository.addToTodaysTasks(task); // Need to be in today to have priority
        }

        List<Task> highPriorityTasks = repository.findByPriority(Task.Priority.HIGH);
        assertEquals(2, highPriorityTasks.size());

        List<Task> lowPriorityTasks = repository.findByPriority(Task.Priority.LOW);
        assertEquals(1, lowPriorityTasks.size());
    }

    @Test
    @DisplayName("Should remove completed one-time tasks")
    void testRemoveCompletedOneTimeTasks() {
        // Create one-time task
        Info info1 = new Info("task-1", "One Time", "Desc", "Work", LocalDate.now());
        BeginAndDueDates dates1 = new BeginAndDueDates(LocalDate.now(), null);
        Task oneTimeTask = new Task(info1, dates1);
        oneTimeTask.setOneTime(true);
        oneTimeTask.editStatus(true); // Mark as complete
        repository.save(oneTimeTask);

        // Create regular completed task
        Info info2 = new Info("task-2", "Regular", "Desc", "Work", LocalDate.now());
        BeginAndDueDates dates2 = new BeginAndDueDates(LocalDate.now(), null);
        Task regularTask = new Task(info2, dates2);
        regularTask.setOneTime(false);
        regularTask.editStatus(true); // Mark as complete
        repository.save(regularTask);

        assertEquals(2, repository.findAll().size());

        // Remove completed one-time tasks
        repository.removeCompletedOneTimeTasks();

        List<Task> remaining = repository.findAll();
        assertEquals(1, remaining.size());
        assertEquals("Regular", remaining.get(0).getInfo().getName());
    }

    @Test
    @DisplayName("Should clear today's tasks")
    void testClearTodaysTasks() {
        // Add tasks to today
        for (int i = 1; i <= 3; i++) {
            Info info = new Info("task-" + i, "Task " + i, "Desc", "Work", LocalDate.now());
            BeginAndDueDates dates = new BeginAndDueDates(LocalDate.now(), null);
            Task task = new Task(info, dates);
            repository.save(task);
            repository.addToTodaysTasks(task);
        }

        assertEquals(3, repository.findTodaysTasks().size());

        // Clear today's tasks
        repository.clearTodaysTasks();

        // Tasks with no due date should not be in today anymore
        assertEquals(0, repository.findTodaysTasks().size());

        // But all tasks still exist
        assertEquals(3, repository.findAll().size());
    }

    @Test
    @DisplayName("Should persist data across repository instances")
    void testDataPersistence() {
        // Save task with first repository
        Info info = new Info("task-1", "Persistent", "Desc", "Work", LocalDate.now());
        BeginAndDueDates dates = new BeginAndDueDates(LocalDate.now(), null);
        Task task = new Task(info, dates);
        repository.save(task);

        // Create new repository instance
        FileTaskRepository newRepository = new TestFileTaskRepository();

        // Should find the task
        Task retrieved = newRepository.findById("task-1");
        assertNotNull(retrieved);
        assertEquals("Persistent", retrieved.getInfo().getName());
    }

    // Test implementation that uses test file location
    private static class TestFileTaskRepository extends FileTaskRepository {
        @Override
        protected String getTasksFilePath() {
            return System.getProperty("tasks.file", TEST_TASKS_FILE);
        }
    }
}