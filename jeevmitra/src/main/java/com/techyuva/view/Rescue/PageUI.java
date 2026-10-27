package com.techyuva.view.Rescue;




    

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class PageUI {

    public static VBox createPage(String title) {
        VBox page = new VBox(18);
        page.setPadding(new Insets(24));
        page.setStyle("-fx-background-color:#F1F8F5;");

        Label heading = new Label("🐾  " + title);
        heading.setFont(Font.font("Arial", 27));
        heading.setTextFill(Color.web("#064E3B"));

        page.getChildren().add(heading);
        return page;
    }

    public static VBox card(String title) {
        VBox box = new VBox(14);
        box.setPadding(new Insets(20));
        box.setStyle(
            "-fx-background-color:white;" +
            "-fx-background-radius:14px;" +
            "-fx-border-color:#DCEAE5;" +
            "-fx-border-radius:14px;"
        );

        Label heading = new Label(title);
        heading.setFont(Font.font("Arial", 18));
        heading.setTextFill(Color.web("#176B50"));

        box.getChildren().add(heading);
        return box;
    }

    public static Label label(String text) {
        Label label = new Label(text);
        label.setStyle(
            "-fx-font-size:13px;" +
            "-fx-font-weight:bold;" +
            "-fx-text-fill:#245B4B;"
        );
        return label;
    }

    public static Button button(String text) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setStyle(
            "-fx-background-color:#21865A;" +
            "-fx-text-fill:white;" +
            "-fx-font-size:13px;" +
            "-fx-font-weight:bold;" +
            "-fx-background-radius:8px;" +
            "-fx-padding:11px;" +
            "-fx-cursor:hand;"
        );
        return button;
    }

    public static void alert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("JeevMitra");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
