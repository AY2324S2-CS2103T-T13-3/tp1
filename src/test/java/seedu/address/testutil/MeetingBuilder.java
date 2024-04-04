package seedu.address.testutil;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

import seedu.address.model.person.Meeting;

/**
 * A utility class to help with building Meeting objects.
 */
public class MeetingBuilder {

    public static final LocalDate DEFAULT_DATE = LocalDate.of(2024, 7, 18);
    public static final LocalTime DEFAULT_TIME = LocalTime.of(14, 0);
    public static final Duration DEFAULT_DURATION = Duration.ofMinutes(60);
    public static final String DEFAULT_AGENDA = "Discuss Insurance Documents";
    public static final String DEFAULT_NOTES = "Bring all necessary certificates";

    private LocalDate meetingDate;
    private LocalTime meetingTime;
    private Duration duration;
    private String agenda;
    private String notes;

    /**
     * Creates a {@code MeetingBuilder} with the default details.
     */
    public MeetingBuilder() {
        meetingDate = DEFAULT_DATE;
        meetingTime = DEFAULT_TIME;
        duration = DEFAULT_DURATION;
        agenda = DEFAULT_AGENDA;
        notes = DEFAULT_NOTES;
    }

    /**
     * Sets the {@code LocalDate} of the {@code Meeting} that we are building.
     */
    public MeetingBuilder withDate(LocalDate meetingDate) {
        this.meetingDate = meetingDate;
        return this;
    }

    /**
     * Sets the {@code LocalTime} of the {@code Meeting} that we are building.
     */
    public MeetingBuilder withTime(LocalTime meetingTime) {
        this.meetingTime = meetingTime;
        return this;
    }

    /**
     * Sets the {@code Duration} of the {@code Meeting} that we are building.
     */
    public MeetingBuilder withDuration(Duration duration) {
        this.duration = duration;
        return this;
    }

    /**
     * Sets the {@code agenda} of the {@code Meeting} that we are building.
     */
    public MeetingBuilder withAgenda(String agenda) {
        this.agenda = agenda;
        return this;
    }

    /**
     * Sets the {@code notes} of the {@code Meeting} that we are building.
     */
    public MeetingBuilder withNotes(String notes) {
        this.notes = notes;
        return this;
    }

    /**
     * Builds and returns a new Meeting object with the specified details.
     *
     * @return A new Meeting object with the specified details.
     */
    public Meeting build() {
        return new Meeting(meetingDate, meetingTime, duration, agenda, notes);
    }
}
