package interface_adapter;

import entity.Goal;
import java.util.List;

public interface GoalRepository {
    List<Goal> getAllGoals();
}
