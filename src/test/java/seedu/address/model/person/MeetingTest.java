package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

public class MeetingTest {

    @Test
    public void constructor_validParameters_success() {
        LocalDate date = LocalDate.of(2023, 3, 20);
        LocalTime time = LocalTime.of(14, 00);
        Duration duration = Duration.ofHours(1);
        String agenda = "Discuss new policy";
        String notes = "Bring all necessary documents";

        Meeting meeting = new Meeting(date, time, duration, agenda, notes);

        assertEquals(date, meeting.getMeetingDate());
        assertEquals(time, meeting.getMeetingTime());
        assertEquals(duration, meeting.getDuration());
        assertEquals(agenda, meeting.getAgenda());
        assertEquals(notes, meeting.getNotes());
    }

    @Test
    public void overlapsWith_sameMeeting_returnsTrue() {
        Meeting meeting1 = new Meeting(LocalDate.of(2023, 3, 20), LocalTime.of(14, 00),
                Duration.ofHours(1), "Discuss new policy", "Bring all necessary documents");
        Meeting meeting2 = new Meeting(LocalDate.of(2023, 3, 20), LocalTime.of(14, 00),
                Duration.ofHours(1), "Discuss new policy", "Bring all necessary documents");

        assertTrue(meeting1.overlapsWith(meeting2));
    }

    @Test
    public void overlapsWith_nonOverlappingMeeting_returnsFalse() {
        Meeting meeting1 = new Meeting(LocalDate.of(2023, 3, 20), LocalTime.of(14, 00),
                Duration.ofHours(1), "Discuss new policy", "Bring all necessary documents");
        Meeting meeting2 = new Meeting(LocalDate.of(2023, 3, 20), LocalTime.of(16, 00),
                Duration.ofHours(1), "Review contract", "Bring contract documents");

        assertFalse(meeting1.overlapsWith(meeting2));
    }

    @Test
    public void equals_sameMeeting_returnsTrue() {
        Meeting meeting1 = new Meeting(LocalDate.of(2023, 3, 20), LocalTime.of(14, 00),
                Duration.ofHours(1), "Discuss new policy", "Bring all necessary documents");
        Meeting meeting2 = new Meeting(LocalDate.of(2023, 3, 20), LocalTime.of(14, 00),
                Duration.ofHours(1), "Discuss new policy", "Bring all necessary documents");

        assertTrue(meeting1.equals(meeting2));
    }

    @Test
    public void equals_differentMeeting_returnsFalse() {
        Meeting meeting1 = new Meeting(LocalDate.of(2023, 3, 20), LocalTime.of(14, 00),
                Duration.ofHours(1), "Discuss new policy", "Bring all necessary documents");
        Meeting meeting2 = new Meeting(LocalDate.of(2023, 3, 21), LocalTime.of(14, 00),
                Duration.ofHours(1), "Review contract", "Bring contract documents");

        assertFalse(meeting1.equals(meeting2));
    }
}
