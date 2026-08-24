package ultron.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import ultron.exception.UltronException;

public class Deadline extends Task {
    protected LocalDate date;
    protected String time;
    public Deadline(String input) throws UltronException {
        super(input, TaskType.DEADLINE);
        String[] inputs = input.split(" /by ");
        if  (inputs.length != 2) {
            throw new UltronException("You FOOL! Your formatting is WRONG! Example input: deadline return book /by 2/12/2019 1800.");
        }
        this.description = inputs[0];
        String[] deadline = inputs[1].split(" ");
        String dateString = "";
        if (deadline.length == 2) {
            this.time = " " + deadline[1];
            dateString = deadline[0];
        } else if (deadline.length == 1) {
            this.time = "";
            dateString = deadline[0];
        } else {
            throw new UltronException("You FOOL! Your formatting is WRONG! Example input: deadline return book /by 2/12/2019 1800.");
        }
        try {
            this.date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("d/MM/yyyy"));
        } catch (DateTimeParseException e) {
            System.out.println(dateString);
            throw new UltronException("You FOOL! Your formatting is WRONG! Example input: deadline return book /by 2/12/2019 1800.");
        }
    }

    @Override
    public String getDescription() {
        return this.description + "( by: " + this.date.format(DateTimeFormatter.ofPattern("dd/MMM/yyyy")) + this.time + " )";
    }
}
