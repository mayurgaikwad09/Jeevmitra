package com.techyuva.view.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ProfilePage {

    private static final Color GREEN = Color.web("#12684f");
    private static final Color DARK_GREEN = Color.web("#0b4938");
    private static final Color TEXT = Color.web("#173b36");
    private static final Color MUTED = Color.web("#607a76");
    private static final Color PALE_GREEN = Color.web("#e8f6ef");
    private static final Color BORDER = Color.web("#d7ebe2");
    private static final Color PAGE_BG = Color.web("#f3faf6");

    private final List<Pet> pets = new ArrayList<>();
    private VBox petsList;
    private VBox activityList;
    private Label petCountLabel;
    private Label adoptionCountLabel;
    private Label appointmentCountLabel;
    private Label reminderCountLabel;
    private Label sectionTitle;
    private VBox mainPetCard;

    private String userName = "Mayur Gaikwad";
    private String userEmail = "mayurgaikwad@example.com";
    private String userPhone = "+91 98765 43210";
    private String userAddress = "Ahmadnagar, Maharashtra";
    private String userBirthDate = "09 March 2005";

    public ProfilePage() {
        pets.add(new Pet("Bruno", "Dog", "2 years", "Dog Food (200g)",
                "08:00 AM", "Healthy", "/assets/img/adoption/charlie.jpg"));
        pets.add(new Pet("Misty", "Cat", "1 year", "Cat Food (100g)",
                "09:00 AM", "Vaccinated", "/assets/img/adoption/misty.jpg"));
        pets.add(new Pet("Rocky", "Dog", "3 years", "Puppy Food (150g)",
                "12:00 PM", "Under Treatment", "/assets/img/adoption/rocky.jpg"));
    }

    public Scene getProfileScene(Runnable backToHome) {
        BorderPaneHolder holder = new BorderPaneHolder();

        VBox sidebar = Sidebar.create("Profile");
        sidebar.setPrefWidth(260);
        sidebar.setMinWidth(260);
        holder.setLeft(sidebar);

        VBox page = new VBox(14);
        page.setPadding(new Insets(16));
        page.setFillWidth(true);

        page.getChildren().addAll(
                createHeader(backToHome),
                createProfileOverview(),
                createTabs(),
                createMainContent()
        );

        ScrollPane scroll = new ScrollPane(page);
        scroll.setFitToWidth(true);
        scroll.setPannable(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setBackground(Background.EMPTY);
        holder.setCenter(scroll);

        return new Scene(holder.root, 1300, 760);
    }

    private HBox createHeader(Runnable backToHome) {
        Label icon = label("●", 32, true, GREEN);
        Label title = label("My Profile", 29, true, DARK_GREEN);
        Label subtitle = label("Manage your account, your pets and your activity.", 14, false, MUTED);
        VBox titles = new VBox(3, title, subtitle);
        HBox left = new HBox(14, icon, titles);
        left.setAlignment(Pos.CENTER_LEFT);

        ImageView dog = petImage("/assets/img/adoption/charlie.jpg", 78, 70);
        ImageView cat = petImage("/assets/img/adoption/misty.jpg", 78, 70);
        HBox photos = new HBox(8, dog, cat);
        photos.setAlignment(Pos.CENTER_RIGHT);

        Button back = secondaryButton("← Back");
        back.setOnAction(e -> {
            if (backToHome != null) backToHome.run();
        });

        Region spacer = spacer();
        HBox header = new HBox(16, back, left, spacer, photos);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(16));
        header.setBackground(background(Color.web("#e2f5eb"), 18));
        header.setBorder(border(BORDER, 18, 1));
        return header;
    }

    private HBox createProfileOverview() {
        VBox identity = new VBox(10);
        identity.setPadding(new Insets(16));
        identity.setAlignment(Pos.CENTER_LEFT);

        StackPane avatar = new StackPane();
        Circle avatarCircle = new Circle(65, Color.web("#d8eee3"));
        Label initials = label("MG", 27, true, GREEN);
        avatar.getChildren().addAll(avatarCircle, initials);
        avatar.setMinWidth(130);

        VBox info = new VBox(8);
        Label name = label(userName, 22, true, Color.web("#142c35"));
        Button editName = textButton("✎");
        editName.setOnAction(e -> showEditProfileDialog());
        HBox nameLine = new HBox(8, name, editName);
        nameLine.setAlignment(Pos.CENTER_LEFT);

        info.getChildren().addAll(
                nameLine,
                label("✉   " + userEmail, 14, false, MUTED),
                label("☎   " + userPhone, 14, false, MUTED),
                label("⌖   " + userAddress, 14, false, MUTED),
                label("▦   " + userBirthDate, 14, false, MUTED)
        );
        HBox identityContent = new HBox(16, avatar, info);
        identityContent.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(info, Priority.ALWAYS);
        identity.getChildren().add(identityContent);
        identity.setBackground(background(Color.WHITE, 18));
        identity.setBorder(border(BORDER, 18, 1));
        HBox.setHgrow(identity, Priority.ALWAYS);

        petCountLabel = label(String.valueOf(pets.size()), 25, true, TEXT);
        adoptionCountLabel = label("2", 25, true, TEXT);
        appointmentCountLabel = label("5", 25, true, TEXT);
        reminderCountLabel = label("8", 25, true, TEXT);

        HBox stats = new HBox(8,
                statCard("🐾", "My Pets", petCountLabel, Color.web("#edf8f2"), GREEN),
                statCard("♥", "Adoptions", adoptionCountLabel, Color.web("#fff0f1"), Color.web("#df334b")),
                statCard("▦", "Appointments", appointmentCountLabel, Color.web("#edf5ff"), Color.web("#267ac7")),
                statCard("♟", "Reminders", reminderCountLabel, Color.web("#fff6e8"), Color.web("#e89b22"))
        );
        stats.setAlignment(Pos.CENTER);
        VBox right = new VBox(12, stats, createProfileActions());
        HBox.setHgrow(right, Priority.ALWAYS);

        HBox overview = new HBox(12, identity, right);
        overview.setAlignment(Pos.TOP_LEFT);
        return overview;
    }

    private VBox statCard(String icon, String title, Label value, Color tint, Color iconColor) {
        Label iconLabel = label(icon, 22, true, iconColor);
        StackPane iconCircle = new StackPane(new Circle(25, tint), iconLabel);
        VBox texts = new VBox(2, value, label(title, 12, false, MUTED));
        texts.setAlignment(Pos.CENTER);
        VBox card = new VBox(7, iconCircle, texts);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(10, 8, 10, 8));
        card.setBackground(background(Color.WHITE, 14));
        card.setBorder(border(BORDER, 14, 1));
        card.setMinWidth(95);
        card.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(card, Priority.ALWAYS);
        return card;
    }

    private HBox createProfileActions() {
        Button edit = primaryButton("✎  Edit Profile");
        edit.setOnAction(e -> showEditProfileDialog());
        Button password = secondaryButton("▣  Change Password");
        password.setOnAction(e -> showInfo("Change Password",
                "Connect this button to your authentication/password update workflow."));
        HBox actions = new HBox(10, edit, password);
        actions.setAlignment(Pos.CENTER);
        HBox.setHgrow(edit, Priority.ALWAYS);
        HBox.setHgrow(password, Priority.ALWAYS);
        return actions;
    }

    private HBox createTabs() {
        Button petsTab = tabButton("🐾  My Pets", true);
        Button adoptionsTab = tabButton("♥  My Adoptions", false);
        Button appointmentsTab = tabButton("▦  Appointments", false);
        Button remindersTab = tabButton("♟  Reminders", false);
        Button settingsTab = tabButton("⚙  Settings", false);

        petsTab.setOnAction(e -> showSection("My Pets"));
        adoptionsTab.setOnAction(e -> showSection("My Adoptions"));
        appointmentsTab.setOnAction(e -> showSection("Appointments"));
        remindersTab.setOnAction(e -> showSection("Reminders"));
        settingsTab.setOnAction(e -> showSection("Settings"));

        HBox tabs = new HBox(4, petsTab, adoptionsTab, appointmentsTab, remindersTab, settingsTab);
        tabs.setAlignment(Pos.CENTER);
        tabs.setPadding(new Insets(4));
        tabs.setBackground(background(Color.WHITE, 15));
        tabs.setBorder(border(BORDER, 15, 1));
        for (Node node : tabs.getChildren()) HBox.setHgrow(node, Priority.ALWAYS);
        return tabs;
    }

    private Button tabButton(String text, boolean active) {
        Button button = new Button(text);
        button.setFont(Font.font("Arial", active ? FontWeight.BOLD : FontWeight.NORMAL, 13));
        button.setTextFill(active ? Color.WHITE : TEXT);
        button.setPadding(new Insets(13, 8, 13, 8));
        button.setMaxWidth(Double.MAX_VALUE);
        button.setBackground(background(active ? GREEN : Color.WHITE, 12));
        button.setBorder(border(active ? GREEN : Color.WHITE, 12, 1));
        button.setOnMouseEntered(e -> {
            if (!active) button.setBackground(background(PALE_GREEN, 12));
        });
        button.setOnMouseExited(e -> {
            if (!active) button.setBackground(background(Color.WHITE, 12));
        });
        return button;
    }

    private HBox createMainContent() {
        mainPetCard = createPetsCard();
        VBox right = new VBox(12, createActivityCard(), createAccountCard());
        HBox.setHgrow(mainPetCard, Priority.ALWAYS);
        HBox.setHgrow(right, Priority.ALWAYS);
        HBox content = new HBox(12, mainPetCard, right);
        content.setAlignment(Pos.TOP_LEFT);
        return content;
    }

    private VBox createPetsCard() {
        Label heading = label("🐾  My Pets", 18, true, TEXT);
        Button addPet = primaryButton("+ Add Pet");
        addPet.setOnAction(e -> showAddPetDialog());
        HBox titleRow = new HBox(10, heading, spacer(), addPet);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        petsList = new VBox(8);
        refreshPetRows();

        VBox card = new VBox(12, titleRow, petsList);
        card.setPadding(new Insets(14));
        card.setBackground(background(Color.WHITE, 17));
        card.setBorder(border(BORDER, 17, 1));
        return card;
    }

    private void refreshPetRows() {
        if (petsList == null) return;
        petsList.getChildren().clear();
        for (Pet pet : pets) petsList.getChildren().add(createPetRow(pet));
        if (petCountLabel != null) petCountLabel.setText(String.valueOf(pets.size()));
    }

    private HBox createPetRow(Pet pet) {
        ImageView photo = petImage(pet.imagePath, 105, 82);
        VBox details = new VBox(5,
                label(pet.name + ("Dog".equals(pet.type) ? "  ♂" : "  ♀"), 16, true, TEXT),
                label(pet.type + "  ·  " + pet.age, 12, false, MUTED),
                label("●  " + pet.food + "     ◷  " + pet.time, 12, false, MUTED)
        );
        HBox.setHgrow(details, Priority.ALWAYS);

        Color statusColor = "Healthy".equals(pet.status) ? Color.web("#16835a")
                : "Vaccinated".equals(pet.status) ? Color.web("#267ac7") : Color.web("#d18415");
        Color statusBg = "Healthy".equals(pet.status) ? Color.web("#e8f7ef")
                : "Vaccinated".equals(pet.status) ? Color.web("#eaf4ff") : Color.web("#fff5e5");
        Label status = label("●  " + pet.status, 11, true, statusColor);
        status.setPadding(new Insets(7, 9, 7, 9));
        status.setBackground(background(statusBg, 12));

        Button edit = smallActionButton("✎", Color.web("#e8f7ef"), GREEN);
        edit.setOnAction(e -> showEditPetDialog(pet));
        Button delete = smallActionButton("▣", Color.web("#fff0f1"), Color.web("#d82f45"));
        delete.setOnAction(e -> {
            pets.remove(pet);
            refreshPetRows();
        });

        VBox actions = new VBox(7, status, new HBox(6, edit, delete));
        actions.setAlignment(Pos.CENTER_RIGHT);

        HBox row = new HBox(12, photo, details, actions);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(9));
        row.setBackground(background(Color.WHITE, 13));
        row.setBorder(border(Color.web("#e0eee8"), 13, 1));
        return row;
    }

    private VBox createActivityCard() {
        Label title = label("◷  Recent Activity", 17, true, TEXT);
        Button viewAll = secondaryButton("View All");
        viewAll.setOnAction(e -> showInfo("Recent Activity", "Your latest pet-care activities are shown here."));
        HBox heading = new HBox(title, spacer(), viewAll);
        heading.setAlignment(Pos.CENTER_LEFT);

        activityList = new VBox(12,
                activityRow("🐾", "You marked Bruno as fed", "Today, 08:00 AM", GREEN),
                activityRow("▦", "Appointment scheduled for Misty", "24 Oct 2026, 10:00 AM", Color.web("#267ac7")),
                activityRow("♥", "Adoption request submitted", "22 Oct 2026", Color.web("#df334b")),
                activityRow("♟", "Feeding reminder set for Rocky", "21 Oct 2026, 12:00 PM", Color.web("#e89b22"))
        );
        VBox card = new VBox(12, heading, activityList);
        card.setPadding(new Insets(14));
        card.setBackground(background(Color.WHITE, 17));
        card.setBorder(border(BORDER, 17, 1));
        return card;
    }

    private HBox activityRow(String icon, String message, String date, Color color) {
        StackPane iconCircle = new StackPane(new Circle(21, lighten(color)), label(icon, 17, true, color));
        VBox text = new VBox(3, label(message, 12, true, TEXT), label(date, 11, false, MUTED));
        HBox row = new HBox(10, iconCircle, text);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private VBox createAccountCard() {
        Label title = label("⚙  Account Information", 17, true, TEXT);
        HBox member = new HBox(10, label("▦", 22, true, GREEN),
                new VBox(3, label("Member Since", 11, false, MUTED),
                        label("15 Sep 2024", 13, true, TEXT)));
        HBox accountType = new HBox(10, label("♛", 22, true, GREEN),
                new VBox(3, label("Account Type", 11, false, MUTED),
                        label("Regular User", 13, true, TEXT)));
        HBox row = new HBox(20, member, accountType);
        row.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(member, Priority.ALWAYS);
        HBox.setHgrow(accountType, Priority.ALWAYS);
        VBox card = new VBox(12, title, row);
        card.setPadding(new Insets(14));
        card.setBackground(background(Color.WHITE, 17));
        card.setBorder(border(BORDER, 17, 1));
        return card;
    }

    private void showSection(String section) {
        if (sectionTitle != null) sectionTitle.setText(section);
        switch (section) {
            case "My Pets" -> showInfo(section, "Your registered pets are displayed in the My Pets section.");
            case "My Adoptions" -> showInfo(section, "Adoption records can be connected here.");
            case "Appointments" -> showInfo(section, "Your upcoming appointments can be connected here.");
            case "Reminders" -> showInfo(section, "Your feeding reminders can be connected here.");
            case "Settings" -> showInfo(section, "Profile and account settings can be managed here.");
            default -> showInfo(section, "Section selected.");
        }
    }

    private void showEditProfileDialog() {
        TextField name = new TextField(userName);
        TextField email = new TextField(userEmail);
        TextField phone = new TextField(userPhone);
        TextField address = new TextField(userAddress);

        VBox form = new VBox(8,
                label("Full Name", 12, true, TEXT), name,
                label("Email", 12, true, TEXT), email,
                label("Phone", 12, true, TEXT), phone,
                label("Address", 12, true, TEXT), address);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Edit Profile");
        alert.setHeaderText("Update your profile details");
        alert.getDialogPane().setContent(form);
        alert.showAndWait().ifPresent(result -> {
            userName = name.getText().trim();
            userEmail = email.getText().trim();
            userPhone = phone.getText().trim();
            userAddress = address.getText().trim();
            showInfo("Profile Updated", "Your updated details will appear when this page is refreshed.");
        });
    }

    private void showAddPetDialog() {
        TextField name = new TextField();
        name.setPromptText("Pet name");
        TextField type = new TextField();
        type.setPromptText("Dog or Cat");
        TextField age = new TextField();
        age.setPromptText("Age, e.g. 1 year");
        TextField food = new TextField();
        food.setPromptText("Food details");
        VBox form = new VBox(8,
                label("Pet Name", 12, true, TEXT), name,
                label("Pet Type", 12, true, TEXT), type,
                label("Age", 12, true, TEXT), age,
                label("Food", 12, true, TEXT), food);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Add Pet");
        alert.setHeaderText("Add a pet to your profile");
        alert.getDialogPane().setContent(form);
        alert.showAndWait().ifPresent(result -> {
            if (name.getText().isBlank() || type.getText().isBlank()) {
                showInfo("Missing Details", "Please enter at least a pet name and type.");
                return;
            }
            String normalizedType = type.getText().trim();
            String image = normalizedType.equalsIgnoreCase("cat")
                    ? "/assets/img/adoption/misty.jpg"
                    : "/assets/img/adoption/charlie.jpg";
            pets.add(new Pet(name.getText().trim(), normalizedType,
                    age.getText().isBlank() ? "Age not set" : age.getText().trim(),
                    food.getText().isBlank() ? "Food not set" : food.getText().trim(),
                    "Not set", "Healthy", image));
            refreshPetRows();
        });
    }

    private void showEditPetDialog(Pet pet) {
        TextField name = new TextField(pet.name);
        TextField age = new TextField(pet.age);
        TextField food = new TextField(pet.food);
        TextField time = new TextField(pet.time);
        TextField status = new TextField(pet.status);
        VBox form = new VBox(8,
                label("Pet Name", 12, true, TEXT), name,
                label("Age", 12, true, TEXT), age,
                label("Food", 12, true, TEXT), food,
                label("Feeding Time", 12, true, TEXT), time,
                label("Health Status", 12, true, TEXT), status);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Edit Pet");
        alert.setHeaderText("Update " + pet.name);
        alert.getDialogPane().setContent(form);
        alert.showAndWait().ifPresent(result -> {
            if (!name.getText().isBlank()) pet.name = name.getText().trim();
            if (!age.getText().isBlank()) pet.age = age.getText().trim();
            if (!food.getText().isBlank()) pet.food = food.getText().trim();
            if (!time.getText().isBlank()) pet.time = time.getText().trim();
            if (!status.getText().isBlank()) pet.status = status.getText().trim();
            refreshPetRows();
        });
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private Button primaryButton(String text) {
        Button b = new Button(text);
        b.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        b.setTextFill(Color.WHITE);
        b.setPadding(new Insets(11, 14, 11, 14));
        b.setBackground(background(GREEN, 10));
        b.setBorder(border(GREEN, 10, 1));
        return b;
    }

    private Button secondaryButton(String text) {
        Button b = new Button(text);
        b.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        b.setTextFill(GREEN);
        b.setPadding(new Insets(10, 13, 10, 13));
        b.setBackground(background(Color.WHITE, 10));
        b.setBorder(border(BORDER, 10, 1));
        return b;
    }

    private Button textButton(String text) {
        Button b = new Button(text);
        b.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        b.setTextFill(GREEN);
        b.setBackground(Background.EMPTY);
        return b;
    }

    private Button smallActionButton(String text, Color bg, Color fg) {
        Button b = new Button(text);
        b.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        b.setTextFill(fg);
        b.setPadding(new Insets(8, 10, 8, 10));
        b.setBackground(background(bg, 9));
        b.setBorder(border(bg, 9, 1));
        return b;
    }

    private Label label(String text, int size, boolean bold, Color color) {
        Label l = new Label(text);
        l.setFont(Font.font("Arial", bold ? FontWeight.BOLD : FontWeight.NORMAL, size));
        l.setTextFill(color);
        l.setWrapText(true);
        return l;
    }

    private Label label(String text, int size, boolean bold, String color) {
        return label(text, size, bold, Color.web(color));
    }

    private ImageView petImage(String path, double width, double height) {
        Image img = null;
        try {
            var stream = getClass().getResourceAsStream(path);
            if (stream != null) {
                img = new Image(stream);
                stream.close();
                if (img.isError()) img = null;
            }
        } catch (Exception ignored) {
            img = null;
        }

        ImageView view = img == null ? new ImageView() : new ImageView(img);
        view.setFitWidth(width);
        view.setFitHeight(height);
        view.setPreserveRatio(false);
        view.setSmooth(true);
        view.setClip(new javafx.scene.shape.Rectangle(width, height, 12, 12));
        if (img == null) {
            StackPane placeholder = new StackPane();
            placeholder.setMinSize(width, height);
            placeholder.setPrefSize(width, height);
            placeholder.setBackground(background(PALE_GREEN, 12));
        }
        return view;
    }

    private Region spacer() {
        Region r = new Region();
        HBox.setHgrow(r, Priority.ALWAYS);
        return r;
    }

    private Background background(Color color, double radius) {
        return new Background(new BackgroundFill(color, new CornerRadii(radius), Insets.EMPTY));
    }

    private Border border(Color color, double radius, double width) {
        return new Border(new BorderStroke(color, BorderStrokeStyle.SOLID,
                new CornerRadii(radius), new BorderWidths(width)));
    }

    private Color lighten(Color color) {
        return color.interpolate(Color.WHITE, 0.88);
    }

    private static class Pet {
        String name;
        String type;
        String age;
        String food;
        String time;
        String status;
        String imagePath;

        Pet(String name, String type, String age, String food, String time,
            String status, String imagePath) {
            this.name = name;
            this.type = type;
            this.age = age;
            this.food = food;
            this.time = time;
            this.status = status;
            this.imagePath = imagePath;
        }
    }

    /*
     * Small BorderPane wrapper so this page can be integrated with the existing Sidebar.
     
     */
    private static class BorderPaneHolder extends javafx.scene.layout.BorderPane {
        final javafx.scene.layout.BorderPane root = this;

        BorderPaneHolder() {
            setBackground(new Background(new BackgroundFill(PAGE_BG, CornerRadii.EMPTY, Insets.EMPTY)));
        }
    }
}
