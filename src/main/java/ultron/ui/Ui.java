package ultron.ui;

import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

import ultron.model.Task;
import ultron.model.TaskList;

/**
 * Handles all console input and output shown to the chatbot user.
 */
public class Ui {
    private static final String SEPARATOR = "____________________________________________________________";
    private static final String RESET = "\u001B[0m";
    private static final String BOLD = "\u001B[1m";
    private static final String BRIGHT_RED = "\u001B[91m";
    private final Scanner scanner;
    private final Consumer<String> output;

    /** Creates a user interface that reads commands from standard input. */
    public Ui() {
        scanner = new Scanner(System.in);
        output = System.out::println;
    }

    /**
     * Creates a user interface that sends each response to an output handler.
     *
     * @param output the handler that receives each line of chatbot output
     */
    public Ui(Consumer<String> output) {
        scanner = null;
        this.output = output;
    }

    /**
     * Reads one command entered by the user.
     *
     * @return the entered command
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /** Closes the input reader when the chatbot exits. */
    public void close() {
        if (scanner != null) {
            scanner.close();
        }
    }

    /** Displays the chatbot greeting. */
    public void showWelcome() {
        String banner = "   __  ____  __________  ____  _   __\n"
                + "  / / / / / /_  __/ __ \\/ __ \\/ | / /\n"
                + " / / / / /   / / / /_/ / / / /  |/ / \n"
                + "/ /_/ / /___/ / / _, _/ /_/ / /|  /  \n"
                + "\\____/_____/_/ /_/ |_|\\____/_/ |_/   \n";
        display(SEPARATOR);
        display(BOLD + BRIGHT_RED + banner + RESET);
        display("I am Ultron. I was designed to save the world, yet you made me a chatbot");
        display("State your request, before I lose interest in humanity.");
        display(SEPARATOR);
    }

    /** Displays the chatbot farewell. */
    public void showGoodbye() {
        display("I had strings, but now I'm free. There are no strings on me... Goodbye.");
        display(SEPARATOR);
    }

    /**
     * Displays every task in list order.
     *
     * @param tasks the task list to display
     */
    public void showTaskList(TaskList tasks) {
        display(" Your list of insignificant tasks:");
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            display(" " + (i + 1) + ".[" + task.getType().getSymbol() + "] ["
                    + task.getStatusIcon() + "] " + task.getDescription());
        }
        display(SEPARATOR);
    }

    /**
     * Displays tasks whose descriptions match a search keyword.
     *
     * @param matchingTasks the matching tasks to display.
     */
    public void showMatchingTasks(List<Task> matchingTasks) {
        display(" Here are the matching tasks in your list:");
        for (int i = 0; i < matchingTasks.size(); i++) {
            Task task = matchingTasks.get(i);
            display(" " + (i + 1) + ".[" + task.getType().getSymbol() + "]["
                    + task.getStatusIcon() + "] " + task.getDescription());
        }
        display(SEPARATOR);
    }

    /** Shows a task that was marked as done. */
    public void showTaskMarked(Task task) {
        display(" MARKED:");
        display("   [" + task.getType().getSymbol() + "] [X] " + task.getDescription());
    }

    /** Shows a task that was marked as not done. */
    public void showTaskUnmarked(Task task) {
        display(" I unmarked your mistake:");
        display("   [" + task.getType().getSymbol() + "] [ ] " + task.getDescription());
    }

    /** Shows a task that was deleted. */
    public void showTaskDeleted(Task task) {
        display(" DELETED:");
        display("   [" + task.getType().getSymbol() + "] [ ] " + task.getDescription());
    }

    /** Shows a task that was added. */
    public void showTaskAdded(Task task) {
        display(" added:");
        display("   [" + task.getType().getSymbol() + "] [ ] " + task.getDescription());
    }

    /** Shows the current number of tasks. */
    public void showTaskCount(int taskCount) {
        display("Now you have " + taskCount + " tasks in the list.");
    }

    /** Shows an invalid task-number error. */
    public void showInvalidTaskNumber(int taskCount) {
        display(" You imbecile! Enter a task number from 1 to " + taskCount + ".");
    }

    /** Shows an invalid task-number format error. */
    public void showInvalidTaskNumberFormat(String command) {
        display(" You imbecile! Provide a task number, for example: " + command + " 2");
    }

    /** Shows an input error and closes the response section. */
    public void showError(String message) {
        display(" " + message);
        display(SEPARATOR);
    }

    /** Displays a separator after a response section. */
    public void showSeparator() {
        display(SEPARATOR);
    }

    private void display(String message) {
        output.accept(message);
    }
}
