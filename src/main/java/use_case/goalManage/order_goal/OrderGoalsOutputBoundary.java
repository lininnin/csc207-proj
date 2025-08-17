package use_case.goalManage.order_goal;

/**
 * Defines the output boundary for the order goals use case.
 * This interface is implemented by the presenter to prepare and present the view.
 */
public interface OrderGoalsOutputBoundary {
    /**
     * Prepares and presents the view with the ordered goals data.
     *
     * @param outputData The data containing the list of ordered goals.
     */
    void present(OrderGoalsOutputData outputData);
}
