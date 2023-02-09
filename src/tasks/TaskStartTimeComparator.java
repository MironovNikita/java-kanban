package tasks;

import java.util.Comparator;

public class TaskStartTimeComparator implements Comparator<Task> {
    @Override
    public int compare(Task task1, Task task2) {
        if(task1.equals(task2)) return 0;
        if(task1.getStartTime() != null && task2.getStartTime() != null) {
            if(task1.getStartTime().isAfter(task2.getStartTime())) {
                return 1;
            } else if (task1.getStartTime().isAfter(task2.getStartTime())) {
                return -1;
            } else return -1;
        }
        if(task1.getStartTime() == null && task2.getStartTime() != null) {
            return 1;
        }
        if(task1.getStartTime() != null && task2.getStartTime() == null) {
            return -1;
        }
        return -1;
    }
}
