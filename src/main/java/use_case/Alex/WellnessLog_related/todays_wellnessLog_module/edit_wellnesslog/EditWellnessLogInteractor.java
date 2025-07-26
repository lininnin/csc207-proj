package use_case.Alex.WellnessLog_related.todays_wellnessLog_module.edit_wellnesslog;

import entity.Alex.WellnessLogEntry.WellnessLogEntry;
import entity.Alex.WellnessLogEntry.Levels;
import entity.Alex.MoodLabel.MoodLabel;

/**
 * Interactor for the EditWellnessLog use case.
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
            // 使用封装对象设置各项值
            existing.setStressLevel(Levels.fromInt(inputData.getStressLevel()));
            existing.setEnergyLevel(Levels.fromInt(inputData.getEnergyLevel()));
            existing.setFatigueLevel(Levels.fromInt(inputData.getFatigueLevel()));
            existing.setMoodLabel(new MoodLabel.Builder(inputData.getMoodName())
                    .type(inputData.getMoodType())
                    .build());
            existing.setUserNote(inputData.getNote());

            boolean success = dataAccess.update(existing);
            if (success) {
                outputBoundary.prepareSuccessView(new EditWellnessLogOutputData(existing.getId(), true));
            } else {
                outputBoundary.prepareFailView("Failed to update the wellness log.");
            }

        } catch (Exception e) {
            outputBoundary.prepareFailView("Error while editing log: " + e.getMessage());
        }
    }
}


