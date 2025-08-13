//package use_case.feedback_history;
//
//import use_case.feedback_history.FeedbackHistoryInputBoundary;
//import use_case.feedback_history.FeedbackHistoryOutputBoundary;
//import use_case.repository.FeedbackRepository;
//import java.util.Comparator;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class FeedbackHistoryInteractor implements FeedbackHistoryInputBoundary {
//    private final FeedbackRepository repo;
//    private final FeedbackHistoryOutputBoundary presenter;
//
//    public FeedbackHistoryInteractor(FeedbackRepository repo, FeedbackHistoryOutputBoundary presenter) {
//        this.repo = repo;
//        this.presenter = presenter;
//    }
//
//    @Override
//    public void loadFeedbackHistory() {
//        List<entity.feedback_entry.FeedbackEntry> history = repo.loadAll().stream()
//                .sorted(Comparator.comparing(entity.feedback_entry.FeedbackEntry::getDate).reversed())
//                .collect(Collectors.toList());
//        presenter.present(new FeedbackHistoryOutputData(history));
//    }
//}