package ultron.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ultron.exception.UltronException;

/**
 * Tests event parsing and display formatting.
 */
class EventTest {

    @Test
    void getDescription_eventWithStartAndEnd_formatsDetails() throws UltronException {
        Event event = new Event("project meeting /from 2pm /to 3pm");

        assertEquals("project meeting( from: 2pm to: 3pm)", event.getDescription());
    }

    @Test
    void constructor_missingTimeMarker_exceptionThrown() {
        assertThrows(UltronException.class, () -> new Event("project meeting /from 2pm"));
    }

    @Test
    void constructor_repeatedFromMarker_exceptionThrown() {
        assertThrows(UltronException.class, () -> new Event("project /from 2pm /from 3pm /to 4pm"));
    }

    @Test
    void constructor_blankEndTime_exceptionThrown() {
        assertThrows(UltronException.class, () -> new Event("project meeting /from 2pm /to "));
    }
}
