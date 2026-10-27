package com.techyuva.view.User;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.util.Arrays;

public class AppointmentPage {

    private static final String GREEN = "#087653";
    private static final String DARK_GREEN = "#064C38";
    private static final String MINT = "#D9F1E5";
    private static final String BACKGROUND = "#F2FAF6";
    private static final String BORDER = "#DCECE5";
    private static final String TEXT = "#153F36";

    public Scene getAppointmentScene(Runnable backToHome) {
        // Owner Details
        TextField ownerName = input("Enter full name");
        TextField contactNumber = input("Enter contact number");
        GridPane ownerGrid = grid();
        ownerGrid.add(field("Full Name *", ownerName), 0, 0, 3, 1);
        ownerGrid.add(field("Contact Number *", contactNumber), 0, 1, 3, 1);
        VBox ownerCard = section(1, "Owner Details", "Your contact information", ownerGrid);

        // Pet Details
        TextField petName = input("Enter your pet name");
        ComboBox<String> animalType = combo("Select Animal Type",
                "Dog", "Cat", "Bird", "Rabbit", "Other");
        TextField breed = input("Enter breed (optional)");
        TextField age = input("Enter age");
        ComboBox<String> ageUnit = combo("Years", "Months", "Years");

        GridPane petGrid = grid();
        petGrid.add(field("Pet Name *", petName), 0, 0);
        petGrid.add(field("Animal Type *", animalType), 1, 0);
        petGrid.add(field("Breed", breed), 0, 1);
        HBox ageBox = new HBox(8, age, ageUnit);
        HBox.setHgrow(age, Priority.ALWAYS);
        ageUnit.setPrefWidth(115);
        petGrid.add(field("Age *", ageBox), 1, 1);

        VBox petCard = section(2, "Pet Details", "Information about your pet", petGrid);

        // Appointment Details
        ComboBox<String> reason = combo("Select Reason", "General Checkup",
                "Vaccination", "Illness / Injury", "Dental Care",
                "Grooming", "Follow-up", "Other");
        ComboBox<String> veterinarian = combo("Select Veterinarian",
                "Dr. Priya Sharma", "Dr. Amit Patel", "Dr. Sneha Kulkarni");
        ComboBox<String> consultation = combo("Select Type",
                "In-clinic Visit", "Video Consultation", "Home Visit");

        GridPane appointmentGrid = grid();
        appointmentGrid.add(field("Reason for Visit *", reason), 0, 0);
        appointmentGrid.add(field("Veterinarian *", veterinarian), 1, 0);
        appointmentGrid.add(field("Consultation Type *", consultation), 2, 0);
        VBox appointmentCard = section(3, "Appointment Details",
                "Reason for visit and veterinarian selection", appointmentGrid);

        // Date and time
        DatePicker datePicker = new DatePicker(LocalDate.now().plusDays(1));
        datePicker.setPromptText("Select date");
        datePicker.setMaxWidth(Double.MAX_VALUE);
        datePicker.setPrefHeight(42);

        ComboBox<String> timeSlot = combo("Select time slot",
                "09:00 AM - 10:00 AM", "10:00 AM - 11:00 AM",
                "11:00 AM - 12:00 PM", "12:00 PM - 01:00 PM",
                "02:00 PM - 03:00 PM", "03:00 PM - 04:00 PM",
                "04:00 PM - 05:00 PM");

        GridPane dateGrid = grid();
        dateGrid.add(field("Appointment Date *", datePicker), 0, 0);
        dateGrid.add(field("Time Slot *", timeSlot), 1, 0);
        VBox dateCard = section(4, "Date & Time",
                "Choose your preferred date and time", dateGrid);

        // Additional notes
        TextArea notes = new TextArea();
        notes.setPromptText("Describe your pet's symptoms, behaviour or any specific concerns...");
        notes.setWrapText(true);
        notes.setPrefRowCount(2);
        notes.setStyle(inputStyle() + "-fx-padding:10px;");
        VBox notesContent = new VBox(8, label("Symptoms / Notes"), notes);
        VBox notesCard = section(5, "Additional Information",
                "Describe your pet's symptoms or any specific concerns", notesContent);

        Button clearButton = new Button("⟳   Clear Form");
        clearButton.setMaxWidth(Double.MAX_VALUE);
        clearButton.setPrefHeight(44);
        clearButton.setStyle("-fx-background-color:#EAF7F0;-fx-text-fill:" + DARK_GREEN
                + ";-fx-border-color:" + GREEN + ";-fx-border-radius:9px;"
                + "-fx-background-radius:9px;-fx-font-size:14px;-fx-font-weight:bold;"
                + "-fx-cursor:hand;");

        Button bookButton = new Button("▣   Book Appointment");
        bookButton.setMaxWidth(Double.MAX_VALUE);
        bookButton.setPrefHeight(44);
        bookButton.setStyle("-fx-background-color:" + GREEN + ";-fx-text-fill:white;"
                + "-fx-background-radius:9px;-fx-font-size:14px;-fx-font-weight:bold;"
                + "-fx-cursor:hand;");

        HBox actions = new HBox(12, clearButton, bookButton);
        HBox.setHgrow(clearButton, Priority.ALWAYS);
        HBox.setHgrow(bookButton, Priority.ALWAYS);
        notesContent.getChildren().add(actions);

        clearButton.setOnAction(e -> {
            ownerName.clear();
            contactNumber.clear();
            petName.clear();
            animalType.getSelectionModel().clearSelection();
            animalType.setPromptText("Select Animal Type");
            breed.clear();
            age.clear();
            ageUnit.getSelectionModel().select("Years");
            reason.getSelectionModel().clearSelection();
            reason.setPromptText("Select Reason");
            veterinarian.getSelectionModel().clearSelection();
            veterinarian.setPromptText("Select Veterinarian");
            consultation.getSelectionModel().clearSelection();
            consultation.setPromptText("Select Type");
            datePicker.setValue(LocalDate.now().plusDays(1));
            timeSlot.getSelectionModel().clearSelection();
            timeSlot.setPromptText("Select time slot");
            notes.clear();
        });

        bookButton.setOnAction(e -> {
            if (ownerName.getText().trim().isEmpty()
                    || contactNumber.getText().trim().isEmpty()
                    || petName.getText().trim().isEmpty()
                    || animalType.getValue() == null
                    || age.getText().trim().isEmpty()
                    || reason.getValue() == null
                    || veterinarian.getValue() == null
                    || consultation.getValue() == null
                    || datePicker.getValue() == null
                    || timeSlot.getValue() == null) {
                alert(Alert.AlertType.WARNING, "Missing Information",
                        "Please complete all required fields marked with *.");
                return;
            }

            String cleanContact = contactNumber.getText().trim().replaceAll("[\\s()+-]", "");
            if (!cleanContact.matches("[0-9]{10,13}")) {
                alert(Alert.AlertType.WARNING, "Invalid Contact Number",
                        "Please enter a valid contact number (10 to 13 digits).");
                return;
            }

            int petAge;
            try {
                petAge = Integer.parseInt(age.getText().trim());
                if (petAge < 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                alert(Alert.AlertType.WARNING, "Invalid Age",
                        "Please enter a valid positive number for your pet's age.");
                return;
            }

            if (datePicker.getValue().isBefore(LocalDate.now())) {
                alert(Alert.AlertType.WARNING, "Invalid Date",
                        "Please select today or a future date.");
                return;
            }

            String message = "Appointment details\n\n"
                    + "Owner: " + ownerName.getText().trim() + "\n"
                    + "Contact: " + contactNumber.getText().trim() + "\n"
                    + "Pet: " + petName.getText().trim() + "\n"
                    + "Animal: " + animalType.getValue() + "\n"
                    + "Breed: " + (breed.getText().trim().isEmpty() ? "Not specified" : breed.getText().trim()) + "\n"
                    + "Age: " + petAge + " " + ageUnit.getValue() + "\n"
                    + "Reason: " + reason.getValue() + "\n"
                    + "Veterinarian: " + veterinarian.getValue() + "\n"
                    + "Consultation: " + consultation.getValue() + "\n"
                    + "Date: " + datePicker.getValue() + "\n"
                    + "Time: " + timeSlot.getValue() + "\n\n"
                    + "This is a demo confirmation. The appointment has not been saved to a database.";

            alert(Alert.AlertType.INFORMATION, "Appointment Details", message);
        });

        // Main page title and back button
        Button back = new Button("←  Back");
        back.setPrefHeight(42);
        back.setStyle("-fx-background-color:white;-fx-text-fill:" + TEXT
                + ";-fx-border-color:" + BORDER + ";-fx-border-radius:10px;"
                + "-fx-background-radius:10px;-fx-font-size:14px;-fx-font-weight:bold;"
                + "-fx-cursor:hand;");
        back.setOnAction(e -> {
            if (backToHome != null) backToHome.run();
        });

        Text title = new Text("▣  Book Appointment");
        title.setFont(Font.font("System", FontWeight.BOLD, 28));
        title.setFill(Color.web("#073E38"));
        Label subtitle = new Label("Schedule a visit with our trusted veterinarians for your pet's health and care.");
        subtitle.setStyle("-fx-font-size:13px;-fx-text-fill:#648078;");

        VBox titleTexts = new VBox(3, title, subtitle);
        HBox header = new HBox(18, back, titleTexts);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(5, 0, 8, 0));

        HBox ownerPetRow = new HBox(12, ownerCard, petCard);
        HBox.setHgrow(ownerCard, Priority.ALWAYS);
        HBox.setHgrow(petCard, Priority.ALWAYS);
        ownerCard.setMaxWidth(Double.MAX_VALUE);
        petCard.setMaxWidth(Double.MAX_VALUE);
        ownerCard.prefWidthProperty().bind(ownerPetRow.widthProperty().subtract(12).divide(2));
        petCard.prefWidthProperty().bind(ownerPetRow.widthProperty().subtract(12).divide(2));

        HBox appointmentDateRow = new HBox(12, appointmentCard, dateCard);
        HBox.setHgrow(appointmentCard, Priority.ALWAYS);
        HBox.setHgrow(dateCard, Priority.ALWAYS);
        appointmentCard.setMaxWidth(Double.MAX_VALUE);
        dateCard.setMaxWidth(Double.MAX_VALUE);
        appointmentCard.prefWidthProperty().bind(appointmentDateRow.widthProperty().subtract(12).divide(2));
        dateCard.prefWidthProperty().bind(appointmentDateRow.widthProperty().subtract(12).divide(2));

        VBox formColumn = new VBox(12, header, ownerPetRow, appointmentDateRow, notesCard);
        formColumn.setPadding(new Insets(18));
        formColumn.setFillWidth(true);
        formColumn.setMinWidth(0);
        formColumn.setStyle("-fx-background-color:" + BACKGROUND + ";");
        HBox.setHgrow(formColumn, Priority.ALWAYS);

        VBox rightColumn = new VBox(14,
                promoCard(),
                veterinariansCard(),
                tipsCard());
        // Flexible width prevents the information panel from forcing horizontal overflow.
        rightColumn.setPrefWidth(290);
        rightColumn.setMinWidth(220);
        rightColumn.setMaxWidth(320);
        rightColumn.setPadding(new Insets(18, 18, 18, 0));
        rightColumn.setStyle("-fx-background-color:" + BACKGROUND + ";");

        HBox body = new HBox(0, formColumn, rightColumn);
        HBox.setHgrow(formColumn, Priority.ALWAYS);
        body.setFillHeight(true);
        body.setMinWidth(0);

        ScrollPane scroll = new ScrollPane(body);
        scroll.setFitToWidth(true);
        // Keep the page content at its natural height so the vertical scrollbar appears
        // whenever the appointment form is taller than the application window.
        scroll.setFitToHeight(false);
        scroll.setPannable(true);
        scroll.setMinWidth(0);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setStyle("-fx-background:" + BACKGROUND + ";-fx-background-color:" + BACKGROUND
                + ";-fx-padding:0;");

        VBox sidebar = Sidebar.create("Book Appointment");
        sidebar.setPrefWidth(255);
        sidebar.setMinWidth(200);
        sidebar.setMaxWidth(255);

        HBox root = new HBox(0, sidebar, scroll);
        HBox.setHgrow(scroll, Priority.ALWAYS);
        root.setFillHeight(true);
        root.setStyle("-fx-background-color:" + BACKGROUND + ";");

        return new Scene(root, 1300, 760);
    }

