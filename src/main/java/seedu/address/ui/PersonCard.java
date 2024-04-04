package seedu.address.ui;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Locale;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import seedu.address.model.person.Meeting;
import seedu.address.model.person.Person;
import seedu.address.model.person.Policy;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {
    private static final String FXML = "PersonListCard.fxml";
    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private Label relationship;
    @FXML
    private Accordion policiesAccordion;
    @FXML
    private Label clientStatus;
    @FXML
    private Label meeting;
    @FXML
    private FlowPane tags;
    @FXML
    private Accordion meetingsAccordion;

    /**
     * Creates a {@code PersonCard} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        address.setText(person.getAddress().value);
        email.setText(person.getEmail().value);
        relationship.setText(person.getRelationship().value);

        policiesAccordion.getPanes().clear();

        policiesAccordion.getPanes().clear();
        if (!person.isClient()) {
            policiesAccordion.setVisible(false);
            clientStatus.setVisible(false);
        } else if (person.getPolicies().isEmpty()) {
            TitledPane noPoliciesPane = new TitledPane("No policies assigned",
                    new Label("No policies assigned"));
            noPoliciesPane.setDisable(true);
            policiesAccordion.getPanes().add(noPoliciesPane);
        } else {
            for (Policy policy : person.getPolicies()) {
                TitledPane policyPane = createPolicyEntry(policy);
                policiesAccordion.getPanes().add(policyPane);
            }
        }
        policiesAccordion.setStyle("-fx-background-color: #D9EDBF !important;"
                + "-fx-border-color: rgba(0, 60, 136, 0.8);"
                + "-fx-font-family: 'Lucida Grande', Verdana, Geneva, Lucida, Arial, Helvetica, sans-serif;"
                + "-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: BLACK;");

        clientStatus.setText(person.getClientStatus().toString());
        switch(person.getClientStatus().getStatus()) {
        case 2:
            clientStatus.setStyle("-fx-background-color: #D97930;");
            break;
        case 3:
            clientStatus.setStyle("-fx-background-color: #1fab2f;");
            break;
        default:
            clientStatus.setStyle("-fx-background-color: #d32f2f;");
        }

        tags.getChildren().clear();
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));

        meetingsAccordion.getPanes().clear();
        if (!person.getMeetings().isEmpty()) {
            for (Meeting meeting : person.getMeetings()) {
                TitledPane meetingPane = createMeetingEntry(meeting);
                meetingsAccordion.getPanes().add(meetingPane);
            }
        } else {
            TitledPane noMeetingsPane = new TitledPane("No meetings scheduled",
                    new Label("No scheduled meetings"));
            noMeetingsPane.setDisable(true);
            meetingsAccordion.getPanes().add(noMeetingsPane);
        }
        applyHoverEffect(cardPane);
    }

    private TitledPane createMeetingEntry(Meeting meeting) {
        VBox meetingDetails = new VBox(5); // Padding around the VBox content

        // Create styled labels for the headings
        Label dateHeading = new Label("Date: ");
        dateHeading.setStyle("-fx-font-weight: bold !important; -fx-text-fill: #2a2a2a !important;");
        Label dateLabel = new Label(meeting.getMeetingDate().toString());

        Label timeHeading = new Label("Time: ");
        timeHeading.setStyle("-fx-font-weight: bold !important; -fx-text-fill: #2a2a2a !important;");
        Label timeLabel = new Label(meeting.getMeetingTime().toString());

        Label agendaHeading = new Label("Agenda: ");
        agendaHeading.setStyle("-fx-font-weight: bold !important; -fx-text-fill: #2a2a2a !important;");
        Label agendaLabel = new Label(meeting.getAgenda());

        Label notesHeading = new Label("Notes: ");
        notesHeading.setStyle("-fx-font-weight: bold !important; -fx-text-fill: #2a2a2a !important;");
        Label notesLabel = new Label(meeting.getNotes());

        Label durationHeading = new Label("Duration: ");
        durationHeading.setStyle("-fx-font-weight: bold !important; -fx-text-fill: #2a2a2a !important;");
        Label durationLabel = new Label(formatDuration(meeting.getDuration()));

        // Combine the headings and content into horizontal layouts
        HBox dateBox = new HBox(dateHeading, dateLabel);
        HBox timeBox = new HBox(timeHeading, timeLabel);
        HBox agendaBox = new HBox(agendaHeading, agendaLabel);
        HBox notesBox = new HBox(notesHeading, notesLabel);
        HBox durationBox = new HBox(durationHeading, durationLabel);

        // Add some spacing between the heading and content
        dateBox.setSpacing(5);
        timeBox.setSpacing(5);
        agendaBox.setSpacing(5);
        notesBox.setSpacing(5);
        durationBox.setSpacing(5);

        // Add all HBoxes to the VBox
        meetingDetails.getChildren().addAll(dateBox, timeBox, durationBox, agendaBox, notesBox);

        // Wrap the VBox in a ScrollPane
        ScrollPane scrollPane = new ScrollPane(meetingDetails);
        scrollPane.setFitToHeight(true); // Ensures the scroll pane fits the height of VBox
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Only show the horizontal bar when needed

        // Create the TitledPane
        TitledPane meetingPane = new TitledPane("Meeting on " + meeting.getMeetingDate().toString(), scrollPane);
        meetingPane.setAnimated(true); // Enable animation

        if (meeting.isComingUp()) {
            ImageView bellIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/bell.png")));
            bellIcon.setFitHeight(15);
            bellIcon.setFitWidth(15);

            // Create a container for the title and the bell icon
            Label titleLabel = new Label(meetingPane.getText());
            // Use the same style class as originally applied to the title text
            titleLabel.getStyleClass().add("meeting-title-label");
            HBox titleContainer = new HBox(titleLabel, bellIcon);
            titleContainer.getStyleClass().add("meeting-title-container"); //Apply CSS styling for alignment and spacing
            titleContainer.setAlignment(Pos.CENTER_LEFT); // Align the title and icon to the left
            titleContainer.setSpacing(5); // Set spacing between the title and icon

            // Set the title container as the graphic of the TitledPane
            meetingPane.setGraphic(titleContainer);

            // Since we are using the graphic now, we don't want the text to show up again
            meetingPane.setText("");
        }


        return meetingPane;
    }

    private TitledPane createPolicyEntry(Policy policy) {
        VBox policyDetails = new VBox(5); // Padding around the VBox content

        // Create styled labels for the headings
        Label policyHeading = new Label("Policy: ");
        policyHeading.setStyle("-fx-font-weight: bold !important; -fx-text-fill: #2a2a2a !important;");
        Label policyLabel = new Label(policy.value);

        Label dateHeading = new Label("Expiry Date: ");
        dateHeading.setStyle("-fx-font-weight: bold !important; -fx-text-fill: #2a2a2a !important;");
        Label timeLabel = (policy.expiryDate == null) ? new Label("-") : new Label(policy.expiryDate.toString());

        Label premiumHeading = new Label("Premium: ");
        premiumHeading.setStyle("-fx-font-weight: bold !important; -fx-text-fill: #2a2a2a !important;");
        String formattedPremium = String.format("%.2f", policy.premium);
        Label premiumLabel = (policy.premium == 0.0) ? new Label("-")
                : new Label(formattedPremium + "$");

        // Combine the headings and content into horizontal layouts
        HBox policyBox = new HBox(policyHeading, policyLabel);
        HBox timeBox = new HBox(dateHeading, timeLabel);
        HBox premiumBox = new HBox(premiumHeading, premiumLabel);

        // Add some spacing between the heading and content
        policyBox.setSpacing(5);
        timeBox.setSpacing(5);
        premiumBox.setSpacing(5);

        // Add all HBoxes to the VBox
        policyDetails.getChildren().addAll(policyBox, timeBox, premiumBox);

        ScrollPane scrollPane = new ScrollPane(policyDetails);

        scrollPane.setFitToHeight(true); // Ensures the scroll pane fits the height of VBox
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Only show the horizontal bar when needed

        // Create the TitledPane
        TitledPane policyPane = new TitledPane("Policy: " + policy.value, scrollPane);



        policyPane.setAnimated(true); // Enable animation

        return policyPane;
    }

    private String formatDuration(Duration duration) {
        long seconds = duration.getSeconds();
        long absSeconds = Math.abs(seconds);
        String positive = String.format(
                "%d:%02d:%02d",
                absSeconds / 3600, (
                absSeconds % 3600) / 60,
                absSeconds % 60);
        return seconds < 0 ? "-" + positive : positive;
    }

    private void applyHoverEffect(Node node) {
        DropShadow hoverShadow = new DropShadow();
        hoverShadow.setColor(Color.PLUM);
        hoverShadow.setRadius(10);
        hoverShadow.setSpread(0.5);

        node.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> node.setEffect(hoverShadow));
        node.addEventHandler(MouseEvent.MOUSE_EXITED, e -> node.setEffect(null));
    }

    /**
     * Formats a {@code LocalDate} object into a string representation in the format "01 March 2020".
     *
     * @param date The {@code LocalDate} object to format.
     * @return A string representation of the date in the format "01 March 2020".
     */
    public static String formatLocalDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH);
        return date.format(formatter);
    }
}
