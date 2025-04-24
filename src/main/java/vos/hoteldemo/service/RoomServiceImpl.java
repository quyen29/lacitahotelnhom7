package vos.hoteldemo.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vos.hoteldemo.dao.RoomRepository;
import vos.hoteldemo.entity.Room;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {
    private RoomRepository roomRepository;

    @Autowired
    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public Room getRoomByID(int roomID) {
        return roomRepository.findById(roomID).orElseThrow(() -> new RuntimeException("Phòng không tồn tại"));
    }

    @Override
    public long countAllRooms() {
        return roomRepository.count();
    }

    @Override
    public long countByStatus(String status) {
        return roomRepository.countByStatus(status);
    }

    @Override
    public long countByStatuses(List<String> statuses) {
        return roomRepository.countByStatusIn(statuses);
    }

    @Override
    public List<Room> filterRooms(String roomTypeName, String status) {
        if ((roomTypeName == null || roomTypeName.isEmpty()) &&
                (status == null || status.isEmpty())) {
            return roomRepository.findAll();
        }
        return roomRepository.findByRoomTypeNameAndStatus(roomTypeName, status);
    }

    @Override
    @Transactional
    public void save(Room room) {
        roomRepository.save(room);
    }

    @Override
    public Page<Room> filterRoomsPaged(String roomTypeName, String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("roomID").descending());
        return roomRepository.findFilteredRooms(roomTypeName, status, pageable);
    }
}
