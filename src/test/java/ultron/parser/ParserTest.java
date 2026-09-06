package ultron.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.Test;

import ultron.command.RescheduleCommand;

class ParserTest {
    @Test
    void parseCommand_rescheduleInput_returnsRescheduleType() {
        Parser parser = new Parser();

        assertEquals(Parser.CommandType.RESCHEDULE, parser.parseCommand("reschedule 1 3d"));
    }

    @Test
    void parseCommandObject_rescheduleInput_returnsRescheduleCommand() {
        Parser parser = new Parser();

        assertInstanceOf(RescheduleCommand.class, parser.parseCommandObject("reschedule 1 /by 2/12/2099"));
    }
}
