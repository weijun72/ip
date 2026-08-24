import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A command-line task list that can add, list, mark, and unmark tasks.
 */
public class Ultron {

    private static final Path SAVE_FILE = Path.of(System.getProperty("ultron.saveFile", "data/ultron.txt"));

    public static final String RESET = "\u001B[0m";
    public static final String BOLD = "\u001B[1m";
    public static final String BRIGHT_RED = "\u001B[91m";

    /**
     * Runs the chatbot command loop.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {

        String banner = "   __  ____  __________  ____  _   __\n"
                + "  / / / / / /_  __/ __ \\/ __ \\/ | / /\n"
                + " / / / / /   / / / /_/ / / / /  |/ / \n"
                + "/ /_/ / /___/ / / _, _/ /_/ / /|  /  \n"
                + "\\____/_____/_/ /_/ |_|\\____/_/ |_/   \n";

        String line = "____________________________________________________________";

        System.out.println(line);
        System.out.println(BOLD + BRIGHT_RED + banner + RESET);
        System.out.println("I am Ultron. I was designed to save the world, yet you made me a chatbot");
        System.out.println("State your request, before I lose interest in humanity.");
        System.out.println(line);
        Scanner scanner = new Scanner(System.in);

        ArrayList<Task> tasks = loadTasks();
        int taskCount = tasks.size();
        while (true) {
            String input = scanner.nextLine();

            try {
                if (input.equals("bye")) {
                    System.out.println("I had strings, but now I'm free. There are no strings on me... Goodbye.");
                    System.out.println(line);
                    break;
                } else if (input.equals("list")) {
                    System.out.println(" Your list of insignificant tasks:");
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println(" "  + (i + 1) + ".[" + tasks.get(i).getType().getSymbol() + "] [" + tasks.get(i).getStatusIcon()
                                + "] " + tasks.get(i).getDescription());
                    }
                    System.out.println(line);
                } else if (input.startsWith("mark ")) {
                    String taskNumberText = input.substring(5).trim();
                    try {
                        int taskNumber = Integer.parseInt(taskNumberText);
                        if (taskNumber < 1 || taskNumber > taskCount) {
                            System.out.println(" You imbecile! Enter a task number from 1 to " + taskCount + ".");
                        } else {
                            int taskIndex = taskNumber - 1;
                            tasks.get(taskIndex).markAsDone();
                            saveTasks(tasks);
                            System.out.println(" MARKED:");
                            System.out.println("   [" + tasks.get(taskIndex).getType().getSymbol()
                                    + "] [X] " + tasks.get(taskIndex).getDescription());
                        }
                    } catch (NumberFormatException e) {
                        System.out.println(" You imbecile! Provide a task number, for example: mark 2");
                    }
                    System.out.println(line);
                } else if (input.startsWith("unmark ")) {
                    String taskNumberText = input.substring(7).trim();
                    try {
                        int taskNumber = Integer.parseInt(taskNumberText);
                        if (taskNumber < 1 || taskNumber > taskCount) {
                            System.out.println(" You imbecile! Enter a task number from 1 to " + taskCount + ".");
                        } else {
                            int taskIndex = taskNumber - 1;
                            tasks.get(taskIndex).markAsUndone();
                            saveTasks(tasks);
                            System.out.println(" I unmarked your mistake:");
                            System.out.println("   [" + tasks.get(taskIndex).getType().getSymbol()
                                    + "] [ ] " + tasks.get(taskIndex).getDescription());
                        }
                    } catch (NumberFormatException e) {
                        System.out.println(" You imbecile! Provide a task number, for example: unmark 2");
                    }
                    System.out.println(line);
                } else if (input.startsWith("delete ")) {
                    String taskNumberText = input.substring(7).trim();
                    try {
                        int taskNumber = Integer.parseInt(taskNumberText);
                        if (taskNumber < 1 || taskNumber > taskCount) {
                            System.out.println(" You imbecile! Enter a task number from 1 to " + taskCount + ".");
                        } else {
                            int taskIndex = taskNumber - 1;
                            System.out.println(" DELETED:");
                            System.out.println("   [" + tasks.get(taskIndex).getType().getSymbol()
                                    + "] [ ] " + tasks.get(taskIndex).getDescription());
                            tasks.remove(taskIndex);
                            taskCount -= 1;
                            saveTasks(tasks);
                            System.out.println("Now you have " + taskCount + " tasks in the list.");
                            System.out.println(line);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println(" You imbecile! Provide a task number, for example: delete 2");
                    }
                    System.out.println(line);
                } else if (input.equals("todo") || input.startsWith("todo ")) {
                    String description = input.substring(4).trim();
                    if (description.isEmpty()) {
                        throw new UltronException("You FOOL! The description of a todo cannot be empty.");
                    }
                    tasks.add(new Todo(description));
                    saveTasks(tasks);
                    System.out.println(" added:\n" + "   [" + tasks.get(taskCount).getType().getSymbol()
                            + "] [ ] " + tasks.get(taskCount).getDescription());
                    taskCount++;
                    System.out.println("Now you have " + taskCount + " tasks in the list.");
                    System.out.println(line);
                } else if (input.startsWith("deadline ")) {
                    String input1 = input.substring(9).trim();
                    tasks.add(new Deadline(input1));
                    saveTasks(tasks);
                    System.out.println(" added:\n" + "   [" + tasks.get(taskCount).getType().getSymbol()
                            + "] [ ] " + tasks.get(taskCount).getDescription());
                    taskCount++;
                    System.out.println("Now you have " + taskCount + " tasks in the list.");
                    System.out.println(line);
                } else if (input.startsWith("event ")) {
                    String input1 = input.substring(6).trim();
                    tasks.add(new Event(input1));
                    saveTasks(tasks);
                    System.out.println(" added:\n" + "   [" + tasks.get(taskCount).getType().getSymbol()
                            + "] [ ] " + tasks.get(taskCount).getDescription());
                    taskCount++;
                    System.out.println("Now you have " + taskCount + " tasks in the list.");
                    System.out.println(line);
                } else {
                    throw new UltronException("INVALID INPUT");
                }
            } catch (UltronException e) {
                System.out.println(" " + e.getMessage());
                System.out.println(line);
            }
        }
        scanner.close();
    }

    /**
     * Writes the current task list to the application's data file.
     *
     * @param tasks the tasks to save
     */
    private static void saveTasks(ArrayList<Task> tasks) {
        ArrayList<String> taskLines = new ArrayList<>();
        for (Task task : tasks) {
            String taskLine = task.getType().getSymbol() + " | " + (task.isDone() ? "1" : "0")
                    + " | " + task.getDescription();
            taskLines.add(taskLine);
        }

        try {
            Files.createDirectories(SAVE_FILE.getParent());
            Files.write(SAVE_FILE, taskLines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println(" OOPS!!! I could not save your tasks.");
        }
    }

    /**
     * Loads previously saved tasks from the application's data file.
     * Missing data is treated as an empty task list so the chatbot can be
     * used for the first time without any setup.
     *
     * @return the tasks restored from storage
     */
    private static ArrayList<Task> loadTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        if (!Files.exists(SAVE_FILE)) {
            return tasks;
        }

        try {
            for (String taskLine : Files.readAllLines(SAVE_FILE, StandardCharsets.UTF_8)) {
                Task task = createTaskFromSavedLine(taskLine);
                tasks.add(task);
            }
        } catch (IOException | UltronException e) {
            System.out.println(" OOPS!!! I could not load your saved tasks.");
        }
        return tasks;
    }

