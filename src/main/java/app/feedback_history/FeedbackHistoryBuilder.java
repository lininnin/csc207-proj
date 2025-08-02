package app.feedback_history;

import interface_adapter.feedback_history.FeedbackHistoryController;
import interface_adapter.feedback_history.FeedbackHistoryPresenter;
import interface_adapter.feedback_history.FeedbackHistoryViewModel;
import use_case.feedback_history.FeedbackHistoryInputBoundary;
import use_case.feedback_history.FeedbackHistoryInteractor;
import use_case.feedback_history.FeedbackHistoryOutputBoundary;
import use_case.repository.FeedbackRepository;
import view.feedback_history.FeedbackHistoryPanel;

import javax.swing.*;

public class FeedbackHistoryBuilder {
    /**
     * Returns FeedbackHistoryPanel
     * Used in main window with CardLayout
     */
    public static JPanel build(FeedbackRepository feedbackRepo) {
        FeedbackHistoryViewModel viewModel = new FeedbackHistoryViewModel();
        FeedbackHistoryOutputBoundary presenter = new FeedbackHistoryPresenter(viewModel);
        FeedbackHistoryInputBoundary interactor = new FeedbackHistoryInteractor(feedbackRepo, presenter);
        FeedbackHistoryController controller = new FeedbackHistoryController(interactor);

        return new FeedbackHistoryPanel(controller, viewModel);
    }
}