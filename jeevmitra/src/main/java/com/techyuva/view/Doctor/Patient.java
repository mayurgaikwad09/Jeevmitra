package com.techyuva.view.Doctor;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Patient {

    private VBox patientList;

    public VBox getView(Runnable backAction) {

        VBox root = new VBox(18);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: #F5F8F7;");

        Button backButton = new Button("← Back to Dashboard");
        backButton.setOnAction(e -> backAction.run());

        Label title = new Label("Patient Management");
        title.setFont(Font.font("Arial", 28));
        title.setTextFill(Color.web("#004D40"));

        Label subtitle = new Label(
            "Manage pet patients and owner information"
        );

        TextField searchField = new TextField();
        searchField.setPromptText("Search patient by name or ID...");
        searchField.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(searchField, Priority.ALWAYS);

        Button addButton = new Button("+ Add New Patient");

        HBox searchRow = new HBox(12, searchField, addButton);
        searchRow.setAlignment(Pos.CENTER_LEFT);

        Label formTitle = new Label("Add Patient Details");
        formTitle.setFont(Font.font("Arial", 19));
        formTitle.setTextFill(Color.web("#004D40"));

        TextField petName = new TextField();
        petName.setPromptText("Enter pet name");

        TextField petType = new TextField();
        petType.setPromptText("Dog / Cat / Bird / Other");

        TextField breed = new TextField();
        breed.setPromptText("Enter breed");

        TextField age = new TextField();
        age.setPromptText("Enter age");

        TextField ownerName = new TextField();
        ownerName.setPromptText("Enter owner name");

        TextField phone = new TextField();
        phone.setPromptText("Enter mobile number");

        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(12);
        form.setPadding(new Insets(18));

        form.setStyle(
            "-fx-background-color:white;" +
            "-fx-background-radius:12;"
        );

        form.add(new Label("Pet Name *"), 0, 0);
        form.add(petName, 1, 0);

        form.add(new Label("Pet Type *"), 0, 1);
        form.add(petType, 1, 1);

        form.add(new Label("Breed"), 0, 2);
        form.add(breed, 1, 2);

        form.add(new Label("Age"), 0, 3);
        form.add(age, 1, 3);

        form.add(new Label("Owner Name *"), 0, 4);
        form.add(ownerName, 1, 4);

        form.add(new Label("Mobile Number *"), 0, 5);
        form.add(phone, 1, 5);

        Button saveButton = new Button("Save Patient");

        saveButton.setStyle(
            "-fx-background-color:#008577;" +
            "-fx-text-fill:white;" +
            "-fx-font-weight:bold;" +
            "-fx-padding:10 22;" +
            "-fx-background-radius:8;"
        );

        Label message = new Label();

        Label listTitle = new Label("Registered Patients");
        listTitle.setFont(Font.font("Arial", 19));
        listTitle.setTextFill(Color.web("#004D40"));

        patientList = new VBox(10);
        patientList.setPadding(new Insets(10));

        Label emptyMessage = new Label(
            "No patients added yet."
        );

        patientList.getChildren().add(emptyMessage);

        // SAVE PATIENT
        saveButton.setOnAction(e -> {

            if (petName.getText().trim().isEmpty()
                    || petType.getText().trim().isEmpty()
                    || ownerName.getText().trim().isEmpty()
                    || phone.getText().trim().isEmpty()) {

                message.setText(
                    "Please fill all required fields (*)."
                );
                message.setTextFill(Color.RED);
                return;
            }

            String name = petName.getText().trim();
            String type = petType.getText().trim();
            String petBreed = breed.getText().trim();
            String petAge = age.getText().trim();
            String owner = ownerName.getText().trim();
            String mobile = phone.getText().trim();

            if (patientList.getChildren().size() == 1
                    && patientList.getChildren().get(0)
                    == emptyMessage) {
                patientList.getChildren().clear();
            }

            VBox patientCard = new VBox(6);

            patientCard.setPadding(new Insets(15));
            patientCard.setStyle(
                "-fx-background-color:white;" +
                "-fx-background-radius:10;" +
                "-fx-border-color:#CDE7DB;" +
                "-fx-border-radius:10;"
            );

            Label petLabel = new Label(name + "  •  " + type);
            petLabel.setFont(Font.font("Arial", 16));
            petLabel.setTextFill(Color.web("#004D40"));

            Label breedLabel = new Label("Breed: " + petBreed);
            Label ageLabel = new Label("Age: " + petAge);
            Label ownerLabel = new Label("Owner: " + owner);
            Label phoneLabel = new Label("Mobile: " + mobile);

            patientCard.getChildren().addAll(
                petLabel,
                breedLabel,
                ageLabel,
                ownerLabel,
                phoneLabel
            );

            patientCard.setUserData(name.toLowerCase());

            patientList.getChildren().add(patientCard);

            message.setText("Patient added to the list successfully!");
            message.setTextFill(Color.web("#008577"));

            petName.clear();
            petType.clear();
            breed.clear();
            age.clear();
            ownerName.clear();
            phone.clear();
        });

        // SEARCH PATIENT
        searchField.textProperty().addListener(
            (observable, oldValue, newValue) -> {

                String search = newValue.trim().toLowerCase();

                for (javafx.scene.Node node :
                        patientList.getChildren()) {

                    if (node instanceof VBox) {
                        String name = String.valueOf(
                            node.getUserData()
                        );

                        node.setVisible(name.contains(search));
                        node.setManaged(name.contains(search));
                    }
                }
            }
        );

        // ADD NEW PATIENT
        addButton.setOnAction(e -> {
            petName.clear();
            petType.clear();
            breed.clear();
            age.clear();
            ownerName.clear();
            phone.clear();

            message.setText("");
            petName.requestFocus();
        });

        ScrollPane scrollPane = new ScrollPane(patientList);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(200);
        scrollPane.setStyle(
            "-fx-background:transparent;" +
            "-fx-background-color:transparent;"
        );

        root.getChildren().addAll(
            backButton,
            title,
            subtitle,
            searchRow,
            formTitle,
            form,
            saveButton,
            message,
            listTitle,
            scrollPane
        );

        return root;
    }
}