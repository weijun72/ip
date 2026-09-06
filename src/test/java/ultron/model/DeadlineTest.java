package ultron.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import ultron.exception.UltronException;

/**
 * Tests deadline parsing and display formatting.
 */
class DeadlineTest {

    @Test
    void getDescription_deadlineWithDate_formatsDate() throws UltronException {
        Deadline deadline = new Deadline("return book /by 2/12/2019");

        assertEquals("return book( by: 02/Dec/2019 )", deadline.getDescription());
    }

    @Test
    void getDescription_deadlineWithDateAndTime_formatsDateAndTime() throws UltronException {
        Deadline deadline = new Deadline("return book /by 2/12/2019 1800");

        assertEquals("return book( by: 02/Dec/2019 1800 )", deadline.getDescription());
    }

    @Test
    void constructor_missingByMarker_exceptionThrown() {
        assertThrows(UltronException.class, () -> new Deadline("return book"));
    }

    @Test
    void constructor_invalidDate_exceptionThrown() {
        assertThrows(UltronException.class, () -> new Deadline("return book /by tomorrow"));
    }

    @Test
    void reschedule_newDateWithoutTime_preservesExistingTime() throws UltronException {
        Deadline deadline = new Deadline("return book /by 2/12/2099 1800");

        deadline.reschedule(LocalDate.of(2100, 1, 3), null);

        assertEquals("return book( by: 03/Jan/2100 1800 )", deadline.getDescription());
    }

    @Test
    void reschedule_newDateWithTime_replacesExistingTime() throws UltronException {
        Deadline deadline = new Deadline("return book /by 2/12/2099 1800");

        deadline.reschedule(LocalDate.of(2100, 1, 3), "0930");

        assertEquals("return book( by: 03/Jan/2100 0930 )", deadline.getDescription());
    }

    @Test
    void reschedule_today_acceptsNewDueDate() throws UltronException {
        Deadline deadline = new Deadline("return book /by 2/12/2099");

        deadline.reschedule(LocalDate.now(), null);

        assertEquals(LocalDate.now(), deadline.getDueDate());
    }

    @Test
    void reschedule_pastDate_exceptionThrownWithoutChangingDeadline() throws UltronException {
        Deadline deadline = new Deadline("return book /by 2/12/2099 1800");

        assertThrows(UltronException.class, () -> deadline.reschedule(LocalDate.now().minusDays(1), null));

        assertEquals("return book( by: 02/Dec/2099 1800 )", deadline.getDescription());
    }

    @Test
    void reschedule_invalidReplacementTime_exceptionThrownWithoutChangingDeadline() throws UltronException {
        Deadline deadline = new Deadline("return book /by 2/12/2099 1800");

        assertThrows(UltronException.class, () -> deadline.reschedule(LocalDate.of(2100, 1, 3), "2460"));

        assertEquals("return book( by: 02/Dec/2099 1800 )", deadline.getDescription());
    }
}
