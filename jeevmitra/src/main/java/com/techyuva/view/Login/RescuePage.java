package com.techyuva.view.Login;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class RescuePage {

    public Scene fromScene;

    public Scene getfromScene(Runnable goToUser) {

        // Page heading
        Text title = new Text("JeevMitra");
        title.setStyle(
            "-fx-font-size: 30px;" +
            "-fx-font-weight: bold;" +
            "-fx-fill: #16864D;"
        );

        Text subtitle = new Text("Animal Rescue Portal");
        subtitle.setStyle(
            "-fx-font-size: 20px;" +
            "-fx-font-weight: bold;" +
            "-fx-fill: #185C39;"
        );

        Text message = new Text(
            "Help animals. Save lives."
        );
        message.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-fill: #647568;"
        );

        // Email label and field
        Text emailTx = new Text("Email");
        emailTx.setStyle(
            "-fx-font-size: 15px;" +
            "-fx-fill: #185C39;" +
            "-fx-font-weight: bold;"
        );

        TextField emailField = new TextField();
        emailField.setPromptText("Enter your email");
        emailField.setPrefHeight(40);
        emailField.setMaxWidth(300);
        emailField.setStyle(fieldStyle());

        // Password label and field
        Text pass = new Text("Password");
        pass.setStyle(
            "-fx-font-size: 15px;" +
            "-fx-fill: #185C39;" +
            "-fx-font-weight: bold;"
        );

        PasswordField passField = new PasswordField();
        passField.setPromptText("Enter your password");
        passField.setPrefHeight(40);
        passField.setMaxWidth(300);
        passField.setStyle(fieldStyle());

        // Login button
        Button login = new Button("Login");
        login.setMaxWidth(300);
        login.setPrefHeight(42);
        login.setStyle(
            "-fx-background-color: #16864D;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 15px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 10;" +
            "-fx-cursor: hand;"
        );

        login.setOnAction(e -> {

            if (emailField.getText().trim().isEmpty()
                    || passField.getText().isEmpty()) {

                Alert alert = new Alert(
                    Alert.AlertType.WARNING
                );

                alert.setTitle("Rescue Login");
                alert.setHeaderText("Missing Details");
                alert.setContentText(
                    "Please enter your email and password."
                );
                alert.showAndWait();

            } else {
                Alert alert = new Alert(
                    Alert.AlertType.INFORMATION
                );

                alert.setTitle("Rescue Login");
                alert.setHeaderText("Login UI Ready");
                alert.setContentText(
                    "Connect authentication to verify your login."
                );
                alert.showAndWait();
            }
        });

        // Back button
        Button back = new Button("Back");
        back.setMaxWidth(300);
        back.setPrefHeight(38);
        back.setStyle(
            "-fx-background-color: white;" +
            "-fx-text-fill: #16864D;" +
            "-fx-border-color: #16864D;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;" +
            "-fx-cursor: hand;"
        );

        back.setOnAction(e -> {
            if (goToUser != null) {
                goToUser.run();
            }
        });

        // Login card
        VBox card = new VBox(
            12,
            title,
            subtitle,
            message,
            emailTx,
            emailField,
            pass,
            passField,
            login,
            back
        );

        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(28, 35, 28, 35));
        card.setMaxWidth(390);

        card.setStyle(
            "-fx-background-color: rgba(255,255,255,0.95);" +
            "-fx-background-radius: 22;" +
            "-fx-border-color: #D5EBDD;" +
            "-fx-border-radius: 22;" +
            "-fx-effect: dropshadow(gaussian," +
            "rgba(20,90,50,0.18), 22, 0.2, 0, 7);"
        );

        // Main root
        StackPane root = new StackPane();

        root.setStyle(
            "-fx-background-color: linear-gradient(" +
            "to bottom right, #F5FFF8, #DDF5E5, #BCE8CE);"
        );

        // Animated green waves
        GradientWaves waves = new GradientWaves();

        waves.prefWidthProperty().bind(root.widthProperty());
        waves.prefHeightProperty().bind(root.heightProperty());

        // Waves behind the card
        root.getChildren().addAll(waves, card);

        StackPane.setAlignment(card, Pos.CENTER);

        fromScene = new Scene(root, 1280, 760);

        return fromScene;
    }

    private String fieldStyle() {
        return
            "-fx-background-color: #F5FBF7;" +
            "-fx-border-color: #C9E5D2;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 10;" +
            "-fx-font-size: 14px;";
    }
}

/*package com.jivmitra.View;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class RescuePage {
    public Scene fromScene;
    public Scene getfromScene(Runnable goToUser){

    Text emailTx=new Text("Email");
    emailTx.setStyle("-fx-font-size:20px;-fx-fill:black");

    TextField emailField=new TextField();
    emailField.setFocusTraversable(false);
    emailField.setPromptText("enter Email");
    
    emailField.setPrefHeight(30);
    emailField.setMaxHeight(30);


    Text Pass=new Text("Password");
    Pass.setStyle("-fx-font-size:20px;-fx-fill:black;");

    TextField PassField=new TextField();
    PassField.setFocusTraversable(false);
    PassField.setPromptText("enter Enter Password");

Button Login=new Button("Login");
Login.setOnAction(r->{

});


      Button Back=new Button("back");
        Back.setOnAction(e->{
            System.out.println("Back clicked");
            goToUser.run();
        });

        VBox vb=new VBox(10,emailTx,emailField,Pass,PassField,Login,Back);
        vb.setPadding(new Insets(200));
        vb.setAlignment(Pos.CENTER);
        Scene sc=new Scene(vb,900,500);
        return sc;

    }
    
}
*/
