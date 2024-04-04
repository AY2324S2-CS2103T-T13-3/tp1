package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class ClientStatusSummaryValuesTest {

    @Test
    public void constructor_invalidValues_throwsIllegalArgumentException() {
        int invalidNumClients = 3;
        int numStart = 1;
        int numMid = 1;
        int numEnd = 2;
        assertThrows(IllegalArgumentException.class, () ->
                new ClientStatusSummaryValues(invalidNumClients, numStart, numMid, numEnd));
    }

    @Test
    public void constructor_validValues_success() {
        ClientStatusSummaryValues clientStatusSummaryValues =
                new ClientStatusSummaryValues(6, 1, 2, 3);

        assertTrue(clientStatusSummaryValues.numberOfClients == 6);
        assertTrue(clientStatusSummaryValues.numberOfStart == 1);
        assertTrue(clientStatusSummaryValues.numberOfMid == 2);
        assertTrue(clientStatusSummaryValues.numberOfEnd == 3);
    }

    @Test
    public void equals() {
        ClientStatusSummaryValues clientStatusSummaryValues =
                new ClientStatusSummaryValues(6, 1, 2, 3);

        // same object -> returns true
        assertTrue(clientStatusSummaryValues.equals(clientStatusSummaryValues));

        // same values -> returns true
        ClientStatusSummaryValues clientStatusSummaryValuesCopy =
                new ClientStatusSummaryValues(6, 1, 2, 3);
        assertTrue(clientStatusSummaryValues.equals(clientStatusSummaryValuesCopy));

        // different types -> returns false
        assertFalse(clientStatusSummaryValues.equals(1));

        // null -> returns false
        assertFalse(clientStatusSummaryValues.equals(null));

        // different values -> returns false
        ClientStatusSummaryValues differentClientStatusSummaryValues =
                new ClientStatusSummaryValues(7, 2, 2, 3);
        assertFalse(clientStatusSummaryValues.equals(differentClientStatusSummaryValues));
    }
}
