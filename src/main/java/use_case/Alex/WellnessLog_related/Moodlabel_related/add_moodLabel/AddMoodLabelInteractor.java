package use_case.Alex.WellnessLog_related.Moodlabel_related.add_moodLabel;

import entity.Alex.MoodLabel.MoodLabel;
import entity.Alex.MoodLabel.MoodLabelFactoryInterf;
import entity.Alex.MoodLabel.MoodLabelInterf;

import java.util.List;

/**
 * Interactor for the AddMoodLabel use case.
 * Implements the business logic for adding a new mood label.
 */
public class AddMoodLabelInteractor implements AddMoodLabelInputBoundary {

    private final AddMoodLabelDataAccessInterf dataAccess;
    private final AddMoodLabelOutputBoundary outputBoundary;
    private final MoodLabelFactoryInterf factory;

    public AddMoodLabelInteractor(AddMoodLabelDataAccessInterf dataAccess,
                                  AddMoodLabelOutputBoundary outputBoundary,
                                  MoodLabelFactoryInterf factory) {
        this.dataAccess = dataAccess;
        this.outputBoundary = outputBoundary;
        this.factory = factory;
    }

    @Override
    public void execute(AddMoodLabelInputData inputData) {
        String name = inputData.getMoodName();
        String type = inputData.getMoodType();

        // Check for empty fields
        if (name == null || name.trim().isEmpty()) {
            outputBoundary.prepareFailView("Mood label name cannot be empty.");
            return;
        }

        if (!(type.equals("Positive") || type.equals("Negative"))) {
            outputBoundary.prepareFailView("Mood label type must be 'Positive' or 'Negative'.");
            return;
        }

        // Check for duplicate name
        List<MoodLabel> existingLabels = dataAccess.getAllLabels();
        for (MoodLabel label : existingLabels) {
            if (label.getName().equalsIgnoreCase(name)) {
                outputBoundary.prepareFailView("Mood label already exists.");
                return;
            }
        }

        // Create mood label via factory
        MoodLabel.Type moodType = type.equals("Positive") ? MoodLabel.Type.Positive : MoodLabel.Type.Negative;
        MoodLabel newLabel = factory.create(name, moodType);

        // Save to data access
        dataAccess.save(newLabel);

        // Prepare success output
        AddMoodLabelOutputData outputData = new AddMoodLabelOutputData(name, type, true);
        outputBoundary.prepareSuccessView(outputData);
    }
}

