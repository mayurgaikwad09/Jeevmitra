
package com.techyuva.view.Admin;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class SettingsPage extends VBox {

    private final String green = "#075747";
    private final String darkGreen = "#034638";
    private final String background = "#F4FAF8";

    private TextField nameField;
    private TextField emailField;
    private TextField phoneField;

    private CheckBox rescueNotifications;
    private CheckBox complaintNotifications;
    private CheckBox appointmentNotifications;
    private CheckBox emailNotifications;

    private ComboBox<String> themeBox;
    private ComboBox<String> defaultStatusBox;

    public SettingsPage(Runnable backAction) {

        setSpacing(20);
        setPadding(new Insets(25));
        setStyle("-fx-background-color: " + background + ";");

        Label title = new Label("Settings");
        title.setStyle(
            "-fx-font-size: 28px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + darkGreen + ";"
        );

        Label subtitle = new Label(
            "Manage your JeevMitra admin account and application preferences."
        );
        subtitle.setStyle(
            "-fx-font-size: 13px; -fx-text-fill: #64748B;"
        );

        Button backButton = new Button("← Back to Admin Dashboard");
        backButton.setStyle(buttonStyle());
        backButton.setOnAction(e -> backAction.run());

        VBox heading = new VBox(8, title, subtitle, backButton);

        // ADMIN PROFILE
        nameField = new TextField("Admin");
        emailField = new TextField("admin@jeevmitra.com");
        phoneField = new TextField("");

        phoneField.setPromptText("Enter phone number");

        GridPane profileGrid = new GridPane();
        profileGrid.setHgap(15);
        profileGrid.setVgap(12);

        profileGrid.addRow(0, new Label("Admin Name:"), nameField);
        profileGrid.addRow(1, new Label("Email Address:"), emailField);
        profileGrid.addRow(2, new Label("Phone Number:"), phoneField);

        Button saveProfileButton = new Button("Save Profile");
        saveProfileButton.setStyle(buttonStyle());
        saveProfileButton.setOnAction(e -> saveProfile());

        VBox profileCard = createCard(
            "Admin Profile",
            "Update your administrator account details.",
            profileGrid,
            saveProfileButton
        );

        // NOTIFICATION SETTINGS
        rescueNotifications = new CheckBox(
            "New rescue report notifications"
        );
        complaintNotifications = new CheckBox(
            "New complaint notifications"
        );
        appointmentNotifications = new CheckBox(
            "Appointment notifications"
        );
        emailNotifications = new CheckBox(
            "Email notifications"
        );

        rescueNotifications.setSelected(true);
        complaintNotifications.setSelected(true);
        appointmentNotifications.setSelected(true);
        emailNotifications.setSelected(false);

        VBox notificationOptions = new VBox(
            12,
            rescueNotifications,
            complaintNotifications,
            appointmentNotifications,
            emailNotifications
        );

        Button saveNotificationsButton = new Button(
            "Save Notifications"
        );
        saveNotificationsButton.setStyle(buttonStyle());
        saveNotificationsButton.setOnAction(e ->
            showMessage(
                "Notifications",
                "Notification preferences saved for this session."
            )
        );

        VBox notificationCard = createCard(
            "Notifications",
            "Choose which notifications you want to receive.",
            notificationOptions,
            saveNotificationsButton
        );

        // SECURITY SETTINGS
        PasswordField currentPassword = new PasswordField();
        currentPassword.setPromptText("Current password");

        PasswordField newPassword = new PasswordField();
        newPassword.setPromptText("New password");

        PasswordField confirmPassword = new PasswordField();
        confirmPassword.setPromptText("Confirm new password");

        GridPane securityGrid = new GridPane();
        securityGrid.setHgap(15);
        securityGrid.setVgap(12);

        securityGrid.addRow(
            0, new Label("Current Password:"), currentPassword
        );
        securityGrid.addRow(
            1, new Label("New Password:"), newPassword
        );
        securityGrid.addRow(
            2, new Label("Confirm Password:"), confirmPassword
        );

        Button changePasswordButton = new Button("Change Password");
        changePasswordButton.setStyle(buttonStyle());

        changePasswordButton.setOnAction(e -> {

            if (currentPassword.getText().isEmpty()
                || newPassword.getText().isEmpty()
                || confirmPassword.getText().isEmpty()) {

                showMessage(
                    "Missing Information",
                    "Please fill in all password fields."
                );
                return;
            }

            if (newPassword.getText().length() < 8) {
                showMessage(
                    "Invalid Password",
                    "New password must contain at least 8 characters."
                );
                return;
            }

            if (!newPassword.getText().equals(
                    confirmPassword.getText())) {

                showMessage(
                    "Password Mismatch",
                    "New password and confirm password do not match."
                );
                return;
            }

            showMessage(
                "Demo Only",
                "Password validation succeeded. Actual password changes "
                + "require authentication and backend integration."
            );

            currentPassword.clear();
            newPassword.clear();
            confirmPassword.clear();
        });

        VBox securityCard = createCard(
            "Security",
            "Password validation demo. No real password is changed.",
            securityGrid,
            changePasswordButton
        );

        // APPLICATION PREFERENCES
        themeBox = new ComboBox<>();
        themeBox.getItems().addAll(
            "Green Theme",
            "Light Theme",
            "Dark Theme"
        );
        themeBox.setValue("Green Theme");

        defaultStatusBox = new ComboBox<>();
        defaultStatusBox.getItems().addAll(
            "Pending",
            "In Progress"
        );
        defaultStatusBox.setValue("Pending");

        GridPane preferencesGrid = new GridPane();
        preferencesGrid.setHgap(15);
        preferencesGrid.setVgap(12);

        preferencesGrid.addRow(
            0, new Label("Theme Preference:"), themeBox
        );
        preferencesGrid.addRow(
            1, new Label("Default Rescue Status:"), defaultStatusBox
        );

        Button savePreferencesButton = new Button(
            "Save Preferences"
        );
        savePreferencesButton.setStyle(buttonStyle());

        savePreferencesButton.setOnAction(e ->
            showMessage(
                "Preferences",
                "Theme: " + themeBox.getValue()
                + "\nDefault Rescue Status: "
                + defaultStatusBox.getValue()
                + "\n\nThese preferences are currently for demonstration."
            )
        );

        VBox preferencesCard = createCard(
            "Application Preferences",
            "Choose your preferred theme and default rescue status.",
            preferencesGrid,
            savePreferencesButton
        );

        // SYSTEM INFORMATION
        Label appName = new Label("Application: JeevMitra");
        Label appVersion = new Label("Version: 1.0");
        Label databaseStatus = new Label(
            "Database: Not connected on this page"
        );

        VBox systemInfo = new VBox(
            10, appName, appVersion, databaseStatus
        );

        VBox systemCard = createCard(
            "System Information",
            "Application details.",
            systemInfo
        );
VBox content = new VBox(
    20,
    heading,
    profileCard,
    notificationCard,
    securityCard,
    preferencesCard,
    systemCard
);

content.setPadding(new Insets(0, 5, 20, 0));

ScrollPane scrollPane = new ScrollPane(content);
scrollPane.setFitToWidth(true);
scrollPane.setPannable(true);
scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

scrollPane.setStyle(
    "-fx-background: " + background + ";" +
    "-fx-background-color: " + background + ";"
);

getChildren().clear();
getChildren().add(scrollPane);

VBox.setVgrow(scrollPane, Priority.ALWAYS);}
    private VBox createCard(
            String titleText,
            String description,
            javafx.scene.Node content,
            javafx.scene.Node... actions) {

        Label title = new Label(titleText);
        title.setStyle(
                "-fx-font-size: 19px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: " + darkGreen + ";");

        Label subtitle = new Label(description);
        subtitle.setStyle(
                "-fx-font-size: 12px; -fx-text-fill: #64748B;");

        VBox card = new VBox(14);
        card.setPadding(new Insets(20));
        card.setMaxWidth(Double.MAX_VALUE);

        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: #E2E8F0;" +
                        "-fx-border-radius: 12;");

        card.getChildren().addAll(title, subtitle, content);
        card.getChildren().addAll(actions);

        return card;
    }

    private String buttonStyle() {
        return "-fx-background-color: " + green + ";" +
                "-fx-text-fill: white;" +
                "-fx-padding: 9 15;" +
                "-fx-background-radius: 6;" +
                "-fx-cursor: hand;";
    }

    private void saveProfile() {

        if (nameField.getText().trim().isEmpty()
                || emailField.getText().trim().isEmpty()) {

            showMessage(
                    "Missing Information",
                    "Admin name and email are required.");
            return;
        }

        showMessage(
                "Profile",
                "Profile details validated.\n\n"
                        + "Name: " + nameField.getText()
                        + "\nEmail: " + emailField.getText()
                        + "\nPhone: " + phoneField.getText()
                        + "\n\nSaving permanently requires database integration.");
    }

    private void showMessage(String title, String message) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

    }

}
