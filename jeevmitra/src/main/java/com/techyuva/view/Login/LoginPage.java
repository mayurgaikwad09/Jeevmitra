package com.techyuva.view.Login;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class LoginPage extends Application {

    public static Stage primStage;
    public Scene homeScene;

    private final Color darkGreen = Color.web("#10543F");
    private final Color green = Color.web("#16815F");
    private final Color paleGreen = Color.web("#EAF7F0");
    private final Color borderGreen = Color.web("#D4E9DF");

    @Override
    public void start(Stage stage) {
        primStage = stage;

        VBox userCard = createRoleCard(
                "/assets/Image/user.png",
                "User",
                "Adoption, appointments and pet care",
                () -> {
                    user us = new user();
                    Runnable back = () -> primStage.setScene(homeScene);
                    primStage.setScene(us.getfromScene(back));
                }
        );

        VBox doctorCard = createRoleCard(
                "/assets/Image/images.png",
                "Doctor",
                "Health checks and appointments",
                () -> {
                    DoctorPage doctor = new DoctorPage();
                    Runnable back = () -> primStage.setScene(homeScene);
                    primStage.setScene(doctor.getfromScene(back));
                }
        );

        VBox rescueCard = createRoleCard(
                "/assets/Image/res.jpg",
                "Rescue",
                "Manage rescue reports and cases",
                () -> {
                    RescuePage rescue = new RescuePage();
                    Runnable back = () -> primStage.setScene(homeScene);
                    primStage.setScene(rescue.getfromScene(back));
                }
        );

        VBox adminCard = createRoleCard(
                "/assets/Image/admin.png",
                "Admin",
                "Manage users and platform activities",
                () -> {
                    AdminPage admin = new AdminPage();
                    Runnable back = () -> primStage.setScene(homeScene);
                    primStage.setScene(admin.getfromScene(back));
                }
        );

        HBox firstRow = new HBox(18, userCard, doctorCard);
        firstRow.setAlignment(Pos.CENTER);

        HBox secondRow = new HBox(18, rescueCard, adminCard);
        secondRow.setAlignment(Pos.CENTER);

        VBox cards = new VBox(18, firstRow, secondRow);
        cards.setAlignment(Pos.CENTER);
        cards.setMaxWidth(620);

        Text title = createText("JeevMitra", 36, true, Color.WHITE);
        Text subtitle = createText(
                "Animal Rescue & Care", 16, false, Color.web("#E0F4E9"));
        Text instruction = createText(
                "Choose your login type", 22, true, Color.WHITE);

        VBox heading = new VBox(8, title, subtitle, instruction);
        heading.setAlignment(Pos.CENTER);

        VBox content = new VBox(25, heading, cards);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(28));
        content.setMaxWidth(760);
        content.setBackground(makeBackground(
                Color.web("#26292B", 0.88), 24));
        content.setBorder(makeBorder(Color.web("#6DAF94"), 24, 1.2));

        // Load background image from src/main/resources/assets/Image/imm.jpeg
        Image bgImage = loadImage("/assets/Image/imm.jpeg");

        ImageView bgView = new ImageView();
        bgView.setPreserveRatio(false);
        bgView.setSmooth(true);

        StackPane root = new StackPane();
        root.setBackground(makeBackground(paleGreen, 0));

        if (bgImage != null) {
            bgView.setImage(bgImage);
            bgView.fitWidthProperty().bind(root.widthProperty());
            bgView.fitHeightProperty().bind(root.heightProperty());
            root.getChildren().add(bgView);
        }

        Region overlay = new Region();
        overlay.setBackground(makeBackground(
                Color.web("#082F25", 0.30), 0));

        StackPane center = new StackPane(overlay, content);
        StackPane.setAlignment(content, Pos.CENTER);
        root.getChildren().add(center);

        center.prefWidthProperty().bind(root.widthProperty());
        center.prefHeightProperty().bind(root.heightProperty());

        homeScene = new Scene(root, 1280, 760);
        stage.setTitle("JeevMitra - Choose Login");
        stage.setScene(homeScene);
        stage.setMaximized(true);
        stage.show();
    }

    private VBox createRoleCard(
            String imagePath,
            String role,
            String description,
            Runnable action) {

        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(18, 16, 16, 16));
        card.setPrefSize(270, 205);
        card.setMinSize(245, 190);
        card.setBackground(makeBackground(Color.WHITE, 18));
        card.setBorder(makeBorder(borderGreen, 18, 1.2));

        Image image = loadImage(imagePath);

        if (image != null) {
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(72);
            imageView.setFitHeight(72);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            card.getChildren().add(imageView);
        } else {
            // Fallback icon is shown if an image is missing.
            StackPane iconCircle = new StackPane();
            Circle circle = new Circle(36, paleGreen);
            Text iconText = createText(
                    role.substring(0, 1), 28, true, darkGreen);
            iconCircle.getChildren().addAll(circle, iconText);
            card.getChildren().add(iconCircle);
        }

        Text title = createText(role, 22, true, darkGreen);
        Text desc = createText(
                description, 12, false, Color.web("#55756A"));
        desc.setWrappingWidth(225);
        desc.setTextAlignment(TextAlignment.CENTER);

        Button continueButton = new Button("Continue as " + role + "  →");
        continueButton.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        continueButton.setTextFill(Color.WHITE);
        continueButton.setPadding(new Insets(10, 14, 10, 14));
        continueButton.setMaxWidth(Double.MAX_VALUE);
        continueButton.setBackground(makeBackground(green, 10));
        continueButton.setBorder(makeBorder(green, 10, 1));
        continueButton.setOnAction(e -> action.run());

        card.getChildren().addAll(title, desc, continueButton);
        HBox.setHgrow(card, Priority.ALWAYS);

        return card;
    }

    /**
     * Loads an image from the Java classpath.
     * Put images inside src/main/resources/assets/Image/
     */
    private Image loadImage(String path) {
        try {
            var resourceUrl = getClass().getResource(path);

            if (resourceUrl == null) {
                System.out.println("IMAGE NOT FOUND: " + path);
                return null;
            }

            Image image = new Image(resourceUrl.toExternalForm());

            if (image.isError()) {
                System.out.println("IMAGE ERROR: " + path);
                return null;
            }

            System.out.println("IMAGE LOADED: " + path);
            return image;

        } catch (Exception e) {
            System.out.println("IMAGE LOADING ERROR: " + path);
            e.printStackTrace();
            return null;
        }
    }

    private Text createText(
            String value,
            int size,
            boolean bold,
            Color color) {

        Text text = new Text(value);
        text.setFont(Font.font(
                "Arial",
                bold ? FontWeight.BOLD : FontWeight.NORMAL,
                size));
        text.setFill(color);
        return text;
    }

    private Background makeBackground(Color color, double radius) {
        return new Background(new BackgroundFill(
                color, new CornerRadii(radius), Insets.EMPTY));
    }

    private Border makeBorder(Color color, double radius, double width) {
        return new Border(new BorderStroke(
                color,
                BorderStrokeStyle.SOLID,
                new CornerRadii(radius),
                new BorderWidths(width)));
    }
}
