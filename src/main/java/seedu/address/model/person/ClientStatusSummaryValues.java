package seedu.address.model.person;

import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents the summary values of clients' status in the person list.
 */
public class ClientStatusSummaryValues {
    public static final String MESSAGE_CONSTRAINTS =
            "Number of clients and sum of number of client statuses are unequal";
    public final int numberOfClients;
    public final int numberOfStart;
    public final int numberOfMid;
    public final int numberOfEnd;

    /**
     * Creates a {@code ClientStatusSummaryValues} with the given values.
     */
    public ClientStatusSummaryValues(int numClients, int numStart, int numMid, int numEnd) {
        checkArgument(numClients == numStart + numMid + numEnd, MESSAGE_CONSTRAINTS);
        numberOfClients = numClients;
        numberOfStart = numStart;
        numberOfMid = numMid;
        numberOfEnd = numEnd;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ClientStatusSummaryValues)) {
            return false;
        }

        ClientStatusSummaryValues otherClientStatusSummaryValues = (ClientStatusSummaryValues) other;
        return this.numberOfClients == otherClientStatusSummaryValues.numberOfClients
                && this.numberOfStart == otherClientStatusSummaryValues.numberOfStart
                && this.numberOfMid == otherClientStatusSummaryValues.numberOfMid
                && this.numberOfEnd == otherClientStatusSummaryValues.numberOfEnd;
    }
}
