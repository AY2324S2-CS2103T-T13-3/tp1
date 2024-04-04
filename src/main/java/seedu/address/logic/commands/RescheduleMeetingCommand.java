package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_TIME;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.time.LocalDateTime;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Meeting;
import seedu.address.model.person.Person;

/**
 * Schedules a meeting with an existing person in the address book.
 */
public class RescheduleMeetingCommand extends Command {

    public static final String COMMAND_WORD = "reschedule";

    public static final String MESSAGE_MEETING_RESCHEDULED_SUCCESS = "Meeting rescheduled successfully: %1$s";
    public static final String MESSAGE_MEETING_INVALID_INDEX = "Invalid index provided for meeting list";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Reschedules a meeting with the person identified "
            + "by the index number used in the last person listing. "
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_MEETING_INDEX + "[MEETING INDEX]"
            + PREFIX_MEETING_DATE + "[DATE] "
            + PREFIX_MEETING_TIME + "[TIME]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_MEETING_INDEX + "1 "
            + PREFIX_MEETING_DATE + "2024-07-19 "
            + PREFIX_MEETING_TIME + "14:00 ";

    private final Index personIndex;
    private final Index meetingIndex;
    private final LocalDateTime newMeetingDateTime;

    /**
     * @param personIndex   of the person in the filtered person list to schedule the meeting with
     * @param meetingIndex of the meeting in the list of meetings
     * @param newMeetingDateTime date time of the meeting to be scheduled
     */
    public RescheduleMeetingCommand(Index personIndex, Index meetingIndex, LocalDateTime newMeetingDateTime) {
        requireAllNonNull(personIndex, meetingIndex, newMeetingDateTime);

        this.personIndex = personIndex;
        this.meetingIndex = meetingIndex;
        this.newMeetingDateTime = newMeetingDateTime;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();
        if (personIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
        Person personToMeetOriginal = lastShownList.get(personIndex.getZeroBased());
        List<Meeting> meetings = personToMeetOriginal.getMeetings();
        if (meetings.size() <= (meetingIndex.getZeroBased())) {
            throw new CommandException(RescheduleMeetingCommand.MESSAGE_MEETING_INVALID_INDEX);
        }
        // Create a copy of the original person and add the meeting to the copy
        Person personToMeetUpdated = personToMeetOriginal.getCopy();
        try {
            personToMeetUpdated.rescheduleMeeting(meetingIndex.getZeroBased(), newMeetingDateTime);
        } catch (IllegalArgumentException e) {
            throw new CommandException(e.getMessage());
        }
        // Update the person in the model
        model.setPerson(personToMeetOriginal, personToMeetUpdated);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        model.commitAddressBook();
        return new CommandResult(String.format(MESSAGE_MEETING_RESCHEDULED_SUCCESS,
                Messages.format(personToMeetUpdated)));
    }


    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof RescheduleMeetingCommand)) {
            return false;
        }
        RescheduleMeetingCommand e = (RescheduleMeetingCommand) other;
        return personIndex.equals(e.personIndex) && meetingIndex.equals(e.meetingIndex)
                && newMeetingDateTime.equals(e.newMeetingDateTime);
    }
}
