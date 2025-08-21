// File: src/test/java/view/LabelComponentPanelTest.java
package view;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class LabelComponentPanelTest {

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
    void constructsWithGivenLabelAndComponent() {
        onEDT(() -> {
            JLabel label = new JLabel("Name:");
            JTextField field = new JTextField(10);

            LabelComponentPanel panel = new LabelComponentPanel(label, field);

            // Panel should have exactly 2 children
            Component[] children = panel.getComponents();
            assertEquals(2, children.length, "Panel should contain exactly two components");

            // They should be exactly the ones we passed, in order
            assertSame(label, children[0], "First child should be the label instance passed in");
            assertSame(field, children[1], "Second child should be the input component instance passed in");
        });
    }

    @Test
    void usesBoxLayoutInstance() {
        onEDT(() -> {
            JLabel label = new JLabel("Email:");
            JPasswordField input = new JPasswordField(12);

            LabelComponentPanel panel = new LabelComponentPanel(label, input);

            // We only assert that BoxLayout is used; JDK modules prevent reflecting the axis.
            LayoutManager lm = panel.getLayout();
            assertTrue(lm instanceof BoxLayout, "Layout should be a BoxLayout");

            // Sanity: components are laid out in the same container in insertion order (horizontal for our usage).
            Component[] children = panel.getComponents();
            assertSame(label, children[0]);
            assertSame(input, children[1]);
        });
    }

    @Test
    void labelAndInputAreUsableAfterConstruction() {
        onEDT(() -> {
            JLabel label = new JLabel("Title:");
            JTextField field = new JTextField();
            LabelComponentPanel panel = new LabelComponentPanel(label, field);

            // Interact with components to ensure no side-effects from layout
            field.setText("hello");
            assertEquals("hello", field.getText());

            label.setText("New Title:");
            assertEquals("New Title:", ((JLabel) panel.getComponent(0)).getText());
        });
    }
}
