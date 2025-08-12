package interface_adapter.feedback_history;

import use_case.feedback_history.FeedbackHistoryOutputBoundary;
import use_case.feedback_history.FeedbackHistoryOutputData;

public class FeedbackHistoryPresenter implements FeedbackHistoryOutputBoundary {
    private final FeedbackHistoryViewModel viewModel;

    public FeedbackHistoryPresenter(FeedbackHistoryViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(FeedbackHistoryOutputData outputData) {
        viewModel.setEntries(outputData.getEntries());
    }
}
