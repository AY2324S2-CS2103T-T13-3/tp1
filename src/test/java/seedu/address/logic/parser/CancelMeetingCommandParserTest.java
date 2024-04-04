package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.CancelMeetingCommand;

public class CancelMeetingCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, CancelMeetingCommand.MESSAGE_USAGE);

    private CancelMeetingCommandParser parser = new CancelMeetingCommandParser();

    @Test
    public void parse_validArgs_returnsCancelMeetingCommand() {
        assertParseSuccess(parser, "1 mi/1", new CancelMeetingCommand(Index.fromOneBased(1), Index.fromOneBased(1)));
        assertParseSuccess(parser, "2 mi/2", new CancelMeetingCommand(Index.fromOneBased(2), Index.fromOneBased(2)));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        // Invalid person index
        assertParseFailure(parser, "a mi/1", MESSAGE_INVALID_FORMAT);

        // Invalid meeting index
        assertParseFailure(parser, "1 mi/a", MESSAGE_INVALID_FORMAT);

        // Missing person index
        assertParseFailure(parser, "mi/1", MESSAGE_INVALID_FORMAT);

        // Missing meeting index
        assertParseFailure(parser, "1", "Meeting index is required for cancelling a meeting.");

        // Extra arguments
        assertParseFailure(parser, "1 mi/1 extra", MESSAGE_INVALID_FORMAT);
    }

}
