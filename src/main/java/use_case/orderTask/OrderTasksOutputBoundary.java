package use_case.orderTask;

public interface OrderTasksOutputBoundary {
    void present(OrderTasksOutputData outputData);

    void presentError(String message);
}