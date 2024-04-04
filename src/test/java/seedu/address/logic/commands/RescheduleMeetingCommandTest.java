package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.person.Meeting;
import seedu.address.model.person.Person;
import seedu.address.testutil.MeetingBuilder;
import seedu.address.testutil.PersonBuilder;

public class RescheduleMeetingCommandTest {

    private static final String VALID_DATE_TIME_STRING = "2024-07-19T14:00";
    private Model model = new ModelManager();

    @Test
    public void execute_validReschedule_success() throws Exception {
        Person validPerson = new PersonBuilder().build();
        Meeting validMeeting = new MeetingBuilder().build();
        validPerson.addMeeting(validMeeting);
        LocalDateTime validDateTimeMeeting = LocalDateTime.parse(VALID_DATE_TIME_STRING);
        model.addPerson(validPerson); // Add a person to the model
        CommandResult commandResult = new RescheduleMeetingCommand(Index.fromZeroBased(0),
                Index.fromZeroBased(0),
                validDateTimeMeeting).execute(model);

        assertEquals(String.format(RescheduleMeetingCommand.MESSAGE_MEETING_RESCHEDULED_SUCCESS,
                        Messages.format(validPerson)),
                commandResult.getFeedbackToUser());
    }
    @Test
    public void execute_invalidPersonIndex_failure() throws Exception {
        Person validPerson = new PersonBuilder().build();
        Meeting validMeeting = new MeetingBuilder().build();
        validPerson.addMeeting(validMeeting);
        LocalDateTime validDateTimeMeeting = LocalDateTime.parse(VALID_DATE_TIME_STRING);
        model.addPerson(validPerson); // Add a person to the model
        Command command = new RescheduleMeetingCommand(Index.fromZeroBased(1),
                Index.fromZeroBased(0),
                validDateTimeMeeting);

        assertCommandFailure(command, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }
    @Test
    public void execute_invalidMeetingIndex_failure() throws Exception {
        Person validPerson = new PersonBuilder().build();
        Meeting validMeeting = new MeetingBuilder().build();
        validPerson.addMeeting(validMeeting);
        LocalDateTime validDateTimeMeeting = LocalDateTime.parse(VALID_DATE_TIME_STRING);
        model.addPerson(validPerson); // Add a person to the model
        Command command = new RescheduleMeetingCommand(Index.fromZeroBased(0),
                Index.fromZeroBased(1),
                validDateTimeMeeting);

        assertCommandFailure(command, model, RescheduleMeetingCommand.MESSAGE_MEETING_INVALID_INDEX);
    }
}
