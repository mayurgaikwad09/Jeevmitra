
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

public class MedicalReportsPage extends VBox {

    private final String green = "#075747";
    private final String darkGreen = "#034638";
    private final String background = "#F4FAF8";

    private final ObservableList<MedicalRecord> records =
            FXCollections.observableArrayList();

    private FilteredList<MedicalRecord> filteredRecords;
    private TableView<MedicalRecord> table;

    private Label totalLabel;
    private Label consultationLabel;
    private Label vaccinationLabel;
    private Label prescriptionLabel;

    private TextField searchField;
    private ComboBox<String> typeFilter;

    public MedicalReportsPage(Runnable backAction) {

        setSpacing(18);
        setPadding(new Insets(25));
        setStyle("-fx-background-color: " + background + ";");

        addSampleRecords();

        Label title = new Label("Medical Reports");
        title.setStyle(
            "-fx-font-size: 28px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + darkGreen + ";"
        );

        Label subtitle = new Label(
            "Manage animal health records, diagnosis, treatment and vaccinations."
        );
        subtitle.setStyle(
            "-fx-font-size: 13px; -fx-text-fill: #64748B;"
        );

        Button backButton = new Button("← Back to Admin Dashboard");
        backButton.setStyle(buttonStyle());
        backButton.setOnAction(e -> backAction.run());

        VBox heading = new VBox(8, title, subtitle, backButton);

        totalLabel = new Label("0");
        consultationLabel = new Label("0");
        vaccinationLabel = new Label("0");
        prescriptionLabel = new Label("0");

        HBox stats = new HBox(
            15,
            createStatCard("Total Records", totalLabel),
            createStatCard("Consultations", consultationLabel),
            createStatCard("Vaccinations", vaccinationLabel),
            createStatCard("Prescriptions", prescriptionLabel)
        );

        searchField = new TextField();
        searchField.setPromptText(
            "Search record ID, animal, or veterinarian..."
        );
        searchField.setPrefWidth(300);

        typeFilter = new ComboBox<>();
        typeFilter.getItems().addAll(
            "All Types",
            "Consultation",
            "Vaccination",
            "Prescription"
        );
        typeFilter.setValue("All Types");

        Button addButton = new Button("+ Add Medical Record");
        addButton.setStyle(buttonStyle());
        addButton.setOnAction(e -> addRecord());

        Button refreshButton = new Button("Refresh");
        refreshButton.setStyle(buttonStyle());
        refreshButton.setOnAction(e -> {
            applyFilters();
            table.refresh();
            updateStats();
        });

        HBox filters = new HBox(
            10, searchField, typeFilter, addButton, refreshButton
        );
        filters.setAlignment(Pos.CENTER_LEFT);

        table = new TableView<>();
        table.setColumnResizePolicy(
            TableView.CONSTRAINED_RESIZE_POLICY
        );
        table.setPlaceholder(
            new Label("No medical records found.")
        );

        TableColumn<MedicalRecord, String> idColumn =
            new TableColumn<>("Record ID");
        idColumn.setCellValueFactory(data ->
            data.getValue().idProperty()
        );

        TableColumn<MedicalRecord, String> animalColumn =
            new TableColumn<>("Animal");
        animalColumn.setCellValueFactory(data ->
            data.getValue().animalProperty()
        );

        TableColumn<MedicalRecord, String> ownerColumn =
            new TableColumn<>("Owner");
        ownerColumn.setCellValueFactory(data ->
            data.getValue().ownerProperty()
        );

        TableColumn<MedicalRecord, String> doctorColumn =
            new TableColumn<>("Veterinarian");
        doctorColumn.setCellValueFactory(data ->
            data.getValue().doctorProperty()
        );

        TableColumn<MedicalRecord, String> typeColumn =
            new TableColumn<>("Record Type");
        typeColumn.setCellValueFactory(data ->
            data.getValue().typeProperty()
        );

        TableColumn<MedicalRecord, String> dateColumn =
            new TableColumn<>("Date");
        dateColumn.setCellValueFactory(data ->
            data.getValue().dateProperty()
        );

        TableColumn<MedicalRecord, Void> actionsColumn =
            new TableColumn<>("Actions");

        actionsColumn.setCellFactory(column ->
            new TableCell<MedicalRecord, Void>() {

                private final Button viewButton = new Button("View");
                private final Button editButton = new Button("Edit");
                private final Button deleteButton = new Button("Delete");

                {
                    viewButton.setStyle(buttonStyle());
                    editButton.setStyle(buttonStyle());

                    deleteButton.setStyle(
                        "-fx-background-color: #FEE2E2;" +
                        "-fx-text-fill: #B91C1C;" +
                        "-fx-background-radius: 5;"
                    );

                    viewButton.setOnAction(e -> {
                        MedicalRecord record = getCurrentRecord();
                        if (record != null) showDetails(record);
                    });

                    editButton.setOnAction(e -> {
                        MedicalRecord record = getCurrentRecord();
                        if (record != null) editRecord(record);
                    });

                    deleteButton.setOnAction(e -> {
                        MedicalRecord record = getCurrentRecord();
                        if (record != null) deleteRecord(record);
                    });
                }

                private MedicalRecord getCurrentRecord() {
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

                    if (empty || getCurrentRecord() == null) {
                        setGraphic(null);
                    } else {
                        HBox buttons = new HBox(
                            5, viewButton, editButton, deleteButton
                        );
                        buttons.setAlignment(Pos.CENTER);
                        setGraphic(buttons);
                    }
                }
            }
        );

        table.getColumns().addAll(
            idColumn, animalColumn, ownerColumn,
            doctorColumn, typeColumn, dateColumn, actionsColumn
        );

        filteredRecords = new FilteredList<>(
            records, record -> true
        );
        table.setItems(filteredRecords);

        searchField.textProperty().addListener(
            (obs, oldValue, newValue) -> applyFilters()
        );

        typeFilter.valueProperty().addListener(
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

    private void addSampleRecords() {

        records.addAll(
            new MedicalRecord(
                "MR101", "Bruno", "Rahul Patil",
                "Dr. Amit Sharma", "Consultation",
                "23-Oct-2026", "Fever",
                "Rest and fluids; follow the veterinarian's advice."
            ),
            new MedicalRecord(
                "MR102", "Kitty", "Priya Jadhav",
                "Dr. Neha Kulkarni", "Vaccination",
                "22-Oct-2026", "Routine vaccination",
                "Vaccination recorded. Follow the vet's next-dose schedule."
            ),
            new MedicalRecord(
                "MR103", "Rocky", "Amit Shinde",
                "Dr. Amit Sharma", "Prescription",
                "21-Oct-2026", "Minor injury",
                "Treatment prescribed by the veterinarian."
            ),
            new MedicalRecord(
                "MR104", "Milo", "Sneha Pawar",
                "Dr. Neha Kulkarni", "Consultation",
                "20-Oct-2026", "Skin irritation",
                "Follow-up recommended by the veterinarian."
            )
        );
    }

    private void applyFilters() {

        String search = searchField.getText() == null
            ? "" : searchField.getText().toLowerCase().trim();

        String selectedType = typeFilter.getValue();

        filteredRecords.setPredicate(record -> {

            boolean matchesSearch =
                record.getId().toLowerCase().contains(search)
                || record.getAnimal().toLowerCase().contains(search)
                || record.getDoctor().toLowerCase().contains(search)
                || record.getOwner().toLowerCase().contains(search);

            boolean matchesType =
                selectedType == null
                || selectedType.equals("All Types")
                || record.getType().equals(selectedType);

            return matchesSearch && matchesType;
        });
    }

    private void updateStats() {

        totalLabel.setText(String.valueOf(records.size()));

        long consultations = records.stream()
            .filter(r -> r.getType().equals("Consultation"))
            .count();

        long vaccinations = records.stream()
            .filter(r -> r.getType().equals("Vaccination"))
            .count();

        long prescriptions = records.stream()
            .filter(r -> r.getType().equals("Prescription"))
            .count();

        consultationLabel.setText(String.valueOf(consultations));
        vaccinationLabel.setText(String.valueOf(vaccinations));
        prescriptionLabel.setText(String.valueOf(prescriptions));
    }

    private void showDetails(MedicalRecord record) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Medical Record Details");
        alert.setHeaderText("Record " + record.getId());

        alert.setContentText(
            "Animal: " + record.getAnimal()
            + "\nOwner: " + record.getOwner()
            + "\nVeterinarian: " + record.getDoctor()
            + "\nRecord Type: " + record.getType()
            + "\nDate: " + record.getDate()
            + "\nDiagnosis: " + record.getDiagnosis()
            + "\n\nTreatment / Notes:\n" + record.getTreatment()
        );

        alert.showAndWait();
    }

    private void addRecord() {

        Dialog<MedicalRecord> dialog = createRecordDialog(
            "Add Medical Record", null
        );

        Optional<MedicalRecord> result = dialog.showAndWait();

        result.ifPresent(record -> {
            records.add(record);
            applyFilters();
            updateStats();
        });
    }

    private void editRecord(MedicalRecord oldRecord) {

        Dialog<MedicalRecord> dialog = createRecordDialog(
            "Edit Medical Record", oldRecord
        );

        Optional<MedicalRecord> result = dialog.showAndWait();

        result.ifPresent(updated -> {
            oldRecord.setAnimal(updated.getAnimal());
            oldRecord.setOwner(updated.getOwner());
            oldRecord.setDoctor(updated.getDoctor());
            oldRecord.setType(updated.getType());
            oldRecord.setDate(updated.getDate());
            oldRecord.setDiagnosis(updated.getDiagnosis());
            oldRecord.setTreatment(updated.getTreatment());

            table.refresh();
            applyFilters();
            updateStats();
        });
    }

    private Dialog<MedicalRecord> createRecordDialog(
            String title, MedicalRecord existing) {

        Dialog<MedicalRecord> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(title);

        ButtonType saveButton = new ButtonType(
            "Save", ButtonBar.ButtonData.OK_DONE
        );

        dialog.getDialogPane().getButtonTypes().addAll(
            saveButton, ButtonType.CANCEL
        );

        TextField animalField = new TextField();
        animalField.setPromptText("Animal name");

        TextField ownerField = new TextField();
        ownerField.setPromptText("Owner name");

        TextField doctorField = new TextField();
        doctorField.setPromptText("Veterinarian name");

        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll(
            "Consultation", "Vaccination", "Prescription"
        );
        typeBox.setValue("Consultation");

        TextField dateField = new TextField();
        dateField.setPromptText("DD-MMM-YYYY");

        TextField diagnosisField = new TextField();
        diagnosisField.setPromptText("Diagnosis");

        TextArea treatmentArea = new TextArea();
        treatmentArea.setPromptText("Treatment / notes");
        treatmentArea.setPrefRowCount(3);
        treatmentArea.setWrapText(true);

        if (existing != null) {
            animalField.setText(existing.getAnimal());
            ownerField.setText(existing.getOwner());
            doctorField.setText(existing.getDoctor());
            typeBox.setValue(existing.getType());
            dateField.setText(existing.getDate());
            diagnosisField.setText(existing.getDiagnosis());
            treatmentArea.setText(existing.getTreatment());
        }

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));

