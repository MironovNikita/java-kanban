package workWithTasks;

import tasks.Task;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {
    private static final HashMap<Integer, Node<Task>> historyList = new HashMap<>();
    private static final CustomLinkedList customLinkedList = new CustomLinkedList();
    @Override
    public List<Task> getHistory() {
        return customLinkedList.getTasks();
    }

    @Override
    public void addToHistory(Task task) {
        if(task == null) {
            System.out.println("В историю просмотра задач нельзя добавить пустой объект!");
            return;
        }
        //Для исключения повторений, если задача уже есть в истории, удаляем ноду и удаляем её из истории просмотров
        removeNode(historyList.get(task.getId()));
        historyList.remove(task.getId());
        //Записываем переданную задачу в customLinkedList (создаём ноду) и в историю просмотров
        customLinkedList.linkLast(task);
        historyList.put(task.getId(), customLinkedList.tail);
    }

    @Override
    public void remove(int id) {
        if (historyList.containsKey(id)) {
            removeNode(historyList.get(id));
            historyList.remove(id);
        }
    }

    public void removeNode(Node<Task> node) {
        if(historyList.containsValue(node)) {
            //Если переданная нода является головой, переписываем голову как следующее от текущей ноды значение
            if(node == customLinkedList.head) customLinkedList.head = node.next;
            if(node.prev != null) node.prev.next = node.next;
            if(node.next != null) node.next.prev = node.prev;
            customLinkedList.size--;
        }
    }

    @Override
    public String toString() {
        return "История просмотров:\n" + getHistory();
    }

    static class CustomLinkedList extends LinkedList<Task> {

        private Node<Task> head = null;
        private Node<Task> tail = null;
        private int size = 0;

        public void linkLast(Task task) {
            final Node<Task> l = tail;
            final Node<Task> newNode = new Node<>(l, task, null);
            tail = newNode;
            if (l == null)
                head = newNode;
            else
                l.next = newNode;
            size++;
        }

        public List<Task> getTasks() {
            ArrayList<Task> history = new ArrayList<>();
            //Если голова null, то история пуста
            Node<Task> buf = head;
            if(buf == null) return history;
            while (buf.next != null) {
                history.add(buf.taskData);
                buf = buf.next;
            }
            history.add(buf.taskData);
            return history;
        }
    }
}


