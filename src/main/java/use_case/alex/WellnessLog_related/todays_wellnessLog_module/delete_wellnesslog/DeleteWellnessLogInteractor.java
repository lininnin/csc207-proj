package use_case.alex.WellnessLog_related.todays_wellnessLog_module.delete_wellnesslog;

/**
 * Interactor for the DeleteWellnessLog use case.
 * Coordinates between input data, data access, and output boundary (presenter).
 */
public class DeleteWellnessLogInteractor implements DeleteWellnessLogInputBoundary {

    private final DeleteWellnessLogDataAccessInterf dataAccess;
    private final DeleteWellnessLogOutputBoundary presenter;

    public DeleteWellnessLogInteractor(DeleteWellnessLogDataAccessInterf dataAccess,
                                       DeleteWellnessLogOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(DeleteWellnessLogInputData inputData) {
        String logId = inputData.getLogId();

        boolean deleted = dataAccess.deleteById(logId);
        if (deleted) {
            DeleteWellnessLogOutputData outputData = new DeleteWellnessLogOutputData(logId, true);
            presenter.prepareSuccessView(outputData);
        } else {
            presenter.prepareFailView("Deletion failed: Log ID " + logId + " not found.");
        }
    }
}

