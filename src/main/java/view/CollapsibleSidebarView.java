package view;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

/**
 * A collapsible sidebar container with dynamic main panel injection and button-driven view switching.
 */
public class CollapsibleSidebarView extends JPanel {
    private static final int SIDEBAR_WIDTH = 200;
    private boolean isSidebarVisible = true;

    private final JSplitPane splitPane;
    private final JPanel sidebar;

    public CollapsibleSidebarView(JPanel mainContentPanel) {
        this.setLayout(new BorderLayout());

        // Sidebar: constructed with ability to control main content switching
        this.sidebar = createSidebar(mainContentPanel);

        // Split pane: sidebar + main panel
        this.splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidebar, mainContentPanel);
        splitPane.setDividerLocation(SIDEBAR_WIDTH);
        splitPane.setOneTouchExpandable(false);
        splitPane.setDividerSize(2);

        // Toggle sidebar button
        JButton toggleButton = new JButton("☰");
        toggleButton.setFocusPainted(false);
        toggleButton.addActionListener(e -> toggleSidebar());

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topBar.add(toggleButton);

        this.add(topBar, BorderLayout.NORTH);
        this.add(splitPane, BorderLayout.CENTER);
    }

    private void toggleSidebar() {
        if (isSidebarVisible) {
            splitPane.setDividerLocation(0);
            splitPane.getLeftComponent().setVisible(false);
        } else {
            splitPane.getLeftComponent().setVisible(true);
            splitPane.setDividerLocation(SIDEBAR_WIDTH);
        }
        isSidebarVisible = !isSidebarVisible;
    }

    private JPanel createSidebar(JPanel mainContentPanel) {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(60, 63, 65));
        sidebar.setPreferredSize(new Dimension(SIDEBAR_WIDTH, 0));

        // Button definitions
        sidebar.add(createSidebarButton("📋 Tasks", () -> switchToCard(mainContentPanel, "Tasks")));
        sidebar.add(createSidebarButton("📆 Events", () -> switchToCard(mainContentPanel, "Events")));
        sidebar.add(createSidebarButton("🎯 Goals", () -> switchToCard(mainContentPanel, "Goals")));
        sidebar.add(createSidebarButton("🧠 Wellness Log", () -> switchToCard(mainContentPanel, "Wellness")));
        sidebar.add(createSidebarButton("📊 Charts", () -> switchToCard(mainContentPanel, "Charts")));
        sidebar.add(createSidebarButton("🤖 AI-Feedback & Analysis", () -> switchToCard(mainContentPanel, "AI")));
        sidebar.add(createSidebarButton("⚙️ Settings", () -> switchToCard(mainContentPanel, "Settings")));

        return sidebar;
    }

    private JButton createSidebarButton(String text, Runnable onClick) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        button.setFocusPainted(false);
        button.addActionListener(e -> onClick.run());
        return button;
    }

    private void switchToCard(JPanel panel, String cardName) {
        LayoutManager layout = panel.getLayout();
        if (layout instanceof CardLayout) {
            ((CardLayout) layout).show(panel, cardName);
        }
    }
}
