package use_case.feedback_entry;

import java.time.LocalDate;

import entity.feedback_entry.FeedbackEntryInterf;
import use_case.repository.FeedbackRepository;

public class FeedbackEntryInteractor implements FeedbackEntryInputBoundary {
    private final FeedbackRepository repo;
    private final FeedbackEntryOutputBoundary output;

    public FeedbackEntryInteractor(FeedbackRepository repo, FeedbackEntryOutputBoundary output) {
        this.repo = repo;
        this.output = output;
    }

    @Override
    public void viewEntry(FeedbackEntryRequestModel request) {
        final LocalDate date = request.getDate();
        final FeedbackEntryInterf entry = repo.loadByDate(date);
        final FeedbackEntryResponseModel response = new FeedbackEntryResponseModel(entry);
        output.present(response);
    }
}
