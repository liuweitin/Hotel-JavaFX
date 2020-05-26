package controller;

import HSQLDB.ConnectionHSQL;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Room;
import service.ServiceFunction;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class AddRoomController implements Initializable {
    @FXML
    private TextField roomId,roomType,feature,numBeds,image;

    @FXML
    private Label information;

    @FXML
    private Button chooseP;

    private ServiceFunction function = ServiceFunction.getInstance();

    private ConnectionHSQL connectionHSQL = ConnectionHSQL.getInstance();

    private static File imageSave;

    public void submitRoom() throws IOException {
        System.out.println("Add Room Submit");
        System.out.println("roomId:" + roomId.getText() + "|| roomtype:" + roomType.getText() + "|| numbeds:" + numBeds.getText() + "|| feature:" + feature.getText());
        //------
//        connectionHSQL.deleteTable("CityLodgeDB","ROOM");
        Boolean flag = connectionHSQL.checkTableExist("CityLodgeDB","ROOM");
        if(!flag){
            //create table
            String createRoom = "CREATE TABLE room(" +
                    "id INT NOT NULL," +
                    "numOfBed INT NOT NULL," +
                    "feature VARCHAR(20) NOT NULL," +
                    "roomStatus VARCHAR(20) NOT NULL," +
                    "roomType VARCHAR(20) NOT NULL," +
                    "imagePath VARCHAR(200) ," +
                    "PRIMARY KEY (id))";
            connectionHSQL.createTable("CityLodgeDB",createRoom);
        }
        //-----

        ArrayList<Room> rooms = function.selectAllRoom();
        Map<Integer, Room> roomMap = new HashMap();
        for (int i = 0; i < rooms.size(); i++) {
            roomMap.put(rooms.get(i).getId(), rooms.get(i));
        }
        //check the room information
        if(roomMap.containsKey(Integer.valueOf(roomId.getText()))){
            information.setText("the id has been exited, you have to change another one");
        }else{
            //   path
            String filePath = "../image/" + roomId.getText() + ".jpg";
//            String filePath = "../../../image/" + roomId.getText() + ".jpg";
            //----
            Room room = new Room();
            room.setId(Integer.valueOf(roomId.getText()));
            room.setNumOfBed(Integer.valueOf(numBeds.getText()));
            room.setFeature(feature.getText());
            room.setRoomType(roomType.getText());
            room.setRoomStatus("Available");
            room.setImagePath(filePath);
            function.addRoom(room);

            //            imageSave
            //--------------保存文件
            FileInputStream in = new FileInputStream(imageSave);
            ByteArrayOutputStream out = new ByteArrayOutputStream(4000);
            byte[] b = new byte[4000];
            int n;
            while ((n = in.read(b)) != -1) {
                out.write(b, 0, n);
            }
            in.close();
            out.close();
            byte[] be = out.toByteArray();
            //---
            String saveFile = "src/image/"+ roomId.getText() +".jpg";
//            String saveFile = "/Users/waiting/Documents/RMIT/Advance Programming/image/" + roomId.getText() + ".jpg";
            File targetFile = new File(saveFile);
            FileOutputStream out1 = new FileOutputStream(saveFile);
            out1.write(be);
            out1.flush();
            out1.close();
            //--
            information.setText("addRoom successfully.");
            //clean the textField
            roomId.setText("");
            roomType.setText("");
            feature.setText("");
            numBeds.setText("");
            image.setText("");
        }



    }

    public void choosePicture() throws IOException {
        Stage stage = (Stage) chooseP.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File selectedFile = fileChooser.showOpenDialog(stage);
        if(selectedFile != null){
            String picturePath = selectedFile.getPath();
            imageSave = selectedFile;
            image.setText(picturePath);
//            System.out.println(selectedFile);

        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
