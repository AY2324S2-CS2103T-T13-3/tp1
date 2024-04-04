package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_TIME;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.RescheduleMeetingCommand;


public class RescheduleMeetingCommandParserTest {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final Index FIRST_PERSON_INDEX = Index.fromOneBased(1);
    private static final Index FIRST_MEETING_INDEX = Index.fromOneBased(1);
    private static final String INVALID_DATE = "2024-07-44";
    private static final String INVALID_TIME = "25:00";
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final String VALID_DATE = "2024-07-19";
    private static final String VALID_TIME = "14:00";

    private static final LocalDate VALID_MEETING_DATE = LocalDate.parse(VALID_DATE, DATE_FORMATTER);
    private static final LocalTime VALID_MEETING_TIME = LocalTime.parse(VALID_TIME, TIME_FORMATTER);
    private RescheduleMeetingCommandParser parser = new RescheduleMeetingCommandParser();


    @Test
    public void parse_allFieldsPresent_success() {
        LocalDateTime meetingDateTime = LocalDateTime.of(VALID_MEETING_DATE, VALID_MEETING_TIME);
        assertParseSuccess(parser, FIRST_PERSON_INDEX.getOneBased() + " "
                        + PREFIX_MEETING_INDEX + FIRST_MEETING_INDEX.getOneBased() + " "
                + PREFIX_MEETING_DATE + VALID_DATE + " "
                + PREFIX_MEETING_TIME + VALID_TIME + " ",
                new RescheduleMeetingCommand(FIRST_PERSON_INDEX, FIRST_MEETING_INDEX, meetingDateTime));

    }
    @Test
    public void parse_missingCompulsoryFields_failure() {
        // Missing time input
        assertParseFailure(parser, FIRST_PERSON_INDEX.getOneBased() + " "
                        + PREFIX_MEETING_INDEX + FIRST_MEETING_INDEX.getOneBased() + " "
                        + PREFIX_MEETING_DATE + VALID_DATE + " ",
                 "Meeting index, date and time are required for scheduling a meeting.");
        // Missing date input
        assertParseFailure(parser, FIRST_PERSON_INDEX.getOneBased() + " "
                        + PREFIX_MEETING_INDEX + FIRST_MEETING_INDEX.getOneBased() + " "
                        + PREFIX_MEETING_TIME + VALID_TIME + " ",
                 "Meeting index, date and time are required for scheduling a meeting.");
        // Missing meeting index input
        assertParseFailure(parser, FIRST_PERSON_INDEX.getOneBased() + " "
                        + PREFIX_MEETING_DATE + VALID_DATE + " "
                        + PREFIX_MEETING_TIME + VALID_TIME + " ",
                 "Meeting index, date and time are required for scheduling a meeting.");
    }
    @Test
    public void parse_missingPersonIndex_failure() {
        assertParseFailure(parser, " "
                        + PREFIX_MEETING_INDEX + FIRST_MEETING_INDEX.getOneBased() + " "
                + PREFIX_MEETING_DATE + VALID_DATE + " "
                + PREFIX_MEETING_TIME + VALID_TIME + " " ,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                RescheduleMeetingCommand.MESSAGE_USAGE));

    }
    @Test
    public void parse_invalidDateTime_failure() {
        // Invalid dates

        assertParseFailure(parser, FIRST_PERSON_INDEX.getOneBased() + " "
                + PREFIX_MEETING_INDEX + FIRST_MEETING_INDEX.getOneBased() + " "
                + PREFIX_MEETING_DATE + INVALID_DATE + " "
                + PREFIX_MEETING_TIME + VALID_TIME + " ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        RescheduleMeetingCommand.MESSAGE_USAGE));
        // Invalid Time
        assertParseFailure(parser, FIRST_PERSON_INDEX.getOneBased() + " "
                        + PREFIX_MEETING_INDEX + FIRST_MEETING_INDEX.getOneBased() + " "
                        + PREFIX_MEETING_DATE + VALID_DATE + " "
                        + PREFIX_MEETING_TIME + INVALID_TIME + " ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        RescheduleMeetingCommand.MESSAGE_USAGE));
    }
}
