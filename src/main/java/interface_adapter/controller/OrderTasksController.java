package interface_adapter.controller;

import use_case.orderTask.OrderTasksInputBoundary;
import use_case.orderTask.OrderTasksInputData;

public class OrderTasksController {
    private final OrderTasksInputBoundary interactor;

    public OrderTasksController(OrderTasksInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void executeOrder(String sortBy, boolean ascending) {
        interactor.execute(new OrderTasksInputData(ascending, sortBy));
    }
}