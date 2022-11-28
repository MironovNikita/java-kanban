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

        //Очистка трекера задач
        /*manager.delAll();
        System.out.println(manager.getAll());*/

        //Получение задачи по id
        /*manager.getById(5);
        System.out.println(manager.getById(5));*/

        //Удаление задачи по id
        /*manager.delById(3);
        System.out.println(manager.getAll());*/

        //Получение списка всех подзадач определённого эпика
        /*manager.getSubTaskList(epic2);
        System.out.println(manager.getSubTaskList(epic2));*/

        //Обновление задачи TASK
        /*task1.description = "Помыть щётку";
        manager.updateTask(task1);
        System.out.println(manager.getAll());
        task1.description = "Убрать щётку";
        task2.description = "Помыть бритву";
        manager.updateTask(task1);
        manager.updateTask(task2);
        System.out.println(manager.getAll());*/

        //Обновление EPIC'a
        /*sub1.description = "Загрузить в машину";
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
        System.out.println(manager.getAll());*/
    }
}
