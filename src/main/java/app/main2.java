package app;

import interface_adapter.Alex.WellnessLog_related.moodLabel_related.AvailableMoodLabelState;
import interface_adapter.Alex.WellnessLog_related.moodLabel_related.AvailableMoodLabelState.MoodLabelEntry;
import interface_adapter.Alex.WellnessLog_related.moodLabel_related.AvailableMoodLabelViewModel;
import view.Alex.WellnessLog.AvailableMoodLabelView;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

class main2 {
    public static void main(String[] args) {
        // 初始化 ViewModel 与 State
        AvailableMoodLabelViewModel viewModel = new AvailableMoodLabelViewModel();
        AvailableMoodLabelState initialState = new AvailableMoodLabelState();

        // 模拟初始数据（供 UI 展示）
        initialState.setMoodLabels(Arrays.asList(
                new MoodLabelEntry("Happy", "Positive"),
                new MoodLabelEntry("chill", "Positive"),
                new MoodLabelEntry("sad", "Negative")
        ));

        viewModel.setState(initialState);

        // 创建视图
        AvailableMoodLabelView view = new AvailableMoodLabelView(viewModel);

        // 创建窗口展示
        JFrame frame = new JFrame("Mood Label Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLayout(new BorderLayout());
        frame.add(view, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}

