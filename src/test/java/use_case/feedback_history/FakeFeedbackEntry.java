package use_case.feedback_history;

import entity.feedback_entry.FeedbackEntryInterf;
import use_case.feedback_history.FeedbackHistoryOutputBoundary;
import use_case.feedback_history.FeedbackHistoryOutputData;

import java.time.LocalDate;

record FakeFeedbackEntry(LocalDate date) implements FeedbackEntryInterf {

    @Override
    public LocalDate getDate() {
        return date;
    }

    @Override
    public String getAiAnalysis() {
        return "";
    }

    @Override
    public String getCorrelationData() {
        return "";
    }

    @Override
    public String getRecommendations() {
        return "";
    }
}