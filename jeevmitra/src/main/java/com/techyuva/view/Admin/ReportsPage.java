
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

public class ReportsPage extends VBox {

    private final String green = "#075747";
    private final String darkGreen = "#034638";
    private final String background = "#F4FAF8";

    private final ObservableList<RescueReport> reports =
            FXCollections.observableArrayList();

    private FilteredList<RescueReport> filteredReports;
    private TableView<RescueReport> table;

    private Label totalLabel;
    private Label pendingLabel;
    private Label progressLabel;
    private Label resolvedLabel;

    private TextField searchField;
    private ComboBox<String> statusFilter;

    public ReportsPage(Runnable backAction) {

        setSpacing(18);
        setPadding(new Insets(25));
        setStyle("-fx-background-color: " + background + ";");

        addSampleReports();

        Label title = new Label("Rescue Reports");
        title.setStyle(
            "-fx-font-size: 28px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + darkGreen + ";"
        );

        Label subtitle = new Label(
            "Manage animal rescue requests and track their progress."
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
            createStatCard("Total Reports", totalLabel),
            createStatCard("Pending", pendingLabel),
            createStatCard("In Progress", progressLabel),
            createStatCard("Resolved", resolvedLabel)
        );

        searchField = new TextField();
        searchField.setPromptText("Search ID, animal, or location...");
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
        table.setPlaceholder(new Label("No rescue reports found."));

        TableColumn<RescueReport, String> idColumn =
                new TableColumn<>("Report ID");
        idColumn.setCellValueFactory(data ->
                data.getValue().idProperty());

        TableColumn<RescueReport, String> animalColumn =
                new TableColumn<>("Animal");
        animalColumn.setCellValueFactory(data ->
                data.getValue().animalProperty());

        TableColumn<RescueReport, String> typeColumn =
                new TableColumn<>("Animal Type");
        typeColumn.setCellValueFactory(data ->
                data.getValue().typeProperty());

        TableColumn<RescueReport, String> locationColumn =
                new TableColumn<>("Location");
        locationColumn.setCellValueFactory(data ->
                data.getValue().locationProperty());

        TableColumn<RescueReport, String> dateColumn =
                new TableColumn<>("Date");
        dateColumn.setCellValueFactory(data ->
                data.getValue().dateProperty());

        TableColumn<RescueReport, String> statusColumn =
                new TableColumn<>("Status");
        statusColumn.setCellValueFactory(data ->
                data.getValue().statusProperty());

        TableColumn<RescueReport, String> teamColumn =
                new TableColumn<>("Rescue Team");
        teamColumn.setCellValueFactory(data ->
                data.getValue().teamProperty());

        TableColumn<RescueReport, Void> actionsColumn =
                new TableColumn<>("Actions");

        actionsColumn.setCellFactory(column ->
            new TableCell<RescueReport, Void>() {

                private final Button viewButton = new Button("View");
                private final Button assignButton = new Button("Assign");
                private final Button updateButton = new Button("Update");
                private final Button deleteButton = new Button("Delete");

                {
                    viewButton.setStyle(buttonStyle());
                    assignButton.setStyle(buttonStyle());
                    updateButton.setStyle(buttonStyle());

                    deleteButton.setStyle(
                        "-fx-background-color: #FEE2E2;" +
                        "-fx-text-fill: #B91C1C;" +
                        "-fx-background-radius: 5;"
                    );

                    viewButton.setOnAction(e -> {
                        RescueReport report = getSelectedReport();
                        if (report != null) showDetails(report);
                    });

                    assignButton.setOnAction(e -> {
                        RescueReport report = getSelectedReport();
                        if (report != null) assignTeam(report);
                    });

                    updateButton.setOnAction(e -> {
                        RescueReport report = getSelectedReport();
                        if (report != null) updateStatus(report);
                    });

                    deleteButton.setOnAction(e -> {
                        RescueReport report = getSelectedReport();
                        if (report != null) deleteReport(report);
                    });
                }

                private RescueReport getSelectedReport() {
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

                    if (empty || getSelectedReport() == null) {
                        setGraphic(null);
                    } else {
                        HBox buttons = new HBox(
                            5, viewButton, assignButton,
                            updateButton, deleteButton
                        );
                        buttons.setAlignment(Pos.CENTER);
                        setGraphic(buttons);
                    }
                }
            }
        );

        table.getColumns().addAll(
            idColumn, animalColumn, typeColumn, locationColumn,
            dateColumn, statusColumn, teamColumn, actionsColumn
        );

        filteredReports = new FilteredList<>(
            reports, report -> true
        );
        table.setItems(filteredReports);

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
               "-fx-padding: 7 10;";
    }

