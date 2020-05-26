package controller;

import HSQLDB.ConnectionHSQL;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.HiringRecord;
import model.Room;
import service.ServiceFunction;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class RecordController implements Initializable {

    @FXML
    private TextArea textpage;

    private ServiceFunction serviceFunction = ServiceFunction.getInstance();
    private ConnectionHSQL connectionHSQL = ConnectionHSQL.getInstance();

    private static String outputString;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ArrayList<Room> rooms = serviceFunction.selectAllRoom();
        ArrayList<HiringRecord> hiringRecords = (ArrayList<HiringRecord>)connectionHSQL.selectQuery("CityLodgeDB","hiringrecord");
        String output = "";
        for (int i = 0; i < rooms.size(); i++) {
            String record = "";
            for (int j = 0; j < hiringRecords.size(); j++) {
                if(rooms.get(i).getId() == hiringRecords.get(j).getRoomId()){
                    record += hiringRecords.get(j).toString() + "\n";
                }
            }
            output += rooms.get(i).toString() + "\n" + record;
        }
        outputString = output;
        textpage.setText(outputString);
    }

    @FXML
    private TextField savePath;

    public void output(){
        System.out.println("output::----------" + savePath);
        ArrayList<Room> rooms = serviceFunction.selectAllRoom();
        ArrayList<HiringRecord> hiringRecords = (ArrayList<HiringRecord>)connectionHSQL.selectQuery("CityLodgeDB","hiringrecord");
        String filename = "/Users/waiting/Downloads/javafx.txt";
        FileWriter fstream;
        try {
            fstream = new FileWriter(filename);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write("JAVAFX  GENERATE: ");
            out.newLine();
            for (int i = 0; i < rooms.size(); i++) {
                out.write(rooms.get(i).toString());
                out.newLine();
                for (int j = 0; j < hiringRecords.size(); j++) {
                    if(rooms.get(i).getId() == hiringRecords.get(j).getRoomId()){
                        out.write(hiringRecords.get(j).toString());
                        out.newLine();
                    }
                }
            }
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
