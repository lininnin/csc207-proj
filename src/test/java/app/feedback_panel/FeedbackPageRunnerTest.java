package app.feedback_panel;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class FeedbackPageRunnerTest {

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

    private static JFrame waitForVisibleFrameByTitle(String title, long timeoutMs) {
        long deadline = System.currentTimeMillis() + timeoutMs;
        while (System.currentTimeMillis() < deadline) {
            JFrame found = onEdtGet(() ->
                    java.util.Arrays.stream(java.awt.Frame.getFrames())
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

    /* ---------- Tests ---------- */

    @Test
    void createAndShowUi_buildsVisibleFrame_withCenterContent() throws Exception {
        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless(), "Headless environment");

        Method m = FeedbackPageRunner.class.getDeclaredMethod("createAndShowUi");
        m.setAccessible(true);
        onEdtRun(() -> { try { m.invoke(null); } catch (Exception e) { throw new RuntimeException(e); } });

        JFrame frame = waitForVisibleFrameByTitle("Feedback Page", 5000);
        assertNotNull(frame, "Feedback Page frame should be created and visible");
        try {
            assertTrue(onEdtGet(frame::isVisible), "Frame should be visible");
            assertTrue(frame.getLayout() instanceof BorderLayout, "Frame should use BorderLayout");

            Component center = ((BorderLayout) frame.getLayout()).getLayoutComponent(BorderLayout.CENTER);
            assertNotNull(center, "Center component (feedback page) should exist");
            assertTrue(center instanceof JComponent, "Center component should be a Swing component");
        } finally {
            disposeFramesByTitle("Feedback Page");
        }
    }

    @Test
    void main_runs_withoutThrowing() {
        // Smoke test: we just ensure it doesn't crash.
        // (Detailed UI assertions are in createAndShowUi test.)
        assertDoesNotThrow(() -> FeedbackPageRunner.main(new String[0]));
    }
}
