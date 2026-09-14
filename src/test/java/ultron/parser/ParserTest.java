package ultron.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.Test;

import ultron.command.DeleteCommand;
import ultron.command.ExitCommand;
import ultron.command.FindCommand;
import ultron.command.ListCommand;
import ultron.command.MarkCommand;
import ultron.command.RescheduleCommand;
import ultron.command.UnmarkCommand;

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

    @Test
    void parseCommand_multipleSpaces_returnsCommandType() {
        Parser parser = new Parser();

        assertEquals(Parser.CommandType.DEADLINE, parser.parseCommand("  deadline   submit report /by 2/12/2099"));
    }

    @Test
    void parseCommand_parameterlessCommandWithArguments_returnsUnknown() {
        Parser parser = new Parser();

        assertEquals(Parser.CommandType.UNKNOWN, parser.parseCommand("list now"));
        assertEquals(Parser.CommandType.UNKNOWN, parser.parseCommand("bye later"));
    }

    @Test
    void parseCommandObject_supportedCommands_returnsExpectedCommandObjects() {
        Parser parser = new Parser();

        assertInstanceOf(ExitCommand.class, parser.parseCommandObject("bye"));
        assertInstanceOf(ListCommand.class, parser.parseCommandObject("list"));
        assertInstanceOf(MarkCommand.class, parser.parseCommandObject("mark 1"));
        assertInstanceOf(UnmarkCommand.class, parser.parseCommandObject("unmark 1"));
        assertInstanceOf(DeleteCommand.class, parser.parseCommandObject("delete 1"));
        assertInstanceOf(FindCommand.class, parser.parseCommandObject("find book"));
    }

    @Test
    void getArgument_extraWhitespace_returnsTrimmedArgument() {
        Parser parser = new Parser();

        assertEquals("read book", parser.getArgument("  todo   read book  ", Parser.CommandType.TODO));
    }
}
