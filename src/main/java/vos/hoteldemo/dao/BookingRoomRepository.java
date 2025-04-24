package vos.hoteldemo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vos.hoteldemo.entity.BookingRoom;
import vos.hoteldemo.entity.BookingRoomID;

import java.util.List;
import java.util.Optional;

public interface BookingRoomRepository extends JpaRepository<BookingRoom, BookingRoomID> {
    @Query("SELECT SUM(b.booking.numberOfPeople) FROM BookingRoom b WHERE b.room.status IN ('Đang sử dụng')")
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

    Optional<BookingRoom> findByRoom_RoomIDAndBooking_BookingID(Integer roomID, Integer bookingID);
}

