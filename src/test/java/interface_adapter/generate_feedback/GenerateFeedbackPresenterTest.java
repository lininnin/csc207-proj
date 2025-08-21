// File: src/test/java/interface_adapter/generate_feedback/GenerateFeedbackPresenterTest.java
package interface_adapter.generate_feedback;

import entity.feedback_entry.FeedbackEntryInterf;
import interface_adapter.feedback_history.FeedbackHistoryViewModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import use_case.generate_feedback.GenerateFeedbackOutputData;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GenerateFeedbackPresenterTest {

    @BeforeAll
    static void headless() {
        System.setProperty("java.awt.headless", "true");
    }

    /** Flushes the EDT queue to ensure invokeLater() tasks have run. */
    private static void flushEdt() {
        try {
            SwingUtilities.invokeAndWait(() -> { /* no-op */ });
        } catch (InterruptedException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void present_pushesSingleEntryToHistoryViewModel_onEDT() {
        // Arrange
        FeedbackHistoryViewModel historyVm = mock(FeedbackHistoryViewModel.class, withSettings().lenient());
        GenerateFeedbackPresenter presenter = new GenerateFeedbackPresenter(historyVm);

        GenerateFeedbackOutputData out = mock(GenerateFeedbackOutputData.class, withSettings().lenient());
        FeedbackEntryInterf entry = mock(FeedbackEntryInterf.class, withSettings().lenient());
        when(out.getFeedbackEntry()).thenReturn(entry);

        // Act
        presenter.present(out);
        flushEdt(); // allow invokeLater() to execute

        // Assert
        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<FeedbackEntryInterf>> listCaptor = ArgumentCaptor.forClass(List.class);
        verify(historyVm, times(1)).setEntries(listCaptor.capture());

        List<FeedbackEntryInterf> captured = listCaptor.getValue();
        assertNotNull(captured, "setEntries should be called with a non-null list");
        assertEquals(1, captured.size(), "setEntries should receive a singleton list");
        assertSame(entry, captured.get(0), "The list should contain exactly the provided entry");
    }
}
