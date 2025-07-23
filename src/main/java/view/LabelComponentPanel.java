package view;

import javax.swing.*;

/**
 * A panel containing a label and a text field.
 */
public class LabelComponentPanel extends JPanel {
    public LabelComponentPanel(JLabel label, JComponent inputComponent) {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(label);
        this.add(inputComponent);
    }
}