    private VBox section(int number, String title, String subtitle, javafx.scene.Node content) {
        HBox badge = new HBox(10, circleNumber(number), iconCircle(number), new VBox(3,
                text(title, 17, true, "#123F37"),
                text(subtitle, 12, false, "#73837E")));
        badge.setAlignment(Pos.CENTER_LEFT);
        VBox card = new VBox(13, badge, content);
        card.setPadding(new Insets(15));
        card.setStyle(cardStyle());
        card.setMaxWidth(Double.MAX_VALUE);
        return card;
    }

    private HBox circleNumber(int n) {
        Label number = new Label(String.valueOf(n));
        number.setTextFill(Color.WHITE);
        number.setFont(Font.font("System", FontWeight.BOLD, 15));
        StackPane circle = new StackPane(number);
        circle.setMinSize(42, 42);
        circle.setMaxSize(42, 42);
        circle.setStyle("-fx-background-color:" + GREEN + ";-fx-background-radius:50;");
        return new HBox(circle);
    }

    private StackPane iconCircle(int n) {
        String[] icons = {"●", "✚", "▦", "▤"};
        Label icon = new Label(icons[(n - 1) % icons.length]);
        icon.setStyle("-fx-text-fill:" + DARK_GREEN + ";-fx-font-size:19px;-fx-font-weight:bold;");
        StackPane circle = new StackPane(icon);
        circle.setMinSize(42, 42);
        circle.setMaxSize(42, 42);
        circle.setStyle("-fx-background-color:" + MINT + ";-fx-background-radius:50;");
        return circle;
    }

