package use_case;

/**
 * Output boundary interface for marking task complete.
 */
public interface MarkTaskCompleteOutputBoundary {
    void presentSuccess(MarkTaskCompleteOutputData outputData);
    void presentError(String error);
}
