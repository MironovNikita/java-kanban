import java.util.ArrayDeque;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int historySize = 10;
    public ArrayDeque<Task> history = new ArrayDeque<>(historySize);

    @Override
    public ArrayDeque<Task> getHistory() {
        return history;
    }

    @Override
    public void addToHistory(Task task) {
        if(history.size() > historySize) {
            history.removeFirst();
        }
        history.addLast(task);
    }

    @Override
    public String toString() {
        return "История просмотров:\n" + history;
    }
}
