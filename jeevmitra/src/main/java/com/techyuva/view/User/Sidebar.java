package com.techyuva.view.User;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

/**
 * Sidebar ithe EKDA banvla ahe.
 * Konatya pan page madhe:  Sidebar.create("Book Appointment")
 * "active" mhanje je page ughadle ahe, tyacha menu green highlight hoto.
 */
public class Sidebar {

    // ---------- SIDEBAR BANVA ----------
    public static VBox create(String active) {

        // ---------- LOGO ----------
        ImageView ivLogo = new ImageView(loadImage("/assets/icons/white/paw.png"));
        ivLogo.setFitHeight(32);
        ivLogo.setFitWidth(32);

        Text tLogo = new Text("JeevMitra");
        tLogo.setStyle("-fx-font-size:22px;-fx-font-weight:bold;");
        tLogo.setFill(Color.WHITE);

        HBox hbLogo = new HBox(10, ivLogo, tLogo);
        hbLogo.setAlignment(Pos.CENTER_LEFT);

        // ---------- USER (photo + naav) ----------
        ImageView ivUser = new ImageView(loadImage("/assets/img/user.png"));
        ivUser.setFitHeight(44);
        ivUser.setFitWidth(44);
        ivUser.setClip(new Circle(22, 22, 22));

        Text tUserName = new Text(Home.currentUser);   // login kelelya user cha naav
        tUserName.setStyle("-fx-font-size:14px;-fx-font-weight:bold;");
        tUserName.setFill(Color.WHITE);

        Text tUserRole = new Text("User");
        tUserRole.setStyle("-fx-font-size:12px;");
        tUserRole.setFill(Color.web("#b7d9c9"));

        VBox vbUserText = new VBox(2, tUserName, tUserRole);

        HBox hbUser = new HBox(12, ivUser, vbUserText);
        hbUser.setAlignment(Pos.CENTER_LEFT);
        hbUser.setPadding(new Insets(10, 0, 10, 0));

        // ---------- MENU (naav, icon cha naav) ----------
        // navin menu pahije tr ithe ek oli add kara, ani khali open() madhe ek case
        String[][] menu = {
            {"Dashboard",         "dashboard"},
            {"AI Health Scan",    "health"},
            {"Book Appointment",  "calendar"},
            {"Report Rescue",     "warning"},
            {"Adoption",          "home"},
            {"Lost & Found",      "location"},
            {"Marketplace",       "cart"},
            {"Feeding Reminders", "bell"},
            {"Profile",           "person"}
        };

        VBox vbMenu = new VBox(4);
        for (String[] item : menu) {
            boolean isActive = item[0].equals(active);
            vbMenu.getChildren().add(menuItem(item[0], item[1], isActive));
        }

        // ---------- LOGOUT ----------
        HBox hbLogout = menuItem("Logout", "logout", false);

        // ---------- SPACE (Logout khali dhakalnyasathi) ----------
        Region spaceSidebar = new Region();
        VBox.setVgrow(spaceSidebar, Priority.ALWAYS);

        // ---------- SIDEBAR ----------
        VBox sidebar = new VBox(12, hbLogo, hbUser, vbMenu, spaceSidebar, hbLogout);
        sidebar.setPrefWidth(260);
        sidebar.setMinWidth(260);
        sidebar.setMaxWidth(260);
        sidebar.setStyle(
            "-fx-background-color:linear-gradient(to bottom, #145a41, #0b3d2c);" +
            "-fx-padding:24px 14px 24px 14px;"
        );
        return sidebar;
    }

    // ---------- EK MENU OLI ----------
    private static HBox menuItem(String text, String iconName, boolean active) {
        ImageView iv = new ImageView(loadImage("/assets/icons/white/" + iconName + ".png"));
        iv.setFitHeight(20);
        iv.setFitWidth(20);

        Text t = new Text(text);
        t.setStyle("-fx-font-size:15px;");
        t.setFill(Color.WHITE);

        HBox hb = new HBox(14, iv, t);
        hb.setAlignment(Pos.CENTER_LEFT);
        hb.setPadding(new Insets(10, 14, 10, 14));
        hb.setMaxWidth(Double.MAX_VALUE);

        if (active) {
            hb.setStyle("-fx-background-color:#2f8f69;-fx-background-radius:10px;-fx-cursor:hand;");
        } else {
            hb.setStyle("-fx-cursor:hand;");
        }

        // click kele ki page ughadto
        hb.setOnMouseClicked(e -> open(text));
        return hb;
    }

    // ---------- CLICK KELYAVAR KUTHE JAYCHA ----------
    private static void open(String page) {
        Runnable back = Home.backtoHome;     // dashboard var parat yenyasathi

        switch (page) {
            case "Dashboard":
                if (Home.backtoHome != null) {
                    Home.backtoHome.run();
                } else if (Home.primaryStage != null && Home.homeScene != null) {
                    Home.primaryStage.setScene(Home.homeScene);
                }
                break;
            case "AI Health Scan":
                Home.primaryStage.setScene(new HealthScanPage().getHealthScanScene(back));
                break;
            case "Book Appointment":
                Home.primaryStage.setScene(new AppointmentPage().getAppointmentScene(back));
                break;
            case "Report Rescue":
                Home.primaryStage.setScene(new RescuePage().getRescueScene(back));
                break;
            case "Adoption":
                Home.primaryStage.setScene(new AdoptionPage().getAdoptionScene(back));
                break;
            case "Lost & Found":
                Home.primaryStage.setScene(new LostFoundPage().getLostFoundScene(back));
                break;
            case "Marketplace":
                Home.primaryStage.setScene(new MarketplacePage().getMarketplaceScene(back));
                break;
            case "Feeding Reminders":
                Home.primaryStage.setScene(new ReminderPage().getReminderScene(back));
                break;
            case "Profile":
                Home.primaryStage.setScene(new ProfilePage().getProfileScene(back));
                break;
            case "Logout":
                System.out.println("logout clicked");
                // TODO: dusryane banavlela login page ithe ughadaycha
                // (aata fakt window band hote)
                Home.primaryStage.close();
                break;
        }
    }

    // image sapadli nahi tr crash honar nahi, console madhe message yeil
    public static Image loadImage(String resourcePath) {
        java.net.URL resource = Sidebar.class.getResource(resourcePath);
        if (resource == null) {
            System.err.println("Image sapadli nahi: " + resourcePath);
            return new WritableImage(1, 1);
        }
        return new Image(resource.toExternalForm());
    }
}





