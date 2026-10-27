package com.techyuva.view.Admin;

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


public class AdminDashboard extends Application {

    private Stage primaryStage;
    private BorderPane mainLayout;

    private final String green = "#075747";
    private final String darkGreen = "#034638";
    private final String background = "#F4FAF8";

    @Override
    public void start(Stage stage) {
        primaryStage = stage;

        mainLayout = new BorderPane();
        mainLayout.setLeft(createSidebar());
        mainLayout.setCenter(createDashboard());
        mainLayout.setStyle("-fx-background-color: " + background + ";");

        Scene scene = new Scene(mainLayout, 1400, 800);

        primaryStage.setTitle("JeevMitra - Admin Dashboard");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(12);
        sidebar.setPadding(new Insets(25, 15, 20, 15));
        sidebar.setPrefWidth(240);
        sidebar.setMinWidth(220);
        sidebar.setStyle("-fx-background-color: " + green + ";");

        ImageView logo = loadImage(
                "/assets/imagaes/logo.jpeg", 65, 65);

        Label appName = new Label("JeevMitra");
        appName.setTextFill(Color.WHITE);
        appName.setFont(Font.font("Arial", 25));

        Label adminLabel = new Label("ADMIN PANEL");
        adminLabel.setTextFill(Color.web("#C8E8DF"));
        adminLabel.setFont(Font.font("Arial", 11));

        VBox branding = new VBox(8, logo, appName, adminLabel);
        branding.setAlignment(Pos.CENTER);
        branding.setPadding(new Insets(0, 0, 25, 0));

        sidebar.getChildren().add(branding);

        sidebar.getChildren().addAll(
                menuItem("Admin", "/assets/imagaes/Admin.jpeg"),
                menuItem("Users", "/assets/imagaes/Users.jpeg"),
                menuItem("Veterinarians", "/assets/imagaes/Veterian.jpeg"),
                menuItem("RescueReports", "/assets/imagaes/Reports.jpeg"),
                menuItem("Medical Reports", "/assets/imagaes/MedicalReports.jpeg"),
                menuItem("Complaints", "/assets/imagaes/Complaint.jpeg"),
                menuItem("Settings", "/assets/imagaes/Settings.jpeg")
        );

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        sidebar.getChildren().add(spacer);

        sidebar.getChildren().add(
                menuItem("Logout", "/assets/imagaes/logout.jpeg"));

        return sidebar;
    }

