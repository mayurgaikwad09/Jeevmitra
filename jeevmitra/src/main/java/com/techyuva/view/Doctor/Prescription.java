
package com.techyuva.view.Doctor;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Prescription {

    private final ObservableList<RxRecord> records =
            FXCollections.observableArrayList();

    private TextField pet, owner, diagnosis, medicine, dosage;
    private TextField frequency, duration, doctor, date;
    private TextArea instructions;
    private TableView<RxRecord> table;
    private Label status;

    public Scene createScene(Stage stage, Runnable backAction) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:#F2F7F3;");

        Label logo = new Label("🐾 JeevMitra");
        logo.setTextFill(Color.WHITE);
        logo.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 25));

        Region gap = new Region();
        HBox.setHgrow(gap, Priority.ALWAYS);

        Label tag = new Label("PRESCRIPTION STUDIO");
        tag.setTextFill(Color.WHITE);
        tag.setStyle("-fx-background-color:#286B50;"
                + "-fx-background-radius:20;-fx-padding:10 16;");

        HBox header = new HBox(15, logo, gap, tag);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(20, 32, 20, 32));
        header.setStyle("-fx-background-color:linear-gradient(to right,"
                + "#103F30,#21845C);");
        root.setTop(header);

        Button back = light("← Back to Dashboard");
        back.setOnAction(e -> backAction.run());

        Label title = new Label("Prescription Studio 💊");
        title.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 28));
        title.setTextFill(Color.web("#123D30"));

        Label subtitle = new Label(
                "Create and manage treatment instructions for your patients.");
        subtitle.setTextFill(Color.web("#687B70"));

        pet = field("Pet name");
        owner = field("Owner name");
        diagnosis = field("Diagnosis");
        medicine = field("Medicine name");
        dosage = field("e.g. As prescribed by veterinarian");
        frequency = field("e.g. As directed");
        duration = field("e.g. As directed");
        doctor = field("Doctor name");
        date = field(java.time.LocalDate.now().toString());

        instructions = new TextArea();
        instructions.setPromptText(
                "Care instructions, follow-up notes and precautions...");
        instructions.setWrapText(true);
        instructions.setPrefRowCount(3);

        GridPane form = new GridPane();
        form.setHgap(18);
        form.setVgap(15);

        add(form, "PET NAME", pet, 0, 0);
        add(form, "OWNER NAME", owner, 1, 0);
        add(form, "DIAGNOSIS", diagnosis, 0, 1);
        add(form, "MEDICINE", medicine, 1, 1);
        add(form, "DOSAGE", dosage, 0, 2);
        add(form, "FREQUENCY", frequency, 1, 2);
        add(form, "DURATION", duration, 0, 3);
        add(form, "VETERINARIAN", doctor, 1, 3);
        add(form, "PRESCRIPTION DATE", date, 0, 4);

        Label instructionLabel = new Label("CARE INSTRUCTIONS");
        instructionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 10));
        instructionLabel.setTextFill(Color.web("#526C5C"));

        VBox instructionBox = new VBox(8, instructionLabel, instructions);
        form.add(instructionBox, 1, 4);
        GridPane.setHgrow(instructionBox, Priority.ALWAYS);

        for (int i = 0; i < 2; i++) {
            ColumnConstraints c = new ColumnConstraints();
            c.setPercentWidth(50);
            c.setHgrow(Priority.ALWAYS);
            form.getColumnConstraints().add(c);
        }

        Label formTitle = new Label("New Prescription");
        formTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        formTitle.setTextFill(Color.web("#123D30"));

        Button save = primary("✓ Save Prescription");
        Button clear = light("Clear Form");
        Button print = light("Print Selected");
        Button delete = light("Delete Selected");

        status = new Label("Complete the form to create a prescription.");
        status.setTextFill(Color.web("#60776A"));
        status.setWrapText(true);

        save.setOnAction(e -> saveRecord());
        clear.setOnAction(e -> clearForm());
        print.setOnAction(e -> printRecord(stage));
        delete.setOnAction(e -> deleteRecord());

        HBox actions = new HBox(10, save, clear);
        actions.setAlignment(Pos.CENTER_LEFT);

        VBox formCard = new VBox(20, formTitle, form, actions, status);
        formCard.setPadding(new Insets(24));
        formCard.setStyle("-fx-background-color:white;"
                + "-fx-background-radius:18;-fx-border-color:#DCEAE0;"
                + "-fx-border-radius:18;");

        Label recordsTitle = new Label("Saved Prescriptions");
        recordsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        recordsTitle.setTextFill(Color.web("#123D30"));

        table = new TableView<>(records);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        table.setPlaceholder(new Label("Your saved prescriptions will appear here."));

        column("Pet", "pet");
        column("Owner", "owner");
        column("Diagnosis", "diagnosis");
        column("Medicine", "medicine");
        column("Date", "date");

        HBox recordActions = new HBox(10, print, delete);
        recordActions.setAlignment(Pos.CENTER_LEFT);

        VBox recordsCard = new VBox(16, recordsTitle, table, recordActions);
        recordsCard.setPadding(new Insets(24));
        recordsCard.setStyle("-fx-background-color:white;"
                + "-fx-background-radius:18;-fx-border-color:#DCEAE0;"
                + "-fx-border-radius:18;");
        VBox.setVgrow(table, Priority.ALWAYS);

        VBox content = new VBox(22, back, title, subtitle,
                formCard, recordsCard);
        content.setPadding(new Insets(28, 35, 30, 35));

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background:#F2F7F3;"
                + "-fx-background-color:#F2F7F3;"
                + "-fx-border-color:transparent;");
        root.setCenter(scroll);

        return new Scene(root);
    }

    private void column(String title, String key) {
        TableColumn<RxRecord, String> c = new TableColumn<>(title);
        c.setCellValueFactory(cell -> cell.getValue().get(key));
        table.getColumns().add(c);
    }

    private TextField field(String prompt) {
        TextField f = new TextField();
        f.setPromptText(prompt);
        f.setPrefHeight(42);
        f.setMaxWidth(Double.MAX_VALUE);
        f.setStyle("-fx-background-color:#F7FAF7;"
                + "-fx-border-color:#DCE8DE;-fx-border-radius:9;"
                + "-fx-background-radius:9;-fx-padding:0 12;");
        return f;
    }

    private void add(GridPane grid, String title, TextField field,
                     int col, int row) {
        Label label = new Label(title);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 10));
        label.setTextFill(Color.web("#526C5C"));

        VBox box = new VBox(8, label, field);
        box.setMaxWidth(Double.MAX_VALUE);
        grid.add(box, col, row);
        GridPane.setHgrow(box, Priority.ALWAYS);
    }

    private void saveRecord() {
        if (pet.getText().trim().isEmpty()
                || owner.getText().trim().isEmpty()
                || diagnosis.getText().trim().isEmpty()
                || medicine.getText().trim().isEmpty()
                || doctor.getText().trim().isEmpty()
                || date.getText().trim().isEmpty()) {
            status.setText("Please fill in pet, owner, diagnosis, medicine, doctor and date.");
            return;
        }

        records.add(new RxRecord(
                pet.getText().trim(),
                owner.getText().trim(),
                diagnosis.getText().trim(),
                medicine.getText().trim(),
                dosage.getText().trim(),
                frequency.getText().trim(),
                duration.getText().trim(),
                doctor.getText().trim(),
                date.getText().trim(),
                instructions.getText().trim()
        ));

        status.setText("Prescription saved for this session.");
        clearForm();
    }

    private void clearForm() {
        pet.clear();
        owner.clear();
        diagnosis.clear();
        medicine.clear();
        dosage.clear();
        frequency.clear();
        duration.clear();
        doctor.clear();
        instructions.clear();
        date.setText(java.time.LocalDate.now().toString());
    }

    private void deleteRecord() {
        RxRecord selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            status.setText("Select a prescription from the table first.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete this prescription?");
        confirm.setHeaderText("Confirm deletion");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            records.remove(selected);
            status.setText("Prescription deleted.");
        }
    }

    private void printRecord(Stage stage) {
        RxRecord r = table.getSelectionModel().getSelectedItem();

        if (r == null) {
            status.setText("Select a saved prescription to print.");
            return;
        }

        String text = "JEEVMITRA - VETERINARY PRESCRIPTION\n\n"
                + "Pet: " + r.pet.get() + "\n"
                + "Owner: " + r.owner.get() + "\n"
                + "Date: " + r.date.get() + "\n"
                + "Doctor: " + r.doctor.get() + "\n\n"
                + "Diagnosis: " + r.diagnosis.get() + "\n"
                + "Medicine: " + r.medicine.get() + "\n"
                + "Dosage: " + r.dosage.get() + "\n"
                + "Frequency: " + r.frequency.get() + "\n"
                + "Duration: " + r.duration.get() + "\n\n"
                + "Care Instructions:\n" + r.instructions.get() + "\n\n"
                + "Note: Medication and dosage must be determined "
                + "by the treating veterinarian.";

        TextArea printable = new TextArea(text);
        printable.setWrapText(true);
        printable.setEditable(false);
        printable.setStyle("-fx-font-family:monospace;-fx-font-size:13px;");
        printable.setPrefColumnCount(65);
        printable.setPrefRowCount(24);

        Dialog<ButtonType> preview = new Dialog<>();
        preview.setTitle("Prescription Print Preview");
        preview.setHeaderText("Review before printing");
        preview.getDialogPane().setContent(printable);
        preview.getDialogPane().getButtonTypes().addAll(
                ButtonType.OK, ButtonType.CANCEL);

        if (preview.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }

        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(stage)) {
            if (job.printPage(printable)) {
                job.endJob();
                status.setText("Prescription sent to printer.");
            } else {
                status.setText("Printing failed. Please try again.");
            }
        } else {
            status.setText("Printing cancelled or no printer available.");
        }
    }

    private Button primary(String text) {
        Button b = new Button(text);
        b.setTextFill(Color.WHITE);
        b.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        b.setStyle("-fx-background-color:#176B4D;"
                + "-fx-background-radius:10;-fx-padding:12 16;"
                + "-fx-cursor:hand;");
        return b;
    }

    private Button light(String text) {
        Button b = new Button(text);
        b.setTextFill(Color.web("#123D30"));
        b.setStyle("-fx-background-color:white;"
                + "-fx-border-color:#C8DDD0;-fx-border-radius:10;"
                + "-fx-background-radius:10;-fx-padding:11 16;"
                + "-fx-cursor:hand;");
        return b;
    }

    private static class RxRecord {
        SimpleStringProperty pet, owner, diagnosis, medicine;
        SimpleStringProperty dosage, frequency, duration, doctor, date, instructions;

        RxRecord(String pet, String owner, String diagnosis, String medicine,
                 String dosage, String frequency, String duration,
                 String doctor, String date, String instructions) {
            this.pet = new SimpleStringProperty(pet);
            this.owner = new SimpleStringProperty(owner);
            this.diagnosis = new SimpleStringProperty(diagnosis);
            this.medicine = new SimpleStringProperty(medicine);
            this.dosage = new SimpleStringProperty(dosage);
            this.frequency = new SimpleStringProperty(frequency);
            this.duration = new SimpleStringProperty(duration);
            this.doctor = new SimpleStringProperty(doctor);
            this.date = new SimpleStringProperty(date);
            this.instructions = new SimpleStringProperty(instructions);
        }

        SimpleStringProperty get(String key) {
            switch (key) {
                case "pet": return pet;
                case "owner": return owner;
                case "diagnosis": return diagnosis;
                case "medicine": return medicine;
                default: return date;
            }
        }
    }
}
