package use_case.feedback_history;

import entity.Ina.FeedbackEntry;
import use_case.repository.FeedbackRepository;
import java.util.List;

public class FeedbackHistoryInteractor {
    private final FeedbackRepository repo;
    private final FeedbackHistoryOutputBoundary presenter;

    public FeedbackHistoryInteractor(FeedbackRepository repo, FeedbackHistoryOutputBoundary presenter) {
        this.repo = repo;
        this.presenter = presenter;
    }

    public void execute() {
        List<FeedbackEntry> entries = repo.loadAll();
        presenter.present(new FeedbackHistoryOutputData(entries));
    }
}
