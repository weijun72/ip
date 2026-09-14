package ultron.ui;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import ultron.model.TaskList;
import ultron.model.Todo;

/** Tests output routing and key text shown by the user interface. */
class UiTest {
    @Test
    void showTaskList_andShowMatchingTasks_writeExpectedTaskInformation() {
        List<String> output = new ArrayList<>();
        Ui ui = new Ui(output::add);
        TaskList tasks = new TaskList(new Todo("read book"));

        ui.showTaskList(tasks);
        ui.showMatchingTasks(tasks, List.of(0));

        assertTrue(output.contains(" TASK INVENTORY // 1 RECORDS"));
        assertTrue(output.contains(" 1.[T] [ ] read book"));
        assertTrue(output.contains(" TARGET ACQUISITION RESULTS:"));
    }

    @Test
    void invalidTaskNumber_writesOnlyToErrorOutput() {
        List<String> output = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        Ui ui = new Ui(output::add, errors::add);

        ui.showInvalidTaskNumber(3);

        assertTrue(output.isEmpty());
        assertTrue(errors.getFirst().contains("1 to 3"));
    }
}
