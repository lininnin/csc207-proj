package use_case.generate_feedback;

public interface GenerateFeedbackInputBoundary {
    /**
     * Executes feedback generation process.
     * @param inputData input required for generating feedback
     */
    void execute(GenerateFeedbackInputData inputData);
}
