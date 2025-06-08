package vos.hoteldemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vos.hoteldemo.dao.BookingRepository;
import vos.hoteldemo.dao.BookingRoomRepository;
import vos.hoteldemo.entity.Booking;
import vos.hoteldemo.entity.BookingRoom;

import java.util.List;

@Service
public class BookingRoomServiceImpl implements BookingRoomService {
    @Autowired
    private BookingRoomRepository bookingRoomRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public List<BookingRoom> getActiveRoomBooking() {
        return bookingRoomRepository.findActiveRoomBookings();
    }

    @Override
    public int countTotalPeopleInHotel() {
        Integer total = bookingRoomRepository.sumNumberOfPeopleInActiveBookings();
        return total != null ? total : 0;
    }

    @Override
    public BookingRoom getByRoomAndBooking(Integer roomID, Integer bookingID) {
        return bookingRoomRepository.findByRoom_RoomIDAndBooking_BookingID(roomID, bookingID)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy BookingRoom phù hợp"));
    }

    @Override
    public void saveBooking(Booking booking) {
        bookingRepository.save(booking);
    }
}

