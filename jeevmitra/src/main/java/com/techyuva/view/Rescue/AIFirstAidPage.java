package com.techyuva.view.Rescue;

import javafx.scene.control.*;
import javafx.scene.layout.*;

public class AIFirstAidPage {

    public static VBox getPage() {
        VBox page = PageUI.createPage("AI First-Aid");
        VBox card = PageUI.card("Animal First-Aid Guidance");

        Label instructions = new Label(
            "🐾  1. Stay calm and observe the animal from a safe distance.\n\n" +
            "🛡  2. Avoid touching an injured or frightened animal.\n\n" +
            "📍  3. Note the exact rescue location.\n\n" +
            "☎  4. Contact a qualified veterinarian or rescue team.\n\n" +
            "❤️  5. Follow the veterinarian's instructions."
        );

        instructions.setWrapText(true);
        instructions.setStyle(
            "-fx-font-size:15px;" +
            "-fx-text-fill:#245B4B;" +
            "-fx-padding:12px;"
        );

        Label warning = new Label(
            "Important: This page provides general safety guidance. " +
            "It does not diagnose an animal or connect to an AI model."
        );
        warning.setWrapText(true);
        warning.setStyle(
            "-fx-background-color:#FFF4D6;" +
            "-fx-text-fill:#805600;" +
            "-fx-padding:12px;" +
            "-fx-background-radius:8px;"
        );

        Button contact = PageUI.button("Request Veterinary Help");
        contact.setOnAction(e ->
            PageUI.alert("Please use your verified local veterinary or rescue contact.")
        );

        card.getChildren().addAll(instructions, warning, contact);
        page.getChildren().add(card);
        return page;
    }
}
