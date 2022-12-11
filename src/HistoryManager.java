import java.util.ArrayDeque;

public interface HistoryManager {
    public ArrayDeque<Task> getHistory();
    public void addToHistory(Task task);
}
