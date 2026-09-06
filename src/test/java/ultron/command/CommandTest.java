package ultron.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

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

    private Storage storage() {
        return new Storage(temporaryDirectory.resolve("tasks.txt").toString());
    }
}
