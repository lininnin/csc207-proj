package use_case.Alex.WellnessLog_related.add_wellnessLog;

import entity.Alex.WellnessLogEntry.WellnessLogEntry;
import entity.Alex.WellnessLogEntry.WellnessLogEntryFactoryInterf;

/**
 * The interactor for adding a wellness log entry.
 * Implements the input boundary and contains the business logic.
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
            System.out.println("[Interactor] Creating WellnessLogEntry...");
            System.out.println("  Mood: " + inputData.getMoodLabel());
            System.out.println("  Stress: " + inputData.getStressLevel());
            System.out.println("  Energy: " + inputData.getEnergyLevel());
            System.out.println("  Fatigue: " + inputData.getFatigueLevel());
            System.out.println("  Note: " + inputData.getUserNote());

            // Create the entity
            WellnessLogEntry entry = factory.create(
                    inputData.getTime(),
                    inputData.getStressLevel(),
                    inputData.getEnergyLevel(),
                    inputData.getFatigueLevel(),
                    inputData.getMoodLabel(),
                    inputData.getUserNote()
            );

            System.out.println("[Interactor] Entry created: " + entry);

            // Save to data layer
            dataAccess.save(entry);

            System.out.println("[Interactor] Entry saved to DAO");

            // Return success
            AddWellnessLogOutputData outputData =
                    new AddWellnessLogOutputData(entry, true, "Wellness log added successfully.");
            presenter.prepareSuccessView(outputData);

        } catch (Exception e) {
            System.out.println("[Interactor] Exception occurred:");
            e.printStackTrace(); // ✅ 打印到 terminal
            presenter.prepareFailView("Failed to add log: " + e.getMessage());
        }
    }
}


