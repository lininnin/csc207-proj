package app;

import interface_adapter.Alex.create_event.CreateEventController;
import interface_adapter.Alex.create_event.CreatedEventViewModel;
import use_case.Alex.create_event.CreateEventInputBoundary;
import view.Alex.EventPageView;
import view.CollapsibleSidebarView;

import javax.swing.*;
import java.awt.*;

class CreateEventTestApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("MindTrack");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 700);

            // --- ViewModel + Mock UseCase ---
            CreatedEventViewModel viewModel = new CreatedEventViewModel();
            CreateEventInputBoundary mockUseCase = inputData -> {
                System.out.println("Mock Created: " + inputData.getName());
                JOptionPane.showMessageDialog(null, "Mock Created: " + inputData.getName());
            };
            CreateEventController controller = new CreateEventController(mockUseCase);

            // --- Sidebar ---
            JPanel sidebar = new CollapsibleSidebarView(new JPanel());

            // --- CreateEventView (上左部分上半) ---
            EventPageView eventPageView = new EventPageView(viewModel);
            eventPageView.setCreateEventController(controller);
            eventPageView.setPreferredSize(new Dimension(300, 40));

            // --- 中央区域左边：上 = NewAvailableEvent，下 = 预留 ---
            JPanel upperLeftTop = eventPageView;
            JPanel upperLeftBottom = new JPanel(); // 可放 future box
            upperLeftBottom.setBackground(new Color(220, 220, 220));

            JSplitPane verticalLeftSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, upperLeftTop, upperLeftBottom);
            verticalLeftSplit.setResizeWeight(0.5); // 上下平分
            verticalLeftSplit.setDividerSize(2);
            verticalLeftSplit.setEnabled(false);

            // --- 中央区域右边：占右侧上半 ---
            JPanel upperRightPanel = new JPanel();
            upperRightPanel.setBackground(new Color(240, 240, 255));

            // --- 上方整行区域：左侧是verticalLeftSplit，右侧是upperRightPanel ---
            JPanel topCenterRow = new JPanel(new GridLayout(1, 2));
            topCenterRow.add(verticalLeftSplit);
            topCenterRow.add(upperRightPanel);

            // --- Bottom Box ---
            JPanel bottomBox = new JPanel();
            bottomBox.setPreferredSize(new Dimension(800, 350));
            bottomBox.setBackground(Color.GRAY);

            // --- 中央总区域：上方两列 + 底部一列 ---
            JPanel centerPanel = new JPanel(new BorderLayout());
            centerPanel.add(topCenterRow, BorderLayout.CENTER);
            centerPanel.add(bottomBox, BorderLayout.SOUTH);

            // --- 右侧详情面板 ---
            JPanel rightPanel = new JPanel();
            rightPanel.setPreferredSize(new Dimension(300, 0));
            rightPanel.setBackground(Color.WHITE);

            // --- 总体布局 ---
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.add(sidebar, BorderLayout.WEST);
            mainPanel.add(centerPanel, BorderLayout.CENTER);
            mainPanel.add(rightPanel, BorderLayout.EAST);

            frame.setContentPane(mainPanel);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}

