import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;
import workWithTasks.InMemoryTaskManager;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    void createManagerAndAddTasks() {
        manager = new InMemoryTaskManager();
    }

    @DisplayName("Возвращение списка TASK'ов менеджера")
    @Test
    void shouldReturnRightManagerTaskList() {
        Task task1 = new Task("Task1", "Description of task",5);
        Task task2 = new Task("Task2", "Description of task",5);
        manager.create(task1);
        manager.create(task2);

        HashMap<Integer, Task> expectedTaskHashMap = new HashMap<>();
        expectedTaskHashMap.put(task1.getId(), task1);
        expectedTaskHashMap.put(task2.getId(), task2);
        Assertions.assertEquals(expectedTaskHashMap, manager.getTaskList(), "Списки задач не совпадают!");
    }

    @DisplayName("Возвращение списка EPIC'ов менеджера")
    @Test
    void shouldReturnRightManagerEpicList() {
        Epic epic1 = new Epic("Epic1", "Description of epic",5);
        Epic epic2 = new Epic("Epic2", "Description of epic",5);
        manager.create(epic1);
        manager.create(epic2);

        HashMap<Integer, Epic> expectedEpicHashMap = new HashMap<>();
        expectedEpicHashMap.put(epic1.getId(), epic1);
        expectedEpicHashMap.put(epic2.getId(), epic2);
        Assertions.assertEquals(expectedEpicHashMap, manager.getEpicList(), "Списки EPIC'ов не совпадают!");
    }

    @DisplayName("Проверка, что все SubTask'и принадлежат EPIC'ам")
    @Test
    void shouldBeTrueIfSubTaskBelongsToEpic() {
        Epic epic1 = new Epic("Epic1", "Description of epic",0);
        Epic epic2 = new Epic("Epic2", "Description of epic",0);
        SubTask subTask1 = new SubTask("SubTask1", "Description of subTask",25);
        SubTask subTask2 = new SubTask("SubTask2", "Description of subTask",5);
        SubTask subTask3 = new SubTask("SubTask3", "Description of subTask",10);
        epic1.createSubTask(subTask1);
        epic2.createSubTask(subTask2);
        epic2.createSubTask(subTask3);
        manager.create(epic1);
        manager.create(epic2);

        boolean everySubIsInEpic = false;
        List<SubTask> subTasks = List.of(subTask1, subTask2, subTask3);

        for(Epic epic : manager.getEpicList().values()) {
            for(SubTask subTask : epic.getAllSubTask().values()) {
                for (SubTask task : subTasks) {
                    if (Objects.equals(subTask.getId(), task.getId())) {
                        everySubIsInEpic = true;
                        break;
                    } else {
                        everySubIsInEpic = false;
                    }
                }
            }
        }
        Assertions.assertTrue(everySubIsInEpic);
    }

    //Тесты EPIC:
    @DisplayName("Обновление статуса эпика без подзадач")
    @Test
    void shouldUpdateEpicStatusWithEmptySubtaskList() {
        Epic epic = new Epic("Epic", "Description of epic",0);
        epic.updateStatus();
        Assertions.assertEquals(TaskStatus.IN_PROGRESS.toString(), epic.getStatus(), "Статус эпика не IN_PROGRESS");
        epic.updateStatus();
        Assertions.assertEquals(TaskStatus.DONE.toString(), epic.getStatus(), "Статус эпика не DONE");
    }

    @DisplayName("Проверка статуса эпика NEW с подзадачами NEW")
    @Test
    void shouldReturnNewEpicStatusWithAllSubtasksNewStatus() {
        Epic epic = new Epic("Epic", "Description of epic",0);
        SubTask subTask1 = new SubTask("SubTask1", "Description of subTask",15);
        SubTask subTask2 = new SubTask("SubTask2", "Description of subTask",17);
        epic.createSubTask(subTask1);
        epic.createSubTask(subTask2);
        manager.create(epic);
        Assertions.assertEquals(TaskStatus.NEW.toString(), epic.getStatus(), "Статус эпика не NEW");
    }

    @DisplayName("Проверка статуса эпика DONE с подзадачами DONE")
    @Test
    void shouldReturnDoneEpicStatusWithAllSubtasksDoneStatus() {
        Epic epic = new Epic("Epic", "Description of epic",0);
        SubTask subTask1 = new SubTask("SubTask1", "Description of subTask",15);
        SubTask subTask2 = new SubTask("SubTask2", "Description of subTask",36);
        epic.createSubTask(subTask1);
        epic.createSubTask(subTask2);
        manager.create(epic);
        manager.updateTask(subTask1);
        manager.updateTask(subTask1);
        manager.updateTask(subTask2);
        manager.updateTask(subTask2);

        Assertions.assertEquals(TaskStatus.DONE.toString(), epic.getStatus(), "Статус эпика не DONE");
    }

    @DisplayName("Проверка статуса эпика IN_PROGRESS с подзадачами NEW и DONE")
    @Test
    void shouldReturnIn_ProgressEpicStatusWithSubtasksNewAndDoneStatuses() {
        Epic epic = new Epic("Epic", "Description of epic",0);
        SubTask subTask1 = new SubTask("SubTask1", "Description of subTask",16);
        SubTask subTask2 = new SubTask("SubTask2", "Description of subTask",22);
        epic.createSubTask(subTask1);
        epic.createSubTask(subTask2);
        manager.create(epic);

        manager.updateTask(subTask1);
        manager.updateTask(subTask1);

        Assertions.assertEquals(TaskStatus.IN_PROGRESS.toString(), epic.getStatus(),
                "Статус эпика не IN_PROGRESS");
    }

    @DisplayName("Проверка статуса эпика IN_PROGRESS с подзадачами IN_PROGRESS")
    @Test
    void shouldReturnIn_ProgressEpicStatusWithSubtasksIn_ProgressStatus() {
        Epic epic = new Epic("Epic", "Description of epic",0);
        SubTask subTask1 = new SubTask("SubTask1", "Description of subTask",15);
        SubTask subTask2 = new SubTask("SubTask2", "Description of subTask",12);
        epic.createSubTask(subTask1);
        epic.createSubTask(subTask2);
        manager.create(epic);

        manager.updateTask(subTask1);
        manager.updateTask(subTask2);

        Assertions.assertEquals(TaskStatus.IN_PROGRESS.toString(), epic.getStatus(),
                "Статус эпика не IN_PROGRESS");
    }

    @DisplayName("Проверка пустого списка задач менеджера")
    @Test
    void taskListShouldBeEmpty() {
        HashMap<Integer, Task> expectedTaskHashMap = new HashMap<>();
        Assertions.assertEquals(expectedTaskHashMap, manager.getTaskList());
    }

    @DisplayName("Проверка пустого списка EPIC'ов менеджера")
    @Test
    void epicListShouldBeEmpty() {
        manager.delAll();
        HashMap<Integer, Task> expectedEpicHashMap = new HashMap<>();
        Assertions.assertEquals(expectedEpicHashMap, manager.getEpicList());
    }

    @DisplayName("Проверка добавления подзадачи в EPIC даже если менеджер без подзадач")
    @Test
    void shouldAddSubTaskToEpicEvenIfManagerIsEmpty() {
        Epic epic = new Epic("Epic", "Description of epic",0);
        SubTask subTask = new SubTask("SubTask", "Description of subTask",15);

        int epicSubtaskListSizeBeforeAdding = epic.getSubTaskList().size();
        epic.createSubTask(subTask);
        int epicSubtaskListSizeAfterAdding = epic.getSubTaskList().size();
        Assertions.assertEquals("", manager.getAll(), "Список задач не пуст!");
        Assertions.assertEquals(epicSubtaskListSizeBeforeAdding + 1, epicSubtaskListSizeAfterAdding,
                "Подзадача не добавляется в EPIC!");
    }

    @DisplayName("Проверка добавления задач без поля startTime")
    @Test
    void shouldAddAnyTypeOfTaskToManagerWithoutStartTimeField() {
        Task task = new Task("Task", "Description of task",14);
        Epic epic = new Epic("Epic", "Description of epic",0);
        SubTask subTask = new SubTask("SubTask", "Description of subTask",15);
        epic.createSubTask(subTask);
        manager.create(task);
        manager.create(epic);

        HashMap<Integer, Task> taskListToCheck = new HashMap<>();
        HashMap<Integer, Epic> epicListToCheck = new HashMap<>();
        taskListToCheck.put(task.getId(), task);
        epicListToCheck.put(epic.getId(), epic);

        Assertions.assertEquals(manager.getTaskList(), taskListToCheck);
        Assertions.assertEquals(manager.getEpicList(), epicListToCheck);
    }

    @DisplayName("Проверка добавления задач с полем startTime")
    @Test
    void shouldAddAnyTypeOfTaskToManagerWithStartTimeField() {
        Task task = new Task("Task", "Description of task",14,
                LocalDateTime.of(2023,1,1,6,0));
        Epic epic = new Epic("Epic", "Description of epic",0);
        SubTask subTask = new SubTask("SubTask", "Description of subTask",15,
                LocalDateTime.of(2023, 1,2,10,30));

        epic.createSubTask(subTask);
        manager.create(task);
        manager.create(epic);

        HashMap<Integer, Task> taskListToCheck = new HashMap<>();
        HashMap<Integer, Epic> epicListToCheck = new HashMap<>();
        taskListToCheck.put(task.getId(), task);
        epicListToCheck.put(epic.getId(), epic);

        Assertions.assertEquals(manager.getTaskList(), taskListToCheck);
        Assertions.assertEquals(manager.getEpicList(), epicListToCheck);
    }

    @DisplayName("Проверка добавления в менеджер задач с пересекающимся временем выполнения")
    @Test
    void shouldNotAddAnyTaskIfItsTimeCrossesWithSomeTasksWhichAreAlreadyInManager() {
        Task task = new Task("Task", "Description of task",120,
                LocalDateTime.of(2023,1,1,10,0));
        Epic epic = new Epic("Epic", "Description of epic",0);
        SubTask subTask = new SubTask("SubTask", "Description of subTask",150,
                LocalDateTime.of(2023, 1,1,11,30));
        epic.createSubTask(subTask);
        manager.create(task);
        manager.create(epic);
        System.out.println(manager.getEpicList());
        Assertions.assertEquals(1, manager.getTaskList().size() + manager.getEpicList().size());
    }

    @DisplayName("Проверка добавления в менеджер задач с одинаковым временем начала")
    @Test
    void shouldNotAddAnyTaskWithEqualStartTimeWithSomeTaskWhichIsAlreadyInManager() {
        Task task1 = new Task("Task1", "Description of task",120,
                LocalDateTime.of(2023,1,1,10,0));
        Task task2 = new Task("Task2", "Description of task",240,
                LocalDateTime.of(2023,1,1,10,0));
        manager.create(task2);
        manager.create(task1);
        Assertions.assertEquals(1, manager.getTaskList().size());
    }

    @DisplayName("Проверка добавления в EPIC подзадачи с пересекающимся временем с другими задачами")
    @Test
    void shouldNotAddSubtaskIfItsStartTimeCrossesWithSomeTaskWhichIsAlreadyAdded() {
        Epic epic = new Epic("Epic", "Description of epic",0);
        SubTask subTask1 = new SubTask("SubTask1", "Description of subTask",120,
                LocalDateTime.of(2023, 1,1,11,30));
        SubTask subTask2 = new SubTask("SubTask2", "Description of subTask",523,
                LocalDateTime.of(2023, 1,1,12,30));
        SubTask subTask3 = new SubTask("SubTask3", "Description of subTask",150,
                LocalDateTime.of(2023, 1,2,15,30));
        SubTask subTask4 = new SubTask("SubTask3", "Description of subTask",150);
        epic.createSubTask(subTask1);
        epic.createSubTask(subTask2);
        epic.createSubTask(subTask3);
        epic.createSubTask(subTask4);
        manager.create(epic);
        System.out.println(epic.getAllSubTask());
        Assertions.assertEquals(3, epic.getAllSubTask().size());
    }

    @DisplayName("Проверка добавления задачи, сроки которой находятся \"внутри\" сроков другой задачи")
    @Test
    void shouldNotAddTaskIfItsDurationIsInsideAnotherTaskDuration() {
        Task task1 = new Task("Task1", "Description of task",5000,
                LocalDateTime.of(2023,1,1,10,0));
        Task task2 = new Task("Task2", "Description of task",240,
                LocalDateTime.of(2023,1,3,10,0));
        manager.create(task1);
        manager.create(task2);
        Assertions.assertEquals(1, manager.getTaskList().size());
    }
}