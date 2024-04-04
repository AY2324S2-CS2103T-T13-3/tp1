package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EXPIRY_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POLICY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POLICY_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PREMIUM;

import java.time.LocalDate;
import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.PolicyCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Policy;

/**
 * Parses input arguments and creates a new {@code PolicyCommand} object
 */
public class PolicyCommandParser implements Parser<PolicyCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the {@code PolicyCommand}
     * and returns a {@code PolicyCommand} object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public PolicyCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_POLICY, PREFIX_POLICY_INDEX,
                PREFIX_EXPIRY_DATE, PREFIX_PREMIUM);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, PolicyCommand.MESSAGE_USAGE), ive);
        }

        if (!arePrefixesPresent(argMultimap, PREFIX_POLICY)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, PolicyCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_POLICY, PREFIX_POLICY_INDEX, PREFIX_EXPIRY_DATE,
                PREFIX_PREMIUM);

        String value = argMultimap.getValue(PREFIX_POLICY).get();
        LocalDate expiryDate = null;
        double premium = 0.0;

        if (argMultimap.getValue(PREFIX_EXPIRY_DATE).isPresent()) {
            expiryDate = ParserUtil.parseExpiryDate(argMultimap.getValue(PREFIX_EXPIRY_DATE).get());
        }

        if (argMultimap.getValue(PREFIX_PREMIUM).isPresent()) {
            premium = ParserUtil.parsePremium(argMultimap.getValue(PREFIX_PREMIUM).get());
        }

        if (argMultimap.getValue(PREFIX_POLICY_INDEX).isPresent()) {
            Index policyIndex;
            try {
                policyIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_POLICY_INDEX).orElse(""));
            } catch (ParseException e) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        PolicyCommand.MESSAGE_USAGE), e);
            }
            Policy policy = new Policy(value, expiryDate, premium);

            return new PolicyCommand(index, policyIndex, policy);
        } else {
            if (value.isBlank()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, PolicyCommand.MESSAGE_USAGE));
            }
            Policy policy = new Policy(value, expiryDate, premium);

            return new PolicyCommand(index, policy);
        }
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
