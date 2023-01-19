package workWithTasks;

public class Managers {
    public static TaskManager getDefault() {
        //return new InMemoryTaskManager();
        return new FileBackedTasksManager();
    }
    //Если я правильно понял замечание, то нужно сделать такое исправление

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}