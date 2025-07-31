package use_case.repository;

import entity.Ina.FeedbackEntry;
import java.time.LocalDate;
import java.util.*;

public class InMemoryFeedbackRepository implements FeedbackRepository {
    private final Map<LocalDate, FeedbackEntry> map = new HashMap<>();

    @Override
    public void save(FeedbackEntry entry) { map.put(entry.getDate(), entry); }
    @Override
    public FeedbackEntry loadByDate(LocalDate date) { return map.get(date); }
    @Override
    public List<FeedbackEntry> loadAll() { return new ArrayList<>(map.values()); }
}
