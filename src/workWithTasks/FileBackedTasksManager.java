package workWithTasks;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskTypes;


import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private static final String FILE_HEADER = "id,type,name,status,description,epic";
    private static final File fileHistory = new File ("history.csv");

    public static void main(String[] args) {

        TaskManager fileManager = Managers.getDefault();

        Task task1 = new Task("Почистить зубы", "Тщательно");
        Task task2 = new Task("Побриться", "Основательно");
        Epic epic1 = new Epic("Переезд", "В новый дом");
        SubTask sub1 = new SubTask("Собрать все коробки", "Да, да... Все коробочки!");
        SubTask sub2 = new SubTask("Упаковать кошку", "Прощаемся с кошкой!");
        SubTask sub3 = new SubTask("Сделать домашнее задание", "До воскресенья");
        epic1.createSubTask(sub1);
        epic1.createSubTask(sub2);
        epic1.createSubTask(sub3);
        Epic epic2 = new Epic("Учёба", "Изучаем JAVA");
        SubTask sub4 = new SubTask("Порадоваться выполненному заданию", "Но ждать замечаний :)");
        epic2.createSubTask(sub4);

        fileManager.create(epic1);
        fileManager.create(epic2);
        fileManager.create(task1);
        fileManager.create(task2);
        System.out.println(fileManager.getAll());

        System.out.println("Обновление EPIC'а");
        sub1.setDescription("Загрузить в машину");
        fileManager.updateTask(sub1);
        System.out.println(fileManager.getAll());
        System.out.println("-----------------------------------------------------------");
        sub2.setDescription("Кошка в клетке");
        fileManager.updateTask(sub2);
        System.out.println(fileManager.getAll());
        System.out.println("-----------------------------------------------------------");
        sub1.setDescription("Найти водителя");
        fileManager.updateTask(sub1);
        System.out.println(fileManager.getAll());
        System.out.println("-----------------------------------------------------------");
        sub2.setDescription("Кошка села за руль и уехала");
        fileManager.updateTask(sub2);

        System.out.println("Получение задачи по id: ");
        System.out.println(fileManager.getById(4));
        System.out.println(fileManager.getById(7));
        System.out.println(fileManager.getById(1));

        System.out.println(fileManager.getById(2));
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

        System.out.println("Менеджер, созданный из файла:");
        FileBackedTasksManager fileManagerRead = loadFromFile(fileHistory);
        System.out.println(fileManagerRead.getAll());
        System.out.println("Восстановленная история менеджера из файла:");
        System.out.println(fileManagerRead.getHistory());

        /*Семён, привет! Я оставил ответы в коде на тех местах, где были вопросы, а также поменял немного программу в
        соответствии с рекомендациями.
        Буду ждать результатов!*/

        /*System.out.println("Проверка работы менеджера из файла:");
        System.out.println(fileManagerRead.getById(8));
        System.out.println(fileManagerRead.getById(1));
        System.out.println(fileManagerRead.getById(7));
        System.out.println("Изменённая история:");
        System.out.println(fileManagerRead.getHistory());*/

        /*fileManagerRead.delById(1);
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
    public HashMap<Integer, Task> getTaskList() {
        return super.getTaskList();
    }

    @Override
    public HashMap<Integer, Epic> getEpicList() {
        return super.getEpicList();
    }

    /* По поводу throws. Я, кажется, понял, для чего оно указывается. Когда сдавал на ревью это ТЗ в первый раз, я
    думал, что throws указывается в том методе, где оно обрабатывается и может произойти. Сейчас понял, что throws мы
    указываем, когда исключение проверяемое. В нашем случае - непроверяемое. Соответственно и здесь у save(), и далее
    у loadFromFile(File file) я убрал throws из сигнатур методов.*/
    public void save() {
        try(OutputStreamWriter historyFile = new OutputStreamWriter(new FileOutputStream(fileHistory),
                Charset.forName("windows-1251"))) {
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
            historyFile.write(historyToString(Managers.getDefaultHistory()));
        } catch (IOException exception) {
            System.out.println("Информация об ошибке:");
            exception.printStackTrace();
            throw new ManagerSaveException("Ошибка записи файла!");

        }
    }

    public String toString(Task task){
        if(task == null) throw new RuntimeException("Task - null!");
        else {
            String type;
            String epicId = "";
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
            /*Да, ты прав. Лучше будет применять StringBuilder. Так будет работать быстрее.
            При чём я посмотрел на код свежим взглядом и увидел, что ранее я в том же historyToString применял его...
            Вот, что значит делать ТЗ за несколько дней. Даже мыслишь по-разному...*/
            StringBuilder toStr = new StringBuilder();
            toStr.append(task.getId()).append(",").append(type).append(",").append(task.getName()).append(",")
                    .append(task.getStatus()).append(",").append(task.getDescription()).append(",")
                    .append(epicId).append("\n");
            return toStr.toString();
        }
    }

    public Task fromString(String value){
        if(value == null) throw new RuntimeException("String - null!");
        else {
            Task task;
            String[] values = value.split(",");
            if (values[1].equals("TASK")) {
                task = new Task(values[2], values[4]);
            } else if (values[1].equals("SUBTASK")) {
                task = new SubTask(values[2], values[4]);
            } else {
                task = new Epic(values[2], values[4]);
            }
            int taskId = Integer.parseInt(values[0]);
            task.setId(taskId);
            task.setStatus(values[3]);
            return task;
        }
    }

    static String historyToString(HistoryManager manager){
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

    static List<Integer> historyFromString(String value) {
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
    static FileBackedTasksManager loadFromFile(File file) {
        final FileBackedTasksManager loadManagerFromFile = new FileBackedTasksManager();
        try (BufferedReader bufReader = new BufferedReader(new InputStreamReader(new FileInputStream(file),
                "windows-1251"))) {
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
                    } else if(task instanceof SubTask && !loadManagerFromFile.getEpicList().isEmpty()) {
                        int whatEpic = Integer.parseInt(line.substring(line.length() - 1));
                        for(Epic epic : loadManagerFromFile.getEpicList().values()) {
                            if(whatEpic == epic.getId()) {
                                epic.getSubTaskList().put(task.getId(), (SubTask) task);
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
            throw new ManagerSaveException("Ошибка чтения файла! Проверьте его наличие по указанному пути!");
        }
        return loadManagerFromFile;
    }
    /* Здесь кидал FileNotFoundException, т.к. подумал, что ManagerSaveException мы пишем для метода сохранения save().
    Но после замечания понял, что ManagerSaveException нужно наследовать от RuntimeException, и всё встало на свои
    места.*/
}