package app.goalPage;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class GoalPageRunnerTest {

    /* ---------- EDT helpers ---------- */

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

    /* ---------- utilities ---------- */

    /** Poll up to timeoutMs for a *visible* frame with the given title to appear. */
    private static JFrame waitForVisibleFrameByTitle(String title, long timeoutMs) {
        long deadline = System.currentTimeMillis() + timeoutMs;
        while (System.currentTimeMillis() < deadline) {
            JFrame found = onEdtGet(() ->
                    java.util.Arrays.stream(Frame.getFrames())
                            .filter(f -> f instanceof JFrame)
                            .map(f -> (JFrame) f)
                            .filter(f -> title.equals(f.getTitle()))
                            .findFirst()
                            .orElse(null)
            );
            if (found != null && onEdtGet(found::isVisible)) return found;
            try { Thread.sleep(20); } catch (InterruptedException ignored) {}
        }
        return null;
    }

    private static void disposeFramesByTitle(String title) {
        onEdtRun(() -> {
            for (Frame f : Frame.getFrames()) {
                if (f instanceof JFrame && title.equals(f.getTitle())) {
                    f.setVisible(false);
                    f.dispose();
                }
            }
        });
    }

    /* ---------- tests ---------- */

    @Test
    void showGoalManagementFrame_buildsVisibleFrame_withContent() throws Exception {
        // Skip in headless environments (e.g., CI without X server)
        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless(), "Headless environment");

        GoalPageRunner runner = new GoalPageRunner();

        // Call the private method that main() ultimately triggers
        Method m = GoalPageRunner.class.getDeclaredMethod("showGoalManagementFrame");
        m.setAccessible(true);
        onEdtRun(() -> {
            try { m.invoke(runner); } catch (Exception e) { throw new RuntimeException(e); }
        });

        JFrame frame = waitForVisibleFrameByTitle("MindTrack - Goal Management", 5000);
        assertNotNull(frame, "Frame should be created and visible");
        try {
            assertTrue(onEdtGet(frame::isVisible), "Frame should be visible");
            // Content should be the page built by GoalPageBuilder
            JRootPane root = onEdtGet(frame::getRootPane);
            assertNotNull(root);
            Container content = onEdtGet(frame::getContentPane);
            assertNotNull(content);
            assertTrue(content.getComponentCount() > 0, "Content pane should contain the goal page UI");
        } finally {
            disposeFramesByTitle("MindTrack - Goal Management");
        }
    }

    @Test
    void run_doesNotThrow() {
        // Smoke test: ensure invoking run() doesn’t crash (no UI assertions here).
        assertDoesNotThrow(() -> new GoalPageRunner().run());
    }
}
