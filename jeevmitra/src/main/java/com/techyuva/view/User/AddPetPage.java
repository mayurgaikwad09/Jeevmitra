package com.techyuva.view.User;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class AddPetPage {

    public Scene getAddPetScene(Runnable backtoHome) {

        // ---------- BACK BUTTON ----------
        Button back = new Button("← Back");
        back.setStyle("-fx-background-color:white;-fx-text-fill:#1e6b4f;-fx-font-size:13px;-fx-border-color:#1e6b4f;-fx-border-radius:8px;-fx-background-radius:8px;-fx-padding:6px 16px;-fx-cursor:hand;");
        back.setOnAction(e -> {
            System.out.println("back button clicked");
            backtoHome.run();   // body Home.java madhe ahe
        });

        // ---------- TITLE ----------
        Text title = new Text("Add Pet");
        title.setStyle("-fx-font-size:28px;-fx-font-weight:bold;-fx-fill:#145a41;");

        // ---------- PET NAME ----------
        Text lblPetName = new Text("Pet Name");
        lblPetName.setStyle("-fx-font-size:15px;-fx-fill:#245B4B;");
        TextField inPetName = new TextField();
        inPetName.setPromptText("Enter pet name");
        inPetName.setMaxWidth(420);

        // ---------- BREED ----------
        Text lblBreed = new Text("Breed");
        lblBreed.setStyle("-fx-font-size:15px;-fx-fill:#245B4B;");
        TextField inBreed = new TextField();
        inBreed.setPromptText("Golden Retriever / Persian Cat ...");
        inBreed.setMaxWidth(420);

        // ---------- AGE ----------
        Text lblAge = new Text("Age");
        lblAge.setStyle("-fx-font-size:15px;-fx-fill:#245B4B;");
        TextField inAge = new TextField();
        inAge.setPromptText("3 years");
        inAge.setMaxWidth(420);

        // ---------- NEXT VACCINATION DATE ----------
        Text lblNextVaccinationDate = new Text("Next Vaccination Date");
        lblNextVaccinationDate.setStyle("-fx-font-size:15px;-fx-fill:#245B4B;");
        DatePicker inNextVaccinationDate = new DatePicker();
        inNextVaccinationDate.setPromptText("Select date");
        inNextVaccinationDate.setMaxWidth(420);

        // ---------- MESSAGE (success / error) ----------
        Text status = new Text();
        status.setStyle("-fx-font-size:15px;");

        // ---------- SUBMIT BUTTON ----------
        Button submit = new Button("Save Pet");
        submit.setStyle("-fx-background-color:#1e6b4f;-fx-text-fill:white;-fx-font-size:14px;-fx-font-weight:bold;-fx-background-radius:9px;-fx-padding:10px 22px;-fx-cursor:hand;");

        submit.setOnAction(e -> {
            // 1) fields madhun value ghya
            String vPetName = inPetName.getText().trim();
            String vBreed = inBreed.getText().trim();
            String vAge = inAge.getText().trim();
            String vNextVaccinationDate = (inNextVaccinationDate.getValue() == null) ? "" : inNextVaccinationDate.getValue().toString();

            // 2) rikame ahe ka te tapasa
            if (vPetName.isEmpty() || vBreed.isEmpty() || vAge.isEmpty() || vNextVaccinationDate.isEmpty()) {
                status.setStyle("-fx-font-size:15px;-fx-fill:red;");
                status.setText("Please fill all the fields");
                return;
            }

            // 3) DATABASE (pude ithe) -------------------------------
            // INSERT INTO pets (pet_name, breed, age, next_vaccination_date) VALUES (?, ?, ?, ?)
            // yaachya jaagi tumcha DB class cha method call kara
            System.out.println("pets -> " + vPetName + ", " + vBreed + ", " + vAge + ", " + vNextVaccinationDate);

            // 4) user la dakhva
            status.setStyle("-fx-font-size:15px;-fx-fill:#1e6b4f;");
            status.setText("Pet saved");
            inPetName.clear();
            inBreed.clear();
            inAge.clear();
            inNextVaccinationDate.setValue(null);
        });

        VBox vb = new VBox(10, back, title, lblPetName, inPetName, lblBreed, inBreed, lblAge, inAge, lblNextVaccinationDate, inNextVaccinationDate, submit, status);
        vb.setPadding(new Insets(40));
        vb.setStyle("-fx-background-color:#F7FCFA;");

        // sidebar ekda banvla ahe (Sidebar.java), ithe fakt lavaycha
        HBox root = new HBox(0, Sidebar.create("Dashboard"), vb);
        HBox.setHgrow(vb, Priority.ALWAYS);

        return new Scene(root, 1300, 760);
    }
}