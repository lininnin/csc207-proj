package data_access;

import entity.Alex.MoodLabel.MoodLabelInterf;
import entity.Alex.MoodLabel.MoodLabel;
import entity.Alex.AvalibleMoodLabel.AvaliableMoodLabelInterf;
import entity.Alex.MoodLabel.Type;
import use_case.alex.WellnessLog_related.Moodlabel_related.add_moodLabel.AddMoodLabelDataAccessInterf;
import use_case.alex.WellnessLog_related.Moodlabel_related.delete_moodLabel.DeleteMoodLabelDataAccessInterf;
import use_case.alex.WellnessLog_related.Moodlabel_related.edit_moodLabel.EditMoodLabelDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for managing available mood labels.
 * Fully decoupled from concrete implementation using DIP.
 */
public class MoodAvailableDataAccessObject implements
        AddMoodLabelDataAccessInterf,
        DeleteMoodLabelDataAccessInterf,
        EditMoodLabelDataAccessInterface {

    private final AvaliableMoodLabelInterf availableLabels;

    /**
     * Constructs the DAO using a factory to obtain the label storage implementation.
     */
    public MoodAvailableDataAccessObject(entity.Alex.AvalibleMoodLabel.AvaliableMoodlabelFactoryInterf factory) {
        this.availableLabels = factory.create();
        // 默认标签初始化
        availableLabels.addLabel(new MoodLabel.Builder("Happy").type(Type.Positive).build());
        availableLabels.addLabel(new MoodLabel.Builder("Calm").type(Type.Positive).build());
        availableLabels.addLabel(new MoodLabel.Builder("Anxious").type(Type.Negative).build());
        availableLabels.addLabel(new MoodLabel.Builder("Stressed").type(Type.Negative).build());
    }

    // ----------- AddMoodLabelDataAccessInterf 实现 -----------

    @Override
    public void save(MoodLabelInterf moodLabel) {
        if (contains(moodLabel.getName())) {
            throw new IllegalArgumentException("Label with the same name already exists");
        }
        availableLabels.addLabel(moodLabel);
    }

    @Override
    public List<MoodLabelInterf> getAllLabels() {
        List<MoodLabelInterf> all = new ArrayList<>();
        all.addAll(availableLabels.getPositiveLabelObjects());
        all.addAll(availableLabels.getNegativeLabelObjects());
        return all;
    }

    // ----------- DeleteMoodLabelDataAccessInterf 实现 -----------

    @Override
    public boolean remove(MoodLabelInterf moodLabel) {
        return removeByName(moodLabel.getName());
    }

    @Override
    public boolean contains(MoodLabelInterf moodLabel) {
        return contains(moodLabel.getName());
    }

    @Override
    public MoodLabelInterf getByName(String name) {
        for (MoodLabelInterf label : getAllLabels()) {
            if (label.getName().equals(name)) {
                return label;
            }
        }
        return null;
    }

    // ----------- EditMoodLabelDataAccessInterface 实现 -----------

    @Override
    public boolean update(MoodLabelInterf updatedLabel) {
        String name = updatedLabel.getName();
        if (!contains(name)) return false;
        availableLabels.removeLabelByName(name);
        availableLabels.addLabel(updatedLabel);
        return true;
    }

    @Override
    public boolean existsByName(String name) {
        return contains(name);
    }

    // ----------- 工具方法 -----------

    public boolean removeByName(String name) {
        boolean existed = contains(name);
        availableLabels.removeLabelByName(name);
        return existed;
    }

    public boolean contains(String name) {
        return availableLabels.getPositiveLabelObjects().stream().anyMatch(l -> l.getName().equals(name)) ||
                availableLabels.getNegativeLabelObjects().stream().anyMatch(l -> l.getName().equals(name));
    }

    public AvaliableMoodLabelInterf getCategorized() {
        return availableLabels;
    }

    public void clearAll() {
        availableLabels.clear();
    }
}
