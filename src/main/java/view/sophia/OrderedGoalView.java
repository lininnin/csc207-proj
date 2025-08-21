package view.Sophia;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import interface_adapter.sophia.order_goal.OrderedGoalViewModel;

/**
 * A Swing panel that displays an ordered list of goals.
 * This view presents goals in a scrollable list format within a titled border.
 */
public class OrderedGoalView extends JPanel {
    /**
     * Constructs an OrderedGoalView with the specified ViewModel.
     * Initializes the panel with a border layout and creates a scrollable list
     * for displaying ordered goals.
     *
     * @param viewModel the ViewModel containing data for ordered goals display
     */
    public OrderedGoalView(OrderedGoalViewModel viewModel) {
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createTitledBorder("Ordered Goals"));

        // Would display ordered goals
        final JList<String> orderedList = new JList<>();
        this.add(new JScrollPane(orderedList), BorderLayout.CENTER);
    }
}
