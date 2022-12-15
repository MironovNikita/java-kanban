import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import workWithTasks.Managers;
import workWithTasks.TaskManager;

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
        System.out.println(inMemoryTaskManager.getHistory());
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
        task1.setDescription("Помыть щётку");
        inMemoryTaskManager.updateTask(task1);
        System.out.println(inMemoryTaskManager.getAll());
        task1.setDescription("Убрать щётку");
        task2.setDescription("Помыть бритву");
        inMemoryTaskManager.updateTask(task1);
        inMemoryTaskManager.updateTask(task2);
        System.out.println(inMemoryTaskManager.getAll());
        inMemoryTaskManager.updateTask(task2);
        System.out.println(inMemoryTaskManager.getAll());
        System.out.println();

        System.out.println("Обновление EPIC'а");
        sub1.setDescription("Загрузить в машину");
        inMemoryTaskManager.updateTask(sub1);
        System.out.println(inMemoryTaskManager.getAll());
        System.out.println("-----------------------------------------------------------");
        sub2.setDescription("Кошка в клетке");
        inMemoryTaskManager.updateTask(sub2);
        System.out.println(inMemoryTaskManager.getAll());
        System.out.println("-----------------------------------------------------------");
        sub1.setDescription("Найти водителя");
        inMemoryTaskManager.updateTask(sub1);
        System.out.println(inMemoryTaskManager.getAll());
        System.out.println("-----------------------------------------------------------");
        sub2.setDescription("Кошка села за руль и уехала");
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

/*Семён, привет! Действительно, я немного запутался с тем, какого типа должна быть та или иная переменная (поле)
класса... В теории всё более менее ясно, на практике есть некоторые затруднения.

В случае с Epic'ом, думаю, subTaskList нужно сделать private. IDEA предлагает также добавить final, но, если честно,
не до конца понимаю, для чего. Почитал теорию, насколько я понял, final в данном случае делает некую "защиту", чтобы
мы не могли Epic'у присвоить какой-либо другой объект (список subTaskList) от другого Epic'а. А private нужен для того,
чтобы закрыть доступ другим классам к списку subTask'ов.

По InMemoryHistoryManager, метод addToHistory. Насколько я понял, двунаправленная очередь может принимать null как
объект. Однако, в таком случае, при проверке на пустоту этой самой очереди будет неясно: null это элемент или же null
это показатель того, что элементы отсутствуют... Поэтому я добавил в метод addToHistory проверку передаваемого в метод
объекта на null. Хотя, по идее, в методе getById у меня есть проверка, содержит ли список Task'ов или Epic'ов объект
с таким id. По идее, если id присвоен объекту, то уже он не может быть null.

По InMemoryTaskManager. По идее мы не должны иметь прямого доступа к списку Epic'ов или Task'ов. Думаю, здесь также
должен стоять модификатор private. И, соответственно, final также добавляется для того, чтобы нельзя было присвоить
никакой другой объект. Объект (в моём случае HashMap) можно менять, добавлять, удалять элементы, но если он уже создан
заменить его другим нельзя. Опять же, если я правильно трактую то, что прочитал из теории и нашёл в интернете.

По Managers. Сделал метод getDefaultHistory package-private или default, т.е. доступным в рамках пакета workWithTasks.
Как я понял, в дальнейшем наш класс Managers будет сам выбирать, какой класс по работе с задачами выбрать (аналог уже
имеющегося у меня InMemoryTaskManager). В соответствии с этим будет выбираться реализация истории просмотров тех или
иных задач. Поэтому, думаю, что static HistoryManager getDefaultHistory() должна быть доступна для работы в рамках
пакета по работе с задачами.

Надеюсь, я правильно понял суть того, что нужно было подправить. В целом вроде стало понятнее. Но пока тяжеловато
продумывать наперёд, насколько должен быть закрыт доступ к тому или иному полю или методу.
 */