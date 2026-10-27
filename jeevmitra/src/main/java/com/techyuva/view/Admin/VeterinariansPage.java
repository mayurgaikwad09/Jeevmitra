
package com.techyuva.view.Admin;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.Locale;

public class VeterinariansPage extends VBox {

    private final String green = "#075747";
    private final String background = "#F4FAF8";

    private final ObservableList<Veterinarian> doctors =
            FXCollections.observableArrayList();

    private final FilteredList<Veterinarian> filteredDoctors =
            new FilteredList<>(doctors, d -> true);

    private final TableView<Veterinarian> table = new TableView<>();

    private final Label totalValue = new Label("0");
    private final Label availableValue = new Label("0");
    private final Label unavailableValue = new Label("0");

    private TextField searchField;
    private ComboBox<String> statusFilter;

    public VeterinariansPage(Runnable backAction) {

        setSpacing(20);
        setPadding(new Insets(30));
        setStyle("-fx-background-color: " + background + ";");

        doctors.addAll(
            new Veterinarian("VT001", "Dr. Anjali Deshmukh",
                "anjali.vet@gmail.com", "9876501001",
                "Small Animals", "Pune", "Available"),

            new Veterinarian("VT002", "Dr. Rahul Kulkarni",
                "rahul.vet@gmail.com", "9876501002",
                "Surgery", "Kothrud", "Available"),

            new Veterinarian("VT003", "Dr. Neha Shah",
                "neha.vet@gmail.com", "9876501003",
                "Birds", "Baner", "Unavailable"),

            new Veterinarian("VT004", "Dr. Amit Patil",
                "amit.vet@gmail.com", "9876501004",
                "Emergency Care", "Wakad", "Available"),

            new Veterinarian("VT005", "Dr. Sneha Joshi",
                "sneha.vet@gmail.com", "9876501005",
                "Dermatology", "Pimpri", "Available")
        );

        Button backButton =
                new Button("← Back to Admin Dashboard");

        styleButton(backButton, green);
        backButton.setOnAction(e -> backAction.run());

        Label heading =
                makeLabel("Veterinarians", 30, green, true);

        Label subtitle = makeLabel(
            "Manage veterinary doctors in JeevMitra.",
            14, "#607D79", false
        );

        HBox stats = new HBox(15,
            createCard("Total Veterinarians", totalValue),
            createCard("Available Doctors", availableValue),
            createCard("Unavailable Doctors", unavailableValue)
        );

        for (javafx.scene.Node node : stats.getChildren()) {
            HBox.setHgrow(node, Priority.ALWAYS);
        }

        searchField = new TextField();
        searchField.setPromptText(
            "Search by name, ID, email, specialty or location..."
        );
        searchField.setPrefHeight(40);

        statusFilter = new ComboBox<>();
        statusFilter.getItems().addAll(
            "All Doctors", "Available", "Unavailable"
        );
        statusFilter.setValue("All Doctors");
        statusFilter.setPrefHeight(40);

        Button addButton = new Button("+ Add Veterinarian");
        styleButton(addButton, green);
        addButton.setOnAction(e -> addDoctor());

        Button exportButton = new Button("Export CSV");
        styleButton(exportButton, "#2876C7");
        exportButton.setOnAction(e -> exportDoctors());

        HBox tools = new HBox(
            10, searchField, statusFilter, addButton, exportButton
        );
        tools.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(searchField, Priority.ALWAYS);

        configureTable();
        table.setItems(filteredDoctors);
        table.setColumnResizePolicy(
            TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN
        );
        table.setPrefHeight(420);
        table.setMinHeight(300);
        table.setPlaceholder(new Label("No doctors found."));

        Label tableTitle =
                makeLabel("Registered Veterinarians", 20, green, true);

        Label tableSubtitle = makeLabel(
            "View details, edit profiles and manage availability.",
            13, "#607D79", false
        );

        VBox tableCard = new VBox(
            12, tableTitle, tableSubtitle, table
        );

        tableCard.setPadding(new Insets(20));
        tableCard.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 14;" +
            "-fx-border-color: #E1ECE7;" +
            "-fx-border-radius: 14;"
        );

