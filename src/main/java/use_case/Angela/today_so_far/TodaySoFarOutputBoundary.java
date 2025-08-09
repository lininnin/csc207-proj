package use_case.Angela.today_so_far;

/**
 * Output boundary for the Today So Far use case.
 */
public interface TodaySoFarOutputBoundary {
    
    /**
     * Presents the updated Today So Far data.
     * @param outputData The data to present
     */
    void presentTodaySoFar(TodaySoFarOutputData outputData);
    
    /**
     * Presents an error if data cannot be retrieved.
     * @param error The error message
     */
    void presentError(String error);
}