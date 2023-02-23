package KV_Server;

import API.HttpTaskServer;
import API.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

public class ServerHandleCheck {

    public static void main(String[] args) throws IOException, InterruptedException {
        /*Семён, привет! Спасибо за ответ! Кстати, с прошедшим праздником тебя! А то я грузился поздно, не сообразил,
        что уже 23-е число...
        Задачи для проверки в Insomnia остались в HttpTaskServer, просто изначально его тестировал, когда ничего не
        было. Ну а потом по традиции оставил "на всякий". Сейчас понимаю, что они там уже не нужны, перенёс их сюда.
        Задание выдалось очень тяжёлое. Структуру уложил в голове, наверное, только сейчас... Ещё и дедлайн на носу,
        мозг устал думать =) Тяжёлый в целом отрезок - 7 и 8 ТЗ. Надеюсь, Spring будет полегче. Что интересно, Spring
        начнём изучать весной)
        По ключам save и load переделал. Ты прав. Я просто пошёл по старой своей реализации и по сути пихал в value
        весь менеджер, что неправильно, т.к. у некоторых задач есть дополнительные поля. И эти поля могли некорректно
        отображаться/возвращаться, т.к. нужен бы был десериализатор на каждый тип задач, если я правильно понял.
        Если можно, оставлю старую реализацию тоже, на всякий случай =)*/

        //Задачи для проверки
        Task task1 = new Task("Почистить зубы", "Тщательно",5);
        Task task2 = new Task("Побриться", "Основательно",120,
                LocalDateTime.of(2023,1,2,12,0));
        Epic epic1 = new Epic("Переезд", "В новый дом",0);
        SubTask sub1 = new SubTask("Собрать все коробки", "Да, да... Все коробочки!",180,
                LocalDateTime.of(2023,2,1,10,0));
        SubTask sub2 = new SubTask("Упаковать кошку", "Прощаемся с кошкой!",10,
                LocalDateTime.of(2023,2,3,12,0));
        SubTask sub3 = new SubTask("Сделать домашнее задание", "До воскресенья",1);

        Epic epic2 = new Epic("Учёба", "Изучаем JAVA",0);
        SubTask sub4 = new SubTask("Порадоваться выполненному заданию", "Но ждать замечаний :)",
                10, LocalDateTime.of(2023, 3,10,10,0));

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

        //Запуск серверов
        KVServer kvServer = new KVServer();
        kvServer.start();
        HttpTaskServer httpTaskServer = new HttpTaskServer();

        //Добавление обычных задач на сервер
        URI url = URI.create("http://localhost:8080/tasks/task/");
        String taskJson = gson.toJson(task1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(taskJson);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        taskJson = gson.toJson(task2);
        body = HttpRequest.BodyPublishers.ofString(taskJson);
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        //Считывание задачи с сервера
        URI getUri = URI.create("http://localhost:8080/tasks/task/?id=" + task1.getId());
        request = HttpRequest.newBuilder()
                .uri(getUri)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("-----------------------------");
        System.out.println("Считанная с сервера задача:");
        System.out.println(gson.fromJson(response.body(), Task.class));

        //Считывание списка обычных задач с сервера
        getUri = URI.create("http://localhost:8080/tasks/task/");
        request = HttpRequest.newBuilder()
                .uri(getUri)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("-----------------------------");
        System.out.println("Считанные с сервера обычные задачи:");
        Type taskListType = new TypeToken<List<Task>>() {
        }.getType();
        System.out.println(gson.fromJson(response.body(), taskListType).toString());

        //Заполнение менеджера эпиками и подзадачами
        url = URI.create("http://localhost:8080/tasks/epic/");
        taskJson = gson.toJson(epic1);
        body = HttpRequest.BodyPublishers.ofString(taskJson);
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        epic1.createSubTask(sub1);
        epic1.createSubTask(sub2);
        epic1.createSubTask(sub3);

        url = URI.create("http://localhost:8080/tasks/subtask/");
        taskJson = gson.toJson(sub1);
        body = HttpRequest.BodyPublishers.ofString(taskJson);
        client = HttpClient.newHttpClient();
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        url = URI.create("http://localhost:8080/tasks/subtask/");
        taskJson = gson.toJson(sub2);
        body = HttpRequest.BodyPublishers.ofString(taskJson);
        client = HttpClient.newHttpClient();
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        url = URI.create("http://localhost:8080/tasks/subtask/");
        taskJson = gson.toJson(sub3);
        body = HttpRequest.BodyPublishers.ofString(taskJson);
        client = HttpClient.newHttpClient();
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        url = URI.create("http://localhost:8080/tasks/epic/");
        taskJson = gson.toJson(epic2);
        body = HttpRequest.BodyPublishers.ofString(taskJson);
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        epic2.createSubTask(sub4);
        url = URI.create("http://localhost:8080/tasks/subtask/");
        taskJson = gson.toJson(sub4);
        body = HttpRequest.BodyPublishers.ofString(taskJson);
        client = HttpClient.newHttpClient();
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        //Получение эпика
        getUri = URI.create("http://localhost:8080/tasks/epic/?id=" + epic1.getId());
        request = HttpRequest.newBuilder()
                .uri(getUri)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("-----------------------------");
        System.out.println("Считанный с сервера эпик:");
        System.out.println(gson.fromJson(response.body(), Epic.class));

        //Получение списка эпиков
        getUri = URI.create("http://localhost:8080/tasks/epic/");
        request = HttpRequest.newBuilder()
                .uri(getUri)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("-----------------------------");
        System.out.println("Считанные с сервера эпики:");
        Type epicListType = new TypeToken<List<Epic>>() {
        }.getType();
        System.out.println(gson.fromJson(response.body(), epicListType).toString());

        //Получение подзадачи по ID
        getUri = URI.create("http://localhost:8080/tasks/subtask/?id=" + sub3.getId());
        request = HttpRequest.newBuilder()
                .uri(getUri)
                .GET()
                .build();
        client = HttpClient.newHttpClient();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("-----------------------------");
        System.out.println("Считанная с сервера подзадача:");
        System.out.println(gson.fromJson(response.body(), SubTask.class));

        //Считывание списка подзадач с сервера
        System.out.println("-----------------------------");
        System.out.println("Считанные с сервера подзадачи:");
        getUri = URI.create("http://localhost:8080/tasks/subtask/");
        request = HttpRequest.newBuilder()
                .uri(getUri)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type subTaskListType = new TypeToken<List<SubTask>>() {
        }.getType();
        System.out.println(gson.fromJson(response.body(), subTaskListType).toString());

        //Считывание истории (получал задачи 1,3,6)
        getUri = URI.create("http://localhost:8080/tasks/history");
        request = HttpRequest.newBuilder()
                .uri(getUri)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("-----------------------------");
        System.out.println("Считанная с сервера история:");
        System.out.println(gson.fromJson(response.body(), subTaskListType).toString());

        //Остановка серверов
        httpTaskServer.stop();
        kvServer.stop();
    }

}
