package use_case.alex.wellness_log_related.add_wellnessLog;

import entity.alex.WellnessLogEntry.WellnessLogEntryInterf;
import entity.alex.WellnessLogEntry.WellnessLogEntryFactoryInterf;

/**
 * The interactor for adding a wellness log entry.
 * Implements the input boundary and contains the business logic.
 * Now fully decoupled from the concrete WellnessLogEntry class by using the WellnessLogEntryInterf interface.
 */
public class AddWellnessLogInteractor implements AddWellnessLogInputBoundary {

    private final AddWellnessLogDataAccessInterf dataAccess;
    private final WellnessLogEntryFactoryInterf factory;
    private final AddWellnessLogOutputBoundary presenter;

    public AddWellnessLogInteractor(AddWellnessLogDataAccessInterf dataAccess,
                                    WellnessLogEntryFactoryInterf factory,
                                    AddWellnessLogOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.factory = factory;
        this.presenter = presenter;
    }

    @Override
    public void execute(AddWellnessLogInputData inputData) {
        try {

            // Create the entity (factory returns an interface type)
            WellnessLogEntryInterf entry = factory.create(
                    inputData.getTime(),
                    inputData.getStressLevel(),
                    inputData.getEnergyLevel(),
                    inputData.getFatigueLevel(),
                    inputData.getMoodLabel(),
                    inputData.getUserNote()
            );


            // Save to data layer
            dataAccess.save(entry);


            // Return success
            AddWellnessLogOutputData outputData =
                    new AddWellnessLogOutputData(entry, true, "Wellness log added successfully.");
            presenter.prepareSuccessView(outputData);

        } catch (Exception e) {
            e.printStackTrace();
            presenter.prepareFailView("Failed to add log: " + e.getMessage());
        }
    }
}
