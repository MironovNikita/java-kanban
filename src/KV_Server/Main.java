package KV_Server;

import API.HttpTaskServer;
import API.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        /*Семён, привет! Сделал небольшой main тут, для проверки Остальное проверяется в тестах.
        Также прогонял через Insomnia, всё было хорошо. Буду ждать ревью =)*/

        //Запуск серверов
        KVServer kvServer = new KVServer();
        kvServer.start();
        HttpTaskServer httpTaskServer = new HttpTaskServer();

        //Добавление задачи на сервер
        Task task = new Task("Побриться", "Основательно",120,
                LocalDateTime.of(2023,1,2,12,0));
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

        //Считывание задачи с сервера
        URI getUri = URI.create("http://localhost:8080/tasks/task/?id=1");
        request = HttpRequest.newBuilder()
                .uri(getUri)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("-----------------------------");
        System.out.println("Считанная с сервера задача");
        System.out.println(gson.fromJson(response.body(), Task.class));

        //Остановка серверов
        httpTaskServer.stop();
        kvServer.stop();
    }

}
