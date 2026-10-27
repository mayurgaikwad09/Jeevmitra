package com.techyuva.view.User;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdoptionPage {
    private static final String GREEN = "#087653";
    private static final String DARK = "#073D30";
    private static final String BG = "#F2FAF6";
    private static final String BORDER = "#DDECE4";
    private static final String MINT = "#E4F5EB";

    private final List<Pet> pets = new ArrayList<>();
    private final List<PetCard> petCards = new ArrayList<>();

    private TextField searchField;
    private ComboBox<String> animalFilter;
    private ComboBox<String> ageFilter;
    private ComboBox<String> genderFilter;
    private FlowPane petGrid;
    private Label countLabel;
    private boolean adoptionTermsAccepted = false;

    public Scene getAdoptionScene(Runnable backToHome) {
        loadPets();
        petCards.clear();

        Button backButton = new Button("← Back");
        backButton.setStyle(outlineButtonStyle());
        backButton.setOnAction(e -> {
            if (backToHome != null) backToHome.run();
        });

        VBox title = new VBox(2,
                text("🐾 Pet Adoption", 30, true, DARK),
                text("Give a loving home to a furry friend. Adopt, don't shop!", 13, false, "#547468"));
        HBox heading = new HBox(18, backButton, title);
        heading.setAlignment(Pos.CENTER_LEFT);

        HBox banner = new HBox(12,
                new VBox(7,
                        text("Adopt Love. Change a Life. 🐾", 23, true, DARK),
                        text("Every pet deserves kindness, care and a family to call their own.", 14, false, DARK)),
                new Region());
        banner.setAlignment(Pos.CENTER_LEFT);
        banner.setPadding(new Insets(20));
        banner.setStyle("-fx-background-color:linear-gradient(to right,#EAF8D9,#D9F5E7);"
                + "-fx-background-radius:16px;");
        HBox.setHgrow(banner.getChildren().get(1), Priority.ALWAYS);

        searchField = new TextField();
        searchField.setPromptText("Search pet name or location...");
        searchField.setPrefHeight(42);
        searchField.setStyle(inputStyle());

        animalFilter = new ComboBox<>();
        animalFilter.getItems().addAll("All Animals", "Dog", "Cat");
        animalFilter.setValue("All Animals");
        animalFilter.setMaxWidth(Double.MAX_VALUE);
        animalFilter.setPrefHeight(42);
        animalFilter.setStyle(inputStyle());

        ageFilter = new ComboBox<>();
        ageFilter.getItems().addAll("Any Age", "Puppy/Kitten", "Young", "Adult", "Senior");
        ageFilter.setValue("Any Age");
        ageFilter.setMaxWidth(Double.MAX_VALUE);
        ageFilter.setPrefHeight(42);
        ageFilter.setStyle(inputStyle());

        genderFilter = new ComboBox<>();
        genderFilter.getItems().addAll("Any Gender", "Male", "Female");
        genderFilter.setValue("Any Gender");
        genderFilter.setMaxWidth(Double.MAX_VALUE);
        genderFilter.setPrefHeight(42);
        genderFilter.setStyle(inputStyle());

        Button searchButton = new Button("⌕ Search");
        searchButton.setStyle(primaryButtonStyle());
        searchButton.setOnAction(e -> applyFilters());

        Button resetButton = new Button("Reset");
        resetButton.setStyle(outlineButtonStyle());
        resetButton.setOnAction(e -> {
            searchField.clear();
            animalFilter.setValue("All Animals");
            ageFilter.setValue("Any Age");
            genderFilter.setValue("Any Gender");
            applyFilters();
        });

        // Keep search + all three dropdown filters on one clean horizontal line.
        HBox filters = new HBox(8);
        filters.setAlignment(Pos.CENTER_LEFT);
        filters.setFillHeight(true);
        filters.setMaxWidth(Double.MAX_VALUE);

        searchField.setPrefWidth(255);
        searchField.setMinWidth(190);
        searchField.setMaxWidth(Double.MAX_VALUE);

        animalFilter.setPrefWidth(135);
        animalFilter.setMinWidth(125);
        animalFilter.setMaxWidth(145);

        ageFilter.setPrefWidth(120);
        ageFilter.setMinWidth(110);
        ageFilter.setMaxWidth(130);

        genderFilter.setPrefWidth(135);
        genderFilter.setMinWidth(125);
        genderFilter.setMaxWidth(145);

        searchButton.setPrefWidth(82);
        searchButton.setMinWidth(78);
        resetButton.setPrefWidth(68);
        resetButton.setMinWidth(64);

        filters.getChildren().addAll(
                searchField, animalFilter, ageFilter, genderFilter, searchButton, resetButton
        );

        Label meetHeading = new Label("Meet your new best friend");
        meetHeading.setStyle("-fx-font-size:20px;-fx-font-weight:bold;-fx-text-fill:" + DARK + ";");

        petGrid = new FlowPane(12, 12);
        petGrid.setAlignment(Pos.TOP_LEFT);
        petGrid.setPrefWrapLength(980);
        petGrid.setMaxWidth(Double.MAX_VALUE);

        for (Pet pet : pets) {
            PetCard card = createPetCard(pet);
            petCards.add(card);
            petGrid.getChildren().add(card.root);
        }

        countLabel = new Label("Available Pets (" + pets.size() + ")");
        countLabel.setStyle("-fx-font-size:14px;-fx-font-weight:bold;-fx-text-fill:" + DARK + ";");

        // Agreement checkboxes are displayed beside Why Adopt and Guidelines.
        CheckBox acceptWhy = new CheckBox("I understand why adoption matters.");
        CheckBox acceptGuidelines = new CheckBox("I accept all adoption guidelines.");
        CheckBox acceptResponsibility = new CheckBox(
                "I agree to provide long-term care and take full responsibility for the adopted pet.");

        for (CheckBox check : List.of(acceptWhy, acceptGuidelines, acceptResponsibility)) {
            check.setWrapText(true);
            check.setMaxWidth(Double.MAX_VALUE);
            check.setStyle("-fx-font-size:12px;-fx-text-fill:#315F50;");
        }

        Label agreementHeading = new Label("🐾 Agreement");
        agreementHeading.setStyle("-fx-font-size:19px;-fx-font-weight:bold;-fx-text-fill:" + DARK + ";");

        Label agreementHint = new Label("Please confirm that you have read and understood the above information.");
        agreementHint.setWrapText(true);
        agreementHint.setStyle("-fx-font-size:11px;-fx-text-fill:#547468;");

        VBox agreementCard = new VBox(12, agreementHeading, agreementHint,
                acceptWhy, acceptGuidelines, acceptResponsibility);
        agreementCard.setPadding(new Insets(16));
        agreementCard.setMinWidth(240);
        agreementCard.setStyle("-fx-background-color:#EAF8F1;-fx-background-radius:15px;"
                + "-fx-border-color:" + BORDER + ";-fx-border-radius:15px;");

        VBox whyCard = whyAdoptCard();
        VBox guidelineCard = guidelinesCard();
        whyCard.setMinWidth(250);
        guidelineCard.setMinWidth(300);
        agreementCard.setMinWidth(260);

        HBox infoRow = new HBox(12, whyCard, guidelineCard, agreementCard);
        infoRow.setAlignment(Pos.TOP_LEFT);
        infoRow.setFillHeight(true);
        HBox.setHgrow(whyCard, Priority.SOMETIMES);
        HBox.setHgrow(guidelineCard, Priority.ALWAYS);
        HBox.setHgrow(agreementCard, Priority.SOMETIMES);
        infoRow.setMaxWidth(Double.MAX_VALUE);

        Runnable refreshAdoptButtons = () -> {
            boolean accepted = acceptWhy.isSelected()
                    && acceptGuidelines.isSelected()
                    && acceptResponsibility.isSelected();
            adoptionTermsAccepted = accepted;
            for (PetCard card : petCards) {
                for (javafx.scene.Node node : card.root.getChildren()) {
                    if (node instanceof Button && ((Button) node).getText().contains("Adopt Now")) {
                        ((Button) node).setDisable(!accepted);
                        ((Button) node).setOpacity(accepted ? 1.0 : 0.58);
                    }
                }
            }
        };
        acceptWhy.selectedProperty().addListener((obs, oldValue, newValue) -> refreshAdoptButtons.run());
        acceptGuidelines.selectedProperty().addListener((obs, oldValue, newValue) -> refreshAdoptButtons.run());
        acceptResponsibility.selectedProperty().addListener((obs, oldValue, newValue) -> refreshAdoptButtons.run());
        refreshAdoptButtons.run();

        HBox petToolbar = new HBox(14, meetHeading, new Region());
        petToolbar.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(petToolbar.getChildren().get(1), Priority.ALWAYS);

        VBox petSection = new VBox(12, petToolbar, filters, countLabel, petGrid);
        petSection.setMaxWidth(Double.MAX_VALUE);

        VBox mainFlow = new VBox(16, infoRow, petSection);
        mainFlow.setMaxWidth(Double.MAX_VALUE);

        VBox pageContent = new VBox(16, heading, banner, mainFlow);
        pageContent.setPadding(new Insets(18));
        pageContent.setStyle("-fx-background-color:" + BG + ";");

        ScrollPane scrollPane = new ScrollPane(pageContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPannable(true);
        scrollPane.setStyle("-fx-background:" + BG + ";-fx-background-color:" + BG + ";-fx-padding:0;");

        VBox sidebar = Sidebar.create("Adoption");
        sidebar.setPrefWidth(260);
        sidebar.setMinWidth(260);
        sidebar.setMaxWidth(260);

        HBox root = new HBox(0, sidebar, scrollPane);
        HBox.setHgrow(scrollPane, Priority.ALWAYS);
        root.setStyle("-fx-background-color:" + BG + ";");
        return new Scene(root, 1300, 760);
    }

    private void loadPets() {
        pets.clear();
        pets.add(new Pet("Charlie", "Dog", "2 years", "Male", "Ahmednagar",
                "Friendly, playful and loves spending time with people.", "charlie.jpg", "Adult"));
        pets.add(new Pet("Misty", "Cat", "6 months", "Female", "Pune",
                "A sweet kitten looking for a safe and loving home.", "misty.jpg", "Puppy/Kitten"));
        pets.add(new Pet("Rocky", "Dog", "1 year", "Male", "Ahmednagar",
                "An energetic companion who enjoys walks and playtime.", "rocky.jpg", "Young"));
        pets.add(new Pet("Luna", "Cat", "1 year", "Female", "Nashik",
                "Calm, affectionate and loves company.", "luna.jpg", "Young"));
        pets.add(new Pet("Max", "Dog", "8 months", "Male", "Pune",
                "Very active and playful. Great with kids and other pets.", "max.jpg", "Puppy/Kitten"));
        pets.add(new Pet("Snowy", "Cat", "1 year", "Female", "Ahmednagar",
                "Gentle and affectionate. Looking for a caring family.", "snowy.jpg", "Young"));
    }

    private PetCard createPetCard(Pet pet) {
        ImageView petImage = loadImage(pet.imageFile, 270, 112);
        petImage.setFitWidth(270);
        petImage.setFitHeight(112);
        petImage.setPreserveRatio(false);
        petImage.setSmooth(true);

        StackPane imageFrame = new StackPane();
        imageFrame.setAlignment(Pos.CENTER);
        imageFrame.setMinSize(0, 112);
        imageFrame.setPrefSize(270, 112);
        imageFrame.setMaxSize(Double.MAX_VALUE, 112);
        imageFrame.setStyle("-fx-background-color:" + MINT + ";-fx-background-radius:9px;");
        imageFrame.getChildren().add(petImage);

        // Keep the image strictly inside the visible image box.
        javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle();
        clip.widthProperty().bind(imageFrame.widthProperty());
        clip.heightProperty().bind(imageFrame.heightProperty());
        clip.setArcWidth(16);
        clip.setArcHeight(16);
        imageFrame.setClip(clip);
        petImage.fitWidthProperty().bind(imageFrame.widthProperty());
        petImage.setMouseTransparent(true);

        Label animalLabel = new Label(pet.type);
        animalLabel.setStyle("-fx-background-color:" + GREEN + ";-fx-text-fill:white;"
                + "-fx-font-size:10px;-fx-font-weight:bold;-fx-padding:5px 8px;-fx-background-radius:8px;");
        StackPane.setAlignment(animalLabel, Pos.TOP_LEFT);
        StackPane.setMargin(animalLabel, new Insets(7));
        imageFrame.getChildren().add(animalLabel);

        Label name = new Label(pet.name);
        name.setStyle("-fx-font-size:17px;-fx-font-weight:bold;-fx-text-fill:" + DARK + ";");

        Label meta = new Label("▦ " + pet.age + "   •   "
                + (pet.gender.equals("Male") ? "♂" : "♀") + " " + pet.gender
                + "   •   📍 " + pet.location);
        meta.setWrapText(true);
        meta.setStyle("-fx-font-size:11px;-fx-text-fill:#547468;");

        Label description = new Label(pet.description);
        description.setWrapText(true);
        description.setMinHeight(32);
        description.setStyle("-fx-font-size:12px;-fx-text-fill:#45675B;");

        Button adopt = new Button("♥ Adopt Now");
        adopt.setMaxWidth(Double.MAX_VALUE);
        adopt.setDisable(true);
        adopt.setOpacity(0.58);
        adopt.setStyle(primaryButtonStyle());
        adopt.setOnAction(e -> showAdoptionDialog(pet));

        VBox card = new VBox(7, imageFrame, name, meta, description, adopt);
        card.setPadding(new Insets(9));
        card.setPrefWidth(290);
        card.setMinWidth(270);
        card.setMaxWidth(300);
        card.setFillWidth(true);
        card.setMinHeight(0);
        card.setStyle("-fx-background-color:white;-fx-background-radius:13px;"
                + "-fx-border-color:" + BORDER + ";-fx-border-radius:13px;");

        imageFrame.prefWidthProperty().bind(card.widthProperty().subtract(18));
        imageFrame.maxWidthProperty().bind(card.widthProperty().subtract(18));

        return new PetCard(pet, card);
    }

    private VBox whyAdoptCard() {
        Label heading = new Label("♥ Why Adopt?");
        heading.setStyle("-fx-font-size:18px;-fx-font-weight:bold;-fx-text-fill:" + DARK + ";");
        Label points = new Label(
                "✓  Save a life — give an animal a second chance.\n\n"
                + "✓  Find a loyal friend — enjoy unconditional love.\n\n"
                + "✓  Make a difference — help create a safer community.");
        points.setWrapText(true);
        points.setStyle("-fx-font-size:12px;-fx-text-fill:#45675B;");
        Label petsArt = new Label("🐕  🐈  ♥");
        petsArt.setAlignment(Pos.CENTER);
        petsArt.setMaxWidth(Double.MAX_VALUE);
        petsArt.setStyle("-fx-font-size:27px;-fx-padding:12px;");
        VBox card = new VBox(12, heading, points, petsArt);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color:white;-fx-background-radius:15px;"
                + "-fx-border-color:" + BORDER + ";-fx-border-radius:15px;");
        return card;
    }

    private VBox guidelinesCard() {
        Label heading = new Label("🐾 Adoption Guidelines");
        heading.setStyle("-fx-font-size:17px;-fx-font-weight:bold;-fx-text-fill:" + DARK + ";");
        Label points = new Label(
                "✓  Be ready for long-term care.\n\n"
                + "✓  Provide a safe and loving home.\n\n"
                + "✓  Meet the pet before adopting.\n\n"
                + "✓  Home verification may be required.\n\n"
                + "✓  Adoption depends on availability.");
        points.setWrapText(true);
        points.setStyle("-fx-font-size:12px;-fx-text-fill:#45675B;");
        VBox card = new VBox(12, heading, points);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color:white;-fx-background-radius:15px;"
                + "-fx-border-color:" + BORDER + ";-fx-border-radius:15px;");
        return card;
    }

    private void applyFilters() {
        String query = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase(Locale.ROOT);
        String type = animalFilter.getValue();
        String age = ageFilter.getValue();
        String gender = genderFilter.getValue();

        List<PetCard> filtered = new ArrayList<>();
        for (PetCard card : petCards) {
            Pet pet = card.pet;
            boolean queryMatch = query.isEmpty()
                    || pet.name.toLowerCase(Locale.ROOT).contains(query)
                    || pet.location.toLowerCase(Locale.ROOT).contains(query);
            boolean typeMatch = "All Animals".equals(type) || pet.type.equals(type);
            boolean ageMatch = "Any Age".equals(age) || pet.ageGroup.equals(age);
            boolean genderMatch = "Any Gender".equals(gender) || pet.gender.equals(gender);
            if (queryMatch && typeMatch && ageMatch && genderMatch) filtered.add(card);
        }

        List<javafx.scene.Node> nodes = new ArrayList<>();
        for (PetCard card : filtered) nodes.add(card.root);
        petGrid.getChildren().setAll(nodes);
        countLabel.setText("Available Pets (" + filtered.size() + ")");
    }

    private void showAdoptionDialog(Pet pet) {
        if (!adoptionTermsAccepted) {
            showAlert("Adoption Agreement", "Please accept all adoption agreement checkboxes before adopting.");
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("JeevMitra — Adoption Request");
        dialog.setHeaderText("Adoption request for " + pet.name);

        TextField fullName = new TextField();
        fullName.setPromptText("Your full legal name");
        TextField phone = new TextField();
        phone.setPromptText("Contact number");
        TextField email = new TextField();
        email.setPromptText("Email address");
        TextField address = new TextField();
        address.setPromptText("Full home address");
        ComboBox<String> housing = new ComboBox<>();
        housing.getItems().addAll("Owned home", "Rented home", "Other");
        housing.setPromptText("Select housing type");
        housing.setMaxWidth(Double.MAX_VALUE);
        TextField household = new TextField();
        household.setPromptText("Family members / other pets");
        TextArea reason = new TextArea();
        reason.setPromptText("Why would you like to adopt " + pet.name + "?");
        reason.setPrefRowCount(3);
        CheckBox declaration = new CheckBox(
                "I confirm the details are true and I agree to responsible, long-term care.");
        declaration.setWrapText(true);

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(9);
        form.setPadding(new Insets(12));
        form.addRow(0, new Label("Selected pet:"), new Label(pet.name + " (" + pet.type + ")"));
        form.addRow(1, new Label("Full name *:"), fullName);
        form.addRow(2, new Label("Phone *:"), phone);
        form.addRow(3, new Label("Email:"), email);
        form.addRow(4, new Label("Home address *:"), address);
        form.addRow(5, new Label("Housing:"), housing);
        form.addRow(6, new Label("Household:"), household);
        form.addRow(7, new Label("Reason *:"), reason);
        form.add(declaration, 0, 8, 2, 1);
        ColumnConstraints left = new ColumnConstraints();
        left.setMinWidth(110);
        ColumnConstraints right = new ColumnConstraints();
        right.setHgrow(Priority.ALWAYS);
        right.setFillWidth(true);
        form.getColumnConstraints().addAll(left, right);
        ScrollPane formScroll = new ScrollPane(form);
        formScroll.setFitToWidth(true);
        formScroll.setPrefViewportHeight(420);
        formScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        dialog.getDialogPane().setContent(formScroll);
        dialog.getDialogPane().setPrefWidth(560);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                if (fullName.getText().isBlank() || phone.getText().isBlank()
                        || address.getText().isBlank() || reason.getText().isBlank()) {
                    showAlert("Required Details", "Please fill in your name, phone, home address and adoption reason.");
                    return null;
                }
                if (!declaration.isSelected()) {
                    showAlert("Declaration Required", "Please accept the responsible-care declaration.");
                    return null;
                }
                showAlert("Request Submitted (Demo)",
                        "Thank you, " + fullName.getText().trim() + "!\n"
                                + "Your adoption request for " + pet.name + " has been prepared.\n"
                                + "This is a demo bond/request form; it is not a legal contract and is not saved to a database.");
            }
            return button;
        });
        dialog.showAndWait();
    }

    private ImageView loadImage(String filename, double width, double height) {
        ImageView imageView = new ImageView();
        String resourcePath = "/assets/img/adoption/" + filename;
        try (InputStream stream = getClass().getResourceAsStream(resourcePath)) {
            if (stream != null) {
                Image image = new Image(stream);
                imageView.setImage(image);
            } else {
                System.out.println("Adoption image not found: " + resourcePath);
            }
        } catch (Exception ex) {
            System.out.println("Could not load " + resourcePath + ": " + ex.getMessage());
        }
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setPreserveRatio(false);
        imageView.setSmooth(true);
        return imageView;
    }

    private ColumnConstraints percentCol(double percent) {
        ColumnConstraints c = new ColumnConstraints();
        c.setPercentWidth(percent);
        return c;
    }

    private Text text(String value, double size, boolean bold, String color) {
        Text t = new Text(value);
        t.setFont(Font.font("System", bold ? FontWeight.BOLD : FontWeight.NORMAL, size));
        t.setFill(Color.web(color));
        return t;
    }

    private String inputStyle() {
        return "-fx-font-size:12px;-fx-background-color:white;-fx-border-color:" + BORDER
                + ";-fx-border-radius:8px;-fx-background-radius:8px;-fx-padding:8px;";
    }

    private String primaryButtonStyle() {
        return "-fx-background-color:" + GREEN + ";-fx-text-fill:white;-fx-font-weight:bold;"
                + "-fx-background-radius:8px;-fx-cursor:hand;-fx-padding:9px 12px;";
    }

    private String outlineButtonStyle() {
        return "-fx-background-color:white;-fx-text-fill:" + GREEN + ";-fx-font-weight:bold;"
                + "-fx-border-color:" + BORDER + ";-fx-border-radius:8px;-fx-background-radius:8px;"
                + "-fx-cursor:hand;-fx-padding:9px 12px;";
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("JeevMitra Adoption");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private static class Pet {
        final String name, type, age, gender, location, description, imageFile, ageGroup;
        Pet(String name, String type, String age, String gender, String location,
            String description, String imageFile, String ageGroup) {
            this.name = name;
            this.type = type;
            this.age = age;
            this.gender = gender;
            this.location = location;
            this.description = description;
            this.imageFile = imageFile;
            this.ageGroup = ageGroup;
        }
    }

    private static class PetCard {
        final Pet pet;
        final VBox root;
        PetCard(Pet pet, VBox root) {
            this.pet = pet;
            this.root = root;
        }
    }
}
