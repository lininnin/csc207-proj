package use_case.feedback_history;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import entity.feedback_entry.FeedbackEntryInterf;
import use_case.repository.FeedbackRepository;

public class FeedbackHistoryInteractor implements FeedbackHistoryInputBoundary {
    private final FeedbackRepository repo;
    private final FeedbackHistoryOutputBoundary presenter;

    public FeedbackHistoryInteractor(FeedbackRepository repo, FeedbackHistoryOutputBoundary presenter) {
        this.repo = repo;
        this.presenter = presenter;
    }

    @Override
    public void loadFeedbackHistory() {
        final List<FeedbackEntryInterf> history = repo.loadAll().stream()
                .sorted(Comparator.comparing(FeedbackEntryInterf::getDate).reversed())
                .collect(Collectors.toList());
        presenter.present(new FeedbackHistoryOutputData(history));
    }
}
