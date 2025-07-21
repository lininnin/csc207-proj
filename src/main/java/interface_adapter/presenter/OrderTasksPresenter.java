package interface_adapter.presenter;

import use_case.orderTask.OrderTasksOutputBoundary;
import use_case.orderTask.OrderTasksOutputData;

public class OrderTasksPresenter implements OrderTasksOutputBoundary {
    @Override
    public void present(OrderTasksOutputData outputData) {
        System.out.println("\nTasks ordered by " + outputData.getSortCriteria() + ":");
        outputData.getOrderedTasks().forEach(task ->
                System.out.println("- " + task.getInfo().getName() +
                        " [Priority: " + task.getTaskPriority() +
                        ", Due: " + task.getBeginAndDueDates().getDueDate() +
                        ", Completed: " + task.isComplete() + "]")
        );
    }

    @Override
    public void presentError(String message) {
        System.err.println("Order Error: " + message);
    }
}