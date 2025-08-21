package use_case.feedback_entry;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;


// ---------------------- Tests ----------------------------

public class FeedbackEntryInteractorTest {

    @Test
    void viewEntry_loadsByDate_andPresentsReturnedEntry() {
        // given
        LocalDate date = LocalDate.of(2025, 7, 28);
        var expected = new FakeFeedbackEntry(date, "A", "C", "R");
        var repo = new CapturingRepo(expected);
        var presenter = new CapturingPresenter();
        var interactor = new FeedbackEntryInteractor(repo, presenter);

        // when
        var request = new FeedbackEntryRequestModel(date);
        interactor.viewEntry(request);

        // then
        assertEquals(date, repo.lastLoadByDate, "Interactor must query repo with the request date");
        assertEquals(1, presenter.calls, "Presenter should be called exactly once");
        assertNotNull(presenter.last, "Presenter should receive a response");
        assertSame(expected, presenter.last.getEntry(),
                "Presenter should receive the same entry object returned by repo");
    }

    @Test
    void viewEntry_whenNotFound_presentsNullEntry() {
        // given: repo returns null
        LocalDate date = LocalDate.of(2025, 8, 4);
        var repo = new CapturingRepo(null);
        var presenter = new CapturingPresenter();
        var interactor = new FeedbackEntryInteractor(repo, presenter);

        // when
        interactor.viewEntry(new FeedbackEntryRequestModel(date));

        // then
        assertEquals(date, repo.lastLoadByDate);
        assertEquals(1, presenter.calls);
        assertNotNull(presenter.last);
        assertNull(presenter.last.getEntry(), "Entry should be null when repo has no record for that date");
    }
}
