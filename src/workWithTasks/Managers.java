package workWithTasks;

public class Managers {
    public static TaskManager getDefault() {
        //return new InMemoryTaskManager();
        //return new FileBackedTasksManager();
        return new HttpTaskManager("http://localhost:8078");
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}