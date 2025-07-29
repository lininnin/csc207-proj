package entity.Sophia;

import entity.info.Info;

/**
 * Represents identifying information for a goal, including its own info
 * and the info of the task it targets.
 */
public class GoalInfo {

    private final Info info;
    private Info targetTaskInfo;

    /**
     * Constructs a GoalInfo object with the given info and target task info.
     *
     * @param info The metadata of this goal; must not be null
     * @param targetTaskInfo The metadata of the task this goal is targeting; must not be null
     * @throws IllegalArgumentException if either {@code info} or {@code targetTaskInfo} is null
     */
    public GoalInfo(Info info, Info targetTaskInfo) {
        if (info == null) {
            throw new IllegalArgumentException("Goal info cannot be null.");
        }
        if (targetTaskInfo == null) {
            throw new IllegalArgumentException("Target task info cannot be null.");
        }
        this.info = info;
        this.targetTaskInfo = targetTaskInfo;
    }

    /**
     * Returns the info of this goal.
     *
     * @return The Info object of the goal
     */
    public Info getInfo() {
        return info;
    }

    /**
     * Returns the info of the target task this goal is associated with.
     *
     * @return The Info object of the targeted task
     */
    public Info getTargetTaskInfo() {
        return targetTaskInfo;
    }

    public void setTargetTaskInfo(Info targetTaskInfo) {
        if (targetTaskInfo == null) {
            throw new IllegalArgumentException("Target task info cannot be null.");
        }
        this.targetTaskInfo = targetTaskInfo;
    }
}

