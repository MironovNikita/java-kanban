public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
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

        manager.create(epic1);
        manager.create(epic2);
        manager.create(task1);
        manager.create(task2);
        System.out.println(manager.getAll());

        System.out.println("Получение задачи по id: " + 5);
        System.out.println(manager.getById(5));
        System.out.println();

        System.out.println("Получение списка всех подзадач определённого эпика: " + 2);
        System.out.println(manager.getSubTaskList(epic2));
        System.out.println();

        System.out.println("Обновление задачи TASK");
        task1.description = "Помыть щётку";
        manager.updateTask(task1);
        System.out.println(manager.getAll());
        task1.description = "Убрать щётку";
        task2.description = "Помыть бритву";
        manager.updateTask(task1);
        manager.updateTask(task2);
        System.out.println(manager.getAll());
        manager.updateTask(task2);
        System.out.println(manager.getAll());
        System.out.println();

        System.out.println("Обновление EPIC'a");
        sub1.description = "Загрузить в машину";
        manager.updateTask(sub1);
        System.out.println(manager.getAll());
        System.out.println("-----------------------------------------------------------");
        sub2.description = "Кошка в клетке";
        manager.updateTask(sub2);
        System.out.println(manager.getAll());
        System.out.println("-----------------------------------------------------------");
        sub1.description = "Найти водителя";
        manager.updateTask(sub1);
        System.out.println(manager.getAll());
        System.out.println("-----------------------------------------------------------");
        sub2.description = "Кошка села за руль и уехала";
        manager.updateTask(sub2);
        System.out.println(manager.getAll());
        System.out.println("Удалим задачу у эпика по id: " + 4);
        manager.delById(4);
        System.out.println(manager.getAll());
        System.out.println();

        System.out.println("Удаление задачи по id: " + 1 + ". Также проверим несуществующий id: " + 32);
        manager.delById(32);
        manager.delById(1);
        System.out.println(manager.getAll());
        System.out.println();

        System.out.println("Очистка трекера задач");
        manager.delAll();
        System.out.println(manager.getAll());
    }
}
/*Семён, привет! Спасибо за замечания, в целом всё подправил. Комментарии, где просил, я оставил:).
Добавил enum. В целом прикольная штука, чтобы никто не мог вместо данных трёх статусов задач закинуть что-то ещё.
Либо же, если нужно будет добавить какой-нибудь новый статус, я просто в enum его закидываю и дальше могу использовать,
вместо того, чтобы переписывать часть программы. Удобно!:)*/