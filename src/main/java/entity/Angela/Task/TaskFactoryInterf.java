package entity.Angela.Task;


import entity.BeginAndDueDates.BeginAndDueDates;
import entity.Info.Info;

public interface TaskFactoryInterf {
    Task createTask(Info info, BeginAndDueDates beginAndDueDates);
    Task createTask(Info info, BeginAndDueDates beginAndDueDates, Task.Priority priority);
}

