package vos.hoteldemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vos.hoteldemo.dao.BillRepository;
import vos.hoteldemo.dao.BookingRepository;
import vos.hoteldemo.dao.RoomRepository;
import vos.hoteldemo.dao.RoomTypeRepository;
import vos.hoteldemo.entity.Bill;
import vos.hoteldemo.entity.Booking;
import vos.hoteldemo.entity.Customer;
import vos.hoteldemo.entity.RoomType;

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

    @Override
    public List<RoomType> getAllRoomTypes() {
        return roomTypeRepository.findAll();
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

    @Override
    public Bill getBillByBillId(int billID) {
        return billRepository.findById(billID).orElse(null);
    }
}
