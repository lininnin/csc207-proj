package use_case.feedback_history;

import entity.feedback_entry.FeedbackEntryInterf;
import use_case.repository.FeedbackRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

final class FakeRepo implements FeedbackRepository {
    private final List<FeedbackEntryInterf> all;
    FakeRepo(List<FeedbackEntryInterf> all) { this.all = all; }

    @Override public void save(FeedbackEntryInterf entry) { throw new UnsupportedOperationException(); }
    @Override public FeedbackEntryInterf loadByDate(LocalDate date) { return null; }
    @Override public List<FeedbackEntryInterf> loadAll() { return new ArrayList<>(all); }
}