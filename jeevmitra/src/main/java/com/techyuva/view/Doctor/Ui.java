package com.techyuva.view.Doctor;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Ui extends Application {

    private BorderPane root;
    private VBox sidebar;
    private StackPane contentArea;
    private DoctorDashboard doctorDashboard;

    private Node currentPage;

    // image cache (ekda load kela ki parat shodhat nahi)
    private Image doctorImage;
    private boolean doctorImageTried = false;

    private Image logoImage;
    private boolean logoImageTried = false;

    @Override
    public void start(Stage stage) {

        root = new BorderPane();
        contentArea = new StackPane();
        doctorDashboard = new DoctorDashboard();

        createSidebar();

        root.setLeft(sidebar);
        root.setCenter(contentArea);

        showDashboard();

        Scene scene = new Scene(root, 1200, 750);

        stage.setTitle("JeevMitra - Pet Care Management System");
        stage.setScene(scene);
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.setMaximized(true);
        stage.show();
    }

    // ---------------- IMAGE HELPERS ----------------

    /**
     * Ek image file load karto.
     * 1) JavaFX ne direct try karto
     * 2) JavaFX la jamla nahi tar Java ImageIO ne vachun PNG madhe badalto
     */
    private Image loadImageFile(File file) {

        try {

            Image img = new Image(file.toURI().toString());

            if (!img.isError()) {
                return img;
            }

            System.out.println(
                "JavaFX could not read " + file.getAbsolutePath()
                + " -> " + img.getException()
            );

            // fallback: ImageIO ne vachun PNG madhe convert kar
            java.awt.image.BufferedImage buffered =
                javax.imageio.ImageIO.read(file);

            if (buffered != null) {

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                javax.imageio.ImageIO.write(buffered, "png", out);

                Image converted = new Image(
                    new ByteArrayInputStream(out.toByteArray())
                );

                if (!converted.isError()) {
                    System.out.println(
                        "Loaded via ImageIO fallback: " + file.getAbsolutePath()
                    );
                    return converted;
                }
            } else {
                System.out.println(
                    "ImageIO also could not read: " + file.getAbsolutePath()
                    + " (file kharab ahe kinva WEBP/CMYK ahe - "
                    + "Paint madhe PNG mhanun save kara)"
                );
            }

        } catch (Exception e) {
            System.out.println(
                "Error reading " + file.getAbsolutePath() + " -> " + e
            );
        }

        return null;
    }

    /** Classpath madhun pahilya sapadnarya path cha image load karto. */
    private Image loadFromClasspath(String... paths) {

        for (String path : paths) {

            try (InputStream is = getClass().getResourceAsStream(path)) {

                if (is != null) {

                    Image img = new Image(is);

                    if (!img.isError()) {
                        System.out.println("Image loaded (classpath): " + path);
                        return img;
                    }

                    System.out.println(
                        "Classpath image found but unreadable: " + path
                        + " -> " + img.getException()
                    );
                }

            } catch (Exception e) {
                System.out.println("Classpath error for " + path + " -> " + e);
            }
        }

        return null;
    }

    /**
     * Project folder (ani tyachya sagalya subfolders) madhe
     * navavarun image shodhto. Folder cha naav chukla tari chalto.
     */
    private static final String PROJECT_ROOT = "D:/SuperX/TechYuva/jeevmitra";

    private Image findImageInProject(Predicate<String> nameMatches) {

        // 1) tuzya project cha pakka path, 2) current working directory
        Image img = findImageUnder(PROJECT_ROOT, nameMatches);

        return img != null ? img : findImageUnder(".", nameMatches);
    }

    private Image findImageUnder(String rootDir, Predicate<String> nameMatches) {

        try (Stream<Path> stream = Files.walk(Paths.get(rootDir), 10)) {

            List<File> candidates = stream
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .filter(f -> {
                    String n = f.getName().toLowerCase();
                    boolean isImage =
                        n.endsWith(".png") || n.endsWith(".jpg")
                        || n.endsWith(".jpeg") || n.endsWith(".gif")
                        || n.endsWith(".bmp");
                    return isImage && nameMatches.test(n);
                })
                .sorted(Comparator.comparing(File::getAbsolutePath))
                .collect(Collectors.toList());

            for (File f : candidates) {

                Image img = loadImageFile(f);

                if (img != null) {
                    System.out.println(
                        "Image loaded from project: " + f.getAbsolutePath()
                    );
                    return img;
                }
            }

        } catch (Exception e) {
            System.out.println("Project search error -> " + e);
        }

        return null;
    }

    private static final File SAVED_CHOICE =
        new File(System.getProperty("user.home"), ".jeevmitra_doctor_photo.txt");

    /** User ne pahilyanda nivadlela photo (click karun) parat vaparto. */
    private Image loadFromSavedChoice() {

        try {

            if (SAVED_CHOICE.exists()) {

                String path = Files.readString(SAVED_CHOICE.toPath()).trim();
                File file = new File(path);

                if (file.exists()) {
                    Image img = loadImageFile(file);

                    if (img != null) {
                        System.out.println("Doctor image loaded (saved choice): " + path);
                        return img;
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Saved choice error -> " + e);
        }

        return null;
    }

    /** Project chya images folder madhun thet (absolute path ne) shodhto. */
    private Image loadFromKnownFolders() {

        String[] folders = {
            PROJECT_ROOT + "/src/main/resources/assets/images",
            "src/main/resources/assets/images",
            "resources/assets/images",
            "assets/images"
        };

        for (String folder : folders) {

            File[] files = new File(folder).listFiles();

            if (files == null) {
                continue;
            }

            for (File f : files) {

                String n = f.getName().toLowerCase();

                if (n.startsWith("imagedoctor") || n.startsWith("doctor")) {

                    Image img = loadImageFile(f);

                    if (img != null) {
                        System.out.println("Doctor image loaded: " + f.getAbsolutePath());
                        return img;
                    }
                }
            }
        }

        return null;
    }

    /** Photo avatar var click kela tar file chooser ughadato. */
    private void chooseDoctorPhoto() {

        javafx.stage.FileChooser chooser = new javafx.stage.FileChooser();

        chooser.setTitle("Select Doctor Photo");

        chooser.getExtensionFilters().add(
            new javafx.stage.FileChooser.ExtensionFilter(
                "Images", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"
            )
        );

        File file = chooser.showOpenDialog(
            root.getScene() != null ? root.getScene().getWindow() : null
        );

        if (file == null) {
            return;
        }

        Image img = loadImageFile(file);

        if (img == null) {
            showMessage(
                "Photo Error",
                "Hi file vachta aali nahi. Paint madhe ughadun "
                + "PNG mhanun save kara ani punha nivda."
            );
            return;
        }

        doctorImage = img;
        doctorImageTried = true;

        try {
            Files.writeString(SAVED_CHOICE.toPath(), file.getAbsolutePath());
        } catch (Exception e) {
            System.out.println("Could not save photo choice -> " + e);
        }

        for (java.util.Map.Entry<StackPane, Double> entry
                : avatarSizes.entrySet()) {
            fillAvatar(entry.getKey(), entry.getValue());
        }
    }

    private final java.util.Map<StackPane, Double> avatarSizes =
        new java.util.WeakHashMap<>();

    /** Doctor cha photo (ekda load karun cache karto). */
    private Image getDoctorImage() {

        if (!doctorImageTried) {

            doctorImageTried = true;

            doctorImage = loadFromSavedChoice();

            if (doctorImage == null) {
                doctorImage = loadFromKnownFolders();
            }

            if (doctorImage == null) doctorImage = loadFromClasspath(
                "/assets/images/imageDOCTOR..jpg",
                "/assets/images/imageDOCTOR.jpg",
                "/assets/images/imageDoctor.jpg",
                "/assets/images/imagedoctor.jpg",
                "/assets/images/doctor.png",
                "/assets/images/doctor.jpg"
            );

            if (doctorImage == null) {
                doctorImage = findImageInProject(
                    n -> n.startsWith("imagedoctor") || n.startsWith("doctor")
                );
            }

            if (doctorImage == null) {
                System.out.println(
                    "Doctor image NOT found. Working dir: "
                    + new File(".").getAbsolutePath()
                );
            }
        }

        return doctorImage;
    }

    /** Logo (ekda load karun cache karto). */
    private Image getLogoImage() {

        if (!logoImageTried) {

            logoImageTried = true;

            logoImage = loadFromClasspath(
                "/assets/images/logo.png",
                "/assets/logo.png",
                "/Assets/logo.png",
                "/Assests/logo.png"
            );

            if (logoImage == null) {
                logoImage = findImageInProject(n -> n.startsWith("logo"));
            }

            if (logoImage == null) {
                System.out.println(
                    "Logo NOT found. Working dir: "
                    + new File(".").getAbsolutePath()
                );
            }
        }

        return logoImage;
    }

    /** Doctor cha round photo (photo nasel tar initials dakhavto). */
    private StackPane createDoctorAvatar(double size) {

        StackPane avatar = new StackPane();

        avatar.setMinSize(size, size);
        avatar.setPrefSize(size, size);
        avatar.setMaxSize(size, size);

        avatar.setStyle(
            "-fx-background-color:#D8F3E8;" +
            "-fx-background-radius:" + size + ";"
        );

        avatarSizes.put(avatar, size);

        fillAvatar(avatar, size);

        // photo disat nasel tar circle var click kara ani photo nivda
        avatar.setCursor(javafx.scene.Cursor.HAND);
        avatar.setOnMouseClicked(e -> chooseDoctorPhoto());
        Tooltip.install(avatar, new Tooltip("Click to choose photo"));

        return avatar;
    }

    /** Avatar madhe photo (kinva initials) bharto. */
    private void fillAvatar(StackPane avatar, double size) {

        avatar.getChildren().clear();

        Image image = getDoctorImage();

        if (image != null) {

            ImageView view = new ImageView(image);

            view.setFitWidth(size);
            view.setFitHeight(size);
            view.setPreserveRatio(false);
            view.setSmooth(true);
            view.setClip(new Circle(size / 2, size / 2, size / 2));

            avatar.getChildren().add(view);

        } else {

            Label initials = new Label("MP");

            initials.setStyle(
                "-fx-font-size:" + (size / 3) + "px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#004D40;"
            );

            avatar.getChildren().add(initials);
        }
    }

    // ---------------- SIDEBAR ----------------

    private void createSidebar() {

        sidebar = new VBox(10);
        sidebar.setPadding(new Insets(18, 14, 18, 14));
        sidebar.setPrefWidth(250);
        sidebar.setMinWidth(250);
        sidebar.setMaxWidth(250);

        sidebar.setStyle(
            "-fx-background-color:linear-gradient(to bottom,#003D33,#006B55,#004D40);"
        );

        ImageView logoView = new ImageView();

        Image logo = getLogoImage();

        if (logo != null) {
            logoView.setImage(logo);
        }

        logoView.setFitWidth(52);
        logoView.setFitHeight(52);
        logoView.setPreserveRatio(true);

        Text logoText = new Text("JeevMitra");

        logoText.setStyle(
            "-fx-font-size:25px;" +
            "-fx-font-weight:bold;" +
            "-fx-fill:white;"
        );

        Text tagline = new Text("Healthy Pets ♥ Happier Lives");

        tagline.setStyle(
            "-fx-font-size:10px;" +
            "-fx-fill:#D8F3E8;"
        );

        VBox brandText = new VBox(5, logoText, tagline);
        brandText.setAlignment(Pos.CENTER_LEFT);

        HBox brand = new HBox(8, brandText);
        brand.setAlignment(Pos.CENTER_LEFT);
        brand.setPadding(new Insets(4, 0, 8, 0));

        // ---------------- DOCTOR IMAGE ----------------

        StackPane photoCircle = createDoctorAvatar(90);

        Label doctorName = new Label("Dr.Mayuri Patil");

        doctorName.setStyle(
            "-fx-font-size:16px;" +
            "-fx-font-weight:bold;" +
            "-fx-text-fill:white;"
        );

        Label doctorRole = new Label("Veterinarian");

        doctorRole.setStyle(
            "-fx-font-size:12px;" +
            "-fx-text-fill:#D8F3E8;"
        );

        VBox doctorInfo = new VBox(
            6, photoCircle, doctorName, doctorRole
        );

        doctorInfo.setAlignment(Pos.CENTER);
        doctorInfo.setPadding(new Insets(4, 0, 10, 0));

        Button vetButton =
            sideButton("⌂   Veterinarian", this::showDashboard);

        Button patientButton =
            sideButton("♟   Patient", this::showPatients);

        Button recordsButton =
            sideButton("✚   Medical Records", this::showMedicalRecords);

        Button profileButton =
            sideButton("●   Profile", this::showProfile);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button logoutButton =
            sideButton("⇥   Logout", this::logout);

        sidebar.getChildren().addAll(
            brand,
            doctorInfo,
            vetButton,
            patientButton,
            recordsButton,
            profileButton,
            spacer,
            logoutButton
        );
    }

    // ---------------- SIDEBAR BUTTON ----------------

    private Button sideButton(String title, Runnable action) {

        Button button = new Button(title);

        button.setMaxWidth(Double.MAX_VALUE);
        button.setMinHeight(48);
        button.setPrefHeight(48);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(0, 12, 0, 15));

        String normal =
            "-fx-background-color:transparent;" +
            "-fx-text-fill:white;" +
            "-fx-font-size:14px;" +
            "-fx-font-weight:bold;" +
            "-fx-background-radius:10px;" +
            "-fx-cursor:hand;";

        String hover =
            "-fx-background-color:#08765F;" +
            "-fx-text-fill:white;" +
            "-fx-font-size:14px;" +
            "-fx-font-weight:bold;" +
            "-fx-background-radius:10px;" +
            "-fx-cursor:hand;";

        button.setStyle(normal);

        button.setOnMouseEntered(e -> {
            button.setStyle(hover);

            button.setEffect(
                new DropShadow(8, Color.rgb(0, 0, 0, 0.20))
            );
        });

        button.setOnMouseExited(e -> {
            button.setStyle(normal);
            button.setEffect(null);
        });

        button.setOnAction(e -> action.run());

        return button;
    }

    // ---------------- GREEN BUTTON ----------------

    private Button greenButton(String title) {

        Button button = new Button(title);

        String normal =
            "-fx-background-color:#08765F;" +
            "-fx-text-fill:white;" +
            "-fx-font-size:14px;" +
            "-fx-font-weight:bold;" +
            "-fx-padding:11px 18px;" +
            "-fx-background-radius:8px;" +
            "-fx-cursor:hand;";

        String hover =
            "-fx-background-color:#099477;" +
            "-fx-text-fill:white;" +
            "-fx-font-size:14px;" +
            "-fx-font-weight:bold;" +
            "-fx-padding:11px 18px;" +
            "-fx-background-radius:8px;" +
            "-fx-cursor:hand;";

        button.setStyle(normal);

        button.setOnMouseEntered(e -> button.setStyle(hover));
        button.setOnMouseExited(e -> button.setStyle(normal));

        return button;
    }

    // ---------------- DASHBOARD ----------------

    private void showDashboard() {

        root.setLeft(sidebar);

        Node dashboard = doctorDashboard.createView(
            this::showDashboard,
            this::openPage
        );

        contentArea.getChildren().setAll(dashboard);
        currentPage = dashboard;
    }

    private void openPage(String page) {

        switch (page) {

            case "Today's Appointments":
            case "All Appointments":
                showAppointments();
                break;

            case "Patient Management":
                showPatients();
                break;

            case "Medical Records":
            case "All Medical Records":
                showMedicalRecords();
                break;

            case "Veterinarian Profile":
                showProfile();
                break;

            case "Start Video Call":
                showVideoCall();
                break;

            case "Schedule Video Call":
                showScheduleCall();
                break;

            default:
                showDashboard();
        }
    }

    // ---------------- COMMON PAGE ----------------

    private Button backButton() {

        Button back = greenButton("← Back to Dashboard");

        back.setOnAction(e -> showDashboard());

        return back;
    }

    private VBox pageLayout(String title) {

        Label heading = new Label(title);

        heading.setStyle(
            "-fx-font-size:26px;" +
            "-fx-font-weight:bold;" +
            "-fx-text-fill:#064E3B;"
        );

        VBox page = new VBox(20, backButton(), heading);

        page.setPadding(new Insets(25));

        page.setStyle(
            "-fx-background-color:linear-gradient(to bottom right,#F5FBF8,#E8F5EE);"
        );

        return page;
    }

    // ---------------- TABLE ----------------

    private TableView<ObservableList<String>> createTable(
            String[] headers, String[][] rows) {

        TableView<ObservableList<String>> table =
            new TableView<>();

        for (int i = 0; i < headers.length; i++) {

            final int index = i;

            TableColumn<ObservableList<String>, String> column =
                new TableColumn<>(headers[i]);

            column.setCellValueFactory(data ->
                new SimpleStringProperty(
                    data.getValue().get(index)
                )
            );

            table.getColumns().add(column);
        }

        ObservableList<ObservableList<String>> items =
            FXCollections.observableArrayList();

        for (String[] row : rows) {
            items.add(
                FXCollections.observableArrayList(row)
            );
        }

        table.setItems(items);

        table.setColumnResizePolicy(
            TableView.CONSTRAINED_RESIZE_POLICY
        );

        VBox.setVgrow(table, Priority.ALWAYS);

        return table;
    }

    // ---------------- APPOINTMENTS ----------------

    private void showAppointments() {

        VBox page = pageLayout("Today's Appointments");

        TableView<ObservableList<String>> table = createTable(
            new String[]{"Pet Name", "Time", "Appointment"},
            new String[][]{
                {"Bella", "10:00 AM", "General checkup"}
            }
        );

        page.getChildren().add(table);

        contentArea.getChildren().setAll(page);
        currentPage = page;
    }

    // ---------------- PATIENTS ----------------

    private void showPatients() {

        VBox page = pageLayout("Patient Management");

        TableView<ObservableList<String>> table = createTable(
            new String[]{
                "Pet Name", "Pet Type",
                "Owner Name", "Contact Number"
            },
            new String[][]{
                {"Bella", "Dog", "Rahul", "9876543210"},
                {"Mochi", "Cat", "Priya", "9876543211"},
                {"Rocky", "Dog", "Amit", "9876543212"}
            }
        );

        Button addButton = greenButton("Add Patient");
        Button deleteButton = greenButton("Delete Selected");

        addButton.setOnAction(e -> {

            Dialog<String[]> dialog = new Dialog<>();

            dialog.setTitle("Add Patient");
            dialog.setHeaderText("Enter patient details");

            ButtonType addType = new ButtonType(
                "Add", ButtonBar.ButtonData.OK_DONE
            );

            dialog.getDialogPane().getButtonTypes().addAll(
                addType, ButtonType.CANCEL
            );

            TextField pet = new TextField();
            pet.setPromptText("Pet name");

            TextField type = new TextField();
            type.setPromptText("Pet type");

            TextField owner = new TextField();
            owner.setPromptText("Owner name");

            TextField phone = new TextField();
            phone.setPromptText("Contact number");

            VBox fields = new VBox(10, pet, type, owner, phone);

            fields.setPadding(new Insets(15));

            dialog.getDialogPane().setContent(fields);

            dialog.setResultConverter(button -> {

                if (button == addType) {
                    return new String[]{
                        pet.getText(),
                        type.getText(),
                        owner.getText(),
                        phone.getText()
                    };
                }

                return null;
            });

            dialog.showAndWait().ifPresent(values -> {

                if (!values[0].trim().isEmpty()) {
                    table.getItems().add(
                        FXCollections.observableArrayList(values)
                    );
                }
            });
        });

        deleteButton.setOnAction(e -> {

            ObservableList<String> selected =
                table.getSelectionModel().getSelectedItem();

            if (selected != null) {
                table.getItems().remove(selected);
            }
        });

        page.getChildren().addAll(
            table, new HBox(12, addButton, deleteButton)
        );

        contentArea.getChildren().setAll(page);
        currentPage = page;
    }

    // ---------------- MEDICAL RECORDS ----------------

    private void showMedicalRecords() {

        VBox page = pageLayout("Medical Records");

        TableView<ObservableList<String>> table = createTable(
            new String[]{
                "Pet Name", "Treatment",
                "Diagnosis / Status", "Date"
            },
            new String[][]{
                {"Bella", "Routine checkup", "Healthy", "Today"},
                {"Rocky", "Vaccination", "Completed", "Today"},
                {"Luna", "Treatment follow-up",
                    "Follow-up required", "Today"}
            }
        );

        Button addButton = greenButton("Add Medical Record");
        Button deleteButton = greenButton("Delete Selected");

        addButton.setOnAction(e -> {

            TextInputDialog dialog = new TextInputDialog();

            dialog.setTitle("Add Medical Record");
            dialog.setHeaderText("Enter pet name");

            dialog.showAndWait().ifPresent(name -> {

                if (!name.trim().isEmpty()) {
                    table.getItems().add(
                        FXCollections.observableArrayList(
                            name, "Not specified", "Pending", "Today"
                        )
                    );
                }
            });
        });

        deleteButton.setOnAction(e -> {

            ObservableList<String> selected =
                table.getSelectionModel().getSelectedItem();

            if (selected != null) {
                table.getItems().remove(selected);
            }
        });

        page.getChildren().addAll(
            table, new HBox(12, addButton, deleteButton)
        );

        contentArea.getChildren().setAll(page);
        currentPage = page;
    }

    // ---------------- PROFILE ----------------

    private void showProfile() {

        VBox page = pageLayout("Veterinarian Profile");

        TextField name = field("Doctor Name", "Dr. Kiran Patil");
        TextField email = field("Email", "doctor@example.com");
        TextField mobile = field("Mobile Number", "");
        TextField qualification = field("Qualification", "B.Tech");
        TextField specialization = field("Specialization", "");
        TextField experience = field("Experience", "");
        TextField license = field("License Number", "");
        TextField clinic = field("Clinic Name", "");
        TextField address = field("Clinic Address", "");

        String[] labels = {
            "Doctor Name", "Email", "Mobile Number",
            "Qualification", "Specialization", "Experience",
            "License Number", "Clinic Name", "Clinic Address"
        };

        TextField[] fields = {
            name, email, mobile, qualification,
            specialization, experience, license, clinic, address
        };

        GridPane form = new GridPane();

        form.setHgap(20);
        form.setVgap(14);
        form.setPadding(new Insets(20));

        for (int i = 0; i < labels.length; i++) {

            Label label = new Label(labels[i]);

            label.setStyle(
                "-fx-font-size:14px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#004D40;"
            );

            form.add(label, 0, i);
            form.add(fields[i], 1, i);
        }

        form.getColumnConstraints().addAll(
            new ColumnConstraints(150),
            new ColumnConstraints(320)
        );

        // ---------------- DOCTOR PROFILE PHOTO ----------------

        StackPane profilePhoto = createDoctorAvatar(140);

        Label profileTitle = new Label("Doctor Profile");

        profileTitle.setStyle(
            "-fx-font-size:24px;" +
            "-fx-font-weight:bold;" +
            "-fx-text-fill:#064E3B;"
        );

        Label profileSubtitle =
            new Label("Manage your professional details");

        profileSubtitle.setStyle(
            "-fx-font-size:13px;" +
            "-fx-text-fill:#64748B;"
        );

        VBox profileHeader = new VBox(
            8, profilePhoto, profileTitle, profileSubtitle
        );

        profileHeader.setAlignment(Pos.CENTER);
        profileHeader.setPadding(new Insets(10, 10, 20, 10));

        Button save = greenButton("Save Profile");
        Button reset = greenButton("Reset");

        save.setOnAction(e ->
            showMessage(
                "Profile Saved",
                "Your profile details have been updated."
            )
        );

        reset.setOnAction(e -> {
            for (TextField f : fields) {
                f.clear();
            }
        });

        VBox card = new VBox(
            15,
            profileHeader,
            form,
            new HBox(12, save, reset)
        );

        card.setPadding(new Insets(15));
        card.setMaxWidth(700);

        card.setStyle(
            "-fx-background-color:white;" +
            "-fx-background-radius:16;" +
            "-fx-border-color:#CDE7DB;" +
            "-fx-border-radius:16;"
        );

        page.getChildren().add(card);

        contentArea.getChildren().setAll(page);
        currentPage = page;
    }

    private TextField field(String prompt, String value) {

        TextField field = new TextField(value);

        field.setPromptText(prompt);
        field.setPrefHeight(38);

        field.setStyle(
            "-fx-background-radius:7;" +
            "-fx-border-radius:7;" +
            "-fx-border-color:#CDE7DB;" +
            "-fx-padding:8;"
        );

        return field;
    }

    // ---------------- VIDEO CALL ----------------

    private void showVideoCall() {

        Stage callStage = new Stage();

        callStage.setTitle("JeevMitra - Video Consultation");
        callStage.setMinWidth(700);
        callStage.setMinHeight(550);

        Label title = new Label("🐾 Video Consultation");

        title.setStyle(
            "-fx-font-size:25px;" +
            "-fx-font-weight:bold;" +
            "-fx-text-fill:#004D40;"
        );

        Label pet = new Label("Patient: Bella (Dog)");
        pet.setStyle("-fx-font-size:16px;");

        Label status = new Label("Call window opened");

        status.setStyle(
            "-fx-font-size:14px;" +
            "-fx-text-fill:#08765F;"
        );

        Label videoArea = new Label(
            "📹\n\nVideo Call Window\n\nCamera integration required"
        );

        videoArea.setAlignment(Pos.CENTER);
        videoArea.setMaxWidth(Double.MAX_VALUE);
        videoArea.setMinHeight(250);
        videoArea.setPrefHeight(320);

        VBox.setVgrow(videoArea, Priority.ALWAYS);

        videoArea.setStyle(
            "-fx-background-color:#DDEFE7;" +
            "-fx-background-radius:16;" +
            "-fx-font-size:20px;" +
            "-fx-text-fill:#004D40;"
        );

        Button camera = greenButton("Camera: ON");
        Button mic = greenButton("Microphone: ON");
        Button end = greenButton("End Call");

        camera.setMinWidth(140);
        mic.setMinWidth(160);
        end.setMinWidth(120);

        camera.setOnAction(e -> {

            boolean on = camera.getText().equals("Camera: ON");

            camera.setText(on ? "Camera: OFF" : "Camera: ON");

            status.setText(
                on ? "Camera display switched off"
                   : "Camera display switched on"
            );
        });

        mic.setOnAction(e -> {

            boolean on = mic.getText().equals("Microphone: ON");

            mic.setText(
                on ? "Microphone: OFF" : "Microphone: ON"
            );
        });

        end.setOnAction(e -> callStage.close());

        HBox controls = new HBox(15, camera, mic, end);

        controls.setAlignment(Pos.CENTER);
        controls.setPadding(new Insets(10));

        VBox layout = new VBox(
            16, title, pet, videoArea, status, controls
        );

        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(25));

        VBox.setVgrow(videoArea, Priority.ALWAYS);

        layout.setStyle("-fx-background-color:#F3FAF6;");

        callStage.setScene(new Scene(layout, 800, 650));
        callStage.show();
    }

    // ---------------- SCHEDULE VIDEO CALL ----------------

    private void showScheduleCall() {

        Dialog<ButtonType> dialog = new Dialog<>();

        dialog.setTitle("Schedule Video Consultation");
        dialog.setHeaderText("Book a video consultation");

        TextField petName = new TextField();
        petName.setPromptText("Pet name");

        TextField ownerName = new TextField();
        ownerName.setPromptText("Owner name");

        DatePicker date = new DatePicker();
        date.setPromptText("Select date");

        date.setDayCellFactory(picker -> new DateCell() {

            @Override
            public void updateItem(
                    java.time.LocalDate item, boolean empty) {

                super.updateItem(item, empty);

                setDisable(
                    empty || item.isBefore(java.time.LocalDate.now())
                );
            }
        });

        TextField time = new TextField();
        time.setPromptText("Time (e.g. 10:00 AM)");

        VBox fields = new VBox(
            10,
            new Label("Pet Name"), petName,
            new Label("Owner Name"), ownerName,
            new Label("Appointment Date"), date,
            new Label("Appointment Time"), time
        );

        fields.setPadding(new Insets(15));

        dialog.getDialogPane().setContent(fields);

        ButtonType schedule = new ButtonType(
            "Schedule", ButtonBar.ButtonData.OK_DONE
        );

        dialog.getDialogPane().getButtonTypes().addAll(
            schedule, ButtonType.CANCEL
        );

        dialog.showAndWait().ifPresent(result -> {

            if (result == schedule) {

                if (petName.getText().trim().isEmpty()
                        || ownerName.getText().trim().isEmpty()
                        || date.getValue() == null
                        || time.getText().trim().isEmpty()) {

                    showMessage(
                        "Missing Details",
                        "Please fill in all appointment details."
                    );

                    return;
                }

                showMessage(
                    "Appointment Scheduled",
                    "Pet: " + petName.getText()
                    + "\nOwner: " + ownerName.getText()
                    + "\nDate: " + date.getValue()
                    + "\nTime: " + time.getText()
                    + "\n\nThis appointment is not saved to a database."
                );
            }
        });
    }

    // ---------------- CUSTOM LOGOUT POPUP BOX ----------------

    private void logout() {

        if (contentArea.getChildren().isEmpty()) {
            return;
        }

        currentPage = contentArea.getChildren().get(0);

        StackPane overlay = new StackPane();

        overlay.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        overlay.setStyle(
            "-fx-background-color:rgba(0,0,0,0.28);"
        );

        VBox popup = new VBox(5);

        popup.setAlignment(Pos.CENTER_LEFT);
        popup.setPadding(new Insets(25));
        popup.setMaxWidth(420);
        popup.setPrefWidth(420);
        popup.setMaxHeight(Region.USE_PREF_SIZE);

        popup.setStyle(
            "-fx-background-color:white;" +
            "-fx-background-radius:12;" +
            "-fx-border-color:#D1E5DC;" +
            "-fx-border-radius:12;" +
            "-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.25),20,0.2,0,5);"
        );

        Label title = new Label("JeevMitra Logout");

        title.setStyle(
            "-fx-font-size:21px;" +
            "-fx-font-weight:bold;" +
            "-fx-text-fill:#004D40;"
        );

        Label message = new Label(
            "Are you sure you want to logout?"
        );

        message.setWrapText(true);

        message.setStyle(
            "-fx-font-size:15px;" +
            "-fx-text-fill:#334155;"
        );

        Label question = new Label(
            "Do you want to end your session?"
        );

        question.setStyle(
            "-fx-font-size:13px;" +
            "-fx-text-fill:#64748B;"
        );

        CheckBox checkBox = new CheckBox(
            "I understand that I am logging out."
        );

        checkBox.setWrapText(true);

        checkBox.setStyle(
            "-fx-font-size:13px;" +
            "-fx-text-fill:#334155;"
        );

        Button logoutBtn = new Button("Logout");
        Button cancelBtn = new Button("Cancel");

        logoutBtn.setDisable(true);

        logoutBtn.setStyle(
            "-fx-background-color:#08765F;" +
            "-fx-text-fill:white;" +
            "-fx-font-weight:bold;" +
            "-fx-background-radius:6;" +
            "-fx-padding:9px 22px;" +
            "-fx-cursor:hand;"
        );

        cancelBtn.setStyle(
            "-fx-background-color:#E2E8F0;" +
            "-fx-text-fill:#334155;" +
            "-fx-font-weight:bold;" +
            "-fx-background-radius:6;" +
            "-fx-padding:9px 22px;" +
            "-fx-cursor:hand;"
        );

        checkBox.selectedProperty().addListener(
            (obs, oldValue, newValue) ->
                logoutBtn.setDisable(!newValue)
        );

        HBox buttons = new HBox(12, logoutBtn, cancelBtn);

        buttons.setAlignment(Pos.CENTER_RIGHT);

        popup.getChildren().addAll(
            title,
            message,
            question,
            checkBox,
            buttons
        );

        overlay.getChildren().add(popup);

        StackPane.setAlignment(popup, Pos.CENTER);

        contentArea.getChildren().add(overlay);

        cancelBtn.setOnAction(e ->
            contentArea.getChildren().remove(overlay)
        );

        logoutBtn.setOnAction(e -> {

            if (checkBox.isSelected()) {
                showLoginPage();
            }
        });
    }

    // ---------------- LOGIN ----------------

    private void showLoginPage() {

        root.setLeft(null);

        Text title = new Text("JeevMitra");

        title.setStyle(
            "-fx-font-size:34px;" +
            "-fx-font-weight:bold;" +
            "-fx-fill:#004D40;"
        );

        Label subtitle = new Label("Veterinarian Login");

        subtitle.setStyle(
            "-fx-font-size:18px;" +
            "-fx-text-fill:#52796F;"
        );

        TextField email = new TextField();
        email.setPromptText("Enter email");
        email.setMaxWidth(320);

        PasswordField password = new PasswordField();
        password.setPromptText("Enter password");
        password.setMaxWidth(320);

        Button login = greenButton("Login");

        login.setMaxWidth(320);
        login.setPrefWidth(320);

        login.setOnAction(e -> {

            if (!email.getText().contains("@")
                    || password.getText().length() < 4) {

                showMessage(
                    "Login Failed",
                    "Enter a valid email and a password " +
                    "with at least 4 characters."
                );

            } else {

                root.setLeft(sidebar);
                showDashboard();
            }
        });

        VBox page = new VBox(
            18, title, subtitle, email, password, login
        );

        page.setAlignment(Pos.CENTER);
        page.setPadding(new Insets(30));

        page.setStyle(
            "-fx-background-color:linear-gradient(to bottom right,#F5FBF8,#E8F5EE);"
        );

        contentArea.getChildren().setAll(page);
        currentPage = page;
    }

    // ---------------- MESSAGE ----------------

    private void showMessage(String title, String message) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);

        if (root.getScene() != null) {
            alert.initOwner(root.getScene().getWindow());
        }

        alert.showAndWait();
    }
}