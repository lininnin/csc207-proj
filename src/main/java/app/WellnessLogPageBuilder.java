package app;

import data_access.TodaysWellnessLogDataAccessObject;
import data_access.MoodAvailableDataAccessObject;
import entity.Alex.MoodLabel.MoodLabel;
import entity.Alex.MoodLabel.MoodLabelFactory;
import entity.Alex.WellnessLogEntry.WellnessLogEntryFactory;
import entity.Alex.WellnessLogEntry.WellnessLogEntryFactoryInterf;

import interface_adapter.Alex.WellnessLog_related.moodLabel_related.AvailableMoodLabelState;
import interface_adapter.Alex.WellnessLog_related.moodLabel_related.delete_moodLabel.DeleteMoodLabelViewModel;
import interface_adapter.Alex.WellnessLog_related.moodLabel_related.edit_moodLabel.EditMoodLabelViewModel;
import interface_adapter.Alex.WellnessLog_related.new_wellness_log.AddWellnessLogController;
import interface_adapter.Alex.WellnessLog_related.new_wellness_log.AddWellnessLogPresenter;
import interface_adapter.Alex.WellnessLog_related.new_wellness_log.AddWellnessLogViewModel;

import interface_adapter.Alex.WellnessLog_related.moodLabel_related.AvailableMoodLabelViewModel;
import interface_adapter.Alex.WellnessLog_related.moodLabel_related.add_moodLabel.AddMoodLabelController;
import interface_adapter.Alex.WellnessLog_related.moodLabel_related.add_moodLabel.AddMoodLabelPresenter;
import interface_adapter.Alex.WellnessLog_related.moodLabel_related.add_moodLabel.AddMoodLabelViewModel;
import interface_adapter.Alex.WellnessLog_related.moodLabel_related.edit_moodLabel.EditMoodLabelController;
import interface_adapter.Alex.WellnessLog_related.moodLabel_related.edit_moodLabel.EditMoodLabelPresenter;
import interface_adapter.Alex.WellnessLog_related.moodLabel_related.delete_moodLabel.DeleteMoodLabelController;
import interface_adapter.Alex.WellnessLog_related.moodLabel_related.delete_moodLabel.DeleteMoodLabelPresenter;

import interface_adapter.Alex.WellnessLog_related.todays_wellnesslog_module.delete_wellnessLog.DeleteWellnessLogController;
import interface_adapter.Alex.WellnessLog_related.todays_wellnesslog_module.delete_wellnessLog.DeleteWellnessLogPresenter;
import interface_adapter.Alex.WellnessLog_related.todays_wellnesslog_module.delete_wellnessLog.DeleteWellnessLogViewModel;

import interface_adapter.Alex.WellnessLog_related.todays_wellnesslog_module.edit_wellnessLog.EditWellnessLogController;
import interface_adapter.Alex.WellnessLog_related.todays_wellnesslog_module.edit_wellnessLog.EditWellnessLogPresenter;
import interface_adapter.Alex.WellnessLog_related.todays_wellnesslog_module.edit_wellnessLog.EditWellnessLogViewModel;

import interface_adapter.Alex.WellnessLog_related.todays_wellnesslog_module.todays_wellness_log.TodaysWellnessLogViewModel;

//import use_case.Alex.WellnessLog_related.Moodlabel_related.add_moodLabel.MoodAvailableDataAccessObject;
import use_case.Alex.WellnessLog_related.Moodlabel_related.add_moodLabel.AddMoodLabelInteractor;
import use_case.Alex.WellnessLog_related.Moodlabel_related.edit_moodLabel.EditMoodLabelInteractor;
import use_case.Alex.WellnessLog_related.Moodlabel_related.delete_moodLabel.DeleteMoodLabelInteractor;

import use_case.Alex.WellnessLog_related.add_wellnessLog.AddWellnessLogInputBoundary;
import use_case.Alex.WellnessLog_related.add_wellnessLog.AddWellnessLogInteractor;
import use_case.Alex.WellnessLog_related.add_wellnessLog.AddWellnessLogOutputBoundary;

import use_case.Alex.WellnessLog_related.todays_wellnessLog_module.delete_wellnesslog.DeleteWellnessLogInputBoundary;
import use_case.Alex.WellnessLog_related.todays_wellnessLog_module.delete_wellnesslog.DeleteWellnessLogInteractor;
import use_case.Alex.WellnessLog_related.todays_wellnessLog_module.delete_wellnesslog.DeleteWellnessLogOutputBoundary;

import use_case.Alex.WellnessLog_related.todays_wellnessLog_module.edit_wellnesslog.EditWellnessLogInteractor;

