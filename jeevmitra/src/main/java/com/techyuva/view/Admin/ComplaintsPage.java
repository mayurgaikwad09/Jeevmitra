package com.techyuva.view.Admin;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.Optional;

public class ComplaintsPage extends VBox {

    private final String green = "#075747";
    private final String darkGreen = "#034638";
    private final String background = "#F4FAF8";

    private final ObservableList<Complaint> complaints =
            FXCollections.observableArrayList();

    private FilteredList<Complaint> filteredComplaints;
    private TableView<Complaint> table;

    private Label totalLabel;
    private Label pendingLabel;
    private Label progressLabel;
    private Label resolvedLabel;

    private TextField searchField;
    private ComboBox<String> statusFilter;

    public ComplaintsPage(Runnable backAction) {

        setSpacing(18);
        setPadding(new Insets(25));
        setStyle("-fx-background-color: " + background + ";");

        addSampleComplaints();

        Label title = new Label("Complaints Management");
        title.setStyle(
            "-fx-font-size: 28px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + darkGreen + ";"
        );

        Label subtitle = new Label(
            "Review user complaints and manage their resolution."
        );
        subtitle.setStyle(
            "-fx-font-size: 13px; -fx-text-fill: #64748B;"
        );

        Button backButton = new Button("← Back to Admin Dashboard");
        backButton.setStyle(buttonStyle());
        backButton.setOnAction(e -> backAction.run());

        VBox heading = new VBox(8, title, subtitle, backButton);

        totalLabel = new Label("0");
        pendingLabel = new Label("0");
        progressLabel = new Label("0");
        resolvedLabel = new Label("0");

        HBox stats = new HBox(
            15,
            createStatCard("Total Complaints", totalLabel),
            createStatCard("Pending", pendingLabel),
            createStatCard("In Progress", progressLabel),
            createStatCard("Resolved", resolvedLabel)
        );

        searchField = new TextField();
        searchField.setPromptText(
            "Search ID, subject, or user..."
        );
        searchField.setPrefWidth(280);

        statusFilter = new ComboBox<>();
        statusFilter.getItems().addAll(
            "All Status", "Pending", "In Progress", "Resolved"
        );
        statusFilter.setValue("All Status");

        Button refreshButton = new Button("Refresh");
        refreshButton.setStyle(buttonStyle());
        refreshButton.setOnAction(e -> {
            applyFilters();
            table.refresh();
            updateStats();
        });

        HBox filters = new HBox(
            12, searchField, statusFilter, refreshButton
        );
        filters.setAlignment(Pos.CENTER_LEFT);

        table = new TableView<>();
        table.setColumnResizePolicy(
            TableView.CONSTRAINED_RESIZE_POLICY
        );
        table.setPlaceholder(new Label("No complaints found."));

        TableColumn<Complaint, String> idColumn =
            new TableColumn<>("Complaint ID");
        idColumn.setCellValueFactory(data ->
            data.getValue().idProperty()
        );

        TableColumn<Complaint, String> userColumn =
            new TableColumn<>("Reported By");
        userColumn.setCellValueFactory(data ->
            data.getValue().userProperty()
        );

        TableColumn<Complaint, String> subjectColumn =
            new TableColumn<>("Subject");
        subjectColumn.setCellValueFactory(data ->
            data.getValue().subjectProperty()
        );

        TableColumn<Complaint, String> categoryColumn =
            new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(data ->
            data.getValue().categoryProperty()
        );

        TableColumn<Complaint, String> dateColumn =
            new TableColumn<>("Date");
        dateColumn.setCellValueFactory(data ->
            data.getValue().dateProperty()
        );

        TableColumn<Complaint, String> statusColumn =
            new TableColumn<>("Status");
        statusColumn.setCellValueFactory(data ->
            data.getValue().statusProperty()
        );

        TableColumn<Complaint, Void> actionsColumn =
            new TableColumn<>("Actions");

        actionsColumn.setCellFactory(column ->
            new TableCell<Complaint, Void>() {

                private final Button viewButton = new Button("View");
                private final Button updateButton = new Button("Update");
                private final Button deleteButton = new Button("Delete");

                {
                    viewButton.setStyle(buttonStyle());
                    updateButton.setStyle(buttonStyle());

                    deleteButton.setStyle(
                        "-fx-background-color: #FEE2E2;" +
                        "-fx-text-fill: #B91C1C;" +
                        "-fx-background-radius: 5;"
                    );

                    viewButton.setOnAction(e -> {
                        Complaint complaint = getCurrentComplaint();
                        if (complaint != null) {
                            showDetails(complaint);
                        }
                    });

                    updateButton.setOnAction(e -> {
                        Complaint complaint = getCurrentComplaint();
                        if (complaint != null) {
                            updateStatus(complaint);
                        }
                    });

                    deleteButton.setOnAction(e -> {
                        Complaint complaint = getCurrentComplaint();
                        if (complaint != null) {
                            deleteComplaint(complaint);
                        }
                    });
                }

                private Complaint getCurrentComplaint() {
                    int index = getIndex();

                    if (index < 0 ||
                        index >= getTableView().getItems().size()) {
                        return null;
                    }

                    return getTableView().getItems().get(index);
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || getCurrentComplaint() == null) {
                        setGraphic(null);
                    } else {
                        HBox buttons = new HBox(
                            5, viewButton, updateButton, deleteButton
                        );
                        buttons.setAlignment(Pos.CENTER);
                        setGraphic(buttons);
                    }
                }
            }
        );

