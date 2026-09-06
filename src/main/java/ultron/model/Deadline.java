package ultron.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Objects;

import ultron.exception.UltronException;

/**
 * Represents a task that must be completed by a specified date and optional time.
 */
public class Deadline extends Task {
    /** Message shown when a deadline command is not formatted correctly. */
    private static final String INVALID_FORMAT_MESSAGE = "You FOOL! Your formatting is WRONG! "
            + "Example input: deadline return book /by 2/12/2019 1800.";
    /** Message shown when a rescheduled date is before today. */
    private static final String PAST_DATE_MESSAGE = "You FOOL! A rescheduled deadline cannot be in the past.";
    /** Message shown when a replacement time is not a 24-hour {@code HHmm} value. */
    private static final String INVALID_TIME_MESSAGE = "You FOOL! Use a 24-hour time in HHmm format.";
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HHmm")
            .withResolverStyle(ResolverStyle.STRICT);

    protected LocalDate date;
    protected String time;

    /**
     * Creates a deadline task from its command argument.
     *
     * @param input the description followed by {@code /by}, a date, and an optional time.
     * @throws UltronException if the deadline is not formatted correctly.
     */
    public Deadline(String input) throws UltronException {
        super(input, TaskType.DEADLINE);
        String[] inputs = input.split(" /by ");
        if (inputs.length != 2) {
            throw new UltronException(INVALID_FORMAT_MESSAGE);
        }
        this.description = inputs[0];
        String[] deadline = inputs[1].split(" ");
        String dateString;
        if (deadline.length == 2) {
            this.time = " " + deadline[1];
            dateString = deadline[0];
        } else if (deadline.length == 1) {
            this.time = "";
            dateString = deadline[0];
        } else {
            throw new UltronException(INVALID_FORMAT_MESSAGE);
        }
        try {
            this.date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("d/MM/yyyy"));
        } catch (DateTimeParseException e) {
            throw new UltronException(INVALID_FORMAT_MESSAGE);
        }
    }

    @Override
    public String getDescription() {
        String formattedDate = this.date.format(DateTimeFormatter.ofPattern("dd/MMM/yyyy"));
        return this.description + "( by: " + formattedDate + this.time + " )";
    }

    /**
     * Returns the current due date.
     *
     * @return the due date without the optional time
     */
    public LocalDate getDueDate() {
        return date;
    }

    /**
     * Reschedules this deadline to a date that is not in the past.
     *
     * <p>When {@code replacementTime} is {@code null}, the existing optional time is retained.</p>
     *
     * @param newDate the new due date
     * @param replacementTime an optional replacement 24-hour {@code HHmm} time
     * @throws UltronException if the date is in the past or the replacement time is invalid
     */
    public void reschedule(LocalDate newDate, String replacementTime) throws UltronException {
        Objects.requireNonNull(newDate);
        if (newDate.isBefore(LocalDate.now())) {
            throw new UltronException(PAST_DATE_MESSAGE);
        }
        if (replacementTime != null && !isValidTime(replacementTime)) {
            throw new UltronException(INVALID_TIME_MESSAGE);
        }

        date = newDate;
        if (replacementTime != null) {
            time = " " + replacementTime;
        }
    }

    private boolean isValidTime(String candidateTime) {
        try {
            LocalTime.parse(candidateTime, TIME_FORMAT);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