    private GridPane grid() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(12);
        grid.setMinWidth(0);
        for (int i = 0; i < 3; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(100.0 / 3);
            col.setHgrow(Priority.ALWAYS);
            col.setMinWidth(0);
            grid.getColumnConstraints().add(col);
        }
        return grid;
    }

    private VBox field(String name, javafx.scene.Node control) {
        VBox box = new VBox(6, label(name), control);
        if (control instanceof Region) {
            ((Region) control).setMaxWidth(Double.MAX_VALUE);
        }
        return box;
    }

    private Label label(String value) {
        Label label = new Label(value);
        label.setStyle("-fx-font-size:13px;-fx-font-weight:bold;-fx-text-fill:#354640;");
        return label;
    }

    private Text text(String value, double size, boolean bold, String color) {
        Text text = new Text(value);
        text.setFont(Font.font("System", bold ? FontWeight.BOLD : FontWeight.NORMAL, size));
        text.setFill(Color.web(color));
        return text;
    }

    private TextField input(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setPrefHeight(42);
        field.setStyle(inputStyle());
        return field;
    }

    private String inputStyle() {
        return "-fx-font-size:13px;-fx-background-color:white;"
                + "-fx-border-color:" + BORDER + ";-fx-border-radius:8px;"
                + "-fx-background-radius:8px;-fx-padding:9px;";
    }

    private <T> ComboBox<T> combo(String prompt, T... options) {
        ComboBox<T> combo = new ComboBox<>();
        combo.getItems().addAll(Arrays.asList(options));
        combo.setPromptText(prompt);
        combo.setPrefHeight(42);
        combo.setMaxWidth(Double.MAX_VALUE);
        combo.setStyle("-fx-font-size:13px;-fx-background-color:white;"
                + "-fx-border-color:" + BORDER + ";-fx-border-radius:8px;"
                + "-fx-background-radius:8px;");
        return combo;
    }

    private VBox promoCard() {
        Label paw = new Label("🐾");
        paw.setStyle("-fx-font-size:28px;");
        Text heading = text("Caring Today\nfor a Healthier\nTomorrow", 21, true, DARK_GREEN);
        Label desc = new Label("Schedule a veterinary appointment and keep your pet happy and healthy.");
        desc.setWrapText(true);
        desc.setStyle("-fx-font-size:12px;-fx-text-fill:#42685C;");
        VBox card = new VBox(10, paw, heading, desc);
        card.setPadding(new Insets(20));
        card.setMinHeight(205);
        card.setStyle("-fx-background-color:linear-gradient(to bottom right,#E4F4C8,#C7E8D9);"
                + "-fx-background-radius:16px;-fx-border-color:#D4E9D9;-fx-border-radius:16px;");
        return card;
    }

    private VBox veterinariansCard() {
        VBox card = new VBox(13);
        card.setPadding(new Insets(16));
        card.setStyle(cardStyle());

        HBox heading = new HBox(text("Available Veterinarians", 16, true, DARK_GREEN),
                new Region());
        HBox.setHgrow(heading.getChildren().get(1), Priority.ALWAYS);
        card.getChildren().add(heading);

        card.getChildren().add(vetRow("👩🏻‍⚕️", "Dr. Priya Sharma", "General Veterinary Care", "4.8  |  Available Today"));
        card.getChildren().add(vetRow("👨🏻‍⚕️", "Dr. Amit Patel", "Surgery & Emergency", "4.6  |  Available Today"));
        card.getChildren().add(vetRow("👩🏻‍⚕️", "Dr. Sneha Kulkarni", "Pet Nutrition & Wellness", "4.9  |  Available Tomorrow"));
        return card;
    }

    private HBox vetRow(String avatar, String name, String specialty, String availability) {
        Label photo = new Label(avatar);
        photo.setStyle("-fx-font-size:26px;-fx-background-color:#EAF5EF;-fx-background-radius:30px;");
        photo.setMinSize(48, 48);
        photo.setAlignment(Pos.CENTER);

        VBox details = new VBox(4,
                text(name, 13, true, "#183E37"),
                text(specialty, 11, false, "#697B75"),
                text("★ " + availability, 11, false, "#087653"));
        HBox row = new HBox(10, photo, details);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private VBox tipsCard() {
        VBox card = new VBox(10);
        card.setPadding(new Insets(16));
        card.setStyle("-fx-background-color:#EDF8F1;-fx-background-radius:16px;"
                + "-fx-border-color:#D9EBE0;-fx-border-radius:16px;");
        card.getChildren().add(text("☼  Important Tips", 16, true, DARK_GREEN));
        for (String tip : new String[]{
                "✓  Book at least 1 day in advance",
                "✓  Bring previous medical records",
                "✓  Keep your pet calm during visit",
                "✓  Arrive 10 minutes early",
                "✓  Bring a leash or carrier for safety"}) {
            Label item = new Label(tip);
            item.setWrapText(true);
            item.setStyle("-fx-font-size:12px;-fx-text-fill:#526C63;");
            card.getChildren().add(item);
        }
        return card;
    }

    private String cardStyle() {
        return "-fx-background-color:white;-fx-background-radius:15px;"
                + "-fx-border-color:" + BORDER + ";-fx-border-radius:15px;";
    }

    private void alert(Alert.AlertType type, String heading, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("JeevMitra - Book Appointment");
        alert.setHeaderText(heading);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
