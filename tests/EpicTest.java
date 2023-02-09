import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.TaskStatus;
import workWithTasks.InMemoryTaskManager;
import workWithTasks.TaskManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {
    private static TaskManager manager;
    private static Epic epic;
    private static SubTask subTask1;
    private static SubTask subTask2;
    private static SubTask subTask3;
    private static SubTask subTask4;

    @BeforeEach
    void createEpicAndSubTasks() {
        epic = new Epic("Epic", "Description of epic", 0);
        manager = new InMemoryTaskManager();
        subTask1 = new SubTask("Subtask 1", "Description of subtask1", 0);
        subTask2 = new SubTask("Subtask 2", "Description of subtask2", 0);
        subTask3 = new SubTask("Subtask 3", "Description of subtask3", 0);
        subTask4 = new SubTask("Subtask 4", "Description of subtask4", 0);
    }

    @DisplayName("Обновление статуса эпика без подзадач")
    @Test
    void shouldUpdateEpicStatusWithEmptySubtaskList() {
        epic.updateStatus();
        assertEquals(TaskStatus.IN_PROGRESS.toString(), epic.getStatus(), "Статус эпика не IN_PROGRESS");
        epic.updateStatus();
        assertEquals(TaskStatus.DONE.toString(), epic.getStatus(), "Статус эпика не DONE");
    }

    @DisplayName("Проверка статуса эпика NEW с подзадачами NEW")
    @Test
    void shouldReturnNewEpicStatusWithAllSubtasksNewStatus() {
        epic.createSubTask(subTask1);
        epic.createSubTask(subTask2);

        assertEquals(TaskStatus.NEW.toString(), epic.getStatus(), "Статус эпика не NEW");
    }

    @DisplayName("Проверка статуса эпика DONE с подзадачами DONE")
    @Test
    void shouldReturnDoneEpicStatusWithAllSubtasksDoneStatus() {
        epic.createSubTask(subTask1);
        epic.createSubTask(subTask2);
        manager.create(epic);
        manager.updateTask(subTask1);
        manager.updateTask(subTask1);
        manager.updateTask(subTask2);
        manager.updateTask(subTask2);

        assertEquals(TaskStatus.DONE.toString(), epic.getStatus(), "Статус эпика не DONE");
    }

    @DisplayName("Проверка статуса эпика IN_PROGRESS с подзадачами NEW и DONE")
    @Test
    void shouldReturnIn_ProgressEpicStatusWithSubtasksNewAndDoneStatuses() {
        epic.createSubTask(subTask1);
        epic.createSubTask(subTask2);
        epic.createSubTask(subTask3);
        epic.createSubTask(subTask4);
        manager.create(epic);

        manager.updateTask(subTask1);
        manager.updateTask(subTask1);
        manager.updateTask(subTask2);
        manager.updateTask(subTask2);

        assertEquals(TaskStatus.IN_PROGRESS.toString(), epic.getStatus(), "Статус эпика не IN_PROGRESS");
    }

    @DisplayName("Проверка статуса эпика IN_PROGRESS с подзадачами IN_PROGRESS")
    @Test
    void shouldReturnIn_ProgressEpicStatusWithSubtasksIn_ProgressStatus() {
        epic.createSubTask(subTask1);
        epic.createSubTask(subTask2);
        epic.createSubTask(subTask3);
        manager.create(epic);

        manager.updateTask(subTask1);
        manager.updateTask(subTask2);
        manager.updateTask(subTask3);

        assertEquals(TaskStatus.IN_PROGRESS.toString(), epic.getStatus(), "Статус эпика не IN_PROGRESS");
    }
}