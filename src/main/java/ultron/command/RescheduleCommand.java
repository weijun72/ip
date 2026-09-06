package ultron.command;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.OptionalInt;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ultron.exception.UltronException;
import ultron.model.Deadline;
import ultron.model.Task;
import ultron.model.TaskList;
import ultron.storage.Storage;
import ultron.ui.Ui;

/**
 * Reschedules an incomplete deadline to an absolute or relative date.
 */
public class RescheduleCommand extends Command {
    private static final String INVALID_FORMAT_MESSAGE = "You FOOL! Use: reschedule <task number> "
            + "/by d/MM/yyyy [HHmm] or reschedule <task number> <positive days>d.";
    private static final String NON_DEADLINE_MESSAGE = "You FOOL! Only deadlines can be rescheduled.";
    private static final String COMPLETED_DEADLINE_MESSAGE = "You FOOL! Completed deadlines cannot be rescheduled.";
    private static final Pattern ABSOLUTE_ARGUMENT_PATTERN = Pattern.compile(
            "(\\S+) /by (\\d{1,2}/\\d{1,2}/\\d{4})(?: (\\S+))?");
    private static final Pattern RELATIVE_ARGUMENT_PATTERN = Pattern.compile("(\\S+) ([1-9]\\d*)d");
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("d/MM/uuuu")
            .withResolverStyle(ResolverStyle.STRICT);

    private final String arguments;

    /**
     * Creates a reschedule command with its task-number and date arguments.
     *
     * @param arguments the text following the command word
     */
    public RescheduleCommand(String arguments) {
        this.arguments = arguments;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws UltronException {
        Matcher absoluteMatcher = ABSOLUTE_ARGUMENT_PATTERN.matcher(arguments);
        Matcher relativeMatcher = RELATIVE_ARGUMENT_PATTERN.matcher(arguments);
        String taskNumberText = getTaskNumberText(absoluteMatcher, relativeMatcher);
        OptionalInt taskIndex = getTaskIndex(taskNumberText, tasks, ui, "reschedule");
        if (taskIndex.isEmpty()) {
            ui.showSeparator();
            return;
        }

        Task task = tasks.get(taskIndex.getAsInt());
        if (!(task instanceof Deadline deadline)) {
            throw new UltronException(NON_DEADLINE_MESSAGE);
        }
        if (deadline.isDone()) {
            throw new UltronException(COMPLETED_DEADLINE_MESSAGE);
        }

        String previousDescription = deadline.getDescription();
        rescheduleDeadline(deadline, absoluteMatcher, relativeMatcher);
        storage.saveTasks(tasks.getTasks());
        ui.showDeadlineRescheduled(previousDescription, deadline);
        ui.showSeparator();
    }

    private String getTaskNumberText(Matcher absoluteMatcher, Matcher relativeMatcher) throws UltronException {
        if (absoluteMatcher.matches() || relativeMatcher.matches()) {
            return absoluteMatcher.matches() ? absoluteMatcher.group(1) : relativeMatcher.group(1);
        }
        throw new UltronException(INVALID_FORMAT_MESSAGE);
    }

    private void rescheduleDeadline(Deadline deadline, Matcher absoluteMatcher, Matcher relativeMatcher)
            throws UltronException {
        try {
            if (absoluteMatcher.matches()) {
                LocalDate newDate = LocalDate.parse(absoluteMatcher.group(2), DATE_FORMAT);
                deadline.reschedule(newDate, absoluteMatcher.group(3));
                return;
            }
            long days = Long.parseLong(relativeMatcher.group(2));
            deadline.reschedule(deadline.getDueDate().plusDays(days), null);
        } catch (DateTimeException | NumberFormatException e) {
            throw new UltronException(INVALID_FORMAT_MESSAGE);
        }
    }
}
