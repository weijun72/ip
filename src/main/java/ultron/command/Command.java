package ultron.command;

import java.util.OptionalInt;

import ultron.exception.UltronException;
import ultron.model.TaskList;
import ultron.storage.Storage;
import ultron.ui.Ui;

/**
 * Represents one executable chatbot command.
 */
public abstract class Command {

    /**
     * Carries out this command using the chatbot's main components.
     *
     * @param tasks the current task list
     * @param ui the user interface
     * @param storage the task storage
     * @throws UltronException if the command cannot be completed
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws UltronException;

    /**
     * Converts a user-supplied one-based task number to a zero-based index.
     *
     * <p>Shows the appropriate error message when the task number is not an integer or does not identify
     * an existing task.</p>
     *
     * @param taskNumberText the task number supplied by the user
     * @param tasks the current task list
     * @param ui the interface that displays errors
     * @param commandName the command name used in an invalid-format error
     * @return an index for an existing task, or an empty value when the input is invalid
     */
    protected OptionalInt getTaskIndex(String taskNumberText, TaskList tasks, Ui ui, String commandName) {
        try {
            int taskNumber = Integer.parseInt(taskNumberText);
            if (taskNumber < 1 || taskNumber > tasks.size()) {
                ui.showInvalidTaskNumber(tasks.size());
                return OptionalInt.empty();
            }
            return OptionalInt.of(taskNumber - 1);
        } catch (NumberFormatException e) {
            ui.showInvalidTaskNumberFormat(commandName);
            return OptionalInt.empty();
        }
    }

    /**
     * Returns whether this command ends the chatbot session.
     *
     * @return {@code true} only for the exit command
     */
    public boolean isExit() {
        return false;
    }
}
