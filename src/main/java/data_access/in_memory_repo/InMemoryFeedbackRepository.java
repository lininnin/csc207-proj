package data_access.in_memory_repo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entity.feedback_entry.FeedbackEntryInterf;
import use_case.repository.FeedbackRepository;

public class InMemoryFeedbackRepository implements FeedbackRepository {
    private final Map<LocalDate, FeedbackEntryInterf> feedbacks = new HashMap<>();

    @Override
    public FeedbackEntryInterf loadByDate(LocalDate date) {
        return feedbacks.get(date);
    }

    @Override
    public List<FeedbackEntryInterf> loadAll() {
        return new ArrayList<>(feedbacks.values());
    }

    @Override
    public void save(FeedbackEntryInterf entry) {
        feedbacks.put(entry.getDate(), entry);
    }
}