        table.getColumns().addAll(
            idColumn, userColumn, subjectColumn,
            categoryColumn, dateColumn, statusColumn, actionsColumn
        );

        filteredComplaints = new FilteredList<>(
            complaints, complaint -> true
        );

        table.setItems(filteredComplaints);

        searchField.textProperty().addListener(
            (obs, oldValue, newValue) -> applyFilters()
        );

        statusFilter.valueProperty().addListener(
            (obs, oldValue, newValue) -> applyFilters()
        );

        VBox tableCard = new VBox(12, filters, table);
        tableCard.setPadding(new Insets(18));
        tableCard.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: #E2E8F0;" +
            "-fx-border-radius: 12;"
        );

        VBox.setVgrow(table, Priority.ALWAYS);
        VBox.setVgrow(tableCard, Priority.ALWAYS);

        getChildren().addAll(heading, stats, tableCard);

        updateStats();
    }

    private VBox createStatCard(String heading, Label valueLabel) {

        Label cardTitle = new Label(heading);
        cardTitle.setStyle(
            "-fx-text-fill: #64748B; -fx-font-size: 13px;"
        );

        valueLabel.setStyle(
            "-fx-font-size: 25px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + green + ";"
        );

        VBox card = new VBox(10, cardTitle, valueLabel);
        card.setPadding(new Insets(18));
        card.setMinWidth(130);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: #E2E8F0;" +
            "-fx-border-radius: 12;"
        );

        HBox.setHgrow(card, Priority.ALWAYS);
        return card;
    }

    private String buttonStyle() {
        return "-fx-background-color: " + green + ";" +
               "-fx-text-fill: white;" +
               "-fx-background-radius: 5;" +
               "-fx-padding: 8 10;";
    }

    private void addSampleComplaints() {

        complaints.addAll(
            new Complaint(
                "CMP101", "Rahul Patil", "Delayed Rescue",
                "Animal Rescue", "23-Oct-2026", "Pending",
                "A reported injured dog has not yet been rescued."
            ),
            new Complaint(
                "CMP102", "Priya Jadhav", "Appointment Issue",
                "Veterinary Service", "22-Oct-2026", "In Progress",
                "The appointment confirmation has not been received."
            ),
            new Complaint(
                "CMP103", "Amit Shinde", "Incorrect Listing",
                "Marketplace", "21-Oct-2026", "Resolved",
                "An incorrect animal listing was reported and reviewed."
            ),
            new Complaint(
                "CMP104", "Sneha Pawar", "Volunteer Response",
                "Animal Rescue", "20-Oct-2026", "Pending",
                "A request for rescue assistance needs review."
            ),
            new Complaint(
                "CMP105", "Neha More", "Profile Issue",
                "User Account", "19-Oct-2026", "In Progress",
                "The user reported incorrect account information."
            )
        );
    }

    private void applyFilters() {

        String search = searchField.getText() == null
            ? "" : searchField.getText().toLowerCase().trim();

        String selectedStatus = statusFilter.getValue();

        filteredComplaints.setPredicate(complaint -> {

            boolean matchesSearch =
                complaint.getId().toLowerCase().contains(search)
                || complaint.getUser().toLowerCase().contains(search)
                || complaint.getSubject().toLowerCase().contains(search)
                || complaint.getCategory().toLowerCase().contains(search);

            boolean matchesStatus =
                selectedStatus == null
                || selectedStatus.equals("All Status")
                || complaint.getStatus().equals(selectedStatus);

            return matchesSearch && matchesStatus;
        });
    }

    private void updateStats() {

        totalLabel.setText(String.valueOf(complaints.size()));

        long pending = complaints.stream()
            .filter(c -> c.getStatus().equals("Pending"))
            .count();

        long inProgress = complaints.stream()
            .filter(c -> c.getStatus().equals("In Progress"))
            .count();

        long resolved = complaints.stream()
            .filter(c -> c.getStatus().equals("Resolved"))
            .count();

        pendingLabel.setText(String.valueOf(pending));
        progressLabel.setText(String.valueOf(inProgress));
        resolvedLabel.setText(String.valueOf(resolved));
    }

    private void showDetails(Complaint complaint) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Complaint Details");
        alert.setHeaderText("Complaint " + complaint.getId());

        alert.setContentText(
            "Reported By: " + complaint.getUser()
            + "\nSubject: " + complaint.getSubject()
            + "\nCategory: " + complaint.getCategory()
            + "\nDate: " + complaint.getDate()
            + "\nStatus: " + complaint.getStatus()
            + "\n\nDescription:\n" + complaint.getDescription()
        );

        alert.showAndWait();
    }

    private void updateStatus(Complaint complaint) {

        ChoiceDialog<String> dialog = new ChoiceDialog<>(
            complaint.getStatus(),
            "Pending", "In Progress", "Resolved"
        );

        dialog.setTitle("Update Complaint Status");
        dialog.setHeaderText(
            "Update status for " + complaint.getId()
        );
        dialog.setContentText("Select status:");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(status -> {
            complaint.setStatus(status);
            table.refresh();
            applyFilters();
            updateStats();
        });
    }

    private void deleteComplaint(Complaint complaint) {

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Complaint");
        confirm.setHeaderText(
            "Delete complaint " + complaint.getId() + "?"
        );
        confirm.setContentText(
            "Are you sure you want to delete this complaint?"
        );

        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            complaints.remove(complaint);
            updateStats();
        }
    }

    public static class Complaint {

        private final StringProperty id;
        private final StringProperty user;
        private final StringProperty subject;
        private final StringProperty category;
        private final StringProperty date;
        private final StringProperty status;
        private final String description;

        public Complaint(
            String id, String user, String subject,
            String category, String date, String status,
            String description) {

            this.id = new SimpleStringProperty(id);
            this.user = new SimpleStringProperty(user);
            this.subject = new SimpleStringProperty(subject);
            this.category = new SimpleStringProperty(category);
            this.date = new SimpleStringProperty(date);
            this.status = new SimpleStringProperty(status);
            this.description = description;
        }

        public String getId() { return id.get(); }
        public String getUser() { return user.get(); }
        public String getSubject() { return subject.get(); }
        public String getCategory() { return category.get(); }
        public String getDate() { return date.get(); }
        public String getStatus() { return status.get(); }
        public String getDescription() { return description; }

        public void setStatus(String value) {
            status.set(value);
        }

        public StringProperty idProperty() { return id; }
        public StringProperty userProperty() { return user; }
        public StringProperty subjectProperty() { return subject; }
        public StringProperty categoryProperty() { return category; }
        public StringProperty dateProperty() { return date; }
        public StringProperty statusProperty() { return status; }
    }
}
