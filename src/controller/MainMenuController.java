package controller;

import HSQLDB.ConnectionHSQL;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.User;

import java.applet.Applet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MainMenuController extends Application{
    @FXML
    private Button exit;

    @FXML
    private Button roomList;

    private static Stage stage;

    private static BorderPane borderPane;

    private static Scene scene;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        borderPane = (BorderPane)FXMLLoader.load(getClass().getResource("../view/mainMenu.fxml"));
        //subclass
        AnchorPane rommList = (AnchorPane)FXMLLoader.load(getClass().getResource("../view/room_list.fxml"));
        borderPane.setCenter(rommList);
        //---
        scene = new Scene(borderPane, 900, 700);
        stage.setScene(scene);
        stage.show();
    }

    public void displayRoomList() throws IOException {
        AnchorPane rommList = (AnchorPane)FXMLLoader.load(getClass().getResource("../view/room_list.fxml"));
        borderPane.setCenter(rommList);
        scene.setRoot(borderPane);
    }

    public void addRoom() throws Exception {
        AnchorPane addRoom = (AnchorPane)FXMLLoader.load(getClass().getResource("../view/addRoom.fxml"));
        borderPane.setCenter(addRoom);
        scene.setRoot(borderPane);
    }

    public void displayRecord() throws IOException {
        AnchorPane record = (AnchorPane)FXMLLoader.load(getClass().getResource("../view/record.fxml"));
        borderPane.setCenter(record);
        scene.setRoot(borderPane);
    }


    public void loadRoom() throws IOException {
        AnchorPane record = (AnchorPane)FXMLLoader.load(getClass().getResource("../view/loadRoom.fxml"));
        borderPane.setCenter(record);
        scene.setRoot(borderPane);
    }

    public void systemExit(){
        System.out.println("exit this system successfully");
        System.exit(0);
    }




}