        searchField.textProperty().addListener(
            (obs, oldValue, newValue) -> applyFilters()
        );

        statusFilter.setOnAction(e -> applyFilters());

        updateStatistics();

        getChildren().addAll(
            backButton, heading, subtitle,
            stats, tools, tableCard
        );

        VBox.setVgrow(tableCard, Priority.ALWAYS);
    }

    private void configureTable() {

        TableColumn<Veterinarian, String> idCol =
                new TableColumn<>("Doctor ID");
        idCol.setCellValueFactory(
            d -> d.getValue().idProperty()
        );

        TableColumn<Veterinarian, String> nameCol =
                new TableColumn<>("Doctor Name");
        nameCol.setCellValueFactory(
            d -> d.getValue().nameProperty()
        );

        TableColumn<Veterinarian, String> emailCol =
                new TableColumn<>("Email");
        emailCol.setCellValueFactory(
            d -> d.getValue().emailProperty()
        );

        TableColumn<Veterinarian, String> phoneCol =
                new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(
            d -> d.getValue().phoneProperty()
        );

        TableColumn<Veterinarian, String> specialtyCol =
                new TableColumn<>("Specialty");
        specialtyCol.setCellValueFactory(
            d -> d.getValue().specialtyProperty()
        );

        TableColumn<Veterinarian, String> locationCol =
                new TableColumn<>("Location");
        locationCol.setCellValueFactory(
            d -> d.getValue().locationProperty()
        );

        TableColumn<Veterinarian, String> statusCol =
                new TableColumn<>("Availability");
        statusCol.setCellValueFactory(
            d -> d.getValue().statusProperty()
        );

        statusCol.setCellFactory(column ->
            new TableCell<Veterinarian, String>() {
                @Override
                protected void updateItem(String status, boolean empty) {
                    super.updateItem(status, empty);

                    if (empty || status == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(status);
                        setStyle(status.equals("Available")
                            ? "-fx-text-fill: #16804A; -fx-font-weight: bold;"
                            : "-fx-text-fill: #C25B48; -fx-font-weight: bold;");
                    }
                }
            }
        );

        TableColumn<Veterinarian, Void> actionsCol =
                new TableColumn<>("Actions");

        actionsCol.setCellFactory(column ->
            new TableCell<Veterinarian, Void>() {

                private final Button viewButton = new Button("View");
                private final Button editButton = new Button("Edit");
                private final Button statusButton = new Button();
                private final Button deleteButton = new Button("Delete");

                private final HBox buttons = new HBox(
                    5, viewButton, editButton,
                    statusButton, deleteButton
                );

                {
                    styleButton(viewButton, "#2876C7");
                    styleButton(editButton, green);
                    styleButton(statusButton, "#B7791F");
                    styleButton(deleteButton, "#B93838");

                    viewButton.setOnAction(e -> {
                        Veterinarian d = getDoctor();
                        if (d != null) showDetails(d);
                    });

                    editButton.setOnAction(e -> {
                        Veterinarian d = getDoctor();
                        if (d != null) editDoctor(d);
                    });

                    statusButton.setOnAction(e -> {
                        Veterinarian d = getDoctor();
                        if (d != null) changeAvailability(d);
                    });

                    deleteButton.setOnAction(e -> {
                        Veterinarian d = getDoctor();
                        if (d != null) deleteDoctor(d);
                    });
                }

                private Veterinarian getDoctor() {
                    if (getIndex() < 0 ||
                        getIndex() >= getTableView().getItems().size()) {
                        return null;
                    }

                    return getTableView().getItems().get(getIndex());
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);

                    Veterinarian d = empty ? null : getDoctor();

                    if (d == null) {
                        setGraphic(null);
                    } else {
                        statusButton.setText(
                            d.getStatus().equals("Available")
                                ? "Set Busy" : "Set Available"
                        );

                        setGraphic(buttons);
                    }
                }
            }
        );

        idCol.setMinWidth(75);
        nameCol.setMinWidth(135);
        emailCol.setMinWidth(150);
        phoneCol.setMinWidth(105);
        specialtyCol.setMinWidth(120);
        locationCol.setMinWidth(90);
        statusCol.setMinWidth(105);
        actionsCol.setMinWidth(280);

        table.getColumns().addAll(
            idCol, nameCol, emailCol, phoneCol,
            specialtyCol, locationCol, statusCol, actionsCol
        );
    }

    private void addDoctor() {

        Dialog<Veterinarian> dialog = new Dialog<>();
        dialog.setTitle("Add Veterinarian");
        dialog.setHeaderText("Enter doctor details");

        ButtonType saveButton = new ButtonType(
            "Add Doctor", ButtonBar.ButtonData.OK_DONE
        );

        dialog.getDialogPane().getButtonTypes().addAll(
            saveButton, ButtonType.CANCEL
        );

        TextField name = new TextField();
        name.setPromptText("Doctor name");

        TextField email = new TextField();
        email.setPromptText("Email address");

        TextField phone = new TextField();
        phone.setPromptText("10-digit phone number");

        TextField specialty = new TextField();
        specialty.setPromptText("Specialty");

        TextField location = new TextField();
        location.setPromptText("Clinic or location");

        VBox form = new VBox(10,
            new Label("Name"), name,
            new Label("Email"), email,
            new Label("Phone"), phone,
            new Label("Specialty"), specialty,
            new Label("Location"), location
        );

        form.setPadding(new Insets(15));
        dialog.getDialogPane().setContent(form);

        dialog.setResultConverter(button -> {
            if (button == saveButton) {

                String n = name.getText().trim();
                String em = email.getText().trim();
                String ph = phone.getText().trim();
                String sp = specialty.getText().trim();
                String loc = location.getText().trim();

                if (n.isEmpty() || em.isEmpty() || ph.isEmpty()
                    || sp.isEmpty() || loc.isEmpty()) {
                    showMessage("Please fill in all fields.");
                    return null;
                }

                if (!em.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
                    showMessage("Enter a valid email address.");
                    return null;
                }

                if (!ph.matches("\\d{10}")) {
                    showMessage("Phone must contain 10 digits.");
                    return null;
                }

                boolean duplicate = doctors.stream().anyMatch(d ->
                    d.getEmail().equalsIgnoreCase(em)
                    || d.getPhone().equals(ph)
                );

                if (duplicate) {
                    showMessage("Email or phone already exists.");
                    return null;
                }

                return new Veterinarian(
                    nextDoctorId(), n, em, ph, sp, loc, "Available"
                );
            }

            return null;
        });

        dialog.showAndWait().ifPresent(doctor -> {
            doctors.add(doctor);
            applyFilters();
            updateStatistics();
            table.refresh();
        });
    }

    private void editDoctor(Veterinarian doctor) {

        Dialog<Veterinarian> dialog = new Dialog<>();
        dialog.setTitle("Edit Veterinarian");
        dialog.setHeaderText("Update " + doctor.getName());

        ButtonType saveButton = new ButtonType(
            "Save Changes", ButtonBar.ButtonData.OK_DONE
        );

        dialog.getDialogPane().getButtonTypes().addAll(
            saveButton, ButtonType.CANCEL
        );

        TextField name = new TextField(doctor.getName());
        TextField email = new TextField(doctor.getEmail());
        TextField phone = new TextField(doctor.getPhone());
        TextField specialty = new TextField(doctor.getSpecialty());
        TextField location = new TextField(doctor.getLocation());

        VBox form = new VBox(10,
            new Label("Name"), name,
            new Label("Email"), email,
            new Label("Phone"), phone,
            new Label("Specialty"), specialty,
            new Label("Location"), location
        );

        form.setPadding(new Insets(15));
        dialog.getDialogPane().setContent(form);

        dialog.setResultConverter(button -> {
            if (button == saveButton) {

                String n = name.getText().trim();
                String em = email.getText().trim();
                String ph = phone.getText().trim();
                String sp = specialty.getText().trim();
                String loc = location.getText().trim();

                if (n.isEmpty() || em.isEmpty() || ph.isEmpty()
                    || sp.isEmpty() || loc.isEmpty()) {
                    showMessage("Please fill in all fields.");
                    return null;
                }

                if (!em.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
                    showMessage("Enter a valid email address.");
                    return null;
                }

                if (!ph.matches("\\d{10}")) {
                    showMessage("Phone must contain 10 digits.");
                    return null;
                }

                boolean duplicate = doctors.stream().anyMatch(d ->
                    d != doctor &&
                    (d.getEmail().equalsIgnoreCase(em)
                    || d.getPhone().equals(ph))
                );

                if (duplicate) {
                    showMessage("Another doctor has this email or phone.");
                    return null;
                }

                doctor.setName(n);
                doctor.setEmail(em);
                doctor.setPhone(ph);
                doctor.setSpecialty(sp);
                doctor.setLocation(loc);

                return doctor;
            }

            return null;
        });

        dialog.showAndWait().ifPresent(updated -> {
            applyFilters();
            updateStatistics();
            table.refresh();
        });
    }

    private void deleteDoctor(Veterinarian doctor) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Veterinarian");
        alert.setHeaderText("Delete " + doctor.getName() + "?");
        alert.setContentText(
            "This will remove the doctor from the current list."
        );

        alert.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                doctors.remove(doctor);
                applyFilters();
                updateStatistics();
            }
        });
    }

    private void changeAvailability(Veterinarian doctor) {

        boolean setUnavailable =
            doctor.getStatus().equals("Available");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Change Availability");
        alert.setHeaderText(
            "Update availability for " + doctor.getName() + "?"
        );

        alert.setContentText(
            setUnavailable
                ? "Set this doctor as unavailable?"
                : "Set this doctor as available?"
        );

        alert.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                doctor.setStatus(
                    setUnavailable ? "Unavailable" : "Available"
                );

                applyFilters();
                updateStatistics();
                table.refresh();
            }
        });
    }

    private void showDetails(Veterinarian doctor) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Veterinarian Details");
        alert.setHeaderText(doctor.getName());

        alert.setContentText(
            "Doctor ID: " + doctor.getId() +
            "\nName: " + doctor.getName() +
            "\nEmail: " + doctor.getEmail() +
            "\nPhone: " + doctor.getPhone() +
            "\nSpecialty: " + doctor.getSpecialty() +
            "\nLocation: " + doctor.getLocation() +
            "\nAvailability: " + doctor.getStatus()
        );

        alert.showAndWait();
    }

    private void applyFilters() {

        String query = searchField.getText() == null
            ? "" : searchField.getText().trim().toLowerCase(Locale.ROOT);

        String status = statusFilter.getValue();

        filteredDoctors.setPredicate(d -> {

            boolean matchesSearch =
                d.getId().toLowerCase(Locale.ROOT).contains(query) ||
                d.getName().toLowerCase(Locale.ROOT).contains(query) ||
                d.getEmail().toLowerCase(Locale.ROOT).contains(query) ||
                d.getPhone().contains(query) ||
                d.getSpecialty().toLowerCase(Locale.ROOT).contains(query) ||
                d.getLocation().toLowerCase(Locale.ROOT).contains(query);

            boolean matchesStatus =
                status == null || status.equals("All Doctors") ||
                d.getStatus().equals(status);

            return matchesSearch && matchesStatus;
        });
    }

    private void updateStatistics() {

        totalValue.setText(String.valueOf(doctors.size()));

        availableValue.setText(String.valueOf(
            doctors.stream()
                .filter(d -> d.getStatus().equals("Available"))
                .count()
        ));

        unavailableValue.setText(String.valueOf(
            doctors.stream()
                .filter(d -> d.getStatus().equals("Unavailable"))
                .count()
        ));
    }

    private String nextDoctorId() {

        int max = 0;

        for (Veterinarian doctor : doctors) {
            try {
                max = Math.max(max,
                    Integer.parseInt(doctor.getId().substring(2)));
            } catch (Exception ignored) {
                // Ignore IDs not following VT001 format.
            }
        }

        return String.format("VT%03d", max + 1);
    }

    private void exportDoctors() {

        javafx.stage.FileChooser chooser =
                new javafx.stage.FileChooser();

        chooser.setTitle("Export Veterinarians");
        chooser.setInitialFileName("JeevMitra_Veterinarians.csv");

        chooser.getExtensionFilters().add(
            new javafx.stage.FileChooser.ExtensionFilter(
                "CSV Files", "*.csv"
            )
        );

        java.io.File file = chooser.showSaveDialog(
            getScene() == null ? null : getScene().getWindow()
        );

        if (file == null) return;

        try (java.io.PrintWriter writer =
                 new java.io.PrintWriter(file, "UTF-8")) {

            writer.println(
                "Doctor ID,Name,Email,Phone,Specialty,Location,Availability"
            );

            for (Veterinarian d : doctors) {
                writer.println(
                    csv(d.getId()) + "," +
                    csv(d.getName()) + "," +
                    csv(d.getEmail()) + "," +
                    csv(d.getPhone()) + "," +
                    csv(d.getSpecialty()) + "," +
                    csv(d.getLocation()) + "," +
                    csv(d.getStatus())
                );
            }

            showMessage("Veterinarians exported successfully.");

        } catch (Exception ex) {
            showMessage("Could not export file: " + ex.getMessage());
        }
    }

    private String csv(String value) {
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    private VBox createCard(String title, Label value) {

        Label titleLabel =
                makeLabel(title, 13, "#607D79", false);

        value.setFont(Font.font("Arial", FontWeight.BOLD, 27));
        value.setTextFill(Color.web(green));

        VBox card = new VBox(12, titleLabel, value);
        card.setPadding(new Insets(20));
        card.setMinHeight(105);
        card.setMaxWidth(Double.MAX_VALUE);

        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: #E1ECE7;" +
            "-fx-border-radius: 12;"
        );

        return card;
    }

    private void styleButton(Button button, String color) {

        button.setTextFill(Color.WHITE);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        button.setPadding(new Insets(10, 13, 10, 13));

        button.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 7;" +
            "-fx-cursor: hand;"
        );
    }

    private void showMessage(String message) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private Label makeLabel(
        String value, int size, String color, boolean bold
    ) {

        Label label = new Label(value);

        label.setFont(bold
            ? Font.font("Arial", FontWeight.BOLD, size)
            : Font.font("Arial", size));

        label.setTextFill(Color.web(color));
        label.setWrapText(true);

        return label;
    }

    public static class Veterinarian {

        private final SimpleStringProperty id;
        private final SimpleStringProperty name;
        private final SimpleStringProperty email;
        private final SimpleStringProperty phone;
        private final SimpleStringProperty specialty;
        private final SimpleStringProperty location;
        private final SimpleStringProperty status;

        public Veterinarian(
            String id, String name, String email, String phone,
            String specialty, String location, String status
        ) {
            this.id = new SimpleStringProperty(id);
            this.name = new SimpleStringProperty(name);
            this.email = new SimpleStringProperty(email);
            this.phone = new SimpleStringProperty(phone);
            this.specialty = new SimpleStringProperty(specialty);
            this.location = new SimpleStringProperty(location);
            this.status = new SimpleStringProperty(status);
        }

        public javafx.beans.property.StringProperty idProperty() {
            return id;
        }

        public javafx.beans.property.StringProperty nameProperty() {
            return name;
        }

        public javafx.beans.property.StringProperty emailProperty() {
            return email;
        }

        public javafx.beans.property.StringProperty phoneProperty() {
            return phone;
        }

        public javafx.beans.property.StringProperty specialtyProperty() {
            return specialty;
        }

        public javafx.beans.property.StringProperty locationProperty() {
            return location;
        }

        public javafx.beans.property.StringProperty statusProperty() {
            return status;
        }

        public String getId() { return id.get(); }
        public String getName() { return name.get(); }
        public String getEmail() { return email.get(); }
        public String getPhone() { return phone.get(); }
        public String getSpecialty() { return specialty.get(); }
        public String getLocation() { return location.get(); }
        public String getStatus() { return status.get(); }

        public void setName(String value) { name.set(value); }
        public void setEmail(String value) { email.set(value); }
        public void setPhone(String value) { phone.set(value); }
        public void setSpecialty(String value) { specialty.set(value); }
        public void setLocation(String value) { location.set(value); }
        public void setStatus(String value) { status.set(value); }
    }
}
