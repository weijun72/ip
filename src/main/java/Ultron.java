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
        Scanner scanner = new Scanner(System.in);

        Storage storage = new Storage(System.getProperty("ultron.saveFile", "data/ultron.txt"));
        TaskList tasks = new TaskList(storage.loadTasks());
        while (true) {
            String input = scanner.nextLine();

            try {
                if (input.equals("bye")) {
                    ui.showGoodbye();
                    break;
                } else if (input.equals("list")) {
                    ui.showTaskList(tasks);
                } else if (input.startsWith("mark ")) {
                    String taskNumberText = input.substring(5).trim();
                    try {
                        int taskNumber = Integer.parseInt(taskNumberText);
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
                } else if (input.startsWith("unmark ")) {
                    String taskNumberText = input.substring(7).trim();
                    try {
                        int taskNumber = Integer.parseInt(taskNumberText);
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
                } else if (input.startsWith("delete ")) {
                    String taskNumberText = input.substring(7).trim();
                    try {
                        int taskNumber = Integer.parseInt(taskNumberText);
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
                } else if (input.equals("todo") || input.startsWith("todo ")) {
                    String description = input.substring(4).trim();
                    if (description.isEmpty()) {
                        throw new UltronException("You FOOL! The description of a todo cannot be empty.");
                    }
                    tasks.add(new Todo(description));
                    storage.saveTasks(tasks.getTasks());
                    ui.showTaskAdded(tasks.get(tasks.size() - 1));
                    ui.showTaskCount(tasks.size());
                    ui.showSeparator();
                } else if (input.startsWith("deadline ")) {
                    String input1 = input.substring(9).trim();
                    tasks.add(new Deadline(input1));
                    storage.saveTasks(tasks.getTasks());
                    ui.showTaskAdded(tasks.get(tasks.size() - 1));
                    ui.showTaskCount(tasks.size());
                    ui.showSeparator();
                } else if (input.startsWith("event ")) {
                    String input1 = input.substring(6).trim();
                    tasks.add(new Event(input1));
                    storage.saveTasks(tasks.getTasks());
                    ui.showTaskAdded(tasks.get(tasks.size() - 1));
                    ui.showTaskCount(tasks.size());
                    ui.showSeparator();
                } else {
                    throw new UltronException("INVALID INPUT");
                }
            } catch (UltronException e) {
                ui.showError(e.getMessage());
            }
        }
        scanner.close();
    }

}
