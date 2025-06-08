package vos.hoteldemo.service;

import vos.hoteldemo.entity.*;

import java.util.List;
import java.util.Map;

public interface BookingService {
    Map<String, Long> countRoomByEmptyStatus();

    List<RoomType> getAllRoomTypes();

    List<Booking> findByCustomer(Customer customer);

    Map<Integer, Float> getTotalByBookingIds(List<Integer> bookingIds);

    public List<Voucher> getAvailableVouchers(int roomTypeId, int customerTypeId);

    Bill getBillByBillId(int billID);
}
