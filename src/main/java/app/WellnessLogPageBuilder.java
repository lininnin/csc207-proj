package app;

import data_access.TodaysWellnessLogDataAccessObject;
import entity.Alex.MoodLabel.MoodLabel;
import entity.Alex.WellnessLogEntry.WellnessLogEntryFactory;
import entity.Alex.WellnessLogEntry.WellnessLogEntryFactoryInterf;
import interface_adapter.Alex.WellnessLog_related.new_wellness_log.AddWellnessLogController;
import interface_adapter.Alex.WellnessLog_related.new_wellness_log.AddWellnessLogPresenter;
import interface_adapter.Alex.WellnessLog_related.new_wellness_log.AddWellnessLogViewModel;
import interface_adapter.Alex.WellnessLog_related.todays_wellness_log.todays_wellnesslog_module.TodaysWellnessLogViewModel;
// import use_case.Alex.WellnessLog_related.delete_log.DeleteWellnessLogController;
// import use_case.Alex.WellnessLog_related.edit_log.EditWellnessLogController;
import use_case.Alex.WellnessLog_related.add_wellnessLog.AddWellnessLogInputBoundary;
import use_case.Alex.WellnessLog_related.add_wellnessLog.AddWellnessLogInteractor;
import use_case.Alex.WellnessLog_related.add_wellnessLog.AddWellnessLogOutputBoundary;
import view.Alex.WellnessLog.AddWellnessLogView;
import view.Alex.WellnessLog.TodaysWellnessLogView;
import view.CollapsibleSidebarView;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class WellnessLogPageBuilder {

    public JPanel build() {

        // --- ViewModel ---
        AddWellnessLogViewModel addLogViewModel = new AddWellnessLogViewModel();
        TodaysWellnessLogViewModel todaysLogViewModel = new TodaysWellnessLogViewModel();

        // --- DAO + Factory ---
        TodaysWellnessLogDataAccessObject wellnessLogDAO = new TodaysWellnessLogDataAccessObject();
        WellnessLogEntryFactoryInterf factory = new WellnessLogEntryFactory();

        // --- Add Log Controller ---
        AddWellnessLogOutputBoundary presenter = new AddWellnessLogPresenter(addLogViewModel, todaysLogViewModel);
        AddWellnessLogInputBoundary interactor = new AddWellnessLogInteractor(wellnessLogDAO, factory, presenter);
        AddWellnessLogController addLogController = new AddWellnessLogController(interactor);

        // --- 初始化 MoodLabel 下拉选项 ---
        List<entity.Alex.MoodLabel.MoodLabel> moodOptions = List.of(
                new entity.Alex.MoodLabel.MoodLabel.Builder("Calm")
                        .type(entity.Alex.MoodLabel.MoodLabel.Type.Positive)
                        .build(),
                new entity.Alex.MoodLabel.MoodLabel.Builder("Happy")
                        .type(entity.Alex.MoodLabel.MoodLabel.Type.Positive)
                        .build(),
                new entity.Alex.MoodLabel.MoodLabel.Builder("Tired")
                        .type(MoodLabel.Type.Negative)
                        .build(),
                new entity.Alex.MoodLabel.MoodLabel.Builder("Anxious")
                        .type(entity.Alex.MoodLabel.MoodLabel.Type.Negative)
                        .build(),
                new entity.Alex.MoodLabel.MoodLabel.Builder("Sad")
                        .type(entity.Alex.MoodLabel.MoodLabel.Type.Negative)
                        .build()
        );
        addLogViewModel.getState().setAvailableMoodLabels(moodOptions);
        addLogViewModel.setState(addLogViewModel.getState());


        // --- View：AddWellnessLogView ---
        AddWellnessLogView addLogView = new AddWellnessLogView(addLogViewModel, addLogController);
        addLogView.setPreferredSize(new Dimension(Short.MAX_VALUE, 300));
        addLogView.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

        JPanel fixedHeightWrapper = new JPanel(new BorderLayout());
        fixedHeightWrapper.add(addLogView, BorderLayout.CENTER);
        fixedHeightWrapper.setPreferredSize(new Dimension(Short.MAX_VALUE, 300));
        fixedHeightWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

        // --- View：TodaysWellnessLogView（暂不提供 edit/delete 控制器） ---
        TodaysWellnessLogView todaysLogView = new TodaysWellnessLogView(
                todaysLogViewModel
        );
        todaysLogView.setBackground(Color.WHITE);

        JPanel lowerPart = new JPanel(new BorderLayout());
        lowerPart.setBorder(BorderFactory.createTitledBorder("Today's Wellness Log"));
        lowerPart.add(todaysLogView, BorderLayout.CENTER);

        // --- Split 上下部分 ---
        JSplitPane verticalSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, fixedHeightWrapper, lowerPart);
        verticalSplit.setDividerLocation(300);
        verticalSplit.setResizeWeight(0); // 上部分固定高度
        verticalSplit.setDividerSize(2);
        verticalSplit.setEnabled(false);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(verticalSplit, BorderLayout.CENTER);

        // --- 左侧导航栏 ---
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(new Color(60, 63, 65));
        sidebarPanel.setPreferredSize(new Dimension(200, 700));
        sidebarPanel.add(new JButton("📋 Tasks"));
        sidebarPanel.add(new JButton("📆 Events"));
        sidebarPanel.add(new JButton("🎯 Goals"));
        sidebarPanel.add(new JButton("🧠 Wellness Log"));
        sidebarPanel.add(new JButton("📊 Charts"));
        sidebarPanel.add(new JButton("🤖 AI-Feedback & Analysis"));
        sidebarPanel.add(new JButton("⚙️ Settings"));

        CollapsibleSidebarView collapsibleCenter = new CollapsibleSidebarView(sidebarPanel, centerPanel);

        // --- 右侧空白预留 ---
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(500, 0));
        rightPanel.setBackground(Color.WHITE);

        // --- 主面板整合 ---
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(collapsibleCenter, BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.EAST);

        return mainPanel;
    }
}


