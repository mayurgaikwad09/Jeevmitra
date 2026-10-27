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

public class MarketplacePage {
    private static final String GREEN = "#087653";
    private static final String DARK = "#073D30";
    private static final String BG = "#F2FAF6";
    private static final String BORDER = "#DDECE4";
    private static final String MINT = "#E4F5EB";

    private final List<Product> products = new ArrayList<>();
    private final List<ProductCard> productCards = new ArrayList<>();
    private TilePane productGrid;
    private TextField searchField;
    private ComboBox<String> categoryFilter;
    private ComboBox<String> sortFilter;
    private Label resultLabel;
    private Label selectedProductLabel;
    private Label selectedPriceLabel;
    private Label selectedDescriptionLabel;
    private ImageView selectedImageView;
    private Spinner<Integer> quantitySpinner;
    private TextArea deliveryAddress;

    public Scene getMarketplaceScene(Runnable backToHome) {
        loadProducts();
        productCards.clear();

        Button back = new Button("← Back");
        back.setStyle(outlineButtonStyle());
        back.setOnAction(e -> {
            if (backToHome != null) backToHome.run();
        });

        VBox title = new VBox(3,
                text("🛒 Marketplace", 29, true, DARK),
                text("Quality products for a healthier and happier life for your pets.", 12, false, "#42675A"));
        HBox heading = new HBox(16, back, title);
        heading.setAlignment(Pos.CENTER_LEFT);

        VBox hero = new VBox(6,
                text("🐶  🐱", 27, true, GREEN),
                text("Care • Shop • Support for a Better Tomorrow.", 12, true, DARK));
        hero.setAlignment(Pos.CENTER);
        hero.setPadding(new Insets(10));
        hero.setStyle("-fx-background-color:linear-gradient(to right,#E7F8E8,#D6F3E4);-fx-background-radius:14px;");

        searchField = new TextField();
        searchField.setPromptText("⌕  Search products (food, toys, accessories...)");
        searchField.setPrefHeight(42);
        searchField.setStyle(inputStyle());

        categoryFilter = new ComboBox<>();
        categoryFilter.getItems().addAll("All Categories", "Food", "Accessories", "Toys", "Health Care", "Hygiene");
        categoryFilter.setValue("All Categories");
        categoryFilter.setPrefHeight(42);
        categoryFilter.setMaxWidth(Double.MAX_VALUE);
        categoryFilter.setStyle(inputStyle());

        sortFilter = new ComboBox<>();
        sortFilter.getItems().addAll("Sort by", "Price: Low to High", "Price: High to Low", "Name: A to Z");
        sortFilter.setValue("Sort by");
        sortFilter.setPrefHeight(42);
        sortFilter.setMaxWidth(Double.MAX_VALUE);
        sortFilter.setStyle(inputStyle());

        Button searchButton = new Button("⌕ Search");
        searchButton.setStyle(primaryButtonStyle());
        Button resetButton = new Button("Reset");
        resetButton.setStyle(outlineButtonStyle());

        VBox searchControls = new VBox(8,
                searchField,
                new HBox(8, categoryFilter, sortFilter, searchButton, resetButton));
        HBox filterButtons = (HBox) searchControls.getChildren().get(1);
        filterButtons.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(categoryFilter, Priority.ALWAYS);
        HBox.setHgrow(sortFilter, Priority.ALWAYS);
        categoryFilter.setPrefWidth(140);
        sortFilter.setPrefWidth(130);

        FlowPane chips = new FlowPane(8, 8);
        chips.setAlignment(Pos.CENTER_LEFT);
        for (String chipName : new String[]{"All Products", "Food", "Accessories", "Toys", "Health Care", "Hygiene"}) {
            Button chip = new Button(chipName);
            chip.setStyle(chipName.equals("All Products") ? primaryButtonStyle() : outlineButtonStyle());
            chip.setOnAction(e -> {
                categoryFilter.setValue(chipName.equals("All Products") ? "All Categories" : chipName);
                applyFilters();
                for (javafx.scene.Node node : chips.getChildren()) {
                    ((Button) node).setStyle(node == chip ? primaryButtonStyle() : outlineButtonStyle());
                }
            });
            chips.getChildren().add(chip);
        }

        productGrid = new TilePane();
        productGrid.setHgap(12);
        productGrid.setVgap(12);
        productGrid.setPrefColumns(4);
        productGrid.setAlignment(Pos.TOP_LEFT);

        for (Product product : products) {
            ProductCard card = createProductCard(product);
            productCards.add(card);
            productGrid.getChildren().add(card.root);
        }

        resultLabel = new Label("All Products (" + products.size() + ")");
        resultLabel.setStyle("-fx-font-size:17px;-fx-font-weight:bold;-fx-text-fill:" + DARK + ";");

        searchButton.setOnAction(e -> applyFilters());
        resetButton.setOnAction(e -> {
            searchField.clear();
            categoryFilter.setValue("All Categories");
            sortFilter.setValue("Sort by");
            applyFilters();
        });

        VBox orderPanel = createOrderPanel();
        VBox productsColumn = new VBox(12, searchControls, chips, resultLabel, productGrid);
        productsColumn.setMinWidth(0);
        productsColumn.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(productsColumn, Priority.ALWAYS);

        VBox rightColumn = new VBox(13,
                orderPanel,
                infoCard("🛍 Why Shop at JeevMitra?",
                        "✓ Quality products for your pet's health\n✓ Trusted and safe pet supplies\n✓ Support our animal welfare mission\n✓ Affordable and genuine products"),
                footerPetCard());
        rightColumn.setPrefWidth(270);
        rightColumn.setMinWidth(250);
        rightColumn.setMaxWidth(280);

        HBox content = new HBox(14, productsColumn, rightColumn);
        VBox main = new VBox(14, heading, hero, content);
        main.setPadding(new Insets(18));
        main.setStyle("-fx-background-color:" + BG + ";");

        ScrollPane scroll = new ScrollPane(main);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setStyle("-fx-background:" + BG + ";-fx-background-color:" + BG + ";-fx-padding:0;");

        // Uses your existing Sidebar.java; no duplicate sidebar is created.
        VBox sidebar = Sidebar.create("Marketplace");
        sidebar.setPrefWidth(260);
        sidebar.setMinWidth(260);
        sidebar.setMaxWidth(260);

        HBox root = new HBox(0, sidebar, scroll);
        HBox.setHgrow(scroll, Priority.ALWAYS);
        root.setStyle("-fx-background-color:" + BG + ";");
        return new Scene(root, 1300, 760);
    }