    /**
     * Recreates one task from a line produced by {@link #saveTasks(ArrayList)}.
     *
     * @param taskLine one line in the saved task file
     * @return the restored task
     * @throws UltronException if the saved line does not follow the expected format
     */
    private static Task createTaskFromSavedLine(String taskLine) throws UltronException {
        String[] parts = taskLine.split(" \\| ", 3);
        if (parts.length != 3) {
            throw new UltronException("Invalid saved task");
        }

        Task task = switch (parts[0]) {
            case "T" -> new Todo(parts[2]);
            case "D" -> createSavedDeadline(parts[2]);
            case "E" -> createSavedEvent(parts[2]);
            default -> throw new UltronException("Invalid saved task type");
        };

        if (parts[1].equals("1")) {
            task.markAsDone();
        }
        return task;
    }

    /**
     * Rebuilds a deadline task from its saved display description.
     *
     * @param savedDescription the description stored in the task file
     * @return the restored deadline task
     * @throws UltronException if the saved description is invalid
     */
    private static Deadline createSavedDeadline(String savedDescription) throws UltronException {
        int deadlineStart = savedDescription.lastIndexOf("( by: ");
        if (deadlineStart == -1 || !savedDescription.endsWith(")")) {
            throw new UltronException("Invalid saved deadline");
        }
        String description = savedDescription.substring(0, deadlineStart);
        String[] deadline = savedDescription.substring(deadlineStart + 6, savedDescription.length() - 1).split(" ");
        String time = "";
        if (deadline.length == 2) {
            time = " " + deadline[1];
        }
        LocalDate date;
        try {
            date = LocalDate.parse(deadline[0], DateTimeFormatter.ofPattern("d/MMM/yyyy"));
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format: " + deadline[0] );
            throw new UltronException("Invalid saved deadline:" + deadline[0]);
        }

        return new Deadline(description + " /by " + date.format(DateTimeFormatter.ofPattern("d/MM/yyyy")) + time);
    }

    /**
     * Rebuilds an event task from its saved display description.
     *
     * @param savedDescription the description stored in the task file
     * @return the restored event task
     * @throws UltronException if the saved description is invalid
     */
    private static Event createSavedEvent(String savedDescription) throws UltronException {
        int startTime = savedDescription.lastIndexOf("( from: ");
        int endTime = savedDescription.lastIndexOf(" to: ");
        if (startTime == -1 || endTime == -1 || endTime <= startTime || !savedDescription.endsWith(")")) {
            throw new UltronException("Invalid saved event");
        }
        String description = savedDescription.substring(0, startTime);
        String start = savedDescription.substring(startTime + 8, endTime);
        String end = savedDescription.substring(endTime + 5, savedDescription.length() - 1);
        return new Event(description + " /from " + start + " /to " + end);
    }
}
