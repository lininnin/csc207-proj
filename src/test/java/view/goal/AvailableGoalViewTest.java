package view.goal;

import entity.BeginAndDueDates.BeginAndDueDates;
import entity.Sophia.Goal;
import entity.Sophia.GoalInfo;
import entity.info.Info;
import interface_adapter.sophia.available_goals.AvailableGoalsController;
import interface_adapter.sophia.available_goals.AvailableGoalsState;
import interface_adapter.sophia.available_goals.AvailableGoalsViewModel;
import org.junit.jupiter.api.Test;
import view.Sophia.AvailableGoalsView;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AvailableGoalViewTest {

    /* --------------- EDT helpers --------------- */

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

    /* --------------- tests --------------- */

    @Test
    void initialRender_populatesList_andButtonsDisabled() {
        Goal g1 = mockGoal("Read", "30 pages", 1, 10,
                LocalDate.of(2025, 8, 1), LocalDate.of(2025, 8, 31), Goal.TimePeriod.MONTH);
        Goal g2 = mockGoal("Run", "5km easy", 0, 3,
                LocalDate.of(2025, 8, 20), LocalDate.of(2025, 8, 27), Goal.TimePeriod.WEEK);

        AvailableGoalsState s = new AvailableGoalsState();
        s.setAvailableGoals(List.of(g1, g2));

        AvailableGoalsViewModel vm = mock(AvailableGoalsViewModel.class);
        when(vm.getState()).thenReturn(s);
        AvailableGoalsController controller = mock(AvailableGoalsController.class);

        AvailableGoalsView view = onEdtGet(() -> new AvailableGoalsView(vm, controller));

        JList<?> list = findFirst(view, JList.class);
        assertNotNull(list, "Goals JList should exist");
        assertEquals(2, onEdtGet(() -> list.getModel().getSize()));

        JButton addBtn = findButton(view, "Add to Today");
        JButton delBtn = findButton(view, "Delete");
        assertNotNull(addBtn);
        assertNotNull(delBtn);
        assertFalse(onEdtGet(addBtn::isEnabled));
        assertFalse(onEdtGet(delBtn::isEnabled));
    }

    @Test
    void propertyChange_state_refreshesList() {
        Goal g1 = mockGoal("G1", "D1", 0, 1,
                LocalDate.of(2025, 8, 1), LocalDate.of(2025, 8, 2), Goal.TimePeriod.WEEK);
        Goal g2 = mockGoal("G2", "D2", 0, 1,
                LocalDate.of(2025, 8, 3), LocalDate.of(2025, 8, 4), Goal.TimePeriod.WEEK);
        Goal g3 = mockGoal("G3", "D3", 0, 1,
                LocalDate.of(2025, 8, 5), LocalDate.of(2025, 8, 6), Goal.TimePeriod.WEEK);

        AvailableGoalsState s1 = new AvailableGoalsState();
        s1.setAvailableGoals(List.of(g1));

        AvailableGoalsState s2 = new AvailableGoalsState();
        s2.setAvailableGoals(List.of(g1, g2, g3));

        AvailableGoalsViewModel vm = mock(AvailableGoalsViewModel.class);
        // first call during constructor -> s1, second when refreshing -> s2
        when(vm.getState()).thenReturn(s1, s2);

        AvailableGoalsController controller = mock(AvailableGoalsController.class);
        AvailableGoalsView view = onEdtGet(() -> new AvailableGoalsView(vm, controller));

        JList<?> list = findFirst(view, JList.class);
        assertEquals(1, onEdtGet(() -> list.getModel().getSize()));

        onEdtRun(() -> view.propertyChange(new PropertyChangeEvent(vm, "state", s1, s2)));

        assertEquals(3, onEdtGet(() -> list.getModel().getSize()));
    }

    @Test
    void selection_enablesButtons_andGetSelectedGoal() {
        Goal g1 = mockGoal("Alpha", "desc", 2, 5,
                LocalDate.of(2025, 8, 20), LocalDate.of(2025, 8, 27), Goal.TimePeriod.WEEK);
        Goal g2 = mockGoal("Beta", "desc", 0, 3,
                LocalDate.of(2025, 8, 20), LocalDate.of(2025, 9, 20), Goal.TimePeriod.MONTH);

        AvailableGoalsState s = new AvailableGoalsState();
        s.setAvailableGoals(List.of(g1, g2));

        AvailableGoalsViewModel vm = mock(AvailableGoalsViewModel.class);
        when(vm.getState()).thenReturn(s);
        AvailableGoalsController controller = mock(AvailableGoalsController.class);

        AvailableGoalsView view = onEdtGet(() -> new AvailableGoalsView(vm, controller));
        JList<?> list = findFirst(view, JList.class);
        JButton addBtn = findButton(view, "Add to Today");
        JButton delBtn = findButton(view, "Delete");

        assertFalse(onEdtGet(addBtn::isEnabled));
        assertFalse(onEdtGet(delBtn::isEnabled));

        onEdtRun(() -> list.setSelectedIndex(1));

        assertTrue(onEdtGet(addBtn::isEnabled));
        assertTrue(onEdtGet(delBtn::isEnabled));

        Goal selected = onEdtGet(view::getSelectedGoal);
        assertSame(g2, selected);
    }

    /* --------------- helpers --------------- */

    private static <T extends Component> T findFirst(Container root, Class<T> type) {
        for (Component c : root.getComponents()) {
            if (type.isInstance(c)) return type.cast(c);
            if (c instanceof Container child) {
                T found = findFirst(child, type);
                if (found != null) return found;
            }
        }
        return null;
    }

    private static JButton findButton(Container root, String text) {
        for (Component c : root.getComponents()) {
            if (c instanceof JButton b && text.equals(b.getText())) return b;
            if (c instanceof Container child) {
                JButton found = findButton(child, text);
                if (found != null) return found;
            }
        }
        return null;
    }

    private static Goal mockGoal(String name, String desc, int progress, int freq,
                                 LocalDate begin, LocalDate due, Goal.TimePeriod tp) {
        Info info = mock(Info.class);
        when(info.getName()).thenReturn(name);
        when(info.getDescription()).thenReturn(desc);

        GoalInfo gi = mock(GoalInfo.class);
        when(gi.getInfo()).thenReturn(info);

        BeginAndDueDates dates = mock(BeginAndDueDates.class);
        when(dates.getBeginDate()).thenReturn(begin);
        when(dates.getDueDate()).thenReturn(due);

        Goal g = mock(Goal.class);
        when(g.getGoalInfo()).thenReturn(gi);
        when(g.getBeginAndDueDates()).thenReturn(dates);
        when(g.getCurrentProgress()).thenReturn(progress);
        when(g.getFrequency()).thenReturn(freq);
        when(g.getTimePeriod()).thenReturn(tp);
        return g;
    }
}
