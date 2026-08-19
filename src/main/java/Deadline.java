public class Deadline extends Task {
    protected char type;
    protected String deadline;
    public Deadline(String input) {
        String[] inputs = input.split(" /by ");
        super(inputs[0]);
        this.deadline = inputs[1];
        this.type = 'D';
    }

    @Override
    public char getType() {
        return this.type;
    }

    @Override
    public String getDescription() {
        return this.description + "( by: " + this.deadline + ')';
    }
}
