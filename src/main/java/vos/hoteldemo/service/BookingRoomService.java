package vos.hoteldemo.service;

import vos.hoteldemo.entity.Booking;
import vos.hoteldemo.entity.BookingRoom;

import java.util.List;

public interface BookingRoomService {
    List<BookingRoom> getActiveRoomBooking();

    int countTotalPeopleInHotel();

    BookingRoom getByRoomAndBooking(Integer roomID, Integer bookingID);

    void saveBooking(Booking booking);
}
