package ultron.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import ultron.exception.UltronException;
import ultron.model.Deadline;
import ultron.model.TaskList;
import ultron.model.Todo;
import ultron.storage.Storage;
import ultron.ui.Ui;

class CommandTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void markCommand_existingTask_marksTaskAsDone() {
        TaskList tasks = new TaskList(new Todo("read book"));

        new MarkCommand("1").execute(tasks, new Ui(), storage());

        assertTrue(tasks.get(0).isDone());
    }

    @Test
    void unmarkCommand_existingDoneTask_marksTaskAsNotDone() {
        Todo task = new Todo("read book");
        task.markAsDone();
        TaskList tasks = new TaskList(task);

        new UnmarkCommand("1").execute(tasks, new Ui(), storage());

        assertFalse(tasks.get(0).isDone());
    }

    @Test
    void deleteCommand_existingTask_removesTask() {
        TaskList tasks = new TaskList(new Todo("read book"));

        new DeleteCommand("1").execute(tasks, new Ui(), storage());

        assertEquals(0, tasks.size());
    }

    @Test
    void markCommand_invalidTaskNumber_doesNotChangeTask() {
        TaskList tasks = new TaskList(new Todo("read book"));

        new MarkCommand("two").execute(tasks, new Ui(), storage());

        assertFalse(tasks.get(0).isDone());
    }

    @Test
    void rescheduleCommand_absoluteDate_preservesTimeAndShowsOldAndNewDetails() throws UltronException {
        Deadline deadline = new Deadline("return book /by 2/12/2099 1800");
        TaskList tasks = new TaskList(deadline);
        List<String> output = new ArrayList<>();

        new RescheduleCommand("1 /by 3/01/2100").execute(tasks, new Ui(output::add), storage());

        assertEquals("return book( by: 03/Jan/2100 1800 )", deadline.getDescription());
        assertTrue(output.contains("   From: return book( by: 02/Dec/2099 1800 )"));
        assertTrue(output.contains("   To:   return book( by: 03/Jan/2100 1800 )"));
    }

    @Test
    void rescheduleCommand_relativeDays_addsDaysToCurrentDueDate() throws UltronException {
        Deadline deadline = new Deadline("return book /by 2/12/2099 1800");
        TaskList tasks = new TaskList(deadline);

        new RescheduleCommand("1 3d").execute(tasks, quietUi(), storage());

        assertEquals("return book( by: 05/Dec/2099 1800 )", deadline.getDescription());
    }

    @Test
    void rescheduleCommand_absoluteDateWithTime_replacesTime() throws UltronException {
        Deadline deadline = new Deadline("return book /by 2/12/2099 1800");
        TaskList tasks = new TaskList(deadline);

        new RescheduleCommand("1 /by 3/01/2100 0930").execute(tasks, quietUi(), storage());

        assertEquals("return book( by: 03/Jan/2100 0930 )", deadline.getDescription());
    }

    @Test
    void rescheduleCommand_zeroDays_exceptionThrownWithoutChangingDeadline() throws UltronException {
        Deadline deadline = new Deadline("return book /by 2/12/2099");
        TaskList tasks = new TaskList(deadline);
        RescheduleCommand command = new RescheduleCommand("1 0d");

        assertThrows(UltronException.class, () -> command.execute(tasks, quietUi(), storage()));

        assertEquals("return book( by: 02/Dec/2099 )", deadline.getDescription());
    }

    @Test
    void rescheduleCommand_relativePastResult_exceptionThrownWithoutChangingDeadline() throws UltronException {
        Deadline deadline = new Deadline("return book /by 1/01/2000");
        TaskList tasks = new TaskList(deadline);
        RescheduleCommand command = new RescheduleCommand("1 3d");

        assertThrows(UltronException.class, () -> command.execute(tasks, quietUi(), storage()));

        assertEquals("return book( by: 01/Jan/2000 )", deadline.getDescription());
    }

    @Test
    void rescheduleCommand_completedDeadline_exceptionThrownWithoutChangingDeadline() throws UltronException {
        Deadline deadline = new Deadline("return book /by 2/12/2099");
        deadline.markAsDone();
        TaskList tasks = new TaskList(deadline);

        RescheduleCommand command = new RescheduleCommand("1 3d");

        assertThrows(UltronException.class, () -> command.execute(tasks, quietUi(), storage()));

        assertEquals("return book( by: 02/Dec/2099 )", deadline.getDescription());
    }

    @Test
    void rescheduleCommand_todo_exceptionThrownWithoutChangingTask() {
        Todo todo = new Todo("read book");
        TaskList tasks = new TaskList(todo);

        RescheduleCommand command = new RescheduleCommand("1 3d");

        assertThrows(UltronException.class, () -> command.execute(tasks, quietUi(), storage()));

        assertEquals("read book", todo.getDescription());
    }

    @Test
    void findCommand_matchingTasks_showsOriginalTaskNumbers() throws UltronException {
        TaskList tasks = new TaskList(
                new Todo("read book"),
                new Todo("buy bread"),
                new Todo("return book"));
        List<String> output = new ArrayList<>();

        new FindCommand("book").execute(tasks, new Ui(output::add), storage());

        assertTrue(output.contains(" 1.[T][ ] read book"));
        assertTrue(output.contains(" 3.[T][ ] return book"));
    }

    private Storage storage() {
        return new Storage(temporaryDirectory.resolve("tasks.txt").toString());
    }

    private Ui quietUi() {
        return new Ui(line -> { });
    }
}
