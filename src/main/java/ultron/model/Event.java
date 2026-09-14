package ultron.model;

import ultron.exception.UltronException;

/**
 * Represents a task scheduled between a start and end time.
 */
public class Event extends Task {
    /** Message shown when an event command is not formatted correctly. */
    private static final String INVALID_FORMAT_MESSAGE = "You FOOL! Your formatting is WRONG! "
            + "Example input: event project meeting /from Mon 2pm /to 4pm.";

    protected String start;
    protected String end;

    /**
     * Creates an event task from its command argument.
     *
     * @param input the description followed by {@code /from} and {@code /to} times.
     * @throws UltronException if the event is not formatted correctly.
     */
    public Event(String input) throws UltronException {
        super(input, TaskType.EVENT);
        String[] inputs = input.trim().split("\\s+/from\\s+|\\s+/to\\s+", -1);
        if (inputs.length != 3 || inputs[0].isBlank() || inputs[1].isBlank() || inputs[2].isBlank()
                || input.trim().split("\\s+/from\\s+", -1).length != 2
                || input.trim().split("\\s+/to\\s+", -1).length != 2
                || !input.trim().matches(".+\\s+/from\\s+.+\\s+/to\\s+.+")) {
            throw new UltronException(INVALID_FORMAT_MESSAGE);
        }
        this.description = inputs[0].trim();
        this.start = inputs[1].trim();
        this.end = inputs[2].trim();
    }

    @Override
    public String getDescription() {
        return this.description + "( from: " + this.start + " to: " + this.end + ')';
    }
}
