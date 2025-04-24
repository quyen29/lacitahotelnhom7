package vos.hoteldemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vos.hoteldemo.dao.RoomTypeRepository;
import vos.hoteldemo.entity.RoomType;

import java.util.List;

@Service
public class RoomTypeServiceImpl implements RoomTypeService {
    RoomTypeRepository roomTypeRepository;

    @Autowired
    public RoomTypeServiceImpl(RoomTypeRepository roomTypeRepository) {
        this.roomTypeRepository = roomTypeRepository;
    }

    @Override
    public List<RoomType> getAllRoomType() {
        return roomTypeRepository.findAll();
    }

    @Override
    public void save(RoomType roomType) {
        roomTypeRepository.save(roomType);
    }
}
