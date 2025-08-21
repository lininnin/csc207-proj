package interface_adapter.Angela.task.add_to_today;

import entity.Angela.Task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.Angela.task.add_to_today.AddTaskToTodayInputBoundary;

import java.time.LocalDate;

import static org.mockito.Mockito.*;

class AddTaskToTodayControllerTest {

    private AddTaskToTodayInputBoundary mockInputBoundary;
    private AddTaskToTodayController controller;

    @BeforeEach
    void setUp() {
        mockInputBoundary = mock(AddTaskToTodayInputBoundary.class);
        controller = new AddTaskToTodayController(mockInputBoundary);
    }

    @Test
    void testExecute_validParameters_callsInputBoundary() {
        String taskId = "task123";
        Task.Priority priority = Task.Priority.HIGH;
        LocalDate dueDate = LocalDate.now().plusDays(1);

        controller.execute(taskId, priority, dueDate);

        verify(mockInputBoundary).execute(argThat(inputData ->
                inputData.getTaskId().equals(taskId) &&
                inputData.getPriority() == priority &&
                inputData.getDueDate().equals(dueDate)
        ));
    }

    @Test
    void testExecute_nullTaskId_callsInputBoundary() {
        String taskId = null;
        Task.Priority priority = Task.Priority.MEDIUM;
        LocalDate dueDate = LocalDate.now();

        controller.execute(taskId, priority, dueDate);

        verify(mockInputBoundary).execute(argThat(inputData ->
                inputData.getTaskId() == null &&
                inputData.getPriority() == priority &&
                inputData.getDueDate().equals(dueDate)
        ));
    }

    @Test
    void testExecute_emptyTaskId_callsInputBoundary() {
        String taskId = "";
        Task.Priority priority = Task.Priority.LOW;
        LocalDate dueDate = LocalDate.now().minusDays(1);

        controller.execute(taskId, priority, dueDate);

        verify(mockInputBoundary).execute(argThat(inputData ->
                inputData.getTaskId().equals(taskId) &&
                inputData.getPriority() == priority &&
                inputData.getDueDate().equals(dueDate)
        ));
    }

    @Test
    void testExecute_nullPriority_callsInputBoundary() {
        String taskId = "task123";
        Task.Priority priority = null;
        LocalDate dueDate = LocalDate.now();

        controller.execute(taskId, priority, dueDate);

        verify(mockInputBoundary).execute(argThat(inputData ->
                inputData.getTaskId().equals(taskId) &&
                inputData.getPriority() == null &&
                inputData.getDueDate().equals(dueDate)
        ));
    }

    @Test
    void testExecute_nullDueDate_callsInputBoundary() {
        String taskId = "task123";
        Task.Priority priority = Task.Priority.HIGH;
        LocalDate dueDate = null;

        controller.execute(taskId, priority, dueDate);

        verify(mockInputBoundary).execute(argThat(inputData ->
                inputData.getTaskId().equals(taskId) &&
                inputData.getPriority() == priority &&
                inputData.getDueDate() == null
        ));
    }

    @Test
    void testExecute_highPriority_callsInputBoundary() {
        String taskId = "task123";
        Task.Priority priority = Task.Priority.HIGH;
        LocalDate dueDate = LocalDate.now();

        controller.execute(taskId, priority, dueDate);

        verify(mockInputBoundary).execute(argThat(inputData ->
                inputData.getTaskId().equals(taskId) &&
                inputData.getPriority() == Task.Priority.HIGH &&
                inputData.getDueDate().equals(dueDate)
        ));
    }

    @Test
    void testExecute_mediumPriority_callsInputBoundary() {
        String taskId = "task123";
        Task.Priority priority = Task.Priority.MEDIUM;
        LocalDate dueDate = LocalDate.now();

        controller.execute(taskId, priority, dueDate);

        verify(mockInputBoundary).execute(argThat(inputData ->
                inputData.getTaskId().equals(taskId) &&
                inputData.getPriority() == Task.Priority.MEDIUM &&
                inputData.getDueDate().equals(dueDate)
        ));
    }

    @Test
    void testExecute_lowPriority_callsInputBoundary() {
        String taskId = "task123";
        Task.Priority priority = Task.Priority.LOW;
        LocalDate dueDate = LocalDate.now();

        controller.execute(taskId, priority, dueDate);

        verify(mockInputBoundary).execute(argThat(inputData ->
                inputData.getTaskId().equals(taskId) &&
                inputData.getPriority() == Task.Priority.LOW &&
                inputData.getDueDate().equals(dueDate)
        ));
    }

    @Test
    void testExecute_pastDueDate_callsInputBoundary() {
        String taskId = "task123";
        Task.Priority priority = Task.Priority.HIGH;
        LocalDate dueDate = LocalDate.now().minusDays(5);

        controller.execute(taskId, priority, dueDate);

        verify(mockInputBoundary).execute(argThat(inputData ->
                inputData.getTaskId().equals(taskId) &&
                inputData.getPriority() == priority &&
                inputData.getDueDate().equals(dueDate)
        ));
    }

    @Test
    void testExecute_futureDueDate_callsInputBoundary() {
        String taskId = "task123";
        Task.Priority priority = Task.Priority.LOW;
        LocalDate dueDate = LocalDate.now().plusDays(10);

        controller.execute(taskId, priority, dueDate);

        verify(mockInputBoundary).execute(argThat(inputData ->
                inputData.getTaskId().equals(taskId) &&
                inputData.getPriority() == priority &&
                inputData.getDueDate().equals(dueDate)
        ));
    }

    @Test
    void testExecuteForTestingOverdue_validParameters_callsInputBoundary() {
        String taskId = "task123";
        Task.Priority priority = Task.Priority.HIGH;
        LocalDate dueDate = LocalDate.now().minusDays(2);

        controller.executeForTestingOverdue(taskId, priority, dueDate);

        verify(mockInputBoundary).execute(argThat(inputData ->
                inputData.getTaskId().equals(taskId) &&
                inputData.getPriority() == priority &&
                inputData.getDueDate().equals(dueDate)
        ));
    }

    @Test
    void testExecuteForTestingOverdue_nullValues_callsInputBoundary() {
        String taskId = null;
        Task.Priority priority = null;
        LocalDate dueDate = null;

        controller.executeForTestingOverdue(taskId, priority, dueDate);

        verify(mockInputBoundary).execute(argThat(inputData ->
                inputData.getTaskId() == null &&
                inputData.getPriority() == null &&
                inputData.getDueDate() == null
        ));
    }

    @Test
    void testExecuteForTestingOverdue_specialCharacters_callsInputBoundary() {
        String taskId = "task@#$%123";
        Task.Priority priority = Task.Priority.MEDIUM;
        LocalDate dueDate = LocalDate.now().minusWeeks(1);

        controller.executeForTestingOverdue(taskId, priority, dueDate);

        verify(mockInputBoundary).execute(argThat(inputData ->
                inputData.getTaskId().equals(taskId) &&
                inputData.getPriority() == priority &&
                inputData.getDueDate().equals(dueDate)
        ));
    }
}