package app;

import interface_adapter.Alex.CreateEventController;
import interface_adapter.Alex.CreatedEventViewModel;
import use_case.Alex.create_event.CreateEventInputBoundary;
import use_case.Alex.create_event.CreateEventInputData;
import view.Alex.CreateEventView;
import view.CollapsibleSidebarView;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

class CreateEventTestApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Create Event Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 600);

            // 1. 创建 ViewModel 和 Controller（用 Mock 代替真正 UseCase）
            CreatedEventViewModel viewModel = new CreatedEventViewModel();

            CreateEventInputBoundary mockUseCase = new CreateEventInputBoundary() {
                @Override
                public void execute(CreateEventInputData inputData) {
                    System.out.println("Mock Created: " + inputData.getName() + " (" + inputData.getCategory() + ")");
                    JOptionPane.showMessageDialog(null, "Mock Created: " + inputData.getName());
                }
            };

            CreateEventController controller = new CreateEventController(mockUseCase);

            // 2. 创建 View 并注入依赖
            CreateEventView createEventView = new CreateEventView(viewModel);
            createEventView.setCreateEventController(controller);

            // 3. 准备主界面卡片视图
            JPanel mainContent = new JPanel(new CardLayout());
            mainContent.add(createEventView, "Events");

            // 4. 构建带 Sidebar 的主界面
            CollapsibleSidebarView fullView = new CollapsibleSidebarView(mainContent);

            frame.setContentPane(fullView);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}