import view.Alex.WellnessLog.AddWellnessLogView;
import view.Alex.WellnessLog.TodaysWellnessLogView;
import view.CollapsibleSidebarView;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
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

        // --- MoodLabel Controllers ---
        AvailableMoodLabelViewModel availableMoodLabelViewModel = new AvailableMoodLabelViewModel();
        AddMoodLabelViewModel addMoodLabelViewModel = new AddMoodLabelViewModel();
        MoodAvailableDataAccessObject moodDAO = new MoodAvailableDataAccessObject();

        AddMoodLabelPresenter addLabelPresenter = new AddMoodLabelPresenter(addMoodLabelViewModel, availableMoodLabelViewModel, moodDAO);
        AddMoodLabelInteractor addLabelInteractor = new AddMoodLabelInteractor(moodDAO, addLabelPresenter, new MoodLabelFactory());
        AddMoodLabelController addLabelController = new AddMoodLabelController(addLabelInteractor);

        EditMoodLabelViewModel editMoodLabelViewModel = new EditMoodLabelViewModel(); // ✅ 新增

        EditMoodLabelPresenter editLabelPresenter = new EditMoodLabelPresenter(
                editMoodLabelViewModel, availableMoodLabelViewModel); // ✅ 正确参数类型
        EditMoodLabelInteractor editLabelInteractor = new EditMoodLabelInteractor(moodDAO, editLabelPresenter);
        EditMoodLabelController editLabelController = new EditMoodLabelController(editLabelInteractor);


        DeleteMoodLabelViewModel deleteMoodLabelViewModel = new DeleteMoodLabelViewModel(); // ✅ 新建

        DeleteMoodLabelPresenter deleteLabelPresenter = new DeleteMoodLabelPresenter(
                deleteMoodLabelViewModel, availableMoodLabelViewModel); // ✅ 修复类型
        DeleteMoodLabelInteractor deleteLabelInteractor = new DeleteMoodLabelInteractor(moodDAO, deleteLabelPresenter);
        DeleteMoodLabelController deleteLabelController = new DeleteMoodLabelController(deleteLabelInteractor);

        // --- 初始化标签列表 ---
        List<MoodLabel> moodOptions = moodDAO.getAllLabels();
        addLogViewModel.getState().setAvailableMoodLabels(moodOptions);
        // ✅ 添加这两行用于同步初始标签给 AvailableMoodLabelViewModel
//        availableMoodLabelViewModel.getState().setMoodLabels(
//                moodOptions.stream()
//                        .map(label -> new AvailableMoodLabelState.MoodLabelEntry(label.getName(), label.getType().name()))
//                        .toList()
//        );
//        addLogViewModel.setState(addLogViewModel.getState());
//        AvailableMoodLabelState initialState = new AvailableMoodLabelState();
//        List<AvailableMoodLabelState.MoodLabelEntry> entries = moodOptions.stream()
//                .map(label -> new AvailableMoodLabelState.MoodLabelEntry(label.getName(), label.getType().name()))
//                .toList();
//        initialState.setMoodLabels(entries);
//        availableMoodLabelViewModel.setState(initialState); // ✅ 触发 property change
        AvailableMoodLabelState initialState = new AvailableMoodLabelState();
        List<AvailableMoodLabelState.MoodLabelEntry> labelList = new ArrayList<>();

        for (MoodLabel label : moodOptions) {
            labelList.add(new AvailableMoodLabelState.MoodLabelEntry(label.getName(), label.getType().name()));
        }

        initialState.setMoodLabels(labelList);
        availableMoodLabelViewModel.setState(initialState);

// ✅ 确保后续 add 操作是对同一个 list


        // --- Add WellnessLog View（整合弹窗标签选择）---
        AddWellnessLogView addLogView = new AddWellnessLogView(
                addLogViewModel,
                addLogController,
                availableMoodLabelViewModel,
                addLabelController,
                editLabelController,
                deleteLabelController
        );
        addLogView.setPreferredSize(new Dimension(Short.MAX_VALUE, 300));
        addLogView.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

        JPanel fixedHeightWrapper = new JPanel(new BorderLayout());
        fixedHeightWrapper.add(addLogView, BorderLayout.CENTER);
        fixedHeightWrapper.setPreferredSize(new Dimension(Short.MAX_VALUE, 300));
        fixedHeightWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

        // --- Today's Log View ---
        TodaysWellnessLogView todaysLogView = new TodaysWellnessLogView(
                todaysLogViewModel,
                new DeleteWellnessLogController(
                        new DeleteWellnessLogInteractor(wellnessLogDAO,
                                new DeleteWellnessLogPresenter(
                                        new DeleteWellnessLogViewModel(),
                                        todaysLogViewModel,
                                        wellnessLogDAO
                                )
                        )
                ),
                new EditWellnessLogController(
                        new EditWellnessLogInteractor(wellnessLogDAO,
                                new EditWellnessLogPresenter(
                                        new EditWellnessLogViewModel(),
                                        todaysLogViewModel,
                                        wellnessLogDAO
                                )
                        )
                ),
                availableMoodLabelViewModel,
                addLabelController,
                editLabelController,
                deleteLabelController
        );


        JPanel lowerPart = new JPanel(new BorderLayout());
        lowerPart.setBorder(BorderFactory.createTitledBorder("Today's Wellness Log"));
        lowerPart.add(todaysLogView, BorderLayout.CENTER);

        JSplitPane verticalSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, fixedHeightWrapper, lowerPart);
        verticalSplit.setDividerLocation(300);
        verticalSplit.setResizeWeight(0);
        verticalSplit.setDividerSize(2);
        verticalSplit.setEnabled(false);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(verticalSplit, BorderLayout.CENTER);

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

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(500, 0));
        rightPanel.setBackground(Color.WHITE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(collapsibleCenter, BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.EAST);

        return mainPanel;
    }
}



