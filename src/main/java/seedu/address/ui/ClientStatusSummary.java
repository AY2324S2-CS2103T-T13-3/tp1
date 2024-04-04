package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import seedu.address.model.person.ClientStatusSummaryValues;

/**
 * A ui for the client status summary that is displayed above the person list panel.
 */
public class ClientStatusSummary extends UiPart<Region> {
    private static final String FXML = "ClientStatusSummary.fxml";

    @FXML
    private Label startSummary;
    @FXML
    private Label midSummary;
    @FXML
    private Label endSummary;

    /**
     * Creates a {@code ClientStatusSummary} with the given (@code ClientStatusSummaryValues}.
     */
    public ClientStatusSummary(ClientStatusSummaryValues clientStatusSummaryValues) {
        super(FXML);
        startSummary.setText("Yet to start - "
                + clientStatusSummaryValues.numberOfStart
                + "/"
                + clientStatusSummaryValues.numberOfClients);
        midSummary.setText("In progress - "
                + clientStatusSummaryValues.numberOfMid
                + "/"
                + clientStatusSummaryValues.numberOfClients);
        endSummary.setText("Completed - "
                + clientStatusSummaryValues.numberOfEnd
                + "/"
                + clientStatusSummaryValues.numberOfClients);
    }
}
