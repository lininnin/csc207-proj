package interface_adapter.generate_feedback;

import java.util.List;

import javax.swing.SwingUtilities;

import entity.feedback_entry.FeedbackEntryInterf;
import interface_adapter.feedback_history.FeedbackHistoryViewModel;
import use_case.generate_feedback.GenerateFeedbackOutputBoundary;
import use_case.generate_feedback.GenerateFeedbackOutputData;

public class GenerateFeedbackPresenter implements GenerateFeedbackOutputBoundary {
    private final FeedbackHistoryViewModel historyVm;

    public GenerateFeedbackPresenter(FeedbackHistoryViewModel historyVm) {
        this.historyVm = historyVm;
    }

    @Override
    public void present(GenerateFeedbackOutputData out) {
        final FeedbackEntryInterf entry = out.getFeedbackEntry();
        SwingUtilities.invokeLater(() -> historyVm.setEntries(List.of(entry))
        );
    }
}
