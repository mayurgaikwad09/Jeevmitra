package com.techyuva.view.Login;



import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class AdminPage {

    public Scene fromScene;

    public Scene getfromScene(Runnable goToUser) {

        // Email label
        Text emailTx = new Text("Email");
        emailTx.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-fill: #174D35;" +
            "-fx-font-weight: bold;"
        );

        // Email field
        TextField emailField = new TextField();
        emailField.setPromptText("Enter your email");
        emailField.setFocusTraversable(false);
        emailField.setPrefHeight(42);
        emailField.setMaxWidth(280);
        emailField.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: #B8DDC5;" +
            "-fx-border-radius: 12;" +
            "-fx-padding: 10;"
        );

        // Password label
        Text pass = new Text("Password");
        pass.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-fill: #174D35;" +
            "-fx-font-weight: bold;"
        );

        // Password field
        PasswordField passField = new PasswordField();
        passField.setPromptText("Enter your password");
        passField.setFocusTraversable(false);
        passField.setPrefHeight(42);
        passField.setMaxWidth(280);
        passField.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: #B8DDC5;" +
            "-fx-border-radius: 12;" +
            "-fx-padding: 10;"
        );

        // Heading
        Text heading = new Text("Admin Login");
        heading.setStyle(
            "-fx-font-size: 30px;" +
            "-fx-font-weight: bold;" +
            "-fx-fill: #087443;"
        );

        Text subtitle = new Text("Manage JeevMitra with care");
        subtitle.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-fill: #527665;"
        );

        // Login button
        Button Logi = new Button("Login");
        Logi.setPrefWidth(280);
        Logi.setPrefHeight(44);
        Logi.setStyle(
            "-fx-background-color: linear-gradient(to right, #087443, #20A464);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 22;" +
            "-fx-cursor: hand;"
        );

        Logi.setOnAction(e -> {

                if (emailField.getText().equals("1")) {
                    System.out.println("Login to User");
                    
                }
                else if (emailField.getText().equals("2")) {
                    System.out.println("Login to Doctor");
                }
                else if (emailField.getText().equals("3")) {
                    System.out.println("Login to Rescue");
                }
                else if(emailField.getText().equals("4")) {
                    System.out.println("Login to Admin");
                }
        });

        // Back button
        Button Back = new Button("←  Back");
        Back.setPrefWidth(280);
        Back.setPrefHeight(40);
        Back.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-border-color: #087443;" +
            "-fx-border-radius: 22;" +
            "-fx-background-radius: 22;" +
            "-fx-text-fill: #087443;" +
            "-fx-font-size: 15px;" +
            "-fx-cursor: hand;"
        );

        Back.setOnAction(e -> {
            if (goToUser != null) {
                goToUser.run();
            }
        });

        // Login card
        VBox vb = new VBox(
            12,
            heading, subtitle,
            emailTx, emailField,
            pass, passField,
            Logi, Back
        );

        vb.setAlignment(Pos.CENTER);
        vb.setPadding(new Insets(30));
        vb.setMaxWidth(370);
        vb.setStyle(
            "-fx-background-color: rgba(255,255,255,0.94);" +
            "-fx-background-radius: 25;" +
            "-fx-border-color: #D2EBDD;" +
            "-fx-border-radius: 25;"
        );

        // Animated background
        StackPane root = new StackPane();
        root.setStyle(
            "-fx-background-color: linear-gradient(" +
            "to bottom right, #073B2A, #16845B, #C8F0D2);"
        );

        // Floating particles
        Circle c1 = new Circle(25, Color.web("#A8FFD0", 0.35));
        Circle c2 = new Circle(15, Color.web("#FFFFFF", 0.45));
        Circle c3 = new Circle(35, Color.web("#4DFFA8", 0.20));
        Circle c4 = new Circle(12, Color.web("#E4FFEF", 0.55));

        c1.setTranslateX(-300);
        c1.setTranslateY(-140);

        c2.setTranslateX(280);
        c2.setTranslateY(100);

        c3.setTranslateX(250);
        c3.setTranslateY(-180);

        c4.setTranslateX(-220);
        c4.setTranslateY(180);

        // Animation 1: Up and down
        TranslateTransition move1 =
            new TranslateTransition(Duration.seconds(3), c1);
        move1.setByY(100);
        move1.setAutoReverse(true);
        move1.setCycleCount(Animation.INDEFINITE);
        move1.play();

        // Animation 2: Left and right
        TranslateTransition move2 =
            new TranslateTransition(Duration.seconds(4), c2);
        move2.setByX(-120);
        move2.setAutoReverse(true);
        move2.setCycleCount(Animation.INDEFINITE);
        move2.play();

        // Animation 3: Pulse glow
        ScaleTransition pulse =
            new ScaleTransition(Duration.seconds(2), c3);
        pulse.setToX(1.5);
        pulse.setToY(1.5);
        pulse.setAutoReverse(true);
        pulse.setCycleCount(Animation.INDEFINITE);
        pulse.play();

        // Animation 4: Floating particle
        TranslateTransition move4 =
            new TranslateTransition(Duration.seconds(3.5), c4);
        move4.setByY(-90);
        move4.setAutoReverse(true);
        move4.setCycleCount(Animation.INDEFINITE);
        move4.play();

        // Login card entrance
        vb.setOpacity(0);
        vb.setTranslateY(35);

        FadeTransition fade =
            new FadeTransition(Duration.seconds(1.2), vb);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();

        TranslateTransition slide =
            new TranslateTransition(Duration.seconds(1.2), vb);
        slide.setFromY(35);
        slide.setToY(0);
        slide.play();

        // Login button hover animation
        Logi.setOnMouseEntered(e -> {
            ScaleTransition zoom =
                new ScaleTransition(Duration.millis(180), Logi);
            zoom.setToX(1.05);
            zoom.setToY(1.05);
            zoom.play();
        });

        Logi.setOnMouseExited(e -> {
            ScaleTransition reset =
                new ScaleTransition(Duration.millis(180), Logi);
            reset.setToX(1.0);
            reset.setToY(1.0);
            reset.play();
        });

        // Add layers
        root.getChildren().addAll(c1, c2, c3, c4, vb);

        // Responsive scene
        Scene sc = new Scene(root, 1280, 760);

        fromScene = sc;
        return sc;
    }
}

