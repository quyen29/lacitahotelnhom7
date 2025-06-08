package vos.hoteldemo.service;

import org.springframework.data.domain.Page;
import vos.hoteldemo.entity.Room;

import java.util.List;

public interface RoomService {
    Room getRoomByID(int roomID);

    List<Room> filterRooms(String roomTypeName, String status);

    void save(Room room);

    long countAllRooms();

    long countByStatus(String status);

    long countByStatuses(List<String> statuses);

    Page<Room> filterRoomsPaged(String roomTypeName, String status, int page, int size);
}
