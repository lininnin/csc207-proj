package use_case.alex.WellnessLog_related.todays_wellnessLog_module.edit_wellnesslog;

import entity.Alex.WellnessLogEntry.WellnessLogEntry;
import entity.Alex.WellnessLogEntry.Levels;
import entity.Alex.MoodLabel.MoodLabel;

/**
 * Interactor for the EditWellnessLog use case.
 * Reconstructs a new WellnessLogEntry instead of mutating the original one.
 */
public class EditWellnessLogInteractor implements EditWellnessLogInputBoundary {

    private final EditWellnessLogDataAccessInterf dataAccess;
    private final EditWellnessLogOutputBoundary outputBoundary;

    public EditWellnessLogInteractor(EditWellnessLogDataAccessInterf dataAccess,
                                     EditWellnessLogOutputBoundary outputBoundary) {
        this.dataAccess = dataAccess;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(EditWellnessLogInputData inputData) {
        WellnessLogEntry existing = dataAccess.getById(inputData.getLogId());

        if (existing == null) {
            outputBoundary.prepareFailView("Wellness log not found for ID: " + inputData.getLogId());
            return;
        }

        try {
            WellnessLogEntry updated = WellnessLogEntry.Builder.from(existing)
                    .stressLevel(Levels.fromInt(inputData.getStressLevel()))
                    .energyLevel(Levels.fromInt(inputData.getEnergyLevel()))
                    .fatigueLevel(Levels.fromInt(inputData.getFatigueLevel()))
                    .moodLabel(new MoodLabel.Builder(inputData.getMoodName())
                            .type(inputData.getMoodType())
                            .build())
                    .userNote(inputData.getNote())
                    .build();

            boolean success = dataAccess.update(updated);
            if (success) {
                outputBoundary.prepareSuccessView(new EditWellnessLogOutputData(updated.getId(), true));
            } else {
                outputBoundary.prepareFailView("Failed to update the wellness log.");
            }

        } catch (Exception e) {
            outputBoundary.prepareFailView("Error while editing log: " + e.getMessage());
        }
    }
}



