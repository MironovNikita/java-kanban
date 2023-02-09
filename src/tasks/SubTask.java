package tasks;

import java.time.LocalDateTime;

public class SubTask extends Task {
    public SubTask(String name, String description, int duration) {
        super(name, description, duration);
    }

    public SubTask(String name, String description, int duration, LocalDateTime startTime) {
        super(name, description, duration, startTime);
    }
}