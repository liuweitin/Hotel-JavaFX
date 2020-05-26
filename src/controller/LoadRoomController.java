package controller;

import HSQLDB.ConnectionHSQL;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Room;
import service.ServiceFunction;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class LoadRoomController {

    @FXML
    private Button chooseF;

    @FXML
    private TextField loadFile;

    private ServiceFunction function = ServiceFunction.getInstance();
    private ConnectionHSQL connectionHSQL = ConnectionHSQL.getInstance();

    public void chooseFile(){
        Stage stage = (Stage) chooseF.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File selectedFile = fileChooser.showOpenDialog(stage);
        if(selectedFile != null){
            String filePath = selectedFile.getPath();
            loadFile.setText(filePath);
        }
    }


   public void submitRoom(){
       Room room = new Room();
       room.setId(9999);
       room.setFeature("airCondition");
       room.setImagePath("../image/9999.jpg");
       room.setNumOfBed(6);
       room.setRoomStatus("Available");
       room.setRoomType("Suite");
       function.addRoom(room);

   }

   public void delete(){
       String deleteHiringRecord = "DELETE FROM HIRINGRECORD WHERE roomId = " + 9999;
       String deleteRoom = "DELETE FROM ROOM WHERE id = " + 9999;
       System.out.println(deleteHiringRecord);
       System.out.println(deleteRoom);


       connectionHSQL.deleteRow("CityLodgeDB","HIRINGRECORD",deleteHiringRecord);
       connectionHSQL.deleteRow("CityLodgeDB","ROOM",deleteRoom);

       //  check--
       connectionHSQL.selectQuery("CityLodgeDB","HIRINGRECORD");
       connectionHSQL.selectQuery("CityLodgeDB","ROOM");

   }


}
