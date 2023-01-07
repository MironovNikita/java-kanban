package workWithTasks;

public class Node <Task> {
    public Task taskData;
    public Node<Task> next;
    public Node<Task> prev;

    public Node(Node<Task> prev, Task taskData, Node<Task> next) {
        this.taskData = taskData;
        this.next = next;
        this.prev = prev;
    }

    @Override
    public String toString(){
        return taskData.toString();
    }
}
