package data_access;

import entity.AvailableTask;
import entity.BeginAndDueDates;
import entity.TodaysTask;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for FileTaskDataAccessObject.
 * Tests file persistence and data integrity.
 */
public class FileTaskDataAccessObjectTest {

    private FileTaskDataAccessObject dao;
    private static final String TEST_AVAILABLE_FILE = "test-available-tasks.json";
    private static final String TEST_TODAYS_FILE = "test-todays-tasks.json";

    @BeforeEach
    public void setUp() {
        // Clean up any existing test files
        deleteTestFiles();

        // Create DAO (would need to modify constructor to accept file names for testing)
        // For now, we'll use the default files
        dao = new FileTaskDataAccessObject();
    }

    @AfterEach
    public void tearDown() {
        deleteTestFiles();
    }

    @Test
    public void testSaveAndFindAvailableTask() {
        AvailableTask task = AvailableTask.createRegular("1", "Test Task", "Description", "Work");

        dao.saveAvailableTask(task);

        AvailableTask found = dao.findAvailableTaskById("1");
        assertNotNull(found);
        assertEquals("Test Task", found.getInfo().getName());
        assertEquals("Description", found.getInfo().getDescription());
        assertEquals("Work", found.getInfo().getCategory());
    }

    @Test
    public void testSaveDuplicateIdThrows() {
        AvailableTask task1 = AvailableTask.createRegular("1", "Task 1", null, null);
        AvailableTask task2 = AvailableTask.createRegular("1", "Task 2", null, null);

        dao.saveAvailableTask(task1);

        assertThrows(IllegalArgumentException.class, () ->
                dao.saveAvailableTask(task2)
        );
    }

    @Test
    public void testUpdateAvailableTask() {
        AvailableTask original = AvailableTask.createRegular("1", "Original", null, null);
        dao.saveAvailableTask(original);

        AvailableTask updated = AvailableTask.createOneTime("1", "Updated", "New Desc", "New Cat");
        dao.updateAvailableTask(updated);

        AvailableTask found = dao.findAvailableTaskById("1");
        assertEquals("Updated", found.getInfo().getName());
        assertTrue(found.isOneTime());
    }

    @Test
    public void testDeleteAvailableTask() {
        AvailableTask task = AvailableTask.createRegular("1", "To Delete", null, null);
        dao.saveAvailableTask(task);

        assertTrue(dao.deleteAvailableTask("1"));
        assertNull(dao.findAvailableTaskById("1"));
        assertFalse(dao.deleteAvailableTask("1")); // Already deleted
    }

    @Test
    public void testAvailableTaskNameExists() {
        AvailableTask task = AvailableTask.createRegular("1", "Unique Name", null, null);
        dao.saveAvailableTask(task);

        assertTrue(dao.availableTaskNameExists("Unique Name"));
        assertFalse(dao.availableTaskNameExists("Other Name"));
    }

    @Test
    public void testTodaysTaskOperations() {
        // Create available task first
        AvailableTask available = AvailableTask.createRegular("1", "Task", null, "Work");
        dao.saveAvailableTask(available);

        // Create today's task
        BeginAndDueDates dates = BeginAndDueDates.startingToday(LocalDate.now().plusDays(2));
        TodaysTask todaysTask = new TodaysTask(available, TodaysTask.Priority.HIGH, dates);

        dao.addTodaysTask(todaysTask);

        // Verify
        assertTrue(dao.isInTodaysList("1"));
        List<TodaysTask> todaysTasks = dao.findAllTodaysTasks();
        assertEquals(1, todaysTasks.size());
        assertEquals(TodaysTask.Priority.HIGH, todaysTasks.get(0).getPriority());
    }

    @Test
    public void testFindTodaysTasksByStatus() {
        AvailableTask available1 = AvailableTask.createRegular("1", "Task 1", null, null);
        AvailableTask available2 = AvailableTask.createRegular("2", "Task 2", null, null);
        dao.saveAvailableTask(available1);
        dao.saveAvailableTask(available2);

        TodaysTask task1 = new TodaysTask(available1, TodaysTask.Priority.HIGH,
                BeginAndDueDates.startingToday(null));
        TodaysTask task2 = new TodaysTask(available2, TodaysTask.Priority.LOW,
                BeginAndDueDates.startingToday(null));

        dao.addTodaysTask(task1);
        dao.addTodaysTask(task2);

        // Mark one as complete
        task1.markComplete();
        dao.updateTodaysTask(task1);

        List<TodaysTask> completed = dao.findTodaysTasksByStatus(true);
        List<TodaysTask> incomplete = dao.findTodaysTasksByStatus(false);

        assertEquals(1, completed.size());
        assertEquals(1, incomplete.size());
        assertTrue(completed.get(0).isCompleted());
        assertFalse(incomplete.get(0).isCompleted());
    }

    @Test
    public void testFindOverdueTasks() {
        AvailableTask available = AvailableTask.createRegular("1", "Overdue Task", null, null);
        dao.saveAvailableTask(available);

        // Create overdue task
        BeginAndDueDates pastDates = new BeginAndDueDates(
                LocalDate.now().minusDays(5),
                LocalDate.now().minusDays(2)
        );
        TodaysTask overdueTask = new TodaysTask(available, TodaysTask.Priority.HIGH, pastDates);
        dao.addTodaysTask(overdueTask);

        List<TodaysTask> overdueTasks = dao.findOverdueTasks();
        assertEquals(1, overdueTasks.size());
        assertTrue(overdueTasks.get(0).isOverdue());
    }

    @Test
    public void testClearTodaysTasks() {
        AvailableTask available = AvailableTask.createRegular("1", "Task", null, null);
        dao.saveAvailableTask(available);

        TodaysTask todaysTask = new TodaysTask(available, TodaysTask.Priority.MEDIUM,
                BeginAndDueDates.startingToday(null));
        dao.addTodaysTask(todaysTask);

        assertEquals(1, dao.findAllTodaysTasks().size());

        dao.clearTodaysTasks();

        assertEquals(0, dao.findAllTodaysTasks().size());
    }

    @Test
    public void testRemoveOneTimeTasks() {
        AvailableTask regular = AvailableTask.createRegular("1", "Regular", null, null);
        AvailableTask oneTime1 = AvailableTask.createOneTime("2", "OneTime1", null, null);
        AvailableTask oneTime2 = AvailableTask.createOneTime("3", "OneTime2", null, null);

        dao.saveAvailableTask(regular);
        dao.saveAvailableTask(oneTime1);
        dao.saveAvailableTask(oneTime2);

        assertEquals(3, dao.findAllAvailableTasks().size());

        dao.removeOneTimeTasks(Arrays.asList("2", "3"));

        assertEquals(1, dao.findAllAvailableTasks().size());
        assertNotNull(dao.findAvailableTaskById("1"));
        assertNull(dao.findAvailableTaskById("2"));
        assertNull(dao.findAvailableTaskById("3"));
    }

    private void deleteTestFiles() {
        new File("available-tasks.json").delete();
        new File("todays-tasks.json").delete();
        new File(TEST_AVAILABLE_FILE).delete();
        new File(TEST_TODAYS_FILE).delete();
    }
}