package workWithTasks;

import tasks.Task;
import java.util.ArrayDeque;

interface HistoryManager {
    ArrayDeque<Task> getHistory();
    void addToHistory(Task task);
}
