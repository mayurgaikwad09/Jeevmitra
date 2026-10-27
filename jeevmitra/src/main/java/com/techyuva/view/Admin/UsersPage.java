
package com.techyuva.view.Admin;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.Locale;
import java.util.Optional;

public class UsersPage extends VBox {

    private final String green = "#075747";
    private final String darkGreen = "#034638";
    private final String background = "#F4FAF8";

    private final ObservableList<User> users =
            FXCollections.observableArrayList();

    private final FilteredList<User> filteredUsers =
            new FilteredList<>(users, user -> true);

    private final TableView<User> table = new TableView<>();

    private final Label totalValue = new Label("0");
    private final Label activeValue = new Label("0");
    private final Label blockedValue = new Label("0");

    private TextField searchField;
    private ComboBox<String> statusFilter;

    public UsersPage(Runnable backAction) {

        setSpacing(20);
        setPadding(new Insets(30));
        setFillWidth(true);
        setStyle("-fx-background-color: " + background + ";");

        // Sample users
        users.addAll(
            new User("JM001", "Rahul Sharma",
                "rahul@gmail.com", "9876543210", "Active"),
            new User("JM002", "Priya Patil",
                "priya@gmail.com", "9876543211", "Active"),
            new User("JM003", "Amit Verma",
                "amit@gmail.com", "9876543212", "Blocked"),
            new User("JM004", "Sneha Joshi",
                "sneha@gmail.com", "9876543213", "Active"),
            new User("JM005", "Rohan Deshmukh",
                "rohan@gmail.com", "9876543214", "Active")
        );

        // Back button
        Button backButton = new Button("← Back to Admin Dashboard");
        styleButton(backButton, green);
        backButton.setOnAction(e -> backAction.run());

        Label heading = makeLabel("Users", 30, green, true);
        Label subtitle = makeLabel(
            "Manage registered users in JeevMitra.",
            14, "#607D79", false
        );

        // Statistics
        HBox stats = new HBox(15);
        stats.getChildren().addAll(
            createCard("Total Users", totalValue),
            createCard("Active Users", activeValue),
            createCard("Blocked Users", blockedValue)
        );

        for (javafx.scene.Node node : stats.getChildren()) {
            HBox.setHgrow(node, Priority.ALWAYS);
        }

        // Search
        searchField = new TextField();
        searchField.setPromptText("Search by name, email, phone or ID...");
        searchField.setPrefHeight(40);

        statusFilter = new ComboBox<>();
        statusFilter.getItems().addAll(
            "All Users", "Active", "Blocked"
        );
        statusFilter.setValue("All Users");
        statusFilter.setPrefHeight(40);

        // Action buttons
        Button addButton = new Button("+ Add User");
        styleButton(addButton, green);
        addButton.setOnAction(e -> addUser());

        Button exportButton = new Button("Export CSV");
        styleButton(exportButton, "#2876C7");
        exportButton.setOnAction(e -> exportUsers());

        HBox searchRow = new HBox(10,
            searchField, statusFilter, addButton, exportButton
        );
        searchRow.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(searchField, Priority.ALWAYS);

        // Table
        configureTable();
        table.setItems(filteredUsers);
        table.setColumnResizePolicy(
            TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN
        );
        table.setPrefHeight(400);
        table.setMinHeight(300);
        table.setPlaceholder(new Label("No users found."));

        Label tableHeading =
            makeLabel("Registered Users", 20, green, true);

        Label tableSubtitle = makeLabel(
            "View, add, edit or manage user accounts.",
            13, "#607D79", false
        );

        VBox tableCard = new VBox(
            12, tableHeading, tableSubtitle, table
        );
        tableCard.setPadding(new Insets(20));
        tableCard.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 14;" +
            "-fx-border-color: #E1ECE7;" +
            "-fx-border-radius: 14;"
        );

        searchField.textProperty().addListener(
            (obs, oldValue, newValue) -> applyFilters()
        );

        statusFilter.setOnAction(e -> applyFilters());

        updateStatistics();

        getChildren().addAll(
            backButton, heading, subtitle, stats,
            searchRow, tableCard
        );

