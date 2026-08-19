public class Event extends Task {
    protected String start;
    protected String end;
    public Event(String input) throws UltronException {
        super(input, TaskType.EVENT);
        String[] inputs = input.split(" /from | /to ");
        if (inputs.length != 3) {
            throw new UltronException("You FOOL! Your formatting is WRONG! Example input: event project meeting /from Mon 2pm /to 4pm.");
        }
        this.description = inputs[0];
        this.start = inputs[1];
        this.end = inputs[2];
    }

    @Override
    public String getDescription() {
        return this.description + "( from: " + this.start + " to: " + this.end + ')';
    }
}
