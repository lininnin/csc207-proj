package use_case.alex.wellness_log_related.todays_wellness_log_module.edit_wellnesslog;

import entity.alex.MoodLabel.MoodLabelFactoryInterf;
import entity.alex.MoodLabel.MoodLabelInterf;
import entity.alex.WellnessLogEntry.WellnessLogEntry;
import entity.alex.WellnessLogEntry.WellnessLogEntryFactoryInterf;
import entity.alex.WellnessLogEntry.Levels;
import entity.alex.WellnessLogEntry.WellnessLogEntryInterf;

/**
 * Interactor for the EditWellnessLog use case.
 * Reconstructs a new WellnessLogEntry instead of mutating the original one.
 */
public class EditWellnessLogInteractor implements EditWellnessLogInputBoundary {

    private final EditWellnessLogDataAccessInterf dataAccess;
    private final EditWellnessLogOutputBoundary outputBoundary;
    private final WellnessLogEntryFactoryInterf wellnessLogEntryFactory; // ✅ 使用接口
    private final MoodLabelFactoryInterf moodLabelFactory;               // ✅ MoodLabel 也依赖接口

    public EditWellnessLogInteractor(EditWellnessLogDataAccessInterf dataAccess,
                                     EditWellnessLogOutputBoundary outputBoundary,
                                     WellnessLogEntryFactoryInterf wellnessLogEntryFactory,
                                     MoodLabelFactoryInterf moodLabelFactory) {
        this.dataAccess = dataAccess;
        this.outputBoundary = outputBoundary;
        this.wellnessLogEntryFactory = wellnessLogEntryFactory;
        this.moodLabelFactory = moodLabelFactory;
    }

    @Override
    public void execute(EditWellnessLogInputData inputData) {
        WellnessLogEntryInterf existing = dataAccess.getById(inputData.getLogId());

        if (existing == null) {
            outputBoundary.prepareFailView("Wellness log not found for ID: " + inputData.getLogId());
            return;
        }

        try {
            MoodLabelInterf moodLabel = moodLabelFactory.create(
                    inputData.getMoodName(),
                    inputData.getMoodType()
            );

            WellnessLogEntryInterf updated = new WellnessLogEntry.Builder()
                    .id(existing.getId()) // ✅ 保留原 ID，确保 update() 成功
                    .time(existing.getTime())
                    .stressLevel(Levels.fromInt(inputData.getStressLevel()))
                    .energyLevel(Levels.fromInt(inputData.getEnergyLevel()))
                    .fatigueLevel(Levels.fromInt(inputData.getFatigueLevel()))
                    .moodLabel(moodLabel)
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
