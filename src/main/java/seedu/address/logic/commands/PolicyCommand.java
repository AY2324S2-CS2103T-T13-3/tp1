package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EXPIRY_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POLICY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POLICY_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PREMIUM;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;
import java.util.Objects;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.Policy;

/**
 * Changes the policy of an existing person in the address book.
 */
public class PolicyCommand extends Command {

    public static final String COMMAND_WORD = "policy";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the policy of the person identified "
            + "by the index number used in the last person listing. \n"
            + "Include policy index and valid policy or expiry date or premium to edit the current policy.\n"
            + "Include policy index and empty policy value to delete the current policy.\n"
            + "Don't include policy index if want to add a new policy\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_POLICY + "[POLICY] "
            + "[" + PREFIX_POLICY_INDEX + "POLICY INDEX]"
            + "[" + PREFIX_EXPIRY_DATE + "EXPIRY DATE] "
            + "[" + PREFIX_PREMIUM + "PREMIUM]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_POLICY + "Policy XYZ "
            + PREFIX_EXPIRY_DATE + "01-01-2020";

    public static final String MESSAGE_ADD_POLICY_SUCCESS = "Added policy to Person: %1$s";
    public static final String MESSAGE_DELETE_POLICY_SUCCESS = "Removed policy from Person: %1$s";
    public static final String MESSAGE_PERSON_NOT_CLIENT_FAILURE =
            "Invalid person. Only clients can be assigned a policy";
    public static final String MESSAGE_POLICY_RESCHEDULED_SUCCESS = "Policy rescheduled successfully: %1$s";
    public static final String MESSAGE_POLICY_INVALID_INDEX = "Invalid index provided for policy list";

    private final Index index;
    private final Index policyIndex;
    private final Policy policy;

    /**
     * Constructs a PolicyCommand to add a new policy to the person at the specified index.
     *
     * @param index  The index of the person in the filtered person list.
     * @param policy The policy to be added.
     */
    public PolicyCommand(Index index, Policy policy) {
        requireAllNonNull(index, policy);

        this.index = index;
        this.policyIndex = null;
        this.policy = policy;
    }

    /**
     * Constructs a PolicyCommand to edit or delete an existing policy of the person at the specified index.
     *
     * @param index       The index of the person in the filtered person list.
     * @param policyIndex The index of the policy to be edited or deleted.
     * @param policy      The new policy to replace the existing policy (or null if deleting).
     */
    public PolicyCommand(Index index, Index policyIndex, Policy policy) {
        requireAllNonNull(index, policyIndex, policy);

        this.index = index;
        this.policyIndex = policyIndex;
        this.policy = policy;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEditOriginal = lastShownList.get(index.getZeroBased());

        if (!personToEditOriginal.isClient()) {
            throw new CommandException(MESSAGE_PERSON_NOT_CLIENT_FAILURE);
        }

        if (policyIndex == null) {
            Person personToUpdated = personToEditOriginal.getCopy();

            try {
                personToUpdated.addPolicy(this.policy);
            } catch (IllegalArgumentException e) {
                throw new CommandException(e.getMessage());
            }

            model.setPerson(personToEditOriginal, personToUpdated);
            model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
            model.commitAddressBook();

            return new CommandResult(String.format(MESSAGE_ADD_POLICY_SUCCESS,
                    Messages.format(personToUpdated)));
        } else {
            List<Policy> policies = personToEditOriginal.getPolicies();
            if (policies.size() <= (policyIndex.getZeroBased())) {
                throw new CommandException(MESSAGE_POLICY_INVALID_INDEX);
            }

            Person personToUpdated = personToEditOriginal.getCopy();

            if (policy.value.isBlank()) {
                // delete policy if policy value is blank
                personToUpdated.cancelPolicy(policyIndex.getZeroBased());

                model.setPerson(personToEditOriginal, personToUpdated);
                model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
                model.commitAddressBook();
                return new CommandResult(String.format(MESSAGE_DELETE_POLICY_SUCCESS,
                        Messages.format(personToUpdated)));
            } else {
                // edit policy if policy value exist
                personToUpdated.reschedulePolicy(policyIndex.getZeroBased(), policy);

                model.setPerson(personToEditOriginal, personToUpdated);
                model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
                model.commitAddressBook();
                return new CommandResult(String.format(MESSAGE_POLICY_RESCHEDULED_SUCCESS,
                        Messages.format(personToUpdated)));
            }
        }
    }

    public Index getIndex() {
        return this.index;
    }

    public Policy getPolicy() {
        return this.policy;
    }

    public Index getPolicyIndex() {
        return this.policyIndex;
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PolicyCommand)) {
            return false;
        }

        // state check
        PolicyCommand e = (PolicyCommand) other;
        return index.equals(e.index)
                && Objects.equals(policyIndex, e.policyIndex)
                && policy.equals(e.policy);
    }
}
