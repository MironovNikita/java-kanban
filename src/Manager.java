import java.util.HashMap;
import java.util.Iterator;

public class Manager {
    HashMap<Integer, Task> taskList;
    HashMap<Integer, Epic> epicList;

    public Manager() {
        taskList = new HashMap<>();
        epicList = new HashMap<>();
    }

    public String getAll() {
        StringBuilder info = new StringBuilder();
        System.out.println("Трекер задач: ");
        System.out.println();
        for (Task task : taskList.values()) {
            info.append("TASK: " + task.getInfo() + "\n");
        }
        for (Epic task : epicList.values()) {
            info.append("EPIC: " + task.getInfo() + "\n");
            for (SubTask sub : task.getAllSubTask().values()) {
                info.append("- sub: " + sub.getInfo() + "\n");
            }
        }
        return info.toString();
    }

    public void delAll() {
        boolean taskEmpty = taskList.isEmpty();
        boolean epicEmpty = epicList.isEmpty();

        if(taskEmpty && epicEmpty) {
            System.out.println("Нет задач для удаления! Трекер задач пуст!");
            return;
        }
        if(!taskEmpty) {
            taskList.clear();
            System.out.println("Список задач очищен!");
        } else {
            System.out.println("Список задач пуст!");
        }
        if(!epicEmpty) {
            epicList.clear();
            System.out.println("Список эпиков очищен!");
        } else {
            System.out.println("Список эпиков пуст!");
        }
    }

    public Task getById(int id) {
        boolean taskKey = taskList.containsKey(id);
        boolean epicKey = epicList.containsKey(id);
        if(taskKey) return taskList.get(id);
        if(epicKey) return epicList.get(id);
        else {
            for (Epic task : epicList.values()) {
                for (SubTask sub : task.getAllSubTask().values()) {
                    if(sub.getId() == id) return sub;
                }
            }
        }
        return null;
    }

    public void create (Task task) {
        if(task instanceof Epic) {
            epicList.put(task.id, (Epic)task);
        }
        else if (task instanceof Task) {
            taskList.put(task.id, task);
        }
    }
    public void updateTask (Task task) {
        int id = task.getId();
        taskList.get(id).name = task.name;
        taskList.get(id).description = task.description;
        if(taskList.get(id).status == "NEW") taskList.get(id).status = "IN_PROGRESS";
        else if(taskList.get(id).status == "IN_PROGRESS") taskList.get(id).status = "DONE";
        else {
            System.out.println("Эта задача уже была завершена");
        }
    }

    public void updateTask (SubTask sub) {
        int id = sub.getId();
        for (Epic task : epicList.values()) {
            for (SubTask subj : task.getAllSubTask().values()) {
                if(subj.getId() == id) {
                    int epicId = task.getId();
                    subj.name = sub.name;
                    subj.description = sub.description;
                    if(subj.status == "NEW") subj.status = "IN_PROGRESS";
                    else if(subj.status == "IN_PROGRESS") subj.status = "DONE";
                    epicList.get(epicId).updateStatus();
                }
            }
        }
    }

    public void delById(int id) {
        boolean taskKey = taskList.containsKey(id);
        boolean epicKey = epicList.containsKey(id);
        if(taskKey) {
            taskList.remove(id);
            return;
        }
        if(epicKey) {
            epicList.remove(id);
            return;
        }
        else {
            for (Epic task : epicList.values()) {
                for (SubTask sub : task.getAllSubTask().values()) {
                    if(sub.getId() == id) {
                        task.subTaskList.remove(id);
                        return;
                    }
                }
            }
        }
        System.out.println("Задачи с таким ID (" + id + ") нет в Трекере!");
    }

    public HashMap<Integer, SubTask> getSubTaskList(Epic epic) {
        return epic.subTaskList;
    }
}