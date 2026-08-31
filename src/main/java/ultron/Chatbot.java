package ultron;

import ultron.command.Command;
import ultron.exception.UltronException;
import ultron.model.Deadline;
import ultron.model.Event;
import ultron.model.Task;
import ultron.model.TaskList;
import ultron.model.Todo;
import ultron.parser.Parser;
import ultron.storage.Storage;
import ultron.ui.Ui;

/**
 * Coordinates command parsing, task management, persistence, and chatbot responses.
 */
public class Chatbot {
    private final Parser parser;
    private final Storage storage;
    private final TaskList tasks;

    /**
     * Creates a chatbot that loads tasks from the specified storage file.
     *
     * @param storagePath the path of the persistent task storage file
     */
    public Chatbot(String storagePath) {
        parser = new Parser();
        storage = new Storage(storagePath);
        tasks = new TaskList(storage.loadTasks());
    }

    /**
     * Processes one user command and sends its response to the given interface.
     *
     * @param input the raw command entered by the user
     * @param ui the interface that displays the chatbot response
     * @return {@code true} when the command ends the chatbot session
     */
    public boolean processCommand(String input, Ui ui) {
        try {
            Command commandObject = parser.parseCommandObject(input);
            if (commandObject != null) {
                commandObject.execute(tasks, ui, storage);
                return commandObject.isExit();
            }

            Parser.CommandType command = parser.parseCommand(input);
            switch (command) {
                case TODO -> addTodo(input, ui);
                case DEADLINE -> addDeadline(input, ui);
                case EVENT -> addEvent(input, ui);
                case UNKNOWN -> throw new UltronException("INVALID INPUT");
                default -> throw new UltronException("Invalid command");
            }
        } catch (UltronException e) {
            ui.showError(e.getMessage());
        }
        return false;
    }

    private void addTodo(String input, Ui ui) throws UltronException {
        String description = parser.getArgument(input, Parser.CommandType.TODO);
        if (description.isEmpty()) {
            throw new UltronException("You FOOL! The description of a todo cannot be empty.");
        }
        addTask(new Todo(description), ui);
    }

    private void addDeadline(String input, Ui ui) throws UltronException {
        String deadlineDetails = parser.getArgument(input, Parser.CommandType.DEADLINE);
        addTask(new Deadline(deadlineDetails), ui);
    }

    private void addEvent(String input, Ui ui) throws UltronException {
        String eventDetails = parser.getArgument(input, Parser.CommandType.EVENT);
        addTask(new Event(eventDetails), ui);
    }

    private void addTask(Task task, Ui ui) {
        tasks.add(task);
        showSavedTask(ui);
    }

    private void showSavedTask(Ui ui) {
        storage.saveTasks(tasks.getTasks());
        ui.showTaskAdded(tasks.get(tasks.size() - 1));
        ui.showTaskCount(tasks.size());
        ui.showSeparator();
    }
}
