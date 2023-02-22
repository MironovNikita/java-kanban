import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import workWithTasks.Managers;
import workWithTasks.TaskManager;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        TaskManager inMemoryTaskManager = Managers.getDefault();
        Task task1 = new Task("Почистить зубы", "Тщательно",5);
        Task task2 = new Task("Побриться", "Основательно",10,
                LocalDateTime.of(2023,1,1,9,0));
        Epic epic1 = new Epic("Переезд", "В новый дом",0);
        SubTask sub1 = new SubTask("Собрать все коробки", "Да, да... Все коробочки!",60,
                LocalDateTime.of(2023,1,1,10,0));
        SubTask sub2 = new SubTask("Упаковать кошку", "Прощаемся с кошкой!",10,
                LocalDateTime.of(2023,1,1,11,0));
        SubTask sub3 = new SubTask("Сделать домашнее задание", "До воскресенья",1000);
        epic1.createSubTask(sub1);
        epic1.createSubTask(sub2);
        epic1.createSubTask(sub3);
        Epic epic2 = new Epic("Учёба", "Изучаем JAVA",0);
        SubTask sub4 = new SubTask("Порадоваться выполненному заданию", "Но ждать замечаний :)",
                1000, LocalDateTime.of(2023, 1,10,10,0));
        epic2.createSubTask(sub4);

        inMemoryTaskManager.create(epic1);
        inMemoryTaskManager.create(epic2);
        inMemoryTaskManager.create(task1);
        inMemoryTaskManager.create(task2);
        System.out.println("Список задач:");
        System.out.println(inMemoryTaskManager.getTaskList());
        System.out.println("Список подзадач:");
        System.out.println(inMemoryTaskManager.getSubTaskList());
        System.out.println("Список эпиков:");
        System.out.println(inMemoryTaskManager.getEpicList());

        System.out.println("Получение списка всех подзадач определённого эпика: " + 1);
        System.out.println(inMemoryTaskManager.getSubTaskList(epic1));
        System.out.println();

        System.out.println("Обновление задачи TASK");
        task1.setDescription("Помыть щётку");
        inMemoryTaskManager.updateTask(task1);
        System.out.println("Список задач:");
        System.out.println(inMemoryTaskManager.getTaskList());
        System.out.println("Список подзадач:");
        System.out.println(inMemoryTaskManager.getSubTaskList());
        System.out.println("Список эпиков:");
        System.out.println(inMemoryTaskManager.getEpicList());
        task1.setDescription("Убрать щётку");
        task2.setDescription("Помыть бритву");
        inMemoryTaskManager.updateTask(task1);
        inMemoryTaskManager.updateTask(task2);
        System.out.println("Список задач:");
        System.out.println(inMemoryTaskManager.getTaskList());
        System.out.println("Список подзадач:");
        System.out.println(inMemoryTaskManager.getSubTaskList());
        System.out.println("Список эпиков:");
        System.out.println(inMemoryTaskManager.getEpicList());
        inMemoryTaskManager.updateTask(task2);
        System.out.println("Список задач:");
        System.out.println(inMemoryTaskManager.getTaskList());
        System.out.println("Список подзадач:");
        System.out.println(inMemoryTaskManager.getSubTaskList());
        System.out.println("Список эпиков:");
        System.out.println(inMemoryTaskManager.getEpicList());
        System.out.println();

        System.out.println("Обновление EPIC'а");
        sub1.setDescription("Загрузить в машину");
        inMemoryTaskManager.updateTask(sub1);
        System.out.println("Список задач:");
        System.out.println(inMemoryTaskManager.getTaskList());
        System.out.println("Список подзадач:");
        System.out.println(inMemoryTaskManager.getSubTaskList());
        System.out.println("Список эпиков:");
        System.out.println(inMemoryTaskManager.getEpicList());
        System.out.println("-----------------------------------------------------------");
        sub2.setDescription("Кошка в клетке");
        inMemoryTaskManager.updateTask(sub2);
        System.out.println("Список задач:");
        System.out.println(inMemoryTaskManager.getTaskList());
        System.out.println("Список подзадач:");
        System.out.println(inMemoryTaskManager.getSubTaskList());
        System.out.println("Список эпиков:");
        System.out.println(inMemoryTaskManager.getEpicList());
        System.out.println("-----------------------------------------------------------");
        sub1.setDescription("Найти водителя");
        inMemoryTaskManager.updateTask(sub1);
        System.out.println("Список задач:");
        System.out.println(inMemoryTaskManager.getTaskList());
        System.out.println("Список подзадач:");
        System.out.println(inMemoryTaskManager.getSubTaskList());
        System.out.println("Список эпиков:");
        System.out.println(inMemoryTaskManager.getEpicList());
        System.out.println("-----------------------------------------------------------");
        sub2.setDescription("Кошка села за руль и уехала");
        inMemoryTaskManager.updateTask(sub2);
        System.out.println("Список задач:");
        System.out.println(inMemoryTaskManager.getTaskList());
        System.out.println("Список подзадач:");
        System.out.println(inMemoryTaskManager.getSubTaskList());
        System.out.println("Список эпиков:");
        System.out.println(inMemoryTaskManager.getEpicList());
        System.out.println();

        System.out.println("Получение задачи по id: ");
        System.out.println(inMemoryTaskManager.getById(4));
        System.out.println(inMemoryTaskManager.getById(7));
        System.out.println(inMemoryTaskManager.getById(1));
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println(inMemoryTaskManager.getById(2));
        System.out.println(inMemoryTaskManager.getById(5));
        System.out.println(inMemoryTaskManager.getById(6));
        System.out.println(inMemoryTaskManager.getById(3));
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println(inMemoryTaskManager.getById(1));
        System.out.println(inMemoryTaskManager.getById(5));
        System.out.println(inMemoryTaskManager.getById(3));
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println();

        System.out.println("Получение задач в порядке приоритета:");
        System.out.println(inMemoryTaskManager.getPrioritizedTasks());

        System.out.println("Список задач:");
        System.out.println(inMemoryTaskManager.getTaskList());
        System.out.println("Список подзадач:");
        System.out.println(inMemoryTaskManager.getSubTaskList());
        System.out.println("Список эпиков:");
        System.out.println(inMemoryTaskManager.getEpicList());
        System.out.println("История просмотров до удаления: ");
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println("Удаление задачи по id: " + 3 + " и " + 1 + ". Также проверим несуществующий id: " + 32);
        inMemoryTaskManager.delById(32);
        inMemoryTaskManager.delById(3);
        System.out.println("История просмотров после удаления эпика по id " + 3 + ":");
        System.out.println(inMemoryTaskManager.getHistory());
        inMemoryTaskManager.delById(1);
        System.out.println("История просмотров после удаления задачи по id " + 1 + ":");
        System.out.println(inMemoryTaskManager.getHistory());

        System.out.println("Список задач:");
        System.out.println(inMemoryTaskManager.getTaskList());
        System.out.println("Список подзадач:");
        System.out.println(inMemoryTaskManager.getSubTaskList());
        System.out.println("Список эпиков:");
        System.out.println(inMemoryTaskManager.getEpicList());
        System.out.println();

        System.out.println("Очистка трекера задач");
        inMemoryTaskManager.delAll();
        System.out.println("Список задач:");
        System.out.println(inMemoryTaskManager.getTaskList());
        System.out.println("Список подзадач:");
        System.out.println(inMemoryTaskManager.getSubTaskList());
        System.out.println("Список эпиков:");
        System.out.println(inMemoryTaskManager.getEpicList());
        System.out.println(inMemoryTaskManager.getHistory());
    }
}