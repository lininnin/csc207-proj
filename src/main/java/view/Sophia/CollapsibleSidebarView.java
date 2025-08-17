package view.Sophia;

import javax.swing.*;

public class CollapsibleSidebarView extends JSplitPane {
    public CollapsibleSidebarView(JPanel sidebar, JPanel mainContent) {
        super(JSplitPane.HORIZONTAL_SPLIT, sidebar, mainContent);
        this.setDividerSize(8);
        this.setDividerLocation(200);
        this.setResizeWeight(0);
    }
}