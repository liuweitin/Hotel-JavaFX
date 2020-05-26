package model;

public class HiringRecord {

    public int id;
    //  FK = room.id
    public int roomId;

    public int estimatedDays;

    public int actualDays;
    // --yyyy-mm-dd
    public String rentDate;
    // --yyyy-mm-dd
    public String estimatedReturnDate;
    // --yyyy-mm-dd
    public String actualReturnDate;

    public Double rentalFee;

    public Double LateFee;

    public String isCheckOut;

    public String isLastOne;

    public HiringRecord() {
    }

    public HiringRecord(int id, int roomId, int estimatedDays, int actualDays, String rentDate, String estimatedReturnDate, String actualReturnDate, Double rentalFee, Double lateFee, String isCheckOut, String isLastOne) {
        this.id = id;
        this.roomId = roomId;
        this.estimatedDays = estimatedDays;
        this.actualDays = actualDays;
        this.rentDate = rentDate;
        this.estimatedReturnDate = estimatedReturnDate;
        this.actualReturnDate = actualReturnDate;
        this.rentalFee = rentalFee;
        LateFee = lateFee;
        this.isCheckOut = isCheckOut;
        this.isLastOne = isLastOne;
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

    public int getEstimatedDays() {
        return estimatedDays;
    }

    public void setEstimatedDays(int estimatedDays) {
        this.estimatedDays = estimatedDays;
    }

    public int getActualDays() {
        return actualDays;
    }

    public void setActualDays(int actualDays) {
        this.actualDays = actualDays;
    }

    public String getRentDate() {
        return rentDate;
    }

    public void setRentDate(String rentDate) {
        this.rentDate = rentDate;
    }

    public String getEstimatedReturnDate() {
        return estimatedReturnDate;
    }

    public void setEstimatedReturnDate(String estimatedReturnDate) {
        this.estimatedReturnDate = estimatedReturnDate;
    }

    public String getActualReturnDate() {
        return actualReturnDate;
    }

    public void setActualReturnDate(String actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
    }

    public Double getRentalFee() {
        return rentalFee;
    }

    public void setRentalFee(Double rentalFee) {
        this.rentalFee = rentalFee;
    }

    public Double getLateFee() {
        return LateFee;
    }

    public void setLateFee(Double lateFee) {
        LateFee = lateFee;
    }

    public String getIsCheckOut() {
        return isCheckOut;
    }

    public void setIsCheckOut(String isCheckOut) {
        this.isCheckOut = isCheckOut;
    }

    public String getIsLastOne() {
        return isLastOne;
    }

    public void setIsLastOne(String isLastOne) {
        this.isLastOne = isLastOne;
    }

    @Override
    public String toString() {
        return "HiringRecord{" +
                "id=" + id +
                ", roomId=" + roomId +
                ", estimatedDays=" + estimatedDays +
                ", actualDays=" + actualDays +
                ", rentDate='" + rentDate + '\'' +
                ", estimatedReturnDate='" + estimatedReturnDate + '\'' +
                ", actualReturnDate='" + actualReturnDate + '\'' +
                ", rentalFee=" + rentalFee +
                ", LateFee=" + LateFee +
                ", isCheckOut='" + isCheckOut + '\'' +
                ", isLastOne='" + isLastOne + '\'' +
                '}';
    }
}
