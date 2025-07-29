package interface_adapter.feedback_history;

import entity.Ina.FeedbackEntry;
import use_case.feedback_history.FeedbackHistoryOutputBoundary;
import use_case.feedback_history.FeedbackHistoryOutputData;

import java.util.Comparator;
import java.util.List;

public class FeedbackHistoryPresenter implements FeedbackHistoryOutputBoundary {
    private final FeedbackHistoryViewModel viewModel;

    public FeedbackHistoryPresenter(FeedbackHistoryViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(FeedbackHistoryOutputData outputData) {
        List<FeedbackEntry> sorted = outputData.getEntries().stream()
                .sorted(Comparator.comparing(FeedbackEntry::getDate).reversed())
                .toList();
        viewModel.setEntries(sorted);
    }
}