/*package com.jivmitra.View;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class AdminPage {
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

Button Logi=new Button("Login");
Logi.setOnAction(r->{

});


      Button Back=new Button("back");
        Back.setOnAction(e->{
            System.out.println("Back clicked");
            goToUser.run();
        });

        
        VBox vb=new VBox(10,emailTx,emailField,Pass,PassField,Logi,Back);
        vb.setPadding(new Insets(200));
        vb.setAlignment(Pos.CENTER);
        StackPane root = new StackPane();

        // Floating particles
        Circle c1 = new Circle(25, Color.web("#A8FFD0", 0.35));
        Circle c2 = new Circle(15, Color.web("#FFFFFF", 0.45));
        Circle c3 = new Circle(35, Color.web("#4DFFA8", 0.20));

        c1.setTranslateX(-300);
        c1.setTranslateY(-120);

        c2.setTranslateX(280);
        c2.setTranslateY(100);

        c3.setTranslateX(200);
        c3.setTranslateY(-180);

        // Animation 1: Particle moves up and down
        TranslateTransition move1 =
            new TranslateTransition(Duration.seconds(3), c1);
        move1.setByY(100);
        move1.setAutoReverse(true);
        move1.setCycleCount(Animation.INDEFINITE);
        move1.play();

        // Animation 2: Particle moves left and right
        TranslateTransition move2 =
            new TranslateTransition(Duration.seconds(4), c2);
        move2.setByX(-120);
        move2.setAutoReverse(true);
        move2.setCycleCount(Animation.INDEFINITE);
        move2.play();

        // Animation 3: Glow pulse
        ScaleTransition pulse =
            new ScaleTransition(Duration.seconds(2), c3);
        pulse.setToX(1.5);
        pulse.setToY(1.5);
        pulse.setAutoReverse(true);
        pulse.setCycleCount(Animation.INDEFINITE);
        pulse.play();

        // Login card entrance animation
        vb.setOpacity(0);
        vb.setTranslateY(35);

        FadeTransition fade =
            new FadeTransition(Duration.seconds(1.2), vb);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();

        TranslateTransition slide =
            new TranslateTransition(Duration.seconds(1.2), vb);
        slide.setFromY(35);
        slide.setToY(0);
        slide.play();

        // Add layers
        root.getChildren().addAll(c1, c2, c3, vb);
        Scene sc=new Scene(vb,900,500);
        return sc;

    }
    
}*/


