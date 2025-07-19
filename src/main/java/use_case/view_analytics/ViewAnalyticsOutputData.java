package use_case.view_analytics;

import entity.DailyTaskSummary;
import java.util.List;

/**
 * Output data containing task summaries for analytics.
 */
public class ViewAnalyticsOutputData {
    private final List<DailyTaskSummary> summaries;

    public ViewAnalyticsOutputData(List<DailyTaskSummary> summaries) {
        this.summaries = summaries;
    }

    public List<DailyTaskSummary> getSummaries() {
        return summaries;
    }
}
