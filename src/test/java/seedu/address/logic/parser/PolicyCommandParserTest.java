package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POLICY;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.PolicyCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Policy;

public class PolicyCommandParserTest {
    private PolicyCommandParser parser = new PolicyCommandParser();
    private final String sample = "some policy";
    private final Policy samplePolicy = new Policy(sample);
    private final List<Policy> nonEmptyPolicy = new ArrayList<>();
    private final Policy emptyPolicy = new Policy("");

    @Test
    public void parse_indexSpecified_success() {
        nonEmptyPolicy.add(samplePolicy);
        // have policy
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + " " + PREFIX_POLICY + sample;
        PolicyCommand expectedCommand = new PolicyCommand(INDEX_FIRST_PERSON, samplePolicy);
        assertParseSuccess(parser, userInput, expectedCommand);

        // no policy
        userInput = targetIndex.getOneBased() + " " + PREFIX_POLICY + " ";
        assertParseFailure(parser, userInput,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, PolicyCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidInput_parsesIncorrectly() {
        String args = "1 po/Policy ABC pi/dummy ";
        assertParseFailure(parser, args, String.format(MESSAGE_INVALID_COMMAND_FORMAT, PolicyCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validInput_parsesCorrectly() throws ParseException {
        String args = "1 po/PolicyName ed/31-12-2024 pm/100.50";
        try {
            PolicyCommand policyCommand = parser.parse(args);
            assertEquals(Index.fromOneBased(1), policyCommand.getIndex());

            Policy expectedPolicies = new Policy("PolicyName", LocalDate.of(2024, 12, 31),
                    100.50);
            assertEquals(expectedPolicies, policyCommand.getPolicy());
        } catch (ParseException e) {
            throw new ParseException(e.getMessage());
        }

        String args2 = "1 po/PolicyName pm/100.50";
        try {
            PolicyCommand policyCommand = parser.parse(args2);
            assertEquals(Index.fromOneBased(1), policyCommand.getIndex());

            Policy expectedPolicies = new Policy("PolicyName", null, 100.50);
            assertEquals(expectedPolicies, policyCommand.getPolicy());
        } catch (ParseException e) {
            throw new ParseException(e.getMessage());
        }

        String args3 = "1 po/PolicyName ed/31-12-2024";
        try {
            PolicyCommand policyCommand = parser.parse(args3);
            assertEquals(Index.fromOneBased(1), policyCommand.getIndex());

            Policy expectedPolicies = new Policy("PolicyName", LocalDate.of(2024, 12, 31),
                    0.0);
            assertEquals(expectedPolicies, policyCommand.getPolicy());
        } catch (ParseException e) {
            throw new ParseException(e.getMessage());
        }

        String args4 = "1 po/PolicyName pi/1 ed/31-12-2024";
        try {
            PolicyCommand policyCommand = parser.parse(args4);
            assertEquals(Index.fromOneBased(1), policyCommand.getIndex());

            Policy expectedPolicies = new Policy("PolicyName", LocalDate.of(2024, 12, 31),
                    0.0);
            PolicyCommand expectedPolicyCommand = new PolicyCommand(Index.fromOneBased(1),
                    Index.fromOneBased(1), expectedPolicies);
            Index expectedIndex = Index.fromOneBased(1);


            assertEquals(expectedPolicyCommand, policyCommand);
            assertEquals(expectedPolicies, policyCommand.getPolicy());
            assertEquals(expectedIndex, policyCommand.getPolicyIndex());
        } catch (ParseException e) {
            throw new ParseException(e.getMessage());
        }
    }

    @Test
    public void parse_missingCompulsoryField_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, PolicyCommand.MESSAGE_USAGE);

        // no policy prefix
        assertParseFailure(parser, " 1", expectedMessage);

        // no parameters
        assertParseFailure(parser, " ", expectedMessage);

        // no index
        assertParseFailure(parser, " po/Policy 1", expectedMessage);
    }
}
