import org.junit.jupiter.api.*;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import workWithTasks.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

abstract class TaskManagerTest <T extends TaskManager> {
    T manager;

    @BeforeEach
    void setTaskUniqIdToDefault() {
        Task.setUniqId(1);
    }

    @DisplayName("Удаление всех задач из менеджера")
    @Test
    void shouldDeleteAllTasksFromManager() {
        Task task = new Task("Task", "Description of task", 10);
        Epic epic = new Epic("Epic", "Description of epic", 10);
        SubTask subTask = new SubTask("SubTask", "Description of subtask", 0);
        manager.create(task);
        manager.create(epic);
        epic.createSubTask(subTask);
        manager.delAll();
        Assertions.assertTrue(manager.getTaskList().isEmpty(), "Список задач не пуст");
        Assertions.assertTrue(manager.getEpicList().isEmpty(), "Список эпиков не пуст");
        Assertions.assertTrue(manager.getSubTaskList().isEmpty(), "Список подзадач не пуст");
    }

    @DisplayName("Возвращение задачи по ID")
    @Test
    void shouldReturnRightTaskById() {
        Task task = new Task("Task", "Description of task", 10);
        Epic epic = new Epic("Epic", "Description of epic", 10);
        manager.create(task);
        Assertions.assertEquals(task, manager.getById(1), "Задачи не совпадают!");
        manager.create(epic);
        Assertions.assertEquals(epic, manager.getById(2), "Задачи не совпадают!");
    }

    @DisplayName("Проверка добавления TASK и EPIC в менеджер")
    @Test
    void shouldAddTaskOrEpicToManager() {
        Task task = new Task("Task", "Description of task", 10);
        Epic epic = new Epic("Epic", "Description of epic", 10);
        manager.create(task);
        manager.create(epic);

        Task createdTask = manager.getById(1);
        Epic createdEpic = (Epic) manager.getById(2);

        Assertions.assertNotNull(createdTask, "Задачи не существует!");
        Assertions.assertNotNull(createdEpic, "Задачи не существует!");
        Assertions.assertEquals(task.getInfo(), createdTask.getInfo(), "Задача не добавлена в менеджер!");
        Assertions.assertEquals(epic.getInfo(), createdEpic.getInfo(), "Задача не добавлена в менеджер!");
    }

    @DisplayName("Проверка обновления статуса задачи")
    @Test
    void shouldUpdateTaskStatus() {
        Task task = new Task("Task", "Description of task", 10);
        manager.create(task);
        manager.updateTask(task);
        String status1 = "IN_PROGRESS";
        Assertions.assertEquals(status1, task.getStatus(), "Статус задачи не IN_PROGRESS");
        manager.updateTask(task);
        String status2 = "DONE";
        Assertions.assertEquals(status2, task.getStatus(), "Статус задачи не DONE");
    }

    @DisplayName("Проверка обновления статуса подзадачи")
    @Test
    void shouldUpdateSubTaskStatus() {
        Epic epic = new Epic("Epic", "Description of epic", 0);
        SubTask subTask = new SubTask("SubTask", "Description of subTask", 5);
        epic.createSubTask(subTask);
        manager.create(epic);
        manager.updateTask(subTask);
        String status1 = "IN_PROGRESS";
        Assertions.assertEquals(status1, subTask.getStatus(), "Статус задачи не IN_PROGRESS");
        manager.updateTask(subTask);
        String status2 = "DONE";
        Assertions.assertEquals(status2, subTask.getStatus(), "Статус задачи не DONE");
    }

    @DisplayName("Удаление задачи по ID")
    @Test
    void shouldDeleteById() {
        Task task1 = new Task("Task1", "Description of task", 5);
        Task task2 = new Task("Task2", "Description of task", 5);
        Epic epic = new Epic("Epic", "Description of epic", 0);
        manager.create(task1);
        manager.create(task2);
        manager.create(epic);
        manager.delById(2);
        Assertions.assertNull(manager.getById(2), "Задача не удалилась или удалилась не та задача!");
    }

    @DisplayName("Получение списка подзадач EPIC'а")
    @Test
    void shouldGetEpicSubTaskList() {
        Epic epic = new Epic("Epic", "Description of epic", 5);
        SubTask subTask1 = new SubTask("SubTask1", "Description of subTask", 0);
        SubTask subTask2 = new SubTask("SubTask2", "Description of subTask", 5);

        HashMap<Integer, SubTask> testSubTaskList = new HashMap<>();
        testSubTaskList.put(subTask1.getId(), subTask1);
        testSubTaskList.put(subTask2.getId(), subTask2);

        epic.createSubTask(subTask1);
        epic.createSubTask(subTask2);
        manager.create(epic);
        Assertions.assertEquals(testSubTaskList, manager.getSubTaskList(epic), "Списки не совпадают!");
    }

