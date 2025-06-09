package vos.hoteldemo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vos.hoteldemo.entity.CustomerType;
import vos.hoteldemo.entity.RoomType;
import vos.hoteldemo.entity.VoucherCustomerRoom;

import java.util.List;

@Repository
public interface VoucherCustomerRoomRepository extends JpaRepository<VoucherCustomerRoom, Integer> {
    List<VoucherCustomerRoom> findByRoomTypeAndCustomerType(RoomType roomType, CustomerType customerType);
}
