package ultron.command;

import java.util.OptionalInt;

import ultron.model.Task;
import ultron.model.TaskList;
import ultron.storage.Storage;
import ultron.ui.Ui;

/**
 * Removes a numbered task from the task list.
 */
public class DeleteCommand extends Command {
    private final String taskNumberText;

    /**
     * Creates a delete command with its task-number argument.
     *
     * @param taskNumberText the task number supplied by the user
     */
    public DeleteCommand(String taskNumberText) {
        this.taskNumberText = taskNumberText;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        OptionalInt taskIndex = getTaskIndex(taskNumberText, tasks, ui, "delete");
        if (taskIndex.isPresent()) {
            Task deletedTask = tasks.remove(taskIndex.getAsInt());
            ui.showTaskDeleted(deletedTask);
            storage.saveTasks(tasks.getTasks());
            ui.showTaskCount(tasks.size());
        }
        ui.showSeparator();
    }
}
