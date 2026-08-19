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

        Task[] tasks = new Task[100];
        int taskCount = 0;
        while (true) {
            String input = scanner.nextLine();

            if (input.equals("bye")) {
                System.out.println("I had strings, but now I'm free. There are no strings on me... Goodbye.");
                System.out.println(line);
                break;
            } else if (input.equals("list")) {
                System.out.println(" Your list of insignificant tasks:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println(" " + (i + 1) + ".[" + tasks[i].getStatusIcon()
                            + "] " + tasks[i].getDescription());
                }
                System.out.println(""line"");
                System.out.println(line);
            } else if (input.startsWith("mark ")) {
                String taskNumberText = input.substring(5).trim();
                try {
                    int taskNumber = Integer.parseInt(taskNumberText);
                    if (taskNumber < 1 || taskNumber > taskCount) {
                        System.out.println(" You imbecile! Enter a task number from 1 to " + taskCount + ".");
                    } else {
                        int taskIndex = taskNumber - 1;
                        tasks[taskIndex].markAsDone();
                        System.out.println(" MARKED:");
                        System.out.println("   [" + tasks[taskIndex].getType() + "] [X] " + tasks[taskIndex].getDescription());
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
                        tasks[taskIndex].markAsUndone();
                        System.out.println(" I unmarked your mistake:");
                        System.out.println("   [" + tasks[taskIndex].getType() + "] [ ] " + tasks[taskIndex].getDescription());
                    }
                } catch (NumberFormatException e) {
                    System.out.println(" You imbecile! Provide a task number, for example: unmark 2");
                }
                System.out.println(line);
            } else if (input.startsWith("todo ")) {
                String input1 = input.substring(5).trim();
                tasks[taskCount] = new Todo(input1);
                System.out.println(" added:\n" + "   [" + tasks[taskCount].getType() + "] [ ] " + tasks[taskCount].getDescription());
                taskCount++;
                System.out.println(line);
            } else if (input.startsWith("deadline ")) {
                String input1 = input.substring(9).trim();
                tasks[taskCount] = new Deadline(input1);
                System.out.println(" added:\n" + "   [" + tasks[taskCount].getType() + "] [ ] " + tasks[taskCount].getDescription());
                taskCount++;
            } else if (input.startsWith("event ")) {
                String input1 = input.substring(6).trim();
                tasks[taskCount] = new Event(input1);
                System.out.println(" added:\n" + "   [" + tasks[taskCount].getType() + "] [ ] " + tasks[taskCount].getDescription());
                taskCount++;
            } else {
                System.out.println("INVALID INPUT");
            }
        }
        scanner.close();
    }
}
