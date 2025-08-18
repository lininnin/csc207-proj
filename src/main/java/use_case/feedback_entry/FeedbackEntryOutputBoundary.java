package use_case.feedback_entry;

public interface FeedbackEntryOutputBoundary {
    /**
     * Present the feedback entry retrieved to output layer.
     * @param response the response model containing feedback entry prepared by interactor
     */
    void present(FeedbackEntryResponseModel response);
}

