package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.person.Meeting;
import seedu.address.model.person.Person;
import seedu.address.testutil.MeetingBuilder;
import seedu.address.testutil.PersonBuilder;

public class ScheduleMeetingCommandTest {

    private Model model = new ModelManager();

    @Test
    public void execute_validMeeting_success() throws Exception {
        Person validPerson = new PersonBuilder().build();
        model.addPerson(validPerson); // Add a person to the model
        Meeting validMeeting = new MeetingBuilder().build();
        CommandResult commandResult = new ScheduleMeetingCommand(Index.fromZeroBased(0),
                validMeeting).execute(model);

        assertEquals(String.format(ScheduleMeetingCommand.MESSAGE_MEETING_SCHEDULED_SUCCESS,
                        Messages.format(validPerson)),
                commandResult.getFeedbackToUser());
    }


    @Test
    public void execute_duplicateMeeting_throwsCommandException() {
        Person validPerson = new PersonBuilder().build();
        Meeting validMeeting = new MeetingBuilder().build();
        model.addPerson(validPerson); // Add a person to the model
        validPerson.addMeeting(validMeeting);
        ScheduleMeetingCommand scheduleMeetingCommand =
                new ScheduleMeetingCommand(Index.fromZeroBased(0), validMeeting);

        assertThrows(CommandException.class, ScheduleMeetingCommand.MESSAGE_MEETING_OVERLAP, () ->
                scheduleMeetingCommand.execute(model));
    }


    @Test
    public void execute_exceedMeetingLimit_throwsCommandException() {
        Person personWithMaxMeetings = new PersonBuilder().build();
        for (int i = 0; i < 5; i++) {
            personWithMaxMeetings.addMeeting(new MeetingBuilder().withDate(LocalDate.now().plusDays(i)).build());
        }
        model.addPerson(personWithMaxMeetings);
        Meeting newMeeting = new MeetingBuilder().withDate(LocalDate.now().plusDays(6)).build();
        ScheduleMeetingCommand scheduleMeetingCommand = new ScheduleMeetingCommand(Index.fromZeroBased(0), newMeeting);

        assertThrows(CommandException.class, "Cannot have more than 5 meetings.", () ->
                scheduleMeetingCommand.execute(model));
    }

    @Test
    public void equals() {
        Meeting meeting1 = new MeetingBuilder().withDate(LocalDate.of(2023, 3, 20)).build();
        Meeting meeting2 = new MeetingBuilder().withDate(LocalDate.of(2023, 3, 21)).build();
        ScheduleMeetingCommand scheduleMeetingCommand1 = new ScheduleMeetingCommand(Index.fromZeroBased(0), meeting1);
        ScheduleMeetingCommand scheduleMeetingCommand2 = new ScheduleMeetingCommand(Index.fromZeroBased(0), meeting2);

        // same object -> returns true
        assertTrue(scheduleMeetingCommand1.equals(scheduleMeetingCommand1));

        // same values -> returns true
        ScheduleMeetingCommand scheduleMeetingCommand1Copy =
                new ScheduleMeetingCommand(Index.fromZeroBased(0), meeting1);
        assertTrue(scheduleMeetingCommand1.equals(scheduleMeetingCommand1Copy));

        // different types -> returns false
        assertFalse(scheduleMeetingCommand1.equals(1));

        // null -> returns false
        assertFalse(scheduleMeetingCommand1.equals(null));

        // different meeting -> returns false
        assertFalse(scheduleMeetingCommand1.equals(scheduleMeetingCommand2));
    }
}
