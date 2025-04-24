package vos.hoteldemo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "booking_room")
public class BookingRoom {
    @EmbeddedId
    private BookingRoomID id;

    @ManyToOne
    @MapsId("bookingID")
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne
    @MapsId("roomID")
    @JoinColumn(name = "room_id")
    private Room room;

    public BookingRoom() {}

    public BookingRoom(BookingRoomID id) {
        this.id = id;
    }

    public BookingRoomID getId() {
        return id;
    }

    public void setId(BookingRoomID id) {
        this.id = id;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
