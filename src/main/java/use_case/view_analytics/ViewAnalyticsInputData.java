package use_case.view_analytics;

/**
 * Input data for viewing analytics.
 */
public class ViewAnalyticsInputData {
    private final String userId;

    public ViewAnalyticsInputData(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
