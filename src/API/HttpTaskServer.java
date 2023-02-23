package API;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import workWithTasks.Managers;
import workWithTasks.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private final TaskManager manager = Managers.getDefault();
    public HttpServer httpServer;
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public HttpTaskServer() throws IOException {
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TasksHandler());
        httpServer.start();
        System.out.println("НТТР-сервер запущен на " + PORT + " порту!");
    }

    public void stop() {
        httpServer.stop(1);
    }

    class TasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Endpoint endpoint = getEndpoint(exchange.getRequestURI().toString(), exchange.getRequestMethod());
            switch (endpoint) {
                case GET_TASKS:
                    List<Task> allTasks = new ArrayList<>(manager.getTaskList().values());
                    if(!allTasks.isEmpty()) {
                        writeResponse(exchange, gson.toJson(allTasks), 200);
                    } else writeResponse(exchange, "Список задач пуст!", 204);
                    break;
                case GET_TASK_BY_ID:
                    Optional<Integer> taskIdOpt = getAnyTaskId(exchange);
                    if(taskIdOpt.isEmpty()) {
                        writeResponse(exchange, "Некорректный идентификатор задачи", 400);
                        return;
                    }
                    int taskId = taskIdOpt.get();
                    if(manager.getTaskList().containsKey(taskId)) {
                        writeResponse(exchange, gson.toJson(manager.getById(taskId)), 200);
                        break;
                    }
                    writeResponse(exchange, "Задача с идентификатором " + taskId + " не найдена",
                            404);
                    break;
                case POST_TASK:
                    InputStream inputStreamTask = exchange.getRequestBody();
                    String bodyTask = new String(inputStreamTask.readAllBytes(), DEFAULT_CHARSET);
                    Task task;
                    try {
                        task = gson.fromJson(bodyTask, Task.class);
                        if(task.getName().isEmpty() || task.getDescription().isEmpty()) {
                            writeResponse(exchange, "Имя и описание задачи не могут быть пустыми",
                                    400);
                            return;
                        }
                        if(manager.getTaskList().containsKey(task.getId())) {
                            manager.updateTask(task);
                            manager.getById(task.getId()).setName(task.getName());
                            manager.getById(task.getId()).setDescription(task.getDescription());
                            manager.getById(task.getId()).setDuration(task.getDuration());
                            if(task.getStartTime() != null) {
                                manager.getById(task.getId()).setStartTime(task.getStartTime());
                            }
                            writeResponse(exchange, "Задача обновлена", 201);
                        } else {
                            manager.create(task);
                            writeResponse(exchange, "Задача добавлена в менеджер", 202);
                            break;
                        }
                    } catch (JsonSyntaxException exception) {
                        writeResponse(exchange, "Получен некорректный JSON", 400);
                        break;
                    }
                    break;
                case DELETE_TASK_BY_ID:
                    Optional<Integer> taskIdToDelete = getAnyTaskId(exchange);
                    if(taskIdToDelete.isEmpty()) {
                        writeResponse(exchange, "Некорректный идентификатор задачи", 400);
                        return;
                    }
                    int idOfTask = taskIdToDelete.get();
                    if(manager.getTaskList().containsKey(idOfTask)) {
                        manager.delById(idOfTask);
                        writeResponse(exchange, "Задача с id: " + idOfTask + " удалена", 200);
                        break;
                    }
                    writeResponse(exchange, "Задача с идентификатором " + idOfTask + " не найдена",
                            404);
                    break;
                case DELETE_TASKLIST:
                    if(manager.getTaskList().isEmpty()) {
                        writeResponse(exchange, "Нет задач для удаления. Список задач пуст",
                                404);
                    } else {
                        manager.getTaskList().clear();
                        writeResponse(exchange, "Список задач очищен", 200);
                    }
                    break;
                case GET_SUBTASKS:
                    List<SubTask> allSubTasks = new ArrayList<>(manager.getSubTaskList().values());
                    if(!allSubTasks.isEmpty()) {
                        writeResponse(exchange, gson.toJson(allSubTasks), 200);
                    } else writeResponse(exchange, "Список подзадач пуст!", 204);
                    break;
                case GET_SUBTASK_BY_ID:
                    Optional<Integer> subtaskIdOpt = getAnyTaskId(exchange);
                    if(subtaskIdOpt.isEmpty()) {
                        writeResponse(exchange, "Некорректный идентификатор подзадачи", 400);
                        return;
                    }
                    int subtaskId = subtaskIdOpt.get();
                    if(manager.getSubTaskList().containsKey(subtaskId)) {
                        writeResponse(exchange, gson.toJson(manager.getById(subtaskId)), 200);
                        break;
                    }
                    writeResponse(exchange, "Подзадача с идентификатором " + subtaskId + " не найдена",
                            404);
                    break;
                case POST_SUBTASK:
                    InputStream inputStreamSub = exchange.getRequestBody();
                    String bodySub = new String(inputStreamSub.readAllBytes(), DEFAULT_CHARSET);
                    SubTask subtask;
                    try {
                        subtask = gson.fromJson(bodySub, SubTask.class);
                        if(subtask.getName().isEmpty() || subtask.getDescription().isEmpty()) {
                            writeResponse(exchange, "Имя и описание задачи не могут быть пустыми",
                                    400);
                            return;
                        }
                        if(manager.getSubTaskList().containsKey(subtask.getId())) {
                            manager.updateTask(subtask);
                            manager.getById(subtask.getId()).setName(subtask.getName());
                            manager.getById(subtask.getId()).setDescription(subtask.getDescription());
                            manager.getById(subtask.getId()).setDuration(subtask.getDuration());
                            if(subtask.getStartTime() != null) {
                                manager.getById(subtask.getId()).setStartTime(subtask.getStartTime());
                            }
                            writeResponse(exchange, "Задача обновлена", 201);
                        } else {
                            manager.create(subtask);
                            writeResponse(exchange, "Задача добавлена в менеджер", 202);
                            break;
                        }
                    } catch (JsonSyntaxException exception) {
                        writeResponse(exchange, "Получен некорректный JSON", 400);
                        break;
                    }
                    break;
                case DELETE_SUBTASK_BY_ID:
                    Optional<Integer> subTaskIdToDelete = getAnyTaskId(exchange);
                    if(subTaskIdToDelete.isEmpty()) {
                        writeResponse(exchange, "Некорректный идентификатор подзадачи", 400);
                        return;
                    }
                    int idOfSubTask = subTaskIdToDelete.get();
                    if(manager.getSubTaskList().containsKey(idOfSubTask)) {
                        manager.delById(idOfSubTask);
                        for(Epic epic : manager.getEpicList().values()) {
                            for(SubTask subTask : epic.getSubTaskList().values()) {
                                if(subTask.getId() == idOfSubTask) {
                                    epic.getSubTaskList().remove(idOfSubTask);
                                    break;
                                }
                            }
                        }
                        writeResponse(exchange, "Подзадача с id: " + idOfSubTask + " удалена",
                                200);
                        break;
                    }
                    writeResponse(exchange, "Подзадача с идентификатором " + idOfSubTask + " не найдена",
                            404);
                    break;
                case DELETE_SUBTASKLIST:
                    if(manager.getSubTaskList().isEmpty()) {
                        writeResponse(exchange, "Нет подзадач для удаления. Список подзадач пуст",
                                404);
                    } else {
                        manager.getSubTaskList().clear();
                        writeResponse(exchange, "Список подзадач очищен", 200);
                    }
                    break;
                case GET_EPICS:
                    List<Epic> allEpics = new ArrayList<>(manager.getEpicList().values());
                    if(!allEpics.isEmpty()) {
                        writeResponse(exchange, gson.toJson(allEpics), 200);
                    } else writeResponse(exchange, "Список эпиков пуст!", 204);
                    break;
                case GET_EPIC_BY_ID:
                    Optional<Integer> epicIdOpt = getAnyTaskId(exchange);
                    if(epicIdOpt.isEmpty()) {
                        writeResponse(exchange, "Некорректный идентификатор эпика", 400);
                        return;
                    }
                    int epicId = epicIdOpt.get();
                    if(manager.getEpicList().containsKey(epicId)) {
                        writeResponse(exchange, gson.toJson(manager.getById(epicId)), 200);
                        break;
                    }
                    writeResponse(exchange, "Эпик с идентификатором " + epicId + " не найден",
                            404);
                    break;
                case POST_EPIC:
                    InputStream inputStreamEpic = exchange.getRequestBody();
                    String bodyEpic = new String(inputStreamEpic.readAllBytes(), DEFAULT_CHARSET);
                    Epic epic;
                    try {
                        epic = gson.fromJson(bodyEpic, Epic.class);
                        if(epic.getName().isEmpty() || epic.getDescription().isEmpty()) {
                            writeResponse(exchange, "Имя и описание задачи не могут быть пустыми",
                                    400);
                            return;
                        }
                        if(manager.getEpicList().containsKey(epic.getId())) {
                            manager.getById(epic.getId()).setName(epic.getName());
                            manager.getById(epic.getId()).setDescription(epic.getDescription());
                            manager.getById(epic.getId()).setDuration(epic.getDuration());
                            if(epic.getStartTime() != null) {
                                manager.getById(epic.getId()).setStartTime(epic.getStartTime());
                            }
                            writeResponse(exchange, "Задача обновлена", 201);
                        } else {
                            manager.create(epic);
                            writeResponse(exchange, "Задача добавлена в менеджер", 202);
                            break;
                        }
                    } catch (JsonSyntaxException exception) {
                        writeResponse(exchange, "Получен некорректный JSON", 400);
                        break;
                    }
                    break;
                case DELETE_EPIC_BY_ID:
                    Optional<Integer> epicIdToDelete = getAnyTaskId(exchange);
                    if(epicIdToDelete.isEmpty()) {
                        writeResponse(exchange, "Некорректный идентификатор эпика", 400);
                        return;
                    }
                    int idOfEpic = epicIdToDelete.get();
                    if(manager.getEpicList().containsKey(idOfEpic)) {
                        manager.delById(idOfEpic);
                        writeResponse(exchange, "Эпик с id: " + idOfEpic + " удалён", 200);
                        break;
                    }
                    writeResponse(exchange, "Эпик с идентификатором " + idOfEpic + " не найден",
                            404);
                    break;
                case DELETE_EPICLIST:
                    if(manager.getEpicList().isEmpty()) {
                        writeResponse(exchange, "Нет эпиков для удаления. Список эпиков пуст",
                                404);
                    } else {
                        manager.getEpicList().clear();
                        writeResponse(exchange, "Список эпиков очищен", 200);
                    }
                    break;
                case DELETE_ALL_TASKS:
                    if(manager.getTaskList().isEmpty() && manager.getSubTaskList().isEmpty()
                            && manager.getEpicList().isEmpty()) {
                        writeResponse(exchange, "Нет задач для удаления. Все списки задач пусты",
                                404);
                    } else {
                        manager.getTaskList().clear();
                        manager.getSubTaskList().clear();
                        manager.getEpicList().clear();
                        writeResponse(exchange, "Менеджер задач очищен", 200);
                    }
                    break;
                case GET_EPIC_SUBTASKS:
                    Optional<Integer> epicSubIdOpt = getAnyTaskId(exchange);
                    if(epicSubIdOpt.isEmpty()) {
                        writeResponse(exchange, "Некорректный идентификатор эпика", 400);
                        return;
                    }
                    int epicSubId = epicSubIdOpt.get();
                    if(manager.getEpicList().containsKey(epicSubId)) {
                        List<SubTask> epicSubTasks = new ArrayList<>(manager.getEpicList().get(epicSubId)
                                .getSubTaskList().values());
                        writeResponse(exchange, gson.toJson(epicSubTasks), 200);
                        break;
                    }
                    writeResponse(exchange, "Эпик с идентификатором " + epicSubId + " не найден",
                            404);
                    break;
                case GET_HISTORY:
                    if(manager.getHistory().getHistory().isEmpty()) {
                        writeResponse(exchange, "История просмотров пуста",
                                204);
                    } else {
                        writeResponse(exchange, gson.toJson(manager.getHistory().getHistory()), 200);
                    }
                    break;
                case GET_PRIORITIZED_TASKS:
                    if(manager.getTaskList().isEmpty() && manager.getSubTaskList().isEmpty()
                            && manager.getEpicList().isEmpty()) {
                        writeResponse(exchange, "Нет задач для отображения. Все списки задач пусты",
                                404);
                    } else {
                        writeResponse(exchange, gson.toJson(manager.getPrioritizedTasks()), 200);
                    }
                    break;
                default:
                    writeResponse(exchange, "Такого эндпоинта не существует. Проверьте запрос",
                            404);
            }
        }

        private Endpoint getEndpoint(String requestPath, String requestMethod) {
            String[] pathParts = requestPath.split("/");

            if(pathParts[1].equals("tasks") && pathParts.length == 2) return Endpoint.GET_PRIORITIZED_TASKS;
            if(pathParts[2].equals("task")) {
                switch (requestMethod) {
                    case "GET":
                        if(pathParts.length == 3) return Endpoint.GET_TASKS;
                        else if(pathParts[3].startsWith("?id=") && pathParts.length == 4)
                            return Endpoint.GET_TASK_BY_ID;
                        break;
                    case "POST":
                        return Endpoint.POST_TASK;
                    case "DELETE":
                        if(pathParts.length == 3) return Endpoint.DELETE_TASKLIST;
                        else if(pathParts[3].startsWith("?id=") && pathParts.length == 4)
                            return Endpoint.DELETE_TASK_BY_ID;
                        break;
                }
                return Endpoint.UNKNOWN;
            } else if(pathParts[2].equals("subtask")) {
                switch (requestMethod) {
                    case "GET":
                        if(pathParts.length == 3) return Endpoint.GET_SUBTASKS;
                        else if(pathParts[3].startsWith("?id=") && pathParts.length == 4)
                            return Endpoint.GET_SUBTASK_BY_ID;
                        break;
                    case "POST":
                        return Endpoint.POST_SUBTASK;
                    case "DELETE":
                        if(pathParts.length == 3) return Endpoint.DELETE_SUBTASKLIST;
                        else if(pathParts[3].startsWith("?id=") && pathParts.length == 4)
                            return Endpoint.DELETE_SUBTASK_BY_ID;
                        break;
                }
                return Endpoint.UNKNOWN;
            } else if(pathParts[2].equals("epic")) {
                switch (requestMethod) {
                    case "GET":
                        if(pathParts.length == 3) return Endpoint.GET_EPICS;
                        else if(pathParts[3].startsWith("?id=") && pathParts.length == 4)
                            return Endpoint.GET_EPIC_BY_ID;
                        break;
                    case "POST":
                        return Endpoint.POST_EPIC;
                    case "DELETE":
                        if(pathParts.length == 3) return Endpoint.DELETE_EPICLIST;
                        else if(pathParts[3].startsWith("?id=") && pathParts.length == 4)
                            return Endpoint.DELETE_EPIC_BY_ID;
                        break;
                }
                return Endpoint.UNKNOWN;
            } else if(pathParts[2].equals("all") && pathParts.length == 3) {
                if ("DELETE".equals(requestMethod)) {
                    return Endpoint.DELETE_ALL_TASKS;
                }
                return Endpoint.UNKNOWN;
            } else if (pathParts[2].equals("subtasks") && pathParts[3].equals("epic")
                    && pathParts[4].startsWith("?id=") && pathParts.length == 5) return Endpoint.GET_EPIC_SUBTASKS;
              else if(pathParts[2].equals("history") && pathParts.length == 3) return Endpoint.GET_HISTORY;
            return Endpoint.UNKNOWN;
        }


        private void writeResponse(HttpExchange exchange, String responseString, int responseCode) throws IOException {
            if(responseString.isBlank()) {
                exchange.sendResponseHeaders(responseCode, 0);
            } else {
                byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);

                if(responseCode == 204) {
                    exchange.getResponseHeaders().add("X-NoData", "No data to display");
                    exchange.sendResponseHeaders(responseCode, -1);
                } else exchange.sendResponseHeaders(responseCode, bytes.length);

                try (OutputStream outputStream = exchange.getResponseBody()) {
                    outputStream.write(bytes);
                }
            }
            exchange.close();
        }

        private Optional<Integer> getAnyTaskId(HttpExchange exchange) {
            String path = exchange.getRequestURI().toString();
            try {
                int index = path.lastIndexOf("=") + 1;
                return Optional.of(Integer.parseInt(path.substring(index)));
            } catch (NumberFormatException exception) {
                return Optional.empty();
            }
        }
    }
    public TaskManager getManager() {
        return manager;
    }
}