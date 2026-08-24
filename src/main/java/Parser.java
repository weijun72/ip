/**
 * Interprets raw user input as a chatbot command and its arguments.
 */
public class Parser {

    /** Supported commands that can be entered by the user. */
    public enum Command {
        BYE, LIST, MARK, UNMARK, DELETE, TODO, DEADLINE, EVENT, UNKNOWN
    }

    /**
     * Identifies the command represented by the input.
     *
     * @param input the raw user input
     * @return the matching command, or {@code UNKNOWN} when no command matches
     */
    public Command parseCommand(String input) {
        if (input.equals("bye")) {
            return Command.BYE;
        } else if (input.equals("list")) {
            return Command.LIST;
        } else if (input.startsWith("mark ")) {
            return Command.MARK;
        } else if (input.startsWith("unmark ")) {
            return Command.UNMARK;
        } else if (input.startsWith("delete ")) {
            return Command.DELETE;
        } else if (input.equals("todo") || input.startsWith("todo ")) {
            return Command.TODO;
        } else if (input.startsWith("deadline ")) {
            return Command.DEADLINE;
        } else if (input.startsWith("event ")) {
            return Command.EVENT;
        }
        return Command.UNKNOWN;
    }

    /**
     * Extracts the text following a command word.
     *
     * @param input the raw user input
     * @param command the command already identified from the input
     * @return the trimmed argument text
     */
    public String getArgument(String input, Command command) {
        return input.substring(getCommandWord(command).length()).trim();
    }

    /**
     * Parses a task number supplied after a task-changing command.
     *
     * @param input the raw user input
     * @param command the mark, unmark, or delete command
     * @return the parsed task number
     * @throws NumberFormatException if the argument is not a whole number
     */
    public int parseTaskNumber(String input, Command command) {
        return Integer.parseInt(getArgument(input, command));
    }

    private String getCommandWord(Command command) {
        return switch (command) {
        case BYE -> "bye";
        case LIST -> "list";
        case MARK -> "mark";
        case UNMARK -> "unmark";
        case DELETE -> "delete";
        case TODO -> "todo";
        case DEADLINE -> "deadline";
        case EVENT -> "event";
        case UNKNOWN -> "";
        };
    }
}
