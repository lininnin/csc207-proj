package app.WellnessPage;

import data_access.TodaysWellnessLogDataAccessObject;
import data_access.MoodAvailableDataAccessObject;
import entity.Alex.AvalibleMoodLabel.AvaliableMoodLabelFactory;
import entity.Alex.AvalibleMoodLabel.AvaliableMoodlabelFactoryInterf;
import entity.Alex.DailyWellnessLog.DailyWellnessLogFactory;
import entity.Alex.DailyWellnessLog.DailyWellnessLogFactoryInterf;
import entity.Alex.MoodLabel.MoodLabel;
import entity.Alex.MoodLabel.MoodLabelFactory;
import entity.Alex.MoodLabel.MoodLabelFactoryInterf;
import entity.Alex.MoodLabel.MoodLabelInterf;
import entity.Alex.WellnessLogEntry.WellnessLogEntryFactory;
import entity.Alex.WellnessLogEntry.WellnessLogEntryFactoryInterf;

import interface_adapter.alex.WellnessLog_related.moodLabel_related.AvailableMoodLabelState;
import interface_adapter.alex.WellnessLog_related.moodLabel_related.delete_moodLabel.DeleteMoodLabelViewModel;
import interface_adapter.alex.WellnessLog_related.moodLabel_related.edit_moodLabel.EditMoodLabelViewModel;
import interface_adapter.alex.WellnessLog_related.new_wellness_log.AddWellnessLogController;
import interface_adapter.alex.WellnessLog_related.new_wellness_log.AddWellnessLogPresenter;
import interface_adapter.alex.WellnessLog_related.new_wellness_log.AddWellnessLogViewModel;

import interface_adapter.alex.WellnessLog_related.moodLabel_related.AvailableMoodLabelViewModel;
import interface_adapter.alex.WellnessLog_related.moodLabel_related.add_moodLabel.AddMoodLabelController;
import interface_adapter.alex.WellnessLog_related.moodLabel_related.add_moodLabel.AddMoodLabelPresenter;
import interface_adapter.alex.WellnessLog_related.moodLabel_related.add_moodLabel.AddMoodLabelViewModel;
import interface_adapter.alex.WellnessLog_related.moodLabel_related.edit_moodLabel.EditMoodLabelController;
import interface_adapter.alex.WellnessLog_related.moodLabel_related.edit_moodLabel.EditMoodLabelPresenter;
import interface_adapter.alex.WellnessLog_related.moodLabel_related.delete_moodLabel.DeleteMoodLabelController;
import interface_adapter.alex.WellnessLog_related.moodLabel_related.delete_moodLabel.DeleteMoodLabelPresenter;

import interface_adapter.alex.WellnessLog_related.todays_wellnesslog_module.delete_wellnessLog.DeleteWellnessLogController;
import interface_adapter.alex.WellnessLog_related.todays_wellnesslog_module.delete_wellnessLog.DeleteWellnessLogPresenter;
import interface_adapter.alex.WellnessLog_related.todays_wellnesslog_module.delete_wellnessLog.DeleteWellnessLogViewModel;

import interface_adapter.alex.WellnessLog_related.todays_wellnesslog_module.edit_wellnessLog.EditWellnessLogController;
import interface_adapter.alex.WellnessLog_related.todays_wellnesslog_module.edit_wellnessLog.EditWellnessLogPresenter;
import interface_adapter.alex.WellnessLog_related.todays_wellnesslog_module.edit_wellnessLog.EditWellnessLogViewModel;

import interface_adapter.alex.WellnessLog_related.todays_wellnesslog_module.todays_wellness_log.TodaysWellnessLogViewModel;

//import use_case.Alex.WellnessLog_related.Moodlabel_related.add_moodLabel.MoodAvailableDataAccessObject;
import use_case.alex.wellness_log_related.moodlabel_related.add_moodLabel.AddMoodLabelInteractor;
import use_case.alex.wellness_log_related.moodlabel_related.edit_moodLabel.EditMoodLabelInteractor;
import use_case.alex.wellness_log_related.moodlabel_related.delete_moodLabel.DeleteMoodLabelInteractor;

