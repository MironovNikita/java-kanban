package API;

enum Endpoint{
    //Методы TASK
    GET_TASKS, // GET /tasks/task/ - вернуть список простых задач - (getTaskList())
    GET_TASK_BY_ID, // GET /tasks/task/?id= - вернуть задачу по ID (getById(int id))
    POST_TASK, // POST /tasks/task/Body{...} - добавить задачу (если есть такой ID, обновить её) (create (Task task)/updateTask (Task task))
    DELETE_TASK_BY_ID, //DELETE /tasks/task/?id=(число) - удалить задачу по ID (delById(int id))
    DELETE_TASKLIST, // DELETE /tasks/task - удалить все простые задачи

    //Методы SUBTASK
    GET_SUBTASKS, // GET /tasks/subtask/ - вернуть список всех подзадач - НАДО ПИСАТЬ МЕТОД
    GET_SUBTASK_BY_ID, // GET /tasks/subtask/?id= - вернуть подзадачу по ID (getById(int id))
    POST_SUBTASK, // POST /tasks/subtask/Body{...} - добавить задачу (если есть такой ID, обновить её) (create (SubTask subtask)/updateTask (SubTask sub))
    DELETE_SUBTASK_BY_ID, //DELETE /tasks/subtask/?id= - удалить подзадачу по ID (delById(int id))
    DELETE_SUBTASKLIST, // DELETE /tasks/subtask - удалить все подзадачи

    //Методы EPIC
    GET_EPICS, // GET /tasks/epic/ - вернуть список всех эпиков - НАДО ПИСАТЬ МЕТОД (вроде есть)
    GET_EPIC_BY_ID, // GET /tasks/epic/?id= - вернуть эпик по ID (getById(int id))
    POST_EPIC, // POST /tasks/epic/Body{...} - добавить задачу (если есть такой ID, обновить её) (create (Epic epic))
    DELETE_EPIC_BY_ID, //DELETE /tasks/epic/?id=(число) - удалить эпик по ID (delById(int id))
    DELETE_EPICLIST, // DELETE /tasks/epic - удалить все эпики

    //Общие, т.е. в единственном экземпляре
    DELETE_ALL_TASKS, // DELETE /tasks/all - удалить вообще все задачи (delAll())
    GET_EPIC_SUBTASKS, // GET /tasks/subtasks/epic/?id=(число) - получить список подзадач эпика (getSubTaskList(Epic epic))
    GET_HISTORY, // GET /tasks/history - получить историю (getHistory())
    GET_PRIORITIZED_TASKS, //GET /tasks/ - получить список приоритета (getPrioritizedTasks())
    UNKNOWN}
