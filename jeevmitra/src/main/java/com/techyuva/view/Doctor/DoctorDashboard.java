package com.techyuva.view.Doctor;

import java.io.File;
import java.util.function.Consumer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class DoctorDashboard {

    private Node image(String path, double width, double height) {
        try {
            File file = new File(path);

            if (!file.exists()) {
                return new Region();
            }

            Image img = new Image(file.toURI().toString());

            ImageView view = new ImageView(img);
            view.setFitWidth(width);
            view.setFitHeight(height);
            view.setPreserveRatio(true);

            return view;

        } catch (Exception e) {
            return new Region();
        }
    }

    private Node petImage(String path, double size) {
        ImageView view = new ImageView();

        try {
            File file = new File(path);

            if (file.exists()) {
                view.setImage(new Image(file.toURI().toString()));
            }
        } catch (Exception e) {
            System.out.println("Pet image not found: " + path);
        }

        view.setFitWidth(size);
        view.setFitHeight(size);
        view.setPreserveRatio(false);
        view.setClip(new Circle(size / 2, size / 2, size / 2));

        return view;
    }

    private Text text(
            String value, int size, boolean bold, String color) {

        Text t = new Text(value);
        t.setStyle(
            "-fx-font-size:" + size + "px;" +
            (bold ? "-fx-font-weight:bold;" : "")
        );
        t.setFill(Color.web(color));

        return t;
    }

    private Button actionButton(String label, Runnable action) {
        Button button = new Button(label);

        String normal =
            "-fx-background-color:#08765F;" +
            "-fx-text-fill:white;" +
            "-fx-font-size:13px;" +
            "-fx-font-weight:bold;" +
            "-fx-padding:10px 14px;" +
            "-fx-background-radius:9px;" +
            "-fx-cursor:hand;";

        String hover =
            "-fx-background-color:#079875;" +
            "-fx-text-fill:white;" +
            "-fx-font-size:13px;" +
            "-fx-font-weight:bold;" +
            "-fx-padding:10px 14px;" +
            "-fx-background-radius:9px;" +
            "-fx-cursor:hand;" +
            "-fx-effect:dropshadow(gaussian,rgba(0,77,64,0.35),14,0.3,0,4);";

        button.setStyle(normal);
        button.setOnMouseEntered(e -> button.setStyle(hover));
        button.setOnMouseExited(e -> button.setStyle(normal));
        button.setOnAction(e -> action.run());

        return button;
    }

    // HOVER EFFECT FOR CARDS

    private void addCardHover(Region card) {
        DropShadow normalShadow = new DropShadow(
            10, 0, 3, Color.rgb(0, 77, 64, 0.10)
        );

        DropShadow hoverShadow = new DropShadow(
            19, 0, 6, Color.rgb(0, 77, 64, 0.24)
        );

        card.setEffect(normalShadow);

        card.setOnMouseEntered(e -> {
            card.setEffect(hoverShadow);
            card.setTranslateY(-2);
        });

        card.setOnMouseExited(e -> {
            card.setEffect(normalShadow);
            card.setTranslateY(0);
        });
    }

    private HBox infoCard(
            String title, String value, String imagePath) {

        VBox words = new VBox(
            8,
            text(title, 14, true, "#187A62"),
            text(value, 27, true, "#123D30")
        );

        HBox card = new HBox(
            14,
            image(imagePath, 48, 48),
            words
        );

        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(18));
        card.setMinWidth(150);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle(
            "-fx-background-color:rgba(255,255,255,0.94);" +
            "-fx-border-color:#CDE7DB;" +
            "-fx-border-radius:15px;" +
            "-fx-background-radius:15px;"
        );

        HBox.setHgrow(card, Priority.ALWAYS);
        addCardHover(card);

        return card;
    }

    private VBox sectionCard(String title, Node content) {
        VBox box = new VBox(
            15,
            text(title, 19, true, "#004D40"),
            content
        );

        box.setPadding(new Insets(20));
        box.setMinHeight(145);
        box.setMaxWidth(Double.MAX_VALUE);

        box.setStyle(
            "-fx-background-color:rgba(255,255,255,0.94);" +
            "-fx-border-color:#CDE7DB;" +
            "-fx-border-radius:16px;" +
            "-fx-background-radius:16px;"
        );

        VBox.setVgrow(box, Priority.ALWAYS);
        addCardHover(box);

        return box;
    }

    private HBox appointmentRow(
            String petName,
            String type,
            String service,
            String time,
            String petPhoto) {

        VBox details = new VBox(
            6,
            text(petName + " (" + type + ")", 15, true, "#164E41"),
            text(service, 13, false, "#718A82")
        );

        VBox timeDetails = new VBox(
            6,
            text(time, 13, false, "#52796F"),
            text(
                service.equals("Vaccination")
                    ? "Vaccination"
                    : service.equals("Painful")
                        ? "Follow-up"
                        : "Checkup",
                12, true, "#08765F"
            )
        );

        timeDetails.setAlignment(Pos.CENTER_RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox row = new HBox(
            14,
            petImage(petPhoto, 58),
            details,
            spacer,
            timeDetails
        );

        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(10, 0, 10, 0));

        return row;
    }

    private HBox recordRow(
            String petName,
            String type,
            String service,
            String date,
            String petPhoto) {

        VBox details = new VBox(
            5,
            text(petName + " (" + type + ")", 15, true, "#164E41"),
            text(service, 13, false, "#718A82")
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox row = new HBox(
            13,
            petImage(petPhoto, 48),
            details,
            spacer,
            text(date, 12, false, "#52796F")
        );

        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(8, 0, 8, 0));

        return row;
    }

    private void addDivider(VBox box) {
        Region line = new Region();
        line.setPrefHeight(1);
        line.setMaxHeight(1);
        line.setStyle("-fx-background-color:#DCECE5;");
        box.getChildren().add(line);
    }

    public Node createView(
            Runnable goHome,
            Consumer<String> openPage) {

        Text title = text(
            "Veterinarian Dashboard",
            30, true, "#064E3B"
        );

        Text subtitle = text(
            "Welcome back, Dr. Sneha Patil!",
            16, false, "#52796F"
        );

        HBox heading = new HBox(
            15,
            image("Assests/medical records paw green.jpg", 55, 55),
            new VBox(7, title, subtitle)
        );

        heading.setAlignment(Pos.CENTER_LEFT);

        // STATISTICS

        HBox stats = new HBox(
            14,
            infoCard(
                "Today's Appointments",
                "12",
                "Assests/heartline.jpg"
            ),
            infoCard(
                "Patients",
                "245",
                "Assests/userrr.png"
            ),
            infoCard(
                "Medical Records",
                "320",
                "Assests/medical records paw green.jpg"
            ),
            infoCard(
                "Pending Reports",
                "5",
                "Assests/pending reports green.jpg"
            )
        );

        // UPCOMING APPOINTMENTS

        VBox appointments = new VBox(5);

        appointments.getChildren().addAll(
            appointmentRow(
                "Bella", "Dog", "Vaccination", "10:00 AM",
                "Assests/bella.jpg"
            )
        );
        addDivider(appointments);

        appointments.getChildren().add(
            appointmentRow(
                "Mimi", "Cat", "Fever", "11:30 AM",
                "Assests/mimi.jpg"
            )
        );
        addDivider(appointments);

        appointments.getChildren().add(
            appointmentRow(
                "Rocky", "Dog", "Painful", "02:00 PM",
                "Assests/rocky.jpg"
            )
        );

        Button viewAppointments = actionButton(
            "View All Appointments  →",
            () -> openPage.accept("All Appointments")
        );

        HBox appointmentActions = new HBox(viewAppointments);
        appointmentActions.setAlignment(Pos.CENTER_RIGHT);
        appointments.getChildren().add(appointmentActions);

        // RECENT MEDICAL RECORDS

        VBox records = new VBox(5);

        records.getChildren().add(
            recordRow(
                "Bella", "Dog", "Vaccination", "12 Oct 2026",
                "Assests/bella.jpg"
            )
        );
        addDivider(records);

        records.getChildren().add(
            recordRow(
                "Max", "Cat", "Treatment", "10 Oct 2026",
                "Assests/max.jpg"
            )
        );
        addDivider(records);

        records.getChildren().add(
            recordRow(
                "Luna", "Cat", "Checkup", "08 Oct 2026",
                "Assests/luna.jpg"
            )
        );

        records.getChildren().add(
            actionButton(
                "View All Records  →",
                () -> openPage.accept("All Medical Records")
            )
        );

        // VIDEO CONSULTATION

        VBox videoDetails = new VBox(
            6,
            text("Buddy (Dog)", 15, true, "#164E41"),
            text("Owner: Priya Sharma", 13, false, "#718A82")
        );

        VBox videoTime = new VBox(
            6,
            text("10:30 AM", 14, true, "#164E41"),
            text("Upcoming", 13, false, "#08765F")
        );

        HBox videoInfo = new HBox(
            12,
            petImage("Assests/bella.jpg", 55),
            videoDetails,
            new Region(),
            videoTime
        );

        videoInfo.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(videoInfo.getChildren().get(2), Priority.ALWAYS);

        Button startCall = actionButton(
            "Start Video Call",
            () -> openPage.accept("Start Video Call")
        );

        Button scheduleCall = actionButton(
            "Schedule Call",
            () -> openPage.accept("Schedule Video Call")
        );

        HBox callButtons = new HBox(10, startCall, scheduleCall);
        callButtons.setAlignment(Pos.CENTER_LEFT);

        VBox video = new VBox(
            13,
            text("Consult with pet owners remotely.", 13, false, "#718A82"),
            videoInfo,
            callButtons
        );

        // TWO-COLUMN CONTENT

        VBox leftColumn = sectionCard(
            "Upcoming Appointments",
            appointments
        );

        VBox recordsCard = sectionCard(
            "Recent Medical Records",
            records
        );

        VBox videoCard = sectionCard(
            "Video Consultation",
            video
        );

        VBox rightColumn = new VBox(
            18,
            recordsCard,
            videoCard
        );

        HBox lower = new HBox(
            18,
            leftColumn,
            rightColumn
        );

        lower.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(leftColumn, Priority.ALWAYS);
        HBox.setHgrow(rightColumn, Priority.ALWAYS);

        leftColumn.setPrefWidth(550);
        rightColumn.setPrefWidth(500);

        // MAIN CONTENT

        VBox content = new VBox(
            24,
            heading,
            stats,
            lower
        );

        content.setPadding(new Insets(30));
        content.setFillWidth(true);
        content.setMaxWidth(Double.MAX_VALUE);

        // BACKGROUND IMAGE WITH FALLBACK

        File bgFile = new File("Assests/dashboard-bg.jpg");

        if (bgFile.exists()) {
            try {
                Image bgImage = new Image(bgFile.toURI().toString());

                BackgroundImage backgroundImage = new BackgroundImage(
                    bgImage,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(
                        100, 100, true, true, false, true
                    )
                );

                content.setBackground(new Background(backgroundImage));

            } catch (Exception e) {
                content.setStyle(
                    "-fx-background-color:linear-gradient(to bottom right,#F5FBF8,#E8F5EE);"
                );
            }
        } else {
            content.setStyle(
                "-fx-background-color:linear-gradient(to bottom right,#F5FBF8,#E8F5EE);"
            );
        }

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setStyle(
            "-fx-background-color:transparent;" +
            "-fx-background:transparent;"
        );

        return scroll;
    }
}                                                               