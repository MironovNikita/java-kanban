import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;
import workWithTasks.FileBackedTasksManager;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @BeforeEach
    void createManagerAndAddTasks() {
        manager = new FileBackedTasksManager();
    }

    @DisplayName("Возвращение списка TASK'ов менеджера")
    @Test
    void shouldReturnRightManagerTaskList() {
        Task task1 = new Task("Task1", "Description of task",15);
        Task task2 = new Task("Task2", "Description of task",15);
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
        Epic epic1 = new Epic("Epic1", "Description of epic",15);
        Epic epic2 = new Epic("Epic2", "Description of epic",15);
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
        SubTask subTask1 = new SubTask("SubTask1", "Description of subTask",7);
        SubTask subTask2 = new SubTask("SubTask2", "Description of subTask",8);
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

    @DisplayName("Перевод всех типов задач в строку")
    @Test
    void shouldMakeAnyTaskAsString() {
        Task task = new Task("Task", "Description of task",9);
        String expectedTaskString = "1,TASK,Task,NEW,Description of task,,9,,\n";
        manager.create(task);
        String actualTaskString = manager.toString(task);
        Assertions.assertEquals(expectedTaskString, actualTaskString, "Задача неверно записывается в строку!");

        Epic epic = new Epic("Epic", "Description of epic",0);
        SubTask subTask = new SubTask("SubTask", "Description of subTask",15);
        String expectedSubTaskString = "3,SUBTASK,SubTask,NEW,Description of subTask,,15,,2\n";
        epic.createSubTask(subTask);
        manager.create(epic);
        String actualSubTaskString = manager.toString(subTask);
        Assertions.assertEquals(expectedSubTaskString, actualSubTaskString,
                "Подзадача неверно записывается в строку!");

        String expectedEpicString = "2,EPIC,Epic,NEW,Description of epic,,15,,\n";
        String actualEpicString = manager.toString(epic);
        Assertions.assertEquals(expectedEpicString, actualEpicString, "EPIC неверно записывается в строку!");
    }

    @DisplayName("Создание задач из строки")
    @Test
    void shouldMakeAnyTaskFromString() {
        /*Сравнивается информация по задачам, так как если сравнивать непосредственно сами объекты задач,
         то, несмотря на то, что задачи одни и те же, объекты по факту будут разные */
        Task task = new Task("Task", "Description of task",5);
        Epic epic = new Epic("Epic", "Description of epic",0);
        SubTask subTask = new SubTask("SubTask", "Description of subTask",12);
        String taskString = "1,TASK,Task,NEW,Description of task,,5,,\n";
        String actualTaskInfo = manager.fromString(taskString).getInfo();
        Assertions.assertEquals(task.getInfo(), actualTaskInfo, "Задача неверно создаётся из строки");

        String epicString = "2,EPIC,Epic,NEW,Description of epic,,0,,\n";
        String actualEpicInfo = manager.fromString(epicString).getInfo();
        Assertions.assertEquals(epic.getInfo(), actualEpicInfo, "EPIC неверно создаётся из строки");

        String subTaskString = "3,SUBTASK,SubTask,NEW,Description of subTask,,12,,2\n";
        String actualSubTaskInfo = manager.fromString(subTaskString).getInfo();
        Assertions.assertEquals(subTask.getInfo(), actualSubTaskInfo, "Подзадача неверно создаётся из строки!");
    }

    @DisplayName("Создание строки из истории запросов")
    @Test
    void shouldMakeHistoryAsString() {
        Task task1 = new Task("Task 1", "Description of task",7);
        Task task2 = new Task("Task 2", "Description of task",4);
        Epic epic = new Epic("Epic", "Description of epic",0);
        SubTask subTask = new SubTask("SubTask", "Description of subTask",8);
        epic.createSubTask(subTask);
        manager.create(task1);
        manager.create(task2);
        manager.create(epic);

        StringBuilder idToCheck = new StringBuilder();
        idToCheck.append(manager.getById(1).getId()).append(",").append(manager.getById(3).getId()).append(",")
                .append(manager.getById(4).getId()).append(",").append(manager.getById(2).getId());
        String historyAsString = FileBackedTasksManager.historyToString(manager.getHistory());

        Assertions.assertEquals(historyAsString, idToCheck.toString(), "Истории запросов не совпадает!");
    }

    @DisplayName("Создание списка ID задач из строки")
    @Test
    void shouldReturnListOfIntegersFromString() {
        String fromString = "7,4,2";
        List<Integer> listOfIds = List.of(7,4,2);
        Assertions.assertEquals(listOfIds, FileBackedTasksManager.historyFromString(fromString),
                "Строка не совпадает со списком ID!");
    }

    //Тесты EPIC:
    @DisplayName("Обновление статуса эпика без подзадач")
    @Test
    void shouldUpdateEpicStatusWithEmptySubtaskList() {
        Epic epic = new Epic("Epic", "Description of epic",0);
        epic.updateStatus();
        Assertions.assertEquals(TaskStatus.IN_PROGRESS.toString(), epic.getStatus(),
                "Статус эпика не IN_PROGRESS");
        epic.updateStatus();
        Assertions.assertEquals(TaskStatus.DONE.toString(), epic.getStatus(), "Статус эпика не DONE");
    }

    @DisplayName("Проверка статуса эпика NEW с подзадачами NEW")
    @Test
    void shouldReturnNewEpicStatusWithAllSubtasksNewStatus() {
        Epic epic = new Epic("Epic", "Description of epic",0);
        SubTask subTask1 = new SubTask("SubTask1", "Description of subTask",6);
        SubTask subTask2 = new SubTask("SubTask2", "Description of subTask",9);
        epic.createSubTask(subTask1);
        epic.createSubTask(subTask2);
        manager.create(epic);
        Assertions.assertEquals(TaskStatus.NEW.toString(), epic.getStatus(), "Статус эпика не NEW");
    }

    @DisplayName("Проверка статуса эпика DONE с подзадачами DONE")
    @Test
    void shouldReturnDoneEpicStatusWithAllSubtasksDoneStatus() {
        Epic epic = new Epic("Epic", "Description of epic",0);
        SubTask subTask1 = new SubTask("SubTask1", "Description of subTask",3);
        SubTask subTask2 = new SubTask("SubTask2", "Description of subTask",3);
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
        SubTask subTask1 = new SubTask("SubTask1", "Description of subTask",2);
        SubTask subTask2 = new SubTask("SubTask2", "Description of subTask",1);
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
        Epic epic = new Epic("Epic", "Description of epic",6);
        SubTask subTask1 = new SubTask("SubTask1", "Description of subTask",5);
        SubTask subTask2 = new SubTask("SubTask2", "Description of subTask",9);
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
        HashMap<Integer, Task> expectedEpicHashMap = new HashMap<>();
        Assertions.assertEquals(expectedEpicHashMap, manager.getEpicList());
    }

    @DisplayName("Проверка добавления подзадачи в EPIC даже если менеджер без подзадач")
    @Test
    void shouldAddSubTaskToEpicEvenIfManagerIsEmpty() {
        Epic epic = new Epic("Epic", "Description of epic",0);
        SubTask subTask = new SubTask("SubTask", "Description of subTask",5);

        int epicSubtaskListSizeBeforeAdding = epic.getSubTaskList().size();
        epic.createSubTask(subTask);
        int epicSubtaskListSizeAfterAdding = epic.getSubTaskList().size();
        Assertions.assertTrue(manager.getEpicList().isEmpty(), "Список эпиков не пуст!");
        Assertions.assertTrue(manager.getSubTaskList().isEmpty(), "Список подзадач не пуст!");
        Assertions.assertEquals(epicSubtaskListSizeBeforeAdding + 1, epicSubtaskListSizeAfterAdding,
                "Подзадача не добавляется в EPIC!");
    }

    //Тесты сохранения и восстановления состояния менеджера из файла
    @DisplayName("Проверка сохранения состояния при пустом списке задач")
    @Test
    void verifySaveAndLoadFunctionsWithEmptyManager() {
        manager.save();
        File file = new File("history.csv");
        FileBackedTasksManager managerToVerify = FileBackedTasksManager.loadFromFile(file);

        Assertions.assertEquals(manager.getTaskList(), managerToVerify.getTaskList()
                ,"Списки задач не совпадают!");
        Assertions.assertEquals(manager.getEpicList(), managerToVerify.getEpicList(),
                "Списки эпиков не совпадают!");
        Assertions.assertEquals(manager.getSubTaskList(), managerToVerify.getSubTaskList(),
                "Списки подзадач не совпадают!");
    }

    @DisplayName("Проверка сохранения и восстановления состояния при EPIC'е без подзадач")
    @Test
    void verifySaveAndLoadFunctionsWithEpicWithoutSubtasks() {
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

        File file = new File("history.csv");
        FileBackedTasksManager managerToVerify = FileBackedTasksManager.loadFromFile(file);

        Assertions.assertEquals(manager.getTaskList(), managerToVerify.getTaskList()
                ,"Списки задач не совпадают!");
        Assertions.assertEquals(manager.getEpicList(), managerToVerify.getEpicList(),
                "Списки эпиков не совпадают!");
        Assertions.assertEquals(manager.getSubTaskList(), managerToVerify.getSubTaskList(),
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

        File file = new File("history.csv");
        FileBackedTasksManager managerToVerify = FileBackedTasksManager.loadFromFile(file);

        Assertions.assertEquals(manager.getTaskList(), managerToVerify.getTaskList()
                ,"Списки задач не совпадают!");
        Assertions.assertEquals(manager.getEpicList(), managerToVerify.getEpicList(),
                "Списки эпиков не совпадают!");
        Assertions.assertEquals(manager.getSubTaskList(), managerToVerify.getSubTaskList(),
                "Списки подзадач не совпадают!");
    }
}