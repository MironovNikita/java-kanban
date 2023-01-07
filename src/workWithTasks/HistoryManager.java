package workWithTasks;

import tasks.Task;

import java.util.List;

interface HistoryManager {
    List<Task> getHistory();
    void addToHistory(Task task);
    void remove(int id);
}
