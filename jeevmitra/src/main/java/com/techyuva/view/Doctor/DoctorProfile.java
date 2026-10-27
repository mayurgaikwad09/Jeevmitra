
package com.techyuva.view.Doctor;

import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class DoctorProfile {

    private TextField name, qualification, specialization, experience;
    private TextField clinic, city, email, phone;
    private Label status;
    private Button edit, save;

    public Scene createScene(Stage stage, Runnable backAction) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:#F2F7F3;");

        Label logo = new Label("🐾 JeevMitra");
        logo.setTextFill(Color.WHITE);
        logo.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 25));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label tag = new Label("DOCTOR PROFILE");
        tag.setTextFill(Color.WHITE);
        tag.setStyle("-fx-background-color:#286B50;"
                + "-fx-background-radius:20;-fx-padding:10 16;");

        HBox header = new HBox(15, logo, spacer, tag);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(20, 32, 20, 32));
        header.setStyle("-fx-background-color:linear-gradient(to right,"
                + "#103F30,#21845C);");
        root.setTop(header);

        Button back = new Button("← Back to Dashboard");
        styleLight(back);
        back.setOnAction(e -> backAction.run());

        Label avatar = new Label("👨‍⚕️");
        avatar.setFont(Font.font(42));
        avatar.setMinSize(100, 100);
        avatar.setAlignment(Pos.CENTER);
        avatar.setStyle("-fx-background-color:#DDF2E4;"
                + "-fx-background-radius:28;-fx-border-color:#B9DDC6;"
                + "-fx-border-radius:28;");

        Label title = new Label("Professional Profile");
        title.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 28));
        title.setTextFill(Color.web("#123D30"));

        Label subtitle = new Label(
                "Keep your professional and clinic details up to date.");
        subtitle.setWrapText(true);
        subtitle.setTextFill(Color.web("#687B70"));

        Label verified = new Label("● VETERINARY DOCTOR");
        verified.setTextFill(Color.web("#176B4D"));
        verified.setStyle("-fx-background-color:#E0F3E6;"
                + "-fx-background-radius:20;-fx-padding:8 12;");

        VBox heroText = new VBox(10, title, subtitle, verified);
        HBox hero = new HBox(25, avatar, heroText);
        hero.setAlignment(Pos.CENTER_LEFT);
        hero.setPadding(new Insets(26));
        hero.setStyle("-fx-background-color:linear-gradient(to right,"
                + "#FFFFFF,#E5F5E9);-fx-background-radius:22;"
                + "-fx-border-color:#D6E8DB;-fx-border-radius:22;");
        hero.setEffect(new DropShadow(12, Color.rgb(18, 61, 48, .08)));

        Label formTitle = new Label("Personal & Professional Details");
        formTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        formTitle.setTextFill(Color.web("#123D30"));

        GridPane form = new GridPane();
        form.setHgap(22);
        form.setVgap(18);

        name = field("Enter full name");
        qualification = field("e.g. BVSc & AH");
        specialization = field("e.g. Small Animal Care");
        experience = field("e.g. 3 years");
        clinic = field("Clinic / Hospital");
        city = field("Clinic city");
        email = field("Email address");
        phone = field("Phone number");

        add(form, "FULL NAME", name, 0, 0);
        add(form, "QUALIFICATION", qualification, 1, 0);
        add(form, "SPECIALIZATION", specialization, 0, 1);
        add(form, "EXPERIENCE", experience, 1, 1);
        add(form, "CLINIC / HOSPITAL", clinic, 0, 2);
        add(form, "CLINIC CITY", city, 1, 2);
        add(form, "EMAIL ADDRESS", email, 0, 3);
        add(form, "PHONE NUMBER", phone, 1, 3);

        for (int i = 0; i < 2; i++) {
            ColumnConstraints c = new ColumnConstraints();
            c.setPercentWidth(50);
            c.setHgrow(Priority.ALWAYS);
            form.getColumnConstraints().add(c);
        }

        VBox formCard = new VBox(24, formTitle, form);
        formCard.setPadding(new Insets(28));
        formCard.setStyle("-fx-background-color:white;"
                + "-fx-background-radius:20;-fx-border-color:#DCEAE0;"
                + "-fx-border-radius:20;");
        formCard.setEffect(new DropShadow(10, Color.rgb(18, 61, 48, .06)));

        status = new Label("Your profile is ready to view.");
        status.setWrapText(true);
        status.setTextFill(Color.web("#60776A"));

        edit = new Button("✎ Edit Profile");
        styleLight(edit);

        save = new Button("✓ Save Changes");
        save.setTextFill(Color.WHITE);
        save.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        save.setStyle("-fx-background-color:#176B4D;"
                + "-fx-background-radius:10;-fx-padding:12 20;");
        save.setDisable(true);

        setEditable(false);

        edit.setOnAction(e -> {
            setEditable(true);
            edit.setDisable(true);
            save.setDisable(false);
            status.setText("Edit your details, then save changes.");
            name.requestFocus();
        });

        save.setOnAction(e -> {
            if (name.getText().trim().isEmpty()) {
                status.setText("Please enter the doctor's name.");
                name.requestFocus();
                return;
            }
            setEditable(false);
            edit.setDisable(false);
            save.setDisable(true);
            status.setText("Changes saved for this session.");
        });

        Region gap = new Region();
        HBox.setHgrow(gap, Priority.ALWAYS);
        HBox actions = new HBox(12, status, gap, edit, save);
        actions.setAlignment(Pos.CENTER_LEFT);

        Label footer = new Label(
                "JeevMitra • Compassionate care for every life 🐾");
        footer.setTextFill(Color.web("#829187"));
        footer.setMaxWidth(Double.MAX_VALUE);
        footer.setAlignment(Pos.CENTER);

        VBox content = new VBox(23, back, hero, formCard, actions, footer);
        content.setPadding(new Insets(28, 38, 30, 38));

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background:#F2F7F3;"
                + "-fx-background-color:#F2F7F3;"
                + "-fx-border-color:transparent;");
        root.setCenter(scroll);

        return new Scene(root);
    }

    private TextField field(String prompt) {
        TextField f = new TextField();
        f.setPromptText(prompt);
        f.setPrefHeight(44);
        f.setMaxWidth(Double.MAX_VALUE);
        f.setEditable(false);
        f.setStyle("-fx-background-color:#F6F9F6;"
                + "-fx-border-color:#DCE8DE;-fx-border-radius:10;"
                + "-fx-background-radius:10;-fx-padding:0 13;");
        return f;
    }

    private void add(GridPane grid, String title, TextField field,
                     int col, int row) {
        Label label = new Label(title);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 10));
        label.setTextFill(Color.web("#526C5C"));
        VBox box = new VBox(8, label, field);
        box.setMaxWidth(Double.MAX_VALUE);
        grid.add(box, col, row);
        GridPane.setHgrow(box, Priority.ALWAYS);
    }

    private void setEditable(boolean value) {
        TextField[] fields = {name, qualification, specialization,
                experience, clinic, city, email, phone};

        for (TextField f : fields) {
            if (f != null) {
                f.setEditable(value);
                f.setStyle("-fx-background-color:"
                        + (value ? "white" : "#F6F9F6") + ";"
                        + "-fx-border-color:"
                        + (value ? "#75B994" : "#DCE8DE") + ";"
                        + "-fx-border-radius:10;-fx-background-radius:10;"
                        + "-fx-padding:0 13;");
            }
        }
    }

    private void styleLight(Button button) {
        button.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        button.setTextFill(Color.web("#123D30"));
        button.setStyle("-fx-background-color:white;"
                + "-fx-border-color:#C8DDD0;-fx-border-radius:10;"
                + "-fx-background-radius:10;-fx-padding:11 17;"
                + "-fx-cursor:hand;");
    }
}