        grid.addRow(0, new Label("Animal:"), animalField);
        grid.addRow(1, new Label("Owner:"), ownerField);
        grid.addRow(2, new Label("Veterinarian:"), doctorField);
        grid.addRow(3, new Label("Record Type:"), typeBox);
        grid.addRow(4, new Label("Date:"), dateField);
        grid.addRow(5, new Label("Diagnosis:"), diagnosisField);
        grid.addRow(6, new Label("Treatment:"), treatmentArea);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().setPrefWidth(500);

        dialog.setResultConverter(button -> {

            if (button != saveButton) {
                return null;
            }

            if (animalField.getText().trim().isEmpty()
                || ownerField.getText().trim().isEmpty()
                || doctorField.getText().trim().isEmpty()
                || dateField.getText().trim().isEmpty()
                || diagnosisField.getText().trim().isEmpty()
                || typeBox.getValue() == null) {

                Alert error = new Alert(Alert.AlertType.WARNING);
                error.setTitle("Missing Information");
                error.setHeaderText("Please complete the required fields.");
                error.setContentText(
                    "Animal, owner, veterinarian, type, date and diagnosis are required."
                );
                error.showAndWait();
                return null;
            }

            String id = existing == null
                ? "MR" + (101 + records.size())
                : existing.getId();

            return new MedicalRecord(
                id,
                animalField.getText().trim(),
                ownerField.getText().trim(),
                doctorField.getText().trim(),
                typeBox.getValue(),
                dateField.getText().trim(),
                diagnosisField.getText().trim(),
                treatmentArea.getText().trim()
            );
        });

