package entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AvailableTask entity.
 */
public class AvailableTaskTest {

    @Test
    public void testCreateRegularTask() {
        AvailableTask task = AvailableTask.createRegular("1", "Study", "Study for exam", "Academic");

        assertNotNull(task);
        assertNotNull(task.getInfo());
        assertEquals("1", task.getInfo().getId());
        assertEquals("Study", task.getInfo().getName());
        assertEquals("Study for exam", task.getInfo().getDescription());
        assertEquals("Academic", task.getInfo().getCategory());
        assertFalse(task.isOneTime());
    }

    @Test
    public void testCreateOneTimeTask() {
        AvailableTask task = AvailableTask.createOneTime("2", "Meeting", "Team standup", "Work");

        assertNotNull(task);
        assertTrue(task.isOneTime());
        assertEquals("Meeting", task.getInfo().getName());
    }

    @Test
    public void testNullInfoThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new AvailableTask(null, false)
        );
    }

    @Test
    public void testAddToToday() {
        AvailableTask availableTask = AvailableTask.createRegular("1", "Task", null, null);
        LocalDate dueDate = LocalDate.now().plusDays(3);

        TodaysTask todaysTask = availableTask.addToToday(TodaysTask.Priority.HIGH, dueDate);

        assertNotNull(todaysTask);
        assertEquals(availableTask, todaysTask.getAvailableTask());
        assertEquals(TodaysTask.Priority.HIGH, todaysTask.getPriority());
        assertEquals(dueDate, todaysTask.getBeginAndDueDates().getDueDate());
        assertEquals(LocalDate.now(), todaysTask.getBeginAndDueDates().getBeginDate());
    }

    @Test
    public void testAddToTodayWithNullPriority() {
        AvailableTask availableTask = AvailableTask.createRegular("1", "Task", null, null);

        TodaysTask todaysTask = availableTask.addToToday(null, null);

        // Should default to MEDIUM priority
        assertEquals(TodaysTask.Priority.MEDIUM, todaysTask.getPriority());
        assertNull(todaysTask.getBeginAndDueDates().getDueDate());
    }

    @Test
    public void testEqualsBasedOnInfo() {
        Info info1 = new Info("1", "Task", null, null, LocalDate.now());
        Info info2 = new Info("1", "Different", null, null, LocalDate.now());

        AvailableTask task1 = new AvailableTask(info1, false);
        AvailableTask task2 = new AvailableTask(info2, true);

        // Equal because same ID
        assertEquals(task1, task2);
        assertEquals(task1.hashCode(), task2.hashCode());
    }

    @Test
    public void testToString() {
        AvailableTask task = AvailableTask.createOneTime("1", "Task Name", null, null);

        String str = task.toString();
        assertTrue(str.contains("Task Name"));
        assertTrue(str.contains("oneTime=true"));
    }
}