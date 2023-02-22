package workWithTasks;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.HashMap;
import java.util.TreeSet;

public interface TaskManager {
    void delAll();
    Task getById(int id);
    void create (Task task);
    void updateTask (Task task);
    void updateTask (SubTask sub);
    void delById(int id);
    HashMap<Integer, Task> getTaskList();
    HashMap<Integer, Epic> getEpicList();
    HashMap<Integer, SubTask> getSubTaskList();
    HashMap<Integer, SubTask> getSubTaskList(Epic epic);
    HistoryManager getHistory();
    TreeSet<Task> getPrioritizedTasks();
}
