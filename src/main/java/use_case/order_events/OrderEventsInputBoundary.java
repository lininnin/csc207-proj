package use_case.order_events;

/**
 * Defines the input boundary for the order events use case.
 * This interface is implemented by the interactor to execute the event ordering logic.
 */
public interface OrderEventsInputBoundary {
    /**
     * Executes the order events use case with the provided input data.
     *
     * @param inputData The data required to order the events, including the sorting criteria, order, and scope.
     */
    void execute(OrderEventsInputData inputData);
}
