package data_access;

import entity.Alex.MoodLabel.MoodLabel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * In-memory DAO for managing available mood labels.
 * Supports basic operations like add, remove, update, and lookup.
 */
public class MoodAvailableDataAccessObject {

    private final List<MoodLabel> availableMoods = new ArrayList<>();

    public MoodAvailableDataAccessObject() {
        // 初始化一些默认的 MoodLabel
        availableMoods.add(
                new MoodLabel.Builder("Happy").type(MoodLabel.Type.Positive).build()
        );
        availableMoods.add(
                new MoodLabel.Builder("Calm").type(MoodLabel.Type.Positive).build()
        );
        availableMoods.add(
                new MoodLabel.Builder("Anxious").type(MoodLabel.Type.Negative).build()
        );
        availableMoods.add(
                new MoodLabel.Builder("Stressed").type(MoodLabel.Type.Negative).build()
        );
    }

    public void add(MoodLabel mood) {
        if (mood == null) {
            throw new IllegalArgumentException("MoodLabel cannot be null");
        }
        if (getByName(mood.getName()).isPresent()) {
            throw new IllegalArgumentException("MoodLabel with the same name already exists");
        }
        availableMoods.add(mood);
    }

    public boolean removeByName(String name) {
        return availableMoods.removeIf(m -> m.getName().equalsIgnoreCase(name));
    }

    public Optional<MoodLabel> getByName(String name) {
        return availableMoods.stream()
                .filter(m -> m.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    public List<MoodLabel> getAll() {
        return new ArrayList<>(availableMoods);
    }

    public boolean update(String oldName, MoodLabel newMood) {
        Optional<MoodLabel> existing = getByName(oldName);
        if (existing.isPresent()) {
            removeByName(oldName);
            add(newMood);
            return true;
        }
        return false;
    }

    public void clearAll() {
        availableMoods.clear();
    }

    public boolean contains(String name) {
        return getByName(name).isPresent();
    }
}

