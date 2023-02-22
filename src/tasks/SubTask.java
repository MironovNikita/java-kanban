package tasks;

import java.time.LocalDateTime;

public class SubTask extends Task {

    private int epicId = 0;
    public SubTask(String name, String description, int duration) {
        super(name, description, duration);
    }

    public SubTask(String name, String description, int duration, LocalDateTime startTime) {
        super(name, description, duration, startTime);
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}