package com.techyuva.view.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ReminderPage {
    private static final String GREEN = "#12684f";
    private static final String DARK = "#0b3f31";
    private static final String BG = "#f2faf6";
    private static final String BORDER = "#d7ebe1";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd MMM yyyy");

    private final List<FeedingEntry> entries = new ArrayList<>();
    private VBox scheduleList;
    private VBox historyRows;
    private Label totalValue, fedValue, pendingValue, rateValue;

    public ReminderPage() {
        // Demo entries use the same pet photos already used by the Adoption page.
        entries.add(new FeedingEntry("Bruno", "Dog", "Male", "2 years", "08:00 AM",
                "Dog Food", "200g", "Completed", "/assets/img/adoption/charlie.jpg"));
        entries.add(new FeedingEntry("Misty", "Cat", "Female", "1 year", "09:00 AM",
                "Cat Food", "100g", "Pending", "/assets/img/adoption/misty.jpg"));
        entries.add(new FeedingEntry("Rocky", "Dog", "Male", "3 years", "12:00 PM",
                "Puppy Food", "150g", "Pending", "/assets/img/adoption/rocky.jpg"));
        entries.add(new FeedingEntry("Luna", "Cat", "Female", "6 months", "06:00 PM",
                "Cat Food", "100g", "Pending", "/assets/img/adoption/luna.jpg"));
    }

    public Scene getReminderScene(Runnable backToHome) {
        BorderPane root = new BorderPane();
        setBackground(root, BG, 0);

        VBox sidebar = Sidebar.create("Feeding Reminders");
        sidebar.setPrefWidth(260);
        sidebar.setMinWidth(260);
        root.setLeft(sidebar);

        VBox content = new VBox(14);
        content.setPadding(new Insets(18));
        content.setFillWidth(true);

        HBox header = createHeader(backToHome);
        HBox stats = createStats();
        HBox mainRow = new HBox(14);
        mainRow.setAlignment(Pos.TOP_LEFT);

        VBox scheduleCard = createScheduleCard();
        VBox formCard = createFormCard();
        scheduleCard.setMinWidth(0);
        formCard.setMinWidth(0);
        HBox.setHgrow(scheduleCard, Priority.ALWAYS);
        HBox.setHgrow(formCard, Priority.ALWAYS);
        scheduleCard.prefWidthProperty().bind(mainRow.widthProperty().multiply(0.52));
        formCard.prefWidthProperty().bind(mainRow.widthProperty().multiply(0.48));
        mainRow.getChildren().addAll(scheduleCard, formCard);

        HBox bottomRow = new HBox(14, createHistoryCard(), createUpcomingCard());
        bottomRow.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(bottomRow.getChildren().get(0), Priority.ALWAYS);
        HBox.setHgrow(bottomRow.getChildren().get(1), Priority.ALWAYS);

        content.getChildren().addAll(header, stats, mainRow, bottomRow);
        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setBackground(Background.EMPTY);
        root.setCenter(scroll);

        refreshAll();
        return new Scene(root, 1300, 760);
    }

    private HBox createHeader(Runnable backToHome) {
        Button back = new Button("← Back");
        styleButton(back, false);
        back.setOnAction(e -> { if (backToHome != null) backToHome.run(); });

        Label paw = label("🐾", 34, true, DARK);
        VBox titleBox = new VBox(2,
                label("Feeding Reminders", 28, true, DARK),
                label("Keep your furry friends healthy with timely meals.", 13, false, "#5c7b6e"));
        HBox title = new HBox(12, paw, titleBox);
        title.setAlignment(Pos.CENTER_LEFT);

        ImageView dog = image("/assets/img/adoption/charlie.jpg", 74, 66);
        ImageView cat = image("/assets/img/adoption/misty.jpg", 66, 66);
        HBox pets = new HBox(-8, dog, cat);
        pets.setAlignment(Pos.CENTER);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button add = new Button("＋ Add Feeding Reminder");
        styleButton(add, true);
        add.setOnAction(e -> showAddFormInfo());
        HBox right = new HBox(12, pets, add);
        right.setAlignment(Pos.CENTER_RIGHT);

        HBox header = new HBox(16, back, title, spacer, right);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(14));
        setBackground(header, "#e0f5e9", 15);
        setBorder(header, BORDER, 15);
        return header;
    }

    private HBox createStats() {
        totalValue = label("4", 27, true, DARK);
        fedValue = label("1", 27, true, GREEN);
        pendingValue = label("3", 27, true, "#bd4b40");
        rateValue = label("25%", 27, true, "#23628a");
        return new HBox(12,
                statCard("👥", "Total Pets", totalValue, "#e3f8eb"),
                statCard("🍚", "Fed Today", fedValue, "#e4f8eb"),
                statCard("◷", "Pending", pendingValue, "#fff0ee"),
                statCard("▥", "Completion Rate", rateValue, "#edf5ff"));
    }

    private VBox statCard(String icon, String title, Label value, String tint) {
        Label iconLabel = label(icon, 25, true, GREEN);
        Circle circle = new Circle(28, Color.web(tint));
        StackIcon iconNode = new StackIcon(circle, iconLabel);
        value.setVisible(true);
        value.setManaged(true);
        value.setTextFill(Color.web(GREEN));
        VBox texts = new VBox(2, label(title, 13, true, "#315b4d"), value);
        texts.setVisible(true);
        texts.setManaged(true);
        HBox inner = new HBox(14, iconNode, texts);
        inner.setAlignment(Pos.CENTER_LEFT);
        VBox card = new VBox(inner);
        card.setPadding(new Insets(12));
        setCard(card);
        HBox.setHgrow(card, Priority.ALWAYS);
        card.setMaxWidth(Double.MAX_VALUE);
        return card;
    }

    private VBox createScheduleCard() {
        Label title = label("▦  Today's Feeding Schedule", 19, true, DARK);
        Label date = label("▦  " + LocalDate.now().format(DATE_FORMAT), 13, false, "#547568");
        HBox heading = new HBox(title, spacer(), date);
        heading.setAlignment(Pos.CENTER_LEFT);
        scheduleList = new VBox(8);
        VBox card = new VBox(12, heading, separator(), scheduleList);
        card.setMinHeight(400);
        card.setPadding(new Insets(14));
        setCard(card);
        return card;
    }

    private VBox createFormCard() {
        Label title = label("⊕  Add Feeding Reminder", 18, true, DARK);

        TextField petName = new TextField();
        petName.setPromptText("Enter pet name");
        ComboBox<String> animal = combo("Select type", "Dog", "Cat", "Other");
        DatePicker date = new DatePicker(LocalDate.now());
        ComboBox<String> time = combo("Select time", "08:00 AM", "09:00 AM", "12:00 PM",
                "02:00 PM", "06:00 PM", "08:00 PM");
        TextField food = new TextField();
        food.setPromptText("e.g. Dog Food");
        TextField quantity = new TextField();
        quantity.setPromptText("e.g. 200g");
        TextArea notes = new TextArea();
        notes.setPromptText("e.g. Add supplements, specific diet, etc...");
        notes.setPrefRowCount(3);
        notes.setWrapText(true);

        GridPane fields = new GridPane();
        fields.setHgap(10);
        fields.setVgap(9);
        addField(fields, "Pet Name", petName, 0, 0);
        addField(fields, "Animal Type", animal, 1, 0);
        addField(fields, "Feeding Date", date, 0, 1);
        addField(fields, "Feeding Time", time, 1, 1);
        addField(fields, "Food Type", food, 0, 2);
        addField(fields, "Food Quantity", quantity, 1, 2);
        fields.add(label("Special Instructions (Optional)", 12, true, "#315b4d"), 0, 3, 2, 1);
        fields.add(notes, 0, 4, 2, 1);
        GridPane.setHgrow(petName, Priority.ALWAYS);
        GridPane.setHgrow(animal, Priority.ALWAYS);
        GridPane.setHgrow(date, Priority.ALWAYS);
        GridPane.setHgrow(time, Priority.ALWAYS);
        GridPane.setHgrow(food, Priority.ALWAYS);
        GridPane.setHgrow(quantity, Priority.ALWAYS);
        petName.setMaxWidth(Double.MAX_VALUE);
        animal.setMaxWidth(Double.MAX_VALUE);
        date.setMaxWidth(Double.MAX_VALUE);
        time.setMaxWidth(Double.MAX_VALUE);
        food.setMaxWidth(Double.MAX_VALUE);
        quantity.setMaxWidth(Double.MAX_VALUE);
        notes.setMaxWidth(Double.MAX_VALUE);

        Button save = new Button("▣  Save Reminder");
        save.setMaxWidth(Double.MAX_VALUE);
        styleButton(save, true);
        Button reset = new Button("↻  Reset");
        styleButton(reset, false);
        reset.setOnAction(e -> {
            petName.clear(); animal.setValue("Select type"); date.setValue(LocalDate.now());
            time.setValue("Select time"); food.clear(); quantity.clear(); notes.clear();
        });
        save.setOnAction(e -> {
            if (petName.getText().trim().isEmpty() || animal.getValue() == null
                    || "Select type".equals(animal.getValue()) || date.getValue() == null
                    || time.getValue() == null || "Select time".equals(time.getValue())
                    || food.getText().trim().isEmpty() || quantity.getText().trim().isEmpty()) {
                alert(Alert.AlertType.WARNING, "Missing Details", "Please complete pet name, animal type, date, time, food and quantity.");
                return;
            }
            String pet = petName.getText().trim();
            String type = animal.getValue();
            String img = "Cat".equals(type) ? "/assets/img/adoption/misty.jpg" : "/assets/img/adoption/charlie.jpg";
            entries.add(new FeedingEntry(pet, type, "-", "-", time.getValue(), food.getText().trim(),
                    quantity.getText().trim(), "Pending", img));
            refreshAll();
            alert(Alert.AlertType.INFORMATION, "Reminder Saved", "Feeding reminder added for " + pet + ".");
            reset.fire();
        });

        HBox actions = new HBox(10, save, reset);
        HBox.setHgrow(save, Priority.ALWAYS);
        VBox form = new VBox(12, title, separator(), fields, actions);
        form.setMinHeight(400);
        form.setPadding(new Insets(14));
        setCard(form);
        return form;
    }

    private VBox createHistoryCard() {
        Label title = label("◷  Feeding History", 17, true, DARK);
        Button view = new Button("View All");
        styleButton(view, false);
        HBox heading = new HBox(title, spacer(), view);
        heading.setAlignment(Pos.CENTER_LEFT);
        historyRows = new VBox(0);
        VBox card = new VBox(10, heading, historyRows);
        card.setPadding(new Insets(14));
        setCard(card);
        HBox.setHgrow(card, Priority.ALWAYS);
        return card;
    }

    private VBox createUpcomingCard() {
        Label title = label("♟  Upcoming Reminders", 17, true, DARK);
        Button view = new Button("View All");
        styleButton(view, false);
        HBox heading = new HBox(title, spacer(), view);
        heading.setAlignment(Pos.CENTER_LEFT);

        VBox rows = new VBox(6);
        rows.getChildren().add(tableHeader("Date", "Time", "Pet Name", "Food", "Status"));
        for (FeedingEntry entry : entries) {
            if ("Pending".equals(entry.status)) {
                rows.getChildren().add(tableRow(LocalDate.now().format(DATE_FORMAT), entry.time,
                        entry.name, entry.food, entry.status));
            }
        }
        VBox card = new VBox(10, heading, rows);
        card.setPadding(new Insets(14));
        setCard(card);
        HBox.setHgrow(card, Priority.ALWAYS);
        return card;
    }

    private void refreshAll() {
        if (scheduleList == null) return;
        scheduleList.getChildren().clear();
        historyRows.getChildren().clear();
        int fed = 0;
        for (FeedingEntry entry : entries) {
            if ("Completed".equals(entry.status)) fed++;
            scheduleList.getChildren().add(scheduleRow(entry));
            historyRows.getChildren().add(tableRow(LocalDate.now().format(DATE_FORMAT),
                    entry.name, entry.food, entry.quantity, entry.status));
        }
        totalValue.setText(String.valueOf(entries.size()));
        fedValue.setText(String.valueOf(fed));
        pendingValue.setText(String.valueOf(entries.size() - fed));
        rateValue.setText(entries.isEmpty() ? "0%" : Math.round((fed * 100.0) / entries.size()) + "%");
    }

    private HBox scheduleRow(FeedingEntry entry) {
        ImageView petImage = image(entry.imagePath, 112, 72);
        petImage.setFitWidth(112);
        petImage.setFitHeight(72);
        petImage.setPreserveRatio(true);
        VBox details = new VBox(5,
                label(entry.name + ("Male".equals(entry.gender) ? "  ♂" : "  ♀"), 16, true, DARK),
                label("🐾  " + entry.type + "   ·   " + entry.age, 12, false, "#59796c"),
                label("◷  " + entry.time + "   ·   🍚  " + entry.food + " (" + entry.quantity + ")", 12, false, "#59796c"));
        details.setMinWidth(170);
        details.setPrefWidth(250);
        details.setVisible(true);
        details.setManaged(true);
        HBox.setHgrow(details, Priority.ALWAYS);

        Label status = label("●  " + entry.status, 12, true,
                "Completed".equals(entry.status) ? "#15834e" : "#bb7b00");
        status.setAlignment(Pos.CENTER);
        status.setMaxWidth(Double.MAX_VALUE);
        status.setPadding(new Insets(7, 9, 7, 9));
        setBackground(status, "Completed".equals(entry.status) ? "#e3f8eb" : "#fff3d9", 9);

        Button mark = new Button("✓  Mark as Fed");
        mark.setMaxWidth(Double.MAX_VALUE);
        styleButton(mark, true);
        mark.setDisable("Completed".equals(entry.status));
        mark.setOnAction(e -> {
            entry.status = "Completed";
            refreshAll();
        });
        VBox actions = new VBox(7, status, mark);
        actions.setPrefWidth(130);
        actions.setMinWidth(115);
        HBox row = new HBox(12, petImage, details, actions);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(8));
        setBackground(row, "#ffffff", 12);
        setBorder(row, BORDER, 12);
        return row;
    }

    private HBox tableHeader(String... values) {
        HBox row = new HBox(8);
        for (String value : values) {
            Label cell = label(value, 11, true, "#315b4d");
            cell.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(cell, Priority.ALWAYS);
            row.getChildren().add(cell);
        }
        row.setPadding(new Insets(8));
        setBackground(row, "#edf6f1", 7);
        return row;
    }

    private HBox tableRow(String... values) {
        HBox row = new HBox(8);
        for (int i = 0; i < values.length; i++) {
            Label cell = label(values[i], 10, false, "#56766a");
            cell.setMaxWidth(Double.MAX_VALUE);
            cell.setMinWidth(50);
            HBox.setHgrow(cell, Priority.ALWAYS);
            if ("Completed".equals(values[i])) cell.setTextFill(Color.web("#16824d"));
            if ("Pending".equals(values[i])) cell.setTextFill(Color.web("#bb7b00"));
            row.getChildren().add(cell);
        }
        row.setPadding(new Insets(7, 8, 7, 8));
        row.setBorder(new Border(new BorderStroke(Color.web(BORDER), Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE, BorderStrokeStyle.NONE, BorderStrokeStyle.NONE, CornerRadii.EMPTY, new BorderWidths(0, 0, 1, 0), Insets.EMPTY)));
        return row;
    }

    private void addField(GridPane grid, String title, javafx.scene.Node field, int col, int row) {
        VBox box = new VBox(5, label(title, 12, true, "#315b4d"), field);
        box.setMinWidth(0);
        grid.add(box, col, row);
    }

    private ComboBox<String> combo(String prompt, String... options) {
        ComboBox<String> box = new ComboBox<>();
        box.getItems().addAll(options);
        box.setPromptText(prompt);
        box.setValue(prompt);
        box.setMaxWidth(Double.MAX_VALUE);
        return box;
    }

    private ImageView image(String path, double width, double height) {
        Image img = null;
        try {
            var stream = getClass().getResourceAsStream(path);
            if (stream != null) {
                img = new Image(stream);
                if (img.isError()) {
                    System.err.println("Image load error: " + path + " -> " + img.getException());
                    img = null;
                }
            } else {
                System.err.println("Image resource not found: " + path);
            }
        } catch (Exception ex) {
            System.err.println("Unable to load image: " + path + " -> " + ex.getMessage());
        }
        ImageView view = img == null ? new ImageView() : new ImageView(img);
        view.setFitWidth(width);
        view.setFitHeight(height);
        view.setPreserveRatio(false);
        view.setSmooth(true);
        javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle(width, height);
        clip.setArcWidth(14);
        clip.setArcHeight(14);
        view.setClip(clip);
        return view;
    }

    private Label label(String text, int size, boolean bold, String color) {
        Label label = new Label(text);
        String safeColor = (color == null || color.isBlank()) ? DARK : color;
        label.setFont(Font.font("Arial", bold ? FontWeight.BOLD : FontWeight.NORMAL, size));
        label.setTextFill(Color.web(safeColor));
        label.setWrapText(true);
        label.setVisible(true);
        label.setManaged(true);
        label.setOpacity(1.0);
        return label;
    }

    private Region spacer() {
        Region r = new Region();
        HBox.setHgrow(r, Priority.ALWAYS);
        return r;
    }

    private Region separator() {
        Region line = new Region();
        line.setPrefHeight(1);
        line.setMaxHeight(1);
        setBackground(line, BORDER, 0);
        return line;
    }

    private void setCard(VBox card) {
        setBackground(card, "#ffffff", 15);
        setBorder(card, BORDER, 15);
    }

    private void setBackground(javafx.scene.layout.Region node, String color, double radius) {
        node.setBackground(new Background(new BackgroundFill(
                Color.web(color), new CornerRadii(radius), Insets.EMPTY)));
    }

    private void setBorder(javafx.scene.layout.Region node, String color, double radius) {
        node.setBorder(new Border(new BorderStroke(
                Color.web(color), BorderStrokeStyle.SOLID,
                new CornerRadii(radius), new BorderWidths(1))));
    }

    private void styleButton(Button button, boolean primary) {
        button.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        button.setTextFill(Color.web(primary ? "#ffffff" : GREEN));
        button.setPadding(primary ? new Insets(11, 14, 11, 14) : new Insets(9, 12, 9, 12));
        button.setBackground(new Background(new BackgroundFill(
                Color.web(primary ? GREEN : "#ffffff"), new CornerRadii(9), Insets.EMPTY)));
        button.setBorder(new Border(new BorderStroke(
                Color.web(primary ? GREEN : BORDER), BorderStrokeStyle.SOLID,
                new CornerRadii(9), new BorderWidths(1))));
        button.setCursor(javafx.scene.Cursor.HAND);
    }

    private void alert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAddFormInfo() {
        alert(Alert.AlertType.INFORMATION, "Add Feeding Reminder",
                "Use the form on the right to add a pet's feeding schedule.");
    }

    private static class FeedingEntry {
        String name, type, gender, age, time, food, quantity, status, imagePath;
        FeedingEntry(String name, String type, String gender, String age, String time,
                     String food, String quantity, String status, String imagePath) {
            this.name = name; this.type = type; this.gender = gender; this.age = age;
            this.time = time; this.food = food; this.quantity = quantity;
            this.status = status; this.imagePath = imagePath;
        }
    }

    private static class StackIcon extends HBox {
        StackIcon(Circle circle, Label icon) {
            super();
            javafx.scene.layout.StackPane stack = new javafx.scene.layout.StackPane(circle, icon);
            getChildren().add(stack);
            setAlignment(Pos.CENTER);
        }
    }
}
