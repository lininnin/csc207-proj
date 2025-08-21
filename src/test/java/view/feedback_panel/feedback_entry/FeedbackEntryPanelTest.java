package view.feedback_panel.feedback_entry;

import entity.feedback_entry.FeedbackEntryInterf;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeedbackEntryPanelTest {

    @Mock FeedbackEntryInterf entry;

    /* ===================== EDT helpers ===================== */

    private static <T> T onEdtGet(java.util.concurrent.Callable<T> c) {
        final java.util.concurrent.atomic.AtomicReference<T> out = new java.util.concurrent.atomic.AtomicReference<>();
        final java.util.concurrent.atomic.AtomicReference<Throwable> err = new java.util.concurrent.atomic.AtomicReference<>();
        try {
            SwingUtilities.invokeAndWait(() -> {
                try { out.set(c.call()); } catch (Throwable t) { err.set(t); }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (err.get() != null) throw new RuntimeException(err.get());
        return out.get();
    }

    private static void onEdtRun(Runnable r) {
        try {
            SwingUtilities.invokeAndWait(r);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /* ======================================================= */

    @Test
    void displaysEntryWithFullData() {
        when(entry.getDate()).thenReturn(LocalDate.of(2025, 8, 20));
        when(entry.getAiAnalysis()).thenReturn("Good progress this week");
        when(entry.getRecommendations()).thenReturn("Take breaks");
        when(entry.getCorrelationData()).thenReturn("{\"some\":\"json\"}");

        FeedbackEntryPanel panel = onEdtGet(FeedbackEntryPanel::new);

        onEdtRun(() -> panel.displayEntry(entry));

        // Header should show formatted date
        JLabel header = (JLabel) panel.getComponent(0);
        assertEquals("Feedback on 2025-08-20", header.getText());

        // Verify analysis and recommendations text areas contain the right text
        JTextArea analysisArea = findTextArea(panel, "Good progress this week");
        JTextArea recArea = findTextArea(panel, "Take breaks");
        assertNotNull(analysisArea);
        assertNotNull(recArea);

        // Bottom component should be a CorrelationPanel
        JSplitPane split = (JSplitPane) panel.getComponent(1);
        Component bottom = split.getBottomComponent();
        assertTrue(bottom instanceof CorrelationPanel, "Should replace with CorrelationPanel");
    }

    @Test
    void displaysEntryWithNullsAndNoCorrelation() {
        when(entry.getDate()).thenReturn(LocalDate.of(2025, 8, 21));
        when(entry.getAiAnalysis()).thenReturn(null);
        when(entry.getRecommendations()).thenReturn(null);
        when(entry.getCorrelationData()).thenReturn("   "); // blank

        FeedbackEntryPanel panel = onEdtGet(FeedbackEntryPanel::new);

        onEdtRun(() -> panel.displayEntry(entry));

        JLabel header = (JLabel) panel.getComponent(0);
        assertEquals("Feedback on 2025-08-21", header.getText());

        // Text areas should show "None"
        assertNotNull(findTextArea(panel, "None"));

        // Bottom component should be a plain JPanel with the default label
        JSplitPane split = (JSplitPane) panel.getComponent(1);
        Component bottom = split.getBottomComponent();
        assertTrue(bottom instanceof JPanel, "Should be a JPanel when no correlation");
        JLabel noCorrLabel = findLabel((Container) bottom, "No correlation data available.");
        assertNotNull(noCorrLabel);
    }

    /* ---------- helpers ---------- */

    private static JTextArea findTextArea(Container root, String expected) {
        for (Component c : root.getComponents()) {
            if (c instanceof JTextArea ta && expected.equals(ta.getText())) return ta;
            if (c instanceof JScrollPane sp) {
                JViewport vp = sp.getViewport();
                Component view = vp.getView();
                if (view instanceof JTextArea ta && expected.equals(ta.getText())) return ta;
            }
            if (c instanceof Container child) {
                JTextArea found = findTextArea(child, expected);
                if (found != null) return found;
            }
        }
        return null;
    }

    private static JLabel findLabel(Container root, String expected) {
        for (Component c : root.getComponents()) {
            if (c instanceof JLabel lbl && expected.equals(lbl.getText())) return lbl;
            if (c instanceof Container child) {
                JLabel found = findLabel(child, expected);
                if (found != null) return found;
            }
        }
        return null;
    }
}