import use_case.alex.wellness_log_related.add_wellnessLog.AddWellnessLogInputBoundary;
import use_case.alex.wellness_log_related.add_wellnessLog.AddWellnessLogInteractor;
import use_case.alex.wellness_log_related.add_wellnessLog.AddWellnessLogOutputBoundary;

import use_case.alex.wellness_log_related.todays_wellness_log_module.delete_wellnesslog.DeleteWellnessLogInteractor;

import use_case.alex.wellness_log_related.todays_wellness_log_module.edit_wellnesslog.EditWellnessLogInteractor;

import view.Alex.WellnessLog.AddWellnessLogView;
import view.Alex.WellnessLog.TodaysWellnessLogView;
import view.Angela.TodaySoFarView;
import interface_adapter.Angela.task.overdue.OverdueTasksViewModel;
import interface_adapter.Angela.task.overdue.OverdueTasksController;
import interface_adapter.Angela.task.overdue.OverdueTasksPresenter;
import interface_adapter.Angela.today_so_far.TodaySoFarViewModel;
import interface_adapter.Angela.today_so_far.TodaySoFarController;
import interface_adapter.Angela.today_so_far.TodaySoFarPresenter;
import use_case.Angela.task.overdue.OverdueTasksInputBoundary;
import use_case.Angela.task.overdue.OverdueTasksInteractor;
import use_case.Angela.task.overdue.OverdueTasksOutputBoundary;
import use_case.Angela.today_so_far.TodaySoFarInputBoundary;
import use_case.Angela.today_so_far.TodaySoFarInteractor;
import data_access.InMemoryTaskGateway;
import data_access.InMemoryCategoryDataAccessObject;
import data_access.InMemoryTodaySoFarDataAccess;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class WellnessLogPageBuilder {
    
    // Today So Far panel fields
    private TodaySoFarController todaySoFarController;
    private OverdueTasksController overdueTasksController;

    public JPanel build() {

        // --- ViewModel ---
        AddWellnessLogViewModel addLogViewModel = new AddWellnessLogViewModel();
        TodaysWellnessLogViewModel todaysLogViewModel = new TodaysWellnessLogViewModel();

        // --- Use shared DAO ---
        TodaysWellnessLogDataAccessObject wellnessLogDAO = 
            app.SharedDataAccess.getInstance().getWellnessDataAccess();
        WellnessLogEntryFactoryInterf wellnessLogEntryFactory = new WellnessLogEntryFactory();
        MoodLabelFactoryInterf moodLabelFactory = new MoodLabelFactory();

        // --- Add Log Controller ---
        AddWellnessLogPresenter presenter = new AddWellnessLogPresenter(addLogViewModel, todaysLogViewModel);
        AddWellnessLogInputBoundary interactor = new AddWellnessLogInteractor(wellnessLogDAO, wellnessLogEntryFactory, presenter);
        AddWellnessLogController addLogController = new AddWellnessLogController(interactor);

        // --- MoodLabel Controllers ---
        AvailableMoodLabelViewModel availableMoodLabelViewModel = new AvailableMoodLabelViewModel();
        AddMoodLabelViewModel addMoodLabelViewModel = new AddMoodLabelViewModel();
        AvaliableMoodlabelFactoryInterf availableMoodFactory = new AvaliableMoodLabelFactory();
        MoodAvailableDataAccessObject moodDAO = new MoodAvailableDataAccessObject(availableMoodFactory, moodLabelFactory);

        AddMoodLabelPresenter addLabelPresenter = new AddMoodLabelPresenter(addMoodLabelViewModel, availableMoodLabelViewModel, moodDAO);
        AddMoodLabelInteractor addLabelInteractor = new AddMoodLabelInteractor(moodDAO, addLabelPresenter, new MoodLabelFactory());
        AddMoodLabelController addLabelController = new AddMoodLabelController(addLabelInteractor);

        EditMoodLabelViewModel editMoodLabelViewModel = new EditMoodLabelViewModel(); // ✅ 新增

        EditMoodLabelPresenter editLabelPresenter = new EditMoodLabelPresenter(
                editMoodLabelViewModel, availableMoodLabelViewModel); // ✅ 正确参数类型
        EditMoodLabelInteractor editLabelInteractor = new EditMoodLabelInteractor(moodDAO, editLabelPresenter, moodLabelFactory);
        EditMoodLabelController editLabelController = new EditMoodLabelController(editLabelInteractor);


        DeleteMoodLabelViewModel deleteMoodLabelViewModel = new DeleteMoodLabelViewModel(); // ✅ 新建

        DeleteMoodLabelPresenter deleteLabelPresenter = new DeleteMoodLabelPresenter(
                deleteMoodLabelViewModel, availableMoodLabelViewModel); // ✅ 修复类型
        DeleteMoodLabelInteractor deleteLabelInteractor = new DeleteMoodLabelInteractor(moodDAO, deleteLabelPresenter);
        DeleteMoodLabelController deleteLabelController = new DeleteMoodLabelController(deleteLabelInteractor);

        // --- 初始化标签列表 ---
        List<MoodLabelInterf> moodOptions = moodDAO.getAllLabels();
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

        for (MoodLabelInterf label : moodOptions) {
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
                deleteLabelController,
                moodLabelFactory
        );
        addLogView.setPreferredSize(new Dimension(Short.MAX_VALUE, 300));
        addLogView.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

        JPanel fixedHeightWrapper = new JPanel(new BorderLayout());
        fixedHeightWrapper.add(addLogView, BorderLayout.CENTER);
        fixedHeightWrapper.setPreferredSize(new Dimension(Short.MAX_VALUE, 300));
        fixedHeightWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

        // --- Today's Log View ---
        DeleteWellnessLogPresenter deletePresenter = new DeleteWellnessLogPresenter(
                new DeleteWellnessLogViewModel(),
                todaysLogViewModel,
                wellnessLogDAO
        );
        
        EditWellnessLogPresenter editPresenter = new EditWellnessLogPresenter(
                new EditWellnessLogViewModel(),
                todaysLogViewModel,
                wellnessLogDAO
        );
        
        TodaysWellnessLogView todaysLogView = new TodaysWellnessLogView(
                todaysLogViewModel,
                new DeleteWellnessLogController(
                        new DeleteWellnessLogInteractor(wellnessLogDAO, deletePresenter)
                ),
                new EditWellnessLogController(
                        new EditWellnessLogInteractor(
                                wellnessLogDAO,
                                editPresenter,
                                new WellnessLogEntryFactory(), // ✅ 新增工厂
                                new MoodLabelFactory()         // ✅ 新增工厂
                        )
                ),

                availableMoodLabelViewModel,
                addLabelController,
                editLabelController,
                deleteLabelController,
                moodLabelFactory
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

        // --- Set up Today So Far Panel ---
        // Get shared Today So Far components
        app.SharedTodaySoFarComponents sharedTodaySoFar = app.SharedTodaySoFarComponents.getInstance();
        overdueTasksController = sharedTodaySoFar.getOverdueTasksController();
        todaySoFarController = sharedTodaySoFar.getTodaySoFarController();
        
        // Create Today So Far view using shared components
        TodaySoFarView todaySoFarView = sharedTodaySoFar.createTodaySoFarView();
        
        // Set Today So Far controller on wellness presenters that need to refresh
        presenter.setTodaySoFarController(todaySoFarController);
        deletePresenter.setTodaySoFarController(todaySoFarController);
        editPresenter.setTodaySoFarController(todaySoFarController);
        
        // Trigger initial data load
        sharedTodaySoFar.refresh();

        // Wrap Today So Far in a panel with flexible sizing
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(todaySoFarView, BorderLayout.CENTER);
        rightPanel.setMinimumSize(new Dimension(250, 0));
        // Remove preferred size to allow flexible sizing

        // Create horizontal split pane for resizable layout
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, centerPanel, rightPanel);
        mainSplitPane.setDividerLocation(800);
        mainSplitPane.setContinuousLayout(true);
        mainSplitPane.setOneTouchExpandable(true);
        mainSplitPane.setDividerSize(8);
        mainSplitPane.setResizeWeight(0.75);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(mainSplitPane, BorderLayout.CENTER);

        return mainPanel;
    }
}



