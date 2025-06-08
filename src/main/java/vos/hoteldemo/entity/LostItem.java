package vos.hoteldemo.entity;

import jakarta.persistence.*;

import java.sql.Blob;

@Entity
@Table(name = "lost_items")
public class LostItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "items_id")
    private int itemID;

    @Column(name = "name")
    private String name;

    @Column(name = "room_id")
    private int roomID;

    @Column(name = "image")
    private Blob image;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private String status;

    public LostItem() {
    }

    public LostItem(int itemID, String name, int roomID, Blob image, String description, String status) {
        this.itemID = itemID;
        this.name = name;
        this.roomID = roomID;
        this.image = image;
        this.description = description;
        this.status = status;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
