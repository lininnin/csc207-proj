package use_case.feedback_entry;

import entity.feedback_entry.FeedbackEntryInterf;

import java.time.LocalDate;

/** Minimal fake entry to satisfy the interface. */
final class FakeFeedbackEntry implements FeedbackEntryInterf {
    private final LocalDate date;
    private final String analysis, corr, recs;

    FakeFeedbackEntry(LocalDate date, String analysis, String corr, String recs) {
        this.date = date; this.analysis = analysis; this.corr = corr; this.recs = recs;
    }

    @Override public LocalDate getDate() { return date; }
    @Override public String getAiAnalysis() { return analysis; }
    @Override public String getCorrelationData() { return corr; }
    @Override public String getRecommendations() { return recs; }
}