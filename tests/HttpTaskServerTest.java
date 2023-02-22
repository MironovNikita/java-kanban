import API.HttpTaskServer;
import API.LocalDateTimeAdapter;
import KV_Server.KVServer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.*;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStartTimeComparator;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class HttpTaskServerTest {
    KVServer kvServer;
    HttpTaskServer httpTaskServer;

    @BeforeEach
    public void serverStart() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        httpTaskServer = new HttpTaskServer();
    }

    @BeforeEach
    void setTaskUniqIdToDefault() {
        Task.setUniqId(1);
    }

    @AfterEach
    void serverStop() {
        kvServer.stop();
        httpTaskServer.stop();
    }

    void postTask(Task task) throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task/");
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        String taskJson = gson.toJson(task);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(taskJson);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }
    void deleteTask(String url) throws IOException, InterruptedException {
        URI getUri = URI.create(url);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(getUri)
                .DELETE()
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }
    Task getTask(String url) throws IOException, InterruptedException {
        URI getUri = URI.create(url);
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(getUri)
                .GET()
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return gson.fromJson(response.body(), Task.class);
    }
    List<Task> getTaskList(String url) throws IOException, InterruptedException {
        URI getUri = URI.create(url);
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(getUri)
                .GET()
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type taskListType = new TypeToken<List<Task>>() {
        }.getType();
        return gson.fromJson(response.body(), taskListType);
    }

    @DisplayName("Эндпоинты POST_TASK и GET_TASK_BY_ID")
    @Test
    void getAndPostTaskEndpoint() throws IOException, InterruptedException {
        Task task = new Task("Побриться", "Основательно",120,
                LocalDateTime.of(2023,1,2,12,0));
        postTask(task);
        //Считывание задачи с сервера
        Task verifyTask = getTask("http://localhost:8080/tasks/task/?id=" + task.getId());
        Assertions.assertEquals(task, verifyTask, "Задачи не совпадают!");
    }

    @DisplayName("Эндпоинт GET_TASKS")
    @Test
    void getTaskListEndpoint() throws IOException, InterruptedException {
        Task task1 = new Task("Почистить зубы", "Тщательно",5);
        Task task2 = new Task("Побриться", "Основательно",120,
                LocalDateTime.of(2023,1,2,12,0));
        Task task3 = new Task("Прилететь вовремя", "Домой");
        postTask(task1);
        postTask(task2);
        postTask(task3);
        List<Task> taskList = getTaskList("http://localhost:8080/tasks/task/");
        List<Task> verifyTaskList = new ArrayList<>();
        verifyTaskList.add(task1);
        verifyTaskList.add(task2);
        verifyTaskList.add(task3);
        Assertions.assertEquals(taskList, verifyTaskList, "Списки задач не совпадают!");
    }

    @DisplayName("Эндпоинт DELETE_TASK_BY_ID")
    @Test
    void deleteTaskByIdEndpoint() throws IOException, InterruptedException {
        Task task1 = new Task("Почистить зубы", "Тщательно",5);
        Task task2 = new Task("Побриться", "Основательно",120,
                LocalDateTime.of(2023,1,2,12,0));
        postTask(task1);
        postTask(task2);
        deleteTask("http://localhost:8080/tasks/task/?id=" + task2.getId());
        List<Task> taskList = getTaskList("http://localhost:8080/tasks/task/");
        List<Task> verifyTaskList = new ArrayList<>();
        verifyTaskList.add(task1);
        Assertions.assertEquals(taskList, verifyTaskList, "Задача не удалилась!");
    }

    @DisplayName("Эндпоинт DELETE_TASKLIST")
    @Test
    void deleteAllTasksEndpoint() throws IOException, InterruptedException {
        Task task1 = new Task("Почистить зубы", "Тщательно",5);
        Task task2 = new Task("Побриться", "Основательно",120,
                LocalDateTime.of(2023,1,2,12,0));
        Task task3 = new Task("Прилететь вовремя", "Домой");
        postTask(task1);
        postTask(task2);
        postTask(task3);
        deleteTask("http://localhost:8080/tasks/task/");
        List<Task> taskListAfterDelete = getTaskList("http://localhost:8080/tasks/task/");
        Assertions.assertNull(taskListAfterDelete, "Список задач не очищен!");
    }

    void postSubTask(SubTask subtask) throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        String taskJson = gson.toJson(subtask);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(taskJson);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    SubTask getSubTask(String url) throws IOException, InterruptedException {
        URI getUri = URI.create(url);
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(getUri)
                .GET()
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return gson.fromJson(response.body(), SubTask.class);
    }

    List<SubTask> getSubTaskList(String url) throws IOException, InterruptedException {
        URI getUri = URI.create(url);
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(getUri)
                .GET()
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type subTaskListType = new TypeToken<List<SubTask>>() {
        }.getType();
        return gson.fromJson(response.body(), subTaskListType);
    }

    void deleteSubTask(String url) throws IOException, InterruptedException {
        URI getUri = URI.create(url);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(getUri)
                .DELETE()
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @DisplayName("Эндпоинты POST_SUBTASK и GET_SUBTASK_BY_ID")
    @Test
    void getAndPostSubTaskEndpoint() throws IOException, InterruptedException {
        Epic epic = new Epic("Переезд", "В новый дом",0);
        SubTask sub = new SubTask("Упаковать кошку", "Прощаемся с кошкой!", 0);
        epic.createSubTask(sub);
        postEpic(epic);
        postSubTask(sub);
        //Считывание задачи с сервера
        SubTask verifySubTask = getSubTask("http://localhost:8080/tasks/subtask/?id=" + sub.getId());
        Assertions.assertEquals(sub, verifySubTask, "Подзадачи не совпадают!");
    }

    @DisplayName("Эндпоинт GET_SUBTASKS")
    @Test
    void getSubTaskListEndpoint() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic", "Description of epic", 0);
        SubTask subTask1 = new SubTask("Subtask 1", "Description of subtask1", 100,
                LocalDateTime.of(2023,1,1,20,20));
        SubTask subTask2 = new SubTask("Subtask 2", "Description of subtask2", 10,
                LocalDateTime.of(2023,2,2,10,20));
        SubTask subTask3 = new SubTask("Subtask 3", "Description of subtask3", 180,
                LocalDateTime.of(2023,3,1,10,20));
        postEpic(epic);
        epic.createSubTask(subTask1);
        epic.createSubTask(subTask2);
        epic.createSubTask(subTask3);
        postSubTask(subTask1);
        postSubTask(subTask2);
        postSubTask(subTask3);
        List<SubTask> subTasksList = getSubTaskList("http://localhost:8080/tasks/subtask/");
        List<Task> verifySubTaskList = new ArrayList<>();
        verifySubTaskList.add(subTask1);
        verifySubTaskList.add(subTask2);
        verifySubTaskList.add(subTask3);
        Assertions.assertEquals(subTasksList, verifySubTaskList, "Списки подзадач не совпадают!");
    }

    @DisplayName("Эндпоинт DELETE_SUBTASK_BY_ID")
    @Test
    void deleteSubtaskByIdEndpoint() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic", "Description of epic", 0);
        SubTask subTask1 = new SubTask("Subtask 1", "Description of subtask1", 100,
                LocalDateTime.of(2023,1,1,20,20));
        SubTask subTask2 = new SubTask("Subtask 2", "Description of subtask2", 10,
                LocalDateTime.of(2023,2,2,10,20));
        SubTask subTask3 = new SubTask("Subtask 3", "Description of subtask3", 180,
                LocalDateTime.of(2023,3,1,10,20));
        postEpic(epic);
        epic.createSubTask(subTask1);
        epic.createSubTask(subTask2);
        epic.createSubTask(subTask3);
        postSubTask(subTask1);
        postSubTask(subTask2);
        postSubTask(subTask3);
        deleteSubTask("http://localhost:8080/tasks/subtask/?id=" + subTask2.getId());
        List<SubTask> subTaskList = getSubTaskList("http://localhost:8080/tasks/subtask/");
        List<SubTask> verifySubTaskList = new ArrayList<>();
        verifySubTaskList.add(subTask1);
        verifySubTaskList.add(subTask3);
        Assertions.assertEquals(subTaskList, verifySubTaskList, "Подзадача не удалилась!");
    }

    @DisplayName("Эндпоинт DELETE_SUBTASKLIST")
    @Test
    void deleteSubTaskListEndpoint() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic", "Description of epic", 0);
        SubTask subTask1 = new SubTask("Subtask 1", "Description of subtask1", 100,
                LocalDateTime.of(2023,1,1,20,20));
        SubTask subTask2 = new SubTask("Subtask 2", "Description of subtask2", 10,
                LocalDateTime.of(2023,2,2,10,20));
        SubTask subTask3 = new SubTask("Subtask 3", "Description of subtask3", 180,
                LocalDateTime.of(2023,3,1,10,20));
        postEpic(epic);
        epic.createSubTask(subTask1);
        epic.createSubTask(subTask2);
        epic.createSubTask(subTask3);
        postSubTask(subTask1);
        postSubTask(subTask2);
        postSubTask(subTask3);
        deleteSubTask("http://localhost:8080/tasks/subtask");
        List<SubTask> subTaskList = getSubTaskList("http://localhost:8080/tasks/subtask/");

        Assertions.assertNull(subTaskList, "Список подзадач не очищен!");
    }

    void postEpic(Epic epic) throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        String taskJson = gson.toJson(epic);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(taskJson);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    Epic getEpic(String url) throws IOException, InterruptedException {
        URI getUri = URI.create(url);
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(getUri)
                .GET()
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return gson.fromJson(response.body(), Epic.class);
    }

    List<Epic> getEpicList(String url) throws IOException, InterruptedException {
        URI getUri = URI.create(url);
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(getUri)
                .GET()
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type epicListType = new TypeToken<List<Epic>>() {
        }.getType();
        return gson.fromJson(response.body(), epicListType);
    }

    void deleteEpic(String url) throws IOException, InterruptedException {
        URI getUri = URI.create(url);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(getUri)
                .DELETE()
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @DisplayName("Эндпоинты POST_EPIC и GET_EPIC_BY_ID")
    @Test
    void getAndPostEpicEndpoint() throws IOException, InterruptedException {
        Epic epic = new Epic("Переезд", "В новый дом",0);
        postEpic(epic);
        //Считывание задачи с сервера
        Epic verifyEpic = getEpic("http://localhost:8080/tasks/epic/?id=" + epic.getId());
        Assertions.assertEquals(epic, verifyEpic, "Подзадачи не совпадают!");
    }

    @DisplayName("Эндпоинт GET_EPICS")
    @Test
    void getEpicListEndpoint() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Epic1", "Description of epic1", 0);
        Epic epic2 = new Epic("Epic2", "Description of epic2", 0);
        Epic epic3 = new Epic("Epic3", "Description of epic3", 0);
        postEpic(epic1);
        postEpic(epic2);
        postEpic(epic3);
        List<Epic> epicList = getEpicList("http://localhost:8080/tasks/epic/");
        List<Epic> verifyEpicList = new ArrayList<>();
        verifyEpicList.add(epic1);
        verifyEpicList.add(epic2);
        verifyEpicList.add(epic3);
        Assertions.assertEquals(epicList, verifyEpicList, "Списки задач не совпадают!");
    }

    @DisplayName("Эндпоинт DELETE_EPIC_BY_ID")
    @Test
    void deleteEpicByIdEndpoint() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Epic1", "Description of epic1", 0);
        Epic epic2 = new Epic("Epic2", "Description of epic2", 0);
        postEpic(epic1);
        postEpic(epic2);
        deleteEpic("http://localhost:8080/tasks/epic/?id=" + epic2.getId());
        List<Epic> epicList = getEpicList("http://localhost:8080/tasks/epic/");
        List<Epic> verifyEpicList = new ArrayList<>();
        verifyEpicList.add(epic1);
        Assertions.assertEquals(epicList, verifyEpicList, "Подзадача не удалилась!");
    }

    @DisplayName("Эндпоинт DELETE_EPICLIST")
    @Test
    void deleteEpicListEndpoint() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Epic1", "Description of epic1", 0);
        Epic epic2 = new Epic("Epic2", "Description of epic2", 0);
        Epic epic3 = new Epic("Epic3", "Description of epic3", 0);
        postEpic(epic1);
        postEpic(epic2);
        postEpic(epic3);
        deleteEpic("http://localhost:8080/tasks/epic");
        List<Epic> epicList = getEpicList("http://localhost:8080/tasks/epic/");

        Assertions.assertNull(epicList, "Список эпиков не очищен!");
    }

    @DisplayName("Эндпоинт DELETE_ALL_TASKS")
    @Test
    void deleteEveryTaskEndpoint() throws IOException, InterruptedException {

        Epic epic = new Epic("Epic", "Description of epic", 0);
        SubTask subTask1 = new SubTask("Subtask 1", "Description of subtask1", 100,
                LocalDateTime.of(2023,1,1,20,20));
        SubTask subTask2 = new SubTask("Subtask 2", "Description of subtask2", 10,
                LocalDateTime.of(2023,2,2,10,20));
        SubTask subTask3 = new SubTask("Subtask 3", "Description of subtask3", 180,
                LocalDateTime.of(2023,3,1,10,20));
        Task task1 = new Task("Почистить зубы", "Тщательно",5);
        Task task2 = new Task("Побриться", "Основательно",120,
                LocalDateTime.of(2023,1,2,12,0));
        postTask(task1);
        postTask(task2);
        postEpic(epic);
        epic.createSubTask(subTask1);
        epic.createSubTask(subTask2);
        epic.createSubTask(subTask3);
        postSubTask(subTask1);
        postSubTask(subTask2);
        postSubTask(subTask3);
        deleteEpic("http://localhost:8080/tasks/epic");
        List<Epic> epicList = getEpicList("http://localhost:8080/tasks/epic/");
        deleteTask("http://localhost:8080/tasks/task");
        List<Task> taskList = getTaskList("http://localhost:8080/tasks/task/");
        deleteSubTask("http://localhost:8080/tasks/subtask");
        List<SubTask> subTaskList = getSubTaskList("http://localhost:8080/tasks/subtask/");
        Assertions.assertNull(epicList);
        Assertions.assertNull(taskList);
        Assertions.assertNull(subTaskList);
    }

    List<SubTask> getEpicSubTaskList(String url) throws IOException, InterruptedException {
        URI getUri = URI.create(url);
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(getUri)
                .GET()
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type subTaskListType = new TypeToken<List<SubTask>>() {
        }.getType();
        return gson.fromJson(response.body(), subTaskListType);
    }

    @DisplayName("Эндпоинт GET_EPIC_SUBTASKS")
    @Test
    void getEpicSubtasksEndpoint() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic", "Description of epic", 0);
        SubTask subTask1 = new SubTask("Subtask 1", "Description of subtask1", 100,
                LocalDateTime.of(2023,1,1,20,20));
        SubTask subTask2 = new SubTask("Subtask 2", "Description of subtask2", 10,
                LocalDateTime.of(2023,2,2,10,20));
        SubTask subTask3 = new SubTask("Subtask 3", "Description of subtask3", 180,
                LocalDateTime.of(2023,3,1,10,20));
        postEpic(epic);
        epic.createSubTask(subTask1);
        epic.createSubTask(subTask2);
        epic.createSubTask(subTask3);
        postSubTask(subTask1);
        postSubTask(subTask2);
        postSubTask(subTask3);
        List<SubTask> epicSubTasks = getEpicSubTaskList("http://localhost:8080/tasks/subtasks/epic/?id="
                + epic.getId());

        List<Task> verifySubTaskList = new ArrayList<>();
        verifySubTaskList.add(subTask1);
        verifySubTaskList.add(subTask2);
        verifySubTaskList.add(subTask3);
        Assertions.assertEquals(epicSubTasks, verifySubTaskList, "Списки подзадач не совпадают!");
    }

    @DisplayName("Эндпоинт GET_HISTORY")
    @Test
    void getHistoryEndpoint() throws IOException, InterruptedException {
        Task task1 = new Task("Почистить зубы", "Тщательно",5);
        Task task2 = new Task("Побриться", "Основательно",120,
                LocalDateTime.of(2023,1,2,12,0));
        Epic epic = new Epic("Epic", "Description of epic", 0);
        SubTask subTask = new SubTask("Subtask ", "Description of subtask", 100,
                LocalDateTime.of(2023,1,1,20,20));
        postTask(task1);
        postTask(task2);
        postEpic(epic);
        epic.createSubTask(subTask);
        postSubTask(subTask);

        getTask("http://localhost:8080/tasks/task/?id=" + task2.getId());
        getTask("http://localhost:8080/tasks/task/?id=" + task1.getId());
        getSubTask("http://localhost:8080/tasks/subtask/?id=" + subTask.getId());
        getEpic("http://localhost:8080/tasks/epic/?id=" + epic.getId());

        List<Integer> verifyListOfIds = List.of(2,1,4,3);
        List<Integer> listofIds = new ArrayList<>();
        List<Task> history = httpTaskServer.getManager().getHistory().getHistory();
        if(!history.isEmpty()) {
            for(Task task : history) {
                listofIds.add(task.getId());
            }
        }
        Assertions.assertEquals(listofIds, verifyListOfIds, "Истории просмотров задач не совпадают!");
    }

    @DisplayName("Эндпоинт GET_PRIORITIZED_TASKS")
    @Test
    void getPrioritiziedTasks() throws IOException, InterruptedException {
        Task task1 = new Task("Почистить зубы", "Тщательно",5);
        Task task2 = new Task("Побриться", "Основательно",120,
                LocalDateTime.of(2023,1,2,12,0));
        Task task3 = new Task("Погулять", "Когда-нибудь",240,
                LocalDateTime.of(2023,2,2,12,0));
        Epic epic = new Epic("Epic", "Description of epic", 0);
        SubTask subTask = new SubTask("Subtask 1", "Description of subtask1", 100,
                LocalDateTime.of(2023,3,1,20,20));
        postTask(task1);
        postTask(task2);
        postTask(task3);
        postEpic(epic);
        epic.createSubTask(subTask);
        postSubTask(subTask);
        List<Task> taskList = getTaskList("http://localhost:8080/tasks/task/");
        List<SubTask> subTasksList = getSubTaskList("http://localhost:8080/tasks/subtask/");
        TaskStartTimeComparator comparator = new TaskStartTimeComparator();
        TreeSet<Task> setByStartTime = new TreeSet<>(comparator);
        if(!taskList.isEmpty()) {
            setByStartTime.addAll(taskList);
        }
        if(!subTasksList.isEmpty()) {
            setByStartTime.addAll(subTasksList);
        }
        TreeSet<Task> verifySet = new TreeSet<>(comparator);
        verifySet.add(task1);
        verifySet.add(task2);
        verifySet.add(task3);
        verifySet.add(subTask);
        Assertions.assertEquals(setByStartTime, verifySet, "Списки приоритетных задач не совпадают!");
    }

    @DisplayName("Эндпоинт UNKNOWN")
    @Test
    void unknownEndpoint() throws IOException, InterruptedException {
        String str = "";
        try{
            getTaskList("http://localhost:8080/jkasjdkd/");
        } catch (JsonSyntaxException exception) {
            str = "Неизвестный запрос";
        }
        Assertions.assertEquals("Неизвестный запрос", str, "Запрос был обработан!");
    }
}
