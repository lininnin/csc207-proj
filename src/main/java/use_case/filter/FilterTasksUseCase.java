package use_case.filter;

import entity.BeginAndDueDates.BeginAndDueDates;
import entity.Info.Info;
import entity.Angela.Task.Task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Use case for filtering tasks by category and priority.
 * Contains inner classes for input and output data.
 */
public class FilterTasksUseCase {

    /**
     * Input data holding filter criteria for category and priority.
     */
    public static class FilterTasksInputData {
        private final String category;
        private final String priority;

        public FilterTasksInputData(String category, String priority) {
            this.category = category;
            this.priority = priority;
        }

        public String getCategory() {
            return category;
        }

        public String getPriority() {
            return priority;
        }
    }

    /**
     * Output data holding filtered task list.
     */
    public static class FilterTasksOutputData {
        private final List<Task> filteredTasks;

        public FilterTasksOutputData(List<Task> filteredTasks) {
            this.filteredTasks = filteredTasks;
        }

        public List<Task> getFilteredTasks() {
            return filteredTasks;
        }
    }

    /**
     * Main filtering logic.
     *
     * @param allTasks List of all tasks
     * @param inputData Filter criteria (category, priority)
     * @return Filtered list wrapped in output data
     */
    public FilterTasksOutputData execute(List<Task> allTasks, FilterTasksInputData inputData) {
        // Validate priority
        if (inputData.getPriority() != null) {
            try {
                Task.Priority.valueOf(inputData.getPriority().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid priority: " + inputData.getPriority());
            }
        }

        List<Task> filtered = allTasks.stream()
                .filter(task -> inputData.getCategory() == null
                        || task.getInfo().getCategory().equalsIgnoreCase(inputData.getCategory()))
                .filter(task -> inputData.getPriority() == null
                        || task.getTaskPriority().name().equalsIgnoreCase(inputData.getPriority()))
                .collect(Collectors.toList());

        return new FilterTasksOutputData(filtered);
    }



    /*
     * Todo: Below is an example run feel free to paste in the main
     *  Things to do: show in comments
     */
    public static void exampleFilterRun() {
        // 1. Create sample tasks
        List <Task>allTasks = new ArrayList<>(); //can be replaced with input tasks use with dailytasksummary

        allTasks.add(new Task(
                new Info("1", "Gym Workout", "work"),
                new BeginAndDueDates(LocalDate.now(), LocalDate.now().plusDays(1)),
                Task.Priority.HIGH));

        allTasks.add(new Task(
                new Info("2", "Submit Report", "Monthly status report"),
                new BeginAndDueDates(LocalDate.now(), LocalDate.now().plusDays(2)),
                Task.Priority.MEDIUM));

        allTasks.add(new Task(
                new Info("3", "Buy Groceries", "Personal"),
                new BeginAndDueDates(LocalDate.now(), LocalDate.now().plusDays(1)),
                Task.Priority.LOW));

        // 2. Get user input for filters
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter category to filter by (or press Enter to skip): ");
        String categoryInput = scanner.nextLine();
        String category = categoryInput.isBlank() ? null : categoryInput;

        System.out.print("Enter priority to filter by (LOW, MEDIUM, HIGH) (or press Enter to skip): ");
        String priorityInput = scanner.nextLine();
        String priority = priorityInput.isBlank() ? null : priorityInput.toUpperCase();

        // 3. Validate priority
        if (priority != null) {
            try {
                Task.Priority.valueOf(priority); // Validate enum
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid priority entered. Please use LOW, MEDIUM, or HIGH.");
                return;
            }
        }

        // 4. Run the use case
        FilterTasksUseCase useCase = new FilterTasksUseCase();
        FilterTasksInputData inputData = new FilterTasksInputData(category, priority);
        FilterTasksOutputData result = useCase.execute(allTasks, inputData);

        // 5. Print results
        List<Task> filtered = result.getFilteredTasks();
        System.out.println("\nFiltered tasks:");
        if (filtered.isEmpty()) {
            System.out.println("No tasks matched your filter.");
        } else {
            for (Task t : filtered) {
                System.out.println("- " + t.getInfo().getName() + " [" + t.getInfo().getCategory() + ", " + t.getTaskPriority() + "]");
            }
        }
    }
}

