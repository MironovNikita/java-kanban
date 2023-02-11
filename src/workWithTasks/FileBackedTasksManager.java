package workWithTasks;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskTypes;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private static final String FILE_HEADER = "id,type,name,status,description,epic";
    private static final File fileHistory = new File ("history.csv");

    public static void main(String[] args) {

        /*Семён, привет! Спасибо за ревью!
        Оставил в коде ответы, где были вопросы (по большей части - конструктор Task).
        Раскомментировал код, добавил второй вызов порядка приоритета менеджера, восстановленного из файла,
        всё совпадает :)
        Также удалил добавление эпиков, не до конца понял ТЗ. Пункт начинается с "Отсортируйте все задачи...", это меня
        и смутило, хотя по логике верно - эпика тут быть не должно
        Касаемо неточности согласен! Я когда делал ТЗ, думал, что уже всё готово и увидел, что у меня один из эпиков
        продолжительностью всего 3 минуты (делал для тестирования), начинается, условно 1-го января, а заканчивается
        20-го января. Т.е. по факту за 20 дней я потрачу всего 3 минуты :D И я прогонял методы через debug - всё уже
        к тому моменту было верно, не мог понять, почему getEndTime() так работает... А потом до меня дошло, что
        берётся самое позднее время из всех подзадач... Попался на этом, в общем. В целом тяжёленькое ТЗ вышло.

        Насчёт записи файла спасибо за помощь! Я более менее разобрался, в чём нюансы записи/чтения. Насчёт FileReader
        и FileWriter ты прав - данные классы используют символьный поток, поэтому они хороши для обработки текстовых
        файлов и используют системную кодировку по умолчанию. А BufferedReader и BufferedWriter позволяют ускорить
        чтение/запись файла и в целом работу программы за счёт того, что сначала заполняется буфер, а затем он
        записывается в файл целиком, а не посимвольно
        Понял, что перемудрил в прошлый раз. Я как раз шёл по пути байтового потока. И, видимо, из-за этого на твоём
        ноутбуке всё возвращалось кракозябрами.*/

        TaskManager fileManager = Managers.getDefault();

        Task task1 = new Task("Почистить зубы", "Тщательно",5);
        Task task2 = new Task("Побриться", "Основательно",120,
                LocalDateTime.of(2023,1,2,12,0));
        Epic epic1 = new Epic("Переезд", "В новый дом",0);
        SubTask sub1 = new SubTask("Собрать все коробки", "Да, да... Все коробочки!",180,
                LocalDateTime.of(2023,1,1,10,0));
        SubTask sub2 = new SubTask("Упаковать кошку", "Прощаемся с кошкой!",10,
                LocalDateTime.of(2023,1,2,12,0));
        SubTask sub3 = new SubTask("Сделать домашнее задание", "До воскресенья",1);

        epic1.createSubTask(sub2);
        epic1.createSubTask(sub3);
        Epic epic2 = new Epic("Учёба", "Изучаем JAVA",0);
        SubTask sub4 = new SubTask("Порадоваться выполненному заданию", "Но ждать замечаний :)",
                10, LocalDateTime.of(2023, 3,10,10,0));
        epic2.createSubTask(sub4);

        fileManager.create(task1);
        fileManager.create(epic1);
        fileManager.create(task2);
        fileManager.create(epic2);

        epic1.createSubTask(sub1); //Задача не добавится
        System.out.println(fileManager.getAll());

        System.out.println("Обновление EPIC'а");
        sub2.setDescription("Кошка в клетке");
        fileManager.updateTask(sub2);
        System.out.println(fileManager.getAll());
        System.out.println("-----------------------------------------------------------");
        fileManager.updateTask(sub2);
        fileManager.updateTask(sub3);
        System.out.println(fileManager.getAll());
        System.out.println("-----------------------------------------------------------");
        sub2.setDescription("Кошка села за руль и уехала");
        fileManager.updateTask(sub2);
        fileManager.updateTask(sub3);
        System.out.println(fileManager.getAll());
        System.out.println("-----------------------------------------------------------");

        System.out.println("Получение задачи по id: ");
        System.out.println(fileManager.getById(7));
        System.out.println(fileManager.getById(1));

        System.out.println(fileManager.getById(5));
        System.out.println(fileManager.getById(6));
        System.out.println(fileManager.getById(3));
        System.out.println(fileManager.getById(8));

        System.out.println(fileManager.getById(1));
        System.out.println(fileManager.getById(5));
        System.out.println(fileManager.getById(3));
        System.out.println("История менеджера, сохранённого в файл:");
        System.out.println(fileManager.getHistory());
        System.out.println();
        System.out.println();

        System.out.println("Вывод задач в порядке приоритета:");
        System.out.println(fileManager.getPrioritizedTasks());

        System.out.println("Менеджер, созданный из файла:");
        FileBackedTasksManager fileManagerRead = loadFromFile(fileHistory);
        System.out.println(fileManagerRead.getAll());
        System.out.println("Восстановленная история менеджера из файла:");
        System.out.println(fileManagerRead.getHistory());

        System.out.println();
        System.out.println("Вывод задач в порядке приоритета у менеджера, восстановленного из файла:");
        System.out.println(fileManagerRead.getPrioritizedTasks());

        /*System.out.println("Проверка работы менеджера из файла:");
        System.out.println(fileManagerRead.getById(8));
        System.out.println(fileManagerRead.getById(1));
        System.out.println(fileManagerRead.getById(7));
        System.out.println("Изменённая история:");
        System.out.println(fileManagerRead.getHistory());

        fileManagerRead.delById(1);
        fileManagerRead.getAll();
        System.out.println("История просмотров после удаления задачи по id " + 1 + ":");
        System.out.println(fileManagerRead.getHistory());

        System.out.println("Очистка трекера задач");
        fileManagerRead.delAll();
        System.out.println(fileManagerRead.getAll());
        System.out.println(fileManagerRead.getHistory());
        if(fileHistory.delete()) {
            System.out.println("Файл \"history.csv\" файл был удален из корневой папки проекта");
        } else System.out.println("Файл \"history.csv\" не был найден в корневой папке проекта");*/
    }

    @Override
    public void create (Task task) {
        super.create(task);
        save();
    }

    @Override
    public Task getById(int id) {
        Task task = super.getById(id);
        save();
        return task;
    }

    @Override
    public void updateTask (Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateTask (SubTask sub) {
        super.updateTask(sub);
        save();
    }

    @Override
    public void delById(int id) {
        super.delById(id);
        save();
    }

    @Override
    public void delAll() {
        super.delAll();
        save();
    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return super.getPrioritizedTasks();
    }

    @Override
    public HashMap<Integer, Task> getTaskList() {
        return super.getTaskList();
    }

    @Override
    public HashMap<Integer, Epic> getEpicList() {
        return super.getEpicList();
    }

    public void save() {
        try(BufferedWriter historyFile = new BufferedWriter(new FileWriter(fileHistory))) {
            historyFile.write(FILE_HEADER + "\n");
            if(!getTaskList().isEmpty()) {
                for(Task task : getTaskList().values()) {
                    historyFile.write(toString(task));
                }
            }
            if(!getEpicList().isEmpty()) {
                for (Epic task : getEpicList().values()) {
                    historyFile.write(toString(task));
                    for (SubTask sub : task.getAllSubTask().values()) {
                        historyFile.write(toString(sub));
                    }
                }
            }
            historyFile.write("\n");
            historyFile.write(historyToString(getHistory()));
        } catch (IOException exception) {
            System.out.println("Информация об ошибке:");
            exception.printStackTrace();
            throw new ManagerSaveException("Ошибка записи файла!", exception);
        }
    }

    public String toString(Task task){
        if(task == null) throw new RuntimeException("Task - null!");
        else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[dd.MM.yyyy - HH:mm]");
            String type;
            String epicId = "";
            String startOf = "";
            String endOf = "";
            if(task instanceof SubTask) {
                type = TaskTypes.SUBTASK.toString();
                for(Epic epic : getEpicList().values()) {
                    if(epic.getSubTaskList().containsKey(task.getId())) {
                        epicId = epic.getId().toString();
                    }
                }
            } else if(task instanceof Epic) {
                type = TaskTypes.EPIC.toString();
            }
            else type = TaskTypes.TASK.toString();

            if(task.getStartTime() != null) {
                startOf = task.getStartTime().format(formatter);
                endOf = task.getEndTime().format(formatter);
            }

            StringBuilder toStr = new StringBuilder();
            toStr.append(task.getId()).append(",").append(type).append(",").append(task.getName()).append(",")
                    .append(task.getStatus()).append(",").append(task.getDescription()).append(",").append(startOf)
                    .append(",").append(task.getDuration()).append(",").append(endOf).append(",").append(epicId)
                    .append("\n");
            return toStr.toString();
        }
    }

    public Task fromString(String value){
        if(value == null) throw new RuntimeException("String - null!");
        else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[dd.MM.yyyy - HH:mm]");
            Task task;
            String[] values = value.split(",");
            if (values[1].equals("TASK")) {
                task = new Task(values[2], values[4], Integer.parseInt(values[6]));
                if(!values[5].equals("")) task.setStartTime(LocalDateTime.from(formatter.parse(values[5])));
                task.setDuration(Integer.parseInt(values[6]));
            } else if (values[1].equals("SUBTASK")) {
                task = new SubTask(values[2], values[4], Integer.parseInt(values[6]));
                if(!values[5].equals("")) {
                    task.setStartTime(LocalDateTime.from(formatter.parse(values[5])));
                }
                task.setDuration(Integer.parseInt(values[6]));
            } else {
                task = new Epic(values[2], values[4], Integer.parseInt(values[6]));
                if(!values[5].equals("")) task.setStartTime(LocalDateTime.from(formatter.parse(values[5])));
                task.setDuration(Integer.parseInt(values[6]));
            }
            int taskId = Integer.parseInt(values[0]);
            task.setId(taskId);
            task.setStatus(values[3]);
            return task;
        }
    }

    public static String historyToString(HistoryManager manager){
        List<Task> hisToStr = manager.getHistory();
        if(hisToStr == null) throw new RuntimeException("Список null!");
        else {
            StringBuilder stringBuilder = new StringBuilder();
            for (Task task : hisToStr) {
                stringBuilder.append(task.getId().toString()).append(",");
            }
            //Удаляем последнюю запятую
            if(stringBuilder.length() > 0) {
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            }
            return stringBuilder.toString();
        }
    }

    public static List<Integer> historyFromString(String value) {
        if(value == null || value.isEmpty()) throw new RuntimeException("String is null!");
        else {
            List<Integer> hisFromStr = new ArrayList<>();
            String[] tasksIds = value.split(",");
            for (String str : tasksIds) {
                hisFromStr.add(Integer.parseInt(str));
            }
            return hisFromStr;
        }
    }

    //Добавил модификатор final, чтобы объект нельзя было перезаписать
    public static FileBackedTasksManager loadFromFile(File file) {
        final FileBackedTasksManager loadManagerFromFile = new FileBackedTasksManager();
        try (BufferedReader bufReader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = bufReader.readLine()) != null) {
                if(line.equals(FILE_HEADER)) {
                    continue;
                } else if(line.equals("")) {
                    break;
                } else {
                    Task task = loadManagerFromFile.fromString(line);
                    if(task instanceof Epic) {
                        loadManagerFromFile.getEpicList().put(task.getId(), (Epic)task);
                        task.setDuration(0);
                    } else if(task instanceof SubTask && !loadManagerFromFile.getEpicList().isEmpty()) {
                        int whatEpic = Integer.parseInt(line.substring(line.length() - 1));
                        for(Epic epic : loadManagerFromFile.getEpicList().values()) {
                            if(whatEpic == epic.getId()) {
                                epic.createSubTask((SubTask)task);
                                break;
                            }
                        }
                    } else {
                        loadManagerFromFile.getTaskList().put(task.getId(), task);
                    }
                }
            }
            while ((line = bufReader.readLine()) != null) {
                if(line.equals("")) {
                    continue;
                }
                List<Integer> histFromNumbs = historyFromString(line);
                for(Integer value : histFromNumbs) {
                    /*Если под этим Id - Task, добавляем в историю
                      Иначе если под этим Id - Epic, добавляем в историю
                      Иначе пробегаемся по всем спискам subTask'ов Epic'ов и добавляем sub в историю*/
                    if(loadManagerFromFile.getTaskList().containsKey(value)) {
                        loadManagerFromFile.getHistory().addToHistory(loadManagerFromFile.getTaskList().get(value));
                    } else if (loadManagerFromFile.getEpicList().containsKey(value)) {
                        loadManagerFromFile.getHistory().addToHistory(loadManagerFromFile.getEpicList().get(value));
                    } else {
                        boolean isAdded = false;
                        for (Epic epic : loadManagerFromFile.getEpicList().values()) {
                            if(!isAdded) {
                                for (SubTask sub : epic.getAllSubTask().values()) {
                                    if(sub.getId().equals(value)) {
                                        loadManagerFromFile.getHistory().addToHistory(sub);
                                        isAdded = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
            throw new ManagerSaveException("Ошибка чтения файла! Проверьте его наличие по указанному пути!", exception);
        }
        return loadManagerFromFile;
    }
}