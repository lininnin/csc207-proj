//package entityTest;
//
//import entity.Angela.Task.Task;
//import entity.Angela.Task.Task.Priority;
//import entity.info.Info;
//import entity.BeginAndDueDates.BeginAndDueDates;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class TaskTest {
//
//    private Info sampleInfo;
//    private BeginAndDueDates validDates;
//
//    @BeforeEach
//    public void setUp() {
//        sampleInfo = new Info.Builder("Test Task")
//                .description("This is a test task.")
//                .category("Work")
//                .build();
//
//        validDates = new BeginAndDueDates(
//                LocalDate.of(2025, 7, 20),
//                LocalDate.of(2025, 7, 25)
//        );
//    }
//
//    @Test
//    public void testBuildValidTask() {
//        Task task = new Task.Builder(sampleInfo)
//                .priority(Priority.Medium)
//                .beginAndDueDates(validDates)
//                .build();
//
//        assertEquals(sampleInfo, task.getInfo());
//        assertEquals(Priority.Medium, task.getPriority());
//        assertEquals(validDates, task.getBeginAndDueDates());
//        assertFalse(task.getStatus());
//        assertNull(task.getCompletedDateTimes());
//    }
//
//    @Test
//    public void testEditInfoSuccessfully() {
//        Task task = createDefaultTask();
//        Info newInfo = new Info.Builder("New Task")
//                .description("Updated")
//                .category("Life")
//                .build();
//
//        task.editInfo(newInfo);
//        assertEquals(newInfo, task.getInfo());
//    }
//
//    @Test
//    public void testEditPrioritySuccessfully() {
//        Task task = createDefaultTask();
//        task.editPriority(Priority.High);
//        assertEquals(Priority.High, task.getPriority());
//    }
//
//    @Test
//    public void testEditStatusToCompleted() {
//        Task task = createDefaultTask();
//        task.editStatus(true);
//        assertTrue(task.getStatus());
//        assertNotNull(task.getCompletedDateTimes());
//    }
//
//    @Test
//    public void testEditStatusToUncompleted() {
//        Task task = createDefaultTask();
//        task.editStatus(true);
//        task.editStatus(false);
//        assertFalse(task.getStatus());
//        assertNull(task.getCompletedDateTimes());
//    }
//
//    @Test
//    public void testEditCompletedDateTime() {
//        Task task = createDefaultTask();
//        LocalDateTime now = LocalDateTime.now();
//        task.editCompletedDateTimes(now);
//        assertEquals(now, task.getCompletedDateTimes());
//    }
//
//    @Test
//    public void testEditDueDateSuccessfully() {
//        Task task = createDefaultTask();
//        LocalDate newDueDate = LocalDate.of(2025, 7, 30);
//        task.editDueDate(newDueDate);
//        assertEquals(newDueDate, task.getBeginAndDueDates().getDueDate());
//    }
//
//    @Test
//    public void testEditDueDateBeforeBeginDateThrows() {
//        Task task = createDefaultTask();
//        LocalDate invalidDueDate = LocalDate.of(2025, 7, 10); // before begin date
//        assertThrows(IllegalArgumentException.class, () -> task.editDueDate(invalidDueDate));
//    }
//
//    @Test
//    public void testNullInfoThrowsException() {
//        assertThrows(IllegalArgumentException.class, () -> new Task.Builder(null));
//    }
//
//    @Test
//    public void testNullPriorityThrowsInBuilder() {
//        Task.Builder builder = new Task.Builder(sampleInfo);
//        assertThrows(IllegalArgumentException.class, () -> builder.priority(null));
//    }
//
//    @Test
//    public void testNullBeginAndDueDatesThrowsInBuilder() {
//        Task.Builder builder = new Task.Builder(sampleInfo);
//        assertThrows(IllegalArgumentException.class, () -> builder.beginAndDueDates(null));
//    }
//
//    @Test
//    public void testBuildWithoutBeginAndDueDatesThrows() {
//        Task.Builder builder = new Task.Builder(sampleInfo);
//        assertThrows(IllegalStateException.class, builder::build);
//    }
//
//    // Helper method to create a default task instance
//    private Task createDefaultTask() {
//        return new Task.Builder(sampleInfo)
//                .priority(Priority.Low)
//                .beginAndDueDates(validDates)
//                .build();
//    }
//}
//
