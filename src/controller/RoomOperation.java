package controller;

import HSQLDB.ConnectionHSQL;
import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.HiringRecord;
import model.Room;
import service.ServiceFunction;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class RoomOperation extends Application implements Initializable {

    @FXML
    public Label informaiton;

    private static Stage stage;

    private static String ROOMID;

    private ServiceFunction serviceFunction = ServiceFunction.getInstance();
    private ConnectionHSQL connectionHSQL = ConnectionHSQL.getInstance();

    @FXML
    private DatePicker rentDate;

    @FXML
    private TextField estimatedDays;

    @FXML
    private Label checkinInformation;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ROOMID = stage.getTitle();
        String roomId  = ROOMID.split(":")[1];
        ArrayList<Room> rooms = serviceFunction.selectAllRoom();
        Room room = null;
        for (int i = 0; i < rooms.size(); i++) {
            if(rooms.get(i).getId() == Integer.valueOf(roomId)){
                room = rooms.get(i);
            }
        }
        //.ROOM STATUS
        if(room.getRoomStatus().equals("Available")){
            informaiton.setText("Room Status : Available");
        }else if(room.getRoomStatus().equals("Rented")){
            informaiton.setText("Room Status : Rented");
        }else if(room.getRoomStatus().equals("Maintenance")){
            informaiton.setText("Room Status : Maintenance");
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        AnchorPane roomOperationPage = FXMLLoader.load(getClass().getResource("../view/roomOperation.fxml"));
        Scene scene = new Scene(roomOperationPage, 700, 500);
        stage.setScene(scene);
        stage.show();
    }

    public void confirmCheckin(){
        String roomId  = ROOMID.split(":")[1];
        ArrayList<Room> rooms = serviceFunction.selectAllRoom();
        Room room = null;
        for (int i = 0; i < rooms.size(); i++) {
            if(rooms.get(i).getId() == Integer.valueOf(roomId)){
                room = rooms.get(i);
            }
        }
        if(rentDate.getValue() != null){
            if(room.getRoomStatus().equalsIgnoreCase("Available")){
                //----------
                int days = Integer.valueOf(estimatedDays.getText());
                boolean flag = containWeekend(rentDate.getValue().toString().trim(),days);
                if(flag){
                    if(days < 3){
                        checkinInformation.setText("If the scheduled time includes a weekend, rent for a minimum of 3 days");
                    }else if(days > 10){
                        checkinInformation.setText("Can only rent for up to 10 days");
                    }else{
                        rentRoom(days, rentDate.getValue().toString());
                    }
                }else{
                    if(days < 2){
                        checkinInformation.setText("If the scheduled time does not include a weekend, rent for a minimum of 2 days");
                    }else if(days > 10){
                        checkinInformation.setText("Can only rent for up to 10 days");
                    }else{
                        rentRoom(days, rentDate.getValue().toString());
                    }
                }
            }else{
                checkinInformation.setText("ROOM STATUS IS NOT AVAILABLE");
            }
        }else{
            checkinInformation.setText("RENTED DATE COULD NOT BE NULL");
        }
    }

    private void rentRoom(int days, String date) {
        Boolean flag = connectionHSQL.checkTableExist("CityLodgeDB","HIRINGRECORD");
        if(!flag){
            //delete table
//            connectionHSQL.deleteTable("CityLodgeDB","hiringrecord");

            //create table
            String createHiringRecord = "CREATE TABLE hiringrecord(" +
                    "id INT NOT NULL," +
                    "roomId INT NOT NULL," +
                    "estimatedDays INT," +
                    "actualDays INT," +
                    "rentDate VARCHAR(20)," +
                    "estimatedReturnDate VARCHAR(20)," +
                    "actualReturnDate VARCHAR(20)," +
                    "rentalFee VARCHAR(20)," +
                    "LateFee VARCHAR(20)," +
                    "isCheckOut VARCHAR(20)," +
                    "isLastOne VARCHAR(20)," +
                    "PRIMARY KEY (id))";
            connectionHSQL.createTable("CityLodgeDB",createHiringRecord);
        }
        //search allHiringRecord   id plus 1 automatically
        ArrayList<HiringRecord> hiringRecords = (ArrayList<HiringRecord>)connectionHSQL.selectQuery("CityLodgeDB","hiringrecord");
        int maxPlusOne;
        if(hiringRecords.size() >0){
            int[] ids = new int[hiringRecords.size()];
            for (int i = 0; i < hiringRecords.size(); i++) {
                ids[i] = hiringRecords.get(i).getId();
            }
            maxPlusOne = Arrays.stream(ids).max().getAsInt() + 1;
        }else{
            maxPlusOne = 1;
        }

//        System.out.println("date : " + date);
        String estimatedReturnDate = getDatePlusDate(date, days);
//        System.out.println("estimatedReturnDate : " + estimatedReturnDate);
        //insert hiringrecord     yyyy-mm-dd
        String insertRow = "INSERT INTO HIRINGRECORD VALUES ("+ String.valueOf(maxPlusOne) + ","
                + ROOMID.split(":")[1] +","
                + String.valueOf(days) + ","
                + "Null,"
                + "'" + date + "',"
                + "'" + estimatedReturnDate + "',"
                + "Null,"
                + "Null,"
                + "Null,"
                + "'false',"
                + "'true')";
        System.out.println("insertRow : " + insertRow);
        connectionHSQL.insertRow("CityLodgeDB","HIRINGRECORD", insertRow);
        //update ROOM STATUS TO RENTED
        String updateRoom = "UPDATE ROOM" +
                " SET roomStatus = 'Rented'" +
                " WHERE id = " + String.valueOf(ROOMID.split(":")[1]);
//        System.out.println("updateRoom : " + updateRoom);
        connectionHSQL.updateQuery("CityLodgeDB","ROOM",updateRoom);
        connectionHSQL.selectQuery("CityLodgeDB","ROOM");
    }


    @FXML
    private DatePicker returnDate;

    @FXML
    private Label checkoutInformation;

    @FXML
    private Label estimatedReturnDate;

    @FXML
    private Label actualDays;

    @FXML
    private Label rentalFee;

    @FXML
    private Label LateDays;

    @FXML
    private Label lateFee;

    @FXML
    private Label totalFee;

    private HiringRecord hiringRecord = new HiringRecord();

    //calculate the date of the rent
    public void calculate() throws ParseException {
        //get the room information
        String roomId  = ROOMID.split(":")[1];
        ArrayList<Room> rooms = serviceFunction.selectAllRoom();
        Room room = null;
        for (int i = 0; i < rooms.size(); i++) {
            if(rooms.get(i).getId() == Integer.valueOf(roomId)){
                room = rooms.get(i);
            }
        }
        //get the record
        ArrayList<HiringRecord> allHiringRecords = (ArrayList<HiringRecord>)connectionHSQL.selectQuery("CityLodgeDB","hiringrecord");
        ArrayList<HiringRecord> roomHiringRecords = new ArrayList<>();
        for (int i = 0; i < allHiringRecords.size(); i++) {
            if(allHiringRecords.get(i).getRoomId() == Integer.valueOf(roomId)){
                roomHiringRecords.add(allHiringRecords.get(i));
            }
        }

        for (int i = 0; i < roomHiringRecords.size(); i++) {
            if(roomHiringRecords.get(i).getIsCheckOut().equalsIgnoreCase("false")){
                hiringRecord = roomHiringRecords.get(i);
            }
        }


        if(returnDate.getValue() != null){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = format.parse(returnDate.getValue().toString());
            Date date2 = format.parse(hiringRecord.getRentDate());
            int daysDefference = (int) ((date1.getTime() - date2.getTime()) / (1000*3600*24));
            if(daysDefference > 0){
                hiringRecord.setActualDays(daysDefference);
                hiringRecord.setActualReturnDate(returnDate.getValue().toString());
                //calculate the Fee
                Double rentalFeeDouble = 0.00;
                Double lateFeeDouble = 0.00;
                if(room.getRoomType().equalsIgnoreCase("Suite")){
                    // $999 per day
                    //returned after the estimated return date   $1099
                    if(hiringRecord.getActualDays() <= hiringRecord.getEstimatedDays()){
                        rentalFeeDouble = Double.valueOf(hiringRecord.getActualDays() * 999);
                    }else{
                        rentalFeeDouble = Double.valueOf(hiringRecord.getActualDays() * 999);
                        lateFeeDouble = Double.valueOf((hiringRecord.getActualDays()-hiringRecord.getEstimatedDays()) * 1099);
                    }
                }else if(room.getRoomType().equalsIgnoreCase("Standard")){
//                    $59 per day if the room has 1 bed
//                    $99 per day if the room has 2 beds
//                    $199 per day if the rooms has 4 beds
                    if(room.getNumOfBed() == 1){
                        if(hiringRecord.getActualDays() <= hiringRecord.getEstimatedDays()){
                            rentalFeeDouble = Double.valueOf(hiringRecord.getActualDays() * 59);
                        }else{
                            rentalFeeDouble = Double.valueOf(hiringRecord.getActualDays() * 59);
                            lateFeeDouble = Double.valueOf((hiringRecord.getActualDays()-hiringRecord.getEstimatedDays()) * 59 * 135/100);
                        }
                    }else if(room.getNumOfBed() == 2){
                        if(hiringRecord.getActualDays() <= hiringRecord.getEstimatedDays()){
                            rentalFeeDouble = Double.valueOf(hiringRecord.getActualDays() * 99);
                        }else{
                            rentalFeeDouble = Double.valueOf(hiringRecord.getActualDays() * 99);
                            lateFeeDouble = Double.valueOf((hiringRecord.getActualDays()-hiringRecord.getEstimatedDays()) * 99 * 135/100);
                        }
                    }else if(room.getNumOfBed() == 4){
                        if(hiringRecord.getActualDays() <= hiringRecord.getEstimatedDays()){
                            rentalFeeDouble = Double.valueOf(hiringRecord.getActualDays() * 199);
                        }else{
                            rentalFeeDouble = Double.valueOf(hiringRecord.getActualDays() * 199);
                            lateFeeDouble = Double.valueOf((hiringRecord.getActualDays()-hiringRecord.getEstimatedDays()) * 199 * 135/100);
                        }
                    }
                }
                hiringRecord.setRentalFee(rentalFeeDouble);
                hiringRecord.setLateFee(lateFeeDouble);


                //set these information to the label on the screen
                estimatedReturnDate.setText("Estimated_Return_Date : " + hiringRecord.getEstimatedReturnDate());
                actualDays.setText("Actual_Days : " + hiringRecord.getActualDays());
                rentalFee.setText("Rental_Fee : " + hiringRecord.getRentalFee());
                if(hiringRecord.getActualDays() - hiringRecord.getEstimatedDays() > 0){
                    LateDays.setText("Late_Days : " + (hiringRecord.getActualDays() - hiringRecord.getEstimatedDays()));
                    lateFee.setText("Late_Fee : " + hiringRecord.getLateFee());
                    totalFee.setText("Total_Fee : " + (Double.valueOf(hiringRecord.getRentalFee()) + Double.valueOf(hiringRecord.getLateFee())));
                }else{
                    LateDays.setText("Check-out time did not exceed expectations");
                    lateFee.setText("Late_Fee : 0.00");
                    totalFee.setText("Total_Fee : " + hiringRecord.getRentalFee());
                }
            }else{
                checkoutInformation.setText("RETURN DATE SHOULD AFTER THE RENTED DATE");
            }
        }else{
            checkoutInformation.setText("RETURN DATE COULD NOT BE NULL");
        }
    }

    //confirm the rent information of the return room
    public void payConfirm(){
        String roomId  = ROOMID.split(":")[1];
        ArrayList<Room> rooms = serviceFunction.selectAllRoom();
        Room room = null;
        for (int i = 0; i < rooms.size(); i++) {
            if(rooms.get(i).getId() == Integer.valueOf(roomId)){
                room = rooms.get(i);
            }
        }
        if(room.getRoomStatus().equalsIgnoreCase("Rented")){
            String updateRoom = "UPDATE ROOM" +
                    " SET roomStatus = 'Available'" +
                    " WHERE id = " + String.valueOf(ROOMID.split(":")[1]);
            connectionHSQL.updateQuery("CityLodgeDB","ROOM",updateRoom);
            String updateHiringRecord = "UPDATE HIRINGRECORD" +
                    " SET actualDays = " + hiringRecord.getActualDays() + "," +
                    " actualReturnDate = '" + hiringRecord.getActualReturnDate() + "'," +
                    " rentalFee = '" + hiringRecord.getRentalFee() + "'," +
                    " LateFee = '" + hiringRecord.getLateFee() + "'," +
                    " isCheckOut = 'true'," +
                    " isLastOne = 'false'" +
                    " WHERE id = " + String.valueOf(hiringRecord.getId());
            System.out.println(updateHiringRecord);
            connectionHSQL.updateQuery("CityLodgeDB","HIRINGRECORD",updateHiringRecord);
        }else {
            checkoutInformation.setText("ROOM STATUS IS NOT RENTED, THEREFORE IT COULD NOT BE PAY THE BILL");
        }
    }


    @FXML
    private Label MInformation;

    @FXML
    private Label CMInformation;

    public void maintenance(){
        String roomId  = ROOMID.split(":")[1];
        ArrayList<Room> rooms = serviceFunction.selectAllRoom();
        Room room = null;
        for (int i = 0; i < rooms.size(); i++) {
            if(rooms.get(i).getId() == Integer.valueOf(roomId)){
                room = rooms.get(i);
            }
        }
        //update the room status
        if(room.getRoomStatus().equalsIgnoreCase("Available")){
            String updateRoom = "UPDATE ROOM" +
                    " SET roomStatus = 'Maintenance'" +
                    " WHERE id = " + String.valueOf(ROOMID.split(":")[1]);
            connectionHSQL.updateQuery("CityLodgeDB","ROOM",updateRoom);
            MInformation.setText("MAINTENANCE OPERATION IS SUCCESSFULLY");
        }else{
            MInformation.setText("ROOM IS NOT AVAILBALE, THEREFORE IT COULD NOT MAINTENANCE");
        }
    }

    public void completeMaintenance(){
        String roomId  = ROOMID.split(":")[1];
        ArrayList<Room> rooms = serviceFunction.selectAllRoom();
        Room room = null;
        for (int i = 0; i < rooms.size(); i++) {
            if(rooms.get(i).getId() == Integer.valueOf(roomId)){
                room = rooms.get(i);
            }
        }
        //update the room status
        if(room.getRoomStatus().equalsIgnoreCase("Maintenance")){
            String updateRoom = "UPDATE ROOM" +
                    " SET roomStatus = 'Available'" +
                    " WHERE id = " + String.valueOf(ROOMID.split(":")[1]);
            connectionHSQL.updateQuery("CityLodgeDB","ROOM",updateRoom);
            CMInformation.setText("COMPLETE MAINTENANCE IS SUCCESSFULLY");
        }else{
            CMInformation.setText("ROOM IS NOT IN MAINTENANCE, THEREFORE IT COULD NOT BE COMPLETED");
        }
    }


    public void deleteRoom(){
        String roomid = ROOMID.split(":")[1];

        String deleteHiringRecord = "DELETE FROM HIRINGRECORD WHERE roomId = " + roomid;
        String deleteRoom = "DELETE FROM ROOM WHERE id = " + roomid;
        System.out.println(deleteHiringRecord);
        System.out.println(deleteRoom);


        connectionHSQL.deleteRow("CityLodgeDB","HIRINGRECORD",deleteHiringRecord);
        connectionHSQL.deleteRow("CityLodgeDB","ROOM",deleteRoom);

        //  check--
        connectionHSQL.selectQuery("CityLodgeDB","HIRINGRECORD");
        connectionHSQL.selectQuery("CityLodgeDB","ROOM");
    }




    @FXML
    private Label id,numBed,feature;

    @FXML
    private ImageView roomImage;

    @FXML
    private TextArea record;

    public void showDetial(){
        ArrayList<Room> rooms = serviceFunction.selectAllRoom();
        ArrayList<HiringRecord> hiringRecords = (ArrayList<HiringRecord>)connectionHSQL.selectQuery("CityLodgeDB","hiringrecord");
        int roomId = Integer.valueOf(ROOMID.split(":")[1]);

        for (int i = 0; i < rooms.size(); i++) {
            if(roomId == rooms.get(i).getId()){
                id.setText("RoomID : " + String.valueOf(rooms.get(i).getId()));
                numBed.setText("Number of beds : " + String.valueOf(rooms.get(i).getNumOfBed()));
                feature.setText("Feature : " + rooms.get(i).getFeature());
                roomImage.setFitHeight(200);
                roomImage.setFitWidth(300);
                roomImage.setImage(new Image(this.getClass().getResourceAsStream(rooms.get(i).getImagePath())));
            }
        }
        String textArea = "";
        for (int i = 0; i < hiringRecords.size(); i++) {
            if(hiringRecords.get(i).getRoomId() == roomId){
                textArea += hiringRecords.toString() + "\n";
            }

        }
        record.setText(textArea);
    }


    //yyyy-mm-dd  imput = result
    private String getDatePlusDate(String dateImput, int days) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date sDate = null;
        String str = null;
        try {
            sDate = sdf.parse(dateImput);
            Calendar c = Calendar.getInstance();
            c.setTime(sDate);
            c.add(Calendar.DAY_OF_MONTH, days);
            Date date = c.getTime();
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            str = format.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String result = str.split("\\/")[2] + "-" + str.split("\\/")[1] + "-" + str.split("\\/")[0];
        return result;
    }

    private boolean containWeekend(String dateEnter, int days) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(dateEnter);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        Date[] newDate = new Date[days];
        for (int i = 0; i < days; i++) {
            cal.add(Calendar.DAY_OF_MONTH, i);
            Date a = cal.getTime();
            newDate[i] = a;
        }
        boolean flag = false;
        for (int i = 0; i < days; i++) {
            if(isWeekend(newDate[i])) {
                flag = true;
            }
        }
        return flag;
    }

    private boolean isWeekend(Date date) {
        boolean isWeekend = false;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        isWeekend = cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
        return isWeekend;
    }







}
