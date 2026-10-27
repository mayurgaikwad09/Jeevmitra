package com.techyuva.view.Rescue;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.LinkedHashMap;
import java.util.Map;

public class resMain extends Application{
   // public Scene getfromScene(Runnable backTO){

    private final String GREEN = "#21865A";
    private final String DARK = "#064E3B";
    private final String BG = "#F1F8F5";

    private BorderPane root;
    private ScrollPane scrollPane;
    private VBox sidebar;

    private final Map<String, Button> navButtons =
            new LinkedHashMap<>();
            // image add kalii
private ImageView createImageView(
        String fileName, double width, double height) {

    String path = "/assets/images/" + fileName;
    var resource = getClass().getResource(path);

    if (resource == null) {
        System.out.println("Image not found: " + path);
        return new ImageView();
    }

    ImageView view = new ImageView(
            new Image(resource.toExternalForm()));

    view.setFitWidth(width);
    view.setFitHeight(height);
    view.setPreserveRatio(true);
    view.setSmooth(true);

    return view;
}
//
private Background createBackground(String fileName) {

    String path = "/assets/images/" + fileName;
    var resource = getClass().getResource(path);

    if (resource == null) {
        System.out.println("Background not found: " + path);
        return Background.EMPTY;
    }

    Image image = new Image(resource.toExternalForm());

    BackgroundSize size = new BackgroundSize(
            100, 100, true, true, false, true);

    BackgroundImage bgImage = new BackgroundImage(
            image,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            size
    );

    return new Background(bgImage);
}
//ithparyant
    @Override
    public void start(Stage stage) {

        root = new BorderPane();
        root.setStyle("-fx-background-color:" + BG + ";");

        sidebar = createSidebar();

        scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        scrollPane.setStyle(
                "-fx-background:" + BG + ";" +
                "-fx-background-color:" + BG + ";"
        );

        root.setLeft(sidebar);
        root.setCenter(scrollPane);

        showPage("Dashboard");

        Scene scene = new Scene(root, 900, 500);

        stage.setTitle("JeevMitra - Rescue Team Dashboard");
        stage.setMinWidth(1050);
        stage.setMinHeight(650);
        stage.setScene(scene);
        stage.show();
        //return scene;

    }

    // ================= SIDEBAR =================

    private VBox createSidebar() {

        VBox side = new VBox(10);
        side.setPadding(new Insets(20, 12, 15, 12));
        side.setPrefWidth(235);
        side.setMinWidth(235);
      /*   side.setStyle(
        "-fx-background-color: #064E3B;" +
        "-fx-border-color: #064E3B;");
        */
        side.setStyle(
                "-fx-background-color: #093b2b;" +
                "-fx-border-color: #092614;"
        );

        ImageView logo = createImageView("logo.png", 100, 100);

Label appName = new Label("JeevMitra");
appName.setStyle(
        "-fx-font-size:23px;" +
        "-fx-font-weight:bold;" +
        "-fx-text-fill: #e1ece9f2;"
);

HBox logoRow = new HBox(8, logo, appName);
logoRow.setAlignment(Pos.CENTER_LEFT);

Label tagline = new Label("Care • Rescue • Protect");
tagline.setStyle("-fx-text-fill: #f8fbfa;");

VBox brand = new VBox(6, logoRow, tagline);
brand.setPadding(new Insets(10, 5, 25, 5));

side.getChildren().add(brand);
//change
        String[] pages = {
                "Dashboard",
                "Rescue Reports",
                "Case Assignment",
                "Status Tracking",
                "Doctor Coordination",
                "AI First-Aid",
                "Nearby Clinics",
                "Settings"
        };

        String[] icons = {
                "▦", "▤", "♟", "⌖",
                "✚", "✳", "●", "⚙"
        };

        for (int i = 0; i < pages.length; i++) {

            String pageName = pages[i];

            Button button = new Button(
                    icons[i] + "   " + pageName
            );

            button.setMaxWidth(Double.MAX_VALUE);
            button.setPrefHeight(46);
            button.setAlignment(Pos.CENTER_LEFT);

            styleNav(button, false);

            button.setOnAction(e -> showPage(pageName));

            navButtons.put(pageName, button);
            side.getChildren().add(button);
        }

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Label footer = new Label(
                "🐕 Together for a kinder world"
        );
        footer.setWrapText(true);
        footer.setTextFill(Color.web("WHITE"));
        footer.setPadding(new Insets(15, 5, 5, 5));

        side.getChildren().addAll(spacer, footer);

        return side;
    }

