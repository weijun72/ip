import java.util.Scanner;

/**
 * A command-line task list that can add, list, mark, and unmark tasks.
 */
public class Ultron {

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

        Storage storage = new Storage(System.getProperty("ultron.saveFile", "data/ultron.txt"));
        TaskList tasks = new TaskList(storage.loadTasks());
        while (true) {
            String input = scanner.nextLine();

            try {
                if (input.equals("bye")) {
                    System.out.println("I had strings, but now I'm free. There are no strings on me... Goodbye.");
                    System.out.println(line);
                    break;
                } else if (input.equals("list")) {
                    System.out.println(" Your list of insignificant tasks:");
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println(" "  + (i + 1) + ".[" + tasks.get(i).getType().getSymbol() + "] [" + tasks.get(i).getStatusIcon()
                                + "] " + tasks.get(i).getDescription());
                    }
                    System.out.println(line);
                } else if (input.startsWith("mark ")) {
                    String taskNumberText = input.substring(5).trim();
                    try {
                        int taskNumber = Integer.parseInt(taskNumberText);
                        if (taskNumber < 1 || taskNumber > tasks.size()) {
                            System.out.println(" You imbecile! Enter a task number from 1 to " + tasks.size() + ".");
                        } else {
                            int taskIndex = taskNumber - 1;
                            tasks.get(taskIndex).markAsDone();
                            storage.saveTasks(tasks.getTasks());
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
                        if (taskNumber < 1 || taskNumber > tasks.size()) {
                            System.out.println(" You imbecile! Enter a task number from 1 to " + tasks.size() + ".");
                        } else {
                            int taskIndex = taskNumber - 1;
                            tasks.get(taskIndex).markAsUndone();
                            storage.saveTasks(tasks.getTasks());
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
                        if (taskNumber < 1 || taskNumber > tasks.size()) {
                            System.out.println(" You imbecile! Enter a task number from 1 to " + tasks.size() + ".");
                        } else {
                            int taskIndex = taskNumber - 1;
                            System.out.println(" DELETED:");
                            System.out.println("   [" + tasks.remove(taskIndex).getType().getSymbol()
                                    + "] [ ] " + tasks.get(taskIndex).getDescription());
                            storage.saveTasks(tasks.getTasks());
                            System.out.println("Now you have " + tasks.size() + " tasks in the list.");
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
                    storage.saveTasks(tasks.getTasks());
                    System.out.println(" added:\n" + "   [" + tasks.get(tasks.size() - 1).getType().getSymbol()
                            + "] [ ] " + tasks.get(tasks.size() - 1).getDescription());
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                    System.out.println(line);
                } else if (input.startsWith("deadline ")) {
                    String input1 = input.substring(9).trim();
                    tasks.add(new Deadline(input1));
                    storage.saveTasks(tasks.getTasks());
                    System.out.println(" added:\n" + "   [" + tasks.get(tasks.size() - 1).getType().getSymbol()
                            + "] [ ] " + tasks.get(tasks.size() - 1).getDescription());
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                    System.out.println(line);
                } else if (input.startsWith("event ")) {
                    String input1 = input.substring(6).trim();
                    tasks.add(new Event(input1));
                    storage.saveTasks(tasks.getTasks());
                    System.out.println(" added:\n" + "   [" + tasks.get(tasks.size() - 1).getType().getSymbol()
                            + "] [ ] " + tasks.get(tasks.size() - 1).getDescription());
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
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

}
