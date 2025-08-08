package interface_adapter.generate_feedback;

import entity.Ina.FeedbackEntry;
import interface_adapter.feedback_history.FeedbackHistoryViewModel;
import use_case.generate_feedback.GenerateFeedbackOutputBoundary;
import use_case.generate_feedback.GenerateFeedbackOutputData;

import javax.swing.SwingUtilities;
import java.util.List;

public class GenerateFeedbackPresenter implements GenerateFeedbackOutputBoundary {
    private final FeedbackHistoryViewModel historyVM;

    public GenerateFeedbackPresenter(FeedbackHistoryViewModel historyVM) {
        this.historyVM = historyVM;
    }

    @Override
    public void present(GenerateFeedbackOutputData out) {
        FeedbackEntry entry = out.getFeedbackEntry();
        SwingUtilities.invokeLater(() ->
                historyVM.setEntries(List.of(entry))
        );
    }
}
