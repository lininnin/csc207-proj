package app.feedback_panel;

import constants.Constants;
import entity.feedback_entry.FeedbackEntryInterf;
import interface_adapter.feedback_history.FeedbackHistoryViewModel;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import use_case.repository.FeedbackRepository;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FeedbackPageBuilderTest {

    /* ===================== EDT helpers ===================== */

    private static <T> T onEdtGet(java.util.concurrent.Callable<T> c) {
        final java.util.concurrent.atomic.AtomicReference<T> out = new java.util.concurrent.atomic.AtomicReference<>();
        final java.util.concurrent.atomic.AtomicReference<Throwable> err = new java.util.concurrent.atomic.AtomicReference<>();
        try {
            SwingUtilities.invokeAndWait(() -> {
                try { out.set(c.call()); } catch (Throwable t) { err.set(t); }
            });
        } catch (Exception e) { throw new RuntimeException(e); }
        if (err.get() != null) throw new RuntimeException(err.get());
        return out.get();
    }

    private static void onEdtRun(Runnable r) {
        try { SwingUtilities.invokeAndWait(r); }
        catch (Exception e) { throw new RuntimeException(e); }
    }

    /* ===================== Tests ===================== */

    @Test
    void build_withNoEntries_showsEmptyHistory_andDoesNotLoadDetail() {
        FeedbackRepository repo = mock(FeedbackRepository.class);
        when(repo.loadAll()).thenReturn(List.of());

        JPanel page = onEdtGet(() -> new FeedbackPageBuilder(repo).build());

        // BorderLayout with CENTER (entry panel) and EAST (history panel)
        assertTrue(page.getLayout() instanceof BorderLayout);
        Component entryCenter = ((BorderLayout) page.getLayout()).getLayoutComponent(BorderLayout.CENTER);
        Component historyEast = ((BorderLayout) page.getLayout()).getLayoutComponent(BorderLayout.EAST);

        assertNotNull(entryCenter, "Entry panel should be in CENTER");
        assertNotNull(historyEast, "History panel should be in EAST");

        // History shows empty state
        JLabel empty = findLabel((Container) historyEast, "No feedback entry on record.");
        assertNotNull(empty, "History panel should show empty-state message");

        // No detail load when there are no entries
        verify(repo, times(1)).loadAll();
        verify(repo, never()).loadByDate(any());
    }

    @Test
    void build_withEntries_loadsHistory_andAutoSelectsNewest() {
        FeedbackRepository repo = mock(FeedbackRepository.class);

        // Two entries; builder will select the first one from VM (provided by interactor → repo.loadAll()).
        LocalDate d1 = LocalDate.of(2025, 8, 20);
        LocalDate d2 = LocalDate.of(2025, 8, 13);

        FeedbackEntryInterf listE1 = mockEntryWithDate(d1);
        FeedbackEntryInterf listE2 = mockEntryWithDate(d2);
        when(repo.loadAll()).thenReturn(List.of(listE1, listE2));

        // When controller selects d1, interactor will call repo.loadByDate(d1)
        FeedbackEntryInterf fullE1 = mock(FeedbackEntryInterf.class);
        when(fullE1.getDate()).thenReturn(d1);
        when(fullE1.getAiAnalysis()).thenReturn("A");
        when(fullE1.getRecommendations()).thenReturn("R");
        when(fullE1.getCorrelationData()).thenReturn(null);
        when(repo.loadByDate(d1)).thenReturn(fullE1);

        JPanel page = onEdtGet(() -> new FeedbackPageBuilder(repo).build());

        // Verify composition
        assertTrue(page.getLayout() instanceof BorderLayout);
        JSplitPaneAware assertEntryHeader = new JSplitPaneAware(page);

        // Header in entry panel should reflect the auto-selected d1
        JLabel header = assertEntryHeader.findEntryHeader();
        assertNotNull(header, "Entry header label should exist");
        assertEquals("Feedback on " + d1, header.getText(),
                "Entry header should show selected date");

        // Verify repository interactions
        verify(repo, times(1)).loadAll();
        verify(repo, times(1)).loadByDate(d1);
        verify(repo, never()).loadByDate(d2);
    }

    /* ---------------- helpers ---------------- */

    private static FeedbackEntryInterf mockEntryWithDate(LocalDate date) {
        FeedbackEntryInterf e = mock(FeedbackEntryInterf.class);
        when(e.getDate()).thenReturn(date);
        return e;
    }

    /** Find a JLabel with given text in component subtree. */
    private static JLabel findLabel(Container root, String text) {
        for (Component c : root.getComponents()) {
            if (c instanceof JLabel lbl && text.equals(lbl.getText())) return lbl;
            if (c instanceof Container child) {
                JLabel found = findLabel(child, text);
                if (found != null) return found;
            }
        }
        return null;
    }

    /**
     * Helper that knows the structure of the page built by FeedbackPageBuilder:
     * BorderLayout with CENTER = FeedbackEntryPanel (header is its NORTH component).
     */
    private static final class JSplitPaneAware {
        private final JPanel page;
        JSplitPaneAware(JPanel page) { this.page = page; }

        JLabel findEntryHeader() {
            BorderLayout bl = (BorderLayout) page.getLayout();
            Component center = bl.getLayoutComponent(BorderLayout.CENTER);
            assertNotNull(center, "CENTER must exist");
            assertTrue(center instanceof Container, "CENTER must be a container");

            // In FeedbackEntryPanel: header label is the NORTH component (index 0)
            Container entryPanel = (Container) center;
            // Try to fetch first component and check if it's a JLabel header
            if (entryPanel.getComponentCount() > 0 && entryPanel.getComponent(0) instanceof JLabel lbl) {
                return lbl;
            }
            // Fallback: search tree for first JLabel
            return findAnyLabel(entryPanel);
        }

        private JLabel findAnyLabel(Container root) {
            for (Component c : root.getComponents()) {
                if (c instanceof JLabel lbl) return lbl;
                if (c instanceof Container child) {
                    JLabel found = findAnyLabel(child);
                    if (found != null) return found;
                }
            }
            return null;
        }
    }
}
