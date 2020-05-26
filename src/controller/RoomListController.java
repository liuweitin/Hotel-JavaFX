package controller;

import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Room;
import service.ServiceFunction;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import java.util.ResourceBundle;

public class RoomListController implements Initializable {

    @FXML
    private TableView<Room> table;

    @FXML
    private TableColumn<Room, String> image,roomId,feature,numbeds,roomType,roomStatus,function;

    private ServiceFunction serviceFunction = ServiceFunction.getInstance();

    private ObservableList<Room> roomList = FXCollections.observableArrayList();
    private ObservableList<Room> roomListSearch = FXCollections.observableArrayList();

    public void findRooms(){
        System.out.println("Display Room List");
        ArrayList<Room> rooms = serviceFunction.selectAllRoom();
        if(rooms.size() > 0){
            for (int i = 0; i < rooms.size(); i++) {
//                ImageView imageView = new ImageView(new Image(this.getClass().getResourceAsStream("../image/100.jpg")));
                ImageView imageView = new ImageView(new Image(this.getClass().getResourceAsStream(rooms.get(i).getImagePath())));
                imageView.setFitHeight(60);
                imageView.setFitWidth(60);
                Room room = new Room();
                room = rooms.get(i);
                room.setRoomPicture(imageView);
                roomListSearch.add(room);
            }
            table.getItems().clear();
            table.setItems(roomListSearch);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        image.setCellValueFactory(new PropertyValueFactory<>("roomPicture"));
        roomId.setCellValueFactory(new PropertyValueFactory<>("id"));
        feature.setCellValueFactory(new PropertyValueFactory<>("feature"));
        numbeds.setCellValueFactory(new PropertyValueFactory<>("numOfBed"));
        roomType.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        roomStatus.setCellValueFactory(new PropertyValueFactory<>("roomStatus"));
        //
        function.setCellFactory((col)-> {
            TableCell<Room, String> cell = new TableCell<Room, String>() {

                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);

                    if (!empty) {
//                        ImageView delICON = new ImageView(getClass().getResource("delete.png").toString());
//                        Button delBtn = new Button("删除", delICON);
                        Button operatioon = new Button("operation");
                        this.setGraphic(operatioon);
                        operatioon.setOnMouseClicked((me) -> {
                            Room clickedRoom = this.getTableView().getItems().get(this.getIndex());
                            //--
                            System.out.println("CLICK ROOM ID : " + clickedRoom.getId());

                            //open new window
                            RoomOperation roomOperation = new RoomOperation();
//                            RoomOperation.setRoomId(clickedRoom.getId());
                            try {
                                Stage stage = new Stage();
                                stage.setTitle("ROOMID:" + clickedRoom.getId());
                                roomOperation.start(stage);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            //

                        });
                    }
                }
            };
            return cell;
        });


        ArrayList<Room> rooms = serviceFunction.selectAllRoom();
        for (int i = 0; i < rooms.size(); i++) {
//            ImageView imageView = new ImageView(new Image(this.getClass().getResourceAsStream("../image/100.jpg")));
            System.out.println(rooms.get(i).getImagePath());
            ImageView imageView = new ImageView(new Image(this.getClass().getResourceAsStream(rooms.get(i).getImagePath())));
            imageView.setFitHeight(60);
            imageView.setFitWidth(60);
            Room room = new Room();
            room = rooms.get(i);
            room.setRoomPicture(imageView);
            roomList.add(room);
        }

        table.setItems(roomList);
    }



}