    private void addSampleReports() {
        reports.addAll(
            new RescueReport(
                "RR101", "Bruno", "Dog",
                "Kurduwadi Main Road", "23-Oct-2026",
                "Pending", "Not Assigned",
                "An injured dog was reported near the main road."
            ),
            new RescueReport(
                "RR102", "Kitty", "Cat",
                "Madha Bus Stand", "22-Oct-2026",
                "In Progress", "Team Alpha",
                "A cat was found trapped and needs assistance."
            ),
            new RescueReport(
                "RR103", "Rocky", "Dog",
                "Solapur Road", "21-Oct-2026",
                "Resolved", "Team Beta",
                "The injured dog was rescued and taken for treatment."
            ),
            new RescueReport(
                "RR104", "Unknown", "Cow",
                "Near Village Market", "20-Oct-2026",
                "Pending", "Not Assigned",
                "An injured cow was reported by a local resident."
            ),
            new RescueReport(
                "RR105", "Milo", "Cat",
                "Kurduwadi Station Road", "19-Oct-2026",
                "In Progress", "Team Alpha",
                "A stray cat needs rescue and medical attention."
            )
        );
    }

    private void applyFilters() {
        String search = searchField.getText() == null
                ? ""
                : searchField.getText().toLowerCase().trim();

        String selectedStatus = statusFilter.getValue();

        filteredReports.setPredicate(report -> {
            boolean matchesSearch =
                report.getId().toLowerCase().contains(search)
                || report.getAnimal().toLowerCase().contains(search)
                || report.getLocation().toLowerCase().contains(search);

            boolean matchesStatus =
                selectedStatus == null
                || selectedStatus.equals("All Status")
                || report.getStatus().equals(selectedStatus);

            return matchesSearch && matchesStatus;
        });
    }

    private void updateStats() {
        totalLabel.setText(String.valueOf(reports.size()));

        long pending = reports.stream()
            .filter(r -> r.getStatus().equals("Pending"))
            .count();

        long inProgress = reports.stream()
            .filter(r -> r.getStatus().equals("In Progress"))
            .count();

        long resolved = reports.stream()
            .filter(r -> r.getStatus().equals("Resolved"))
            .count();

        pendingLabel.setText(String.valueOf(pending));
        progressLabel.setText(String.valueOf(inProgress));
        resolvedLabel.setText(String.valueOf(resolved));
    }

    private void showDetails(RescueReport report) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Rescue Report Details");
        alert.setHeaderText("Report " + report.getId());

        alert.setContentText(
            "Animal: " + report.getAnimal()
            + "\nAnimal Type: " + report.getType()
            + "\nLocation: " + report.getLocation()
            + "\nDate: " + report.getDate()
            + "\nStatus: " + report.getStatus()
            + "\nRescue Team: " + report.getTeam()
            + "\n\nDescription:\n" + report.getDescription()
        );

        alert.showAndWait();
    }

    private void assignTeam(RescueReport report) {
        ChoiceDialog<String> dialog = new ChoiceDialog<>(
            report.getTeam(),
            "Not Assigned", "Team Alpha", "Team Beta", "Team Gamma"
        );

        dialog.setTitle("Assign Rescue Team");
        dialog.setHeaderText("Assign team to " + report.getId());
        dialog.setContentText("Select rescue team:");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(team -> {
            report.setTeam(team);

            if (team.equals("Not Assigned")) {
                report.setStatus("Pending");
            } else {
                report.setStatus("In Progress");
            }

            table.refresh();
            applyFilters();
            updateStats();
        });
    }

    private void updateStatus(RescueReport report) {
        ChoiceDialog<String> dialog = new ChoiceDialog<>(
            report.getStatus(),
            "Pending", "In Progress", "Resolved"
        );

        dialog.setTitle("Update Rescue Status");
        dialog.setHeaderText("Update status for " + report.getId());
        dialog.setContentText("Select status:");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(status -> {
            report.setStatus(status);
            table.refresh();
            applyFilters();
            updateStats();
        });
    }

    private void deleteReport(RescueReport report) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Rescue Report");
        confirm.setHeaderText("Delete report " + report.getId() + "?");
        confirm.setContentText("This action cannot be undone.");

        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            reports.remove(report);
            updateStats();
        }
    }

    public static class RescueReport {
        private final StringProperty id;
        private final StringProperty animal;
        private final StringProperty type;
        private final StringProperty location;
        private final StringProperty date;
        private final StringProperty status;
        private final StringProperty team;
        private final String description;

        public RescueReport(
            String id, String animal, String type,
            String location, String date, String status,
            String team, String description) {

            this.id = new SimpleStringProperty(id);
            this.animal = new SimpleStringProperty(animal);
            this.type = new SimpleStringProperty(type);
            this.location = new SimpleStringProperty(location);
            this.date = new SimpleStringProperty(date);
            this.status = new SimpleStringProperty(status);
            this.team = new SimpleStringProperty(team);
            this.description = description;
        }

        public String getId() { return id.get(); }
        public String getAnimal() { return animal.get(); }
        public String getType() { return type.get(); }
        public String getLocation() { return location.get(); }
        public String getDate() { return date.get(); }
        public String getStatus() { return status.get(); }
        public String getTeam() { return team.get(); }
        public String getDescription() { return description; }

        public void setStatus(String value) { status.set(value); }
        public void setTeam(String value) { team.set(value); }

        public StringProperty idProperty() { return id; }
        public StringProperty animalProperty() { return animal; }
        public StringProperty typeProperty() { return type; }
        public StringProperty locationProperty() { return location; }
        public StringProperty dateProperty() { return date; }
        public StringProperty statusProperty() { return status; }
        public StringProperty teamProperty() { return team; }
    }
}