    private HBox menuItem(String name, String imagePath) {
        ImageView imageView = loadImage(imagePath, 25, 25);

        String displayName = name.equals("RescueReports")
                ? "Rescue Reports"
                : name;

        Label label = new Label(displayName);
        label.setTextFill(Color.WHITE);
        label.setFont(Font.font("Arial", 14));

        HBox item = new HBox(15, imageView, label);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(12, 12, 12, 15));
        item.setMaxWidth(Double.MAX_VALUE);
        item.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;"
        );

        item.setOnMouseEntered(e -> item.setStyle(
                "-fx-background-color: " + darkGreen + ";" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;"
        ));

        item.setOnMouseExited(e -> item.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;"
        ));

        item.setOnMouseClicked(e -> {
            if (name.equals("Logout")) {
                logout();
            } else {
                showPage(name);
            }
        });

        return item;
    }

    private void showPage(String pageName) {
        switch (pageName) {
            case "Admin":
                mainLayout.setCenter(createDashboard());
                break;

            case "Users":
                mainLayout.setCenter(
                        new UsersPage(() -> showPage("Admin")));
                break;

            case "Veterinarians":
                mainLayout.setCenter(
                        new VeterinariansPage(() -> showPage("Admin")));
                break;

            case "Reports":
            case "RescueReports":
                mainLayout.setCenter(
                        new ReportsPage(() -> showPage("Admin")));
                break;

            case "Medical Reports":
                mainLayout.setCenter(
                        new MedicalReportsPage(() -> showPage("Admin")));
                break;

            case "Complaints":
                mainLayout.setCenter(
                        new ComplaintsPage(() -> showPage("Admin")));
                break;

            case "Settings":
                mainLayout.setCenter(
                        new SettingsPage(() -> showPage("Admin")));
                break;

            default:
                mainLayout.setCenter(createDashboard());
                break;
        }
    }

    private VBox createDashboard() {
        VBox dashboard = new VBox(22);
        dashboard.setPadding(new Insets(30));
        dashboard.setStyle(
                "-fx-background-color: " + background + ";");

        Label heading = new Label("Admin Dashboard");
        heading.setFont(Font.font("Arial", 29));
        heading.setTextFill(Color.web(darkGreen));

        Label subtitle = new Label(
                "Welcome to JeevMitra Animal Welfare Management System");
        subtitle.setStyle(
                "-fx-font-size: 14px; -fx-text-fill: #64748B;");

        VBox headingBox = new VBox(7, heading, subtitle);

        HBox stats = new HBox(
                18,
                statCard("Total Users", "245", "#E0F2EE"),
                statCard("Veterinarians", "48", "#E0E7FF"),
                statCard("Rescue Reports", "32", "#FEF3C7"),
                statCard("Appointments", "120", "#FCE7F3")
        );

        Label recentTitle = new Label("Recent Rescue Reports");
        recentTitle.setFont(Font.font("Arial", 20));
        recentTitle.setTextFill(Color.web(darkGreen));

        VBox recentReports = new VBox(
                12,
                reportRow("RR101", "Injured Dog", "Pending"),
                reportRow("RR102", "Trapped Cat", "In Progress"),
                reportRow("RR103", "Injured Cow", "Resolved")
        );

        VBox recentCard = new VBox(18, recentTitle, recentReports);
        recentCard.setPadding(new Insets(22));
        recentCard.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: #E2E8F0;" +
                "-fx-border-radius: 12;"
        );

        Label quickTitle = new Label("Quick Actions");
        quickTitle.setFont(Font.font("Arial", 20));
        quickTitle.setTextFill(Color.web(darkGreen));

        HBox quickActions = new HBox(
                15,
                quickAction("Manage Users", "Users"),
                quickAction("Manage Veterinarians", "Veterinarians"),
                quickAction("View Reports", "RescueReports"),
                quickAction("Medical Reports", "Medical Reports")
        );

        VBox quickBox = new VBox(15, quickTitle, quickActions);
        quickBox.setPadding(new Insets(22));
        quickBox.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: #E2E8F0;" +
                "-fx-border-radius: 12;"
        );

        dashboard.getChildren().addAll(
                headingBox, stats, recentCard, quickBox);

        ScrollPane scrollPane = new ScrollPane(dashboard);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle(
                "-fx-background: " + background + ";" +
                "-fx-background-color: " + background + ";"
        );

        VBox wrapper = new VBox(scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        return wrapper;
    }

    private VBox statCard(
            String title, String value, String cardColor) {

        Label titleLabel = new Label(title);
        titleLabel.setStyle(
                "-fx-font-size: 14px; -fx-text-fill: #64748B;");

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Arial", 27));
        valueLabel.setTextFill(Color.web(darkGreen));

        VBox card = new VBox(15, titleLabel, valueLabel);
        card.setPadding(new Insets(22));
        card.setMinWidth(150);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: " + cardColor + ";" +
                "-fx-border-radius: 12;" +
                "-fx-border-width: 2;"
        );

        HBox.setHgrow(card, Priority.ALWAYS);

        return card;
    }

    private HBox reportRow(
            String id, String description, String status) {

        Label idLabel = new Label(id);
        idLabel.setStyle(
                "-fx-font-weight: bold; -fx-text-fill: " + darkGreen + ";");

        Label descriptionLabel = new Label(description);

        Label statusLabel = new Label(status);
        statusLabel.setStyle(
                "-fx-background-color: #E0F2EE;" +
                "-fx-text-fill: " + darkGreen + ";" +
                "-fx-padding: 6 10;" +
                "-fx-background-radius: 10;"
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox row = new HBox(
                15, idLabel, descriptionLabel, spacer, statusLabel);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12));
        row.setStyle(
                "-fx-background-color: #F8FAFC;" +
                "-fx-background-radius: 8;"
        );

        return row;
    }

    // QUICK ACTION BUTTON
    private Button quickAction(String title, String pageName) {
        Button button = new Button(title);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setMinHeight(55);
        button.setStyle(
                "-fx-background-color: " + green + ";" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 13px;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;"
        );

        button.setOnAction(e -> showPage(pageName));
        HBox.setHgrow(button, Priority.ALWAYS);

        return button;
    }

    // IMAGE LOADING
private ImageView loadImage(String imagePath, double width, double height) {
    ImageView imageView = new ImageView();

    try {
        java.nio.file.Path path = java.nio.file.Paths.get(
            "D:/SuperX/TechYuva/jeevmitra/src/main/resources"
                + imagePath.replace("/assets", "/assets")
        );

        if (imagePath.startsWith("/")) {
            path = java.nio.file.Paths.get(
                "D:/SuperX/TechYuva/jeevmitra/src/main/resources"
                    + imagePath
            );
        }

        if (java.nio.file.Files.exists(path)) {
            imageView.setImage(new Image(path.toUri().toString()));
        } else {
            System.out.println("FILE NOT FOUND: " + path);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    imageView.setFitWidth(width);
    imageView.setFitHeight(height);
    imageView.setPreserveRatio(true);
    return imageView;
}

    private void logout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Are you sure you want to logout?");
        alert.setContentText("You will exit the Admin Dashboard.");

        alert.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                primaryStage.close();
            }
        });
    }
}