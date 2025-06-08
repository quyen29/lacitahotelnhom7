package vos.hoteldemo.service;

import vos.hoteldemo.entity.RoomType;

import java.util.List;

public interface RoomTypeService {
    List<RoomType> getAllRoomType();

    void save(RoomType roomType);
<<<<<<< HEAD

    Float getPriceForAdult(int roomTypeId);
=======
>>>>>>> 6db47b97814a1f9e6915b6f5424c8df81c3b6c43
}
