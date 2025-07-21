package use_case.orderTask;

import entity.Task;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class OrderTasksInteractor implements OrderTasksInputBoundary {
    private final OrderTasksOutputBoundary outputBoundary;
    private final List<Task> tasks;

    public OrderTasksInteractor(OrderTasksOutputBoundary outputBoundary, List<Task> tasks) {
        this.outputBoundary = outputBoundary;
        this.tasks = tasks;
    }

    @Override
    public void execute(OrderTasksInputData inputData) {
        try {
            Comparator<Task> comparator = getComparator(inputData.getSortBy());

            if (!inputData.isAscending()) {
                comparator = comparator.reversed();
            }

            List<Task> orderedTasks = tasks.stream()
                    .sorted(comparator)
                    .collect(Collectors.toList());

            outputBoundary.present(
                    new OrderTasksOutputData(orderedTasks, inputData.getSortBy())
            );
        } catch (Exception e) {
            outputBoundary.presentError("Failed to order tasks: " + e.getMessage());
        }
    }

    private Comparator<Task> getComparator(String sortBy) {
        switch (sortBy.toLowerCase()) {
            case "name":
                return Comparator.comparing(task -> task.getInfo().getName());
            case "priority":
                return Comparator.comparing(Task::getTaskPriority);
            case "duedate":
                return Comparator.comparing(task -> task.getBeginAndDueDates().getDueDate());
            case "completion":
                return Comparator.comparing(Task::isComplete);
            default:
                throw new IllegalArgumentException("Invalid sort criteria: " + sortBy);
        }
    }
}