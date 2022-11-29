import java.util.HashMap;

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
            info.append("TASK: ").append(task.getInfo()).append("\n");
        }
        for (Epic task : epicList.values()) {
            info.append("EPIC: ").append(task.getInfo()).append("\n");
            for (SubTask sub : task.getAllSubTask().values()) {
                info.append("- sub: ").append(sub.getInfo()).append("\n");
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
            epicList.put(task.getId(), (Epic)task);
        }
        else if (task instanceof Task) {
            taskList.put(task.getId(), task);
        }
    }
    public void updateTask (Task task) {
        int id = task.getId();
        taskList.get(id).name = task.name;
        taskList.get(id).description = task.description;
        if(taskList.get(id).getStatus().equals(TaskStatus.NEW.toString())) {
            taskList.get(id).setStatus(TaskStatus.IN_PROGRESS.toString());
        }
        else if(taskList.get(id).getStatus().equals(TaskStatus.IN_PROGRESS.toString())) {
            taskList.get(id).setStatus(TaskStatus.DONE.toString());
        }
        else System.out.println("Эта задача уже была завершена");
    }

    public void updateTask (SubTask sub) {
        int id = sub.getId();
        for (Epic task : epicList.values()) {
            for (SubTask subj : task.getAllSubTask().values()) {
                if(subj.getId() == id) {
                    int epicId = task.getId();
                    subj.name = sub.name;
                    subj.description = sub.description;
                    if(subj.getStatus().equals(TaskStatus.NEW.toString())) {
                        subj.setStatus(TaskStatus.IN_PROGRESS.toString());
                    }
                    else if(subj.getStatus().equals(TaskStatus.IN_PROGRESS.toString())) {
                        subj.setStatus(TaskStatus.DONE.toString());
                    }
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