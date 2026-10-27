package com.techyuva.view.User;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LostFoundPage {

    private static final String GREEN = "#176B4D";
    private static final String DARK_GREEN = "#0B3D2C";
    private static final String MINT = "#E7F6EE";
    private static final String BG = "#F5FAF7";
    private static final String BORDER = "#D9E9E0";
    private static final String TEXT = "#21483A";

    public Scene getLostFoundScene(Runnable backToHome) {
        TextField search = new TextField();
        search.setPromptText("Search pet name, breed or location...");
        search.setPrefHeight(42);
        search.setStyle(inputStyle());

        ComboBox<String> animalFilter = combo("All Animals",
                "All Animals", "Dogs", "Cats", "Rabbits", "Birds", "Other");
        ComboBox<String> locationFilter = combo("All Locations",
                "All Locations", "Ahmednagar", "Pune", "Nashik");
        ComboBox<String> dateFilter = combo("All Status",
                "All Status", "Found Pets", "Lost Pets");

        Button searchButton = new Button("⌕ Search");
        searchButton.setStyle(buttonStyle(true));
        Button resetButton = new Button("Reset");
        resetButton.setStyle(buttonStyle(false));

        HBox filters = new HBox(10, search, animalFilter, dateFilter, locationFilter,
                searchButton, resetButton);
        filters.setAlignment(Pos.CENTER_LEFT);
        filters.setFillHeight(true);
        HBox.setHgrow(search, Priority.ALWAYS);
        animalFilter.setPrefWidth(135);
        dateFilter.setPrefWidth(135);
        locationFilter.setPrefWidth(145);
        search.setPrefWidth(330);
        search.setMinWidth(180);

        TilePane lostGrid = createGrid();
        TilePane foundGrid = createGrid();

        List<javafx.scene.Node> lostPets = new ArrayList<>();
        lostPets.add(petCard("Buddy", "Dog", "2 years", "Ahmednagar", "12 Oct 2026", "found_golden_retriever_bruno.jpg", true));
        lostPets.add(petCard("Misty", "Cat", "1 year", "Pune", "10 Oct 2026", "found_tabby_kitten_misty.jpg", true));
        lostPets.add(petCard("Rocky", "Dog", "3 years", "Nashik", "08 Oct 2026", "found_beagle_puppy_rocky.jpg", true));
        lostPets.add(petCard("Luna", "Cat", "6 months", "Ahmednagar", "05 Oct 2026", "found_orange_white_cat_luna.jpg", true));

        List<javafx.scene.Node> foundPets = new ArrayList<>();
        foundPets.add(petCard("Shadow", "Dog", "1 year", "Pune", "14 Oct 2026", "lost_rottweiler_shadow.jpg", false));
        foundPets.add(petCard("Bella", "Cat", "8 months", "Ahmednagar", "12 Oct 2026", "lost_white_cat_bella.jpg", false));
        foundPets.add(petCard("Max", "Dog", "2 years", "Nashik", "11 Oct 2026", "lost_blue_eyed_husky_max.jpg", false));
        foundPets.add(petCard("Coco", "Cat", "4 months", "Pune", "09 Oct 2026", "lost_brown_tabby_coco.jpg", false));

        lostGrid.getChildren().setAll(lostPets);
        foundGrid.getChildren().setAll(foundPets);

        VBox lostSection = petSection("🔴  Lost Pets", "Pets reported as lost by their owners", "#FFF0F0", lostGrid);
        VBox foundSection = petSection("🐾  Found Pets", "Pets found and waiting for their owners", "#E7F8EF", foundGrid);

        searchButton.setOnAction(e -> {
            String query = search.getText() == null ? "" : search.getText().trim().toLowerCase();
            String animal = animalFilter.getValue();
            String location = locationFilter.getValue();
            String status = dateFilter.getValue();

            filterPets(lostGrid, lostPets, query, animal, location);
            filterPets(foundGrid, foundPets, query, animal, location);
            lostSection.setVisible(!"Found Pets".equals(status));
            lostSection.setManaged(!"Found Pets".equals(status));
            foundSection.setVisible(!"Lost Pets".equals(status));
            foundSection.setManaged(!"Lost Pets".equals(status));
        });

        resetButton.setOnAction(e -> {
            search.clear();
            animalFilter.setValue("All Animals");
            locationFilter.setValue("All Locations");
            dateFilter.setValue("All Status");
            lostGrid.getChildren().setAll(lostPets);
            foundGrid.getChildren().setAll(foundPets);
            lostSection.setVisible(true);
            lostSection.setManaged(true);
            foundSection.setVisible(true);
            foundSection.setManaged(true);
        });

        Button reportLost = new Button("📣 Report Lost Pet");
        reportLost.setMaxWidth(Double.MAX_VALUE);
        reportLost.setStyle(buttonStyleRed());
        reportLost.setOnAction(e -> showAlert("Report a Lost Pet",
                "This demo button is ready to connect to your lost-pet reporting form."));

        Button reportFound = new Button("🐾 Report Found Pet");
        reportFound.setMaxWidth(Double.MAX_VALUE);
        reportFound.setStyle(buttonStyle(true));
        reportFound.setOnAction(e -> showAlert("Report a Found Pet",
                "This demo button is ready to connect to your found-pet reporting form."));

        VBox reportCard = new VBox(12,
                text("Report a Lost or Found Pet", 19, true, DARK_GREEN),
                text("Help bring them home!", 13, false, TEXT),
                reportLost, reportFound);
        reportCard.setPadding(new Insets(18));
        reportCard.setStyle("-fx-background-color:linear-gradient(to right,#E8F8D9,#D9F5E5);"
                + "-fx-background-radius:16px;");

        VBox tipsCard = infoCard("💡 Tips to Reunite Pets",
                "✓ Check nearby areas regularly\\n"
                + "✓ Share clear photos\\n"
                + "✓ Mention exact location and date\\n"
                + "✓ Check with local shelters\\n"
                + "✓ Share with your community");

        HBox reportAndTips = new HBox(14, reportCard, tipsCard);
        reportAndTips.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(reportCard, Priority.ALWAYS);
        reportCard.setMaxWidth(Double.MAX_VALUE);
        tipsCard.setPrefWidth(300);
        tipsCard.setMinWidth(260);

        HBox statusTabs = new HBox(10);
        Button allTab = new Button("All Pets (" + (lostPets.size() + foundPets.size()) + ")");
        Button foundTab = new Button("Found Pets (" + foundPets.size() + ")");
        Button lostTab = new Button("Lost Pets (" + lostPets.size() + ")");
        for (Button tab : Arrays.asList(allTab, foundTab, lostTab)) {
            tab.setMaxWidth(Double.MAX_VALUE);
            tab.setStyle(buttonStyle(false));
            HBox.setHgrow(tab, Priority.ALWAYS);
        }
        allTab.setStyle(buttonStyle(true));
        allTab.setOnAction(e -> {
            dateFilter.setValue("All Status");
            lostSection.setVisible(true); lostSection.setManaged(true);
            foundSection.setVisible(true); foundSection.setManaged(true);
            allTab.setStyle(buttonStyle(true));
            foundTab.setStyle(buttonStyle(false)); lostTab.setStyle(buttonStyle(false));
        });
        foundTab.setOnAction(e -> {
            dateFilter.setValue("Found Pets");
            lostSection.setVisible(false); lostSection.setManaged(false);
            foundSection.setVisible(true); foundSection.setManaged(true);
            allTab.setStyle(buttonStyle(false));
            foundTab.setStyle(buttonStyle(true)); lostTab.setStyle(buttonStyle(false));
        });
        lostTab.setOnAction(e -> {
            dateFilter.setValue("Lost Pets");
            lostSection.setVisible(true); lostSection.setManaged(true);
            foundSection.setVisible(false); foundSection.setManaged(false);
            allTab.setStyle(buttonStyle(false));
            foundTab.setStyle(buttonStyle(false)); lostTab.setStyle(buttonStyle(true));
        });
        statusTabs.getChildren().setAll(allTab, foundTab, lostTab);

        VBox lists = new VBox(14, filters, statusTabs, lostSection, foundSection);
        lists.setMinWidth(0);
        lists.setFillWidth(true);

        VBox titleBox = new VBox(5,
                text("Lost & Found", 28, true, DARK_GREEN),
                text("Help reunite pets with their families. Search lost and found animals.", 13, false, "#688177"));

        Button back = new Button("← Back");
        back.setStyle(buttonStyle(false));
        back.setOnAction(e -> {
            if (backToHome != null) backToHome.run();
        });

        HBox header = new HBox(16, back, titleBox);
        header.setAlignment(Pos.CENTER_LEFT);

        VBox main = new VBox(18, header, reportAndTips, lists);
        main.setPadding(new Insets(22));
        main.setStyle("-fx-background-color:" + BG + ";");

        ScrollPane scroll = new ScrollPane(main);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setStyle("-fx-background:" + BG + ";-fx-background-color:" + BG + ";-fx-padding:0;");

        VBox sidebar = Sidebar.create("Lost & Found");
        sidebar.setPrefWidth(260);
        sidebar.setMinWidth(260);
        sidebar.setMaxWidth(260);

        HBox root = new HBox(0, sidebar, scroll);
        HBox.setHgrow(scroll, Priority.ALWAYS);
        root.setStyle("-fx-background-color:" + BG + ";");

        return new Scene(root, 1300, 760);
    }

    private TilePane createGrid() {
        TilePane grid = new TilePane();
        grid.setHgap(12);
        grid.setVgap(12);
        grid.setPrefColumns(3);
        grid.setPrefTileWidth(260);
        grid.setMinWidth(0);
        grid.setMaxWidth(Double.MAX_VALUE);
        grid.setAlignment(Pos.TOP_LEFT);

        // Keep exactly 3 equal-width cards across the available section width.
        grid.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            double available = newWidth.doubleValue() - 28; // section padding
            double tileWidth = (available - grid.getHgap() * 2) / 3.0;
            if (tileWidth >= 210) {
                grid.setPrefTileWidth(tileWidth);
                for (javafx.scene.Node node : grid.getChildren()) {
                    if (node instanceof VBox) {
                        VBox card = (VBox) node;
                        card.setPrefWidth(tileWidth);
                        card.setMinWidth(0);
                        card.setMaxWidth(Double.MAX_VALUE);
                    }
                }
            }
        });
        grid.getChildren().addListener((javafx.collections.ListChangeListener<javafx.scene.Node>) change -> {
            while (change.next()) {
                for (javafx.scene.Node node : change.getAddedSubList()) {
                    if (node instanceof VBox) {
                        VBox card = (VBox) node;
                        double available = grid.getWidth() - 28;
                        double tileWidth = (available - grid.getHgap() * 2) / 3.0;
                        if (tileWidth >= 210) {
                            card.setPrefWidth(tileWidth);
                            card.setMinWidth(0);
                            card.setMaxWidth(Double.MAX_VALUE);
                        }
                    }
                }
            }
        });
        return grid;
    }

    private VBox petSection(String title, String subtitle, String background, TilePane grid) {
        VBox headerText = new VBox(3,
                text(title, 19, true, DARK_GREEN),
                text(subtitle, 12, false, "#688177"));
        VBox section = new VBox(12, headerText, grid);
        section.setPadding(new Insets(14));
        section.setStyle("-fx-background-color:" + background + ";-fx-background-radius:15px;"
                + "-fx-border-color:" + BORDER + ";-fx-border-radius:15px;");
        return section;
    }

    private VBox petCard(String name, String animal, String age, String location,
                         String date, String imageFile, boolean lost) {
        ImageView petImage = loadPetImage(imageFile);
        petImage.setFitWidth(360);
        petImage.setFitHeight(150);
        petImage.setPreserveRatio(true);
        petImage.setSmooth(true);

        // Image stays fully visible inside this fixed-height frame (no zoom/cropping).
        StackPane photoPane = new StackPane(petImage);
        photoPane.setAlignment(Pos.CENTER);
        photoPane.setMinHeight(150);
        photoPane.setPrefHeight(150);
        photoPane.setMaxHeight(150);
        photoPane.setStyle("-fx-background-color:" + MINT + ";-fx-background-radius:10px;");
        photoPane.setClip(null);

        petImage.fitWidthProperty().bind(photoPane.widthProperty().subtract(8));
        petImage.setFitHeight(142);

        Label status = new Label(lost ? "LOST" : "FOUND");
        status.setStyle("-fx-background-color:" + (lost ? "#E83E4B" : GREEN)
                + ";-fx-text-fill:white;-fx-font-weight:bold;-fx-padding:5px 9px;-fx-background-radius:7px;");
        StackPane.setAlignment(status, Pos.TOP_LEFT);
        StackPane.setMargin(status, new Insets(7));
        photoPane.getChildren().add(status);

        Label nameLabel = new Label(name);
        nameLabel.setStyle("-fx-font-size:17px;-fx-font-weight:bold;-fx-text-fill:" + DARK_GREEN + ";");

        Label details = new Label("🐾 " + animal + "  •  " + age);
        details.setWrapText(true);
        details.setStyle("-fx-font-size:12px;-fx-text-fill:#688177;");

        Label place = new Label("📍 " + location);
        place.setWrapText(true);
        place.setStyle("-fx-font-size:12px;-fx-text-fill:#526C60;");

        Label dateLabel = new Label("◷ " + (lost ? "Lost on " : "Found on ") + date);
        dateLabel.setWrapText(true);
        dateLabel.setStyle("-fx-font-size:11px;-fx-text-fill:#526C60;");

        Button detailsButton = new Button("◉ View Details");
        detailsButton.setMaxWidth(Double.MAX_VALUE);
        detailsButton.setStyle(buttonStyle(true));
        detailsButton.setOnAction(e -> showPetDetails(name, animal, age, location, date, imageFile, lost));

        VBox card = new VBox(8, photoPane, nameLabel, details, place, dateLabel, detailsButton);
        card.setPadding(new Insets(10));
        card.setPrefWidth(260);
        card.setMinWidth(0);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setFillWidth(true);
        card.setStyle("-fx-background-color:white;-fx-background-radius:13px;"
                + "-fx-border-color:" + BORDER + ";-fx-border-radius:13px;");

        // Keep the photo frame inside the card's padded content area.
        photoPane.setMinWidth(0);
        photoPane.prefWidthProperty().bind(card.widthProperty().subtract(20));
        photoPane.maxWidthProperty().bind(card.widthProperty().subtract(20));
        card.getProperties().put("name", name);
        card.getProperties().put("animal", animal);
        card.getProperties().put("location", location);
        return card;
    }

    private ImageView loadPetImage(String fileName) {
        String path = "/assets/img/lostfound/" + fileName;
        java.net.URL resource = getClass().getResource(path);
        Image image;
        if (resource != null) {
            image = new Image(resource.toExternalForm(), 0, 0, true, true);
        } else {
            // Friendly placeholder if an image has not been copied into resources yet.
            image = null;
        }

        ImageView view = new ImageView();
        if (image != null && !image.isError()) {
            view.setImage(image);
        } else {
            // Local-resource fallback: the card still renders without an external network request.
            view.setImage(null);
        }
        return view;
    }

    private void showPetDetails(String name, String animal, String age, String location,
                                String date, String imageFile, boolean lost) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle((lost ? "Lost" : "Found") + " Pet Details");
        dialog.setHeaderText(name + " — " + (lost ? "Lost Pet" : "Found Pet"));
        ButtonType closeType = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(closeType);

        ImageView image = loadPetImage(imageFile);
        image.setFitWidth(300);
        image.setFitHeight(200);
        image.setPreserveRatio(true);
        image.setSmooth(true);

        Label status = new Label(lost ? "LOST PET" : "FOUND PET");
        status.setStyle("-fx-background-color:" + (lost ? "#E83E4B" : GREEN)
                + ";-fx-text-fill:white;-fx-font-weight:bold;-fx-padding:6px 12px;-fx-background-radius:8px;");
        Label info = new Label("Animal: " + animal + "\\nAge: " + age
                + "\\nLocation: " + location + "\\n" + (lost ? "Lost on: " : "Found on: ") + date);
        info.setStyle("-fx-font-size:14px;-fx-text-fill:" + TEXT + ";");
        info.setWrapText(true);

        Label address = new Label("📍 Location details\\n" + location
                + "\\n\\nFinder / owner contact details are shared only with permission.\\n"
                + "Please verify ownership before arranging a safe reunion.");
        address.setWrapText(true);
        address.setStyle("-fx-background-color:" + MINT + ";-fx-padding:12px;"
                + "-fx-background-radius:10px;-fx-text-fill:" + TEXT + ";");

        StackPane detailPhoto = new StackPane(image);
        detailPhoto.setAlignment(Pos.CENTER);
        detailPhoto.setPrefSize(320, 205);
        detailPhoto.setMinHeight(205);
        detailPhoto.setStyle("-fx-background-color:" + MINT + ";-fx-background-radius:10px;");
        image.fitWidthProperty().bind(detailPhoto.widthProperty().subtract(8));
        image.setFitHeight(195);

        VBox content = new VBox(12, detailPhoto, status, info, address);
        content.setPadding(new Insets(10));
        content.setPrefWidth(340);
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().setPrefWidth(390);
        dialog.showAndWait();
    }

    private void filterPets(TilePane grid, List<javafx.scene.Node> allPets,
                            String query, String selectedAnimal, String selectedLocation) {
        grid.getChildren().clear();
        for (javafx.scene.Node node : allPets) {
            VBox card = (VBox) node;
            String name = (String) card.getProperties().get("name");
            String animal = (String) card.getProperties().get("animal");
            String location = (String) card.getProperties().get("location");

            boolean queryMatch = query.isEmpty() || name.toLowerCase().contains(query)
                    || animal.toLowerCase().contains(query) || location.toLowerCase().contains(query);
            boolean animalMatch = "All Animals".equals(selectedAnimal)
                    || animal.equalsIgnoreCase(selectedAnimal.endsWith("s")
                    ? selectedAnimal.substring(0, selectedAnimal.length() - 1) : selectedAnimal);
            boolean locationMatch = "All Locations".equals(selectedLocation)
                    || location.equalsIgnoreCase(selectedLocation);

            if (queryMatch && animalMatch && locationMatch) {
                grid.getChildren().add(card);
            }
        }
    }

    private VBox infoCard(String title, String body) {
        Label heading = new Label(title);
        heading.setStyle("-fx-font-size:17px;-fx-font-weight:bold;-fx-text-fill:" + DARK_GREEN + ";");
        Label description = new Label(body);
        description.setWrapText(true);
        description.setStyle("-fx-font-size:13px;-fx-text-fill:#526C60;");
        VBox card = new VBox(12, heading, description);
        card.setPadding(new Insets(16));
        card.setStyle("-fx-background-color:#FFF9E6;-fx-background-radius:15px;"
                + "-fx-border-color:#F1E7BF;-fx-border-radius:15px;");
        return card;
    }

    private <T> ComboBox<T> combo(String prompt, T... values) {
        ComboBox<T> combo = new ComboBox<>();
        combo.getItems().addAll(Arrays.asList(values));
        combo.setValue((T) prompt);
        combo.setPrefHeight(42);
        combo.setMaxWidth(Double.MAX_VALUE);
        combo.setStyle(inputStyle());
        return combo;
    }

    private Text text(String value, double size, boolean bold, String color) {
        Text text = new Text(value);
        text.setFont(Font.font("System", bold ? FontWeight.BOLD : FontWeight.NORMAL, size));
        text.setFill(Color.web(color));
        return text;
    }

    private String inputStyle() {
        return "-fx-font-size:12px;-fx-background-color:white;-fx-border-color:" + BORDER
                + ";-fx-border-radius:8px;-fx-background-radius:8px;-fx-padding:8px;";
    }

    private String buttonStyle(boolean primary) {
        if (primary) {
            return "-fx-background-color:" + GREEN + ";-fx-text-fill:white;-fx-font-weight:bold;"
                    + "-fx-background-radius:9px;-fx-cursor:hand;-fx-padding:9px 12px;";
        }
        return "-fx-background-color:white;-fx-text-fill:" + GREEN + ";-fx-font-weight:bold;"
                + "-fx-border-color:" + BORDER + ";-fx-border-radius:9px;"
                + "-fx-background-radius:9px;-fx-cursor:hand;-fx-padding:9px 12px;";
    }

    private String buttonStyleRed() {
        return "-fx-background-color:#E83E4B;-fx-text-fill:white;-fx-font-weight:bold;"
                + "-fx-background-radius:9px;-fx-cursor:hand;-fx-padding:10px 12px;";
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("JeevMitra - Lost & Found");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
