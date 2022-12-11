import java.util.ArrayDeque;
import java.util.HashMap;

public interface TaskManager {
    public String getAll();
    public void delAll();
    public Task getById(int id);
    public void create (Task task);
    public void updateTask (Task task);
    public void updateTask (SubTask sub);
    public void delById(int id);
    public HashMap<Integer, SubTask> getSubTaskList(Epic epic);
    public HistoryManager getHistory();
}
