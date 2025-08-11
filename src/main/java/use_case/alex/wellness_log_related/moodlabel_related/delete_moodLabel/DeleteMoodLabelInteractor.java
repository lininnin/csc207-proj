package use_case.alex.wellness_log_related.moodlabel_related.delete_moodLabel;

import entity.Alex.MoodLabel.MoodLabelInterf;

/**
 * Interactor for the DeleteMoodLabel use case.
 * Handles business logic for removing a mood label from the system.
 * Now fully decoupled from the concrete MoodLabel class by using the MoodLabelInterf interface.
 */
public class DeleteMoodLabelInteractor implements DeleteMoodLabelInputBoundary {

    private final DeleteMoodLabelDataAccessInterf dataAccess;
    private final DeleteMoodLabelOutputBoundary outputBoundary;

    public DeleteMoodLabelInteractor(DeleteMoodLabelDataAccessInterf dataAccess,
                                     DeleteMoodLabelOutputBoundary outputBoundary) {
        this.dataAccess = dataAccess;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(DeleteMoodLabelInputData inputData) {
        String name = inputData.getMoodLabelName();

        MoodLabelInterf targetLabel = dataAccess.getByName(name);
        if (targetLabel == null) {
            outputBoundary.prepareFailView(
                    new DeleteMoodLabelOutputData(name, "Mood label not found.")
            );
            return;
        }

        boolean success = dataAccess.remove(targetLabel);
        if (success) {
            outputBoundary.prepareSuccessView(
                    new DeleteMoodLabelOutputData(name)
            );
        } else {
            outputBoundary.prepareFailView(
                    new DeleteMoodLabelOutputData(name, "Failed to delete mood label.")
            );
        }
    }
}


