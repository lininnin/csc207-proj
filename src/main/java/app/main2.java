package app;

import entity.Alex.MoodLabel.MoodLabel;
import entity.Alex.MoodLabel.MoodLabelFactoryInterf;
import interface_adapter.Alex.WellnessLog_related.moodLabel_related.AvailableMoodLabelState;
import interface_adapter.Alex.WellnessLog_related.moodLabel_related.AvailableMoodLabelState.MoodLabelEntry;
import interface_adapter.Alex.WellnessLog_related.moodLabel_related.AvailableMoodLabelViewModel;
import interface_adapter.Alex.WellnessLog_related.moodLabel_related.add_moodLabel.*;
import interface_adapter.Alex.WellnessLog_related.moodLabel_related.edit_moodLabel.*;
import use_case.Alex.WellnessLog_related.Moodlabel_related.add_moodLabel.*;
import use_case.Alex.WellnessLog_related.Moodlabel_related.edit_moodLabel.*;
import view.Alex.WellnessLog.AvailableMoodLabelView;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class main2 {
    public static void main(String[] args) {
        // ---------------- Step 1: 初始化 ViewModel 和初始状态 ----------------
        AvailableMoodLabelViewModel viewModel = new AvailableMoodLabelViewModel();
        AvailableMoodLabelState initialState = new AvailableMoodLabelState();

        initialState.setMoodLabels(new ArrayList<>(Arrays.asList(
                new MoodLabelEntry("Happy", "Positive"),
                new MoodLabelEntry("Chill", "Positive"),
                new MoodLabelEntry("Sad", "Negative")
        )));
        viewModel.setState(initialState);

        // ---------------- Step 2: 准备 Use Case 层 ----------------

        // 模拟 DAO（使用内存列表）
        List<MoodLabel> store = new ArrayList<>();
        store.add(new MoodLabel.Builder("Happy").type(MoodLabel.Type.Positive).build());
        store.add(new MoodLabel.Builder("Chill").type(MoodLabel.Type.Positive).build());
        store.add(new MoodLabel.Builder("Sad").type(MoodLabel.Type.Negative).build());

        AddMoodLabelDataAccessInterf addDao = new AddMoodLabelDataAccessInterf() {
            @Override
            public void save(MoodLabel moodLabel) {
                store.add(moodLabel);
                viewModel.getState().getMoodLabels().add(
                        new MoodLabelEntry(moodLabel.getName(), moodLabel.getType().name())
                );
                viewModel.fireLabelListChanged();
            }

            @Override
            public List<MoodLabel> getAllLabels() {
                return store;
            }
        };

        EditMoodLabelDataAccessInterface editDao = new EditMoodLabelDataAccessInterface() {
            @Override
            public boolean update(MoodLabel updatedLabel) {
                for (int i = 0; i < store.size(); i++) {
                    if (store.get(i).getName().equals(updatedLabel.getName())) {
                        store.set(i, updatedLabel);
                        return true;
                    }
                }
                return false;
            }

            @Override
            public MoodLabel getByName(String name) {
                for (MoodLabel m : store) {
                    if (m.getName().equals(name)) return m;
                }
                return null;
            }

            @Override
            public boolean existsByName(String name) {
                return store.stream().anyMatch(m -> m.getName().equals(name));
            }
        };

        // Factory
        MoodLabelFactoryInterf factory = (name, type) -> new MoodLabel.Builder(name).type(type).build();

        // ViewModels
        AddMoodLabelViewModel addViewModel = new AddMoodLabelViewModel();
        EditMoodLabelViewModel editViewModel = new EditMoodLabelViewModel();

        // Presenters
        AddMoodLabelPresenter addPresenter = new AddMoodLabelPresenter(addViewModel, viewModel, addDao);
        EditMoodLabelPresenter editPresenter = new EditMoodLabelPresenter(editViewModel, viewModel);

        // Interactors
        AddMoodLabelInputBoundary addInteractor = new AddMoodLabelInteractor(addDao, addPresenter, factory);
        EditMoodLabelInputBoundary editInteractor = new EditMoodLabelInteractor(editDao, editPresenter);

        // Controllers
        AddMoodLabelController addController = new AddMoodLabelController(addInteractor);
        EditMoodLabelController editController = new EditMoodLabelController(editInteractor);

        // ---------------- Step 3: 创建视图并注入两个 controller ----------------
        AvailableMoodLabelView view = new AvailableMoodLabelView(viewModel, addController, editController);

        // ---------------- Step 4: 显示主窗口 ----------------
        JFrame frame = new JFrame("Mood Label Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLayout(new BorderLayout());
        frame.add(view, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}




