// File: src/test/java/view/DueDatePickerPanelTest.java
package view;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class DueDatePickerPanelTest {

    // ---- EDT helper ----
    private static void onEDT(Runnable r) {
        try {
            if (SwingUtilities.isEventDispatchThread()) {
                r.run();
            } else {
                SwingUtilities.invokeAndWait(r);
            }
        } catch (InterruptedException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    static void headless() {
        System.setProperty("java.awt.headless", "true");
    }

    @Test
    void constructsWithLabelAndDatePicker() {
        onEDT(() -> {
            DueDatePickerPanel panel = new DueDatePickerPanel();

            JLabel label = findLabelWithText(panel, "Due Date:");
            assertNotNull(label, "Label 'Due Date:' should be present");

            DatePicker picker = findDatePicker(panel);
            assertNotNull(picker, "DatePicker should be present");
        });
    }

    @Test
    void settingsConfiguredCorrectly() {
        onEDT(() -> {
            DueDatePickerPanel panel = new DueDatePickerPanel();
            DatePicker picker = findDatePicker(panel);
            assertNotNull(picker);

            DatePickerSettings s = picker.getSettings();
            assertNotNull(s);

            // Allow empty dates
            assertTrue(s.getAllowEmptyDates(), "Allow empty dates should be true");

            // Keyboard editing disabled
            assertFalse(s.getAllowKeyboardEditing(), "Keyboard editing should be disabled");

            // Date format: library may store a DateTimeFormatter internally.
            Object fmtObj = s.getFormatForDatesCommonEra();
            LocalDate probe = LocalDate.of(2025, 1, 2);
            String formatted;
            if (fmtObj instanceof DateTimeFormatter dtf) {
                formatted = dtf.format(probe);
            } else if (fmtObj instanceof String pattern) {
                formatted = probe.format(DateTimeFormatter.ofPattern(pattern));
            } else {
                fail("Unexpected format type from DatePickerSettings#getFormatForDatesCommonEra(): " +
                        (fmtObj == null ? "null" : fmtObj.getClass().getName()));
                return;
            }
            assertEquals("2025-01-02", formatted, "Expected yyyy-MM-dd formatting");
        });
    }

    @Test
    void getSetAndClearSelectedDate_workAsExpected() {
        onEDT(() -> {
            DueDatePickerPanel panel = new DueDatePickerPanel();

            assertNull(panel.getSelectedDate(), "Default selected date should be null (empty allowed)");

            LocalDate today = LocalDate.now();
            panel.setSelectedDate(today);
            assertEquals(today, panel.getSelectedDate(), "Setting today should succeed");

            panel.clear();
            assertNull(panel.getSelectedDate(), "clear() should empty the selection");
        });
    }

    @Test
    void pastDatesAreDisallowedByRangeLimits() {
        onEDT(() -> {
            DueDatePickerPanel panel = new DueDatePickerPanel();
            DatePicker picker = findDatePicker(panel);
            assertNotNull(picker);

            LocalDate today = LocalDate.now();
            LocalDate yesterday = today.minusDays(1);

            // Set a valid date first.
            panel.setSelectedDate(today);
            assertEquals(today, panel.getSelectedDate());

            // Attempt to set an invalid (past) date. LGoodDatePicker enforces range limits,
            // which typically results in either ignoring the change (stays today) or clearing to null.
            panel.setSelectedDate(yesterday);

            LocalDate selected = panel.getSelectedDate();
            assertNotEquals(yesterday, selected, "Yesterday should not be accepted due to range limits");
        });
    }

    // ---- Component tree helpers ----
    private static JLabel findLabelWithText(Container root, String text) {
        if (root == null) return null;
        for (Component c : root.getComponents()) {
            if (c instanceof JLabel l && text.equals(l.getText())) {
                return l;
            }
            if (c instanceof Container child) {
                JLabel found = findLabelWithText(child, text);
                if (found != null) return found;
            }
        }
        return null;
    }

    private static DatePicker findDatePicker(Container root) {
        if (root == null) return null;
        for (Component c : root.getComponents()) {
            if (c instanceof DatePicker dp) {
                return dp;
            }
            if (c instanceof Container child) {
                DatePicker found = findDatePicker(child);
                if (found != null) return found;
            }
        }
        return null;
    }
}
