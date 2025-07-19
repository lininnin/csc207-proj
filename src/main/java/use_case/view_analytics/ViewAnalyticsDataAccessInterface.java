package use_case.view_analytics;

import entity.DailyTaskSummary;
import java.util.List;

/**
 * Interface for retrieving analytics-related data from repository.
 */
public interface ViewAnalyticsDataAccessInterface {
    List<DailyTaskSummary> getSummariesForUser(String userId);
}
