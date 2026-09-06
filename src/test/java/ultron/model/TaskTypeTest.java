package ultron.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TaskTypeTest {
    @Test
    void fromSymbol_knownSymbol_returnsMatchingTaskType() {
        assertEquals(TaskType.DEADLINE, TaskType.fromSymbol("D").orElseThrow());
    }

    @Test
    void fromSymbol_unknownSymbol_returnsEmptyValue() {
        assertTrue(TaskType.fromSymbol("X").isEmpty());
    }
}
