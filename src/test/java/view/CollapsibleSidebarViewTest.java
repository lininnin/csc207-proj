package view;

import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class CollapsibleSidebarViewTest {

    /* ------------ EDT helpers ------------ */

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

    /* ------------ helpers ------------ */

    private static JSplitPane centerSplit(JPanel root) {
        BorderLayout bl = (BorderLayout) root.getLayout();
        Component c = bl.getLayoutComponent(BorderLayout.CENTER);
        assertNotNull(c, "CENTER should contain the split pane");
        assertTrue(c instanceof JSplitPane, "CENTER component should be a JSplitPane");
        return (JSplitPane) c;
    }

    private static JButton findToggleButton(JPanel root) {
        BorderLayout bl = (BorderLayout) root.getLayout();
        Component north = bl.getLayoutComponent(BorderLayout.NORTH);
        assertNotNull(north, "NORTH should contain the top bar");
        assertTrue(north instanceof JPanel, "Top bar should be a JPanel");
        JPanel topBar = (JPanel) north;
        for (Component comp : topBar.getComponents()) {
            if (comp instanceof JButton b && "☰".equals(b.getText())) return b;
        }
        return null;
    }

    private static JPanel sizedContainerWith(JPanel child) {
        JPanel host = new JPanel(new BorderLayout());
        host.setSize(800, 600);
        host.add(child, BorderLayout.CENTER);
        host.doLayout();
        host.validate();
        return host;
    }

    /* ------------ tests ------------ */

    @Test
    void initialState_hasSidebarOnLeft_andDividerDefaults() {
        JPanel sidebar = onEdtGet(() -> { JPanel p = new JPanel(); p.setName("sidebar"); return p; });
        JPanel main = onEdtGet(() -> { JPanel p = new JPanel(); p.setName("main"); return p; });

        JPanel view = onEdtGet(() -> new CollapsibleSidebarView(sidebar, main));
        // Ensure layout happens so divider location applies
        JPanel host = onEdtGet(() -> sizedContainerWith(view));

        JSplitPane split = onEdtGet(() -> centerSplit(view));

        // Left component should be our sidebar initially
        Component left = onEdtGet(split::getLeftComponent);
        assertSame(sidebar, left, "Sidebar should be on the left initially");

        // Divider size should be 2 as set by the component
        assertEquals(2, onEdtGet(split::getDividerSize), "Divider size should be 2");

        // With a sized container, divider location should be the configured 200
        assertEquals(200, onEdtGet(split::getDividerLocation), "Divider location should be 200 initially");

        // Right component should be the main content and remain unchanged
        Component right = onEdtGet(split::getRightComponent);
        assertSame(main, right, "Main content must be on the right");
    }

    @Test
    void toggleSidebar_hides_then_shows_programmatically() {
        JPanel sidebar = onEdtGet(JPanel::new);
        JPanel main = onEdtGet(JPanel::new);

        JPanel view = onEdtGet(() -> new CollapsibleSidebarView(sidebar, main));
        onEdtGet(() -> sizedContainerWith(view)); // layout

        JSplitPane split = onEdtGet(() -> centerSplit(view));

        // Hide
        onEdtRun(((CollapsibleSidebarView) view)::toggleSidebar);
        Component leftHidden = onEdtGet(split::getLeftComponent);
        assertNotSame(sidebar, leftHidden, "Left component should be replaced when hidden");
        assertEquals(0, onEdtGet(split::getDividerSize), "Divider size should be 0 when hidden");
        assertEquals(0, onEdtGet(split::getDividerLocation), "Divider location should be 0 when hidden");

        // Show again
        onEdtRun(((CollapsibleSidebarView) view)::toggleSidebar);
        Component leftShown = onEdtGet(split::getLeftComponent);
        assertSame(sidebar, leftShown, "Sidebar should be restored when shown");
        assertEquals(2, onEdtGet(split::getDividerSize), "Divider size should return to 2 when shown");
        assertEquals(200, onEdtGet(split::getDividerLocation), "Divider should return to 200 when shown");
    }

    @Test
    void toggleButton_click_hides_and_shows() {
        JPanel sidebar = onEdtGet(JPanel::new);
        JPanel main = onEdtGet(JPanel::new);

        JPanel view = onEdtGet(() -> new CollapsibleSidebarView(sidebar, main));
        onEdtGet(() -> sizedContainerWith(view)); // layout
        JSplitPane split = onEdtGet(() -> centerSplit(view));

        JButton toggle = onEdtGet(() -> findToggleButton(view));
        assertNotNull(toggle, "Toggle button should be present with label ☰");

        // Click to hide
        onEdtRun(toggle::doClick);
        assertEquals(0, onEdtGet(split::getDividerSize));
        assertEquals(0, onEdtGet(split::getDividerLocation));

        // Click to show
        onEdtRun(toggle::doClick);
        assertEquals(2, onEdtGet(split::getDividerSize));
        assertEquals(200, onEdtGet(split::getDividerLocation));
        assertSame(sidebar, onEdtGet(split::getLeftComponent));
    }
}
