package use_case.feedback_history;

import entity.feedback_entry.FeedbackEntryInterf;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// ---- Tests ----
class FeedbackHistoryInteractorTest {

    @Test
    void loadsFromRepo_sortsByDateDescending_andPresentsOnce() {
        LocalDate d1 = LocalDate.of(2025, 1, 5);
        LocalDate d2 = LocalDate.of(2025, 2, 2);
        LocalDate d3 = LocalDate.of(2025, 3, 1); // newest

        var repo = new FakeRepo(List.of(
                new FakeFeedbackEntry(d2),
                new FakeFeedbackEntry(d3),
                new FakeFeedbackEntry(d1)
        ));
        var presenter = new CapturingPresenter();
        var interactor = new FeedbackHistoryInteractor(repo, presenter);

        interactor.loadFeedbackHistory();

        assertEquals(1, presenter.calls, "presenter should be called exactly once");
        assertNotNull(presenter.last, "output should be captured");

        var dates = presenter.last.getEntries().stream().map(FeedbackEntryInterf::getDate).toList();
        assertEquals(List.of(d3, d2, d1), dates, "history must be newest → oldest");
    }

    @Test
    void emptyRepo_yieldsEmptyEntries() {
        var repo = new FakeRepo(List.of());
        var presenter = new CapturingPresenter();
        var interactor = new FeedbackHistoryInteractor(repo, presenter);

        interactor.loadFeedbackHistory();

        assertEquals(1, presenter.calls);
        assertNotNull(presenter.last);
        assertTrue(presenter.last.getEntries().isEmpty(), "entries should be empty");
    }
}
