package tasks;

import java.util.HashMap;

public class Epic extends Task {
    protected HashMap<Integer, SubTask> subTaskList;

    public Epic(String name, String description) {
        super(name, description);
        subTaskList = new HashMap<>();
    }

    public HashMap<Integer, SubTask> getSubTaskList() {
        return subTaskList;
    }

    public void createSubTask(SubTask sub) {
        subTaskList.put(sub.getId(), sub);
    }

    public HashMap<Integer, SubTask> getAllSubTask() {
        return subTaskList;
    }

    public void updateStatus() {
        boolean flag = true;
        for(SubTask sub : subTaskList.values()) {
            if(!sub.getStatus().equals(TaskStatus.DONE.toString())) {
                flag = false;
            }
            if(sub.getStatus().equals(TaskStatus.IN_PROGRESS.toString())) {
                this.setStatus(TaskStatus.IN_PROGRESS.toString());
                flag = false;
                break;
            }
        }
        if (flag) this.setStatus(TaskStatus.DONE.toString());
    }
}
