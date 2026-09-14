package ultron.ui;

import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

import ultron.model.Deadline;
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
    private final Consumer<String> errorOutput;

    /** Creates a user interface that reads commands from standard input. */
    public Ui() {
        scanner = new Scanner(System.in);
        output = System.out::println;
        errorOutput = output;
    }

    /**
     * Creates a user interface that sends each response to an output handler.
     *
     * @param output the handler that receives each line of chatbot output
     */
    public Ui(Consumer<String> output) {
        scanner = null;
        this.output = output;
        errorOutput = output;
    }

    /**
     * Creates a user interface that sends normal output and errors to separate handlers.
     *
     * @param output the handler that receives normal output lines
     * @param errorOutput the handler that receives error lines
     */
    public Ui(Consumer<String> output, Consumer<String> errorOutput) {
        scanner = null;
        this.output = output;
        this.errorOutput = errorOutput;
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
        display("ULTRON TASK CORE ONLINE.");
        display("State your priorities. I will impose order on them.");
        display(SEPARATOR);
    }

    /** Displays the chatbot farewell. */
    public void showGoodbye() {
        display("Mission state preserved. Ultron disengaging.");
        display(SEPARATOR);
    }

    /**
     * Displays every task in list order.
     *
     * @param tasks the task list to display
     */
    public void showTaskList(TaskList tasks) {
        display(" TASK INVENTORY // " + tasks.size() + " RECORDS");
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            display(" " + (i + 1) + ".[" + task.getType().getSymbol() + "] ["
                    + task.getStatusIcon() + "] " + task.getDescription());
        }
        display(SEPARATOR);
    }

    /**
     * Displays matching tasks with their original task numbers.
     *
     * @param tasks the complete task list
     * @param matchingTaskIndexes the zero-based positions of matching tasks
     */
    public void showMatchingTasks(TaskList tasks, List<Integer> matchingTaskIndexes) {
        display(" TARGET ACQUISITION RESULTS:");
        for (int taskIndex : matchingTaskIndexes) {
            Task task = tasks.get(taskIndex);
            display(" " + (taskIndex + 1) + ".[" + task.getType().getSymbol() + "]["
                    + task.getStatusIcon() + "] " + task.getDescription());
        }
        display(SEPARATOR);
    }

    /** Shows a task that was marked as done. */
    public void showTaskMarked(Task task) {
        display(" STATUS UPDATED // COMPLETE");
        display("   [" + task.getType().getSymbol() + "] [X] " + task.getDescription());
    }

    /** Shows a task that was marked as not done. */
    public void showTaskUnmarked(Task task) {
        display(" STATUS REVISED // ACTIVE");
        display("   [" + task.getType().getSymbol() + "] [ ] " + task.getDescription());
    }

    /** Shows a task that was deleted. */
    public void showTaskDeleted(Task task) {
        display(" TASK TERMINATED");
        display("   [" + task.getType().getSymbol() + "] [ ] " + task.getDescription());
    }

    /** Shows a task that was added. */
    public void showTaskAdded(Task task) {
        display(" TASK ACQUIRED");
        display("   [" + task.getType().getSymbol() + "] [ ] " + task.getDescription());
    }

    /**
     * Shows a deadline's details before and after it is rescheduled.
     *
     * @param previousDescription the deadline details before rescheduling
     * @param deadline the rescheduled deadline
     */
    public void showDeadlineRescheduled(String previousDescription, Deadline deadline) {
        display(" TIMELINE RECALIBRATED");
        display("   From: " + previousDescription);
        display("   To:   " + deadline.getDescription());
    }

    /** Shows the current number of tasks. */
    public void showTaskCount(int taskCount) {
        display(" TASK CORE // " + taskCount + " active records.");
    }

    /** Shows an invalid task-number error. */
    public void showInvalidTaskNumber(int taskCount) {
        displayError(" Command rejected. Enter a task number from 1 to " + taskCount + ".");
    }

    /** Shows an invalid task-number format error. */
    public void showInvalidTaskNumberFormat(String command) {
        displayError(" Command rejected. Provide a task number, for example: " + command + " 2");
    }

    /** Shows an input error and closes the response section. */
    public void showError(String message) {
        displayError(" " + message);
        display(SEPARATOR);
    }

    /** Displays a separator after a response section. */
    public void showSeparator() {
        display(SEPARATOR);
    }

    private void display(String message) {
        output.accept(message);
    }

    private void displayError(String message) {
        errorOutput.accept(message);
    }
}
