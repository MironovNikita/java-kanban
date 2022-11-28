import java.util.HashMap;

public class Epic extends Task {
    HashMap<Integer, SubTask> subTaskList;

    public Epic(String name, String description) {
        super(name, description);
        subTaskList = new HashMap<>();
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
            if(sub.getStatus() != "DONE") {
                flag = false;
            }
            if(sub.getStatus() == "IN_PROGRESS") {
                this.status = "IN_PROGRESS";
                flag = false;
                break;
            }
        }
        if (flag == true) this.status = "DONE";
    }
}
