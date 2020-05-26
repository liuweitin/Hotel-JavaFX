package model;

public class Image {

    public int id;

    //  PK = room.id
    public int roomId;

    public String imagePath;

    public Image() {
    }

    public Image(int id, int roomId, String imagePath) {
        this.id = id;
        this.roomId = roomId;
        this.imagePath = imagePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
