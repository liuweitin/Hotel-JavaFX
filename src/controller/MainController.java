package controller;

import HSQLDB.ConnectionHSQL;
import javafx.application.Application;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainController extends Application{
    //CityLodgeDB    <---- databaseNAME;

    @FXML
    private Button login;

    @FXML
    private TextField userName;

    @FXML
    private TextField password;

    private static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        ConnectionHSQL connectionHSQL = ConnectionHSQL.getInstance();
        //database initialisation
        connectionHSQL.getConnection("CityLodgeDB");
        Boolean flag = connectionHSQL.checkTableExist("CityLodgeDB","USER");
        if(flag){
            //delete table
            connectionHSQL.deleteTable("CityLodgeDB","user");
            //create table
            String createUser = "CREATE TABLE user(" +
                    "id INT NOT NULL," +
                    "userName VARCHAR(20) NOT NULL," +
                    "password VARCHAR(20) NOT NULL," +
                    "PRIMARY KEY (id))";
            connectionHSQL.createTable("CityLodgeDB",createUser);
            //insert user
            String insertRow1 = "INSERT INTO USER VALUES (1 ,'admin', '123')";
            String insertRow2 = "INSERT INTO USER VALUES (2 ,'waiting', '123')";
            connectionHSQL.insertRow("CityLodgeDB","USER", insertRow1);
            connectionHSQL.insertRow("CityLodgeDB","USER", insertRow2);
            //search allUser
            connectionHSQL.selectQuery("CityLodgeDB","user");
        }
        //-----stage code
        stage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("../view/login.fxml"));
        stage.setTitle("CityLodge");
        stage.setScene(new Scene(root, 500, 350));
        stage.show();
    }


    public static void main(String[] args) { launch(args); }


    public void loginMain() throws Exception {
        //display the username and password
        System.out.println("user entered information: " + userName.getText() + "-------------" + password.getText());
        //get the users informaiton from the database.
        ArrayList<User> users = (ArrayList<User>)ConnectionHSQL.selectQuery("CityLodgeDB","user");
        HashMap<String, User> usersMap = new HashMap<>();
        for (int i = 0; i <users.size() ; i++) {
            usersMap.put(users.get(i).getUserName(), users.get(i));
        }
        // Discriminate the username and the password that is correct or not
        if(usersMap.containsKey(userName.getText()) && usersMap.get(userName.getText()).getPassword().equalsIgnoreCase( password.getText()) ){
            System.out.println("login successfully");
            stage.hide();
            //display the mainMenu
            MainMenuController mainMenuController = new MainMenuController();
            mainMenuController.start(new Stage());
        }else{

        }
    }

}