    private void loadProducts() {
        products.clear();
        products.add(new Product("Royal Canin Dog Food", "Food", 1299, "4.8",
                "Complete nutrition for adult dogs.", "dog_food.png"));
        products.add(new Product("Whiskas Cat Food", "Food", 499, "4.6",
                "Nutritious & tasty food for cats.", "cat_food.png"));
        products.add(new Product("Pet Collar", "Accessories", 299, "4.7",
                "Comfortable and durable collar.", "pet_collars.png"));
        products.add(new Product("Pet Toy Set", "Toys", 399, "4.5",
                "Fun and safe toys for your pet.", "pet_toys.png"));
        products.add(new Product("Multivitamin Tablets", "Health Care", 599, "4.7",
                "Supports overall health.", "vitamins.png"));
        products.add(new Product("Pet Shampoo", "Hygiene", 349, "4.6",
                "Gentle and skin-friendly.", "pet_shampoo.png"));
        products.add(new Product("Pet Bed", "Accessories", 899, "4.8",
                "Soft and comfortable bed.", "pet_beds.png"));
        products.add(new Product("Dog Treats", "Food", 199, "4.4",
                "Healthy & tasty treats.", "pet_treats.png"));
    }

    private ProductCard createProductCard(Product p) {
        ImageView image = loadImage(p.imageFile, 200, 135);
        StackPane imageBox = new StackPane(image);
        imageBox.setMinHeight(135);
        imageBox.setPrefHeight(135);
        imageBox.setStyle("-fx-background-color:" + MINT + ";-fx-background-radius:9px;");

        Label cat = new Label(p.category);
        cat.setStyle("-fx-background-color:" + GREEN + ";-fx-text-fill:white;-fx-font-size:10px;"
                + "-fx-font-weight:bold;-fx-padding:5px 8px;-fx-background-radius:8px;");
        StackPane.setAlignment(cat, Pos.TOP_LEFT);
        StackPane.setMargin(cat, new Insets(6));
        imageBox.getChildren().add(cat);

        Label name = new Label(p.name);
        name.setWrapText(true);
        name.setStyle("-fx-font-size:14px;-fx-font-weight:bold;-fx-text-fill:" + DARK + ";");

        Label price = new Label("₹" + String.format("%,d", p.price));
        price.setStyle("-fx-font-size:18px;-fx-font-weight:bold;-fx-text-fill:#092B25;");
        Label rating = new Label("⭐ " + p.rating);
        rating.setStyle("-fx-font-size:12px;-fx-text-fill:#324C43;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox priceRow = new HBox(6, price, spacer, rating);

        Label desc = new Label(p.description);
        desc.setWrapText(true);
        desc.setMinHeight(32);
        desc.setStyle("-fx-font-size:11px;-fx-text-fill:#597268;");

        Button order = new Button("🛒 Order Now");
        order.setMaxWidth(Double.MAX_VALUE);
        order.setStyle(primaryButtonStyle());
        order.setOnAction(e -> selectProduct(p));

        VBox card = new VBox(7, imageBox, name, priceRow, desc, order);
        card.setPadding(new Insets(9));
        card.setPrefWidth(160);
        card.setMinWidth(145);
        card.setMaxWidth(180);
        card.setStyle("-fx-background-color:white;-fx-background-radius:13px;"
                + "-fx-border-color:" + BORDER + ";-fx-border-radius:13px;");
        return new ProductCard(p, card);
    }

    private VBox createOrderPanel() {
        VBox header = new VBox(5,
                text("🛒 Place Your Order", 18, true, DARK),
                text("Fill the details to order your selected product.", 11, false, "#42675A"));
        header.setPadding(new Insets(13));
        header.setStyle("-fx-background-color:#DDF6E7;-fx-background-radius:13px 13px 0 0;");

        selectedImageView = loadImage("dog_food.png", 82, 80);
        selectedProductLabel = new Label("Royal Canin Dog Food");
        selectedProductLabel.setWrapText(true);
        selectedProductLabel.setStyle("-fx-font-size:13px;-fx-font-weight:bold;-fx-text-fill:" + DARK + ";");
        selectedPriceLabel = new Label("₹1,299");
        selectedPriceLabel.setStyle("-fx-font-size:18px;-fx-font-weight:bold;-fx-text-fill:" + DARK + ";");
        selectedDescriptionLabel = new Label("Complete nutrition for adult dogs.");
        selectedDescriptionLabel.setWrapText(true);
        selectedDescriptionLabel.setStyle("-fx-font-size:11px;-fx-text-fill:#597268;");

        VBox selectedDetails = new VBox(5, selectedProductLabel, selectedPriceLabel, selectedDescriptionLabel);
        HBox selectedRow = new HBox(10, selectedImageView, selectedDetails);
        selectedRow.setAlignment(Pos.CENTER_LEFT);

        quantitySpinner = new Spinner<>(1, 99, 1);
        quantitySpinner.setEditable(true);
        quantitySpinner.setPrefWidth(125);
        Button minus = new Button("−");
        Button plus = new Button("+");
        minus.setOnAction(e -> { if (quantitySpinner.getValue() > 1) quantitySpinner.decrement(); });
        plus.setOnAction(e -> { if (quantitySpinner.getValue() < 99) quantitySpinner.increment(); });
        HBox quantityRow = new HBox(8, minus, quantitySpinner, plus);
        quantityRow.setAlignment(Pos.CENTER);

        deliveryAddress = new TextArea();
        deliveryAddress.setPromptText("📍 Enter your full address");
        deliveryAddress.setPrefRowCount(2);
        deliveryAddress.setWrapText(true);
        deliveryAddress.setStyle(inputStyle());

        Button placeOrder = new Button("🛒 Place Order");
        placeOrder.setMaxWidth(Double.MAX_VALUE);
        placeOrder.setStyle(primaryButtonStyle());
        placeOrder.setOnAction(e -> placeOrder());

        VBox form = new VBox(10,
                selectedRow,
                text("Quantity", 12, true, DARK), quantityRow,
                text("Delivery Address", 12, true, DARK), deliveryAddress, placeOrder);
        form.setPadding(new Insets(13));
        VBox panel = new VBox(0, header, form);
        panel.setStyle("-fx-background-color:white;-fx-background-radius:13px;"
                + "-fx-border-color:" + BORDER + ";-fx-border-radius:13px;");
        return panel;
    }

    private void selectProduct(Product p) {
        selectedProductLabel.setText(p.name);
        selectedPriceLabel.setText("₹" + String.format("%,d", p.price));
        selectedDescriptionLabel.setText(p.description);
        selectedImageView.setImage(loadImage(p.imageFile, 82, 80).getImage());
        quantitySpinner.getValueFactory().setValue(1);
    }

    private void applyFilters() {
        String query = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase();
        String category = categoryFilter.getValue();
        String sort = sortFilter.getValue();

        List<ProductCard> filtered = new ArrayList<>();
        for (ProductCard card : productCards) {
            boolean textMatch = query.isEmpty()
                    || card.product.name.toLowerCase().contains(query)
                    || card.product.category.toLowerCase().contains(query);
            boolean categoryMatch = "All Categories".equals(category)
                    || card.product.category.equals(category);
            if (textMatch && categoryMatch) filtered.add(card);
        }

        if ("Price: Low to High".equals(sort)) {
            filtered.sort((a, b) -> Integer.compare(a.product.price, b.product.price));
        } else if ("Price: High to Low".equals(sort)) {
            filtered.sort((a, b) -> Integer.compare(b.product.price, a.product.price));
        } else if ("Name: A to Z".equals(sort)) {
            filtered.sort((a, b) -> a.product.name.compareToIgnoreCase(b.product.name));
        }

        List<javafx.scene.Node> nodes = new ArrayList<>();
        for (ProductCard card : filtered) nodes.add(card.root);
        productGrid.getChildren().setAll(nodes);
        resultLabel.setText(filtered.isEmpty() ? "No products found."
                : "Products (" + filtered.size() + ")");
    }

    private void placeOrder() {
        String name = selectedProductLabel.getText();
        String address = deliveryAddress.getText() == null ? "" : deliveryAddress.getText().trim();

        if (address.isEmpty()) {
            showAlert("Delivery Address Required", "Please enter your full delivery address.");
            return;
        }

        int quantity;
        try {
            quantity = quantitySpinner.getValue();
        } catch (Exception ex) {
            showAlert("Invalid Quantity", "Please enter a valid quantity.");
            return;
        }

        Product selected = products.get(0);
        for (Product p : products) {
            if (p.name.equals(name)) {
                selected = p;
                break;
            }
        }

        showAlert("Order Preview",
                "Product: " + selected.name
                        + "\nQuantity: " + quantity
                        + "\nTotal: ₹" + String.format("%,d", selected.price * quantity)
                        + "\nDelivery Address: " + address
                        + "\n\nDemo only: this order is not saved to a database.");
    }

    private VBox infoCard(String title, String body) {
        Label heading = new Label(title);
        heading.setStyle("-fx-font-size:16px;-fx-font-weight:bold;-fx-text-fill:" + DARK + ";");
        Label desc = new Label(body);
        desc.setWrapText(true);
        desc.setStyle("-fx-font-size:12px;-fx-text-fill:#38594C;");
        VBox box = new VBox(10, heading, desc);
        box.setPadding(new Insets(15));
        box.setStyle("-fx-background-color:#FFF9E5;-fx-background-radius:13px;"
                + "-fx-border-color:#F3EAC7;-fx-border-radius:13px;");
        return box;
    }

    private VBox footerPetCard() {
        VBox box = new VBox(6,
                text("🐶  🐱  ❤️", 25, true, GREEN),
                text("Happy Pets. Healthy Lives. Stronger Communities.", 13, true, DARK));
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(12));
        box.setStyle("-fx-background-color:linear-gradient(to right,#E5F8E8,#D7F2E4);-fx-background-radius:13px;");
        return box;
    }

    // Images are loaded from: src/main/resources/assets/img/products/
    private ImageView loadImage(String fileName, double width, double height) {
        ImageView view = new ImageView();
        String resourcePath = "/assets/img/products/" + fileName;
        try (InputStream stream = getClass().getResourceAsStream(resourcePath)) {
            if (stream != null) {
                view.setImage(new Image(stream));
            } else {
                System.out.println("Image not found: " + resourcePath);
            }
        } catch (Exception ex) {
            System.out.println("Unable to load image " + resourcePath + ": " + ex.getMessage());
        }
        view.setFitWidth(width);
        view.setFitHeight(height);
        view.setPreserveRatio(true);
        view.setSmooth(true);
        return view;
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
        alert.setTitle("JeevMitra Marketplace");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private static class Product {
        final String name;
        final String category;
        final int price;
        final String rating;
        final String description;
        final String imageFile;

        Product(String name, String category, int price, String rating, String description, String imageFile) {
            this.name = name;
            this.category = category;
            this.price = price;
            this.rating = rating;
            this.description = description;
            this.imageFile = imageFile;
        }
    }

    private static class ProductCard {
        final Product product;
        final VBox root;

        ProductCard(Product product, VBox root) {
            this.product = product;
            this.root = root;
        }
    }
}
