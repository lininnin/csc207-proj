package data_access;

import entity.Alex.MoodLabel.MoodLabel;
import entity.Alex.MoodLabel.MoodLabelInterf;
import entity.Alex.AvalibleMoodLabel.AvaliableMoodLabel;
import use_case.Alex.WellnessLog_related.Moodlabel_related.add_moodLabel.AddMoodLabelDataAccessInterf;
import use_case.Alex.WellnessLog_related.Moodlabel_related.delete_moodLabel.DeleteMoodLabelDataAccessInterf;
import use_case.Alex.WellnessLog_related.Moodlabel_related.edit_moodLabel.EditMoodLabelDataAccessInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MoodAvailableDataAccessObject implements
        AddMoodLabelDataAccessInterf,
        DeleteMoodLabelDataAccessInterf,
        EditMoodLabelDataAccessInterface {

    private final AvaliableMoodLabel availableLabels;

    public MoodAvailableDataAccessObject() {
        this.availableLabels = new AvaliableMoodLabel();
        availableLabels.addLabel(new MoodLabel.Builder("Happy").type(MoodLabel.Type.Positive).build());
        availableLabels.addLabel(new MoodLabel.Builder("Calm").type(MoodLabel.Type.Positive).build());
        availableLabels.addLabel(new MoodLabel.Builder("Anxious").type(MoodLabel.Type.Negative).build());
        availableLabels.addLabel(new MoodLabel.Builder("Stressed").type(MoodLabel.Type.Negative).build());
    }

    // ----------- AddMoodLabelDataAccessInterf 实现 -----------

    @Override
    public void save(MoodLabel moodLabel) {
        if (contains(moodLabel.getName())) {
            throw new IllegalArgumentException("Label with the same name already exists");
        }
        availableLabels.addLabel(moodLabel);
    }

    @Override
    public List<MoodLabel> getAllLabels() {
        List<MoodLabel> all = new ArrayList<>();
        all.addAll(availableLabels.getPositiveLabelObjects());
        all.addAll(availableLabels.getNegativeLabelObjects());
        return all;
    }

    // ----------- DeleteMoodLabelDataAccessInterf 实现 -----------

    @Override
    public boolean remove(MoodLabel moodLabel) {
        return removeByName(moodLabel.getName());
    }

    @Override
    public boolean contains(MoodLabel moodLabel) {
        return contains(moodLabel.getName());
    }

    @Override
    public MoodLabel getByName(String name) {
        for (MoodLabel label : getAllLabels()) {
            if (label.getName().equals(name)) {
                return label;
            }
        }
        return null;
    }

    // ----------- EditMoodLabelDataAccessInterface 实现 -----------

    @Override
    public boolean update(MoodLabel updatedLabel) {
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

    public AvaliableMoodLabel getCategorized() {
        return availableLabels;
    }

    public void clearAll() {
        availableLabels.clear();
    }
}