    @DisplayName("Получение истории запросов менеджера")
    @Test
    void shouldGetHistoryFromManager() {
        Task task = new Task("Task", "Description of task", 5);
        Epic epic = new Epic("Epic", "Description of epic", 5);
        manager.create(task);
        manager.create(epic);
        List<Task> expectedHistoryList = new ArrayList<>();
        expectedHistoryList.add(task);
        expectedHistoryList.add(epic);

        manager.getById(1);
        manager.getById(2);

        List<Task> actualHistoryList = new ArrayList<>(manager.getHistory().getHistory());
        Assertions.assertEquals(expectedHistoryList, actualHistoryList, "Истории запросов не совпадают!");
    }

    //Тесты с пустым списком задач
    @DisplayName("Проверка пустого списка задач")
    @Test
    void shouldVerifyIfTaskManagerListIsEmpty() {
        Assertions.assertTrue(manager.getTaskList().isEmpty(), "Список задач не пуст!");
        Assertions.assertTrue(manager.getEpicList().isEmpty(), "Список эпиков не пуст!");
        Assertions.assertTrue(manager.getSubTaskList().isEmpty(), "Список подзадач не пуст!");
    }

    @DisplayName("Проверка удаления всех задач из менеджера")
    @Test
    void shouldDeleteAllTasksFromTaskManager() {
        Task task = new Task("Task", "Description of task", 5);
        Epic epic = new Epic("Epic", "Description of epic", 5);
        manager.create(task);
        manager.create(epic);
        Assertions.assertFalse(manager.getEpicList().isEmpty(), "Список эпиков пуст!");
        Assertions.assertFalse(manager.getTaskList().isEmpty(), "Список задач пуст!");
        manager.delAll();
        Assertions.assertTrue(manager.getTaskList().isEmpty(), "Список задач не пуст!");
        Assertions.assertTrue(manager.getEpicList().isEmpty(), "Список эпиков не пуст!");
    }

    @DisplayName("Проверка получения задачи из пустого менеджера")
    @Test
    void shouldVerifyNullIfGetByIdIsFromEmptyList() {
        Assertions.assertNull(manager.getById(1), "Список задач не пуст!");
    }

    @DisplayName("Отсутствие обновления статуса задачи, потому что она не в менеджере")
    @Test
    void shouldNotUpdateTaskBecauseItIsNotInManager() {
        Task task = new Task("Task", "Description of task", 5);
        String taskBeforeUpdate = task.getInfo();
        manager.updateTask(task);
        String taskAfterUpdate = task.getInfo();
        Assertions.assertEquals(taskBeforeUpdate, taskAfterUpdate, "Задача находится в менеджере!");
    }

    @DisplayName("Отсутствие обновления статуса подзадачи, потому что она не в менеджере")
    @Test
    void shouldNotUpdateSubTaskBecauseItIsNotInManager() {
        SubTask subTask = new SubTask("SubTask", "Description of subTask", 5);
        String taskBeforeUpdate = subTask.getInfo();
        manager.updateTask(subTask);
        String taskAfterUpdate = subTask.getInfo();
        Assertions.assertEquals(taskBeforeUpdate, taskAfterUpdate, "Подзадача находится в менеджере!");
    }

    @DisplayName("Проверка удаления задачи вне менеджера")
    @Test
    void shouldGetNullFromEmptyTaskList() {
        Task task = new Task("Task", "Description of task", 16);
        manager.delById(task.getId());
        Assertions.assertNull(manager.getById(1), "Задача не удалилась!");
    }

    @DisplayName("Проверка получения списка подзадач EPIC'а, который не добавлен в менеджер")
    @Test
    void shouldGetNullIfEpicIsNotInManager() {
        Epic epic = new Epic("Epic", "Description of epic", 16);
        SubTask subTask = new SubTask("SubTask", "Description of subTask", 16);
        epic.createSubTask(subTask);
        Assertions.assertNull(manager.getSubTaskList(epic), "EPIC находится в менеджере!");
    }

