package use_case.repository;

import entity.FeedbackEntry;

import java.time.LocalDate;

public interface FeedbackRepository {
    void save(FeedbackEntry entry);
    FeedbackEntry loadByDate(LocalDate date);
}
