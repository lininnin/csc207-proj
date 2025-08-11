// InMemoryFeedbackRepository.java (for FeedbackEntry)
package data_access.in_memory_repo;

import entity.Ina.FeedbackEntry;
import use_case.repository.FeedbackRepository;

import java.time.LocalDate;
import java.util.*;

public class InMemoryFeedbackRepository implements FeedbackRepository {
    private final Map<LocalDate, FeedbackEntry> feedbacks = new HashMap<>();
    @Override
    public FeedbackEntry loadByDate(LocalDate date) {
        return feedbacks.get(date);
    }

    @Override
    public List<FeedbackEntry> loadAll() {
        return new ArrayList<>(feedbacks.values());
    }

    @Override
    public void save(FeedbackEntry entry) {
        feedbacks.put(entry.getDate(), entry);
    }
}
