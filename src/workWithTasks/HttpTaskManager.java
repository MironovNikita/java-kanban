package workWithTasks;

import KV_Server.KVTaskClient;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {
    private String url;
    KVTaskClient client;
    public HttpTaskManager (String url) {
        this.url = url;
        client = new KVTaskClient(url);
    }

    @Override
    public void save() {
        StringBuilder data = new StringBuilder();
        if(!getTaskList().isEmpty()) {
            for(Task task : getTaskList().values()) {
                data.append(toString(task));
            }
        }
        if(!getEpicList().isEmpty()) {
            for (Epic task : getEpicList().values()) {
                data.append(toString(task));
                for (SubTask sub : task.getAllSubTask().values()) {
                    data.append((toString(sub)));
                }
            }
        }
        if(!getSubTaskList().isEmpty()) {
            for (SubTask subTask : getSubTaskList().values()) {
                data.append(toString(subTask));
            }
        }
        data.append("\n");
        data.append(historyToString(getHistory()));
        client.put("httpManager", data.toString());
    }

    public HttpTaskManager load(){
        int taskQuantity = 0;
        final HttpTaskManager httpManager = new HttpTaskManager(url);
        String[] lines = client.load("httpManager").split("\n");
        if(lines.length == 0) return httpManager;
        for(String str : lines) {
            if(str.equals("")) {
                break;
            }
            Task task = httpManager.fromString(str);
            taskQuantity++;
            if(Task.getUniqId() < task.getId()) Task.setUniqId(task.getId() + 1);
            if(task instanceof Epic) {
                httpManager.getEpicList().put(task.getId(), (Epic)task);
                task.setDuration(0);
            } else if(task instanceof SubTask && !httpManager.getEpicList().isEmpty()) {
                int whatEpic = Integer.parseInt(str.substring(str.length() - 1));
                for(Epic epic : httpManager.getEpicList().values()) {
                    if(whatEpic == epic.getId()) {
                        epic.createSubTask((SubTask)task);
                        ((SubTask) task).setEpicId(epic.getId());
                        break;
                    }
                }
            } else {
                httpManager.getTaskList().put(task.getId(), task);
            }
        }
        if(lines.length == taskQuantity) {
            return httpManager;
        }
        String history = lines[lines.length - 1];
        List<Integer> histFromNumbs = historyFromString(history);
        for(Integer value : histFromNumbs) {
                    /*Если под этим Id - Task, добавляем в историю
                      Иначе если под этим Id - Epic, добавляем в историю
                      Иначе пробегаемся по всем спискам subTask'ов Epic'ов и добавляем sub в историю*/
            if(httpManager.getTaskList().containsKey(value)) {
                httpManager.getHistory().addToHistory(httpManager.getTaskList().get(value));
            } else if (httpManager.getEpicList().containsKey(value)) {
                httpManager.getHistory().addToHistory(httpManager.getEpicList().get(value));
            } else {
                boolean isAdded = false;
                for (Epic epic : httpManager.getEpicList().values()) {
                    if(!isAdded) {
                        for (SubTask sub : epic.getAllSubTask().values()) {
                            if(sub.getId().equals(value)) {
                                httpManager.getHistory().addToHistory(sub);
                                isAdded = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return httpManager;
    }
}
