package ultron;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Exercises writing tasks to storage and restoring them on the next startup.
 */
public class UltronStorageTest {
    /**
     * Runs the persistence integration test.
     *
     * @param args command-line arguments, which are not used
     * @throws Exception if the saved data differs from the expected data
     */
    public static void main(String[] args) throws Exception {
        Path testSaveFile = Files.createTempFile("ultron-storage-test", ".txt");
        Files.delete(testSaveFile);
        System.setProperty("ultron.saveFile", testSaveFile.toString());

        InputStream originalInput = System.in;
        PrintStream originalOutput = System.out;
        String commands = "todo read book\n"
                + "deadline return book /by 2/12/2019\n"
                + "event project meeting /from 2pm /to 3pm\n"
                + "mark 2\n"
                + "unmark 2\n"
                + "delete 3\n"
                + "bye\n";

        try {
            System.setIn(new ByteArrayInputStream(commands.getBytes(StandardCharsets.UTF_8)));
            System.setOut(new PrintStream(OutputStream.nullOutputStream()));
            Ultron.main(new String[0]);
        } finally {
            System.setIn(originalInput);
            System.setOut(originalOutput);
        }

        List<String> expectedLines = List.of(
                "T | 0 | read book",
                "D | 0 | return book( by: 02/Dec/2019 )");
        List<String> actualLines = Files.readAllLines(testSaveFile, StandardCharsets.UTF_8);


        if (!actualLines.equals(expectedLines)) {
            throw new AssertionError("Unexpected saved tasks: " + actualLines);
        }

        String restoredOutput = runUltron("list\nbye\n");
        Files.deleteIfExists(testSaveFile);
        if (!restoredOutput.contains("1.[T] [ ] read book")
                || !restoredOutput.contains("2.[D] [ ] return book( by: 02/Dec/2019 )")) {
            throw new AssertionError("Tasks were not restored: " + restoredOutput);
        }
    }

    /**
     * Runs the chatbot with the supplied commands and captures its output.
     *
     * @param commands newline-separated chatbot commands
     * @return the chatbot output
     */
    private static String runUltron(String commands) {
        InputStream originalInput = System.in;
        PrintStream originalOutput = System.out;
        OutputStream capturedOutput = new java.io.ByteArrayOutputStream();

        try {
            System.setIn(new ByteArrayInputStream(commands.getBytes(StandardCharsets.UTF_8)));
            System.setOut(new PrintStream(capturedOutput));
            Ultron.main(new String[0]);
        } finally {
            System.setIn(originalInput);
            System.setOut(originalOutput);
        }
        return capturedOutput.toString();
    }
}
