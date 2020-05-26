package HSQLDB;

import model.HiringRecord;
import model.Room;
import model.User;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConnectionHSQL {
    //CityLodgeDB    <---- databaseNAME;

    private static ConnectionHSQL instance;

    public static synchronized ConnectionHSQL getInstance(){
        if(instance == null){
            instance = new ConnectionHSQL();
        }
        return instance;
    }

    //  connection
    public static Connection getConnection(String DB_NAME) {
        Connection con = null;
        //Registering the HSQLDB JDBC driver
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            con = DriverManager.getConnection ("jdbc:hsqldb:file:database/" + DB_NAME, "SA", "");

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        /* Database files will be created in the "database"
         * folder in the project. If no username or password is
         * specified, the default SA user and an empty password are used */
          return con;
    }

    //  users  database;
    public static Boolean createTable(String DB_NAME, String sql){
        int result = 9999999;
        try {
            Connection conn = getConnection(DB_NAME);
            Statement stmt = conn.createStatement();

            String sqlTest = "CREATE TABLE user(" +
                    "id INT NOT NULL," +
                    "email VARCHAR(20) NOT NULL," +
                    "password VARCHAR(20) NOT NULL," +
                    "user_name VARCHAR(20) NOT NULL," +
                    "PRIMARY KEY (id))";

            result  = stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(0 == result){
            return true;
        }else{
            return false;
        }
    }

    public static Boolean checkTableExist(String DB_NAME, String TABLE_NAME){
        Connection conn = getConnection(DB_NAME);
        Boolean flag = false;
        try {
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);
            if(tables != null){
                if(tables.next()){
                    flag = true;
                    System.out.println("Table " + TABLE_NAME + " exists.");
                }else{
                    flag = false;
                    System.out.println("Table " + TABLE_NAME + " does not exist.");
                }
                tables.close();
            }else{
                System.out.println(("Problem with retrieving database metadata"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return flag;
    }

    public static Boolean deleteTable(String DB_NAME, String TABLE_NAME){
        Boolean flag = false;
        try {
            Connection conn = getConnection(DB_NAME);
            Statement stmt = conn.createStatement();
            int result = stmt.executeUpdate("DROP TABLE " + TABLE_NAME.toUpperCase());
            if(0 == result){
                flag = true;
                System.out.println("Table " + TABLE_NAME + " has been deleted successfully");
            }else{
                flag = false;
                System.out.println("Table " + TABLE_NAME + " was not deleted");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static Boolean insertRow(String DB_NAME, String TABLE_NAME, String insertRow){
        Boolean flag = false;
        try {
            Connection conn = getConnection(DB_NAME);
            Statement stmt = conn.createStatement();
            String query = "INSERT INTO " + TABLE_NAME +
                    " VALUES (1, 's3388490', 'Peter', 'Tilmanis')";
            //" VALUES (2, 's3089940', 'Tom', 'Bruster')";

            int result = stmt.executeUpdate(insertRow);
            conn.commit();
            System.out.println("Insert into table " + TABLE_NAME + " executed successfully");
            System.out.println(result + " row(s) affected");
            flag = true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return flag;
    }

    public static Boolean deleteRow(String DB_NAME, String TABLE_NAME, String deleteRow){
        Boolean flag = false;
        try {
            Connection conn = getConnection(DB_NAME);
            Statement stmt = conn.createStatement();
            String query = "DELETE FROM " + TABLE_NAME +
                    " WHERE first_name LIKE 'Tom'";

            int result = stmt.executeUpdate(deleteRow);
            System.out.println("Delete from table " + TABLE_NAME + " executed successfully");
            System.out.println(result + " row(s) affected");
            flag = true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return flag;
    }

    public static Boolean updateQuery(String DB_NAME, String TABLE_NAME, String updateSQL){
        Boolean flag = false;
        try {
            Connection conn = getConnection(DB_NAME);
            Statement stmt = conn.createStatement();

            String query = "UPDATE " + TABLE_NAME +
                    " SET last_name = 'Singleton'" +
                    " WHERE student_number LIKE 's3388490'";

            int result = stmt.executeUpdate(updateSQL);
            System.out.println("Update table " + TABLE_NAME + " executed successfully");
            System.out.println(result + " row(s) affected");
            flag = true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return flag;
    }

    public static Object selectQuery(String DB_NAME, String TABLE_NAME){
        try {
            Connection conn = getConnection(DB_NAME);
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM " + TABLE_NAME;
            ResultSet resultSet = stmt.executeQuery(query);
            if("user".equalsIgnoreCase(TABLE_NAME)){
                ArrayList<User> users = new ArrayList<>();
                while (resultSet.next()){
                    User user = new User();
                    System.out.printf("Id: %d | userName: %s | password: %s \n",resultSet.getInt("id"), resultSet.getString("userName"), resultSet.getString("password"));
                    user.setId(resultSet.getInt("id"));
                    user.setUserName(resultSet.getString("userName"));
                    user.setPassword(resultSet.getString("password"));
                    users.add(user);
                }
                return users;
            }else if("room".equalsIgnoreCase(TABLE_NAME)){
                ArrayList<Room> rooms = new ArrayList<>();
                while(resultSet.next() ){
                    Room room = new Room();

                    String createRoom = "CREATE TABLE room(" +
                            "id INT NOT NULL," +
                            "numOfBed INT NOT NULL," +
                            "feature VARCHAR(20) NOT NULL," +
                            "roomStatus VARCHAR(20) NOT NULL," +
                            "roomType VARCHAR(20) NOT NULL," +
                            "imagePath VARCHAR(20) ," +
                            "PRIMARY KEY (id))";

                    System.out.printf("Id: %d | numOfBed: %d | feature: %s | roomStatus: %s | roomType: %s | imagePath: %s \n",
                            resultSet.getInt("id"),
                            resultSet.getInt("numOfBed"),
                            resultSet.getString("feature"),
                            resultSet.getString("roomStatus"),
                            resultSet.getString("roomType"),
                            resultSet.getString("imagePath"));

                    room.setId(resultSet.getInt("id"));
                    room.setNumOfBed(resultSet.getInt("numOfBed"));
                    room.setFeature(resultSet.getString("feature"));
                    room.setRoomStatus(resultSet.getString("roomStatus"));
                    room.setRoomType(resultSet.getString("roomType"));
                    room.setImagePath(resultSet.getString("imagePath"));
                    rooms.add(room);
                }
                return rooms;
            }else if("hiringrecord".equalsIgnoreCase(TABLE_NAME)){
                ArrayList<HiringRecord> hiringRecords = new ArrayList<>();
                while(resultSet.next() ){
                    HiringRecord hiringRecord = new HiringRecord();
                    System.out.printf("Id: %d | roomId: %d | estimatedDays: %d | actualDays: %d | rentDate: %s | estimatedReturnDate: %s | actualReturnDate: %s | rentalFee: %s | LateFee: %s | isCheckOut: %s| isLastOne: %s \n",
                            resultSet.getInt("id"),
                            resultSet.getInt("roomId"),
                            resultSet.getInt("estimatedDays"),
                            resultSet.getInt("actualDays"),
                            resultSet.getString("rentDate"),
                            resultSet.getString("estimatedReturnDate"),
                            resultSet.getString("actualReturnDate"),
                            resultSet.getString("rentalFee"),
                            resultSet.getString("LateFee"),
                            resultSet.getString("isCheckOut"),
                            resultSet.getString("isLastOne"));

                    hiringRecord.setId(resultSet.getInt("id"));
                    hiringRecord.setRoomId(resultSet.getInt("roomId"));
                    hiringRecord.setEstimatedDays(resultSet.getInt("estimatedDays"));
                    hiringRecord.setActualDays(resultSet.getInt("actualDays"));
                    hiringRecord.setRentDate(resultSet.getString("rentDate"));
                    hiringRecord.setEstimatedReturnDate(resultSet.getString("estimatedReturnDate"));
                    hiringRecord.setActualReturnDate(resultSet.getString("actualReturnDate"));
                    if(resultSet.getString("rentalFee") != null){
                        hiringRecord.setRentalFee(Double.valueOf(resultSet.getString("rentalFee")));
                    }
                    if(resultSet.getString("LateFee") != null){
                        hiringRecord.setLateFee(Double.valueOf(resultSet.getString("LateFee")));
                    }
                    hiringRecord.setIsCheckOut(resultSet.getString("isCheckOut"));
                    hiringRecord.setIsLastOne(resultSet.getString("isLastOne"));
                    hiringRecords.add(hiringRecord);
                }
                return hiringRecords;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }







}