    private void styleNav(Button button, boolean active) {

        button.setStyle(
                "-fx-background-color:" +
                (active ? GREEN : "transparent") + ";" +
                "-fx-text-fill:white" +
                (active ? "white" : DARK) + ";" +
                "-fx-font-size:13px;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:9px;" +
                "-fx-padding:12px;" +
                "-fx-cursor:hand;"
        );
    }

    // ================= PAGE NAVIGATION =================

    private void showPage(String pageName) {

        for (Map.Entry<String, Button> entry
                : navButtons.entrySet()) {

            styleNav(
                    entry.getValue(),
                    entry.getKey().equals(pageName)
            );
        }

        VBox page;

        switch (pageName) {

            case "Dashboard":
                page = dashboardPage();
                break;

            case "Rescue Reports":
                page = rescueReportsPage();
                break;

            case "Case Assignment":
                page = caseAssignmentPage();
                break;

            case "Status Tracking":
                page = statusTrackingPage();
                break;

            case "Doctor Coordination":
                page = doctorPage();
                break;

            case "AI First-Aid":
                page = firstAidPage();
                break;

            case "Nearby Clinics":
                page = clinicsPage();
                break;

            case "Settings":
                page = settingsPage();
                break;

            default:
                page = dashboardPage();
        }

        scrollPane.setContent(page);
        scrollPane.setVvalue(0);
    }

    // ================= COMMON PAGE =================

    private VBox newPage(String title, String subtitle) {

        VBox page = new VBox(18);
        page.setPadding(new Insets(22));
        page.setFillWidth(true);
        page.setStyle("-fx-background-color:" + BG + ";");

        Label heading = new Label("🐾  " + title);
        heading.setFont(Font.font("Arial", 27));
        heading.setTextFill(Color.web(DARK));
        heading.setWrapText(true);

        Label sub = new Label(subtitle);
        sub.setFont(Font.font("Arial", 13));
        sub.setTextFill(Color.web("#668078"));
        sub.setWrapText(true);

        page.getChildren().addAll(heading, sub);
        return page;
    }

    private VBox card(String title) {

        VBox box = new VBox(13);
        box.setPadding(new Insets(18));
        box.setMaxWidth(Double.MAX_VALUE);
        box.setStyle(
                "-fx-background-color:white;" +
                "-fx-background-radius:14px;" +
                "-fx-border-color:#DCEAE5;" +
                "-fx-border-radius:14px;"
        );

        Label heading = new Label(title);
        heading.setFont(Font.font("Arial", 18));
        heading.setTextFill(Color.web(DARK));
        heading.setWrapText(true);

        box.getChildren().add(heading);
        return box;
    }

    private Label label(String text) {

        Label label = new Label(text);
        label.setFont(Font.font("Arial", 13));
        label.setStyle(
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#245B4B;"
        );
        return label;
    }

    private TextField field(String hint) {

        TextField field = new TextField();
        field.setPromptText(hint);
        field.setPrefHeight(40);
        field.setMaxWidth(Double.MAX_VALUE);
        field.setStyle(
                "-fx-background-color:white;" +
                "-fx-border-color:#DCEAE5;" +
                "-fx-border-radius:8px;" +
                "-fx-background-radius:8px;" +
                "-fx-padding:9px;"
        );
        return field;
    }

    private Button actionButton(String text) {

        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setStyle(
                "-fx-background-color:" + GREEN + ";" +
                "-fx-text-fill:white;" +
                "-fx-font-size:13px;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:8px;" +
                "-fx-padding:11px;" +
                "-fx-cursor:hand;"
        );
        return button;
    }

