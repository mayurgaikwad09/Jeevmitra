package com.techyuva.view.Doctor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class MedicalRecords {

    private final ObservableList<String> records =
            FXCollections.observableArrayList();

    private VBox recordsList;
    private TextField searchField;

    public VBox getView(Runnable backAction) {

        VBox root = new VBox(20);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: #F5F8F7;");

        // Header
        Button backButton = new Button("← Back to Dashboard");
        backButton.setOnAction(e -> backAction.run());

        Label title = new Label("Medical Records");
        title.setStyle(
                "-fx-font-size: 27px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #004D40;"
        );

        Label subtitle = new Label(
                "Manage and view pet medical history"
        );
        subtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: #666666;");

        VBox header = new VBox(8, backButton, title, subtitle);

        // Form title
        Label formTitle = new Label("Add Medical Record");
        formTitle.setStyle(
                "-fx-font-size: 19px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #004D40;"
        );

        // Form fields
        TextField petField = new TextField();
        petField.setPromptText("Pet Name");

        TextField ownerField = new TextField();
        ownerField.setPromptText("Owner Name");

        TextField dateField = new TextField();
        dateField.setPromptText("Date (DD-MM-YYYY)");

        TextField diagnosisField = new TextField();
        diagnosisField.setPromptText("Diagnosis");

        TextField treatmentField = new TextField();
        treatmentField.setPromptText("Treatment / Medicine");

        Button saveButton = new Button("Save Record");
        saveButton.setStyle(
                "-fx-background-color: #004D40;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 10 20 10 20;"
        );

        Label message = new Label();
        message.setWrapText(true);

        saveButton.setOnAction(e -> {
            String pet = petField.getText().trim();
            String owner = ownerField.getText().trim();
            String date = dateField.getText().trim();
            String diagnosis = diagnosisField.getText().trim();
            String treatment = treatmentField.getText().trim();

            if (pet.isEmpty() || owner.isEmpty()
                    || date.isEmpty() || diagnosis.isEmpty()
                    || treatment.isEmpty()) {

                message.setText("Please fill in all fields.");
                message.setStyle("-fx-text-fill: #C62828;");
                return;
            }

            String record = "Pet: " + pet
                    + " | Owner: " + owner
                    + " | Date: " + date
                    + " | Diagnosis: " + diagnosis
                    + " | Treatment: " + treatment;

            records.add(record);
            showRecords("");

            petField.clear();
            ownerField.clear();
            dateField.clear();
            diagnosisField.clear();
            treatmentField.clear();

            message.setText("Medical record added successfully!");
            message.setStyle("-fx-text-fill: #2E7D32;");
        });

        VBox form = new VBox(
                12,
                formTitle,
                petField,
                ownerField,
                dateField,
                diagnosisField,
                treatmentField,
                saveButton,
                message
        );

        form.setPadding(new Insets(20));
        form.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: #DCE7E3;" +
                "-fx-border-radius: 12;"
        );

        // Records section
        Label recordsTitle = new Label("Recent Medical Records");
        recordsTitle.setStyle(
                "-fx-font-size: 19px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #004D40;"
        );

        searchField = new TextField();
        searchField.setPromptText("Search pet, owner or diagnosis...");
        searchField.textProperty().addListener(
                (observable, oldValue, newValue) ->
                        showRecords(newValue)
        );

        recordsList = new VBox(10);
        recordsList.setPadding(new Insets(5));

        Label emptyLabel = new Label("No medical records added yet.");
        emptyLabel.setStyle("-fx-text-fill: #777777;");
        recordsList.getChildren().add(emptyLabel);

        ScrollPane scrollPane = new ScrollPane(recordsList);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(220);
        scrollPane.setStyle("-fx-background: transparent;");

        VBox recordsSection = new VBox(
                12,
                recordsTitle,
                searchField,
                scrollPane
        );

        VBox.setVgrow(recordsSection, Priority.ALWAYS);

        root.getChildren().addAll(
                header,
                new Separator(),
                form,
                recordsSection
        );

        return root;
    }

    private void showRecords(String searchText) {

        recordsList.getChildren().clear();

        String search = searchText == null
                ? ""
                : searchText.trim().toLowerCase();

        boolean found = false;

        for (String record : records) {
            if (record.toLowerCase().contains(search)) {

                Label recordLabel = new Label(record);
                recordLabel.setWrapText(true);
                recordLabel.setMaxWidth(Double.MAX_VALUE);
                recordLabel.setStyle(
                        "-fx-background-color: white;" +
                        "-fx-padding: 14;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-color: #DCE7E3;" +
                        "-fx-border-radius: 8;"
                );

                recordsList.getChildren().add(recordLabel);
                found = true;
            }
        }

        if (!found) {
            Label emptyLabel = new Label(
                    search.isEmpty()
                            ? "No medical records added yet."
                            : "No matching records found."
            );

            emptyLabel.setStyle("-fx-text-fill: #777777;");
            recordsList.getChildren().add(emptyLabel);
        }
    }
}