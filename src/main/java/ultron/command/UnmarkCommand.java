package ultron.command;

import java.util.OptionalInt;

import ultron.model.Task;
import ultron.model.TaskList;
import ultron.storage.Storage;
import ultron.ui.Ui;

/**
 * Marks a numbered task as not done.
 */
public class UnmarkCommand extends Command {
    private final String taskNumberText;

    /**
     * Creates an unmark command with its task-number argument.
     *
     * @param taskNumberText the task number supplied by the user
     */
    public UnmarkCommand(String taskNumberText) {
        this.taskNumberText = taskNumberText;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        OptionalInt taskIndex = getTaskIndex(taskNumberText, tasks, ui, "unmark");
        if (taskIndex.isPresent()) {
            Task task = tasks.get(taskIndex.getAsInt());
            task.markAsUndone();
            storage.saveTasks(tasks.getTasks());
            ui.showTaskUnmarked(task);
        }
        ui.showSeparator();
    }
}
