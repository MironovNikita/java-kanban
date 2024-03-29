package workWithTasks;

public class Node <Task> {
    private final Task taskData;
    private Node<Task> next;
    private Node<Task> prev;

    public Node(Node<Task> prev, Task taskData, Node<Task> next) {
        this.taskData = taskData;
        this.next = next;
        this.prev = prev;
    }

    @Override
    public String toString(){
        return taskData.toString();
    }

    public Task getTaskData() {
        return taskData;
    }

    public Node<Task> getNext() {
        return next;
    }

    public void setNext(Node<Task> next) {
        this.next = next;
    }

    public Node<Task> getPrev() {
        return prev;
    }

    public void setPrev(Node<Task> prev) {
        this.prev = prev;
    }
}