        VBox.setVgrow(tableCard, Priority.ALWAYS);
    }

    private void configureTable() {

        TableColumn<User, String> idCol =
            new TableColumn<>("User ID");
        idCol.setCellValueFactory(
            d -> d.getValue().idProperty()
        );

        TableColumn<User, String> nameCol =
            new TableColumn<>("Name");
        nameCol.setCellValueFactory(
            d -> d.getValue().nameProperty()
        );

        TableColumn<User, String> emailCol =
            new TableColumn<>("Email");
        emailCol.setCellValueFactory(
            d -> d.getValue().emailProperty()
        );

        TableColumn<User, String> phoneCol =
            new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(
            d -> d.getValue().phoneProperty()
        );

        TableColumn<User, String> statusCol =
            new TableColumn<>("Status");
        statusCol.setCellValueFactory(
            d -> d.getValue().statusProperty()
        );

        statusCol.setCellFactory(col ->
            new TableCell<User, String>() {
                @Override
                protected void updateItem(String status, boolean empty) {
                    super.updateItem(status, empty);

                    if (empty || status == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(status);
                        setStyle(status.equals("Active")
                            ? "-fx-text-fill: #16804A; -fx-font-weight: bold;"
                            : "-fx-text-fill: #C25B48; -fx-font-weight: bold;"
                        );
                    }
                }
            }
        );

        TableColumn<User, Void> actionsCol =
            new TableColumn<>("Actions");

        actionsCol.setCellFactory(col ->
            new TableCell<User, Void>() {

                private final Button viewButton = new Button("View");
                private final Button editButton = new Button("Edit");
                private final Button statusButton = new Button();
                private final Button deleteButton = new Button("Delete");

                private final HBox buttons = new HBox(
                    5, viewButton, editButton, statusButton, deleteButton
                );

                {
                    styleButton(viewButton, "#2876C7");
                    styleButton(editButton, green);
                    styleButton(statusButton, "#B7791F");
                    styleButton(deleteButton, "#B93838");

                    viewButton.setOnAction(e -> {
                        User user = getCurrentUser();
                        if (user != null) showDetails(user);
                    });

                    editButton.setOnAction(e -> {
                        User user = getCurrentUser();
                        if (user != null) editUser(user);
                    });

                    statusButton.setOnAction(e -> {
                        User user = getCurrentUser();
                        if (user != null) changeStatus(user);
                    });

                    deleteButton.setOnAction(e -> {
                        User user = getCurrentUser();
                        if (user != null) deleteUser(user);
                    });
                }

                private User getCurrentUser() {
                    if (getIndex() < 0 ||
                        getIndex() >= getTableView().getItems().size()) {
                        return null;
                    }
                    return getTableView().getItems().get(getIndex());
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);

                    User user = empty ? null : getCurrentUser();

                    if (user == null) {
                        setGraphic(null);
                    } else {
                        statusButton.setText(
                            user.getStatus().equals("Active")
                                ? "Block" : "Unblock"
                        );
                        setGraphic(buttons);
                    }
                }
            }
        );

        idCol.setMinWidth(75);
        nameCol.setMinWidth(110);
        emailCol.setMinWidth(150);
        phoneCol.setMinWidth(105);
        statusCol.setMinWidth(80);
        actionsCol.setMinWidth(290);

        table.getColumns().addAll(
            idCol, nameCol, emailCol, phoneCol, statusCol, actionsCol
        );
    }

    private void addUser() {

        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Add New User");
        dialog.setHeaderText("Enter the user's details");

        ButtonType saveType = new ButtonType(
            "Add User", ButtonBar.ButtonData.OK_DONE
        );
        dialog.getDialogPane().getButtonTypes().addAll(
            saveType, ButtonType.CANCEL
        );

        TextField name = new TextField();
        name.setPromptText("Full name");

        TextField email = new TextField();
        email.setPromptText("Email address");

        TextField phone = new TextField();
        phone.setPromptText("10-digit phone number");

        VBox form = new VBox(12,
            new Label("Full Name"), name,
            new Label("Email"), email,
            new Label("Phone"), phone
        );
        form.setPadding(new Insets(15));
        dialog.getDialogPane().setContent(form);

        dialog.setResultConverter(button -> {
            if (button == saveType) {
                String n = name.getText().trim();
                String em = email.getText().trim();
                String ph = phone.getText().trim();

                if (n.isEmpty() || em.isEmpty() || ph.isEmpty()) {
                    showMessage("Please fill in all fields.");
                    return null;
                }

                if (!em.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
                    showMessage("Enter a valid email address.");
                    return null;
                }

                if (!ph.matches("\\d{10}")) {
                    showMessage("Phone number must contain 10 digits.");
                    return null;
                }

                boolean duplicate = users.stream().anyMatch(u ->
                    u.getEmail().equalsIgnoreCase(em) ||
                    u.getPhone().equals(ph)
                );

                if (duplicate) {
                    showMessage("This email or phone number already exists.");
                    return null;
                }

                return new User(
                    nextUserId(), n, em, ph, "Active"
                );
            }
            return null;
        });

        Optional<User> result = dialog.showAndWait();
        result.ifPresent(user -> {
            users.add(user);
            applyFilters();
            updateStatistics();
            table.refresh();
        });
    }

    private void editUser(User user) {

        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Edit User");
        dialog.setHeaderText("Update " + user.getName());

        ButtonType saveType = new ButtonType(
            "Save Changes", ButtonBar.ButtonData.OK_DONE
        );
        dialog.getDialogPane().getButtonTypes().addAll(
            saveType, ButtonType.CANCEL
        );

        TextField name = new TextField(user.getName());
        TextField email = new TextField(user.getEmail());
        TextField phone = new TextField(user.getPhone());

        VBox form = new VBox(12,
            new Label("Full Name"), name,
            new Label("Email"), email,
            new Label("Phone"), phone
        );
        form.setPadding(new Insets(15));
        dialog.getDialogPane().setContent(form);

        dialog.setResultConverter(button -> {
            if (button == saveType) {
                String n = name.getText().trim();
                String em = email.getText().trim();
                String ph = phone.getText().trim();

                if (n.isEmpty() || em.isEmpty() || ph.isEmpty()) {
                    showMessage("Please fill in all fields.");
                    return null;
                }

                if (!em.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
                    showMessage("Enter a valid email address.");
                    return null;
                }

                if (!ph.matches("\\d{10}")) {
                    showMessage("Phone number must contain 10 digits.");
                    return null;
                }

                boolean duplicate = users.stream().anyMatch(u ->
                    u != user &&
                    (u.getEmail().equalsIgnoreCase(em) ||
                     u.getPhone().equals(ph))
                );

                if (duplicate) {
                    showMessage("Another user already has this email or phone.");
                    return null;
                }

                user.setName(n);
                user.setEmail(em);
                user.setPhone(ph);
                return user;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(updated -> {
            applyFilters();
            updateStatistics();
            table.refresh();
        });
    }

    private void deleteUser(User user) {

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Delete User");
        confirmation.setHeaderText("Delete " + user.getName() + "?");
        confirmation.setContentText(
            "This will remove the user from the current list."
        );

        confirmation.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                users.remove(user);
                applyFilters();
                updateStatistics();
            }
        });
    }

    private void changeStatus(User user) {

        boolean blocking = user.getStatus().equals("Active");

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Action");
        confirmation.setHeaderText(
            (blocking ? "Block " : "Unblock ") + user.getName() + "?"
        );
        confirmation.setContentText(
            blocking
                ? "Do you want to block this user?"
                : "Do you want to unblock this user?"
        );

        confirmation.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                user.setStatus(blocking ? "Blocked" : "Active");
                applyFilters();
                updateStatistics();
                table.refresh();
            }
        });
    }

    private void showDetails(User user) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("User Profile");
        alert.setHeaderText(user.getName());

        alert.setContentText(
            "User ID: " + user.getId() +
            "\nName: " + user.getName() +
            "\nEmail: " + user.getEmail() +
            "\nPhone: " + user.getPhone() +
            "\nStatus: " + user.getStatus()
        );

        alert.showAndWait();
    }

    private void applyFilters() {

        String query = searchField.getText() == null
            ? "" : searchField.getText().trim().toLowerCase(Locale.ROOT);

        String status = statusFilter.getValue();

        filteredUsers.setPredicate(user -> {
            boolean matchesSearch =
                user.getId().toLowerCase(Locale.ROOT).contains(query) ||
                user.getName().toLowerCase(Locale.ROOT).contains(query) ||
                user.getEmail().toLowerCase(Locale.ROOT).contains(query) ||
                user.getPhone().contains(query);

            boolean matchesStatus =
                status == null || status.equals("All Users") ||
                user.getStatus().equals(status);

            return matchesSearch && matchesStatus;
        });
    }

    private void updateStatistics() {

        totalValue.setText(String.valueOf(users.size()));

        activeValue.setText(String.valueOf(
            users.stream()
                .filter(u -> u.getStatus().equals("Active"))
                .count()
        ));

        blockedValue.setText(String.valueOf(
            users.stream()
                .filter(u -> u.getStatus().equals("Blocked"))
                .count()
        ));
    }

    private String nextUserId() {
        int max = 0;

        for (User user : users) {
            try {
                max = Math.max(max,
                    Integer.parseInt(user.getId().substring(2)));
            } catch (Exception ignored) {
                // Ignore IDs that do not follow the JM number format.
            }
        }

        return String.format("JM%03d", max + 1);
    }

    private void exportUsers() {

        javafx.stage.FileChooser chooser =
            new javafx.stage.FileChooser();

        chooser.setTitle("Export Users");
        chooser.setInitialFileName("JeevMitra_Users.csv");
        chooser.getExtensionFilters().add(
            new javafx.stage.FileChooser.ExtensionFilter(
                "CSV Files", "*.csv"
            )
        );

        java.io.File file = chooser.showSaveDialog(
            getScene() == null ? null : getScene().getWindow()
        );

        if (file == null) return;

        try (java.io.PrintWriter writer =
                 new java.io.PrintWriter(file, "UTF-8")) {

            writer.println("User ID,Name,Email,Phone,Status");

            for (User user : users) {
                writer.println(
                    csv(user.getId()) + "," +
                    csv(user.getName()) + "," +
                    csv(user.getEmail()) + "," +
                    csv(user.getPhone()) + "," +
                    csv(user.getStatus())
                );
            }

            showMessage("Users exported successfully.");

        } catch (Exception ex) {
            showMessage("Could not export the file: " + ex.getMessage());
        }
    }

    private String csv(String value) {
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    private VBox createCard(String title, Label value) {

        Label titleLabel = makeLabel(title, 13, "#607D79", false);

        value.setFont(Font.font("Arial", FontWeight.BOLD, 27));
        value.setTextFill(Color.web(green));

        VBox card = new VBox(12, titleLabel, value);
        card.setPadding(new Insets(20));
        card.setMinHeight(105);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: #E1ECE7;" +
            "-fx-border-radius: 12;"
        );

        return card;
    }

    private void styleButton(Button button, String color) {
        button.setTextFill(Color.WHITE);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        button.setPadding(new Insets(10, 13, 10, 13));
        button.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 7;" +
            "-fx-cursor: hand;"
        );
    }

    private void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private Label makeLabel(
        String value, int size, String color, boolean bold
    ) {
        Label label = new Label(value);
        label.setFont(bold
            ? Font.font("Arial", FontWeight.BOLD, size)
            : Font.font("Arial", size));
        label.setTextFill(Color.web(color));
        label.setWrapText(true);
        return label;
    }

    public static class User {

        private final SimpleStringProperty id;
        private final SimpleStringProperty name;
        private final SimpleStringProperty email;
        private final SimpleStringProperty phone;
        private final SimpleStringProperty status;

        public User(
            String id, String name, String email,
            String phone, String status
        ) {
            this.id = new SimpleStringProperty(id);
            this.name = new SimpleStringProperty(name);
            this.email = new SimpleStringProperty(email);
            this.phone = new SimpleStringProperty(phone);
            this.status = new SimpleStringProperty(status);
        }

        public javafx.beans.property.StringProperty idProperty() {
            return id;
        }

        public javafx.beans.property.StringProperty nameProperty() {
            return name;
        }

        public javafx.beans.property.StringProperty emailProperty() {
            return email;
        }

        public javafx.beans.property.StringProperty phoneProperty() {
            return phone;
        }

        public javafx.beans.property.StringProperty statusProperty() {
            return status;
        }

        public String getId() { return id.get(); }
        public String getName() { return name.get(); }
        public String getEmail() { return email.get(); }
        public String getPhone() { return phone.get(); }
        public String getStatus() { return status.get(); }

        public void setName(String value) { name.set(value); }
        public void setEmail(String value) { email.set(value); }
        public void setPhone(String value) { phone.set(value); }
        public void setStatus(String value) { status.set(value); }
    }
}
