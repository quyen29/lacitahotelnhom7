package vos.hoteldemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vos.hoteldemo.dao.*;
import vos.hoteldemo.entity.*;
import vos.hoteldemo.service.BookingService;
import vos.hoteldemo.service.EmailService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/booking")
public class BookingController {
    @Autowired
    private BookingService bookingService;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RoomTypeRepository roomTypeRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private BookingRoomRepository bookingRoomRepository;
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private EmailService emailService;

    @GetMapping
    public String showBookingPage(Model model) {
        model.addAttribute("room", bookingService.countRoomByEmptyStatus());
        model.addAttribute("roomTypes", bookingService.getAllRoomTypes());
        return "customer/booking";
    }

    @GetMapping("/showBookingForm")
    public String showBookingForm(@RequestParam("roomType") String roomTypeName,
                                  @RequestParam("price") float price,
                                  @RequestParam("available") int available,
                                  Model model) {
        RoomType selectedRoomType = bookingService.getAllRoomTypes().stream()
                .filter(rt -> rt.getRoomTypeName().equals(roomTypeName))
                .findFirst().orElse(null);

        if (selectedRoomType != null) {
            model.addAttribute("roomType", selectedRoomType);
            model.addAttribute("available", available);
        }
        return "customer/bookingForm";
    }

    @PostMapping("/showBookingForm/submit")
    public String submitBooking(@RequestParam int roomTypeId,
                                @RequestParam float price,
                                @RequestParam String checkInDate,
                                @RequestParam String checkOutDate,
                                @RequestParam int checkInHour,
                                @RequestParam int checkOutHour,
                                @RequestParam int roomQuantity,
                                @RequestParam int totalPeople,
                                @RequestParam String paymentMethod,
                                @RequestParam float totalPrice,
                                Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Customer customer = customerRepository.findByEmail(auth.getName());
        Booking booking = new Booking();
        booking.setNumberOfPeople(totalPeople);
        booking.setBookingDate(LocalDateTime.now());
        booking.setCheckInDate(LocalDateTime.parse(checkInDate + "T" + String.format("%02d:00:00", checkInHour)));
        booking.setCheckOutDate(LocalDateTime.parse(checkOutDate + "T" + String.format("%02d:00:00", checkOutHour)));
        bookingRepository.save(booking);
        Bill bill = new Bill();
        bill.setBooking(booking);
        bill.setCustomer(customer);
        bill.setInvoiceTime(LocalDateTime.now());
        bill.setPaymentStatus(1);
        bill.setPaymentMethod(paymentMethod);
        bill.setTotal(totalPrice);
        billRepository.save(bill);
        List<Room> availableRooms = roomRepository.findAvailableByRoomTypeId(roomTypeId);
        if (availableRooms.size() < roomQuantity) {
            model.addAttribute("error", "Không đủ phòng trống để đặt.");
            return "/showPage404";
        }
        for (int i = 0; i < roomQuantity; i++) {
            Room room = availableRooms.get(i);
            room.setStatus("Đã đặt");
            roomRepository.save(room);
            BookingRoom bookingRoom = new BookingRoom();
            bookingRoom.setId(new BookingRoomID(booking.getBookingID(), room.getRoomID()));
            bookingRoom.setBooking(booking);
            bookingRoom.setRoom(room);
            bookingRoomRepository.save(bookingRoom);
        }
        RoomType roomType = roomTypeRepository.findById(roomTypeId).orElse(null);
        emailService.sendInvoiceEmail(customer.getEmail(), bill, booking, customer, roomType, roomQuantity);
        model.addAttribute("bill", bill);
        model.addAttribute("booking", booking);
        model.addAttribute("customer", customer);
        model.addAttribute("roomType", roomType);
        model.addAttribute("roomQuantity", roomQuantity);
        return "customer/bill";
    }

    @GetMapping("/showBookingForm/showBill")
    public String showBill(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Customer customer = customerRepository.findByEmail(auth.getName());
        Bill bill = billRepository.findTopByCustomer_CustomerIDOrderByBillIDDesc(customer.getCustomerID());
        Booking booking = bookingRepository.findById(bill.getBooking().getBookingID()).orElse(null);
        List<Room> bookedRooms = roomRepository.findRoomsByBookingID(booking.getBookingID());
        RoomType roomType = bookedRooms.get(0).getRoomType();
        model.addAttribute("bill", bill);
        model.addAttribute("booking", booking);
        model.addAttribute("customer", customer);
        model.addAttribute("roomType", roomType);
        model.addAttribute("roomQuantity", bookedRooms.size());
        return "customer/bill";
    }
}
