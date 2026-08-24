import java.util.Scanner;

/**
 * A command-line task list that can add, list, mark, and unmark tasks.
 */
public class Ultron {

    /**
     * Runs the chatbot command loop.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {

        Ui ui = new Ui();
        ui.showWelcome();
        Parser parser = new Parser();
        Scanner scanner = new Scanner(System.in);

        Storage storage = new Storage(System.getProperty("ultron.saveFile", "data/ultron.txt"));
        TaskList tasks = new TaskList(storage.loadTasks());
        while (true) {
            String input = scanner.nextLine();

            try {
                Parser.Command command = parser.parseCommand(input);
                switch (command) {
                case BYE:
                    ui.showGoodbye();
                    scanner.close();
                    return;
                case LIST:
                    ui.showTaskList(tasks);
                    break;
                case MARK:
                    try {
                        int taskNumber = parser.parseTaskNumber(input, command);
                        if (taskNumber < 1 || taskNumber > tasks.size()) {
                            ui.showInvalidTaskNumber(tasks.size());
                        } else {
                            int taskIndex = taskNumber - 1;
                            tasks.get(taskIndex).markAsDone();
                            storage.saveTasks(tasks.getTasks());
                            ui.showTaskMarked(tasks.get(taskIndex));
                        }
                    } catch (NumberFormatException e) {
                        ui.showInvalidTaskNumberFormat("mark");
                    }
                    ui.showSeparator();
                    break;
                case UNMARK:
                    try {
                        int taskNumber = parser.parseTaskNumber(input, command);
                        if (taskNumber < 1 || taskNumber > tasks.size()) {
                            ui.showInvalidTaskNumber(tasks.size());
                        } else {
                            int taskIndex = taskNumber - 1;
                            tasks.get(taskIndex).markAsUndone();
                            storage.saveTasks(tasks.getTasks());
                            ui.showTaskUnmarked(tasks.get(taskIndex));
                        }
                    } catch (NumberFormatException e) {
                        ui.showInvalidTaskNumberFormat("unmark");
                    }
                    ui.showSeparator();
                    break;
                case DELETE:
                    try {
                        int taskNumber = parser.parseTaskNumber(input, command);
                        if (taskNumber < 1 || taskNumber > tasks.size()) {
                            ui.showInvalidTaskNumber(tasks.size());
                        } else {
                            int taskIndex = taskNumber - 1;
                            Task deletedTask = tasks.remove(taskIndex);
                            ui.showTaskDeleted(deletedTask);
                            storage.saveTasks(tasks.getTasks());
                            ui.showTaskCount(tasks.size());
                            ui.showSeparator();
                        }
                    } catch (NumberFormatException e) {
                        ui.showInvalidTaskNumberFormat("delete");
                    }
                    ui.showSeparator();
                    break;
                case TODO:
                    String description = parser.getArgument(input, command);
                    if (description.isEmpty()) {
                        throw new UltronException("You FOOL! The description of a todo cannot be empty.");
                    }
                    tasks.add(new Todo(description));
                    storage.saveTasks(tasks.getTasks());
                    ui.showTaskAdded(tasks.get(tasks.size() - 1));
                    ui.showTaskCount(tasks.size());
                    ui.showSeparator();
                    break;
                case DEADLINE:
                    String input1 = parser.getArgument(input, command);
                    tasks.add(new Deadline(input1));
                    storage.saveTasks(tasks.getTasks());
                    ui.showTaskAdded(tasks.get(tasks.size() - 1));
                    ui.showTaskCount(tasks.size());
                    ui.showSeparator();
                    break;
                case EVENT:
                    String eventDetails = parser.getArgument(input, command);
                    tasks.add(new Event(eventDetails));
                    storage.saveTasks(tasks.getTasks());
                    ui.showTaskAdded(tasks.get(tasks.size() - 1));
                    ui.showTaskCount(tasks.size());
                    ui.showSeparator();
                    break;
                case UNKNOWN:
                    throw new UltronException("INVALID INPUT");
                }
            } catch (UltronException e) {
                ui.showError(e.getMessage());
            }
        }
    }

}
