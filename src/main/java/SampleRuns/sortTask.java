package SampleRuns;

import entity.BeginAndDueDates;
import entity.Info;
import entity.Task;
import interface_adapter.controller.OrderTasksController;
import interface_adapter.presenter.OrderTasksPresenter;
import use_case.orderTask.OrderTasksInteractor;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class sortTask {
    public static void main(String[] args) {
        // 1. Create sample tasks
        List<Task> tasks = createSampleTasks();

        // 2. Create presenter
        OrderTasksPresenter presenter = new OrderTasksPresenter();

        // 3. Create interactor
        OrderTasksInteractor interactor = new OrderTasksInteractor(presenter, tasks);

        // 4. Create controller
        OrderTasksController controller = new OrderTasksController(interactor);

        // 5. Test different sorting scenarios
        System.out.println("=== TESTING TASK ORDERING ===");

        // Test case 1: Sort by name (ascending)
        System.out.println("\n1. Sorting by name (ascending):");
        controller.executeOrder("name", true);

        // Test case 2: Sort by priority (descending)
        System.out.println("\n2. Sorting by priority (descending):");
        controller.executeOrder("priority", false);

        // Test case 3: Sort by due date (ascending)
        System.out.println("\n3. Sorting by due date (ascending):");
        controller.executeOrder("duedate", true);

        // Test case 4: Sort by completion status (completed first)
        System.out.println("\n4. Sorting by completion status (completed first):");
        tasks.get(0).completeTask(LocalDate.now().atStartOfDay());
        controller.executeOrder("completion", false);

        // Test case 5: Invalid sort criteria
        System.out.println("\n5. Testing invalid sort criteria:");
        controller.executeOrder("invalid", true);
    }

    private static List<Task> createSampleTasks() {
        Info info1 = new Info("1", "Write report", "Work");
        Info info2 = new Info("2", "Buy groceries", "Personal");
        Info info3 = new Info("3", "Call client", "Work");

        BeginAndDueDates dates1 = new BeginAndDueDates(
                LocalDate.now(),
                LocalDate.now().plusDays(3));

        BeginAndDueDates dates2 = new BeginAndDueDates(
                LocalDate.now(),
                LocalDate.now().plusDays(1));

        BeginAndDueDates dates3 = new BeginAndDueDates(
                LocalDate.now(),
                LocalDate.now().plusDays(5));

        return Arrays.asList(
                new Task(info1, dates1, Task.Priority.MEDIUM),
                new Task(info2, dates2, Task.Priority.LOW),
                new Task(info3, dates3, Task.Priority.HIGH)
        );
    }
}