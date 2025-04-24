package vos.hoteldemo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "room_type")
public class RoomType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_type_id")
    private int roomTypeID;

    @Column(name = "room_type_name")
    private String roomTypeName;

    @Column(name = "max_occupancy")
    private int maxOccupancy;

    @Column(name = "room_image")
    private String roomImage;

    @Column(name = "area")
    private float area;

    @Column(name = "price")
    private float price;

    @Column(name = "description")
    private String description;

    public RoomType() {
    }

    public RoomType(int roomTypeID, String roomTypeName, int maxOccupancy, String roomImage, float area, float price, String description) {
        this.roomTypeID = roomTypeID;
        this.roomTypeName = roomTypeName;
        this.maxOccupancy = maxOccupancy;
        this.roomImage = roomImage;
        this.area = area;
        this.price = price;
        this.description = description;
    }

    public int getRoomTypeID() {
        return roomTypeID;
    }

    public void setRoomTypeID(int roomTypeID) {
        this.roomTypeID = roomTypeID;
    }

    public String getRoomTypeName() {
        return roomTypeName;
    }

    public void setRoomTypeName(String roomTypeName) {
        this.roomTypeName = roomTypeName;
    }

    public int getMaxOccupancy() {
        return maxOccupancy;
    }

    public void setMaxOccupancy(int maxOccupancy) {
        this.maxOccupancy = maxOccupancy;
    }

    public String getRoomImage() {
        return roomImage;
    }

    public void setRoomImage(String roomImage) {
        this.roomImage = roomImage;
    }

    public float getArea() {
        return area;
    }

    public void setArea(float area) {
        this.area = area;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
