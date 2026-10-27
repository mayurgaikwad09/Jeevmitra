package com.techyuva.view.Rescue;



import javafx.scene.control.*;
import javafx.scene.layout.*;

public class NearbyClinicsPage {

    public static VBox getPage() {
        VBox page = PageUI.createPage("Nearby Clinics");

        VBox card = PageUI.card("Veterinary Clinic Directory");

        String[] clinics = {
            "Paw Care Veterinary Clinic",
            "Happy Tails Animal Hospital",
            "Vets for Life Clinic",
            "City Veterinary Care"
        };

        String[] areas = {
            "Kothrud", "Hadapsar", "Wagholi", "Baner"
        };

        for (int i = 0; i < clinics.length; i++) {
            final String clinic = clinics[i];
            final String area = areas[i];

            VBox clinicCard = new VBox(6);
            clinicCard.setPadding(new javafx.geometry.Insets(12));
            clinicCard.setStyle(
                "-fx-background-color:#F1F8F5;" +
                "-fx-background-radius:10px;"
            );

            Label name = new Label("✚  " + clinic);
            name.setStyle(
                "-fx-font-size:15px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#176B50;"
            );

            Label location = new Label("Area: " + area);

            Button details = PageUI.button("View Details");
            details.setOnAction(e ->
                PageUI.alert(
                    clinic + "\nArea: " + area +
                    "\n\nAdd verified address and phone number here."
                )
            );

            clinicCard.getChildren().addAll(name, location, details);
            card.getChildren().add(clinicCard);
        }

        page.getChildren().add(card);
        return page;
    } 
}
