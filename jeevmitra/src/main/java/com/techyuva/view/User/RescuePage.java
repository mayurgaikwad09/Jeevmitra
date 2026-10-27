package com.techyuva.view.User;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.Arrays;

public class RescuePage {

    private static final String GREEN = "#176B4D";
    private static final String DARK_GREEN = "#0B3D2C";
    private static final String MINT = "#E7F6EE";
    private static final String BG = "#F5FAF7";
    private static final String BORDER = "#D9E9E0";
    private static final String TEXT = "#21483A";

    public Scene getRescueScene(Runnable backToHome) {

        // ---------- Form controls ----------
        TextField reporterName = input("Enter your full name");
        TextField contact = input("10-digit mobile number");

        ComboBox<String> animalType = combo("Select animal",
                "Dog", "Cat", "Cow", "Bird", "Goat", "Horse", "Other");
        ComboBox<String> injuryType = combo("Select injury / situation",
                "Bleeding / Open Wound", "Leg Injury", "Road Accident",
                "Unable to Walk", "Trapped Animal", "Sick / Weak", "Abandoned", "Other");
        ComboBox<String> urgency = combo("Select urgency",
                "Critical - Immediate Danger", "High", "Medium", "Low");

        TextField location = input("Enter area, landmark or full address");
        TextArea description = new TextArea();
        description.setPromptText("Describe what happened, visible injuries, and the animal's condition...");
        description.setWrapText(true);
        description.setPrefRowCount(4);
        description.setMinHeight(95);
        description.setStyle(inputStyle());

        Label photoStatus = new Label("No photo selected");
        photoStatus.setStyle("-fx-text-fill:#6D8278;-fx-font-size:12px;");
        ImageView photoPreview = new ImageView();
        photoPreview.setFitWidth(180);
        photoPreview.setFitHeight(130);
        photoPreview.setPreserveRatio(true);
        StackPane preview = new StackPane(photoPreview,
                new Label("🐾\nUpload animal photo"));
        preview.setAlignment(Pos.CENTER);
        preview.setMinHeight(145);
        preview.setStyle("-fx-background-color:#F7FCF9;-fx-border-color:#B9D5C6;"
                + "-fx-border-style:dashed;-fx-border-radius:12px;-fx-background-radius:12px;");

        final File[] selectedPhoto = new File[1];
        Button upload = new Button("＋ Upload Photo");
        upload.setStyle(buttonStyle(false));
        upload.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Choose animal photo");
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                    "Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.webp"));
            File file = chooser.showOpenDialog(null);
            if (file != null) {
                Image image = new Image(file.toURI().toString(), 180, 130, true, true);
                if (image.isError()) {
                    showAlert(Alert.AlertType.ERROR, "Invalid image",
                            "This image could not be opened. Please select another photo.");
                    return;
                }
                selectedPhoto[0] = file;
                photoPreview.setImage(image);
                preview.getChildren().setAll(photoPreview);
                photoStatus.setText(file.getName());
            }
        });

        // ---------- Form cards ----------
        GridPane reporterGrid = grid(2);
        reporterGrid.add(field("Reporter Name *", reporterName), 0, 0);
        reporterGrid.add(field("Contact Number *", contact), 1, 0);
        VBox reporterCard = section("01", "Your Details",
                "How can the rescue team contact you?", reporterGrid);

        GridPane animalGrid = grid(2);
        animalGrid.add(field("Animal Type *", animalType), 0, 0);
        animalGrid.add(field("Injury / Situation *", injuryType), 1, 0);
        animalGrid.add(field("Urgency Level *", urgency), 0, 1);
        animalGrid.add(field("Animal Location *", location), 1, 1);
        VBox animalCard = section("02", "Animal & Incident",
                "Tell us about the animal and the situation", animalGrid);

        VBox photoBox = new VBox(10, preview, upload, photoStatus);
        photoBox.setAlignment(Pos.CENTER);
        VBox descriptionBox = new VBox(8,
                label("Incident Description *"),
                description,
                label("Please share clear details to help responders prepare."));
        VBox detailContent = new VBox(14, photoBox, descriptionBox);
        HBox incidentRow = new HBox(16, photoBox, descriptionBox);
        incidentRow.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(photoBox, Priority.NEVER);
        HBox.setHgrow(descriptionBox, Priority.ALWAYS);
        photoBox.setPrefWidth(210);
        photoBox.setMinWidth(180);
        descriptionBox.setMinWidth(0);
        VBox incidentCard = section("03", "Photo & Description",
                "A photo and clear description can help the rescue team", incidentRow);

        Button clear = new Button("Clear Form");
        clear.setStyle(buttonStyle(false));
        Button submit = new Button("🚨 Submit Rescue Report");
        submit.setStyle(buttonStyle(true));
        clear.setMaxWidth(Double.MAX_VALUE);
        submit.setMaxWidth(Double.MAX_VALUE);
        clear.setPrefHeight(46);
        submit.setPrefHeight(46);
        HBox actions = new HBox(12, clear, submit);
        HBox.setHgrow(clear, Priority.ALWAYS);
        HBox.setHgrow(submit, Priority.ALWAYS);

        clear.setOnAction(e -> {
            reporterName.clear();
            contact.clear();
            animalType.getSelectionModel().clearSelection();
            animalType.setPromptText("Select animal");
            injuryType.getSelectionModel().clearSelection();
            injuryType.setPromptText("Select injury / situation");
            urgency.getSelectionModel().clearSelection();
            urgency.setPromptText("Select urgency");
            location.clear();
            description.clear();
            selectedPhoto[0] = null;
            photoPreview.setImage(null);
            preview.getChildren().setAll(new Label("🐾\nUpload animal photo"));
            photoStatus.setText("No photo selected");
        });

        submit.setOnAction(e -> {
            if (reporterName.getText().trim().isEmpty()
                    || contact.getText().trim().isEmpty()
                    || animalType.getValue() == null
                    || injuryType.getValue() == null
                    || urgency.getValue() == null
                    || location.getText().trim().isEmpty()
                    || description.getText().trim().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Missing information",
                        "Please complete all required fields marked with *.");
                return;
            }
            String cleanContact = contact.getText().trim().replaceAll("[\\s()+-]", "");
            if (!cleanContact.matches("[0-9]{10,13}")) {
                showAlert(Alert.AlertType.WARNING, "Invalid contact number",
                        "Please enter a valid contact number (10 to 13 digits).");
                return;
            }

            String message = "Rescue report details\n\n"
                    + "Reporter: " + reporterName.getText().trim() + "\n"
                    + "Contact: " + contact.getText().trim() + "\n"
                    + "Animal: " + animalType.getValue() + "\n"
                    + "Situation: " + injuryType.getValue() + "\n"
                    + "Urgency: " + urgency.getValue() + "\n"
                    + "Location: " + location.getText().trim() + "\n"
                    + "Photo: " + (selectedPhoto[0] == null ? "Not attached" : selectedPhoto[0].getName())
                    + "\n\nDemo only: this report has not been saved to a database.";
            showAlert(Alert.AlertType.INFORMATION, "Report ready", message);
        });

        // ---------- Header ----------
        Button back = new Button("← Back");
        back.setStyle(buttonStyle(false));
        back.setOnAction(e -> {
            if (backToHome != null) backToHome.run();
        });

        Text title = new Text("Report Rescue");
        title.setFont(Font.font("System", FontWeight.BOLD, 27));
        title.setFill(Color.web(DARK_GREEN));
        Label subtitle = new Label("Report an injured, sick or distressed animal. Help us reach them faster.");
        subtitle.setWrapText(true);
        subtitle.setStyle("-fx-font-size:13px;-fx-text-fill:#688177;");
        VBox titleBox = new VBox(4, title, subtitle);
        HBox header = new HBox(18, back, titleBox);
        header.setAlignment(Pos.CENTER_LEFT);

        VBox form = new VBox(14, header, reporterCard, animalCard, incidentCard, actions);
        form.setPadding(new Insets(22));
        form.setFillWidth(true);
        form.setMinWidth(0);
        form.setStyle("-fx-background-color:" + BG + ";");

        // ---------- Right information panel ----------
        VBox heroCard = new VBox(10);
        heroCard.setPadding(new Insets(20));
        heroCard.setStyle("-fx-background-color:linear-gradient(to right,#E8F8D9,#D9F5E5);"
                + "-fx-background-radius:18px;");
        heroCard.getChildren().addAll(
                text("Be Their Voice 🐶", 22, true, DARK_GREEN),
                text("Report • Rescue • Save Lives", 12, false, TEXT));

        VBox stepsCard = new VBox(13);
        stepsCard.setPadding(new Insets(18));
        stepsCard.setStyle(cardStyle());
        stepsCard.getChildren().add(text("📖  Rescue Guidelines", 18, true, DARK_GREEN));
        for (String tip : Arrays.asList(
                "✓  Keep a safe distance if the animal is aggressive or scared.",
                "✓  Do not try to handle severely injured animals.",
                "✓  Share clear location and photos.",
                "✓  Provide accurate information.",
                "✓  Our team will contact you shortly.")) {
            Label item = new Label(tip);
            item.setWrapText(true);
            item.setStyle("-fx-font-size:13px;-fx-text-fill:#506B5F;");
            stepsCard.getChildren().add(item);
        }
        VBox statusCard = infoCard("💛 Important Note",
                "Your report can help save an animal's life. Please provide accurate information for faster assistance.");

        VBox right = new VBox(14, heroCard, stepsCard, statusCard);
        right.setPadding(new Insets(22, 18, 22, 0));
        right.setPrefWidth(285);
        right.setMinWidth(230);
        right.setMaxWidth(310);
        right.setStyle("-fx-background-color:" + BG + ";");

        HBox content = new HBox(0, form, right);
        HBox.setHgrow(form, Priority.ALWAYS);
        content.setMinWidth(0);

        ScrollPane pageScroll = new ScrollPane(content);
        pageScroll.setFitToWidth(true);
        pageScroll.setFitToHeight(false);
        pageScroll.setPannable(true);
        pageScroll.setMinWidth(0);
        pageScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pageScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        pageScroll.setStyle("-fx-background:" + BG + ";-fx-background-color:" + BG + ";-fx-padding:0;");

        VBox sidebar = Sidebar.create("Report Rescue");
        sidebar.setPrefWidth(260);
        sidebar.setMinWidth(260);
        sidebar.setMaxWidth(260);

        HBox root = new HBox(0, sidebar, pageScroll);
        HBox.setHgrow(pageScroll, Priority.ALWAYS);
        root.setStyle("-fx-background-color:" + BG + ";");

        return new Scene(root, 1300, 760);
    }

    private VBox section(String number, String title, String subtitle, javafx.scene.Node content) {
        Label badge = new Label(number);
        badge.setMinSize(42, 42);
        badge.setAlignment(Pos.CENTER);
        badge.setStyle("-fx-background-color:" + MINT + ";-fx-background-radius:21px;"
                + "-fx-text-fill:" + GREEN + ";-fx-font-size:13px;-fx-font-weight:bold;");
        VBox titles = new VBox(3, text(title, 17, true, TEXT), text(subtitle, 12, false, "#71857B"));
        HBox heading = new HBox(12, badge, titles);
        heading.setAlignment(Pos.CENTER_LEFT);
        VBox card = new VBox(15, heading, content);
        card.setPadding(new Insets(17));
        card.setStyle(cardStyle());
        card.setMaxWidth(Double.MAX_VALUE);
        return card;
    }

    private GridPane grid(int columns) {
        GridPane grid = new GridPane();
        grid.setHgap(14);
        grid.setVgap(13);
        for (int i = 0; i < columns; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(100.0 / columns);
            col.setHgrow(Priority.ALWAYS);
            col.setMinWidth(0);
            grid.getColumnConstraints().add(col);
        }
        grid.setMinWidth(0);
        return grid;
    }

    private VBox field(String name, javafx.scene.Node control) {
        VBox box = new VBox(7, label(name), control);
        if (control instanceof Region) ((Region) control).setMaxWidth(Double.MAX_VALUE);
        return box;
    }

    private Label label(String value) {
        Label label = new Label(value);
        label.setStyle("-fx-font-size:13px;-fx-font-weight:bold;-fx-text-fill:#354F43;");
        return label;
    }

    private Text text(String value, double size, boolean bold, String color) {
        Text text = new Text(value);
        text.setFont(Font.font("System", bold ? FontWeight.BOLD : FontWeight.NORMAL, size));
        text.setFill(Color.web(color));
        return text;
    }

    private TextField input(String prompt) {
        TextField input = new TextField();
        input.setPromptText(prompt);
        input.setPrefHeight(42);
        input.setStyle(inputStyle());
        return input;
    }

    private <T> ComboBox<T> combo(String prompt, T... values) {
        ComboBox<T> combo = new ComboBox<>();
        combo.getItems().addAll(Arrays.asList(values));
        combo.setPromptText(prompt);
        combo.setPrefHeight(42);
        combo.setMaxWidth(Double.MAX_VALUE);
        combo.setStyle(inputStyle());
        return combo;
    }

    private String inputStyle() {
        return "-fx-font-size:13px;-fx-background-color:white;-fx-border-color:" + BORDER
                + ";-fx-border-radius:8px;-fx-background-radius:8px;-fx-padding:9px;";
    }

    private String buttonStyle(boolean primary) {
        if (primary) {
            return "-fx-background-color:" + GREEN + ";-fx-text-fill:white;-fx-font-weight:bold;"
                    + "-fx-background-radius:9px;-fx-cursor:hand;-fx-padding:11px 16px;";
        }
        return "-fx-background-color:white;-fx-text-fill:" + GREEN + ";-fx-font-weight:bold;"
                + "-fx-border-color:" + BORDER + ";-fx-border-radius:9px;"
                + "-fx-background-radius:9px;-fx-cursor:hand;-fx-padding:11px 16px;";
    }

    private VBox infoCard(String title, String body) {
        Label heading = new Label(title);
        heading.setStyle("-fx-font-size:16px;-fx-font-weight:bold;-fx-text-fill:" + DARK_GREEN + ";");
        Label description = new Label(body);
        description.setWrapText(true);
        description.setStyle("-fx-font-size:12px;-fx-text-fill:#526C60;");
        VBox card = new VBox(10, heading, description);
        card.setPadding(new Insets(17));
        card.setStyle(cardStyle());
        return card;
    }

    private String cardStyle() {
        return "-fx-background-color:white;-fx-background-radius:15px;"
                + "-fx-border-color:" + BORDER + ";-fx-border-radius:15px;";
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("JeevMitra - Report Rescue");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
