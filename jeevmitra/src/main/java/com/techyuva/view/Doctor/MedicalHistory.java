
package com.techyuva.view.Doctor;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MedicalHistory {

    private final ObservableList<Record> records =
            FXCollections.observableArrayList();

    private TableView<Record> table;
    private TextField search;

    public Scene createScene(Stage stage, Runnable backAction) {
        if (records.isEmpty()) {
            records.addAll(
                new Record("Buddy", "Rahul Patil", "General Check-up",
                    "Healthy", "2026-10-20", "Routine examination completed."),
                new Record("Milo", "Sneha Joshi", "Vaccination",
                    "Completed", "2026-10-21", "Vaccination recorded."),
                new Record("Coco", "Amit Shah", "Skin Examination",
                    "Follow-up", "2026-10-22", "Follow-up recommended.")
            );
        }

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:#F2F7F3;");

        Label logo = new Label("🐾 JeevMitra");
        logo.setTextFill(Color.WHITE);
        logo.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 25));

        Region gap = new Region();
        HBox.setHgrow(gap, Priority.ALWAYS);

        Label tag = new Label("MEDICAL RECORDS");
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

        Label title = new Label("Pet Medical History");
        title.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 28));
        title.setTextFill(Color.web("#123D30"));

        Label subtitle = new Label(
                "A clear timeline of treatment, check-ups and recovery.");
        subtitle.setTextFill(Color.web("#687B70"));

        Label total = new Label("📋  " + records.size() + " DEMO RECORDS");
        total.setTextFill(Color.web("#176B4D"));
        total.setStyle("-fx-background-color:#E0F3E6;"
                + "-fx-background-radius:18;-fx-padding:10 14;");

        HBox titleRow = new HBox(15, title, total);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        search = new TextField();
        search.setPromptText("Search by pet, owner, diagnosis or date...");
        search.setPrefHeight(42);

        Button add = primary("+ Add Medical Record");
        add.setOnAction(e -> addRecord());

        HBox toolbar = new HBox(12, search, add);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(search, Priority.ALWAYS);

        table = new TableView<>(records);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        table.setPlaceholder(new Label("No medical records found."));

        column("Pet Name", "pet");
        column("Owner", "owner");
        column("Treatment / Diagnosis", "diagnosis");
        column("Status", "status");
        column("Visit Date", "date");

        search.textProperty().addListener((o, old, value) -> filterRecords());

        Button view = light("View Record");
        view.setOnAction(e -> viewRecord());

        Button delete = light("Delete Selected");
        delete.setOnAction(e -> deleteRecord());

        HBox actions = new HBox(12, view, delete);
        actions.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(18, toolbar, table, actions);
        card.setPadding(new Insets(22));
        card.setStyle("-fx-background-color:white;"
                + "-fx-background-radius:18;-fx-border-color:#DCEAE0;"
                + "-fx-border-radius:18;");
        VBox.setVgrow(table, Priority.ALWAYS);

        VBox content = new VBox(22, back, titleRow, subtitle, card);
        content.setPadding(new Insets(28, 35, 30, 35));
        VBox.setVgrow(card, Priority.ALWAYS);

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background:#F2F7F3;"
                + "-fx-background-color:#F2F7F3;"
                + "-fx-border-color:transparent;");
        root.setCenter(scroll);

        return new Scene(root);
    }

    private void column(String title, String key) {
        TableColumn<Record, String> c = new TableColumn<>(title);
        c.setCellValueFactory(cell -> cell.getValue().get(key));
        table.getColumns().add(c);
    }

    private void filterRecords() {
        String q = search.getText().toLowerCase().trim();
        ObservableList<Record> result = FXCollections.observableArrayList();

        for (Record r : records) {
            String all = r.pet.get() + " " + r.owner.get() + " "
                    + r.diagnosis.get() + " " + r.date.get();
            if (all.toLowerCase().contains(q)) result.add(r);
        }
        table.setItems(result);
    }

    private void addRecord() {
        Dialog<Record> dialog = new Dialog<>();
        dialog.setTitle("Add Medical Record");
        dialog.setHeaderText("Enter pet treatment details");

        ButtonType saveType = new ButtonType("Save Record",
                ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(
                saveType, ButtonType.CANCEL);

        TextField pet = new TextField();
        pet.setPromptText("Pet name");
        TextField owner = new TextField();
        owner.setPromptText("Owner name");
        TextField diagnosis = new TextField();
        diagnosis.setPromptText("Treatment / diagnosis");
        TextField status = new TextField();
        status.setPromptText("e.g. Stable, Follow-up");
        DatePicker date = new DatePicker();
        TextArea notes = new TextArea();
        notes.setPromptText("Treatment notes");
        notes.setPrefRowCount(3);

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(12);
        form.addRow(0, new Label("Pet"), pet);
        form.addRow(1, new Label("Owner"), owner);
        form.addRow(2, new Label("Diagnosis"), diagnosis);
        form.addRow(3, new Label("Status"), status);
        form.addRow(4, new Label("Visit Date"), date);
        form.addRow(5, new Label("Notes"), notes);
        dialog.getDialogPane().setContent(form);

        dialog.setResultConverter(button -> {
            if (button != saveType) return null;

            if (pet.getText().trim().isEmpty()
                    || owner.getText().trim().isEmpty()
                    || diagnosis.getText().trim().isEmpty()
                    || date.getValue() == null) {
                new Alert(Alert.AlertType.WARNING,
                        "Pet, owner, diagnosis and date are required.")
                        .showAndWait();
                return null;
            }

            return new Record(pet.getText().trim(), owner.getText().trim(),
                    diagnosis.getText().trim(),
                    status.getText().trim().isEmpty()
                            ? "Recorded" : status.getText().trim(),
                    date.getValue().toString(), notes.getText().trim());
        });

        dialog.showAndWait().ifPresent(records::add);
        filterRecords();
    }

    private void viewRecord() {
        Record r = table.getSelectionModel().getSelectedItem();
        if (r == null) {
            message("Please select a record first.");
            return;
        }

        message("PET: " + r.pet.get()
                + "\nOWNER: " + r.owner.get()
                + "\nTREATMENT: " + r.diagnosis.get()
                + "\nSTATUS: " + r.status.get()
                + "\nDATE: " + r.date.get()
                + "\n\nNOTES:\n" + r.notes.get());
    }

    private void deleteRecord() {
        Record r = table.getSelectionModel().getSelectedItem();
        if (r == null) {
            message("Select a record to delete.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete the selected medical record?");
        confirm.setHeaderText("Confirm deletion");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            records.remove(r);
            filterRecords();
        }
    }

    private void message(String text) {
        new Alert(Alert.AlertType.INFORMATION, text).showAndWait();
    }

    private Button primary(String text) {
        Button b = new Button(text);
        b.setTextFill(Color.WHITE);
        b.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        b.setStyle("-fx-background-color:#176B4D;"
                + "-fx-background-radius:10;-fx-padding:12 16;");
        return b;
    }

    private Button light(String text) {
        Button b = new Button(text);
        b.setTextFill(Color.web("#123D30"));
        b.setStyle("-fx-background-color:white;"
                + "-fx-border-color:#C8DDD0;-fx-border-radius:10;"
                + "-fx-background-radius:10;-fx-padding:11 16;");
        return b;
    }

    private static class Record {
        SimpleStringProperty pet, owner, diagnosis, status, date, notes;

        Record(String pet, String owner, String diagnosis, String status,
               String date, String notes) {
            this.pet = new SimpleStringProperty(pet);
            this.owner = new SimpleStringProperty(owner);
            this.diagnosis = new SimpleStringProperty(diagnosis);
            this.status = new SimpleStringProperty(status);
            this.date = new SimpleStringProperty(date);
            this.notes = new SimpleStringProperty(notes);
        }

        SimpleStringProperty get(String key) {
            switch (key) {
                case "pet": return pet;
                case "owner": return owner;
                case "diagnosis": return diagnosis;
                case "status": return status;
                default: return date;
            }
        }
    }
}
