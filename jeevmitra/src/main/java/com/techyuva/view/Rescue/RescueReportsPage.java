package com.techyuva.view.Rescue;


import javafx.scene.control.*;
import javafx.scene.layout.*;

public class RescueReportsPage {

    public static VBox getPage() {
        VBox page = PageUI.createPage("Rescue Reports");
        VBox card = PageUI.card("Create New Rescue Report");

        TextField location = new TextField();
        location.setPromptText("Enter rescue location");

        ComboBox<String> animal = new ComboBox<>();
        animal.getItems().addAll("Dog", "Cat", "Cow", "Bird", "Other");
        animal.setPromptText("Select animal");

        ComboBox<String> priority = new ComboBox<>();
        priority.getItems().addAll("Critical", "High", "Medium", "Low");
        priority.setPromptText("Select priority");

        TextArea description = new TextArea();
        description.setPromptText("Describe the animal's condition");
        description.setPrefRowCount(4);
        description.setWrapText(true);

        Button submit = PageUI.button("Submit Rescue Report");

        submit.setOnAction(e -> {
            if (location.getText().isBlank()
                    || animal.getValue() == null
                    || priority.getValue() == null) {
                PageUI.alert("Please fill in location, animal and priority.");
                return;
            }

            PageUI.alert("Rescue report submitted successfully (demo).");
            location.clear();
            animal.setValue(null);
            priority.setValue(null);
            description.clear();
        });

        card.getChildren().addAll(
            PageUI.label("Rescue Location"), location,
            PageUI.label("Animal Type"), animal,
            PageUI.label("Priority"), priority,
            PageUI.label("Description"), description,
            submit
        );

        page.getChildren().add(card);
        return page;
    }
}
