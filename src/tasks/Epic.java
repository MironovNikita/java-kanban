package tasks;

import workWithTasks.InMemoryTaskManager;

import java.time.LocalDateTime;
import java.util.HashMap;

public class Epic extends Task {
    private final HashMap<Integer, SubTask> subTaskList;
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
        subTaskList = new HashMap<>();
    }

    public Epic(String name, String description, int duration) {
        super(name, description, duration);
        subTaskList = new HashMap<>();
    }

    public HashMap<Integer, SubTask> getSubTaskList() {
        return subTaskList;
    }


    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    //При добавлении подзадачи должен происходить расчёт его продолжительности и времени начала/конца эпика
    public void createSubTask(SubTask sub) {

            if (sub.getStartTime() != null) {
                boolean isAdded = true;
                for(SubTask subTask : subTaskList.values()) {
                    if(subTask.getStartTime() != null) {
                        if (!subTask.getStatus().equals(TaskStatus.DONE.toString())) {
                            if(InMemoryTaskManager.shouldNotBeAdded(sub, subTask)) {
                                isAdded = false;
                                System.out.println("Подзадача " + sub.getInfo()
                                        + " не была добавлена в EPIC " + this.getId()
                                        + ", так как её время пересекается с текущими задачами");
                                break;
                            }
                        }
                    }
                }
                if(isAdded) {
                    this.setDuration(this.getDuration() + sub.getDuration());
                    if (subTaskList.isEmpty()) {
                        this.setStartTime(sub.getStartTime());;
                        endTime = sub.getEndTime();
                    } else if (this.getStartTime().isAfter(sub.getStartTime())) {
                        this.setStartTime(sub.getStartTime());;
                    } else if (endTime.isBefore(sub.getEndTime())) {
                        endTime = sub.getEndTime();
                    }
                    subTaskList.put(sub.getId(), sub);
                    sub.setEpicId(this.getId());
                }
            } else {
                this.setDuration(this.getDuration() + sub.getDuration());
                subTaskList.put(sub.getId(), sub);
                sub.setEpicId(this.getId());
            }
        }


    public HashMap<Integer, SubTask> getAllSubTask() {
        return subTaskList;
    }

    public void updateStatus() {
        if(this.subTaskList.isEmpty()) {
            if(this.getStatus().equals(TaskStatus.NEW.toString())) this.setStatus(TaskStatus.IN_PROGRESS.toString());
            else this.setStatus(TaskStatus.DONE.toString());
        } else {
            boolean flag = true;
            for (SubTask sub : subTaskList.values()) {
                if (!sub.getStatus().equals(TaskStatus.DONE.toString())) {
                    flag = false;
                }
                if (sub.getStatus().equals(TaskStatus.IN_PROGRESS.toString())) {
                    this.setStatus(TaskStatus.IN_PROGRESS.toString());
                    flag = false;
                    break;
                }
            }
            if (flag) this.setStatus(TaskStatus.DONE.toString());
        }
    }
}
