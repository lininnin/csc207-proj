package use_case.feedback_entry;

public interface FeedbackEntryInputBoundary {
    /**
     * Request to view a specific feedback entry.
     * @param request the request model containing the data
     *                needed to get/display the feedback entry
     */
    void viewEntry(FeedbackEntryRequestModel request);
}

