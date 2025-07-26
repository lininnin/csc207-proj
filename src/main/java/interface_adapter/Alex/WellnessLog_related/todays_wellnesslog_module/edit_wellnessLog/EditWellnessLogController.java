package interface_adapter.Alex.WellnessLog_related.todays_wellnesslog_module.edit_wellnessLog;

import entity.Alex.MoodLabel.MoodLabel;
import use_case.Alex.WellnessLog_related.todays_wellnessLog_module.edit_wellnesslog.EditWellnessLogInputBoundary;
import use_case.Alex.WellnessLog_related.todays_wellnessLog_module.edit_wellnesslog.EditWellnessLogInputData;

/**
 * Controller for the EditWellnessLog use case.
 * Receives user input from the View and passes it to the interactor.
 */
public class EditWellnessLogController {

    private final EditWellnessLogInputBoundary interactor;

    public EditWellnessLogController(EditWellnessLogInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Called by the View to submit an edit request.
     *
     * @param logId        ID of the wellness log to edit
     * @param energyLevel  New energy level (1-10)
     * @param stressLevel  New stress level (1-10)
     * @param fatigueLevel New fatigue level (1-10)
     * @param moodName     Name of the mood (e.g., "Happy")
     * @param moodType     Type of the mood (Positive/Negative)
     * @param note         Optional user note
     */
    public void execute(String logId,
                        int energyLevel,
                        int stressLevel,
                        int fatigueLevel,
                        String moodName,
                        MoodLabel.Type moodType,
                        String note) {

        EditWellnessLogInputData inputData = new EditWellnessLogInputData(
                logId,
                energyLevel,
                stressLevel,
                fatigueLevel,
                moodName,
                moodType,
                note
        );

        interactor.execute(inputData);
    }
}

