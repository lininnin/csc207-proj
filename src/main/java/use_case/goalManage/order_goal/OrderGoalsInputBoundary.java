package use_case.goalManage.order_goal;

/**
 * Defines the input boundary for the order goals use case.
 * This interface is implemented by the interactor to execute the ordering logic.
 */
public interface OrderGoalsInputBoundary {
    /**
     * Executes the order goals use case with the provided input data.
     *
     * @param inputData The data required to order the goals, including the sorting criteria and order.
     */
    void execute(OrderGoalsInputData inputData);
}
