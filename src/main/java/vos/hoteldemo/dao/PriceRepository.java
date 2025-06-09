package vos.hoteldemo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vos.hoteldemo.entity.Price;

@Repository
public interface PriceRepository extends JpaRepository<Price, Integer> {
    @Query("SELECT p.price FROM Price p WHERE p.roomTypeID = :roomTypeID AND p.ageID = 4")
    Float findDefaultPriceByRoomTypeId(@Param("roomTypeID") Integer roomTypeID);

    @Query("SELECT p.price FROM Price p WHERE p.ageID = :ageId AND p.roomTypeID = :roomTypeId")
    Float findPriceByAgeIdAndRoomTypeId(@Param("ageId") int ageId, @Param("roomTypeId") int roomTypeId);

    @Query("SELECT p.price FROM Price p WHERE p.roomTypeID = :roomTypeId AND p.ageID = :ageId")
    Float findPriceByRoomTypeIdAndAgeId(@Param("roomTypeId") int roomTypeId, @Param("ageId") int ageId);
}