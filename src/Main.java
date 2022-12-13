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

/*Семён, привет! Пробегусь по замечаниям:
1) В Epic'е сделал модификатор доступа HashMap'а protected. Вдруг в дальнейшем от Epic'а будет что-либо наследоваться
(какая-нибудь разновидность Epic'а). Ну и делать его package-private не стал, так как внутри пакета tasks могут быть
новые типы задач, чтобы они не имели доступа к Hasmap'у Epic'а.
2) HistoryManager - убрал модификатор public и у интерфейса, и у его методов (которые и так public).
3) InMemoryTaskManager - сделал модификаторы доступа у полей protected. По той же причине, что и в Epic'е. А public
убрал (не подумал исправить сразу, обрадовался, что всё работает :D и забыл)
Про пустые строки - простая невнимательность :( У меня в IDEA над каждым методом и полем подписывается, сколько раз его
используют в коде. И программа сама "отделяет" строку использований от предыдущей. От того иногда не вижу, что не
хватает строки в том или ином месте.
4) Managers - с импортируемыми пакетами тоже я просто забыл их убрать, когда делал последний пункт ТЗ "Сделайте
историю задач интерфейсом". При переносе забыл, что они там есть)) Хорошо, что IDEA добавляет автоматически нужные
импорты, но вот с удалением я попался, впредь обещаю быть внимательнее:)

По пакетам: я раскидал файлики по принципу: в tasks - все виды задач и подзадач, а в workWithTasks - все файлики,
отвечающие за работу с классами задач. Main оставил вне пакетов, правда из-за этого пришлось сделать метод getDefault()
в Managers public'ом, дабы к нему был доступ. Main оставил без пакета, т.к. это главный файл программы и ко всем классам
относится одинаково равнозначно, думаю)

В целом, когда читаю теорию, всё более менее понятно, когда 1 класс, у него 1 интерфейс - тут всё просто и ясно) Но вот
когда начинаются моменты с множеством классов, несколькими интерфейсами, наследованиями и так далее, у меня уже кипит
голова:D Тяжело удержать в голове всё: что к чему и как относится, что откуда принимает, что возвращает и так далее)
Но, как говорится, тяжело в учении - легко в бою) Потому будем набираться опыта, думаю, со временем станет проще))

P.S. Тебе спасибо огромное за подробный разбор задания, а также за дополнительные плюшки, которые подкидываешь для
лучшей реализации задания (в прошлом ТЗ enum, в этом - разбиение на пакеты). Это только в плюс идёт к обучению))
 */