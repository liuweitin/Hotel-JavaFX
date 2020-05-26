package service;

import HSQLDB.ConnectionHSQL;
import model.Room;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ServiceFunction {

    private static ServiceFunction instance;

    public static synchronized ServiceFunction getInstance(){
        if(instance == null){
            instance = new ServiceFunction();
        }
        return instance;
    }

    public void addRoom(Room room){
        ConnectionHSQL connectionHSQL = ConnectionHSQL.getInstance();
        Boolean flag = connectionHSQL.checkTableExist("CityLodgeDB","ROOM");
        if(!flag){
            //create table
            String createRoom = "CREATE TABLE room(" +
                    "id INT NOT NULL," +
                    "numOfBed INT NOT NULL," +
                    "feature VARCHAR(20) NOT NULL," +
                    "roomStatus VARCHAR(20) NOT NULL," +
                    "roomType VARCHAR(20) NOT NULL," +
                    "imagePath VARCHAR(20) ," +
                    "PRIMARY KEY (id))";
            connectionHSQL.createTable("CityLodgeDB",createRoom);
        }
        //insert user
        String insertRow = "INSERT INTO ROOM VALUES ("+ String.valueOf(room.getId()) + ","
                + String.valueOf(room.getNumOfBed()) +","
                + "'" + room.getFeature() + "',"
                + "'" + room.getRoomStatus() + "',"
                + "'" + room.getRoomType() + "',"
                + "'" + room.getImagePath() + "')";
        System.out.println(insertRow);
        connectionHSQL.insertRow("CityLodgeDB","ROOM", insertRow);
        connectionHSQL.selectQuery("CityLodgeDB","ROOM");
    }

    public ArrayList<Room> selectAllRoom(){
        ConnectionHSQL connectionHSQL = ConnectionHSQL.getInstance();
        ArrayList<Room> rooms = (ArrayList<Room>)connectionHSQL.selectQuery("CityLodgeDB","room");
        return rooms;
    }




}
