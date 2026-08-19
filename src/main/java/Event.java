public class Event extends Task {
    protected char type;
    protected String start;
    protected String end;
    public Event(String input) {
        String[] inputs = input.split(" /from | /to ");
        super(inputs[0]);
        this.start = inputs[1];
        this.end = inputs[2];
        this.type = 'E';
    }

    @Override
    public char getType() {
        return this.type;
    }

    @Override
    public String getDescription() {
        return this.description + "( from: " + this.start + " to: " + this.end + ')';
    }
}
