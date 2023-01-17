package tasks;

public class Task {
    static int uniqId = 1;

    private int id;
    private String status = TaskStatus.NEW.toString();
    protected String name;
    protected String description;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        id = uniqId;
        uniqId++;
    }

    public String getInfo() {
        return id + ")" + " " + name + " " + "(" + description + ")" + " " + status;
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
        return id + ")" + " " + name + " " + "(" + description + ")" + " " + status + "\n";
    }
}
