public class Task {
    static int uniqId = 1;

    int id;
    String status = "NEW";
    String name;
    String description;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        id = uniqId;
        uniqId++;
    }

    public String getInfo() {
        return id + ")" + " " + name + " " + "(" + description + ")" + " " + status;
    }

    public String getStatus() {
        return status;
    }

    public Integer getId() {
        return id;
    }

    public void setStatus(String status) {
        if (status == "DONE" || status == "NEW" || status == "IN_PROGRESS") {
            this.status = status;
        }
    }

    //Переопределение toString() для проверки, что выводится именно та задача/эпик/подзадача по id
    /*@Override
    public String toString() {
        return id + ")" + " " + name + " " + "(" + description + ")" + " " + status;
    }*/
}
