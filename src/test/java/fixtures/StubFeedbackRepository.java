package fixtures;

import entity.FeedbackEntry;
import use_case.FeedbackRepository;
import java.time.LocalDate;

public class StubFeedbackRepository implements FeedbackRepository {
    private FeedbackEntry storedEntry;

    /**
     * @param entry
     */
    @Override
    public void save(FeedbackEntry entry) {
        this.storedEntry = entry;
    }

    /**
     * @param date
     * @return
     */
    @Override
    public FeedbackEntry loadByDate(LocalDate date) {
        if (storedEntry != null && storedEntry.getDate().equals(date)) {
            return storedEntry;
        }
        return null;
    }
}
