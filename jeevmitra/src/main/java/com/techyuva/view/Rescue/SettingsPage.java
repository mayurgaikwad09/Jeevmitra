package com.techyuva.view.Rescue;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class SettingsPage {

    public static VBox getPage() {
        VBox page = PageUI.createPage("Settings");
        VBox card = PageUI.card("Rescue Team Settings");

        TextField teamName = new TextField("Rescue Team");
        TextField memberName = new TextField();
        memberName.setPromptText("Enter team member name");

        ComboBox<String> notifications = new ComboBox<>();
        notifications.getItems().addAll("Enabled", "Disabled");
        notifications.setValue("Enabled");

        ComboBox<String> theme = new ComboBox<>();
        theme.getItems().addAll("Green & White");
        theme.setValue("Green & White");

        Button save = PageUI.button("Save Settings");

        save.setOnAction(e -> {
            if (teamName.getText().isBlank()) {
                PageUI.alert("Team name cannot be empty.");
                return;
            }

            PageUI.alert(
                "Settings validated successfully.\n" +
                "Team: " + teamName.getText() +
                "\nMember: " + memberName.getText() +
                "\nNotifications: " + notifications.getValue() +
                "\n\nNote: These settings are not saved permanently."
            );
        });

        card.getChildren().addAll(
            PageUI.label("Team Name"), teamName,
            PageUI.label("Member Name"), memberName,
            PageUI.label("Notifications"), notifications,
            PageUI.label("Theme"), theme,
            save
        );

        page.getChildren().add(card);
        return page;
    }
}
