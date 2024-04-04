package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Policy;
import seedu.address.model.person.Relationship;
import seedu.address.model.tag.Tag;



/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";
    public static final String[] POSSIBLE_DAY_OF_WEEK_FORMATS = {"EEEE", "EEE", "EEEEE"};
    public static final String[] POSSIBLE_DATE_FORMATS = {
        "yyyy-MM-dd",
        "dd-MM-yyyy",
        "yyyyMMdd",
        "yyyy MM dd",
        "dd MM yyyy",
        "ddMMyyyy" // Add more formats as needed
    };

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Address parseAddress(String address) throws ParseException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        if (!Address.isValidAddress(trimmedAddress)) {
            throw new ParseException(Address.MESSAGE_CONSTRAINTS);
        }
        return new Address(trimmedAddress);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String relationship} into a {@code Relationship}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code relationship} is invalid.
     */
    public static Relationship parseRelationship(String relationship) throws ParseException {
        requireNonNull(relationship);
        String trimmedRelationship = relationship.trim();
        if (!Relationship.isValidRelationship(trimmedRelationship)) {
            throw new ParseException(Relationship.MESSAGE_CONSTRAINTS);
        }
        return new Relationship(trimmedRelationship);
    }

    /**
     * Parses a {@code String expiryDate} into a {@code LocalDateTime}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code expiryDate} is invalid.
     */
    public static LocalDate parseExpiryDate(String expiryDate) throws ParseException {
        requireNonNull(expiryDate);
        String trimmedExpiryDate = expiryDate.trim();
        if (!Policy.isValidExpiryDate(trimmedExpiryDate)) {
            throw new ParseException(Policy.EXPIRY_DATE_MESSAGE_CONSTRAINTS);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse(trimmedExpiryDate, formatter);
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static double parsePremium(String premium) throws ParseException {
        requireNonNull(premium);
        String trimmedPremium = premium.trim();
        if (!Policy.isValidPremium(trimmedPremium)) {
            throw new ParseException(Policy.PREMIUM_MESSAGE_CONSTRAINTS);
        }
        return Double.parseDouble(trimmedPremium);
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }


    /**
     * Parses a {@code String date} into a {@code LocalDate}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code date} is invalid.
     */
    public static LocalDate parseDate(String dateString) throws ParseException {
        requireNonNull(dateString);
        String trimmedDate = dateString.trim();
        for (String format : POSSIBLE_DATE_FORMATS) {
            try {
                LocalDate date = LocalDate.parse(trimmedDate, DateTimeFormatter.ofPattern(format));
                return date;
            } catch (Exception ignored) {
                // If parsing fails, ignore the exception and try the next format
            }
        }

        throw new ParseException("Invalid date format. Use YYYY-MM-DD.");
    }

    /**
     * Parses a {@code String time} into a {@code LocalTime}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code time} is invalid.
     */
    public static LocalTime parseTime(String time) throws ParseException {
        requireNonNull(time);
        String trimmedTime = time.trim();
        try {
            return LocalTime.parse(trimmedTime, DateTimeFormatter.ISO_LOCAL_TIME);
        } catch (DateTimeParseException e) {
            throw new ParseException("Invalid time format. Use HH:MM.");
        }
    }

    /**
     * Parses a {@code String durationStr} into a {@code Duration}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code durationStr} is invalid.
     */
    public static Duration parseDuration(String durationStr) throws ParseException {
        requireNonNull(durationStr);
        try {
            long minutes = Long.parseLong(durationStr.trim());
            if (minutes < 0) {
                throw new ParseException("Duration must be a non-negative integer.");
            }
            return Duration.ofMinutes(minutes);
        } catch (NumberFormatException e) {
            throw new ParseException("Invalid duration format. Use minutes as an integer.");
        }
    }

    /**
     * Converts the string of date and time to a LocalDateTime format
     * @param stringDate input date string
     * @param stringTime input time string
     * @return LocalDateTime the meeting date time
     * @throws ParseException for invalid inputs
     */
    public static LocalDateTime parseLocalDateTime(String stringDate, String stringTime) throws ParseException {
        try {
            String stringDateAndTime = stringDate + " " + stringTime;
            LocalDate meetingDate;
            LocalTime meetingTime;
            if (isDayOfWeek(stringDateAndTime)) {
                LocalDateTime datetime = getLocalDateTimeFromDayOfWeek(stringDateAndTime);
                meetingDate = datetime.toLocalDate();
                meetingTime = datetime.toLocalTime();
            } else {
                meetingDate = ParserUtil.parseDate(stringDate);
                meetingTime = ParserUtil.parseTime(stringTime);
            }
            LocalDateTime meetingDateTime = LocalDateTime.of(meetingDate, meetingTime);
            return meetingDateTime;
        } catch (ParseException e) {
            throw new ParseException("Invalid date or time format. Use YYYY-MM-DD for date and HH:MM for time.");
        }
    }

    /**
     * Checks if the date provided is in day of week format
     * @param dateTime the date input string
     * @return if true if it is in the day of week format
     */
    public static boolean isDayOfWeek(String dateTime) {
        String date = dateTime.split("\\s+")[0];
        for (String format : POSSIBLE_DAY_OF_WEEK_FORMATS) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
                DayOfWeek.from(formatter.parse(date));
                return true;
            } catch (Exception ignored) {
                // If parsing fails, ignore the exception and try the next format
            }
        }
        return false;
    }
    public static LocalDateTime getLocalDateTimeFromDayOfWeek(String dateString) throws ParseException {
        String[] splitdateString;
        LocalTime chosenTime;
        try {
            splitdateString = dateString.split("\\s+");
            chosenTime = LocalTime.parse(splitdateString[1], DateTimeFormatter.ofPattern("HH:mm"));
        } catch (Exception e) {
            throw new ParseException("Invalid time, use the format of \"day HH:mm\"");
        }
        for (String format : POSSIBLE_DAY_OF_WEEK_FORMATS) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
                DayOfWeek dayOfWeek = DayOfWeek.from(formatter.parse(splitdateString[0]));
                LocalDate currentDate = LocalDate.now();
                LocalDate currentDay = currentDate.with(dayOfWeek);
                LocalDate nextOccurrence = currentDate.with(TemporalAdjusters.nextOrSame(dayOfWeek));
                nextOccurrence = checkIfTimeHasPassedOnSameDayAsCurrent(chosenTime, dayOfWeek,
                        currentDay, nextOccurrence);
                LocalDateTime dateTime = nextOccurrence.atTime(chosenTime);
                return dateTime;
            } catch (Exception ignored) {
                // If parsing fails, ignore the exception and try the next format
            }
        }
        throw new ParseException("Invalid format for day of week");
    }

    /**
     * Checks if the time when the user inputted the command has passed
     * if the day chosen is same as the current day as the
     * TemporalAdjusters.nextOrSame(current day) does not take into account
     * if the time chosen has already passed
     *
     * @param time time of input
     * @param InputDayOfWeek day inputted by user
     * @param currentDay the current day
     * @param nextOccurrence the date returned by TemporalAdjusters.nextOrSame(current day)
     * @return LocalDate of next occurence, taking time into account
     */
    public static LocalDate checkIfTimeHasPassedOnSameDayAsCurrent(LocalTime time, DayOfWeek InputDayOfWeek,
                                                                    LocalDate currentDay, LocalDate nextOccurrence) {
        if (nextOccurrence.with(InputDayOfWeek) == currentDay) {
            LocalTime currentTime = LocalTime.now();
            if (time.isBefore(currentTime)) {
                nextOccurrence = nextOccurrence.plusWeeks(1);
            }
        }
        return nextOccurrence;
    }


}
