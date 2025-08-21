package view.feedback_panel.feedback_history;

import entity.feedback_entry.FeedbackEntryInterf;
import interface_adapter.feedback_history.FeedbackHistoryViewModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import view.feedback_panel.FeedbackHistoryPanel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeedbackHistoryPanelTest {

    @Mock FeedbackHistoryViewModel vm;

    /* ===================== EDT helpers (Option A: distinct names) ===================== */

    /** Run code on EDT and return its value. */
    private static <T> T onEdtGet(Callable<T> c) {
        final AtomicReference<T> out = new AtomicReference<>();
        final AtomicReference<Throwable> err = new AtomicReference<>();
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

    /** Run a task on EDT (void). */
    private static void onEdtRun(Runnable r) {
        try {
            SwingUtilities.invokeAndWait(r);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /* ================================================================================== */

    @Test
    void showsEmptyStateWhenNoEntries() {
        when(vm.getEntries()).thenReturn(null);

        ArgumentCaptor<PropertyChangeListener> lcap = ArgumentCaptor.forClass(PropertyChangeListener.class);
        doNothing().when(vm).addPropertyChangeListener(lcap.capture());

        FeedbackHistoryPanel panel = onEdtGet(() ->
                new FeedbackHistoryPanel(vm, e -> { /* no-op */ })
        );

        JLabel empty = findLabel(panel, "No feedback entry on record.");
        assertNotNull(empty, "Empty-state label should be present");
    }

    @Test
    void rendersRowsAndInvokesCallbackOnClick() {
        FeedbackEntryInterf e1 = mock(FeedbackEntryInterf.class);
        when(e1.getDate()).thenReturn(LocalDate.of(2025, 8, 11));
        FeedbackEntryInterf e2 = mock(FeedbackEntryInterf.class);
        when(e2.getDate()).thenReturn(LocalDate.of(2025, 8, 18));

        when(vm.getEntries()).thenReturn(List.of(e1, e2));

        ArgumentCaptor<PropertyChangeListener> lcap = ArgumentCaptor.forClass(PropertyChangeListener.class);
        doNothing().when(vm).addPropertyChangeListener(lcap.capture());

        AtomicReference<FeedbackEntryInterf> clicked = new AtomicReference<>();
        FeedbackHistoryPanel panel = onEdtGet(() ->
                new FeedbackHistoryPanel(vm, clicked::set)
        );

        List<JPanel> rows = findImmediateRows(panel);
        assertEquals(2, rows.size(), "Should render one row per entry");

        JPanel firstRow = rows.get(0);
        JLabel date0 = (JLabel) findIn(firstRow, JLabel.class);
        assertNotNull(date0);
        assertEquals("2025-08-11", date0.getText());

        JButton view0 = (JButton) findIn(firstRow, JButton.class);
        assertNotNull(view0);

        onEdtRun(view0::doClick);
        assertSame(e1, clicked.get(), "onViewEntry should be called with the first entry");
    }

    @Test
    void rebuildsOnEntriesPropertyChange() {
        FeedbackEntryInterf e = mock(FeedbackEntryInterf.class);
        when(e.getDate()).thenReturn(LocalDate.of(2025, 8, 18));
        when(vm.getEntries()).thenReturn(List.of()); // initial empty

        ArgumentCaptor<PropertyChangeListener> lcap = ArgumentCaptor.forClass(PropertyChangeListener.class);
        doNothing().when(vm).addPropertyChangeListener(lcap.capture());

        FeedbackHistoryPanel panel = onEdtGet(() ->
                new FeedbackHistoryPanel(vm, x -> {})
        );

        assertNotNull(findLabel(panel, "No feedback entry on record."));

        when(vm.getEntries()).thenReturn(List.of(e));

        PropertyChangeListener listener = lcap.getValue();
        assertNotNull(listener, "Panel should register a property change listener");

        onEdtRun(() ->
                listener.propertyChange(new PropertyChangeEvent(vm, "entries", List.of(), List.of(e)))
        );

        List<JPanel> rows = findImmediateRows(panel);
        assertEquals(1, rows.size(), "Should rebuild to show a single row");

        JLabel date = (JLabel) findIn(rows.get(0), JLabel.class);
        assertEquals("2025-08-18", date.getText());
    }

    /* ---------- small helpers to traverse the component tree ---------- */

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

    /** Finds immediate child JPanels that correspond to entry rows (skips the header label). */
    private static List<JPanel> findImmediateRows(JPanel panel) {
        return java.util.Arrays.stream(panel.getComponents())
                .filter(c -> c instanceof JPanel)
                .map(c -> (JPanel) c)
                .toList();
    }

    private static Component findIn(Container parent, Class<? extends Component> type) {
        for (Component c : parent.getComponents()) {
            if (type.isInstance(c)) return c;
        }
        return null;
    }
}
