package view;

import interface_adapter.view_model.CategoryViewModel;
import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Dialog view for managing categories.
 * Allows users to create, edit, and delete categories.
 */
public class CategoryDialogView extends JPanel implements PropertyChangeListener {
    public final String viewName = "category";
    private final CategoryViewModel categoryViewModel;

    public CategoryDialogView(CategoryViewModel categoryViewModel) {
        this.categoryViewModel = categoryViewModel;
        this.categoryViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());

        // Placeholder content
        JLabel label = new JLabel("Category Management - To Be Implemented");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            CategoryViewModel.CategoryState state = categoryViewModel.getState();
            // Update view based on new state
            // TODO: Implement view updates
        }
    }
}