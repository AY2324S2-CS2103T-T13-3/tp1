package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.testutil.Assert.assertThrows;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

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

public class CancelMeetingCommandTest {

    @Test
    public void execute_validIndex_success() throws CommandException {
        Model model = new ModelManager();
        Person person = new PersonBuilder().build();
        Meeting meeting1 = new MeetingBuilder().withDate(LocalDate.parse("2024-07-19"))
                        .withTime(LocalTime.parse("14:00")).build();
        Meeting meeting2 = new MeetingBuilder().withDate(LocalDate.parse("2024-08-19"))
                .withTime(LocalTime.parse("15:00")).build();
        person.setMeetings(Arrays.asList(meeting1, meeting2));
        model.addPerson(person);

        Index personIndex = Index.fromOneBased(1);
        Index meetingIndex = Index.fromOneBased(1);

        CancelMeetingCommand cancelMeetingCommand = new CancelMeetingCommand(personIndex, meetingIndex);
        CommandResult commandResult = cancelMeetingCommand.execute(model);

        String expectedMessage = String.format(CancelMeetingCommand.MESSAGE_MEETING_CANCELLED_SUCCESS,
                "Amy Bee; Phone: 85355255; Email: amy@gmail.com; Address: 123, Jurong West Ave 6, #08-111; "
                        + "Relationship: client; Tags: ");
        assertEquals(expectedMessage, commandResult.getFeedbackToUser());
    }

    @Test
    public void execute_invalidPersonIndex_throwsCommandException() {
        Model model = new ModelManager();
        Person person = new PersonBuilder().build();
        Meeting meeting = new MeetingBuilder().build();
        person.setMeetings(Arrays.asList(meeting));
        model.addPerson(person);

        Index personIndex = Index.fromOneBased(2);
        Index meetingIndex = Index.fromOneBased(1);

        CancelMeetingCommand cancelMeetingCommand = new CancelMeetingCommand(personIndex, meetingIndex);

        assertThrows(CommandException.class,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, () -> cancelMeetingCommand.execute(model));
    }

    @Test
    public void execute_invalidMeetingIndex_throwsCommandException() {
        Model model = new ModelManager();
        Person person = new PersonBuilder().build();
        Meeting meeting = new MeetingBuilder().build();
        person.setMeetings(Arrays.asList(meeting));
        model.addPerson(person);

        Index personIndex = Index.fromOneBased(1);
        Index meetingIndex = Index.fromOneBased(2);

        CancelMeetingCommand cancelMeetingCommand = new CancelMeetingCommand(personIndex, meetingIndex);

        assertThrows(CommandException.class,
                CancelMeetingCommand.MESSAGE_MEETING_INVALID_INDEX, () -> cancelMeetingCommand.execute(model));
    }

    @Test
    public void equals() {
        Index personIndexOne = Index.fromOneBased(1);
        Index meetingIndexOne = Index.fromOneBased(1);
        Index personIndexTwo = Index.fromOneBased(2);
        Index meetingIndexTwo = Index.fromOneBased(2);

        CancelMeetingCommand cancelMeetingCommandOne = new CancelMeetingCommand(personIndexOne, meetingIndexOne);
        CancelMeetingCommand cancelMeetingCommandTwo = new CancelMeetingCommand(personIndexTwo, meetingIndexTwo);

        // same object -> returns true
        assertEquals(cancelMeetingCommandOne, cancelMeetingCommandOne);

        // same values -> returns true
        CancelMeetingCommand cancelMeetingCommandOneCopy = new CancelMeetingCommand(personIndexOne, meetingIndexOne);
        assertEquals(cancelMeetingCommandOne, cancelMeetingCommandOneCopy);

        // different types -> returns false
        assertNotEquals(1, cancelMeetingCommandOne);

        // null -> returns false
        assertNotEquals(null, cancelMeetingCommandOne);

        assertNotEquals(cancelMeetingCommandOne, cancelMeetingCommandTwo);
    }
}
