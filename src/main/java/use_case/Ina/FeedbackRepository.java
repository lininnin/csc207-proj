package use_case.Ina;

import entity.Ina.FeedbackEntry;

import java.time.LocalDate;

public interface FeedbackRepository {
    void save(FeedbackEntry entry);
    FeedbackEntry loadByDate(LocalDate date);
}
