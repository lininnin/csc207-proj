package use_case.feedback_Entry;

import entity.Ina.FeedbackEntry;

public class FeedbackEntryInteractor implements FeedbackEntryInputBoundary {
    private final FeedbackEntryRepository repository;
    private final FeedbackEntryOutputBoundary presenter;

    public FeedbackEntryInteractor(FeedbackEntryRepository repo, FeedbackEntryOutputBoundary presenter) {
        this.repository = repo;
        this.presenter = presenter;
    }

    @Override
    public void viewEntry(FeedbackEntryRequestModel request) {
        FeedbackEntry entry = repository.getByDate(request.getDate());
        presenter.present(new FeedbackEntryResponseModel(entry));
    }
}
