package vos.hoteldemo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "price")
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "price_id")
    private int priceID;

    @Column(name = "age_id")
    private int ageID;

    @Column(name = "room_type_id")
    private int roomTypeID;

    @Column(name = "price")
    private float price;

    public Price() {
    }

    public Price(int priceID, int ageID, int roomTypeID, float price) {
        this.priceID = priceID;
        this.ageID = ageID;
        this.roomTypeID = roomTypeID;
        this.price = price;
    }

    public int getPriceID() {
        return priceID;
    }

    public void setPriceID(int priceID) {
        this.priceID = priceID;
    }

    public int getAgeID() {
        return ageID;
    }

    public void setAgeID(int ageID) {
        this.ageID = ageID;
    }

    public int getRoomTypeID() {
        return roomTypeID;
    }

    public void setRoomTypeID(int roomTypeID) {
        this.roomTypeID = roomTypeID;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
