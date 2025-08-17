package views;

import interface_adapter.sophia.order_goal.OrderedGoalViewModel;
import javax.swing.*;
import java.awt.*;

public class OrderedGoalView extends JPanel {
    public OrderedGoalView(OrderedGoalViewModel viewModel) {
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createTitledBorder("Ordered Goals"));

        // Would display ordered goals
        JList<String> orderedList = new JList<>();
        this.add(new JScrollPane(orderedList), BorderLayout.CENTER);
    }
}