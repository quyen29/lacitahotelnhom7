package vos.hoteldemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vos.hoteldemo.dao.*;
import vos.hoteldemo.entity.*;

import java.time.LocalDate;
import java.util.Date;
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

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private VoucherRepository voucherRepository;

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

    public List<RoomType> getAllRoomTypes() {
        List<RoomType> roomTypes = roomTypeRepository.findAll();
        for (RoomType rt : roomTypes) {
            Float defaultPrice = priceRepository.findPriceByAgeIdAndRoomTypeId(4, rt.getRoomTypeID());
            rt.setPrice(defaultPrice != null ? defaultPrice : 0f);
        }
        return roomTypes;
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

    public List<Voucher> getAvailableVouchers(int roomTypeId, int customerTypeId) {
        Date today = java.sql.Date.valueOf(LocalDate.now());
        return voucherRepository.findAvailableVouchers(today, roomTypeId, customerTypeId);
    }

    @Override
    public Bill getBillByBillId(int billID) {
        return billRepository.findById(billID).orElse(null);
    }
}
