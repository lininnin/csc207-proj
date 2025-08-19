package use_case.order_events;

/**
 * Defines the output boundary for the order events use case.
 * This interface is implemented by the presenter to prepare and present the view with the ordered event data.
 */
public interface OrderEventsOutputBoundary {
    /**
     * Prepares and presents the view with the ordered event data.
     *
     * @param outputData The data containing the list of ordered events and the criteria used.
     */
    void present(OrderEventsOutputData outputData);
}
