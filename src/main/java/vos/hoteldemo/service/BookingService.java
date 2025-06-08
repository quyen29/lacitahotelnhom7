package vos.hoteldemo.service;

<<<<<<< HEAD
import vos.hoteldemo.entity.*;
=======
import vos.hoteldemo.entity.Bill;
import vos.hoteldemo.entity.Booking;
import vos.hoteldemo.entity.Customer;
import vos.hoteldemo.entity.RoomType;
>>>>>>> 6db47b97814a1f9e6915b6f5424c8df81c3b6c43

import java.util.List;
import java.util.Map;

public interface BookingService {
    Map<String, Long> countRoomByEmptyStatus();

    List<RoomType> getAllRoomTypes();

    List<Booking> findByCustomer(Customer customer);

    Map<Integer, Float> getTotalByBookingIds(List<Integer> bookingIds);

<<<<<<< HEAD
    public List<Voucher> getAvailableVouchers(int roomTypeId, int customerTypeId);

=======
>>>>>>> 6db47b97814a1f9e6915b6f5424c8df81c3b6c43
    Bill getBillByBillId(int billID);
}