        return dialog;
    }

    private void deleteRecord(MedicalRecord record) {

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Medical Record");
        confirm.setHeaderText("Delete " + record.getId() + "?");
        confirm.setContentText(
            "Are you sure you want to delete this record?"
        );

        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            records.remove(record);
            updateStats();
        }
    }

    public static class MedicalRecord {

        private final StringProperty id;
        private final StringProperty animal;
        private final StringProperty owner;
        private final StringProperty doctor;
        private final StringProperty type;
        private final StringProperty date;
        private final StringProperty diagnosis;
        private final StringProperty treatment;

        public MedicalRecord(
            String id, String animal, String owner,
            String doctor, String type, String date,
            String diagnosis, String treatment) {

            this.id = new SimpleStringProperty(id);
            this.animal = new SimpleStringProperty(animal);
            this.owner = new SimpleStringProperty(owner);
            this.doctor = new SimpleStringProperty(doctor);
            this.type = new SimpleStringProperty(type);
            this.date = new SimpleStringProperty(date);
            this.diagnosis = new SimpleStringProperty(diagnosis);
            this.treatment = new SimpleStringProperty(treatment);
        }

        public String getId() { return id.get(); }
        public String getAnimal() { return animal.get(); }
        public String getOwner() { return owner.get(); }
        public String getDoctor() { return doctor.get(); }
        public String getType() { return type.get(); }
        public String getDate() { return date.get(); }
        public String getDiagnosis() { return diagnosis.get(); }
        public String getTreatment() { return treatment.get(); }

        public void setAnimal(String v) { animal.set(v); }
        public void setOwner(String v) { owner.set(v); }
        public void setDoctor(String v) { doctor.set(v); }
        public void setType(String v) { type.set(v); }
        public void setDate(String v) { date.set(v); }
        public void setDiagnosis(String v) { diagnosis.set(v); }
        public void setTreatment(String v) { treatment.set(v); }

        public StringProperty idProperty() { return id; }
        public StringProperty animalProperty() { return animal; }
        public StringProperty ownerProperty() { return owner; }
        public StringProperty doctorProperty() { return doctor; }
        public StringProperty typeProperty() { return type; }
        public StringProperty dateProperty() { return date; }
    }
}
