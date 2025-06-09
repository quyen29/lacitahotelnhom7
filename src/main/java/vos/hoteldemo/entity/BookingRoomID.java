package vos.hoteldemo.entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class BookingRoomID implements Serializable {
    private int bookingID;
    private int roomID;

    public BookingRoomID() {}

    public BookingRoomID(int bookingID, int roomID) {
        this.bookingID = bookingID;
        this.roomID = roomID;
    }

    public int getBookingID() {
        return bookingID;
    }

    public int getRoomID() {
        return roomID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookingRoomID)) return false;
        BookingRoomID that = (BookingRoomID) o;
        return bookingID == that.bookingID && roomID == that.roomID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingID, roomID);
    }
}
