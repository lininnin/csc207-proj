package data_access.alex;

import entity.alex.AvalibleMoodLabel.AvaliableMoodlabelFactoryInterf;
import entity.alex.AvalibleMoodLabel.AvaliableMoodLabelInterf;
import entity.alex.MoodLabel.MoodLabelFactoryInterf;
import entity.alex.MoodLabel.MoodLabelInterf;
import entity.alex.MoodLabel.Type;
import use_case.alex.wellness_log_related.moodlabel_related.add_moodLabel.AddMoodLabelDataAccessInterf;
import use_case.alex.wellness_log_related.moodlabel_related.delete_moodLabel.DeleteMoodLabelDataAccessInterf;
import use_case.alex.wellness_log_related.moodlabel_related.edit_moodLabel.EditMoodLabelDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for managing available mood labels.
 * Now decoupled from concrete implementation using DIP.
 */
public class MoodAvailableDataAccessObject implements
        AddMoodLabelDataAccessInterf,
        DeleteMoodLabelDataAccessInterf,
        EditMoodLabelDataAccessInterface {

    /**
     * Reference to the mood label storage (positive/negative).
     */
    private final AvaliableMoodLabelInterf availableLabels;
    private final MoodLabelFactoryInterf moodLabelFactory;

    /**
     * Constructs the DAO using a factory to obtain the label storage implementation.
     *
     * @param factory A factory that provides the implementation for available mood labels.
     */
    public MoodAvailableDataAccessObject(final AvaliableMoodlabelFactoryInterf factory,
                                         final MoodLabelFactoryInterf moodLabelFactory) {
        this.availableLabels = factory.create();
        this.moodLabelFactory = moodLabelFactory;
        // 默认标签初始化
        availableLabels.addLabel(moodLabelFactory.create("Happy", Type.Positive));
        availableLabels.addLabel(moodLabelFactory.create("Calm", Type.Positive));
        availableLabels.addLabel(moodLabelFactory.create("Anxious", Type.Negative));
        availableLabels.addLabel(moodLabelFactory.create("Stressed", Type.Negative));
    }

    /**
     * Stores a new mood label if it does not already exist.
     *
     * @param moodLabel The mood label to store.
     * @throws IllegalArgumentException if a label with the same name already exists.
     */
    @Override
    public void save(final MoodLabelInterf moodLabel) {
        if (contains(moodLabel.getName())) {
            throw new IllegalArgumentException("Label with the same name already exists");
        }
        availableLabels.addLabel(moodLabel);
    }

    /**
     * Returns all positive and negative mood labels.
     *
     * @return A list of all mood labels.
     */
    @Override
    public List<MoodLabelInterf> getAllLabels() {
        List<MoodLabelInterf> all = new ArrayList<>();
        all.addAll(availableLabels.getPositiveLabelObjects());
        all.addAll(availableLabels.getNegativeLabelObjects());
        return all;
    }

    /**
     * Removes the given mood label from storage.
     *
     * @param moodLabel The label to remove.
     * @return true if the label existed and was removed, false otherwise.
     */
    @Override
    public boolean remove(final MoodLabelInterf moodLabel) {
        return removeByName(moodLabel.getName());
    }

    /**
     * Checks if the mood label exists in storage.
     *
     * @param moodLabel The label to check.
     * @return true if exists, false otherwise.
     */
    @Override
    public boolean contains(final MoodLabelInterf moodLabel) {
        return contains(moodLabel.getName());
    }

    /**
     * Retrieves a label by name.
     *
     * @param name The name of the label.
     * @return The MoodLabel if found, null otherwise.
     */
    @Override
    public MoodLabelInterf getByName(final String name) {
        for (MoodLabelInterf label : getAllLabels()) {
            if (label.getName().equals(name)) {
                return label;
            }
        }
        return null;
    }

    /**
     * Updates the label with the same name.
     *
     * @param updatedLabel The updated label object.
     * @return true if update successful, false if label does not exist.
     */
    @Override
    public boolean update(final MoodLabelInterf updatedLabel) {
        final String name = updatedLabel.getName();
        if (!contains(name)) {
            return false;
        }
        availableLabels.removeLabelByName(name);
        availableLabels.addLabel(updatedLabel);
        return true;
    }

    /**
     * Checks if a label with the given name exists.
     *
     * @param name The name to check.
     * @return true if exists, false otherwise.
     */
    @Override
    public boolean existsByName(final String name) {
        return contains(name);
    }

    /**
     * Removes label by name.
     *
     * @param name The name of the label.
     * @return true if existed and removed, false otherwise.
     */
    public boolean removeByName(final String name) {
        final boolean existed = contains(name);
        availableLabels.removeLabelByName(name);
        return existed;
    }

    /**
     * Checks whether a label with the given name exists.
     *
     * @param name The label name.
     * @return true if exists, false otherwise.
     */
    public boolean contains(final String name) {
        return availableLabels.getPositiveLabelObjects().stream()
                .anyMatch(l -> l.getName().equals(name))
                || availableLabels.getNegativeLabelObjects().stream()
                .anyMatch(l -> l.getName().equals(name));
    }

    /**
     * Returns the categorized interface (positive/negative).
     *
     * @return categorized label interface.
     */
    public AvaliableMoodLabelInterf getCategorized() {
        return availableLabels;
    }

    /**
     * Removes all mood labels.
     */
    public void clearAll() {
        availableLabels.clear();
    }
}
