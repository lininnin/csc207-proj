package use_case.generate_feedback;

public interface GenerateFeedbackOutputBoundary {
    /**
     * Presents generated feedback to output layer.
     * @param outputData data containing generated feedback
     *                   entry and anything for presenting information
     */
    void present(GenerateFeedbackOutputData outputData);
}
