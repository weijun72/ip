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

        String[] tasks = new String[100];
        boolean[] isDone = new boolean[100];
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
                    String status = isDone[i] ? "[X]" : "[ ]";
                    System.out.println(" " + (i + 1) + "." + status + " " + tasks[i]);
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
                        isDone[taskIndex] = true;
                        System.out.println(" MARKED:");
                        System.out.println("   [X] " + tasks[taskIndex]);
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
                        isDone[taskIndex] = false;
                        System.out.println(" I unmarked your mistake:");
                        System.out.println("   [ ] " + tasks[taskIndex]);
                    }
                } catch (NumberFormatException e) {
                    System.out.println(" You imbecile! Provide a task number, for example: unmark 2");
                }
                System.out.println(line);
            } else {
                tasks[taskCount] = input;
                taskCount++;
                System.out.println(" added: " + input);
                System.out.println(line);
            }
        }
        scanner.close();
    }
}
