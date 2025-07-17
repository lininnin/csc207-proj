package interface_adapter.persistence;

import entity.FeedbackEntry;
import use_case.FeedbackRepository;

import java.time.LocalDate;

public class FeedbackRepositoryImpl implements FeedbackRepository {
    /**
     * @param entry
     */
    @Override
    public void save(FeedbackEntry entry) {

    }

    /**
     * @param date
     * @return the FeedbackEntry logged on date.
     */
    @Override
    public FeedbackEntry loadByDate(LocalDate date) {
        return null;
    }
}
