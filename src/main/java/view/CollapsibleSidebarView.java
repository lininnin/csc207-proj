package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

/**
 * Sidebar + main panel container that supports collapsing sidebar.
 */
public class CollapsibleSidebarView extends JPanel {
    private static final int SIDEBAR_WIDTH = 200;

    private final JPanel sidebarPanel;
    private final JPanel mainContentPanel;
    private final JSplitPane splitPane;
    private boolean sidebarVisible = true;

    public CollapsibleSidebarView(JPanel sidebarPanel, JPanel mainContentPanel) {
        this.sidebarPanel = sidebarPanel;
        this.mainContentPanel = mainContentPanel;

        setLayout(new BorderLayout());

        // Split Pane
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidebarPanel, mainContentPanel);
        splitPane.setDividerLocation(SIDEBAR_WIDTH);
        splitPane.setOneTouchExpandable(false);
        splitPane.setDividerSize(2);
        splitPane.setResizeWeight(0);
        // mainContentPanel expands

        // Toggle Button
        final JButton toggleButton = new JButton("☰");
        toggleButton.setFocusPainted(false);
        toggleButton.addActionListener(event -> toggleSidebar());

        final JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topBar.add(toggleButton);

        add(topBar, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
    }

    public void toggleSidebar() {
        if (sidebarVisible) {
            final JPanel empty = new JPanel();
            empty.setPreferredSize(new Dimension(0, 0));
            splitPane.setLeftComponent(empty);
            splitPane.setDividerSize(0);
            splitPane.setDividerLocation(0);
        }
        else {
            splitPane.setLeftComponent(sidebarPanel);
            splitPane.setDividerSize(2);
            splitPane.setDividerLocation(SIDEBAR_WIDTH);
        }
        sidebarVisible = !sidebarVisible;

        revalidate();
        repaint();
    }
}
