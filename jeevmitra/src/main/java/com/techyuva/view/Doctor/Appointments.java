
package com.techyuva.view.Doctor;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Appointments {

    private final ObservableList<Appointment> data =
            FXCollections.observableArrayList();

    private TableView<Appointment> table;
    private TextField search;
    private ComboBox<String> filter;

    public Scene createScene(Stage stage, Runnable backAction) {
        if (data.isEmpty()) {
            data.addAll(
                new Appointment("AP-101", "Buddy", "Rahul Patil",
                    "General Check-up", "2026-10-25", "10:00 AM", "Scheduled"),
                new Appointment("AP-102", "Milo", "Sneha Joshi",
                    "Vaccination", "2026-10-25", "11:30 AM", "Scheduled"),
                new Appointment("AP-103", "Coco", "Amit Shah",
                    "Follow-up", "2026-10-26", "02:00 PM", "Completed")
            );
        }

        BorderPane root = base("📅 Appointments", "MANAGE VISITS");

        Button back = lightButton("← Back to Dashboard");
        back.setOnAction(e -> backAction.run());

        Label heading = heading("Appointment Management");
        Label sub = muted("Review visits, update statuses and schedule appointments.");

        search = new TextField();
        search.setPromptText("Search pet, owner or appointment ID...");
        search.setPrefHeight(42);

        filter = new ComboBox<>();
        filter.getItems().addAll("All Statuses", "Scheduled",
                "Completed", "Cancelled");
        filter.setValue("All Statuses");
        filter.setPrefHeight(42);

        Button add = primaryButton("+ New Appointment");
        add.setOnAction(e -> showForm());

        HBox tools = new HBox(12, search, filter, add);
        tools.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(search, Priority.ALWAYS);

        table = new TableView<>();
        table.setItems(data);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        table.setPlaceholder(new Label("No matching appointments found."));

        addColumn("Appointment ID", "id");
        addColumn("Pet Name", "pet");
        addColumn("Owner", "owner");
        addColumn("Visit Type", "type");
        addColumn("Date", "date");
        addColumn("Time", "time");
        addColumn("Status", "status");

        search.textProperty().addListener((o, old, value) -> refresh());
        filter.setOnAction(e -> refresh());

        Button complete = primaryButton("✓ Mark Completed");
        Button cancel = lightButton("Cancel Appointment");
        Button view = lightButton("View Details");

        complete.setOnAction(e -> updateStatus("Completed"));
        cancel.setOnAction(e -> updateStatus("Cancelled"));
        view.setOnAction(e -> viewDetails());

        HBox actions = new HBox(12, view, complete, cancel);
        actions.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(18, tools, table, actions);
        card.setPadding(new Insets(22));
        card.setStyle("-fx-background-color:white;"
                + "-fx-background-radius:18;-fx-border-color:#DCEAE0;"
                + "-fx-border-radius:18;");
        VBox.setVgrow(table, Priority.ALWAYS);

        VBox content = new VBox(22, back, heading, sub, card);
        content.setPadding(new Insets(28, 35, 30, 35));
        VBox.setVgrow(card, Priority.ALWAYS);

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background:#F2F7F3;"
                + "-fx-background-color:#F2F7F3;"
                + "-fx-border-color:transparent;");
        root.setCenter(scroll);

        return new Scene(root);
    }

    private BorderPane base(String title, String tag) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:#F2F7F3;");

        Label logo = new Label("🐾 JeevMitra");
        logo.setTextFill(Color.WHITE);
        logo.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 25));

        Region gap = new Region();
        HBox.setHgrow(gap, Priority.ALWAYS);

        Label right = new Label(tag);
        right.setTextFill(Color.WHITE);
        right.setStyle("-fx-background-color:#286B50;"
                + "-fx-background-radius:18;-fx-padding:10 16;");

        HBox header = new HBox(15, logo, gap, right);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(20, 32, 20, 32));
        header.setStyle("-fx-background-color:linear-gradient(to right,"
                + "#103F30,#21845C);");
        root.setTop(header);
        return root;
    }

    private void addColumn(String title, String key) {
        TableColumn<Appointment, String> col = new TableColumn<>(title);
        col.setCellValueFactory(cell -> cell.getValue().value(key));
        table.getColumns().add(col);
    }

    private void refresh() {
        String q = search.getText() == null ? "" :
                search.getText().toLowerCase().trim();
        String selected = filter.getValue();

        ObservableList<Appointment> result =
                FXCollections.observableArrayList();

        for (Appointment a : data) {
            boolean matches = (a.pet.get().toLowerCase().contains(q)
                    || a.owner.get().toLowerCase().contains(q)
                    || a.id.get().toLowerCase().contains(q));

            boolean statusMatches = selected.equals("All Statuses")
                    || a.status.get().equals(selected);

            if (matches && statusMatches) result.add(a);
        }
        table.setItems(result);
    }

    private void showForm() {
        Dialog<Appointment> dialog = new Dialog<>();
        dialog.setTitle("New Appointment");
        dialog.setHeaderText("Schedule a veterinary visit");

        ButtonType saveType = new ButtonType("Save Appointment",
                ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(
                saveType, ButtonType.CANCEL);

        TextField pet = new TextField();
        pet.setPromptText("Pet name");
        TextField owner = new TextField();
        owner.setPromptText("Owner name");
        TextField type = new TextField();
        type.setPromptText("Visit type");
        DatePicker date = new DatePicker();
        TextField time = new TextField();
        time.setPromptText("e.g. 10:30 AM");

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(12);
        form.addRow(0, new Label("Pet Name"), pet);
        form.addRow(1, new Label("Owner"), owner);
        form.addRow(2, new Label("Visit Type"), type);
        form.addRow(3, new Label("Date"), date);
        form.addRow(4, new Label("Time"), time);
        dialog.getDialogPane().setContent(form);

        dialog.setResultConverter(button -> {
            if (button != saveType) return null;

            if (pet.getText().trim().isEmpty()
                    || owner.getText().trim().isEmpty()
                    || type.getText().trim().isEmpty()
                    || date.getValue() == null
                    || time.getText().trim().isEmpty()) {
                new Alert(Alert.AlertType.WARNING,
                        "Please complete all appointment fields.").showAndWait();
                return null;
            }

            return new Appointment("AP-" + (101 + data.size()),
                    pet.getText().trim(), owner.getText().trim(),
                    type.getText().trim(), date.getValue().toString(),
                    time.getText().trim(), "Scheduled");
        });

        dialog.showAndWait().ifPresent(a -> {
            data.add(a);
            refresh();
        });
    }

    private void updateStatus(String status) {
        Appointment a = table.getSelectionModel().getSelectedItem();
        if (a == null) {
            message("Select an appointment first.");
            return;
        }
        a.status.set(status);
        refresh();
        message("Appointment marked as " + status + ".");
    }

    private void viewDetails() {
        Appointment a = table.getSelectionModel().getSelectedItem();
        if (a == null) {
            message("Select an appointment to view details.");
            return;
        }
        message("ID: " + a.id.get()
                + "\nPet: " + a.pet.get()
                + "\nOwner: " + a.owner.get()
                + "\nVisit: " + a.type.get()
                + "\nDate: " + a.date.get()
                + "\nTime: " + a.time.get()
                + "\nStatus: " + a.status.get());
    }

    private void message(String text) {
        new Alert(Alert.AlertType.INFORMATION, text).showAndWait();
    }

    private Label heading(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 27));
        l.setTextFill(Color.web("#123D30"));
        return l;
    }

    private Label muted(String text) {
        Label l = new Label(text);
        l.setTextFill(Color.web("#687B70"));
        return l;
    }

    private Button primaryButton(String text) {
        Button b = new Button(text);
        b.setTextFill(Color.WHITE);
        b.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        b.setStyle("-fx-background-color:#176B4D;"
                + "-fx-background-radius:10;-fx-padding:12 16;"
                + "-fx-cursor:hand;");
        return b;
    }

    private Button lightButton(String text) {
        Button b = new Button(text);
        b.setTextFill(Color.web("#123D30"));
        b.setStyle("-fx-background-color:white;"
                + "-fx-border-color:#C8DDD0;-fx-border-radius:10;"
                + "-fx-background-radius:10;-fx-padding:11 16;"
                + "-fx-cursor:hand;");
        return b;
    }

    private static class Appointment {
        SimpleStringProperty id, pet, owner, type, date, time, status;

        Appointment(String id, String pet, String owner, String type,
                    String date, String time, String status) {
            this.id = new SimpleStringProperty(id);
            this.pet = new SimpleStringProperty(pet);
            this.owner = new SimpleStringProperty(owner);
            this.type = new SimpleStringProperty(type);
            this.date = new SimpleStringProperty(date);
            this.time = new SimpleStringProperty(time);
            this.status = new SimpleStringProperty(status);
        }

        SimpleStringProperty value(String key) {
            switch (key) {
                case "id": return id;
                case "pet": return pet;
                case "owner": return owner;
                case "type": return type;
                case "date": return date;
                case "time": return time;
                default: return status;
            }
        }
    }
}
