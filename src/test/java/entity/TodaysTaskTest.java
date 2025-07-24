package entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TodaysTask entity.
 */
public class TodaysTaskTest {

    private AvailableTask availableTask;
    private BeginAndDueDates dates;

    @BeforeEach
    public void setUp() {
        availableTask = AvailableTask.createRegular("1", "Test Task", "Description", "Work");
        dates = new BeginAndDueDates(LocalDate.now(), LocalDate.now().plusDays(3));
    }

    @Test
    public void testCreateTodaysTask() {
        TodaysTask task = new TodaysTask(availableTask, TodaysTask.Priority.HIGH, dates);

        assertNotNull(task);
        assertEquals(availableTask, task.getAvailableTask());
        assertEquals(TodaysTask.Priority.HIGH, task.getPriority());
        assertEquals(dates, task.getBeginAndDueDates());
        assertFalse(task.isCompleted());
        assertNull(task.getCompletedDateTime());
    }

    @Test
    public void testDefaultPriority() {
        TodaysTask task = new TodaysTask(availableTask, null, dates);
        assertEquals(TodaysTask.Priority.MEDIUM, task.getPriority());
    }

    @Test
    public void testNullAvailableTaskThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new TodaysTask(null, TodaysTask.Priority.LOW, dates)
        );
    }

    @Test
    public void testNullDatesThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new TodaysTask(availableTask, TodaysTask.Priority.LOW, null)
        );
    }

    @Test
    public void testMarkComplete() {
        TodaysTask task = new TodaysTask(availableTask, TodaysTask.Priority.HIGH, dates);

        assertFalse(task.isCompleted());
        LocalDateTime beforeComplete = LocalDateTime.now();

        task.markComplete();

        assertTrue(task.isCompleted());
        assertNotNull(task.getCompletedDateTime());
        assertTrue(!task.getCompletedDateTime().isBefore(beforeComplete));
        assertTrue(!task.getCompletedDateTime().isAfter(LocalDateTime.now()));
    }

    @Test
    public void testMarkCompleteAlreadyCompleted() {
        TodaysTask task = new TodaysTask(availableTask, TodaysTask.Priority.HIGH, dates);
        task.markComplete();

        assertThrows(IllegalStateException.class, task::markComplete);
    }

    @Test
    public void testMarkIncomplete() {
        TodaysTask task = new TodaysTask(availableTask, TodaysTask.Priority.HIGH, dates);
        task.markComplete();
        LocalDateTime completedTime = task.getCompletedDateTime();

        task.markIncomplete();

        assertFalse(task.isCompleted());
        assertNull(task.getCompletedDateTime());
    }

    @Test
    public void testMarkIncompleteNotCompleted() {
        TodaysTask task = new TodaysTask(availableTask, TodaysTask.Priority.HIGH, dates);

        assertThrows(IllegalStateException.class, task::markIncomplete);
    }

    @Test
    public void testIsOverdueNotCompleted() {
        BeginAndDueDates pastDates = new BeginAndDueDates(
                LocalDate.now().minusDays(5),
                LocalDate.now().minusDays(2)
        );
        TodaysTask task = new TodaysTask(availableTask, TodaysTask.Priority.HIGH, pastDates);

        assertTrue(task.isOverdue());
    }

    @Test
    public void testIsOverdueCompleted() {
        BeginAndDueDates pastDates = new BeginAndDueDates(
                LocalDate.now().minusDays(5),
                LocalDate.now().minusDays(2)
        );
        TodaysTask task = new TodaysTask(availableTask, TodaysTask.Priority.HIGH, pastDates);
        task.markComplete();

        assertFalse(task.isOverdue());
    }

    @Test
    public void testIsOverdueNoDueDate() {
        BeginAndDueDates noDueDate = new BeginAndDueDates(LocalDate.now(), null);
        TodaysTask task = new TodaysTask(availableTask, TodaysTask.Priority.HIGH, noDueDate);

        assertFalse(task.isOverdue());
    }

    @Test
    public void testWithPriority() {
        TodaysTask original = new TodaysTask(availableTask, TodaysTask.Priority.LOW, dates);

        TodaysTask updated = original.withPriority(TodaysTask.Priority.HIGH);

        assertEquals(TodaysTask.Priority.LOW, original.getPriority());
        assertEquals(TodaysTask.Priority.HIGH, updated.getPriority());
        assertEquals(original.getAvailableTask(), updated.getAvailableTask());
        assertEquals(original.getBeginAndDueDates(), updated.getBeginAndDueDates());
    }

    @Test
    public void testWithDueDate() {
        TodaysTask original = new TodaysTask(availableTask, TodaysTask.Priority.LOW, dates);
        LocalDate newDueDate = LocalDate.now().plusDays(7);

        TodaysTask updated = original.withDueDate(newDueDate);

        assertEquals(dates.getDueDate(), original.getBeginAndDueDates().getDueDate());
        assertEquals(newDueDate, updated.getBeginAndDueDates().getDueDate());
        assertEquals(original.getBeginAndDueDates().getBeginDate(),
                updated.getBeginAndDueDates().getBeginDate());
    }

    @Test
    public void testGetInfo() {
        TodaysTask task = new TodaysTask(availableTask, TodaysTask.Priority.HIGH, dates);

        assertEquals(availableTask.getInfo(), task.getInfo());
    }
}