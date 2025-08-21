package app.goal;

import app.goalPage.GoalPageBuilder;
import data_access.InMemoryTaskDataAccessObject;
import entity.Angela.Task.TaskAvailable;
import entity.info.Info;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GoalPageBuilderTest {

    /* ----------------- EDT helpers ----------------- */

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

    /* ----------------- reflection helpers ----------------- */

    private static void setField(Object target, String name, Object value) {
        try {
            Field f = GoalPageBuilder.class.getDeclaredField(name);
            f.setAccessible(true);
            f.set(target, value);
        } catch (Exception e) { throw new RuntimeException(e); }
    }

    @SuppressWarnings("unchecked")
    private static <T> T getField(Object target, String name, Class<T> type) {
        try {
            Field f = GoalPageBuilder.class.getDeclaredField(name);
            f.setAccessible(true);
            return (T) f.get(target);
        } catch (Exception e) { throw new RuntimeException(e); }
    }

    private static Object invoke(Object target, String method) {
        try {
            Method m = GoalPageBuilder.class.getDeclaredMethod(method);
            m.setAccessible(true);
            return m.invoke(target);
        } catch (Exception e) { throw new RuntimeException(e); }
    }

    /* ----------------- tests ----------------- */

    @Test
    void createGoalFormPanel_buildsExpectedDefaults_andPopulatesTargetTasks() {
        InMemoryTaskDataAccessObject gateway = mock(InMemoryTaskDataAccessObject.class);
        TaskAvailable t1 = mockTask("T1");
        TaskAvailable t2 = mockTask("T2");
        when(gateway.getAvailableTaskTemplates()).thenReturn(List.of(t1, t2));

        GoalPageBuilder builder = new GoalPageBuilder();
        setField(builder, "taskGateway", gateway);

        JPanel form = onEdtGet(() -> (JPanel) invoke(builder, "createGoalFormPanel"));
        assertNotNull(form, "Form panel should be created");
        assertTrue(form.getLayout() instanceof BoxLayout, "Form uses BoxLayout Y_AXIS");

        // Amounts panel at index 6 -> [targetAmountPanel, currentAmountPanel]
        JPanel amountPanel = (JPanel) form.getComponent(6);
        JPanel targetAmountPanel = (JPanel) amountPanel.getComponent(0);
        JTextField targetAmountField = (JTextField) targetAmountPanel.getComponent(1);
        assertEquals("0", targetAmountField.getText(), "Default target amount should be 0");

        JPanel currentAmountPanel = (JPanel) amountPanel.getComponent(1);
        JTextField currentAmountField = (JTextField) currentAmountPanel.getComponent(1);
        assertEquals("0", currentAmountField.getText(), "Default current amount should be 0");

        // Date panel at index 8 -> [startDatePanel, endDatePanel]; text fields at component index 1
        LocalDate today = LocalDate.now();
        LocalDate in7 = today.plusDays(7);

        JPanel datePanel = (JPanel) form.getComponent(8);
        JPanel startDatePanel = (JPanel) datePanel.getComponent(0);
        JTextField startDateField = (JTextField) startDatePanel.getComponent(1);
        assertEquals(today.toString(), startDateField.getText(), "Start date defaults to today");

        JPanel endDatePanel = (JPanel) datePanel.getComponent(1);
        JTextField endDateField = (JTextField) endDatePanel.getComponent(1);
        assertEquals(in7.toString(), endDateField.getText(), "End date defaults to +7 days");

        // Time/frequency panel at index 10
        JPanel timeFreqPanel = (JPanel) form.getComponent(10);
        JPanel timePeriodPanel = (JPanel) timeFreqPanel.getComponent(0);
        JComboBox<?> timePeriodBox = (JComboBox<?>) timePeriodPanel.getComponent(1);
        assertEquals(2, timePeriodBox.getItemCount(), "Time period has WEEK and MONTH");
        assertEquals("WEEK", timePeriodBox.getItemAt(0));
        assertEquals("MONTH", timePeriodBox.getItemAt(1));

        JPanel frequencyPanel = (JPanel) timeFreqPanel.getComponent(1);
        JTextField frequencyField = (JTextField) frequencyPanel.getComponent(1);
        assertEquals("1", frequencyField.getText(), "Default frequency is 1");

        // targetTaskBox is a private field set by createGoalFormPanel
        @SuppressWarnings("unchecked")
        JComboBox<TaskAvailable> targetTaskBox = getField(builder, "targetTaskBox", JComboBox.class);
        assertNotNull(targetTaskBox, "targetTaskBox should be initialized");
        assertEquals(2, targetTaskBox.getItemCount(), "targetTaskBox should be populated from gateway");
        assertSame(t1, targetTaskBox.getItemAt(0));
        assertSame(t2, targetTaskBox.getItemAt(1));
    }

    @Test
    void refreshTargetTaskDropdown_preservesSelection() {
        InMemoryTaskDataAccessObject gateway = mock(InMemoryTaskDataAccessObject.class);
        TaskAvailable a = mockTask("A");
        TaskAvailable b = mockTask("B");
        TaskAvailable b2 = mockTask("B"); // different instance, same id (should match)
        TaskAvailable c = mockTask("C");
        when(gateway.getAvailableTaskTemplates()).thenReturn(List.of(a, b), List.of(b2, c));

        GoalPageBuilder builder = new GoalPageBuilder();
        setField(builder, "taskGateway", gateway);

        // Build form once (first call populates [A, B])
        JPanel form = onEdtGet(() -> (JPanel) invoke(builder, "createGoalFormPanel"));
        assertNotNull(form);

        @SuppressWarnings("unchecked")
        JComboBox<TaskAvailable> box = getField(builder, "targetTaskBox", JComboBox.class);
        assertNotNull(box);

        // Select "B"
        onEdtRun(() -> box.setSelectedIndex(1));
        assertSame(b, onEdtGet(box::getSelectedItem));

        // Call refresh (second gateway call returns [B2, C])
        onEdtRun(() -> {
            try {
                Method m = GoalPageBuilder.class.getDeclaredMethod("refreshTargetTaskDropdown");
                m.setAccessible(true);
                m.invoke(builder);
            } catch (Exception e) { throw new RuntimeException(e); }
        });

        // Verify items updated and selection preserved by ID ("B")
        assertEquals(2, box.getItemCount(), "Items should be replaced by new list");
        TaskAvailable selected = (TaskAvailable) onEdtGet(box::getSelectedItem);
        assertNotNull(selected);
        assertEquals("B", selected.getId(), "Selection should stick to same ID across refresh");
        // Also ensure the selected instance is the *new* 'B2' (not the old one)
        assertSame(b2, selected, "Selection should reference the new instance with same ID");
    }

    /* ----------------- helpers ----------------- */

    private static TaskAvailable mockTask(String id) {
        TaskAvailable t = mock(TaskAvailable.class);
        Info info = mock(Info.class);
        when(info.getName()).thenReturn(id + "-name"); // any non-null label is fine
        when(t.getId()).thenReturn(id);
        when(t.getInfo()).thenReturn(info);
        return t;
    }

}
