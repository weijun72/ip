package ultron.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the completion-status display of a task.
 */
class TaskTest {

    @Test
    void getStatusIcon_newTask_spaceIcon() {
        Task task = new Task("read book", TaskType.TODO);

        assertEquals(" ", task.getStatusIcon());
    }

    @Test
    void getStatusIcon_taskMarkedDone_xIcon() {
        Task task = new Task("read book", TaskType.TODO);
        task.markAsDone();

        assertEquals("X", task.getStatusIcon());
    }

    @Test
    void getStatusIcon_taskUnmarkedAfterDone_spaceIcon() {
        Task task = new Task("read book", TaskType.TODO);
        task.markAsDone();
        task.markAsUndone();

        assertEquals(" ", task.getStatusIcon());
    }
}
