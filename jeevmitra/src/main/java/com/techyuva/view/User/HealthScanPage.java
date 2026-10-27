package com.techyuva.view.User;

import java.io.File;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

public class HealthScanPage {

    private File selectedImage;

    public Scene getHealthScanScene(Runnable backtoHome) {

        // ================= PAGE HEADER =================

        Button back = new Button("← Back");
        back.setStyle(
            "-fx-background-color:white;-fx-text-fill:#176B50;" +
            "-fx-border-color:#D5E8E0;-fx-border-radius:8px;" +
            "-fx-background-radius:8px;-fx-padding:7px 14px;" +
            "-fx-cursor:hand;"
        );
        back.setOnAction(e -> backtoHome.run());

        Label pageIcon = new Label("✧");
        pageIcon.setStyle("-fx-font-size:27px;-fx-text-fill:#238764;");

        Text title = new Text("AI Health Scan");
        title.setStyle("-fx-font-size:28px;-fx-font-weight:bold;-fx-fill:#064E3B;");

        Text subtitle = new Text(
            "Upload your pet's image and get an AI-assisted health assessment"
        );
        subtitle.setStyle("-fx-font-size:13px;-fx-fill:#668078;");

        VBox titleText = new VBox(3, title, subtitle);
        HBox titleRow = new HBox(12, pageIcon, titleText);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        HBox pageHeader = new HBox(18, back, titleRow);
        pageHeader.setAlignment(Pos.CENTER_LEFT);

        // ================= UPLOAD PET IMAGE CARD =================

        Text uploadTitle = new Text("▣  Upload Pet Image");
        uploadTitle.setStyle("-fx-font-size:20px;-fx-font-weight:bold;-fx-fill:#064E3B;");

        Label uploadBadge = new Label("Upload a clear image of your pet");
        uploadBadge.setStyle(
            "-fx-font-size:11px;-fx-text-fill:#286D59;" +
            "-fx-background-color:#EAF7F2;-fx-background-radius:16px;" +
            "-fx-padding:7px 10px;"
        );

        Region uploadHeaderSpacer = new Region();
        HBox.setHgrow(uploadHeaderSpacer, Priority.ALWAYS);
        HBox uploadHeader = new HBox(8, uploadTitle, uploadHeaderSpacer, uploadBadge);
        uploadHeader.setAlignment(Pos.CENTER_LEFT);

        Label uploadIcon = new Label("⇧");
        uploadIcon.setStyle("-fx-font-size:38px;-fx-text-fill:#238764;-fx-font-weight:bold;");

        Text uploadMessage = new Text("Choose an image of your pet");
        uploadMessage.setStyle("-fx-font-size:16px;-fx-font-weight:bold;-fx-fill:#174F3E;");

        Text uploadHint = new Text("Click below to browse • JPG, JPEG or PNG");
        uploadHint.setStyle("-fx-font-size:12px;-fx-fill:#668078;");

        ImageView imagePreview = new ImageView();
        imagePreview.setFitWidth(210);
        imagePreview.setFitHeight(105);
        imagePreview.setPreserveRatio(true);
        imagePreview.setSmooth(true);
        imagePreview.setVisible(false);
        imagePreview.setManaged(false);

        // Empty state shown before an image is selected
        VBox emptyUploadContent = new VBox(6, uploadIcon, uploadMessage, uploadHint);
        emptyUploadContent.setAlignment(Pos.CENTER);

        // Green success tag shown underneath the selected image
        Label uploadSuccess = new Label();
        uploadSuccess.setWrapText(true);
        uploadSuccess.setMaxWidth(Double.MAX_VALUE);
        uploadSuccess.setAlignment(Pos.CENTER_LEFT);
        uploadSuccess.setStyle(
                "-fx-font-size:15px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#16805D;" +
                "-fx-background-color:white;" +
                "-fx-border-color:#DCEAE5;" +
                "-fx-border-width:1px;" +
                "-fx-border-radius:10px;" +
                "-fx-background-radius:10px;" +
                "-fx-padding:10px 12px;"
            );
        uploadSuccess.setVisible(false);
        uploadSuccess.setManaged(false);

        VBox previewContent = new VBox(imagePreview);
        previewContent.setAlignment(Pos.CENTER);
        previewContent.setVisible(false);
        previewContent.setManaged(false);

        StackPane dropZone = new StackPane();
        dropZone.getChildren().addAll(emptyUploadContent, previewContent);
        dropZone.setMinHeight(180);
        dropZone.setPrefHeight(180);
        dropZone.setMaxHeight(180);
        dropZone.setMaxWidth(Double.MAX_VALUE);

        dropZone.setStyle(
            "-fx-background-color:#F2FBF7;" +
            "-fx-border-color:#A8DCC7;" +
            "-fx-border-style:dashed;" +
            "-fx-border-radius:12px;" +
            "-fx-background-radius:12px;"
        );

        Button chooseImage = new Button("Choose Pet Image");
        chooseImage.setMaxWidth(Double.MAX_VALUE);
        chooseImage.setStyle(
            "-fx-background-color:#1E7658;-fx-text-fill:white;" +
            "-fx-font-size:13px;-fx-font-weight:bold;" +
            "-fx-background-radius:9px;-fx-padding:11px;" +
            "-fx-cursor:hand;"
        );

        chooseImage.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Select Pet Image");
            chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
            );

