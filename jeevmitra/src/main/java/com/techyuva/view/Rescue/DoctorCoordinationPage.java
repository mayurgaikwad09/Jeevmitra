package com.techyuva.view.Rescue;



import javafx.scene.control.*;
import javafx.scene.layout.*;

public class DoctorCoordinationPage {

    public static VBox getPage() {
        VBox page = PageUI.createPage("Doctor Coordination");
        VBox card = PageUI.card("Contact Veterinarian");

        Label info = new Label(
            "Available Doctors: 8\n" +
            "On-call Doctors: 3\n" +
            "Response time: To be confirmed"
        );
        info.setStyle("-fx-font-size:14px;-fx-text-fill:#176B50;");

        TextField caseId = new TextField();
        caseId.setPromptText("Enter rescue case ID");

        ComboBox<String> doctor = new ComboBox<>();
        doctor.getItems().addAll(
            "Dr. Patil", "Dr. Sharma", "Dr. Joshi"
        );
        doctor.setPromptText("Select veterinarian");

        TextArea message = new TextArea();
        message.setPromptText("Describe the animal's condition");
        message.setPrefRowCount(3);
        message.setWrapText(true);

        Button contact = PageUI.button("Contact Doctor");

        contact.setOnAction(e -> {
            if (caseId.getText().isBlank() || doctor.getValue() == null) {
                PageUI.alert("Enter case ID and select a doctor.");
                return;
            }

            PageUI.alert(
                "Demo request prepared for " + doctor.getValue()
                + "\nCase: " + caseId.getText()
                + "\nMessage: " + message.getText()
                + "\n\nNo actual message has been sent."
            );
        });

        card.getChildren().addAll(
            info,
            PageUI.label("Case ID"), caseId,
            PageUI.label("Veterinarian"), doctor,
            PageUI.label("Medical Details"), message,
            contact
        );

        page.getChildren().add(card);
        return page;
    }

}
