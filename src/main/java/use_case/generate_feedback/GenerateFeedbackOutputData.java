package use_case.generate_feedback;

import entity.Ina.FeedbackEntry;

public class GenerateFeedbackOutputData {
    private final FeedbackEntry feedbackEntry;

    public GenerateFeedbackOutputData(FeedbackEntry feedbackEntry) {
        this.feedbackEntry = feedbackEntry;
    }

    public FeedbackEntry getFeedbackEntry() {
        return feedbackEntry;
    }
}
