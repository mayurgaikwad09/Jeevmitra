package com.techyuva.view.Login;



import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Random;

public class DoctorPage {

    private final Random random = new Random();

    public Scene getfromScene(Runnable backTO) {

        StackPane root = new StackPane();

        root.setStyle(
            "-fx-background-color: linear-gradient("
            + "to bottom right, #F7FFF9, #DDF5E5, #BCE8CE);"
        );

        // Particles background layer
        Pane particleLayer = new Pane();
        particleLayer.setMouseTransparent(true);
        particleLayer.prefWidthProperty().bind(root.widthProperty());
        particleLayer.prefHeightProperty().bind(root.heightProperty());

        // Login card
        Text logo = new Text("🐾 JeevMitra");
        logo.setFont(Font.font("Arial", FontWeight.BOLD, 29));
        logo.setFill(Color.web("#16834B"));

        Text heading = new Text("Doctor Login");
        heading.setFont(Font.font("Arial", FontWeight.BOLD, 25));
        heading.setFill(Color.web("#145C37"));

        Text subtitle = new Text(
            "Care for every life, one step at a time"
        );
        subtitle.setFont(Font.font("Arial", 13));
        subtitle.setFill(Color.web("#647568"));

        TextField email = new TextField();
        email.setPromptText("Enter your email");
        styleField(email);

        PasswordField password = new PasswordField();
        password.setPromptText("Enter your password");
        styleField(password);

        Button login = new Button("Login");
        login.setMaxWidth(Double.MAX_VALUE);
        login.setPrefHeight(42);
        login.setStyle(
            "-fx-background-color: #16864D;"
            + "-fx-text-fill: white;"
            + "-fx-font-size: 15px;"
            + "-fx-font-weight: bold;"
            + "-fx-background-radius: 10;"
            + "-fx-cursor: hand;"
        );

        login.setOnAction(e -> {
            if (email.getText().trim().isEmpty()
                    || password.getText().isEmpty()) {
                showMessage("Please enter your email and password.");
            } else {
                showMessage(
                    "Login UI ready. Connect Firebase or database authentication."
                );
            }
        });

        Button back = new Button("Back");
        back.setMaxWidth(Double.MAX_VALUE);
        back.setPrefHeight(38);
        back.setStyle(
            "-fx-background-color: transparent;"
            + "-fx-text-fill: #16864D;"
            + "-fx-border-color: #16864D;"
            + "-fx-border-radius: 9;"
            + "-fx-background-radius: 9;"
            + "-fx-cursor: hand;"
        );

        back.setOnAction(e -> {
            if (backTO != null) {
                backTO.run();
            }
        });

        VBox card = new VBox(
            15, logo, heading, subtitle, email, password, login, back
        );

        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(30, 35, 30, 35));
        card.setMaxWidth(390);
        card.setStyle(
            "-fx-background-color: rgba(255,255,255,0.94);"
            + "-fx-background-radius: 24;"
            + "-fx-border-color: rgba(255,255,255,0.9);"
            + "-fx-border-radius: 24;"
            + "-fx-effect: dropshadow(gaussian,"
            + " rgba(20,90,50,0.18), 25, 0.2, 0, 8);"
        );

        // Add particles behind the card
        root.getChildren().addAll(particleLayer, card);

        // Create particles after the layout gets its size
        root.widthProperty().addListener((obs, oldVal, newVal) ->
            createParticles(particleLayer)
        );

        root.heightProperty().addListener((obs, oldVal, newVal) ->
            createParticles(particleLayer)
        );

        // Initial particles
        Platform.runLater(() -> createParticles(particleLayer));

        return new Scene(root,1280 , 760);
    }

    private void styleField(TextField field) {
        field.setPrefHeight(42);
        field.setMaxWidth(300);
        field.setStyle(
            "-fx-background-color: #F5FBF7;"
            + "-fx-border-color: #C9E5D2;"
            + "-fx-border-radius: 10;"
            + "-fx-background-radius: 10;"
            + "-fx-padding: 10;"
            + "-fx-font-size: 14px;"
        );
    }

    private void createParticles(Pane layer) {

        double width = layer.getWidth();
        double height = layer.getHeight();

        if (width <= 0 || height <= 0) {
            return;
        }

        // Avoid creating duplicate particles on every resize
        if (layer.getChildren().size() >= 35) {
            return;
        }

        for (int i = layer.getChildren().size(); i < 35; i++) {

            double radius = 2 + random.nextDouble() * 4;

            Circle particle = new Circle(
                radius,
                Color.rgb(22, 134, 77, 0.55)
            );

            particle.setEffect(
                new javafx.scene.effect.Glow(0.65)
            );

            double x = random.nextDouble() * width;
            double y = random.nextDouble() * height;
            double travel = 70 + random.nextDouble() * 140;
            double seconds = 3 + random.nextDouble() * 5;

            particle.setCenterX(x);
            particle.setCenterY(y);
            particle.setOpacity(0);

            layer.getChildren().add(particle);

            TranslateTransition move = new TranslateTransition(
                Duration.seconds(seconds), particle
            );

            move.setFromY(0);
            move.setToY(-travel);
            move.setFromX(0);
            move.setToX(-25 + random.nextDouble() * 50);
            move.setInterpolator(
                javafx.animation.Interpolator.EASE_BOTH
            );

            FadeTransition fade = new FadeTransition(
                Duration.seconds(seconds), particle
            );

            fade.setFromValue(0);
            fade.setToValue(0.85);

            ParallelTransition animation =
                new ParallelTransition(move, fade);

            animation.setCycleCount(
                javafx.animation.Animation.INDEFINITE
            );

            // Start particles at different times
            animation.setDelay(
                Duration.millis(random.nextInt(2500))
            );

            animation.play();
        }
    }

    private void showMessage(String message) {
        javafx.scene.control.Alert alert =
            new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.INFORMATION
            );

        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
}


/*package com.jivmitra.View;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class DoctorPage {
public Scene DocScene;
    public Scene getfromScene(Runnable backTO){
        
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



        Button back = new Button("Back");
        back.setOnAction(w->{
        System.out.println("back Clicked");
        backTO.run();
 });

        VBox vb=new VBox(emailTx,emailField,Pass,PassField,Login,back);
        Scene sc=new Scene(vb,900,500);
        return sc;
        
    }
    

    }
    */


