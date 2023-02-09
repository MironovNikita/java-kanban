import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import workWithTasks.InMemoryHistoryManager;

import java.util.ArrayList;
import java.util.List;

public class HistoryManagerTest extends InMemoryHistoryManager {
    InMemoryHistoryManager historyManager;

    @BeforeEach
    void createObjectsToTestHistory() {
        historyManager = new InMemoryHistoryManager();
    }

    //Методы с пустой историей задач
    @DisplayName("Добавление задач в пустую историю")
    @Test
    void shouldAddTasksToHistoryWhichIsEmpty() {
        Task task = new Task("Task", "Description of task",10);
        Epic epic = new Epic("Epic", "Description of epic",0);
        SubTask subTask = new SubTask("SubTask", "Description of subTask",10);
        ArrayList<Task> checkListOfTasks = new ArrayList<>();
        checkListOfTasks.add(subTask);
        checkListOfTasks.add(task);
        checkListOfTasks.add(epic);

        historyManager.addToHistory(subTask);
        historyManager.addToHistory(task);
        historyManager.addToHistory(epic);

        Assertions.assertEquals(historyManager.getHistory(), checkListOfTasks,
                "Списки историй задач не совпадают!");
    }

    @DisplayName("Получение пустой истории задач")
    @Test
    void shouldGetEmptyHistoryList() {
        ArrayList<Task> checkListOfTasks = new ArrayList<>();
        Assertions.assertEquals(historyManager.getHistory(), checkListOfTasks,
                "Списки историй задач не совпадают!");
    }

    @DisplayName("Удаление задачи из пустого списка")
    @Test
    void shouldDoNothingIfTaskIsNotInHistoryBeforeRemove() {
        Task task = new Task("Task", "Description of task",10);
        historyManager.remove(task.getId());
    }

    //Методы с дублированием
    @DisplayName("Добавление дублированных задач в пустую историю и получение этой истории")
    @Test
    void shouldLeaveOnlyOneTaskInHistoryIfItIsDuplicated() {
        Task task = new Task("Task", "Description of task",10);

        ArrayList<Task> checkListOfTasks = new ArrayList<>();
        checkListOfTasks.add(task);

        historyManager.addToHistory(task);
        historyManager.addToHistory(task);
        historyManager.addToHistory(task);
        Assertions.assertEquals(historyManager.getHistory(), checkListOfTasks,
                "Списки историй задач не совпадают!");
    }

    @DisplayName("Проверка удаления из истории при дублировании")
    @Test
    void shouldDeleteFromHistoryEvenIfOneTaskWasWrittenThereSeveralTimes() {
        Task task1 = new Task("Task", "Description of task",10);
        Task task2 = new Task("Task", "Description of task",10);
        ArrayList<Task> checkListOfTasks = new ArrayList<>();
        checkListOfTasks.add(task2);

        historyManager.addToHistory(task1);
        historyManager.addToHistory(task1);
        historyManager.addToHistory(task2);
        historyManager.addToHistory(task1);
        historyManager.remove(task1.getId());

        Assertions.assertEquals(historyManager.getHistory(), checkListOfTasks,
                "Задача не удалилась из истории!");
    }

    //Удаление из истории: начало, середина, конец
    @DisplayName("Удаление из истории просмотров задач: начало, середина, конец")
    @Test
    void shouldDeleteTasksFromHistoryFromAnyPlace() {
        Task task1 = new Task("Task1", "Description of task",14);
        Task task2 = new Task("Task2", "Description of task",1);
        Epic epic1 = new Epic("Epic1", "Description of epic",0);
        Epic epic2 = new Epic("Epic2", "Description of epic",0);
        SubTask subTask1 = new SubTask("SubTask1", "Description of subTask",5);
        SubTask subTask2 = new SubTask("SubTask2", "Description of subTask",5);

        historyManager.addToHistory(task2);
        historyManager.addToHistory(epic1);
        historyManager.addToHistory(subTask1);
        historyManager.addToHistory(task1);
        historyManager.addToHistory(epic2);
        historyManager.addToHistory(subTask2);

        List<Task> checkListOfTasks = new ArrayList<>();
        checkListOfTasks.add(epic1);
        checkListOfTasks.add(subTask1);
        checkListOfTasks.add(task1);
        checkListOfTasks.add(epic2);
        checkListOfTasks.add(subTask2);

        historyManager.remove(task2.getId()); //удаление из начала
        Assertions.assertEquals(checkListOfTasks, historyManager.getHistory(),
                "Списки историй при удалении из начала не совпадают!");

        historyManager.remove(subTask2.getId()); //удаление из середины
        checkListOfTasks.remove(subTask2);
        Assertions.assertEquals(checkListOfTasks, historyManager.getHistory(),
                "Списки историй при удалении из середины не совпадают!");

        historyManager.remove(task1.getId());
        checkListOfTasks.remove(task1);
        Assertions.assertEquals(checkListOfTasks, historyManager.getHistory(),
                "Списки историй при удалении с конца не совпадают!");
    }
}