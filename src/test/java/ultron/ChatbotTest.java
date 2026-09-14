package ultron;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import ultron.ui.Ui;

/** Tests chatbot command processing without launching the console or GUI. */
class ChatbotTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void processCommand_taskCommands_updatesTaskListAndShowsOutput() {
        Chatbot chatbot = chatbot();
        List<String> output = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        Ui ui = new Ui(output::add, errors::add);

        chatbot.processCommand("todo read book", ui);
        chatbot.processCommand("deadline submit report /by 2/12/2099 1800", ui);
        chatbot.processCommand("event project meeting /from 2pm /to 3pm", ui);
        chatbot.processCommand("list", ui);

        assertTrue(output.stream().anyMatch(line -> line.contains("TASK INVENTORY // 3 RECORDS")));
        assertTrue(errors.isEmpty());
    }

    @Test
    void processCommand_invalidCommands_sendsErrorsWithoutEndingSession() {
        Chatbot chatbot = chatbot();
        List<String> errors = new ArrayList<>();
        Ui ui = new Ui(line -> { }, errors::add);

        assertFalse(chatbot.processCommand("todo", ui));
        assertFalse(chatbot.processCommand("unknown command", ui));

        assertTrue(errors.stream().anyMatch(line -> line.contains("requires a description")));
        assertTrue(errors.stream().anyMatch(line -> line.contains("Unknown directive")));
    }

    @Test
    void processCommand_bye_endsSessionAndShowsFarewell() {
        Chatbot chatbot = chatbot();
        List<String> output = new ArrayList<>();

        assertTrue(chatbot.processCommand("bye", new Ui(output::add)));

        assertTrue(output.contains("Mission state preserved. Ultron disengaging."));
    }

    private Chatbot chatbot() {
        return new Chatbot(temporaryDirectory.resolve("tasks.txt").toString());
    }
}
