package ultron.parser;

import ultron.command.Command;
import ultron.command.DeleteCommand;
import ultron.command.ExitCommand;
import ultron.command.FindCommand;
import ultron.command.ListCommand;
import ultron.command.MarkCommand;
import ultron.command.RescheduleCommand;
import ultron.command.UnmarkCommand;

/**
 * Interprets raw user input as a chatbot command and its arguments.
 */
public class Parser {

    /** Supported commands that can be entered by the user. */
    public enum CommandType {
        BYE, LIST, MARK, UNMARK, DELETE, FIND, RESCHEDULE, TODO, DEADLINE, EVENT, UNKNOWN
    }

    /**
     * Identifies the command represented by the input.
     *
     * @param input the raw user input
     * @return the matching command, or {@code UNKNOWN} when no command matches
     */
    public CommandType parseCommand(String input) {
        String trimmedInput = input.trim();
        String commandWord = trimmedInput.split("\\s+", 2)[0];
        return switch (commandWord) {
            case "bye" -> trimmedInput.equals("bye") ? CommandType.BYE : CommandType.UNKNOWN;
            case "list" -> trimmedInput.equals("list") ? CommandType.LIST : CommandType.UNKNOWN;
            case "mark" -> CommandType.MARK;
            case "unmark" -> CommandType.UNMARK;
            case "delete" -> CommandType.DELETE;
            case "find" -> CommandType.FIND;
            case "reschedule" -> CommandType.RESCHEDULE;
            case "todo" -> CommandType.TODO;
            case "deadline" -> CommandType.DEADLINE;
            case "event" -> CommandType.EVENT;
            default -> CommandType.UNKNOWN;
        };
    }

    /**
     * Creates a command object for each command migrated to the command pattern.
     *
     * @param input the raw user input
     * @return an executable command, or {@code null} when the command is not migrated yet
     */
    public Command parseCommandObject(String input) {
        CommandType commandType = parseCommand(input);
        return switch (commandType) {
            case BYE -> new ExitCommand();
            case LIST -> new ListCommand();
            case MARK -> new MarkCommand(getArgument(input, commandType));
            case UNMARK -> new UnmarkCommand(getArgument(input, commandType));
            case DELETE -> new DeleteCommand(getArgument(input, commandType));
            case FIND -> new FindCommand(getArgument(input, commandType));
            case RESCHEDULE -> new RescheduleCommand(getArgument(input, commandType));
            default -> null;
        };
    }

    /**
     * Extracts the text following a command word.
     *
     * @param input the raw user input
     * @param command the command already identified from the input
     * @return the trimmed argument text
     */
    public String getArgument(String input, CommandType command) {
        assert command != CommandType.UNKNOWN : "An unknown command has no command word";
        String trimmedInput = input.trim();
        return trimmedInput.substring(getCommandWord(command).length()).trim();
    }

    private String getCommandWord(CommandType command) {
        return switch (command) {
            case BYE -> "bye";
            case LIST -> "list";
            case MARK -> "mark";
            case UNMARK -> "unmark";
            case DELETE -> "delete";
            case FIND -> "find";
            case RESCHEDULE -> "reschedule";
            case TODO -> "todo";
            case DEADLINE -> "deadline";
            case EVENT -> "event";
            case UNKNOWN -> "";
        };
    }
}
