package vos.hoteldemo.entity;

import jakarta.persistence.*;

import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private int bookingID;

    @Column(name = "number_of_adult")
    private int numberOfAdult;

    @Column(name = "number_of_child")
    private int numberOfChild;

    @Column(name = "booking_date")
    private LocalDateTime bookingDate;

    @Column(name = "check_in_date")
    private LocalDateTime checkInDate;

    @Column(name = "check_out_date")
    private LocalDateTime checkOutDate;

    @Column(name = "check_in_image")
    private Blob checkinImage;

    @Column(name = "check_out_image")
    private Blob checkoutImage;

    @Transient
    private RoomType roomType;

    @Transient
    private int roomQuantity;

    @Transient
    private int adult;
    @Transient
    private int elder;
    @Transient
    private int teen;
    @Transient
    private int child;
    @Transient
    private int underThree;
    @Transient
    private float totalPrice;

    public Booking() {
    }

    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public int getNumberOfAdult() {
        return numberOfAdult;
    }

    public void setNumberOfAdult(int numberOfAdult) {
        this.numberOfAdult = numberOfAdult;
    }

    public int getNumberOfChild() {
        return numberOfChild;
    }

    public void setNumberOfChild(int numberOfChild) {
        this.numberOfChild = numberOfChild;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public LocalDateTime getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDateTime checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDateTime getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDateTime checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public Blob getCheckinImage() {
        return checkinImage;
    }

    public void setCheckinImage(Blob checkinImage) {
        this.checkinImage = checkinImage;
    }

    public Blob getCheckoutImage() {
        return checkoutImage;
    }

    public void setCheckoutImage(Blob checkoutImage) {
        this.checkoutImage = checkoutImage;
    }

    @ManyToMany
    @JoinTable(
            name = "booking_room",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "room_id")
    )
    private List<Room> rooms;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Bill bill;

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    @Transient
    public float getTotal() {
        return (bill != null) ? bill.getTotal() : 0;
    }

    @Transient
    public String getCustomerName() {
        return (bill != null && bill.getCustomer() != null) ? bill.getCustomer().getFullName() : "N/A";
    }

    @Transient
    public String getRoomStatus() {
        if (rooms != null && !rooms.isEmpty()) {
            return rooms.get(0).getStatus();
        }
        return "Danh sách trống!";
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public int getRoomQuantity() {
        return roomQuantity;
    }

    public void setRoomQuantity(int roomQuantity) {
        this.roomQuantity = roomQuantity;
    }

    public int getAdult() {
        return adult;
    }

    public void setAdult(int adult) {
        this.adult = adult;
    }

    public int getElder() {
        return elder;
    }

    public void setElder(int elder) {
        this.elder = elder;
    }

    public int getTeen() {
        return teen;
    }

    public void setTeen(int teen) {
        this.teen = teen;
    }

    public int getChild() {
        return child;
    }

    public void setChild(int child) {
        this.child = child;
    }

    public int getUnderThree() {
        return underThree;
    }

    public void setUnderThree(int underThree) {
        this.underThree = underThree;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }
}
