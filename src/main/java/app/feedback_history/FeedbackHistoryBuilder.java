package app.feedback_history;

import interface_adapter.feedback_history.FeedbackHistoryController;
import interface_adapter.feedback_history.FeedbackHistoryPresenter;
import interface_adapter.feedback_history.FeedbackHistoryViewModel;
import use_case.feedback_history.FeedbackHistoryInputBoundary;
import use_case.feedback_history.FeedbackHistoryInteractor;
import use_case.feedback_history.FeedbackHistoryOutputBoundary;
import use_case.repository.FeedbackRepository;
import view.feedback_history.FeedbackHistoryDialog;

import java.awt.*;

public class FeedbackHistoryBuilder {
    public static FeedbackHistoryDialog build(Frame owner, FeedbackRepository feedbackRepo) {
        // ViewModel
        FeedbackHistoryViewModel viewModel = new FeedbackHistoryViewModel();
        // Presenter
        FeedbackHistoryOutputBoundary presenter = new FeedbackHistoryPresenter(viewModel);
        // Interactor
        FeedbackHistoryInputBoundary interactor =
                new FeedbackHistoryInteractor(feedbackRepo, presenter);
        // Controller
        FeedbackHistoryController controller = new FeedbackHistoryController(interactor);
        // Dialog
        return new FeedbackHistoryDialog(owner, controller, viewModel);
    }
}
