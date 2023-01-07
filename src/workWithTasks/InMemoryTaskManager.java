package workWithTasks;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> taskList = new HashMap<>();
    private final HashMap<Integer, Epic> epicList = new HashMap<>();
    private final HistoryManager watchHistory = Managers.getDefaultHistory();

    @Override
    public HistoryManager getHistory() {
        return watchHistory;
    }

    @Override
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

    @Override
    public void delAll() {
        boolean taskEmpty = taskList.isEmpty();
        boolean epicEmpty = epicList.isEmpty();

        if(taskEmpty && epicEmpty) {
            System.out.println("Нет задач для удаления! Трекер задач пуст!");
            return;
        }
        if(!taskEmpty) {
            for (Task task : taskList.values()) {
                watchHistory.remove(task.getId());
            }
            taskList.clear();
            System.out.println("Список задач очищен!");
        } else {
            System.out.println("Список задач пуст!");
        }
        if(!epicEmpty) {
            for (Epic task : epicList.values()) {
                for (SubTask sub : task.getAllSubTask().values()) {
                    watchHistory.remove(sub.getId());
                }
                watchHistory.remove(task.getId());
            }
            epicList.clear();
            System.out.println("Список эпиков очищен!");
        } else {
            System.out.println("Список эпиков пуст!");
        }
    }

    @Override
    public Task getById(int id) {
        boolean taskKey = taskList.containsKey(id);
        boolean epicKey = epicList.containsKey(id);
        if(taskKey) {
            watchHistory.addToHistory(taskList.get(id));
            return taskList.get(id);
        }
        else if(epicKey) {
            watchHistory.addToHistory(epicList.get(id));
            return epicList.get(id);
        }
        else {
            for (Epic task : epicList.values()) {
                for (SubTask sub : task.getAllSubTask().values()) {
                    if(sub.getId() == id) {
                        watchHistory.addToHistory(sub);
                        return sub;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void create (Task task) {
        if(task instanceof Epic) {
            epicList.put(task.getId(), (Epic)task);
        }
        else if (task instanceof Task) {
            taskList.put(task.getId(), task);
        }
    }

    @Override
    public void updateTask (Task task) {
        int id = task.getId();
        taskList.get(id).setName(task.getName());
        taskList.get(id).setDescription(task.getDescription());
        if(taskList.get(id).getStatus().equals(TaskStatus.NEW.toString())) {
            taskList.get(id).setStatus(TaskStatus.IN_PROGRESS.toString());
        }
        else if(taskList.get(id).getStatus().equals(TaskStatus.IN_PROGRESS.toString())) {
            taskList.get(id).setStatus(TaskStatus.DONE.toString());
        }
        else System.out.println("Эта задача уже была завершена");
    }

    @Override
    public void updateTask (SubTask sub) {
        int id = sub.getId();
        for (Epic task : epicList.values()) {
            for (SubTask subj : task.getAllSubTask().values()) {
                if(subj.getId() == id) {
                    int epicId = task.getId();
                    subj.setName(sub.getName());
                    subj.setDescription(sub.getDescription());
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

    @Override
    public void delById(int id) {
        boolean taskKey = taskList.containsKey(id);
        boolean epicKey = epicList.containsKey(id);
        if(taskKey) {
            taskList.remove(id);
            watchHistory.remove(id);
            return;
        }
        else if(epicKey) {
            for (SubTask sub : epicList.get(id).getAllSubTask().values()) {
                watchHistory.remove(sub.getId());
            }
            epicList.get(id).getSubTaskList().clear();
            epicList.remove(id);
            watchHistory.remove(id);
            return;
        }
        else {
            for (Epic task : epicList.values()) {
                for (SubTask sub : task.getAllSubTask().values()) {
                    if(sub.getId() == id) {
                        task.getSubTaskList().remove(id);
                        watchHistory.remove(id);
                        return;
                    }
                }
            }
        }
        System.out.println("Задачи с таким ID (" + id + ") нет в Трекере!");
    }

    @Override
    public HashMap<Integer, SubTask> getSubTaskList(Epic epic) {
        return epic.getSubTaskList();
    }
}