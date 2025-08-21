package use_case.feedback_entry;

import entity.feedback_entry.FeedbackEntryInterf;
import use_case.repository.FeedbackRepository;

import java.time.LocalDate;
import java.util.List;

/** Fake repo that captures the last date it was asked to load and returns a preset entry. */
final class CapturingRepo implements FeedbackRepository {
    LocalDate lastLoadByDate = null;
    FeedbackEntryInterf toReturn;

    CapturingRepo(FeedbackEntryInterf toReturn) { this.toReturn = toReturn; }

    @Override public void save(FeedbackEntryInterf entry) { throw new UnsupportedOperationException(); }
    @Override public FeedbackEntryInterf loadByDate(LocalDate date) { lastLoadByDate = date; return toReturn; }
    @Override public List<FeedbackEntryInterf> loadAll() { throw new UnsupportedOperationException(); }
}