            File file = chooser.showOpenDialog(null);
            if (file == null) {
                return;
            }

            try {
                Image image = new Image(file.toURI().toString(), 500, 400, true, true);
                if (image.isError()) {
                    throw new IllegalArgumentException("Invalid image");
                }

                selectedImage = file;
                imagePreview.setImage(image);

                // Hide the empty upload message and show the image preview
                emptyUploadContent.setVisible(false);
                emptyUploadContent.setManaged(false);

                imagePreview.setVisible(true);
                imagePreview.setManaged(true);
                uploadSuccess.setText("✔ Image uploaded successfully!  •  " + file.getName());
                uploadSuccess.setVisible(true);
                uploadSuccess.setManaged(true);

                previewContent.setVisible(true);
                previewContent.setManaged(true);
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Image Error", "Unable to load this image. Please choose another JPG or PNG file.");
            }
        });

        // Order matters: success message appears BELOW the Choose Pet Image button.
        VBox uploadCard = new VBox(12, uploadHeader, dropZone, chooseImage, uploadSuccess);
        uploadCard.setPadding(new Insets(16));
        uploadCard.setPrefWidth(430);
        uploadCard.setMinWidth(280);
        uploadCard.setMaxWidth(Double.MAX_VALUE);
        uploadCard.setStyle(cardStyle());
        HBox.setHgrow(uploadCard, Priority.ALWAYS);

        // ================= PET DETAILS CARD =================

        Text detailsTitle = new Text("▤  Pet Details");
        detailsTitle.setStyle("-fx-font-size:20px;-fx-font-weight:bold;-fx-fill:#064E3B;");

        Label lblPetName = createLabel("Pet Name");
        TextField inPetName = new TextField();
        inPetName.setPromptText("Enter pet name");
        styleInput(inPetName);

        Label lblPetType = createLabel("Animal Type");
        ComboBox<String> inPetType = new ComboBox<>();
        inPetType.getItems().addAll("Dog", "Cat", "Bird", "Rabbit", "Other");
        inPetType.setPromptText("Select animal type");
        inPetType.setMaxWidth(Double.MAX_VALUE);
        styleComboBox(inPetType);

        Label lblBreed = createLabel("Breed");
        TextField inBreed = new TextField();
        inBreed.setPromptText("Enter breed");
        styleInput(inBreed);

        Label lblAge = createLabel("Age");
        TextField inAge = new TextField();
        inAge.setPromptText("e.g. 3");
        styleInput(inAge);

        Label lblAgeUnit = createLabel("Age Unit");
        ComboBox<String> ageUnit = new ComboBox<>();
        ageUnit.getItems().addAll("Months", "Years");
        ageUnit.setValue("Years");
        ageUnit.setMaxWidth(Double.MAX_VALUE);
        styleComboBox(ageUnit);

        Label lblSymptoms = createLabel("Symptoms (optional)");
        TextArea inSymptoms = new TextArea();
        inSymptoms.setPromptText("Describe symptoms, if any...");
        inSymptoms.setWrapText(true);
        inSymptoms.setPrefRowCount(2);
        inSymptoms.setMaxWidth(Double.MAX_VALUE);
        inSymptoms.setStyle(
            "-fx-font-size:13px;-fx-border-color:#DCEAE5;" +
            "-fx-border-radius:8px;-fx-background-radius:8px;"
        );

        VBox petNameBox = new VBox(6, lblPetName, inPetName);
        VBox petTypeBox = new VBox(6, lblPetType, inPetType);
        VBox breedBox = new VBox(6, lblBreed, inBreed);
        VBox ageBox = new VBox(6, lblAge, inAge);
        VBox ageUnitBox = new VBox(6, lblAgeUnit, ageUnit);

        HBox typeBreedRow = new HBox(12, petTypeBox, breedBox);
        HBox.setHgrow(petTypeBox, Priority.ALWAYS);
        HBox.setHgrow(breedBox, Priority.ALWAYS);
        petTypeBox.setMaxWidth(Double.MAX_VALUE);
        breedBox.setMaxWidth(Double.MAX_VALUE);

        HBox ageRow = new HBox(12, ageBox, ageUnitBox);
        HBox.setHgrow(ageBox, Priority.ALWAYS);
        HBox.setHgrow(ageUnitBox, Priority.ALWAYS);
        ageBox.setMaxWidth(Double.MAX_VALUE);
        ageUnitBox.setMaxWidth(Double.MAX_VALUE);

        Text status = new Text();
        status.setStyle("-fx-font-size:12px;-fx-fill:#16805D;");

        Button analyzeButton = new Button("✚   Analyze Pet Health");
        analyzeButton.setMaxWidth(Double.MAX_VALUE);
        analyzeButton.setStyle(
            "-fx-background-color:linear-gradient(to right,#18805D,#064E3B);" +
            "-fx-text-fill:white;-fx-font-size:15px;-fx-font-weight:bold;" +
            "-fx-background-radius:10px;-fx-padding:13px;" +
            "-fx-cursor:hand;"
        );

        VBox detailsCard = new VBox(
            10, detailsTitle, petNameBox, typeBreedRow, ageRow,
            lblSymptoms, inSymptoms, analyzeButton, status
        );
        detailsCard.setPadding(new Insets(16));
        detailsCard.setPrefWidth(460);
        detailsCard.setMinWidth(280);
        detailsCard.setMaxWidth(Double.MAX_VALUE);
        detailsCard.setStyle(cardStyle());
        HBox.setHgrow(detailsCard, Priority.ALWAYS);

        HBox topCards = new HBox(14, uploadCard, detailsCard);
        topCards.setAlignment(Pos.TOP_CENTER);
        HBox.setHgrow(uploadCard, Priority.ALWAYS);
        HBox.setHgrow(detailsCard, Priority.ALWAYS);

        // ================= RESULT CARD =================

        Text resultTitle = new Text("▤  AI Health Scan Result");
        resultTitle.setStyle("-fx-font-size:21px;-fx-font-weight:bold;-fx-fill:#064E3B;");

        // Pet summary section
        ImageView resultPetImage = new ImageView();
        resultPetImage.setFitWidth(105);
        resultPetImage.setFitHeight(105);
        resultPetImage.setPreserveRatio(true);
        StackPane resultImageFrame = new StackPane(resultPetImage);
        resultImageFrame.setMinSize(105, 105);
        resultImageFrame.setPrefSize(105, 105);
        resultImageFrame.setStyle("-fx-background-color:#E8F5EF;-fx-background-radius:60px;");

        Label resultPetName = new Label("Your pet");
        resultPetName.setStyle("-fx-font-size:20px;-fx-font-weight:bold;-fx-text-fill:#064E3B;");

        Label resultPetInfo = new Label("Pet breed  •  Age");
        resultPetInfo.setStyle("-fx-font-size:13px;-fx-text-fill:#668078;");

        Label healthBadge = new Label("●  Waiting for scan");
        healthBadge.setStyle(
            "-fx-font-size:12px;-fx-font-weight:bold;-fx-text-fill:#8A6D3B;" +
            "-fx-background-color:#FFF4D6;-fx-background-radius:18px;-fx-padding:8px 12px;"
        );

        Label healthSummary = new Label(
            "Upload an image and enter pet details, then select Analyze Pet Health."
        );
        healthSummary.setWrapText(true);
        healthSummary.setStyle(
            "-fx-font-size:12px;-fx-text-fill:#245B4B;" +
            "-fx-background-color:#EAF8F0;-fx-background-radius:10px;-fx-padding:12px;"
        );

        VBox petResultColumn = new VBox(10, resultImageFrame, resultPetName, resultPetInfo, healthBadge, healthSummary);
        petResultColumn.setPadding(new Insets(10));
        petResultColumn.setMinWidth(230);
        petResultColumn.setStyle("-fx-background-color:#F2FBF6;-fx-background-radius:12px;");
        VBox.setVgrow(healthSummary, Priority.ALWAYS);

        // Observations panel
        Label observationsTitle = new Label("⌕  Possible Observations");
        observationsTitle.setStyle("-fx-font-size:16px;-fx-font-weight:bold;-fx-text-fill:#1456A0;");

        Label observationsText = new Label(
            "• Observations will appear here after assessment.\n\n" +
            "• Image-based findings require a connected AI model.\n\n" +
            "• Symptoms you enter will be shown for review."
        );
        observationsText.setWrapText(true);
        observationsText.setStyle("-fx-font-size:13px;-fx-text-fill:#526B7A;");

        VBox observationsCard = new VBox(12, observationsTitle, observationsText);
        observationsCard.setPadding(new Insets(16));
        observationsCard.setMinWidth(190);
        observationsCard.setStyle(
            "-fx-background-color:#EFF7FF;-fx-background-radius:12px;" +
            "-fx-border-color:#D8EAFB;-fx-border-radius:12px;"
        );
        HBox.setHgrow(observationsCard, Priority.ALWAYS);

        // Recommendations panel
        Label recommendationsTitle = new Label("✦  Recommendations");
        recommendationsTitle.setStyle("-fx-font-size:16px;-fx-font-weight:bold;-fx-text-fill:#7A4B00;");

        Label recommendationsText = new Label(
            "✓  Maintain regular vaccinations\n\n" +
            "✓  Provide a balanced diet and clean water\n\n" +
            "✓  Keep your pet clean and active\n\n" +
            "✓  Check regularly for ticks and fleas\n\n" +
            "✓  Contact a vet if symptoms persist"
        );
        recommendationsText.setWrapText(true);
        recommendationsText.setStyle("-fx-font-size:13px;-fx-text-fill:#526B7A;");

        VBox recommendationsCard = new VBox(12, recommendationsTitle, recommendationsText);
        recommendationsCard.setPadding(new Insets(16));
        recommendationsCard.setMinWidth(190);
        recommendationsCard.setStyle(
            "-fx-background-color:#FFFAEB;-fx-background-radius:12px;" +
            "-fx-border-color:#F7E9BE;-fx-border-radius:12px;"
        );
        HBox.setHgrow(recommendationsCard, Priority.ALWAYS);

        HBox resultColumns = new HBox(14, petResultColumn, observationsCard, recommendationsCard);
        resultColumns.setAlignment(Pos.TOP_LEFT);

        VBox resultCard = new VBox(16, resultTitle, new Separator(), resultColumns);
        resultCard.setPadding(new Insets(20));
        resultCard.setMaxWidth(Double.MAX_VALUE);
        resultCard.setStyle(cardStyle());

        // ================= ANALYZE ACTION =================

        analyzeButton.setOnAction(e -> {
            String petName = inPetName.getText().trim();
            String petType = inPetType.getValue();
            String breed = inBreed.getText().trim();
            String ageText = inAge.getText().trim();
            String symptoms = inSymptoms.getText().trim();

            if (selectedImage == null) {
                showAlert(Alert.AlertType.WARNING, "Image Required", "Please upload your pet's image first.");
                return;
            }

            if (petName.isEmpty() || petType == null || breed.isEmpty() || ageText.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Missing Information", "Please enter pet name, animal type, breed and age.");
                return;
            }

            double ageValue;
            try {
                ageValue = Double.parseDouble(ageText);
                if (!Double.isFinite(ageValue) || ageValue < 0 || ageValue > 100) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.WARNING, "Invalid Age", "Please enter a valid age between 0 and 100.");
                return;
            }

            String ageDisplay = ageText + " " + ageUnit.getValue().toLowerCase();
            resultPetImage.setImage(new Image(selectedImage.toURI().toString(), 160, 160, true, true));
            resultPetName.setText(petName);
            resultPetInfo.setText(breed + "  •  " + ageDisplay);
            healthBadge.setText("●  Assessment not connected");
            healthBadge.setStyle(
                "-fx-font-size:12px;-fx-font-weight:bold;-fx-text-fill:#8A6D3B;" +
                "-fx-background-color:#FFF4D6;-fx-background-radius:18px;-fx-padding:8px 12px;"
            );
            healthSummary.setText(
                "Your pet's information has been collected. This app is not connected to an AI model yet, so it cannot confirm whether your pet is healthy."
            );
            observationsText.setText(
                "Pet type: " + petType + "\n\n" +
                "Reported symptoms:\n" + (symptoms.isEmpty() ? "None entered" : symptoms) +
                "\n\nVisual observations will appear here only after an AI image-analysis service is connected."
            );
            status.setText("Details validated. Result panel updated.");
            System.out.println("===== JEEVMITRA HEALTH SCAN =====");
            System.out.println("Pet Name: " + petName);
            System.out.println("Animal Type: " + petType);
            System.out.println("Breed: " + breed);
            System.out.println("Age: " + ageDisplay);
            System.out.println("Symptoms: " + symptoms);
            System.out.println("Image: " + selectedImage.getAbsolutePath());
        });

        // ================= MAIN PAGE / STATIC SIDEBAR =================

        VBox content = new VBox(14, pageHeader, topCards, resultCard);
        content.setPadding(new Insets(16));
        content.setFillWidth(true);
        content.setStyle("-fx-background-color:#F1F8F5;");

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        scrollPane.setStyle("-fx-background:#F1F8F5;-fx-background-color:#F1F8F5;");

        HBox root = new HBox(0, Sidebar.create("AI Health Scan"), scrollPane);
        HBox.setHgrow(scrollPane, Priority.ALWAYS);

        return new Scene(root, Home.primaryStage.getWidth(), Home.primaryStage.getHeight());
    }

    public  String cardStyle() {
        return "-fx-background-color:white;" +
               "-fx-border-color:#E0ECE7;" +
               "-fx-border-radius:16px;" +
               "-fx-background-radius:16px;";
    }

   public Label createLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size:13px;-fx-font-weight:bold;-fx-text-fill:#245B4B;");
        return label;
    }

    public void styleInput(TextField field) {
        field.setMaxWidth(Double.MAX_VALUE);
        field.setPrefHeight(42);
        field.setStyle(
            "-fx-font-size:13px;-fx-background-color:white;" +
            "-fx-border-color:#DCEAE5;-fx-border-radius:8px;" +
            "-fx-background-radius:8px;-fx-padding:10px;"
        );
    }

    public  void styleComboBox(ComboBox<String> combo) {
        combo.setPrefHeight(42);
        combo.setStyle(
            "-fx-font-size:13px;-fx-background-color:white;" +
            "-fx-border-color:#DCEAE5;-fx-border-radius:8px;" +
            "-fx-background-radius:8px;"
        );
    }

    private void showAlert(Alert.AlertType type, String heading, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("JeevMitra - AI Health Scan");
        alert.setHeaderText(heading);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
