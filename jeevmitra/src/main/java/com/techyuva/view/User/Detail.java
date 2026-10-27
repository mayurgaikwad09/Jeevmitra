package com.techyuva.view.User;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class Detail {

    public Scene getDetailScene(Runnable backtoHome) {

        Button back = new Button("← Back");
        back.setStyle("-fx-background-color:white;-fx-text-fill:#1e6b4f;-fx-font-size:13px;-fx-border-color:#1e6b4f;-fx-border-radius:8px;-fx-background-radius:8px;-fx-padding:6px 16px;-fx-cursor:hand;");
        back.setOnAction(e -> {
            System.out.println("detail back button clicked");
            backtoHome.run();
        });

        Text t1 = new Text("Appointment Details");
        t1.setStyle("-fx-font-size:28px;-fx-font-weight:bold;-fx-fill:#145a41;");

        // DATABASE (pude ithe): SELECT * FROM appointments WHERE id = ?
        Text t2 = new Text("Veterinarian : Dr. Sneha Patil");
        t2.setStyle("-fx-font-size:16px;-fx-fill:#245B4B;");

        Text t3 = new Text("Date : 25 Oct 2025");
        t3.setStyle("-fx-font-size:16px;-fx-fill:#245B4B;");

        Text t4 = new Text("Time : 10:00 AM");
        t4.setStyle("-fx-font-size:16px;-fx-fill:#245B4B;");

        Text t5 = new Text("Status : Upcoming");
        t5.setStyle("-fx-font-size:16px;-fx-fill:#245B4B;");

        VBox vb = new VBox(12, back, t1, t2, t3, t4, t5);
        vb.setPadding(new Insets(40));
        vb.setStyle("-fx-background-color:#F7FCFA;");

        // sidebar ekda banvla ahe (Sidebar.java), ithe fakt lavaycha
        HBox root = new HBox(0, Sidebar.create("Dashboard"), vb);
        HBox.setHgrow(vb, Priority.ALWAYS);

        return new Scene(root, 1300, 760);
    }
}
