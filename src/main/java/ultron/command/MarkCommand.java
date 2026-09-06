package ultron.command;

import java.util.OptionalInt;

import ultron.model.Task;
import ultron.model.TaskList;
import ultron.storage.Storage;
import ultron.ui.Ui;

/**
 * Marks a numbered task as done.
 */
public class MarkCommand extends Command {
    private final String taskNumberText;

    /**
     * Creates a mark command with its task-number argument.
     *
     * @param taskNumberText the task number supplied by the user
     */
    public MarkCommand(String taskNumberText) {
        this.taskNumberText = taskNumberText;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        OptionalInt taskIndex = getTaskIndex(taskNumberText, tasks, ui, "mark");
        if (taskIndex.isPresent()) {
            Task task = tasks.get(taskIndex.getAsInt());
            task.markAsDone();
            storage.saveTasks(tasks.getTasks());
            ui.showTaskMarked(task);
        }
        ui.showSeparator();
    }
}
