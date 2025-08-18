package use_case.generate_feedback;

import entity.feedback_entry.FeedbackEntryInterf;

public class GenerateFeedbackOutputData {
    private final FeedbackEntryInterf feedbackEntry;

    public GenerateFeedbackOutputData(FeedbackEntryInterf feedbackEntry) {
        this.feedbackEntry = feedbackEntry;
    }

    public FeedbackEntryInterf getFeedbackEntry() {
        return feedbackEntry;
    }
}
