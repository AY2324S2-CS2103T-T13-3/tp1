package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_AGENDA;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_DURATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_NOTES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_TIME;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.Assert.assertThrows;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ScheduleMeetingCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Meeting;



public class ScheduleMeetingCommandParserTest {

    private static final String VALID_DATE = "2024-07-19";
    private static final String VALID_TIME = "14:00";
    private static final String VALID_DURATION = "60"; // ISO-8601 duration format
    private static final String VALID_AGENDA = "Discuss new policy";
    private static final String VALID_NOTES = "Bring all necessary documents";

    private static final String INVALID_DATE = "2024-13-19"; // Invalid month
    private static final String INVALID_TIME = "25:00"; // Invalid hour
    private static final String INVALID_DURATION = "-60"; // Negative duration
    private static final String EMPTY_AGENDA = ""; // Empty agenda

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private ScheduleMeetingCommandParser parser = new ScheduleMeetingCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Index targetIndex = Index.fromOneBased(1);
        LocalDate meetingDate = LocalDate.parse(VALID_DATE, DATE_FORMATTER);
        LocalTime meetingTime = LocalTime.parse(VALID_TIME, TIME_FORMATTER);
        Duration duration = Duration.ofMinutes(Long.parseLong(VALID_DURATION));
        Meeting expectedMeeting = new Meeting(meetingDate, meetingTime, duration, VALID_AGENDA, VALID_NOTES);

