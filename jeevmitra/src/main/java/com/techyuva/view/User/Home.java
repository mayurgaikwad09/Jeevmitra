package com.techyuva.view.User;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Home extends Application {

    public static Stage primaryStage;
    public static String currentUser = "Mayur";   // dusryacha login page pude ithe user cha naav set karel

    public static Home instance;        // Sidebar madhun logout sathi
    public static Scene homeScene;      // static: konatyahi page madhun dashboard var yeta yeil
    public static Runnable backtoHome;

    @Override
    public void start(Stage arg0) throws Exception {
        instance = this;
        primaryStage = arg0;
        primaryStage.setTitle("JeevMitra");
        primaryStage.setMaximized(true);
        showDashboard();
        primaryStage.show();
    }

    // ---------- DASHBOARD DAKHVA ----------
    // Dusryane banavlela login page, login yashasvi zalyavar he 2 oli call karel:
    //     Home.currentUser = "user cha naav";
    //     Home.instance.showDashboard();
    public void showDashboard() {
        // Build the dashboard only once so returning from another page does not
        // recreate the layout or reset its state.
        if (homeScene == null) {
            buildHome();
        }
        primaryStage.setScene(homeScene);
    }

    // Open the Book Appointment page from any dashboard action.
    public void openAppointmentPage() {
        if (primaryStage != null) {
            primaryStage.setScene(
                new AppointmentPage().getAppointmentScene(backtoHome)
            );
        }
    }

    // ---------- DASHBOARD ----------
    public void buildHome() {

        // =====================================================
        //  1. SIDEBAR  (Sidebar.java madhe ekda banvla ahe, sagle pages madhe vaparla jato)
        // =====================================================

        VBox sidebar = Sidebar.create("Dashboard");

        // =====================================================
        //  2. TOP BAR (search + bell + profile)
        // =====================================================

        Image imgSearch = loadImage("/assets/icons/gray/search.png");
        ImageView ivSearch = new ImageView(imgSearch);
        ivSearch.setFitHeight(20);
        ivSearch.setFitWidth(20);

        Text tSearch = new Text("Search anything...");
        tSearch.setStyle("-fx-font-size:15px;");
        tSearch.setFill(Color.web("#668078"));

        HBox hbSearch = new HBox(12, ivSearch, tSearch);
        hbSearch.setAlignment(Pos.CENTER_LEFT);
        hbSearch.setPadding(new Insets(14, 18, 14, 18));
        hbSearch.setMaxWidth(Double.MAX_VALUE);
        hbSearch.setStyle(
            "-fx-background-color:white;" +
            "-fx-background-radius:20px;" +
            "-fx-border-color:#DCEAE5;" +
            "-fx-border-radius:20px;"
        );
        HBox.setHgrow(hbSearch, Priority.ALWAYS);

        Image imgBell = loadImage("/assets/icons/red/bell.png");
        ImageView ivBell = new ImageView(imgBell);
        ivBell.setFitHeight(24);
        ivBell.setFitWidth(24);

        Image imgTopUser = loadImage("/assets/img/user.png");
        ImageView ivTopUser = new ImageView(imgTopUser);
        ivTopUser.setFitHeight(38);
        ivTopUser.setFitWidth(38);
        ivTopUser.setClip(new Circle(19, 19, 19));

        HBox hbTop = new HBox(15, hbSearch, ivBell, ivTopUser);
        hbTop.setAlignment(Pos.CENTER_LEFT);

        // =====================================================
        //  3. BANNER
        // =====================================================

        Image imgBanner = loadImage("/assets/img/vbox4.png");
        ImageView ivBanner = new ImageView(imgBanner);
        ivBanner.setFitWidth(700);
        ivBanner.setFitHeight(180);
        ivBanner.setPreserveRatio(false);
        ivBanner.setSmooth(true);

        Rectangle clipBanner = new Rectangle(700, 180);
        clipBanner.setArcWidth(30);
        clipBanner.setArcHeight(30);
        ivBanner.setClip(clipBanner);

        Text tHello = new Text("Hello, " + currentUser + "!");
        tHello.setStyle("-fx-font-size:30px;-fx-font-weight:bold;");
        tHello.setFill(Color.WHITE);

        Text tHelloSub = new Text("Together for a healthier and happier world");
        tHelloSub.setStyle("-fx-font-size:14px;");
        tHelloSub.setFill(Color.WHITE);

        VBox vbBannerText = new VBox(4, tHello, tHelloSub);
        vbBannerText.setAlignment(Pos.CENTER_LEFT);
        vbBannerText.setPadding(new Insets(0, 0, 0, 30));

        StackPane banner = new StackPane(ivBanner, vbBannerText);
        banner.setAlignment(Pos.CENTER_LEFT);
        banner.setMaxWidth(700);

        // =====================================================
        //  4. REPORT AN INJURED ANIMAL
        // =====================================================

        Text reportTitle = new Text("🐾 Report an Injured Animal");
        reportTitle.setStyle("-fx-font-size:20px;-fx-font-weight:bold;");
        reportTitle.setFill(Color.web("#064E3B"));

        Text reportAction = new Text("+ Report");
        reportAction.setStyle("-fx-font-size:13px;-fx-font-weight:bold;-fx-cursor:hand;");
        reportAction.setFill(Color.web("#1e6b4f"));
        Region reportHeadSpace = new Region();
        HBox.setHgrow(reportHeadSpace, Priority.ALWAYS);
        HBox reportHeader = new HBox(10, reportTitle, reportHeadSpace, reportAction);
        reportHeader.setAlignment(Pos.CENTER_LEFT);

        Label photoLabel = new Label("📸 Pet Photo");
        photoLabel.setStyle("-fx-font-size:13px;-fx-font-weight:bold;-fx-text-fill:#245B4B;");

        ImageView petPhotoPreview = new ImageView();
        petPhotoPreview.setFitWidth(150);
        petPhotoPreview.setFitHeight(110);
        petPhotoPreview.setPreserveRatio(true);
        StackPane photoPreviewBox = new StackPane(petPhotoPreview);
        photoPreviewBox.setAlignment(Pos.CENTER);
        photoPreviewBox.setPrefSize(170, 125);
        photoPreviewBox.setMinSize(170, 125);
        photoPreviewBox.setStyle("-fx-background-color:#F4FBF7;-fx-border-color:#BFD9CD;-fx-border-style:dashed;-fx-border-radius:12px;-fx-background-radius:12px;");
        Label photoPlaceholder = new Label("Image preview");
        photoPlaceholder.setStyle("-fx-text-fill:#668078;-fx-font-size:12px;");
        photoPreviewBox.getChildren().add(photoPlaceholder);
        petPhotoPreview.setPreserveRatio(true);
        petPhotoPreview.setSmooth(true);

        Button uploadImageButton = new Button("+ Upload Image");
        uploadImageButton.setStyle("-fx-background-color:#1e6b4f;-fx-text-fill:white;-fx-background-radius:8px;-fx-cursor:hand;-fx-padding:8px 14px;");
        final java.io.File[] selectedPetImage = new java.io.File[1];
        uploadImageButton.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Choose Pet Photo");
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.webp"));
            java.io.File file = chooser.showOpenDialog(primaryStage);
            if (file != null) {
                try {
                    Image chosen = new Image(file.toURI().toString());
                    if (chosen.isError()) throw new IllegalArgumentException("Invalid image");
                    selectedPetImage[0] = file;
                    petPhotoPreview.setImage(chosen);
                    photoPlaceholder.setVisible(false);
                } catch (Exception ex) {
                    new Alert(Alert.AlertType.ERROR, "Could not open this image. Please choose another image.").showAndWait();
                }
            }
        });

        VBox photoColumn = new VBox(12, photoLabel, photoPreviewBox, uploadImageButton);
        photoColumn.setAlignment(Pos.TOP_LEFT);
        photoColumn.setPrefWidth(200);

        Label injuryLabel = new Label("🩹 Injury Details");
        injuryLabel.setStyle("-fx-font-size:13px;-fx-font-weight:bold;-fx-text-fill:#245B4B;");
        ComboBox<String> injuryType = new ComboBox<>();
        injuryType.getItems().addAll("Leg Injury", "Bleeding / Open Wound", "Eye Injury", "Unable to Walk", "Accident Case", "Other");
        injuryType.setPromptText("Select Injury Type");
        injuryType.setMaxWidth(Double.MAX_VALUE);
        injuryType.setPrefHeight(38);
        injuryType.setStyle("-fx-background-color:#F8FCFA;-fx-border-color:#DCEAE5;-fx-background-radius:8px;-fx-border-radius:8px;");

        TextArea injuryDescription = new TextArea();
        injuryDescription.setPromptText("Describe the animal's injury...");
        injuryDescription.setWrapText(true);
        injuryDescription.setPrefRowCount(3);
        injuryDescription.setPrefHeight(82);
        injuryDescription.setMaxWidth(Double.MAX_VALUE);
        injuryDescription.setStyle("-fx-control-inner-background:#F8FCFA;-fx-border-color:#DCEAE5;-fx-border-radius:8px;-fx-background-radius:8px;");

        Label locationLabel = new Label("📍 Animal Location");
        locationLabel.setStyle("-fx-font-size:13px;-fx-font-weight:bold;-fx-text-fill:#245B4B;");
        Text locationValue = new Text("Location not added");
        locationValue.setStyle("-fx-font-size:12px;");
        locationValue.setFill(Color.web("#668078"));
        Button locationButton = new Button("📍 Add Location");
        locationButton.setStyle("-fx-background-color:#E4F6EE;-fx-text-fill:#1e6b4f;-fx-font-weight:bold;-fx-background-radius:9px;-fx-cursor:hand;-fx-padding:10px 14px;");
        locationButton.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Animal Location");
            dialog.setHeaderText("Add the animal's location");
            dialog.setContentText("Enter address or area:");
            dialog.showAndWait().ifPresent(value -> {
                if (!value.trim().isEmpty()) locationValue.setText(value.trim());
            });
        });
        Text locationHint = new Text("Enter the address/area. Automatic GPS access can be connected next.");
        locationHint.setStyle("-fx-font-size:10px;");
        locationHint.setFill(Color.web("#668078"));

        VBox injuryColumn = new VBox(10, injuryLabel, injuryType, injuryDescription, locationLabel, locationButton, locationValue, locationHint);
        injuryColumn.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(injuryColumn, Priority.ALWAYS);

        HBox reportColumns = new HBox(24, photoColumn, injuryColumn);
        reportColumns.setAlignment(Pos.TOP_LEFT);

        Button submitReportButton = new Button("🚨 Submit Rescue Report");
        submitReportButton.setStyle("-fx-background-color:#176B4D;-fx-text-fill:white;-fx-font-size:14px;-fx-font-weight:bold;-fx-background-radius:10px;-fx-cursor:hand;-fx-padding:12px 20px;");
        submitReportButton.setMaxWidth(Double.MAX_VALUE);
        submitReportButton.setOnAction(e -> {
            if (selectedPetImage[0] == null || injuryType.getValue() == null || injuryDescription.getText().trim().isEmpty() || locationValue.getText().equals("Location not added")) {
                new Alert(Alert.AlertType.WARNING, "Please upload a pet photo, select an injury type, describe the injury, and add the location.").showAndWait();
                return;
            }
            Alert confirmation = new Alert(Alert.AlertType.INFORMATION);
            confirmation.setTitle("Rescue Report");
            confirmation.setHeaderText("Report details captured");
            confirmation.setContentText("Photo: " + selectedPetImage[0].getName() + "\nInjury: " + injuryType.getValue() + "\nLocation: " + locationValue.getText() + "\n\nThis demo validates the report; database saving can be added next.");
            confirmation.showAndWait();
        });

        VBox reportSection = new VBox(14, reportHeader, reportColumns, submitReportButton);
        reportSection.setPadding(new Insets(20));
        reportSection.setMaxWidth(Double.MAX_VALUE);
        reportSection.setStyle("-fx-background-color:white;-fx-border-color:#DCEAE5;-fx-border-radius:16px;-fx-background-radius:16px;");

        // =====================================================
        //  5. UPCOMING APPOINTMENTS (center khali)
        // =====================================================

        Text tStripTitle = new Text("Upcoming Appointments");
        tStripTitle.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");
        tStripTitle.setFill(Color.web("#064E3B"));

        Text tViewAll = new Text("View All");
        tViewAll.setStyle("-fx-font-size:14px;");
        tViewAll.setFill(Color.web("#1e6b4f"));

        Region spaceStrip1 = new Region();
        HBox.setHgrow(spaceStrip1, Priority.ALWAYS);

        HBox hbStripHead = new HBox(10, tStripTitle, spaceStrip1, tViewAll);
        hbStripHead.setAlignment(Pos.CENTER_LEFT);

        Image imgStripCal = loadImage("/assets/icons/gray/calendar.png");
        ImageView ivStripCal = new ImageView(imgStripCal);
        ivStripCal.setFitHeight(22);
        ivStripCal.setFitWidth(22);

        Text tStripDoc = new Text("Dr Sneha Patil - Vet");
        tStripDoc.setStyle("-fx-font-size:14px;-fx-font-weight:bold;");
        tStripDoc.setFill(Color.web("#245B4B"));

        Text tStripDate = new Text("25 Oct 2025 - 10:00 AM");
        tStripDate.setStyle("-fx-font-size:13px;");
        tStripDate.setFill(Color.web("#668078"));

        VBox vbStripText = new VBox(2, tStripDoc, tStripDate);

        Region spaceStrip2 = new Region();
        HBox.setHgrow(spaceStrip2, Priority.ALWAYS);

        Text tDetails = new Text("View Details");
        tDetails.setStyle("-fx-font-size:13px;");
        tDetails.setFill(Color.web("#1e6b4f"));

        HBox hbDetails = new HBox(tDetails);
        hbDetails.setAlignment(Pos.CENTER);
        hbDetails.setPadding(new Insets(8, 16, 8, 16));
        hbDetails.setStyle(
            "-fx-background-color:white;" +
            "-fx-border-color:#1e6b4f;" +
            "-fx-border-radius:8px;" +
            "-fx-background-radius:8px;"
        );

        HBox hbStripRow = new HBox(12, ivStripCal, vbStripText, spaceStrip2, hbDetails);
        hbStripRow.setAlignment(Pos.CENTER_LEFT);

        VBox strip = new VBox(14, hbStripHead, hbStripRow);
        strip.setPadding(new Insets(16));
        strip.setMaxWidth(Double.MAX_VALUE);
        strip.setStyle(
            "-fx-background-color:white;" +
            "-fx-border-color:#DCEAE5;" +
            "-fx-border-radius:14px;" +
            "-fx-background-radius:14px;"
        );

        // =====================================================
        //  6. CENTER DASHBOARD
        // =====================================================

        VBox dashboard = new VBox(18, hbTop, banner, reportSection, strip);
        dashboard.setAlignment(Pos.TOP_LEFT);
        dashboard.setPadding(new Insets(20));
        dashboard.setMinWidth(0);
        dashboard.setPrefWidth(Region.USE_COMPUTED_SIZE);
        dashboard.setMaxWidth(Double.MAX_VALUE);
        dashboard.setFillWidth(true);
        dashboard.setStyle("-fx-background-color:#F7FCFA;");

        // Center content scrolls vertically so the full report and appointments remain accessible.
        ScrollPane centerScroll = new ScrollPane(dashboard);
        centerScroll.setFitToWidth(true);
        centerScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        centerScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        centerScroll.setMinWidth(0);
        centerScroll.setStyle("-fx-background:#F7FCFA;-fx-background-color:#F7FCFA;-fx-padding:0;");

        // =====================================================
        //  7. RIGHT SIDE : QUICK ACTIONS
        // =====================================================

        Text tQuick = new Text("Quick Actions");
        tQuick.setStyle("-fx-font-size:20px;-fx-font-weight:bold;");
        tQuick.setFill(Color.web("#064E3B"));

        // ---------- ACTION 1 ----------
        Image imgQ1 = loadImage("/assets/icons/green/paw.png");
        ImageView ivQ1 = new ImageView(imgQ1);
        ivQ1.setFitHeight(20);
        ivQ1.setFitWidth(20);

        Text tQ1 = new Text("Scan Your Pet");
        tQ1.setStyle("-fx-font-size:13px;");
        tQ1.setFill(Color.web("#245B4B"));

        HBox hbQ1 = new HBox(12, ivQ1, tQ1);
        hbQ1.setAlignment(Pos.CENTER_LEFT);
        hbQ1.setPadding(new Insets(9, 10, 9, 10));
        hbQ1.setStyle("-fx-background-color:#F4FBF7;-fx-background-radius:10px;");

        // ---------- ACTION 2 ----------
        Image imgQ2 = loadImage("/assets/icons/green/calendar.png");
        ImageView ivQ2 = new ImageView(imgQ2);
        ivQ2.setFitHeight(20);
        ivQ2.setFitWidth(20);

        Text tQ2 = new Text("Book Vet Appointment");
        tQ2.setStyle("-fx-font-size:13px;");
        tQ2.setFill(Color.web("#245B4B"));

        HBox hbQ2 = new HBox(12, ivQ2, tQ2);
        hbQ2.setAlignment(Pos.CENTER_LEFT);
        hbQ2.setPadding(new Insets(9, 10, 9, 10));
        hbQ2.setStyle("-fx-background-color:#F4FBF7;-fx-background-radius:10px;");

        // ---------- ACTION 3 ----------
        Image imgQ3 = loadImage("/assets/icons/green/warning.png");
        ImageView ivQ3 = new ImageView(imgQ3);
        ivQ3.setFitHeight(20);
        ivQ3.setFitWidth(20);

        Text tQ3 = new Text("Report an Injured Animal");
        tQ3.setStyle("-fx-font-size:13px;");
        tQ3.setFill(Color.web("#245B4B"));

        HBox hbQ3 = new HBox(12, ivQ3, tQ3);
        hbQ3.setAlignment(Pos.CENTER_LEFT);
        hbQ3.setPadding(new Insets(9, 10, 9, 10));
        hbQ3.setStyle("-fx-background-color:#F4FBF7;-fx-background-radius:10px;");

        // ---------- ACTION 4 ----------
        Image imgQ4 = loadImage("/assets/icons/green/heart.png");
        ImageView ivQ4 = new ImageView(imgQ4);
        ivQ4.setFitHeight(20);
        ivQ4.setFitWidth(20);

        Text tQ4 = new Text("Find a Pet for Adoption");
        tQ4.setStyle("-fx-font-size:13px;");
        tQ4.setFill(Color.web("#245B4B"));

        HBox hbQ4 = new HBox(12, ivQ4, tQ4);
        hbQ4.setAlignment(Pos.CENTER_LEFT);
        hbQ4.setPadding(new Insets(9, 10, 9, 10));
        hbQ4.setStyle("-fx-background-color:#F4FBF7;-fx-background-radius:10px;");

        // ---------- QUICK ACTIONS BOX ----------
        VBox quick = new VBox(8, tQuick, hbQ1, hbQ2, hbQ3, hbQ4);
        quick.setPadding(new Insets(14));
        quick.setMaxWidth(Double.MAX_VALUE);
        VBox.setMargin(quick, new Insets(20, 0, 0, 0));

        quick.setStyle(
            "-fx-background-color:white;" +
            "-fx-border-color:#DCEAE5;" +
            "-fx-border-radius:14px;" +
            "-fx-background-radius:14px;"
        );

        // =====================================================
        //  8. RIGHT SIDE : MY PETS
        // =====================================================

        // ---------- DATABASE (pude ithe) ----------
        // SELECT name, breed, age, vaccine_status FROM pets WHERE owner = currentUser
        // aata fakt demo data ahe. Database madhun data alyavar ya variables madhe bhara.
        String pet1Name  = "Bruno";
        String pet1Info  = "Golden Retriever  \u2022  3 yrs";
        String pet1Badge = "Vaccine due: 5 Nov";

        String pet2Name  = "Misty";
        String pet2Info  = "Persian Cat  \u2022  2 yrs";
        String pet2Badge = "Up to date";

        Text tMyPets = new Text("My Pets");
        tMyPets.setStyle("-fx-font-size:20px;-fx-font-weight:bold;");
        tMyPets.setFill(Color.web("#064E3B"));

        Text tAddPet = new Text("+ Add Pet");
        tAddPet.setStyle("-fx-font-size:13px;-fx-cursor:hand;");
        tAddPet.setFill(Color.web("#1e6b4f"));

        Region spaceMyPets = new Region();
        HBox.setHgrow(spaceMyPets, Priority.ALWAYS);

        HBox hbMyPetsHead = new HBox(10, tMyPets, spaceMyPets, tAddPet);
        hbMyPetsHead.setAlignment(Pos.CENTER_LEFT);

        // ---------- PET 1 ----------
        // pet cha asli photo pahije tr ithe paw icon chya jaagi tyacha image lava
        ImageView ivPet1 = new ImageView(loadImage("/assets/icons/white/paw.png"));
        ivPet1.setFitHeight(24);
        ivPet1.setFitWidth(24);

        StackPane boxPet1 = new StackPane(ivPet1);
        boxPet1.setPrefSize(46, 46);
        boxPet1.setMinSize(46, 46);
        boxPet1.setMaxSize(46, 46);
        boxPet1.setStyle("-fx-background-color:#e67e22;-fx-background-radius:23px;");

        Text tPet1Name = new Text(pet1Name);
        tPet1Name.setStyle("-fx-font-size:16px;-fx-font-weight:bold;");
        tPet1Name.setFill(Color.web("#245B4B"));

        Text tPet1Info = new Text(pet1Info);
        tPet1Info.setStyle("-fx-font-size:12px;");
        tPet1Info.setFill(Color.web("#668078"));

        // badge cha rang: lal/narangi = vaccine baki, hirva = up to date
        Text tPet1Badge = new Text(pet1Badge);
        tPet1Badge.setStyle("-fx-font-size:11px;-fx-font-weight:bold;");
        tPet1Badge.setFill(Color.web("#B8620F"));

        HBox hbPet1Badge = new HBox(tPet1Badge);
        hbPet1Badge.setPadding(new Insets(2, 8, 2, 8));
        hbPet1Badge.setStyle("-fx-background-color:#FDEEDE;-fx-background-radius:12px;");

        // naav + badge ek ch oli madhe (jaaga vachvnyasathi)
        Region spacePet1 = new Region();
        HBox.setHgrow(spacePet1, Priority.ALWAYS);

        HBox hbPet1Top = new HBox(8, tPet1Name, spacePet1, hbPet1Badge);
        hbPet1Top.setAlignment(Pos.CENTER_LEFT);

        VBox vbPet1Text = new VBox(2, hbPet1Top, tPet1Info);
        HBox.setHgrow(vbPet1Text, Priority.ALWAYS);

        HBox hbPet1 = new HBox(12, boxPet1, vbPet1Text);
        hbPet1.setAlignment(Pos.CENTER_LEFT);
        hbPet1.setPadding(new Insets(6, 0, 6, 0));

        // ---------- LINE (donhi pets madhe) ----------
        Region lineMyPets = new Region();
        lineMyPets.setPrefHeight(1);
        lineMyPets.setMinHeight(1);
        lineMyPets.setMaxHeight(1);
        lineMyPets.setStyle("-fx-background-color:#E8F1ED;");

        // ---------- PET 2 ----------
        ImageView ivPet2 = new ImageView(loadImage("/assets/icons/white/paw.png"));
        ivPet2.setFitHeight(24);
        ivPet2.setFitWidth(24);

        StackPane boxPet2 = new StackPane(ivPet2);
        boxPet2.setPrefSize(46, 46);
        boxPet2.setMinSize(46, 46);
        boxPet2.setMaxSize(46, 46);
        boxPet2.setStyle("-fx-background-color:#16a085;-fx-background-radius:23px;");

        Text tPet2Name = new Text(pet2Name);
        tPet2Name.setStyle("-fx-font-size:16px;-fx-font-weight:bold;");
        tPet2Name.setFill(Color.web("#245B4B"));

        Text tPet2Info = new Text(pet2Info);
        tPet2Info.setStyle("-fx-font-size:12px;");
        tPet2Info.setFill(Color.web("#668078"));

        Text tPet2Badge = new Text(pet2Badge);
        tPet2Badge.setStyle("-fx-font-size:11px;-fx-font-weight:bold;");
        tPet2Badge.setFill(Color.web("#1e6b4f"));

        HBox hbPet2Badge = new HBox(tPet2Badge);
        hbPet2Badge.setPadding(new Insets(2, 8, 2, 8));
        hbPet2Badge.setStyle("-fx-background-color:#E4F6EE;-fx-background-radius:12px;");

        // naav + badge ek ch oli madhe (jaaga vachvnyasathi)
        Region spacePet2 = new Region();
        HBox.setHgrow(spacePet2, Priority.ALWAYS);

        HBox hbPet2Top = new HBox(8, tPet2Name, spacePet2, hbPet2Badge);
        hbPet2Top.setAlignment(Pos.CENTER_LEFT);

        VBox vbPet2Text = new VBox(2, hbPet2Top, tPet2Info);
        HBox.setHgrow(vbPet2Text, Priority.ALWAYS);

        HBox hbPet2 = new HBox(12, boxPet2, vbPet2Text);
        hbPet2.setAlignment(Pos.CENTER_LEFT);
        hbPet2.setPadding(new Insets(6, 0, 6, 0));

        // ---------- MY PETS BOX ----------
        VBox myPets = new VBox(4, hbMyPetsHead, hbPet1, lineMyPets, hbPet2);
        myPets.setPadding(new Insets(14));
        myPets.setMaxWidth(Double.MAX_VALUE);
        myPets.setStyle(
            "-fx-background-color:white;" +
            "-fx-border-color:#DCEAE5;" +
            "-fx-border-radius:14px;" +
            "-fx-background-radius:14px;"
        );

        // =====================================================
        //  8B. RIGHT SIDE : EMERGENCY HELP
        // =====================================================

        ImageView ivEmergency = new ImageView(loadImage("/assets/icons/white/warning.png"));
        ivEmergency.setFitHeight(22);
        ivEmergency.setFitWidth(22);

        StackPane boxEmergency = new StackPane(ivEmergency);
        boxEmergency.setPrefSize(42, 42);
        boxEmergency.setMinSize(42, 42);
        boxEmergency.setMaxSize(42, 42);
        boxEmergency.setStyle("-fx-background-color:#e74c3c;-fx-background-radius:21px;");

        Text tEmergencyTitle = new Text("Emergency Help");
        tEmergencyTitle.setStyle("-fx-font-size:17px;-fx-font-weight:bold;");
        tEmergencyTitle.setFill(Color.web("#B03A2E"));

        Text tEmergencySub = new Text("Found an injured animal?\nGet help from a vet right now.");
        tEmergencySub.setStyle("-fx-font-size:12px;");
        tEmergencySub.setFill(Color.web("#8E4A44"));

        VBox vbEmergencyText = new VBox(3, tEmergencyTitle, tEmergencySub);

        HBox hbEmergencyTop = new HBox(14, boxEmergency, vbEmergencyText);
        hbEmergencyTop.setAlignment(Pos.BOTTOM_CENTER);

        Text tCall = new Text("Call Vet Helpline");
        tCall.setStyle("-fx-font-size:15px;-fx-font-weight:bold;");
        tCall.setFill(Color.WHITE);

        HBox hbCall = new HBox(tCall);
        hbCall.setAlignment(Pos.CENTER);
        hbCall.setPadding(new Insets(10));
        hbCall.setMaxWidth(Double.MAX_VALUE);
        hbCall.setStyle(
            "-fx-background-color:#D63031;" +
            "-fx-background-radius:12px;" +
            "-fx-cursor:hand;"
        );

        // helpline cha asli number ithe taka
        Text tCallInfo = new Text("Available 24 x 7  \u2022  98XXXXXXXX");
        tCallInfo.setStyle("-fx-font-size:12px;");
        tCallInfo.setFill(Color.web("#8E4A44"));

        HBox hbCallInfo = new HBox(tCallInfo);
        hbCallInfo.setAlignment(Pos.CENTER);

        VBox emergency = new VBox(20, hbEmergencyTop, hbCall, hbCallInfo);
        emergency.setPadding(new Insets(14));
        emergency.setMaxWidth(Double.MAX_VALUE);
        emergency.setAlignment(Pos.BOTTOM_CENTER);
        emergency.setStyle(
            "-fx-background-color:#FDECEA;" +
            "-fx-border-color:#F5B7B1;" +
            "-fx-border-radius:14px;" +
            "-fx-background-radius:14px;"
        );

        // =====================================================
        //  9. RIGHT SIDE
        // =====================================================

        VBox rightSide = new VBox(28, quick, myPets, emergency);
        rightSide.setPrefWidth(300);
        rightSide.setMinWidth(260);
        rightSide.setMaxWidth(300);
        rightSide.setPadding(new Insets(12, 20, 12, 0));
        rightSide.setStyle("-fx-background-color:#F7FCFA;");

        // =====================================================
        //  10. MAIN LAYOUT
        // =====================================================

        // right side cha content jaast zala tr scroll hoto (chhotya window sathi)
        ScrollPane rightScroll = new ScrollPane(rightSide);
        rightScroll.setFitToWidth(true);
        rightScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        rightScroll.setPrefWidth(320);
        rightScroll.setMinWidth(260);
        rightScroll.setMaxWidth(320);
        rightScroll.setStyle("-fx-background:#F7FCFA;-fx-background-color:#F7FCFA;-fx-padding:0;");

        HBox main = new HBox(0, sidebar, centerScroll, rightScroll);
        HBox.setHgrow(centerScroll, Priority.ALWAYS);
        main.setFillHeight(true);

        homeScene = new Scene(main, primaryStage.getWidth(), primaryStage.getHeight());

        // pages madhun parat dashboard var yenyasathi (Formpage/Aboutpage sarkhach)
        backtoHome = new Runnable() {
            @Override
            public void run() {
                primaryStage.setScene(homeScene);
            }
        };

        // =====================================================
        //  11. BUTTONS : click kele ki kuthe jaycha
        //      pratyek page chi vegli class ahe, ti scene deti
        // =====================================================

        // ---------- QUICK ACTIONS ----------
        hbQ1.setOnMouseClicked(e -> primaryStage.setScene(new HealthScanPage().getHealthScanScene(backtoHome)));
        hbQ2.setOnMouseClicked(e -> openAppointmentPage());
        hbQ3.setOnMouseClicked(e -> primaryStage.setScene(new RescuePage().getRescueScene(backtoHome)));
        hbQ4.setOnMouseClicked(e -> primaryStage.setScene(new AdoptionPage().getAdoptionScene(backtoHome)));

        // ---------- UPCOMING APPOINTMENT ----------
        hbDetails.setOnMouseClicked(e -> primaryStage.setScene(new Detail().getDetailScene(backtoHome)));
        hbStripHead.setOnMouseClicked(e -> openAppointmentPage());

        // ---------- TOP BAR PROFILE PHOTO ----------
        ivTopUser.setOnMouseClicked(e -> primaryStage.setScene(new ProfilePage().getProfileScene(backtoHome)));

        // ---------- MY PETS ----------
        tAddPet.setOnMouseClicked(e -> primaryStage.setScene(new AddPetPage().getAddPetScene(backtoHome)));

        // pet var click kela tr: pude pet cha detail page ughadaycha (aata fakt console madhe print)
        hbPet1.setOnMouseClicked(e -> System.out.println("pet clicked: " + pet1Name));
        hbPet2.setOnMouseClicked(e -> System.out.println("pet clicked: " + pet2Name));

        // ---------- EMERGENCY HELP ----------
        hbCall.setOnMouseClicked(e -> {
            System.out.println("Call Vet Helpline clicked");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Vet Helpline");
            alert.setHeaderText("Emergency Help");
            alert.setContentText("Call: 98XXXXXXXX  (24 x 7)");
            alert.showAndWait();
        });

        // clickable rows var hand cursor dakhva
        HBox[] clickable = { hbQ1, hbQ2, hbQ3, hbQ4, hbDetails, hbStripHead, hbPet1, hbPet2 };
        for (HBox row : clickable) {
            row.setStyle(row.getStyle() + "-fx-cursor:hand;");
        }
        ivTopUser.setStyle("-fx-cursor:hand;");
    }

    // image sapadli nahi tr crash honar nahi, console madhe message yeil
    private Image loadImage(String resourcePath) {
        java.net.URL resource = getClass().getResource(resourcePath);
        if (resource == null) {
            System.err.println("Image sapadli nahi: " + resourcePath);
            return new WritableImage(1, 1);
        }
        return new Image(resource.toExternalForm());
    }

    // card chya khalcha green button
    private Button createActionButton(String text) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setStyle(
            "-fx-background-color:#1e6b4f;" +
            "-fx-text-fill:white;" +
            "-fx-font-size:12px;" +
            "-fx-font-weight:bold;" +
            "-fx-background-radius:8px;" +
            "-fx-cursor:hand;" +
            "-fx-padding:7px 14px;"
        );
        return button;
    }

}