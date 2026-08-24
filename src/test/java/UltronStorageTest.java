import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Exercises task-list changes and verifies that they are written to storage.
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
                + "deadline return book /by Friday\n"
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
                "D | 0 | return book( by: Friday)");
        List<String> actualLines = Files.readAllLines(testSaveFile, StandardCharsets.UTF_8);
        Files.deleteIfExists(testSaveFile);

        if (!actualLines.equals(expectedLines)) {
            throw new AssertionError("Unexpected saved tasks: " + actualLines);
        }
    }
}