    private Label info(String text) {

        Label label = new Label(text);
        label.setWrapText(true);
        label.setTextFill(Color.web("#365D50"));
        label.setPadding(new Insets(6, 2, 6, 2));
        return label;
    }

    private void alert(String message) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("JeevMitra");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // ================= DASHBOARD =================

    private VBox dashboardPage() {

        VBox page = newPage(
                "Rescue Team Dashboard",
                "Helping animals in need, one rescue at a time."
        );
        page.setBackground(createBackground("rescue-bg.jpg"));

        GridPane stats = new GridPane();
        stats.setHgap(12);
        stats.setVgap(12);

        String[][] values = {
                {"▤ Total Reports", "128", "#E5F5EA"},
                {"⚠ Critical Cases", "12", "#FFF0F0"},
                {"◷ In Progress", "24", "#EAF5FF"},
                {"✔ Resolved Today", "18", "#E5F5EA"}
        };

        for (int i = 0; i < values.length; i++) {

            VBox stat = new VBox(12);
            stat.setPadding(new Insets(16));
            stat.setMinHeight(105);
            stat.setStyle(
                    "-fx-background-color:" + values[i][2] + ";" +
                    "-fx-background-radius:12px;" +
                    "-fx-border-color:#DCEAE5;" +
                    "-fx-border-radius:12px;"
            );

            Label title = label(values[i][0]);

            Label number = new Label(values[i][1]);
            number.setFont(Font.font("Arial", 28));
            number.setTextFill(Color.web(GREEN));

            stat.getChildren().addAll(title, number);

            stats.add(stat, i, 0);
            GridPane.setHgrow(stat, Priority.ALWAYS);
        }

        for (int i = 0; i < 4; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(25);
            cc.setHgrow(Priority.ALWAYS);
            stats.getColumnConstraints().add(cc);
        }
        //add photo
        VBox animalPhotos = card("Animals Needing Our Care");

        ImageView dogImage = createImageView("dog.png", 100, 90);
        ImageView catImage = createImageView("cat.png", 100, 90);

        VBox dogBox = new VBox(8, dogImage, label("Rescue Dogs"));
        VBox catBox = new VBox(8, catImage, label("Rescue Cats"));

        dogBox.setAlignment(Pos.CENTER);
        catBox.setAlignment(Pos.CENTER);

        HBox animalRow = new HBox(25, dogBox, catBox);
        animalRow.setAlignment(Pos.CENTER_LEFT);

        animalPhotos.getChildren().add(animalRow);

        // ith
        VBox cases = card("⚠  Priority Rescue Cases");

        GridPane table = new GridPane();
        table.setHgap(14);
        table.setVgap(15);
        table.setPadding(new Insets(8));

        String[][] data = {
                {"Case ID", "Animal", "Location", "Priority", "Status", "Team"},
                {"RM-1042", "Injured Dog", "Kothrud", "Critical", "New", "Alpha"},
                {"RM-1041", "Stray Cat", "Hadapsar", "High", "In Progress", "Bravo"},
                {"RM-1038", "Injured Cow", "Wagholi", "Medium", "New", "Charlie"},
                {"RM-1035", "Sick Dog", "Sinhagad Rd", "High", "In Progress", "Bravo"},
                {"RM-1031", "Abandoned Kitten", "Baner", "Medium", "New", "Alpha"}
        };

        for (int r = 0; r < data.length; r++) {
            for (int c = 0; c < data[r].length; c++) {

                Label cell = new Label(data[r][c]);
                cell.setWrapText(true);

                if (r == 0) {
                    cell.setStyle(
                            "-fx-font-weight:bold;" +
                            "-fx-text-fill:" + GREEN + ";"
                    );
                } else {
                    cell.setTextFill(Color.web("#365D50"));
                }

                table.add(cell, c, r);
            }
        }

        ScrollPane tableScroll = new ScrollPane(table);
        tableScroll.setFitToWidth(true);
        tableScroll.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED
        );
        tableScroll.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-background:transparent;"
        );

        cases.getChildren().add(tableScroll);

        VBox tracking = card("Rescue Status Tracking");
        tracking.getChildren().addAll(
                info("✔  Reported"),
                info("●  Team Assigned"),
                info("🚑  Rescue In Progress"),
                info("♡  Treatment Pending"),
                actionButton("View All Tracking")
        );

        tracking.getChildren().get(5).setOnMouseClicked(e ->
                showPage("Status Tracking")
        );

        HBox middle = new HBox(15, cases, tracking);
        HBox.setHgrow(cases, Priority.ALWAYS);
        tracking.setPrefWidth(260);
        tracking.setMinWidth(220);

        VBox aid = card("✳  AI First-Aid Guidance");
        aid.getChildren().addAll(
                info("Keep the animal calm."),
                info("Avoid unsafe handling."),
                info("Contact a qualified veterinarian."),
                actionButton("View First-Aid")
        );

        aid.getChildren().get(4).setOnMouseClicked(e ->
                showPage("AI First-Aid")
        );

        VBox doctors = card("Doctor Coordination");
        doctors.getChildren().addAll(
                info("Available doctors: 8 (sample data)"),
                info("On-call doctors: 3 (sample data)"),
                actionButton("Contact Doctor")
        );

        doctors.getChildren().get(3).setOnMouseClicked(e ->
                showPage("Doctor Coordination")
        );

        VBox clinics = card("Nearby Veterinary Clinics");
        clinics.getChildren().addAll(
                info("Paw Care Veterinary Clinic — Kothrud"),
                info("Happy Tails Animal Hospital — Hadapsar"),
                info("Vets for Life Clinic — Wagholi"),
                actionButton("View All Clinics")
        );

        clinics.getChildren().get(4).setOnMouseClicked(e ->
                showPage("Nearby Clinics")
        );

        VBox rightBottom = new VBox(15, doctors, clinics);
        HBox bottom = new HBox(15, aid, rightBottom);
        HBox.setHgrow(aid, Priority.ALWAYS);
        HBox.setHgrow(rightBottom, Priority.ALWAYS);

       // page.getChildren().addAll(stats, middle, bottom);
        page.getChildren().addAll(
        stats,
        animalPhotos,
        middle,
        bottom);
       return page;
    }

    // ================= RESCUE REPORTS =================

    private VBox rescueReportsPage() {

        VBox page = newPage(
                "Rescue Reports",
                "Register a rescue case and record animal details."
        );

        VBox form = card("Create Rescue Report");

        TextField location = field("Enter rescue location");

        ComboBox<String> animal = new ComboBox<>();
        animal.getItems().addAll(
                "Dog", "Cat", "Cow", "Bird", "Rabbit", "Other"
        );
        animal.setPromptText("Select animal");
        animal.setMaxWidth(Double.MAX_VALUE);

        ComboBox<String> priority = new ComboBox<>();
        priority.getItems().addAll(
                "Critical", "High", "Medium", "Low"
        );
        priority.setPromptText("Select priority");
        priority.setMaxWidth(Double.MAX_VALUE);

        TextArea description = new TextArea();
        description.setPromptText("Describe the animal's condition");
        description.setWrapText(true);
        description.setPrefRowCount(4);

        Button submit = actionButton("Submit Rescue Report");

        submit.setOnAction(e -> {

            if (location.getText().isBlank()
                    || animal.getValue() == null
                    || priority.getValue() == null) {

                alert("Please enter location, animal type and priority.");
                return;
            }

            alert("Demo report created successfully.\n"
                    + "Animal: " + animal.getValue()
                    + "\nLocation: " + location.getText()
                    + "\nPriority: " + priority.getValue()
                    + "\n\nDatabase is not connected yet.");
        });

        form.getChildren().addAll(
                label("Rescue Location"), location,
                label("Animal Type"), animal,
                label("Priority"), priority,
                label("Description"), description,
                submit
        );

        page.getChildren().add(form);
        return page;
    }

    // ================= CASE ASSIGNMENT =================

    private VBox caseAssignmentPage() {

        VBox page = newPage(
                "Case Assignment",
                "Assign a rescue team to an animal rescue case."
        );

        VBox form = card("Assign Rescue Team");

        TextField caseId = field("Enter case ID, e.g. RM-1042");

        ComboBox<String> team = new ComboBox<>();
        team.getItems().addAll(
                "Team Alpha", "Team Bravo", "Team Charlie"
        );
        team.setPromptText("Select rescue team");
        team.setMaxWidth(Double.MAX_VALUE);

        ComboBox<String> priority = new ComboBox<>();
        priority.getItems().addAll(
                "Critical", "High", "Medium", "Low"
        );
        priority.setPromptText("Select priority");
        priority.setMaxWidth(Double.MAX_VALUE);

        Button assign = actionButton("Assign Team");

        assign.setOnAction(e -> {

            if (caseId.getText().isBlank()
                    || team.getValue() == null
                    || priority.getValue() == null) {

                alert("Please complete all fields.");
                return;
            }

            alert("Demo assignment successful.\nCase: "
                    + caseId.getText()
                    + "\nTeam: " + team.getValue()
                    + "\nPriority: " + priority.getValue());
        });

        form.getChildren().addAll(
                label("Case ID"), caseId,
                label("Rescue Team"), team,
                label("Priority"), priority,
                assign
        );

        page.getChildren().add(form);
        return page;
    }

    // ================= STATUS TRACKING =================

    private VBox statusTrackingPage() {

        VBox page = newPage(
                "Status Tracking",
                "Track the progress of a rescue case."
        );

        VBox form = card("Update Case Status");

        TextField caseId = field("Enter case ID");

        ComboBox<String> status = new ComboBox<>();
        status.getItems().addAll(
                "Reported",
                "Team Assigned",
                "Rescue In Progress",
                "Treatment",
                "Resolved"
        );
        status.setPromptText("Select status");
        status.setMaxWidth(Double.MAX_VALUE);

        Label result = info("No status update yet.");

        Button update = actionButton("Update Status");

        update.setOnAction(e -> {

            if (caseId.getText().isBlank()
                    || status.getValue() == null) {

                alert("Please enter case ID and select status.");
                return;
            }

            result.setText(
                    "Case ID: " + caseId.getText()
                    + "\nCurrent Status: " + status.getValue()
            );
        });

        form.getChildren().addAll(
                info("✔ 1. Reported"),
                info("● 2. Team Assigned"),
                info("🚑 3. Rescue In Progress"),
                info("♡ 4. Treatment"),
                info("✔ 5. Resolved"),
                new Separator(),
                label("Case ID"), caseId,
                label("New Status"), status,
                update, result
        );

        page.getChildren().add(form);
        return page;
    }

    // ================= DOCTOR COORDINATION =================

    private VBox doctorPage() {

        VBox page = newPage(
                "Doctor Coordination",
                "Prepare a request for veterinary assistance."
        );

        VBox form = card("Contact Veterinarian");

        TextField caseId = field("Enter rescue case ID");

        ComboBox<String> doctor = new ComboBox<>();
        doctor.getItems().addAll(
                "Dr. Patil", "Dr. Sharma", "Dr. Joshi"
        );
        doctor.setPromptText("Select veterinarian");
        doctor.setMaxWidth(Double.MAX_VALUE);

        TextArea symptoms = new TextArea();
        symptoms.setPromptText("Describe the animal's condition");
        symptoms.setWrapText(true);
        symptoms.setPrefRowCount(3);

        Button contact = actionButton("Prepare Doctor Request");

        contact.setOnAction(e -> {

            if (caseId.getText().isBlank()
                    || doctor.getValue() == null) {

                alert("Enter case ID and select a veterinarian.");
                return;
            }

            alert("Doctor request prepared.\nDoctor: "
                    + doctor.getValue()
                    + "\nCase: " + caseId.getText()
                    + "\n\nNo actual message was sent.");
        });

        form.getChildren().addAll(
                info("Doctor directory contains sample entries."),
                label("Case ID"), caseId,
                label("Veterinarian"), doctor,
                label("Animal Condition"), symptoms,
                contact
        );

        page.getChildren().add(form);
        return page;
    }

    // ================= AI FIRST AID =================

    private VBox firstAidPage() {

        VBox page = newPage(
                "AI First-Aid",
                "General safety guidance for animal rescue situations."
        );

        VBox form = card("First-Aid Guidance");
       // image add
        ImageView firstAidImage = createImageView("first-aid.png", 240, 150);

        HBox imageRow = new HBox(firstAidImage);
        imageRow.setAlignment(Pos.CENTER);

        form.getChildren().add(imageRow);

        form.getChildren().addAll(
                info("🐾 1. Stay calm and observe from a safe distance."),
                info("🛡 2. Avoid touching frightened or injured animals."),
                info("📍 3. Record the rescue location."),
                info("☎ 4. Contact a qualified veterinarian or rescue team."),
                info("❤️ 5. Follow professional instructions.")
        );

        Label warning = info(
                "Important: This page does not diagnose an animal "
                + "or connect to an AI model."
        );
        warning.setStyle(
                "-fx-background-color:#FFF4D6;" +
                "-fx-text-fill:#805600;" +
                "-fx-padding:12px;" +
                "-fx-background-radius:8px;"
        );

        form.getChildren().add(warning);

        Button contact = actionButton("View Doctor Coordination");
        contact.setOnAction(e -> showPage("Doctor Coordination"));

        form.getChildren().add(contact);
        page.getChildren().add(form);

        return page;
    }

    // ================= NEARBY CLINICS =================

    private VBox clinicsPage() {

        VBox page = newPage(
                "Nearby Clinics",
                "Sample veterinary clinic directory. Verify details before visiting."
        );

        VBox list = card("Veterinary Clinic Directory");

        String[][] clinics = {
                {"Paw Care Veterinary Clinic", "Kothrud"},
                {"Happy Tails Animal Hospital", "Hadapsar"},
                {"Vets for Life Clinic", "Wagholi"},
                {"City Veterinary Care", "Baner"}
        };

        for (String[] clinic : clinics) {

            VBox item = new VBox(6);
            item.setPadding(new Insets(12));
            item.setStyle(
                    "-fx-background-color:#F1F8F5;" +
                    "-fx-background-radius:10px;"
            );

            Label name = label("✚  " + clinic[0]);
            Label area = info("Area: " + clinic[1]);

            Button details = actionButton("View Details");
            details.setOnAction(e ->
                    alert(clinic[0] + "\nArea: " + clinic[1]
                            + "\n\nAdd verified address and phone number.")
            );

            item.getChildren().addAll(name, area, details);
            list.getChildren().add(item);
        }

        page.getChildren().add(list);
        return page;
    }

    // ================= SETTINGS =================

    private VBox settingsPage() {

        VBox page = newPage(
                "Settings",
                "Configure basic rescue team preferences."
        );

        VBox form = card("Team Settings");

        TextField teamName = field("Rescue Team");
        teamName.setText("Rescue Team");

        TextField member = field("Enter team member name");

        ComboBox<String> notifications = new ComboBox<>();
        notifications.getItems().addAll("Enabled", "Disabled");
        notifications.setValue("Enabled");
        notifications.setMaxWidth(Double.MAX_VALUE);

        Button save = actionButton("Save Settings");

        save.setOnAction(e -> {

            if (teamName.getText().isBlank()) {
                alert("Team name cannot be empty.");
                return;
            }

            alert("Settings validated.\nTeam: "
                    + teamName.getText()
                    + "\nMember: " + member.getText()
                    + "\nNotifications: " + notifications.getValue()
                    + "\n\nSettings are not stored permanently.");
        });

        form.getChildren().addAll(
                label("Team Name"), teamName,
                label("Member Name"), member,
                label("Notifications"), notifications,
                save
        );

        page.getChildren().add(form);
        return page;
    }



}
