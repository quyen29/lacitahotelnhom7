package vos.hoteldemo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vos.hoteldemo.entity.BookingRoom;
import vos.hoteldemo.entity.BookingRoomID;

import java.util.List;
import java.util.Optional;

public interface BookingRoomRepository extends JpaRepository<BookingRoom, BookingRoomID> {
    @Query("SELECT SUM(b.booking.numberOfAdult + b.booking.numberOfChild) FROM BookingRoom b WHERE b.room.status = 'Đang sử dụng'")
    Integer sumNumberOfPeopleInActiveBookings();

    @Query("""
                SELECT br FROM BookingRoom br
                WHERE br.room.status IN ('Đã đặt', 'Đang sử dụng')
                AND br.booking.bookingID = (
                    SELECT MAX(br2.booking.bookingID)
                    FROM BookingRoom br2
                    WHERE br2.room.roomID = br.room.roomID
                )
                ORDER BY br.booking.bookingID DESC
            """)
    List<BookingRoom> findActiveRoomBookings();

    @Query(value = "SELECT room_id FROM booking_room WHERE booking_id = :booking_id", nativeQuery = true)
    List<Integer> findRoomIdsByBookingID(@Param("booking_id") int bookingID);

    List<BookingRoom> findByBooking_BookingID(Integer bookingID);

    Optional<BookingRoom> findByRoom_RoomIDAndBooking_BookingID(Integer roomID, Integer bookingID);
}

