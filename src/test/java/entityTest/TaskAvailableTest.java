package entityTest;

import entity.info.Info;
import entity.Angela.TaskAvailable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaskAvailableTest {

    private TaskAvailable taskAvailable;
    private Info info1;
    private Info info2;
    private Info info3;

    @BeforeEach
    public void setUp() {
        taskAvailable = new TaskAvailable();

        info1 = new Info.Builder("Write Report")
                .description("Write the weekly report")
                .category("Work")
                .build();

        info2 = new Info.Builder("Exercise")
                .description("Morning workout session")
                .category("Health")
                .build();

        info3 = new Info.Builder("Buy Groceries")
                .description("Get ingredients for dinner")
                .category("Personal")
                .build();
    }

    @Test
    public void testAddTaskSuccessfully() {
        taskAvailable.addTask(info1);
        assertEquals(1, taskAvailable.getTaskCount());
        assertTrue(taskAvailable.contains(info1));
    }

    @Test
    public void testAddNullTaskThrows() {
        assertThrows(IllegalArgumentException.class, () -> taskAvailable.addTask(null));
    }

    @Test
    public void testRemoveTaskSuccessfully() {
        taskAvailable.addTask(info1);
        assertTrue(taskAvailable.removeTask(info1));
        assertFalse(taskAvailable.contains(info1));
    }

    @Test
    public void testRemoveNonExistentTaskReturnsFalse() {
        assertFalse(taskAvailable.removeTask(info2));
    }

    @Test
    public void testGetTaskAvailableReturnsCopy() {
        taskAvailable.addTask(info1);
        List<Info> result = taskAvailable.getTaskAvailable();

        assertEquals(1, result.size());
        result.clear();  // modify copy
        assertEquals(1, taskAvailable.getTaskCount()); // original unchanged
    }

    @Test
    public void testGetTasksByCategory() {
        taskAvailable.addTask(info1);
        taskAvailable.addTask(info2);
        taskAvailable.addTask(info3);

        List<Info> workTasks = taskAvailable.getTasksByCategory("Work");
        List<Info> personalTasks = taskAvailable.getTasksByCategory("Personal");

        assertEquals(1, workTasks.size());
        assertTrue(workTasks.contains(info1));
        assertEquals(1, personalTasks.size());
        assertTrue(personalTasks.contains(info3));
    }

    @Test
    public void testGetTasksByName() {
        taskAvailable.addTask(info1);
        taskAvailable.addTask(info2);

        List<Info> result = taskAvailable.getTasksByName("Exercise");
        assertEquals(1, result.size());
        assertEquals("Exercise", result.get(0).getName());
    }

    @Test
    public void testContainsReturnsTrueIfExists() {
        taskAvailable.addTask(info3);
        assertTrue(taskAvailable.contains(info3));
    }

    @Test
    public void testContainsReturnsFalseIfNotExists() {
        assertFalse(taskAvailable.contains(info1));
    }

    @Test
    public void testClearAllTasks() {
        taskAvailable.addTask(info1);
        taskAvailable.addTask(info2);
        taskAvailable.clearAll();
        assertEquals(0, taskAvailable.getTaskCount());
        assertFalse(taskAvailable.contains(info1));
    }

    @Test
    public void testGetTaskCountAccurate() {
        assertEquals(0, taskAvailable.getTaskCount());
        taskAvailable.addTask(info1);
        taskAvailable.addTask(info2);
        assertEquals(2, taskAvailable.getTaskCount());
    }
}

