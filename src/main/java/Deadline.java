public class Deadline extends Task {
    protected String deadline;
    public Deadline(String input) throws UltronException {
        super(input, TaskType.DEADLINE);
        String[] inputs = input.split(" /by ");
        if  (inputs.length != 2) {
            throw new UltronException("You FOOL! Your formatting is WRONG! Example input: deadline return book /by Sunday.");
        }
        this.description = inputs[0];
        this.deadline = inputs[1];
    }

    @Override
    public String getDescription() {
        return this.description + "( by: " + this.deadline + ')';
    }
}
