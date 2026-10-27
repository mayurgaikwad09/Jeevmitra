package com.techyuva.view.Rescue;



import javafx.scene.control.*;
import javafx.scene.layout.*;

public class CaseAssignmentPage {

    public static VBox getPage() {
        VBox page = PageUI.createPage("Case Assignment");
        VBox card = PageUI.card("Assign Rescue Team");

        TextField caseId = new TextField();
        caseId.setPromptText("Example: RM-1042");

        ComboBox<String> team = new ComboBox<>();
        team.getItems().addAll(
            "Team Alpha", "Team Bravo", "Team Charlie"
        );
        team.setPromptText("Select rescue team");

        ComboBox<String> priority = new ComboBox<>();
        priority.getItems().addAll("Critical", "High", "Medium", "Low");
        priority.setPromptText("Select priority");

        Button assign = PageUI.button("Assign Team");

        assign.setOnAction(e -> {
            if (caseId.getText().isBlank()
                    || team.getValue() == null
                    || priority.getValue() == null) {
                PageUI.alert("Please complete all fields.");
                return;
            }

            PageUI.alert(
                "Case: " + caseId.getText()
                + "\nTeam: " + team.getValue()
                + "\nPriority: " + priority.getValue()
                + "\n\nDemo assignment completed."
            );
        });

        card.getChildren().addAll(
            PageUI.label("Case ID"), caseId,
            PageUI.label("Rescue Team"), team,
            PageUI.label("Priority"), priority,
            assign
        );

        page.getChildren().add(card);
        return page;
    }

}
