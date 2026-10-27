package com.techyuva.view.Login;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class user {
    public Scene fromScene;
    public Scene getfromScene(Runnable goToUser){

    Text emailTx=new Text("Email");
    emailTx.setStyle("-fx-font-size:20px;-fx-fill:black");

    TextField emailField=new TextField();
    emailField.setFocusTraversable(false);
    emailField.setPromptText("enter Email");
    
    emailField.setPrefHeight(30);
    emailField.setMaxHeight(30);


    Text Pass=new Text("Password");
    Pass.setStyle("-fx-font-size:20px;-fx-fill:black;");

    TextField PassField=new TextField();
    PassField.setFocusTraversable(false);
    PassField.setPromptText("enter Enter Password");

Button Logi=new Button("Login");
Logi.setOnAction(r->{

});


      Button Back=new Button("back");
        Back.setOnAction(e->{
            System.out.println("Back clicked");
            goToUser.run();
        });

        VBox vb=new VBox(10,emailTx,emailField,Pass,PassField,Logi,Back);
        vb.setPadding(new Insets(200));
        vb.setAlignment(Pos.CENTER);
        Scene sc=new Scene(vb,1280,760);
        return sc;

    }

}

