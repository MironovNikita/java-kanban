import KV_Server.KVServer;
import org.junit.jupiter.api.*;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import workWithTasks.HttpTaskManager;
import workWithTasks.Managers;

import java.io.IOException;

public class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    KVServer kvServer;

    @BeforeEach
    public void beforeEach() {
        try {
            kvServer = new KVServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        kvServer.start();
        manager = (HttpTaskManager) Managers.getDefault();
    }

    @AfterEach
    void serverStop() {
        kvServer.stop();
    }

    //Тесты сохранения и восстановления состояния менеджера из сервера
    @DisplayName("Проверка сохранения состояния при пустом списке задач")
    @Test
    void verifySaveAndLoadFunctionsWithEmptyManager() {
        manager.save();
        HttpTaskManager managerToVerify = manager.load();

        Assertions.assertEquals(manager.getTaskList(), managerToVerify.getTaskList(),
                "Списки задач не совпадают!");
        Assertions.assertEquals(manager.getSubTaskList(), managerToVerify.getSubTaskList(),
                "Списки задач не совпадают!");
        Assertions.assertEquals(manager.getEpicList(), managerToVerify.getEpicList(),
                "Списки задач не совпадают!");
    }

    @DisplayName("Проверка сохранения и восстановления состояния при EPIC'е без подзадач")
    @Test
    void shouldSaveManagerOnServer() {
        Epic epic = new Epic("Epic", "Description of epic",0);
        Task task1 = new Task("Task1", "Description of task",9);
        Task task2 = new Task("Task2", "Description of task",19);
        //save() происходит при вызове create
        manager.create(epic);
        manager.create(task1);
        manager.create(task2);

        manager.getById(task2.getId());
        manager.getById(epic.getId());
        manager.getById(task1.getId());

        HttpTaskManager httpTaskManager = manager.load();

        System.out.println(httpTaskManager.getTaskList());
        Assertions.assertEquals(manager.getTaskList(), httpTaskManager.getTaskList()
                ,"Списки задач не совпадают!");
        Assertions.assertEquals(manager.getEpicList(), httpTaskManager.getEpicList(),
                "Списки эпиков не совпадают!");
        Assertions.assertEquals(manager.getSubTaskList(), httpTaskManager.getSubTaskList(),
                "Списки подзадач не совпадают!");
    }

    @DisplayName("Проверка сохранения и восстановления состояния при пустом списке истории")
    @Test
    void verifySaveAndLoadFunctionsWithEmptyHistoryList() {
        Epic epic1 = new Epic("Epic1", "Description of epic",0);
        Epic epic2 = new Epic("Epic2", "Description of epic",0);
        Task task1 = new Task("Task1", "Description of task",14);
        Task task2 = new Task("Task2", "Description of task",12);
        SubTask subTask1 = new SubTask("SubTask1", "Description of subTask",15);
        SubTask subTask2 = new SubTask("SubTask2", "Description of subTask",13);
        epic1.createSubTask(subTask1);
        epic1.createSubTask(subTask2);
        //save() происходит при вызове create
        manager.create(epic1);
        manager.create(epic2);
        manager.create(task1);
        manager.create(task2);

        HttpTaskManager managerToVerify = manager.load();

        Assertions.assertEquals(manager.getTaskList(), managerToVerify.getTaskList()
                ,"Списки задач не совпадают!");
        Assertions.assertEquals(manager.getEpicList(), managerToVerify.getEpicList(),
                "Списки эпиков не совпадают!");
        Assertions.assertEquals(manager.getSubTaskList(), managerToVerify.getSubTaskList(),
                "Списки подзадач не совпадают!");
    }
}
