package com.techyuva.view.Doctor;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class VeterinarianProfile {

    public VBox getView(Runnable backAction) {

        VBox root = new VBox(15);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #F5F8F7;");

        Button back = new Button("← Back to Dashboard");
        back.setOnAction(e -> backAction.run());

        Label title = new Label("Veterinarian Profile");
        title.setStyle(
            "-fx-font-size: 26px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #004D40;"
        );

        TextField name = new TextField();
        name.setPromptText("Enter Doctor Name");

        TextField email = new TextField();
        email.setPromptText("Enter Email");

        TextField mobile = new TextField();
        mobile.setPromptText("Enter Mobile Number");

        TextField qualification = new TextField();
        qualification.setPromptText("Enter Qualification");

        Button save = new Button("Save Profile");

        Label message = new Label();

        save.setOnAction(e -> {
            message.setText("Profile details saved for this session.");
        });

        root.getChildren().addAll(
            back, title, name, email, mobile,
            qualification, save, message
        );

        return root;
    }
}