public class Deadline extends Task {
    protected char type;
    protected String deadline;
    public Deadline(String input) throws UltronException {
        String[] inputs = input.split(" /by ");
        if  (inputs.length != 2) {
            throw new UltronException("You FOOL! Your formatting is WRONG! Example input: deadline return book /by Sunday.");
        }
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
