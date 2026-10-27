package com.techyuva.view.Rescue;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class StatusTrackingPage {

    public static VBox getPage() {
        VBox page = PageUI.createPage("Status Tracking");
        VBox card = PageUI.card("Update Rescue Status");

        TextField caseId = new TextField();
        caseId.setPromptText("Enter case ID");

        ComboBox<String> status = new ComboBox<>();
        status.getItems().addAll(
            "Reported",
            "Team Assigned",
            "Rescue In Progress",
            "Treatment",
            "Resolved"
        );
        status.setPromptText("Select new status");

        Label timeline = new Label(
            "1. Reported\n\n" +
            "2. Team Assigned\n\n" +
            "3. Rescue In Progress\n\n" +
            "4. Treatment\n\n" +
            "5. Resolved"
        );
        timeline.setStyle(
            "-fx-font-size:14px;" +
            "-fx-text-fill:#176B50;"
        );

        Label result = new Label("No status updated yet.");
        result.setWrapText(true);

        Button update = PageUI.button("Update Status");

        update.setOnAction(e -> {
            if (caseId.getText().isBlank() || status.getValue() == null) {
                PageUI.alert("Enter case ID and select a status.");
                return;
            }

            result.setText(
                "Case: " + caseId.getText()
                + "\nCurrent status: " + status.getValue()
            );
        });

        card.getChildren().addAll(
            timeline,
            new Separator(),
            PageUI.label("Case ID"), caseId,
            PageUI.label("New Status"), status,
            update, result
        );

        page.getChildren().add(card);
        return page;
    }

}
