package interface_adapter.alex.WellnessLog_related.new_wellness_log;

import entity.Alex.MoodLabel.MoodLabel;
import entity.Alex.WellnessLogEntry.Levels;
import use_case.alex.wellness_log_related.add_wellnessLog.AddWellnessLogInputData;
import use_case.alex.wellness_log_related.add_wellnessLog.AddWellnessLogInputBoundary;

import java.time.LocalDateTime;

/**
 * Controller for the AddWellnessLog use case.
 * It packages the user input and passes it to the use case interactor.
 */
public class AddWellnessLogController {

    private final AddWellnessLogInputBoundary interactor;

    public AddWellnessLogController(AddWellnessLogInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Called by the UI to add a new wellness log.
     *
     * @param time         Timestamp of the log
     * @param stressLevel  Stress level (enum)
     * @param energyLevel  Energy level (enum)
     * @param fatigueLevel Fatigue level (enum)
     * @param moodLabel    Mood label
     * @param userNote     Optional user note
     */
    public void execute(LocalDateTime time,
                        Levels stressLevel,
                        Levels energyLevel,
                        Levels fatigueLevel,
                        MoodLabel moodLabel,
                        String userNote) {

        AddWellnessLogInputData inputData = new AddWellnessLogInputData(
                time,
                stressLevel,
                energyLevel,
                fatigueLevel,
                moodLabel,
                userNote
        );

        interactor.execute(inputData);
    }
}

