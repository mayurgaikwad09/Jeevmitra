package com.techyuva.view.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class LoginPage {

    // login yashasvi zala ki onLoginSuccess.run() chalto (body Home.java madhe ahe)
    public Scene getLoginScene(Runnable onLoginSuccess) {

        Text title = new Text("JeevMitra Login");
        title.setStyle("-fx-font-size:30px;-fx-font-weight:bold;-fx-fill:#145a41;");

        // ---------- USERNAME ----------
        Text lblUser = new Text("Username");
        lblUser.setStyle("-fx-font-size:15px;-fx-fill:#245B4B;");
        TextField inUser = new TextField();
        inUser.setPromptText("Enter your username");
        inUser.setMaxWidth(320);

        // ---------- PASSWORD ----------
        Text lblPass = new Text("Password");
        lblPass.setStyle("-fx-font-size:15px;-fx-fill:#245B4B;");
        PasswordField inPass = new PasswordField();
        inPass.setPromptText("Enter your password");
        inPass.setMaxWidth(320);

        Text status = new Text();
        status.setStyle("-fx-font-size:15px;");

        // ---------- LOGIN BUTTON ----------
        Button login = new Button("Login");
        login.setStyle("-fx-background-color:#1e6b4f;-fx-text-fill:white;-fx-font-size:14px;-fx-font-weight:bold;-fx-background-radius:9px;-fx-padding:10px 22px;-fx-cursor:hand;");

        login.setOnAction(e -> {
            String vUser = inUser.getText().trim();
            String vPass = inPass.getText();

            if (vUser.isEmpty() || vPass.isEmpty()) {
                status.setStyle("-fx-font-size:15px;-fx-fill:red;");
                status.setText("Enter username and password");
                return;
            }

            // DATABASE (pude ithe) -------------------------------
            // SELECT * FROM users WHERE username = ? AND password = ?
            // sapadla tr khali currentUser set kara, nahi tr error dakhva
            System.out.println("login -> " + vUser);

            Home.currentUser = vUser;   // dashboard var "Hello, <naav>" yeil
            onLoginSuccess.run();
        });

        VBox vb = new VBox(12, title, lblUser, inUser, lblPass, inPass, login, status);
        vb.setAlignment(Pos.CENTER);
        vb.setPadding(new Insets(40));
        vb.setStyle("-fx-background-color:#F7FCFA;");

        return new Scene(vb, 1300, 760);
    }
}
