import java.util.Scanner;

public class Ultron {


    public static final String RESET = "\u001B[0m";
    public static final String BOLD = "\u001B[1m";
    public static final String BRIGHT_RED = "\u001B[91m";

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

        while (true) {
            String input = scanner.nextLine();

            if (input.equals("bye")) {
                System.out.println("I had strings, but now I'm free. There are no strings on me... Goodbye.");
                System.out.println(line);
                break;
            }

            System.out.println(" " + input);
            System.out.println(line);
        }

        scanner.close();
    }
}