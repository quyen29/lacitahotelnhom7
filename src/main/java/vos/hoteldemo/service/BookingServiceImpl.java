package vos.hoteldemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
<<<<<<< HEAD
import vos.hoteldemo.dao.*;
import vos.hoteldemo.entity.*;

import java.time.LocalDate;
import java.util.Date;
=======
import vos.hoteldemo.dao.BillRepository;
import vos.hoteldemo.dao.BookingRepository;
import vos.hoteldemo.dao.RoomRepository;
import vos.hoteldemo.dao.RoomTypeRepository;
import vos.hoteldemo.entity.Bill;
import vos.hoteldemo.entity.Booking;
import vos.hoteldemo.entity.Customer;
import vos.hoteldemo.entity.RoomType;

>>>>>>> 6db47b97814a1f9e6915b6f5424c8df81c3b6c43
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookingServiceImpl implements BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @Autowired
    private BillRepository billRepository;

<<<<<<< HEAD
    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private VoucherRepository voucherRepository;

=======
>>>>>>> 6db47b97814a1f9e6915b6f5424c8df81c3b6c43
    @Override
    public Map<String, Long> countRoomByEmptyStatus() {
        List<Object[]> list = roomRepository.countRoomByEmptyStatus();
        Map<String, Long> map = new HashMap<>();
        for (Object[] row : list) {
            String roomTypeName = (String) row[0];
            Long count = (Long) row[1];
            map.put(roomTypeName, count);
        }
        return map;
    }

<<<<<<< HEAD
    public List<RoomType> getAllRoomTypes() {
        List<RoomType> roomTypes = roomTypeRepository.findAll();
        for (RoomType rt : roomTypes) {
            Float defaultPrice = priceRepository.findPriceByAgeIdAndRoomTypeId(4, rt.getRoomTypeID());
            rt.setPrice(defaultPrice != null ? defaultPrice : 0f);
        }
        return roomTypes;
=======
    @Override
    public List<RoomType> getAllRoomTypes() {
        return roomTypeRepository.findAll();
>>>>>>> 6db47b97814a1f9e6915b6f5424c8df81c3b6c43
    }

    @Override
    public List<Booking> findByCustomer(Customer customer) {
        return bookingRepository.findAllByCustomerViaBill(customer);
    }

    @Override
    public Map<Integer, Float> getTotalByBookingIds(List<Integer> bookingIds) {
        Map<Integer, Float> map = new HashMap<>();
        for (int id : bookingIds) {
            Bill bill = billRepository.findByBookingID(id);
            if (bill != null) {
                map.put(id, bill.getTotal());
            } else {
                map.put(id, 0f);
            }
        }
        return map;
    }

<<<<<<< HEAD
    public List<Voucher> getAvailableVouchers(int roomTypeId, int customerTypeId) {
        Date today = java.sql.Date.valueOf(LocalDate.now());
        return voucherRepository.findAvailableVouchers(today, roomTypeId, customerTypeId);
    }

=======
>>>>>>> 6db47b97814a1f9e6915b6f5424c8df81c3b6c43
    @Override
    public Bill getBillByBillId(int billID) {
        return billRepository.findById(billID).orElse(null);
    }
}
