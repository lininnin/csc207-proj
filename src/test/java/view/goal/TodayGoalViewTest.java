package view.goal;

import entity.Sophia.Goal;
import entity.Sophia.GoalInfo;
import entity.info.Info;
import view.Sophia.*;
import interface_adapter.sophia.today_goal.TodayGoalController;
import interface_adapter.sophia.today_goal.TodayGoalsViewModel;
import interface_adapter.sophia.today_goal.TodaysGoalsState;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TodayGoalViewTest {

    /* ------------- EDT helpers ------------- */

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

    /* ------------- tests ------------- */

    @Test
    void initialState_listExists_empty_singleSelection() {
        TodayGoalsViewModel vm = mock(TodayGoalsViewModel.class);
        TodayGoalController controller = mock(TodayGoalController.class);

        TodayGoalView view = onEdtGet(() -> new TodayGoalView(vm, controller));

        JList<?> list = findFirst(view, JList.class);
        assertNotNull(list, "JList should be created");
        assertEquals(ListSelectionModel.SINGLE_SELECTION, onEdtGet(list::getSelectionMode));
        assertEquals(0, onEdtGet(() -> list.getModel().getSize()), "List should be empty until state arrives");
    }

    @Test
    void propertyChange_updatesListWithGoals() {
        // Two goals: one incomplete, one completed
        Goal g1 = mockGoal("Read", "30 pages", 1, 3); // incomplete
        Goal g2 = mockGoal("Run", "5km", 3, 3);       // completed

        TodaysGoalsState state = new TodaysGoalsState();
        state.setTodayGoals(List.of(g1, g2));

        TodayGoalsViewModel vm = mock(TodayGoalsViewModel.class);
        TodayGoalController controller = mock(TodayGoalController.class);

        TodayGoalView view = onEdtGet(() -> new TodayGoalView(vm, controller));
        JList<?> list = findFirst(view, JList.class);
        assertEquals(0, onEdtGet(() -> list.getModel().getSize()));

        // Fire the property change that the view listens to
        onEdtRun(() -> view.propertyChange(new PropertyChangeEvent(vm, "state", null, state)));

        assertEquals(2, onEdtGet(() -> list.getModel().getSize()), "List model should reflect provided goals");
        assertSame(g1, onEdtGet(() -> list.getModel().getElementAt(0)));
        assertSame(g2, onEdtGet(() -> list.getModel().getElementAt(1)));
    }

    @Test
    void cellRenderer_formatsText_andHighlightsCompleted() {
        Goal incomplete = mockGoal("Goal A", "desc A", 2, 5); // not done
        Goal completed  = mockGoal("Goal B", "desc B", 4, 4); // done

        TodayGoalsViewModel vm = mock(TodayGoalsViewModel.class);
        TodayGoalController controller = mock(TodayGoalController.class);
        TodayGoalView view = onEdtGet(() -> new TodayGoalView(vm, controller));

        JList<Goal> list = onEdtGet(() -> {
            @SuppressWarnings("unchecked")
            JList<Goal> l = (JList<Goal>) findFirst(view, JList.class);
            return l;
        });
        assertNotNull(list);

        ListCellRenderer<? super Goal> renderer = onEdtGet(list::getCellRenderer);
        assertNotNull(renderer);

        // Render incomplete
        Component c1 = onEdtGet(() ->
                renderer.getListCellRendererComponent(list, incomplete, 0, false, false));
        assertTrue(c1 instanceof JLabel);
        Color expectedBgIncomplete = onEdtGet(list::getBackground);
        assertEquals(expectedBgIncomplete, c1.getBackground(), "Incomplete goal uses list background");
        String html1 = ((JLabel) c1).getText();
        assertTrue(html1.contains("Goal A"), "Renderer text should include goal name");
        assertTrue(html1.contains("Progress: 2 of 5"), "Renderer text should include progress");

        // Render completed -> light green background (200,255,200)
        Component c2 = onEdtGet(() ->
                renderer.getListCellRendererComponent(list, completed, 1, false, false));
        assertEquals(new Color(200, 255, 200), c2.getBackground(),
                "Completed goal should be highlighted in green");
        String html2 = ((JLabel) c2).getText();
        assertTrue(html2.contains("Goal B"));
        assertTrue(html2.contains("Progress: 4 of 4"));
    }

    /* ------------- helpers ------------- */

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

    private static Goal mockGoal(String name, String desc, int current, int freq) {
        Info info = mock(Info.class);
        when(info.getName()).thenReturn(name);
        when(info.getDescription()).thenReturn(desc);

        GoalInfo gi = mock(GoalInfo.class);
        when(gi.getInfo()).thenReturn(info);

        Goal g = mock(Goal.class);
        when(g.getGoalInfo()).thenReturn(gi);
        when(g.getCurrentProgress()).thenReturn(current);
        when(g.getFrequency()).thenReturn(freq);
        return g;
    }
}
