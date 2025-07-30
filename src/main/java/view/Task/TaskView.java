package view.Task;

import interface_adapter.view_model.TaskViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Main view for task management.
 * Displays both Available Tasks and Today's Tasks.
 */
public class TaskView extends JPanel implements PropertyChangeListener {
    public final String viewName = "task";
    private final TaskViewModel taskViewModel;

    public TaskView(TaskViewModel taskViewModel) {
        this.taskViewModel = taskViewModel;
        this.taskViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());

        // Placeholder content
        JLabel label = new JLabel("Task Management - To Be Implemented");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // Update view based on view model changes
    }
}