package workWithTasks;

class ManagerSaveException extends RuntimeException {
    public ManagerSaveException(String message, Throwable ex) {
        super(message, ex);
    }
}
