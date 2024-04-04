package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_INDEX;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.CancelMeetingCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code CancelMeeting} object
 */
public class CancelMeetingCommandParser implements Parser<CancelMeetingCommand> {
    @Override
    public CancelMeetingCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_MEETING_INDEX);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    CancelMeetingCommand.MESSAGE_USAGE), ive);
        }
        if (!argMultimap.arePrefixesPresent(PREFIX_MEETING_INDEX)) {
            throw new ParseException("Meeting index is required for cancelling a meeting.");
        }
        Index meetingIndex;
        try {
            meetingIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_MEETING_INDEX).orElse(""));
        } catch (ParseException e) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    CancelMeetingCommand.MESSAGE_USAGE), e);
        }
        return new CancelMeetingCommand(index, meetingIndex);

    }
}
