package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.logic.parser.ParserUtil.checkIfTimeHasPassedOnSameDayAsCurrent;
import static seedu.address.logic.parser.ParserUtil.parseLocalDateTime;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Relationship;
import seedu.address.model.tag.Tag;

public class ParserUtilTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_NAME = "Rachel Walker";
    private static final String VALID_PHONE = "123456";
    private static final String VALID_ADDRESS = "123 Main Street #0505";
    private static final String VALID_EMAIL = "rachel@example.com";
    private static final String VALID_TAG_1 = "friend";
    private static final String VALID_TAG_2 = "neighbour";

    private static final String WHITESPACE = " \t\r\n";

    @Test
    public void parseIndex_invalidInput_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseIndex("10 a"));
    }

    @Test
    public void parseIndex_outOfRangeInput_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_INVALID_INDEX, ()
            -> ParserUtil.parseIndex(Long.toString(Integer.MAX_VALUE + 1)));
    }

    @Test
    public void parseIndex_validInput_success() throws Exception {
        // No whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("1"));

        // Leading and trailing whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("  1  "));
    }

    @Test
    public void parseName_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseName((String) null));
    }

    @Test
    public void parseName_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseName(INVALID_NAME));
    }

    @Test
    public void parseName_validValueWithoutWhitespace_returnsName() throws Exception {
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(VALID_NAME));
    }

    @Test
    public void parseName_validValueWithWhitespace_returnsTrimmedName() throws Exception {
        String nameWithWhitespace = WHITESPACE + VALID_NAME + WHITESPACE;
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(nameWithWhitespace));
    }

    @Test
    public void parsePhone_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parsePhone((String) null));
    }

    @Test
    public void parsePhone_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parsePhone(INVALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithoutWhitespace_returnsPhone() throws Exception {
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(VALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithWhitespace_returnsTrimmedPhone() throws Exception {
        String phoneWithWhitespace = WHITESPACE + VALID_PHONE + WHITESPACE;
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(phoneWithWhitespace));
    }

    @Test
    public void parseAddress_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseAddress((String) null));
    }

    @Test
    public void parseAddress_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseAddress(INVALID_ADDRESS));
    }

    @Test
    public void parseAddress_validValueWithoutWhitespace_returnsAddress() throws Exception {
        Address expectedAddress = new Address(VALID_ADDRESS);
        assertEquals(expectedAddress, ParserUtil.parseAddress(VALID_ADDRESS));
    }

    @Test
    public void parseAddress_validValueWithWhitespace_returnsTrimmedAddress() throws Exception {
        String addressWithWhitespace = WHITESPACE + VALID_ADDRESS + WHITESPACE;
        Address expectedAddress = new Address(VALID_ADDRESS);
        assertEquals(expectedAddress, ParserUtil.parseAddress(addressWithWhitespace));
    }

    @Test
    public void parseEmail_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseEmail((String) null));
    }

    @Test
    public void parseEmail_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseEmail(INVALID_EMAIL));
    }

    @Test
    public void parseEmail_validValueWithoutWhitespace_returnsEmail() throws Exception {
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(VALID_EMAIL));
    }

    @Test
    public void parseEmail_validValueWithWhitespace_returnsTrimmedEmail() throws Exception {
        String emailWithWhitespace = WHITESPACE + VALID_EMAIL + WHITESPACE;
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(emailWithWhitespace));
    }

    @Test
    public void parseTag_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTag(null));
    }

    @Test
    public void parseTag_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTag(INVALID_TAG));
    }

    @Test
    public void parseTag_validValueWithoutWhitespace_returnsTag() throws Exception {
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(VALID_TAG_1));
    }

    @Test
    public void parseTag_validValueWithWhitespace_returnsTrimmedTag() throws Exception {
        String tagWithWhitespace = WHITESPACE + VALID_TAG_1 + WHITESPACE;
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(tagWithWhitespace));
    }

    @Test
    public void parseTags_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTags(null));
    }

    @Test
    public void parseTags_collectionWithInvalidTags_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, INVALID_TAG)));
    }

    @Test
    public void parseTags_emptyCollection_returnsEmptySet() throws Exception {
        assertTrue(ParserUtil.parseTags(Collections.emptyList()).isEmpty());
    }

    @Test
    public void parseTags_collectionWithValidTags_returnsTagSet() throws Exception {
        Set<Tag> actualTagSet = ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, VALID_TAG_2));
        Set<Tag> expectedTagSet = new HashSet<Tag>(Arrays.asList(new Tag(VALID_TAG_1), new Tag(VALID_TAG_2)));

        assertEquals(expectedTagSet, actualTagSet);
    }

    @Test
    public void parseDate_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseDate(null));
    }

    @Test
    public void parseDate_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseDate("2025-02-41"));
        assertThrows(ParseException.class, () -> ParserUtil.parseDate("not a date"));
    }

    @Test
    public void parseDate_validValue_returnsLocalDate() throws Exception {
        LocalDate expectedDate = LocalDate.of(2021, 1, 1);
        assertEquals(expectedDate, ParserUtil.parseDate("2021-01-01"));
    }

    @Test
    public void parseTime_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTime(null));
    }

    @Test
    public void parseTime_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTime("25:00"));
        assertThrows(ParseException.class, () -> ParserUtil.parseTime("not a time"));
    }

    @Test
    public void parseTime_validValue_returnsLocalTime() throws Exception {
        LocalTime expectedTime = LocalTime.of(14, 30);
        assertEquals(expectedTime, ParserUtil.parseTime("14:30"));
    }

    @Test
    public void parseDuration_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseDuration(null));
    }

    @Test
    public void parseDuration_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseDuration("not a number"));
        assertThrows(ParseException.class, () -> ParserUtil.parseDuration("-1"));
    }

    @Test
    public void parseDuration_validValue_returnsDuration() throws Exception {
        Duration expectedDuration = Duration.ofMinutes(90);
        assertEquals(expectedDuration, ParserUtil.parseDuration("90"));
    }

    @Test
    public void parseDate_emptyString_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseDate(""));
    }

    @Test
    public void parseDate_validValueWithWhitespace_returnsLocalDate() throws Exception {
        LocalDate expectedDate = LocalDate.of(2021, 1, 1);
        assertEquals(expectedDate, ParserUtil.parseDate(" 2021-01-01 "));
    }

    @Test
    public void parseTime_emptyString_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTime(""));
    }

    @Test
    public void parseTime_validValueWithWhitespace_returnsLocalTime() throws Exception {
        LocalTime expectedTime = LocalTime.of(14, 30);
        assertEquals(expectedTime, ParserUtil.parseTime(" 14:30 "));
    }

    @Test
    public void parseDuration_emptyString_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseDuration(""));
    }

    @Test
    public void parseDuration_validValueWithWhitespace_returnsDuration() throws Exception {
        Duration expectedDuration = Duration.ofMinutes(90);
        assertEquals(expectedDuration, ParserUtil.parseDuration(" 90 "));
    }

    @Test
    public void parseRelationship_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseRelationship(null));
    }

    @Test
    public void parseRelationship_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseRelationship(" "));
        assertThrows(ParseException.class, () -> ParserUtil.parseRelationship("123"));
    }

    @Test
    public void parseRelationship_validValueWithoutWhitespace_returnsRelationship() throws Exception {
        Relationship expectedRelationship = new Relationship("client");
        assertEquals(expectedRelationship, ParserUtil.parseRelationship("client"));
    }

    @Test
    public void parseRelationship_validValueWithWhitespace_returnsTrimmedRelationship() throws Exception {
        String relationshipWithWhitespace = " client ";
        Relationship expectedRelationship = new Relationship("client");
        assertEquals(expectedRelationship, ParserUtil.parseRelationship(relationshipWithWhitespace));
    }


    @Test
    public void parseLocalDateTime_validInput_success() throws Exception {
        // Valid date and time
        assertEquals(LocalDateTime.of(2023, 10, 25, 14, 30),
                parseLocalDateTime("2023-10-25", "14:30"));

        // Valid day of week with time
        LocalDate nearestWednesday = LocalDate.now().with(java.time.temporal.TemporalAdjusters
                        .nextOrSame(java.time.DayOfWeek.WEDNESDAY));
        LocalDate nextWednesday = checkIfTimeHasPassedOnSameDayAsCurrent(LocalTime.parse("16:00"),
                DayOfWeek.WEDNESDAY, nearestWednesday, nearestWednesday);
        assertEquals(LocalDateTime.of(nextWednesday, LocalTime.of(16, 0)),
                parseLocalDateTime("Wednesday", "16:00"));
    }

    //    @Test
    //    public void parseLocalDateTime_invalidDate_throwsParseException() {
    //        // Invalid date format
    //        assertThrows(ParseException.class, () ->
    //                ParserUtil.parseLocalDateTime("2023/10/25", "14:30"));
    //
    //        // Non-existent date
    //        assertThrows(ParseException.class, () ->
    //                ParserUtil.parseLocalDateTime("2023-02-30", "14:30"));
    //    }
    @Test
    public void parseLocalDateTime_invalidTime_throwsParseException() {

        // Non-existent time
        assertThrows(ParseException.class, () ->
                ParserUtil.parseLocalDateTime("2023-10-25", "25:00"));
    }

    @Test
    public void parseLocalDateTime_invalidDayOfWeek_throwsParseException() {
        // Invalid day of week
        assertThrows(ParseException.class, () ->
                parseLocalDateTime("Funday", "14:30"));

        // Invalid time format with day of week
        assertThrows(ParseException.class, () ->
                parseLocalDateTime("Monday", "14:30:00"));
    }



}
