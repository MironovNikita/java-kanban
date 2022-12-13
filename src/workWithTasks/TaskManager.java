package workWithTasks;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.HashMap;

public interface TaskManager {
    String getAll();
    void delAll();
    Task getById(int id);
    void create (Task task);
    void updateTask (Task task);
    void updateTask (SubTask sub);
    void delById(int id);
    HashMap<Integer, SubTask> getSubTaskList(Epic epic);
    HistoryManager getHistory();
}
