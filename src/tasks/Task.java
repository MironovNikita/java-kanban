package tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    static int uniqId = 1;

    private int id;
    private String status = TaskStatus.NEW.toString();
    protected String name;
    protected String description;
    protected int duration;//(в минутах)

    protected LocalDateTime startTime;

    /*Конструктор забыл раскомментировать =) Когда тестировал программу, закомментировал и потом забыл
    раскомментировать*/
    //Конструктор без обработки времени
    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.duration = 0;
        id = uniqId;
        uniqId++;
    }

    /*Здесь по этому конструктору и следующему:
    Логика такая: если кто-то вдруг случайно укажет или duration, или startTime при создании эпика, конструктор
    автоматически присвоит такие поля, какие нужно и рассчитает правильно и его продолжительность, и время начала, и
    время конца соответственно (3 конструктор). Т.е. таким образом я предупреждаю ошибку - программа будет работать
    верно даже если объект эпика будет неверно создан (с продолжительностью и с/без времени начала).*/
    //Конструктор только с продолжительностью (если не задано время начала задачи)
    public Task(String name, String description, int duration) {
        this.name = name;
        this.description = description;
        if(this instanceof Epic) this.duration = 0;
        else this.duration = duration;
        id = uniqId;
        uniqId++;
    }

    //Конструктор с полным набором полей времени
    public Task(String name, String description, int duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        if(this instanceof Epic) {
            this.duration = 0;
            this.startTime = null;
        } else {
            this.duration = duration;
            this.startTime = startTime;
        }
        id = uniqId;
        uniqId++;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(duration);
    }

    //Данный метод нужен для обнуления ID класса Task, а также для указания желаемой точки отсчёта ID для новых задач
    public static void setUniqId(int id) {
        uniqId = id;
    }

    public String getInfo() {
        String startOf = "";
        String endOf = "";
        if(startTime != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[dd.MM.yyyy - HH:mm]");
            startOf = startTime.format(formatter);
            /*if(this.getEndTime() != null)*/ endOf = this.getEndTime().format(formatter);
        }
        StringBuilder taskInfo = new StringBuilder();
        taskInfo.append(id).append(") ").append(name).append(" (").append(description).append(") ").append(status)
                .append(" |Начало: ").append(startOf).append("| |Продолжительность: ").append(duration)
                .append(" мин.|").append(" |Конец: ").append(endOf).append("|");
        return taskInfo.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public Integer getId() {
        return id;
    }

    //Метод добавлен для установки id у задачи при чтении из файла
    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(String status) {
        if (status.equals(TaskStatus.DONE.toString())
                || status.equals(TaskStatus.NEW.toString())
                || status.equals(TaskStatus.IN_PROGRESS.toString())) {
            this.status = status;
        }
    }

    @Override
    public String toString() {
        String startOf = "";
        String endOf = "";
        if(startTime != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[dd.MM.yyyy - HH:mm]");
            startOf = startTime.format(formatter);
            endOf = this.getEndTime().format(formatter);
        }
        StringBuilder taskInfo = new StringBuilder();
        taskInfo.append(id).append(") ").append(name).append(" (").append(description).append(") ").append(status)
                .append(" |Начало: ").append(startOf).append("| |Продолжительность: ").append(duration)
                .append(" мин.|").append(" |Конец: ").append(endOf).append("|").append("\n");
        return taskInfo.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && duration == task.duration && Objects.equals(status, task.status)
                && Objects.equals(name, task.name) && Objects.equals(description, task.description)
                && Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, name, description, duration, startTime) + 31;
    }
}
