package use_case.view_analytics;

import entity.DailyTaskSummary;
import java.util.List;

/**
 * Application business logic for viewing analytics such as task summaries, completion rates, etc.
 */
public class ViewAnalyticsUseCase implements ViewAnalyticsInputBoundary {
    private final ViewAnalyticsDataAccessInterface dataAccess;
    private final ViewAnalyticsOutputBoundary presenter;

    public ViewAnalyticsUseCase(ViewAnalyticsDataAccessInterface dataAccess,
                                ViewAnalyticsOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    /**
     * Executes the use case: gathers analytics from the data source
     * and prepares it for the presenter layer.
     */
    @Override
    public void execute(ViewAnalyticsInputData inputData) {
        List<DailyTaskSummary> summaries = dataAccess.getSummariesForUser(inputData.getUserId());

        ViewAnalyticsOutputData outputData = new ViewAnalyticsOutputData(summaries);
        presenter.prepareSuccessView(outputData);
    }
}
