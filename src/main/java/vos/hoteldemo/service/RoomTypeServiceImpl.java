package vos.hoteldemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vos.hoteldemo.dao.PriceRepository;
import vos.hoteldemo.dao.RoomTypeRepository;
import vos.hoteldemo.entity.RoomType;

import java.util.List;

@Service
public class RoomTypeServiceImpl implements RoomTypeService {
    RoomTypeRepository roomTypeRepository;
    PriceRepository priceRepository;

    @Autowired
    public RoomTypeServiceImpl(RoomTypeRepository roomTypeRepository, PriceRepository priceRepository) {
        this.roomTypeRepository = roomTypeRepository;
        this.priceRepository = priceRepository;
    }

    @Override
    public List<RoomType> getAllRoomType() {
        return roomTypeRepository.findAll();
    }

    @Override
    public void save(RoomType roomType) {
        roomTypeRepository.save(roomType);
    }

    public Float getPriceForAdult(int roomTypeId) {
        return priceRepository.findPriceByRoomTypeIdAndAgeId(roomTypeId, 4);
    }
}
