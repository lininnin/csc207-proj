package use_case.repository;

import entity.Ina.FeedbackEntry;

import java.time.LocalDate;
import java.util.List;

public interface FeedbackRepository {
    void save(FeedbackEntry entry);
    FeedbackEntry loadByDate(LocalDate date);
    List<FeedbackEntry> loadAll();
}