        assertParseSuccess(parser, targetIndex.getOneBased() + " "
                        +
                        PREFIX_MEETING_DATE + VALID_DATE + " "
                        +
                        PREFIX_MEETING_TIME + VALID_TIME + " "
                        +
                        PREFIX_MEETING_DURATION + VALID_DURATION + " "
                        +
                        PREFIX_MEETING_AGENDA + VALID_AGENDA + " "
                        +
                        PREFIX_MEETING_NOTES + VALID_NOTES,
                new ScheduleMeetingCommand(targetIndex, expectedMeeting));
    }


    @Test
    public void parse_missingCompulsoryField_failure() {
        // Missing date
        assertParseFailure(parser, "1 "
                        +
                        PREFIX_MEETING_TIME + VALID_TIME + " "
                        +
                        PREFIX_MEETING_DURATION + VALID_DURATION + " "
                        +
                        PREFIX_MEETING_AGENDA + VALID_AGENDA + " "
                        +
                        PREFIX_MEETING_NOTES + VALID_NOTES,
                Meeting.MESSAGE_CONSTRAINTS_DATE);

        // Missing time
        assertParseFailure(parser, "1 "
                        +
                        PREFIX_MEETING_DATE + VALID_DATE + " "
                        +
                        PREFIX_MEETING_DURATION + VALID_DURATION + " "
                        +
                        PREFIX_MEETING_AGENDA + VALID_AGENDA + " "
                        +
                        PREFIX_MEETING_NOTES + VALID_NOTES,
                Meeting.MESSAGE_CONSTRAINTS_TIME);

        // Missing duration
        assertParseFailure(parser, "1 "
                        +
                        PREFIX_MEETING_DATE + VALID_DATE + " "
                        +
                        PREFIX_MEETING_TIME + VALID_TIME + " "
                        +
                        PREFIX_MEETING_AGENDA + VALID_AGENDA + " "
                        +
                        PREFIX_MEETING_NOTES + VALID_NOTES,
                Meeting.MESSAGE_CONSTRAINTS_DURATION);

        // Missing agenda
        assertParseFailure(parser, "1 "
                        +
                        PREFIX_MEETING_DATE + VALID_DATE + " "
                        +
                        PREFIX_MEETING_TIME + VALID_TIME + " "
                        +
                        PREFIX_MEETING_DURATION + VALID_DURATION + " "
                        +
                        PREFIX_MEETING_NOTES + VALID_NOTES,
                Meeting.MESSAGE_CONSTRAINTS_AGENDA);
    }

    @Test
    public void parse_invalidValue_failure() {
        // Invalid date
        assertParseFailure(parser, "1 "
                        +
                        PREFIX_MEETING_DATE + INVALID_DATE + " "
                        +
                        PREFIX_MEETING_TIME + VALID_TIME + " "
                        +
                        PREFIX_MEETING_DURATION + VALID_DURATION + " "
                        +
                        PREFIX_MEETING_AGENDA + VALID_AGENDA + " "
                        +
                        PREFIX_MEETING_NOTES + VALID_NOTES,
                "Invalid date or time format. Use YYYY-MM-DD for date and HH:MM for time.");

        // Invalid time
        assertParseFailure(parser, "1 "
                        +
                        PREFIX_MEETING_DATE + VALID_DATE + " "
                        +
                        PREFIX_MEETING_TIME + INVALID_TIME + " "
                        +
                        PREFIX_MEETING_DURATION + VALID_DURATION + " "
                        +
                        PREFIX_MEETING_AGENDA + VALID_AGENDA + " "
                        +
                        PREFIX_MEETING_NOTES + VALID_NOTES,
                "Invalid date or time format. Use YYYY-MM-DD for date and HH:MM for time.");

        // Invalid duration
        assertParseFailure(parser, "1 "
                        +
                        PREFIX_MEETING_DATE + VALID_DATE + " "
                        +
                        PREFIX_MEETING_TIME + VALID_TIME + " "
                        +
                        PREFIX_MEETING_DURATION + INVALID_DURATION + " "
                        +
                        PREFIX_MEETING_AGENDA + VALID_AGENDA + " "
                        +
                        PREFIX_MEETING_NOTES + VALID_NOTES,
                "Duration must be a non-negative integer.");

        // Empty agenda
        assertParseFailure(parser, "1 "
                        +
                        PREFIX_MEETING_DATE + VALID_DATE + " "
                        +
                        PREFIX_MEETING_TIME + VALID_TIME + " "
                        +
                        PREFIX_MEETING_DURATION + VALID_DURATION + " "
                        +
                        PREFIX_MEETING_AGENDA + EMPTY_AGENDA + " "
                        +
                        PREFIX_MEETING_NOTES + VALID_NOTES,
                "Agenda is required and cannot be empty.");
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // No notes
        Index targetIndex = Index.fromOneBased(1);
        LocalDate meetingDate = LocalDate.parse(VALID_DATE, DATE_FORMATTER);
        LocalTime meetingTime = LocalTime.parse(VALID_TIME, TIME_FORMATTER);
        Duration duration = Duration.ofMinutes(Long.parseLong(VALID_DURATION));
        Meeting expectedMeeting = new Meeting(meetingDate, meetingTime, duration, VALID_AGENDA, "");

        assertParseSuccess(parser, targetIndex.getOneBased() + " "
                        +
                        PREFIX_MEETING_DATE + VALID_DATE + " "
                        +
                        PREFIX_MEETING_TIME + VALID_TIME + " "
                        +
                        PREFIX_MEETING_DURATION + VALID_DURATION + " "
                        +
                        PREFIX_MEETING_AGENDA + VALID_AGENDA,
                new ScheduleMeetingCommand(targetIndex, expectedMeeting));
    }
    @Test
    public void execute_meetingInPast_throwsParseException() {
        String pastMeetingString = "1 md/29 05 2021 mt/00:00 mdur/60 ma/hi";
        assertThrows(ParseException.class, "Cannot schedule a meeting in the past.", () ->
                parser.parse(pastMeetingString));
    }

    @Test
    public void execute_meetingInDistantFuture_throwsParseException() {
        String meetingInDistantFutureString = "2 md/29 05 2026 mt/00:00 mdur/60 ma/hi";
        assertThrows(ParseException.class, "Cannot schedule a meeting more than a year in the future.", () ->
                parser.parse(meetingInDistantFutureString));
    }

}
