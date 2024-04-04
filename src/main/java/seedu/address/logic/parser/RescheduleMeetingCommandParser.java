package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_TIME;
import static seedu.address.logic.parser.ParserUtil.parseLocalDateTime;

import java.time.LocalDate;
import java.time.LocalDateTime;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.RescheduleMeetingCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code RescheduleMeetingCommand} object
 */
public class RescheduleMeetingCommandParser implements Parser<RescheduleMeetingCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the {@code ScheduleMeetingCommand}
     * and returns a {@code ScheduleMeetingCommand} object for execution.
     *
     * @throws ParseException if the user input does not conform to the expected format
     */
    public RescheduleMeetingCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_MEETING_INDEX, PREFIX_MEETING_DATE, PREFIX_MEETING_TIME);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    RescheduleMeetingCommand.MESSAGE_USAGE), ive);
        }
        if (!argMultimap.arePrefixesPresent(PREFIX_MEETING_INDEX, PREFIX_MEETING_DATE, PREFIX_MEETING_TIME)) {
            throw new ParseException("Meeting index, date and time are required for scheduling a meeting.");
        }
        LocalDateTime meetingDateTime;
        Index meetingIndex;
        try {
            String stringMeetingDate = argMultimap.getValue(PREFIX_MEETING_DATE).orElse("");
            String stringMeetingTime = argMultimap.getValue(PREFIX_MEETING_TIME).orElse("");
            meetingIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_MEETING_INDEX).orElse(""));
            meetingDateTime = parseLocalDateTime(stringMeetingDate, stringMeetingTime);
        } catch (ParseException e) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    RescheduleMeetingCommand.MESSAGE_USAGE), e);
        }
        LocalDate today = LocalDate.now();
        LocalDate meetingDate = meetingDateTime.toLocalDate();
        if (meetingDate.isAfter(today.plusYears(1))) { // Assuming 1 year is too far in the future
            throw new ParseException("Cannot reschedule a meeting more than a year in the future.");
        } else if (meetingDate.isBefore(today)) {
            throw new ParseException("Cannot reschedule a meeting in the past.");
        }
        return new RescheduleMeetingCommand(index, meetingIndex, meetingDateTime);
    }

}
