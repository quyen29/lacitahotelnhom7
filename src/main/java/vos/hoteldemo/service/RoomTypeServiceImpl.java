package vos.hoteldemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
<<<<<<< HEAD
import vos.hoteldemo.dao.PriceRepository;
=======
>>>>>>> 6db47b97814a1f9e6915b6f5424c8df81c3b6c43
import vos.hoteldemo.dao.RoomTypeRepository;
import vos.hoteldemo.entity.RoomType;

import java.util.List;

@Service
public class RoomTypeServiceImpl implements RoomTypeService {
    RoomTypeRepository roomTypeRepository;
<<<<<<< HEAD
    PriceRepository priceRepository;

    @Autowired
    public RoomTypeServiceImpl(RoomTypeRepository roomTypeRepository, PriceRepository priceRepository) {
        this.roomTypeRepository = roomTypeRepository;
        this.priceRepository = priceRepository;
=======

    @Autowired
    public RoomTypeServiceImpl(RoomTypeRepository roomTypeRepository) {
        this.roomTypeRepository = roomTypeRepository;
>>>>>>> 6db47b97814a1f9e6915b6f5424c8df81c3b6c43
    }

    @Override
    public List<RoomType> getAllRoomType() {
        return roomTypeRepository.findAll();
    }

    @Override
    public void save(RoomType roomType) {
        roomTypeRepository.save(roomType);
    }
<<<<<<< HEAD

    public Float getPriceForAdult(int roomTypeId) {
        return priceRepository.findPriceByRoomTypeIdAndAgeId(roomTypeId, 4);
    }
=======
>>>>>>> 6db47b97814a1f9e6915b6f5424c8df81c3b6c43
}
