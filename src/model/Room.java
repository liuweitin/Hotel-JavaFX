package model;


import javafx.scene.image.ImageView;

public class Room {

    public int id;

    // A standard room can have either 1, 2, or 4 beds.
    // A suite always has 6 beds.
    public int numOfBed;

    // a short string (maximum 20 words) to summarise all the room features. For example "air conditioning, cable TV, Wifi, fridge, electric kettle".
    public String feature;

    //Available, Rented and Maintenance
    public String roomStatus;

    //Suite or Standard
    public String roomType;

    public String imagePath;

    public ImageView roomPicture;

    public Room(){
    }

    public Room(ImageView roomPicture, int id, String imagePath, int numOfBed, String feature, String roomStatus, String roomType) {
        this.roomPicture = roomPicture;
        this.id = id;
        this.numOfBed = numOfBed;
        this.feature = feature;
        this.roomStatus = roomStatus;
        this.roomType = roomType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getNumOfBed() {
        return numOfBed;
    }

    public void setNumOfBed(int numOfBed) {
        this.numOfBed = numOfBed;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(String roomStatus) {
        this.roomStatus = roomStatus;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public ImageView getRoomPicture() {
        return roomPicture;
    }

    public void setRoomPicture(ImageView roomPicture) {
        this.roomPicture = roomPicture;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", numOfBed=" + numOfBed +
                ", feature='" + feature + '\'' +
                ", roomStatus='" + roomStatus + '\'' +
                ", roomType='" + roomType + '\'' +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}
