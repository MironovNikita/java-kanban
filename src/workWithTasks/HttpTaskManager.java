package workWithTasks;

import API.LocalDateTimeAdapter;
import KV_Server.KVTaskClient;
import com.google.gson.*;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTasksManager {
    private String url;
    KVTaskClient client;
    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();
    public String Tasks = "Tasks";
    public String Epics = "Epics";
    public String Subtasks = "Subtasks";
    public String History = "History";

    public HttpTaskManager (String url) {
        this.url = url;
        client = new KVTaskClient(url);
    }

    @Override
    public void save() {
        List<Task> taskListToSave = new ArrayList<>();
        getTaskList().values().stream().collect(Collectors.toCollection(() -> taskListToSave));
        String taskListToJson = gson.toJson(taskListToSave);

        List<Epic> epicListToSave = new ArrayList<>();
        getEpicList().values().stream().collect(Collectors.toCollection(()->epicListToSave));
        String epicListToJson = gson.toJson(epicListToSave);

        List<SubTask> subTaskListToSave = new ArrayList<>();
        getSubTaskList().values().stream().collect(Collectors.toCollection(()->subTaskListToSave));
        String subTaskListToJson = gson.toJson(subTaskListToSave);

        List<Task> historyToSave = new ArrayList<>(getHistory().getHistory());
        String historyToJson = gson.toJson(historyToSave);

        client.put(Tasks, taskListToJson);
        client.put(Epics, epicListToJson);
        client.put(Subtasks, subTaskListToJson);
        client.put(History, historyToJson);
    }

    public HttpTaskManager load(){
        final HttpTaskManager httpManager = new HttpTaskManager(url);
        JsonElement jsonElementOfTasks = JsonParser.parseString(client.load(Tasks));
        JsonArray jsonOfTasks = jsonElementOfTasks.getAsJsonArray();
        for (int i = 0; i < jsonOfTasks.size(); i++) {
            Task task = gson.fromJson(jsonOfTasks.get(i), Task.class);
            httpManager.getTaskList().put(task.getId(), task);
        }
        JsonElement jsonElementOfEpics = JsonParser.parseString(client.load(Epics));
        JsonArray jsonOfEpics = jsonElementOfEpics.getAsJsonArray();
        for(int i = 0; i < jsonOfEpics.size(); i++) {
            Epic epic = gson.fromJson(jsonOfEpics.get(i), Epic.class);
            httpManager.getEpicList().put(epic.getId(), epic);
        }
        JsonElement jsonElementOfSubTasks = JsonParser.parseString(client.load(Subtasks));
        JsonArray jsonOfSubTasks = jsonElementOfSubTasks.getAsJsonArray();
        for(int i = 0; i < jsonOfSubTasks.size(); i++) {
            SubTask subTask = gson.fromJson(jsonOfSubTasks.get(i), SubTask.class);
            httpManager.getSubTaskList().put(subTask.getId(), subTask);
        }

        JsonElement jsonElementOfHistory = JsonParser.parseString(client.load(History));
        JsonArray jsonOfHistory = jsonElementOfHistory.getAsJsonArray();
        for(int i = 0; i < jsonOfHistory.size(); i++) {
            Task task = gson.fromJson(jsonOfHistory.get(i), Task.class);
            if(httpManager.getTaskList().containsKey(task.getId())) {
                httpManager.getHistory().addToHistory(httpManager.getTaskList().get(task.getId()));
            } else if(httpManager.getEpicList().containsKey(task.getId())) {
                httpManager.getHistory().addToHistory(httpManager.getEpicList().get(task.getId()));
            } else if (httpManager.getSubTaskList().containsKey(task.getId())) {
                httpManager.getHistory().addToHistory(httpManager.getSubTaskList().get(task.getId()));
            }
        }
        return httpManager;
    }
}
