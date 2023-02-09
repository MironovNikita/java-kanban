package workWithTasks;

public class Managers {
    public static TaskManager getDefault() {
        //return new InMemoryTaskManager();
        return new FileBackedTasksManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}