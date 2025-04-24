package vos.hoteldemo.service;

import vos.hoteldemo.entity.Bill;
import vos.hoteldemo.entity.Booking;
import vos.hoteldemo.entity.Customer;
import vos.hoteldemo.entity.RoomType;

import java.util.List;
import java.util.Map;

public interface BookingService {
    Map<String, Long> countRoomByEmptyStatus();

    List<RoomType> getAllRoomTypes();

    List<Booking> findByCustomer(Customer customer);

    Map<Integer, Float> getTotalByBookingIds(List<Integer> bookingIds);

    Bill getBillByBillId(int billID);
}
