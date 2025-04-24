package vos.hoteldemo.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vos.hoteldemo.entity.Room;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    long countByStatus(String status);

    long countByStatusIn(List<String> statuses);

    @Query("SELECT r FROM Room r WHERE "
            + "(:roomTypeName IS NULL OR :roomTypeName = '' OR r.roomType.roomTypeName = :roomTypeName) AND "
            + "(:status IS NULL OR :status = '' OR r.status = :status)")
    List<Room> findByRoomTypeNameAndStatus(
            @Param("roomTypeName") String roomTypeName,
            @Param("status") String status);

    @Query("SELECT r.roomType.roomTypeName, COUNT(r) FROM Room r WHERE r.status = 'Trống' GROUP BY r.roomType.roomTypeName")
    List<Object[]> countRoomByEmptyStatus();

    @Query("SELECT r FROM Room r WHERE r.roomType.roomTypeID = :roomTypeId AND r.status = 'Trống' ORDER BY r.roomID ASC")
    List<Room> findAvailableByRoomTypeId(@Param("roomTypeId") int roomTypeId);

    @Query("SELECT r FROM Room r JOIN r.bookingRooms br WHERE br.booking.bookingID = :bookingId")
    List<Room> findRoomsByBookingID(@Param("bookingId") int bookingId);

    @Query("SELECT r FROM Room r WHERE " +
            "(:roomTypeName IS NULL OR :roomTypeName = '' OR r.roomType.roomTypeName = :roomTypeName) AND " +
            "(:status IS NULL OR :status = '' OR r.status = :status) AND " +
            "(r.status = 'Trống' OR r.status = 'Bảo trì')")
    Page<Room> findFilteredRooms(@Param("roomTypeName") String roomTypeName,
                                 @Param("status") String status,
                                 Pageable pageable);
}
