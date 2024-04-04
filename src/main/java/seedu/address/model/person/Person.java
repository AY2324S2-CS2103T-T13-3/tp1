package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Address address;

    private List<Policy> policies;

    private final Relationship relationship;

    private final ClientStatus clientStatus;

    private final Set<Tag> tags = new HashSet<>();


    private List<Meeting> meetings;


    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, Relationship relationship,
                  List<Policy> policies, ClientStatus clientStatus, Set<Tag> tags, List<Meeting> meetings) {
        requireAllNonNull(name, phone, email, address, relationship, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.policies = getPolicyListCopy(policies);
        this.relationship = relationship;
        this.clientStatus = clientStatus;
        this.tags.addAll(tags);
        this.meetings = getMeetingListCopy(meetings);
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public Relationship getRelationship() {
        return relationship;
    }

    public ClientStatus getClientStatus() {
        return clientStatus;
    }

    public boolean isClient() {
        return relationship.value.equals("client");
    }

    public boolean isPartner() {
        return relationship.value.equals("partner");
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }



    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && address.equals(otherPerson.address)
                && relationship.equals(otherPerson.relationship)
                && policies.equals(otherPerson.policies)
                && tags.equals(otherPerson.tags);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, policies, relationship, tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("relationship", relationship)
                .add("clientStatus", clientStatus)
                .add("policy", policies)
                .add("tags", tags)
                .toString();
    }

    /**
     * Returns an immutable list of meetings, sorted by start date and time.
     */
    public List<Meeting> getMeetings() {
        List<Meeting> sortedMeetings = new ArrayList<>(meetings);
        sortedMeetings.sort(Comparator.comparing(Meeting::getStartDateTime));
        return Collections.unmodifiableList(sortedMeetings);
    }

    /**
     * Returns an immutable list of policies, sorted by policy value.
     */
    public List<Policy> getPolicies() {
        List<Policy> sortedPolicies = new ArrayList<>(policies);
        sortedPolicies.sort(Comparator.comparing(policy -> policy.value));
        return Collections.unmodifiableList(sortedPolicies);
    }

    /**
     * Adds a meeting to the list of meetings associated with this person.
     *
     * @param meeting The meeting to be added.
     * @throws IllegalArgumentException if the meeting overlaps with existing meetings or
     *     if scheduling constraints are violated.
     */
    public void addMeeting(Meeting meeting) {
        if (meetings.size() >= 5) {
            throw new IllegalArgumentException("Cannot have more than 5 meetings.");
        } else if (isOverlapWithOtherMeetings(meeting)) {
            throw new IllegalArgumentException("Meeting overlaps with existing meetings with this client.");
        } else {
            meetings.add(meeting);
        }
    }

    /**
     * Adds a meeting to the list of meetings associated with this person.
     *
     * @param policy The meeting to be added.
     * @throws IllegalArgumentException if the meeting overlaps with existing meetings or
     *     if scheduling constraints are violated.
     */
    public void addPolicy(Policy policy) {
        if (policies.size() >= 5) {
            throw new IllegalArgumentException("Cannot have more than 5 policies.");
        } else {
            policies.add(policy);
        }
    }

    /**
     * Sets the list of meetings associated with this person.
     *
     * @param meetings The list of meetings to be set.
     */
    public void setMeetings(List<Meeting> meetings) {
        this.meetings = meetings;
    }

    public void setPolicies(List<Policy> policies) {
        this.policies = policies;
    }

    /**
     * Reschedules a meeting associated with this person.
     *
     * @param index      The index of the meeting to be rescheduled.
     * @param newDateTime The new date and time for the meeting.
     * @throws IllegalArgumentException if the rescheduled meeting overlaps with existing meetings.
     */
    public void rescheduleMeeting(int index, LocalDateTime newDateTime) {
        Meeting meetingToReschedule = meetings.get(index);
        Meeting rescheduledMeeting = new Meeting(
                newDateTime.toLocalDate(),
                newDateTime.toLocalTime(),
                meetingToReschedule.getDuration(),
                meetingToReschedule.getAgenda(),
                meetingToReschedule.getNotes()
        );

        // Remove the old meeting and try adding the rescheduled one
        meetings.remove(index);
        if (!isOverlapWithOtherMeetings(rescheduledMeeting)) {
            meetings.add(rescheduledMeeting);
        } else {
            // If there's an overlap, add the old meeting back and throw an exception
            meetings.add(index, meetingToReschedule);
            throw new IllegalArgumentException("Rescheduled meeting overlaps with existing meetings.");
        }
    }

    /**
     * Reschedules a policy at the specified index by replacing it with the provided policy.
     *
     * @param index The index of the policy to be rescheduled.
     * @param policy The new policy to replace the existing one.
     */
    public void reschedulePolicy(int index, Policy policy) {
        policies.remove(index);
        policies.add(index, policy);
    }


    public void cancelMeeting(int index) {
        meetings.remove(index);
    }

    public void cancelPolicy(int index) {
        policies.remove(index);
    }

    private boolean isOverlapWithOtherMeetings(Meeting meetingToCheck) {
        LocalDateTime startDateTimeToCheck = LocalDateTime.of(meetingToCheck.getMeetingDate(),
                meetingToCheck.getMeetingTime());
        LocalDateTime endDateTimeToCheck = startDateTimeToCheck.plus(meetingToCheck.getDuration());

        for (Meeting meeting : meetings) {
            LocalDateTime startDateTime = LocalDateTime.of(meeting.getMeetingDate(), meeting.getMeetingTime());
            LocalDateTime endDateTime = startDateTime.plus(meeting.getDuration());

            if (startDateTimeToCheck.isBefore(endDateTime) && endDateTimeToCheck.isAfter(startDateTime)) {
                return true;
            }
        }
        return false;
    }

    public Person getCopy() {
        Person p = new Person(this.name, this.phone, this.email, this.address, this.relationship,
                this.getPolicies(), this.clientStatus, this.getTags(), meetings);

        // Create a deep copy of the meetings
        List<Meeting> copiedMeetings = new ArrayList<>();
        for (Meeting meeting : this.getMeetings()) {
            Meeting copiedMeeting = new Meeting(
                    meeting.getMeetingDate(),
                    meeting.getMeetingTime(),
                    meeting.getDuration(),
                    meeting.getAgenda(),
                    meeting.getNotes()
            );
            copiedMeetings.add(copiedMeeting);
        }
        p.setMeetings(copiedMeetings);

        return p;
    }
    public List<Meeting> getMeetingListCopy(List<Meeting> listToBeCopied) {
        List<Meeting> copiedMeetings = new ArrayList<>();
        for (Meeting meeting : listToBeCopied) {
            Meeting copiedMeeting = new Meeting(
                    meeting.getMeetingDate(),
                    meeting.getMeetingTime(),
                    meeting.getDuration(),
                    meeting.getAgenda(),
                    meeting.getNotes()
            );
            copiedMeetings.add(copiedMeeting);
        }
        return copiedMeetings;
    }

    public List<Policy> getPolicyListCopy(List<Policy> listToBeCopied) {
        List<Policy> copiedPolicies = new ArrayList<>();
        for (Policy policy : listToBeCopied) {
            Policy copiedPolicy = new Policy(
                    policy.value,
                    policy.expiryDate,
                    policy.premium
            );
            copiedPolicies.add(copiedPolicy);
        }
        return copiedPolicies;
    }

    public Optional<Meeting> getEarliestMeeting() {
        return getMeetings().stream().min(Comparator.comparing(Meeting::getStartDateTime));
    }
}
