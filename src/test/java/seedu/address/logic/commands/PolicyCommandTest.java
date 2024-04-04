package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_POLICY_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_POLICY_BOB;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIFTH_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_POLICY;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.Policy;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for PolicyCommand.
 */
public class PolicyCommandTest {

    private static final Policy POLICY_STUB = new Policy("Some policy");

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_addPolicyUnfilteredList_success() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(firstPerson).withPolicy(POLICY_STUB).build();

        PolicyCommand policyCommand = new PolicyCommand(INDEX_FIRST_PERSON, POLICY_STUB);

        String expectedMessage = String.format(PolicyCommand.MESSAGE_ADD_POLICY_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);
        expectedModel.commitAddressBook();

        assertCommandSuccess(policyCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_editPolicyFilteredList_success() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(firstPerson).build();

        List<Policy> policies = new ArrayList<>(firstPerson.getPolicies());
        policies.set(INDEX_FIRST_POLICY.getZeroBased(), POLICY_STUB);
        editedPerson.setPolicies(policies);

        PolicyCommand policyCommand = new PolicyCommand(INDEX_FIRST_PERSON, INDEX_FIRST_POLICY, POLICY_STUB);

        String expectedMessage = String.format(PolicyCommand.MESSAGE_POLICY_RESCHEDULED_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);
        expectedModel.commitAddressBook();

        assertCommandSuccess(policyCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_deletePolicyUnfilteredList_success() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(firstPerson).build();

        editedPerson.setPolicies(new ArrayList<>());

        PolicyCommand remarkCommand = new PolicyCommand(INDEX_SECOND_PERSON, INDEX_FIRST_POLICY, new Policy(""));
        String expectedMessage =
                String.format(PolicyCommand.MESSAGE_DELETE_POLICY_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);
        expectedModel.commitAddressBook();

        assertCommandSuccess(remarkCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()))
                .withPolicy(POLICY_STUB).build();

        PolicyCommand policyCommand = new PolicyCommand(INDEX_FIRST_PERSON, POLICY_STUB);

        String expectedMessage = String.format(PolicyCommand.MESSAGE_ADD_POLICY_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);
        expectedModel.commitAddressBook();

        assertCommandSuccess(policyCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        PolicyCommand policyCommand = new PolicyCommand(outOfBoundIndex,
                new Policy(VALID_POLICY_BOB));

        assertCommandFailure(policyCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_fullPoliciesFilteredList_failure() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        ArrayList<Policy> policies = new ArrayList<>(Collections.nCopies(5, new Policy("Policy ABC")));
        Person editedPerson = new PersonBuilder(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()))
                .build();
        editedPerson.setPolicies(policies);

        PolicyCommand policyCommand = new PolicyCommand(INDEX_FIRST_PERSON, POLICY_STUB);

        String expectedMessage = "Cannot have more than 5 policies.";

        Model editedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        editedModel.setPerson(firstPerson, editedPerson);
        editedModel.commitAddressBook();

        assertCommandFailure(policyCommand, editedModel, expectedMessage);
    }

    @Test
    public void execute_invalidRelationshipFilteredList_failure() {
        PolicyCommand policyCommand = new PolicyCommand(INDEX_FIFTH_PERSON, POLICY_STUB);

        assertCommandFailure(policyCommand, model, PolicyCommand.MESSAGE_PERSON_NOT_CLIENT_FAILURE);
    }

    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        PolicyCommand policyCommand = new PolicyCommand(outOfBoundIndex,
                new Policy(VALID_POLICY_BOB));
        assertCommandFailure(policyCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidPolicyIndexFilteredList_failure() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        List<Policy> policies = firstPerson.getPolicies();
        Index outOfBoundIndex = Index.fromZeroBased(policies.size());

        assertTrue(outOfBoundIndex.getZeroBased() >= policies.size());

        PolicyCommand policyCommand = new PolicyCommand(Index.fromOneBased(1), outOfBoundIndex,
                new Policy(VALID_POLICY_BOB));

        assertCommandFailure(policyCommand, model, PolicyCommand.MESSAGE_POLICY_INVALID_INDEX);
    }

    @Test
    public void equals() {
        final PolicyCommand standardCommand = new PolicyCommand(INDEX_FIRST_PERSON,
                new Policy(VALID_POLICY_AMY));
        final PolicyCommand standardCommand2 = new PolicyCommand(INDEX_FIRST_PERSON, INDEX_FIRST_POLICY,
                new Policy(VALID_POLICY_AMY));

        // same values -> returns true
        PolicyCommand commandWithSameValues = new PolicyCommand(INDEX_FIRST_PERSON,
                new Policy(VALID_POLICY_AMY));
        PolicyCommand commandWithSameValues2 = new PolicyCommand(INDEX_FIRST_PERSON, INDEX_FIRST_POLICY,
                new Policy(VALID_POLICY_AMY));
        assertTrue(standardCommand.equals(commandWithSameValues));
        assertTrue(standardCommand2.equals(commandWithSameValues2));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));
        assertTrue(standardCommand2.equals(standardCommand2));

        // null -> returns false
        assertFalse(standardCommand.equals(null));
        assertFalse(standardCommand2.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));
        assertFalse(standardCommand2.equals(new ClearCommand()));

        // different policyIndex -> returns false
        PolicyCommand commandWithDifferentPolicyIndex = new PolicyCommand(INDEX_FIRST_PERSON, Index.fromZeroBased(3),
                new Policy(VALID_POLICY_AMY));
        PolicyCommand commandWithDifferentPolicy = new PolicyCommand(INDEX_FIRST_PERSON, INDEX_FIRST_POLICY,
                new Policy(VALID_POLICY_BOB));
        assertFalse(standardCommand2.equals(commandWithDifferentPolicyIndex));
        assertFalse(standardCommand2.equals(commandWithDifferentPolicy));
    }
}
