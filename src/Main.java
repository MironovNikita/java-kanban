public class Main {
    public static void main(String[] args) {
        TaskManager inMemoryTaskManager = Managers.getDefault();
        Task task1 = new Task("Почистить зубы", "Тщательно");
        Task task2 = new Task("Побриться", "Основательно");
        Epic epic1 = new Epic("Переезд", "В новый дом");
        SubTask sub1 = new SubTask("Собрать все коробки", "Да, да... Все коробочки!");
        SubTask sub2 = new SubTask("Упаковать кошку", "Прощай, кошка!");
        epic1.createSubTask(sub1);
        epic1.createSubTask(sub2);
        Epic epic2 = new Epic("Учёба", "Изучаем JAVA");
        SubTask sub3 = new SubTask("Сделать домашнее задание", "До воскресенья");
        epic2.createSubTask(sub3);

        inMemoryTaskManager.create(epic1);
        inMemoryTaskManager.create(epic2);
        inMemoryTaskManager.create(task1);
        inMemoryTaskManager.create(task2);
        System.out.println(inMemoryTaskManager.getAll());

        System.out.println("Получение задачи по id: ");
        System.out.println(inMemoryTaskManager.getById(5));
        System.out.println(inMemoryTaskManager.getById(2));
        System.out.println(inMemoryTaskManager.getById(1));
        System.out.println(inMemoryTaskManager.getHistory().toString());
        System.out.println("Получение задачи по id: ");
        System.out.println(inMemoryTaskManager.getById(7));
        System.out.println(inMemoryTaskManager.getById(4));
        System.out.println(inMemoryTaskManager.getById(6));
        System.out.println(inMemoryTaskManager.getById(1));
        System.out.println(inMemoryTaskManager.getById(3));
        System.out.println(inMemoryTaskManager.getById(5));
        System.out.println(inMemoryTaskManager.getById(2));
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println("Получение задачи по id: ");
        System.out.println(inMemoryTaskManager.getById(3));
        System.out.println(inMemoryTaskManager.getById(5));
        System.out.println(inMemoryTaskManager.getById(2));
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println();

        System.out.println("Получение списка всех подзадач определённого эпика: " + 2);
        System.out.println(inMemoryTaskManager.getSubTaskList(epic2));
        System.out.println();

        System.out.println("Обновление задачи TASK");
        task1.description = "Помыть щётку";
        inMemoryTaskManager.updateTask(task1);
        System.out.println(inMemoryTaskManager.getAll());
        task1.description = "Убрать щётку";
        task2.description = "Помыть бритву";
        inMemoryTaskManager.updateTask(task1);
        inMemoryTaskManager.updateTask(task2);
        System.out.println(inMemoryTaskManager.getAll());
        inMemoryTaskManager.updateTask(task2);
        System.out.println(inMemoryTaskManager.getAll());
        System.out.println();

        System.out.println("Обновление EPIC'a");
        sub1.description = "Загрузить в машину";
        inMemoryTaskManager.updateTask(sub1);
        System.out.println(inMemoryTaskManager.getAll());
        System.out.println("-----------------------------------------------------------");
        sub2.description = "Кошка в клетке";
        inMemoryTaskManager.updateTask(sub2);
        System.out.println(inMemoryTaskManager.getAll());
        System.out.println("-----------------------------------------------------------");
        sub1.description = "Найти водителя";
        inMemoryTaskManager.updateTask(sub1);
        System.out.println(inMemoryTaskManager.getAll());
        System.out.println("-----------------------------------------------------------");
        sub2.description = "Кошка села за руль и уехала";
        inMemoryTaskManager.updateTask(sub2);
        System.out.println(inMemoryTaskManager.getAll());
        System.out.println("Удалим задачу у эпика по id: " + 4);
        inMemoryTaskManager.delById(4);
        System.out.println(inMemoryTaskManager.getAll());
        System.out.println();

        System.out.println("Удаление задачи по id: " + 1 + ". Также проверим несуществующий id: " + 32);
        inMemoryTaskManager.delById(32);
        inMemoryTaskManager.delById(1);
        System.out.println(inMemoryTaskManager.getAll());
        System.out.println();

        System.out.println("Очистка трекера задач");
        inMemoryTaskManager.delAll();
        System.out.println(inMemoryTaskManager.getAll());
    }
}