    //Тесты с неверным идентификатором задачи
    @DisplayName("Попытка получить задачу по несуществующему ID")
    @Test
    void shouldVerifyNullIfIdDoesNotExist() {
        Assertions.assertNull(manager.getById(1000), "Получена задача с несуществующим ID");
    }

    @DisplayName("Попытка удалить задачу по несуществующему ID")
    @Test
    void shouldWriteInConsoleThatTaskWithSuchIdDoesNotExist() {
        manager.delById(1000);
    }
    @DisplayName("Расчёт времени конца задач")
    @Test
    void shouldCountRightTimeOfAnyTaskType() {
        Task task = new Task("Task", "Description of task",14,
                LocalDateTime.of(2023,1,1,6,0));
        String taskEndTime = "2023-01-01T06:14";
        Assertions.assertEquals(taskEndTime, task.getEndTime().toString());

        Epic epic = new Epic("Epic", "Description of epic",0);
        Assertions.assertNull(epic.getStartTime());
        Assertions.assertNull(epic.getEndTime());

        SubTask subTask1 = new SubTask("SubTask1", "Description of subTask",15,
                LocalDateTime.of(2023, 1,2,10,30));
        String subTask1EndTime = "2023-01-02T10:45";
        Assertions.assertEquals(subTask1EndTime, subTask1.getEndTime().toString());

        SubTask subTask2 = new SubTask("SubTask2", "Description of subTask",15,
                LocalDateTime.of(2023, 1,3,20,30));
        String subTask2EndTime = "2023-01-03T20:45";
        Assertions.assertEquals(subTask2EndTime, subTask2.getEndTime().toString());

        epic.createSubTask(subTask1);
        epic.createSubTask(subTask2);
        Assertions.assertEquals(epic.getStartTime(), subTask1.getStartTime());
        Assertions.assertEquals(epic.getEndTime(), subTask2.getEndTime());
    }

    @DisplayName("Расчёт продолжительности EPIC'а")
    @Test
    void shouldCountEpicDurationRight() {
        Epic epic = new Epic("Epic", "Description of epic",500);
        SubTask subTask1 = new SubTask("SubTask1", "Description of subTask",30,
                LocalDateTime.of(2023, 1,2,10,30));
        SubTask subTask2 = new SubTask("SubTask2", "Description of subTask",30,
                LocalDateTime.of(2023, 1,3,20,30));

        epic.createSubTask(subTask1);
        epic.createSubTask(subTask2);
        int durationCheck = 60;
        Assertions.assertEquals(durationCheck, epic.getDuration());
    }

    @DisplayName("Проверка начала и конца EPIC'а по времени, если все подзадачи не имеют времени начала")
    @Test
    void shouldGiveNothingInEpicStartTimeIfAllSubTasksDoNotHaveStartTime() {
        Epic epic = new Epic("Epic", "Description of epic",500);
        SubTask subTask1 = new SubTask("SubTask1", "Description of subTask",30);
        SubTask subTask2 = new SubTask("SubTask2", "Description of subTask",45);
        epic.createSubTask(subTask1);
        epic.createSubTask(subTask2);

        int durationCheck = 75;
        Assertions.assertEquals(durationCheck, epic.getDuration());

        Assertions.assertNull(epic.getStartTime());
        Assertions.assertNull(epic.getEndTime());
    }

    @DisplayName("Проверка возвращения задач в порядке приоритета")
    @Test
    void shouldCreatePrioritizedTaskList() {
        Task task1 = new Task("Task1", "Description of task",140,
                LocalDateTime.of(2023,1,5,10,0));
        Task task2 = new Task("Task2", "Description of task",180,
                LocalDateTime.of(2023,1,10,10,0));
        Epic epic = new Epic("Epic", "Description of epic",0);
        SubTask subTask1 = new SubTask("SubTask1", "Description of subTask",150,
                LocalDateTime.of(2023, 1,3,9,30));
        SubTask subTask2 = new SubTask("SubTask2", "Description of subTask",80,
                LocalDateTime.of(2023, 1,2,10,30));
        epic.createSubTask(subTask1);
        epic.createSubTask(subTask2);

        manager.create(task1);
        manager.create(task2);
        manager.create(epic);

        StringBuilder stringCheck = new StringBuilder();
        stringCheck.append("[").append(subTask2.getInfo()).append("\n, ").append(subTask1.getInfo())
                        .append("\n, ").append(task1.getInfo()).append("\n, ").append(task2.getInfo()).append("\n]");

        Assertions.assertEquals(stringCheck.toString(), manager.getPrioritizedTasks().toString());
    }
}