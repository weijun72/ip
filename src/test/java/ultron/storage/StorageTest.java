package ultron.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import ultron.model.Task;

/** Tests recovery from absent or malformed saved-task files. */
class StorageTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void loadTasks_missingFile_returnsEmptyList() {
        Storage storage = new Storage(temporaryDirectory.resolve("missing.txt").toString());

        ArrayList<Task> tasks = storage.loadTasks();

        assertEquals(0, tasks.size());
    }

    @Test
    void loadTasks_corruptLine_skipsOnlyCorruptLine() throws Exception {
        Path saveFile = temporaryDirectory.resolve("tasks.txt");
        Files.write(saveFile, java.util.List.of(
                "not a stored task",
                "T | 0 | read book",
                "D | 1 | submit report( by: 02/Dec/2099 1800 )"), StandardCharsets.UTF_8);
        Storage storage = new Storage(saveFile.toString());

        ArrayList<Task> tasks = storage.loadTasks();

        assertEquals(2, tasks.size());
        assertEquals("read book", tasks.get(0).getDescription());
        assertEquals("submit report( by: 02/Dec/2099 1800 )", tasks.get(1).getDescription());
        assertEquals(true, tasks.get(1).isDone());
    }
}
