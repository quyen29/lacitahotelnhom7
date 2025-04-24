package vos.hoteldemo.service;

import vos.hoteldemo.entity.RoomType;

import java.util.List;

public interface RoomTypeService {
    List<RoomType> getAllRoomType();

    void save(RoomType roomType);
}
