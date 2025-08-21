package app;

import app.alex.eventPage.EventPageBuilder;
import app.taskPage.TaskPageBuilder;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RunAppTest {

    /* ========= tiny EDT helpers ========= */

    private static <T> T onEdtGet(java.util.concurrent.Callable<T> c) {
        final java.util.concurrent.atomic.AtomicReference<T> out = new java.util.concurrent.atomic.AtomicReference<>();
        final java.util.concurrent.atomic.AtomicReference<Throwable> err = new java.util.concurrent.atomic.AtomicReference<>();
        try { SwingUtilities.invokeAndWait(() -> { try { out.set(c.call()); } catch (Throwable t) { err.set(t); } }); }
        catch (Exception e) { throw new RuntimeException(e); }
        if (err.get() != null) throw new RuntimeException(err.get());
        return out.get();
    }
    private static void onEdtRun(Runnable r) {
        try { SwingUtilities.invokeAndWait(r); } catch (Exception e) { throw new RuntimeException(e); }
    }

    /* ========= reflection helpers ========= */

    private static Method getConfigureButton() {
        try {
            Method m = app.RunApp.class.getDeclaredMethod(
                    "configureButton", String.class, JPanel.class, TaskPageBuilder.class, EventPageBuilder.class);
            m.setAccessible(true);
            return m;
        } catch (Exception e) { throw new RuntimeException(e); }
    }

    private static Method getMenuSelection() {
        try {
            Method m = app.RunApp.class.getDeclaredMethod(
                    "menuSelection", String.class, JPanel.class, TaskPageBuilder.class, EventPageBuilder.class);
            m.setAccessible(true);
            return m;
        } catch (Exception e) { throw new RuntimeException(e); }
    }

    /* ========= tests ========= */

    @Test
    void configureButton_setsExpectedProperties_forTasks() throws Exception {
        JPanel center = onEdtGet(() -> new JPanel(new CardLayout()));

        JButton btn = onEdtGet(() -> {
            try {
                return (JButton) getConfigureButton().invoke(
                        null, "📋 Tasks", center, null, null);
            } catch (Exception e) { throw new RuntimeException(e); }
        });

        assertEquals("📋 Tasks", btn.getText());
        assertTrue(btn.isOpaque());
        assertFalse(btn.isBorderPainted());
        assertEquals(Color.WHITE, btn.getForeground());
        assertEquals(new Dimension(constants.Constants.TWO_HUNDRED, constants.Constants.FORTY), btn.getMaximumSize());
        assertEquals(Component.CENTER_ALIGNMENT, btn.getAlignmentX(), 0.0001);

        // Tasks button should have the highlighted background
        Color expected = new Color(constants.Constants.FORTY_FIVE,
                constants.Constants.FORTY_SEV,
                constants.Constants.FORTY_NINE);
        assertEquals(expected, btn.getBackground());
    }

    @Test
    void clickingEventsButton_navigatesToEvents_andCallsRefresh() throws Exception {
        JPanel centrePanel = onEdtGet(() -> {
            JPanel p = new JPanel(new CardLayout());
            JPanel tasks = new JPanel(); tasks.setName("Tasks"); p.add(tasks, "Tasks");
            JPanel events = new JPanel(); events.setName("Events"); p.add(events, "Events");
            ((CardLayout) p.getLayout()).show(p, "Tasks");
            return p;
        });

        // Mocks so we can verify refresh is called
        TaskPageBuilder taskBuilder = mock(TaskPageBuilder.class);
        EventPageBuilder eventBuilder = mock(EventPageBuilder.class);

        JButton eventsBtn = onEdtGet(() -> {
            try {
                return (JButton) getConfigureButton().invoke(
                        null, "📆 Events", centrePanel, taskBuilder, eventBuilder);
            } catch (Exception e) { throw new RuntimeException(e); }
        });

        // Click -> should show "Events" card and call eventBuilder.refreshViews()
        onEdtRun(eventsBtn::doClick);

        Component[] comps = centrePanel.getComponents();
        Component tasks = comps[0];
        Component events = comps[1];

        assertFalse(tasks.isVisible(), "Tasks card should be hidden after clicking Events");
        assertTrue(events.isVisible(), "Events card should be visible after clicking Events");

        verify(eventBuilder, times(1)).refreshViews();
        verifyNoInteractions(taskBuilder);
    }

    @Test
    void menuSelection_switchesCards_forAllSections() throws Exception {
        JPanel centrePanel = onEdtGet(() -> {
            JPanel p = new JPanel(new CardLayout());
            JPanel tasks = new JPanel(); p.add(tasks, "Tasks");
            JPanel events = new JPanel(); p.add(events, "Events");
            JPanel goals = new JPanel(); p.add(goals, "Goals");
            JPanel wellness = new JPanel(); p.add(wellness, "WellnessLog");
            JPanel feedback = new JPanel(); p.add(feedback, "FeedbackPage");
            JPanel settings = new JPanel(); p.add(settings, "Settings");
            ((CardLayout) p.getLayout()).show(p, "Tasks");
            return p;
        });

        // Use mocks so refresh hooks can be invoked without real builders
        TaskPageBuilder taskBuilder = mock(TaskPageBuilder.class);
        EventPageBuilder eventBuilder = mock(EventPageBuilder.class);

        // Helper to invoke private method
        java.util.function.Consumer<String> select = item -> onEdtRun(() -> {
            try {
                getMenuSelection().invoke(null, item, centrePanel, taskBuilder, eventBuilder);
            } catch (Exception e) { throw new RuntimeException(e); }
        });

        // Events
        select.accept("📆 Events");
        assertCardVisible(centrePanel, "Events");
        verify(eventBuilder, times(1)).refreshViews();

        // Goals
        select.accept("🎯 Goals");
        assertCardVisible(centrePanel, "Goals");

        // Wellness Log
        select.accept("🧠 Wellness Log");
        assertCardVisible(centrePanel, "WellnessLog");

        // Feedback
        select.accept("🤖 AI-Feedback & Analysis");
        assertCardVisible(centrePanel, "FeedbackPage");

        // Settings
        select.accept("⚙️ Settings");
        assertCardVisible(centrePanel, "Settings");

        // Back to Tasks (also verifies task refresh called once)
        select.accept("📋 Tasks");
        assertCardVisible(centrePanel, "Tasks");
        verify(taskBuilder, times(1)).refreshViews();
    }

    /* ========= small helper ========= */

    private static void assertCardVisible(JPanel container, String cardName) {
        Component target = java.util.Arrays.stream(container.getComponents())
                .filter(c -> container.getLayout() instanceof CardLayout)
                .filter(c -> {
                    // CardLayout doesn't expose the constraint; track by visibility:
                    // Only the shown card is visible.
                    return c.isVisible();
                })
                .findFirst()
                .orElse(null);
        assertNotNull(target, "Some card should be visible");
        // Ensure the visible one is the expected reference
        // We added components in known order; map by name using a lookup:
        Component expected = switch (cardName) {
            case "Tasks" -> container.getComponent(0);
            case "Events" -> container.getComponent(1);
            case "Goals" -> container.getComponent(2);
            case "WellnessLog" -> container.getComponent(3);
            case "FeedbackPage" -> container.getComponent(4);
            case "Settings" -> container.getComponent(5);
            default -> null;
        };
        assertSame(expected, target, "Expected visible card: " + cardName);
    }
}
