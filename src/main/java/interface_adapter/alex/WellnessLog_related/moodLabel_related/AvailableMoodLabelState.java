package interface_adapter.alex.WellnessLog_related.moodLabel_related;

import java.util.ArrayList;
import java.util.List;

public class AvailableMoodLabelState {

    // 表示 UI 中的每一个 MoodLabel 条目
    public static class MoodLabelEntry {
        private String name;
        private String type; // "Positive" 或 "Negative"
        private boolean isEditing;
        private boolean isNew; // true 表示是新添加的还没保存的条目

        public MoodLabelEntry(String name, String type) {
            this.name = name;
            this.type = type;
            this.isEditing = false;
            this.isNew = false;
        }

        // ---------------- Getters ----------------
        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public boolean isEditing() {
            return isEditing;
        }

        public boolean isNew() {
            return isNew;
        }

        // ---------------- Setters ----------------
        public void setName(String name) {
            this.name = name;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setEditing(boolean editing) {
            this.isEditing = editing;
        }

        public void setNew(boolean aNew) {
            this.isNew = aNew;
        }
    }

    // 当前 UI 要渲染的条目列表
    private List<MoodLabelEntry> moodLabels = new ArrayList<>();

    // ---------------- Getters ----------------
    public List<MoodLabelEntry> getMoodLabels() {
        return moodLabels;
    }

    // ---------------- Setters ----------------
    public void setMoodLabels(List<MoodLabelEntry> moodLabels) {
        this.moodLabels = moodLabels;
    }

    // 根据类型过滤
    public List<MoodLabelEntry> getPositiveLabels() {
        List<MoodLabelEntry> positives = new ArrayList<>();
        for (MoodLabelEntry entry : moodLabels) {
            if ("Positive".equalsIgnoreCase(entry.getType())) {
                positives.add(entry);
            }
        }
        return positives;
    }

    public List<MoodLabelEntry> getNegativeLabels() {
        List<MoodLabelEntry> negatives = new ArrayList<>();
        for (MoodLabelEntry entry : moodLabels) {
            if ("Negative".equalsIgnoreCase(entry.getType())) {
                negatives.add(entry);
            }
        }
        return negatives;
    }
}

