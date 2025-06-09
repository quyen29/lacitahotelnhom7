package vos.hoteldemo.controller;

import jakarta.servlet.http.HttpSession;
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
import java.util.*;

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
    @Autowired
    private PriceRepository priceRepository;

    @GetMapping
    public String showBookingPage(Model model) {
        model.addAttribute("room", bookingService.countRoomByEmptyStatus());
        List<RoomType> roomTypes = bookingService.getAllRoomTypes();
        Map<Integer, Float> roomTypePrices = new HashMap<>();
        for (RoomType rt : roomTypes) {
            Float price = priceRepository.findDefaultPriceByRoomTypeId(rt.getRoomTypeID());
            roomTypePrices.put(rt.getRoomTypeID(), price != null ? price : 0f);
        }
        model.addAttribute("roomTypes", roomTypes);
        model.addAttribute("roomTypePrices", roomTypePrices);
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
            Map<Integer, Float> priceByAge = new HashMap<>();
            for (int ageId = 1; ageId <= 5; ageId++) {
                Float p = priceRepository.findPriceByAgeIdAndRoomTypeId(ageId, selectedRoomType.getRoomTypeID());
                priceByAge.put(ageId, p != null ? p : 0f);
            }
            model.addAttribute("priceByAge", priceByAge);
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
                                @RequestParam int numberOfAdult,
                                @RequestParam int numberOfChild,
                                @RequestParam(required = false) String paymentMethod,
                                @RequestParam float totalPrice,
                                @RequestParam(required = false) String platform,
                                HttpSession session,
                                Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Customer customer = customerRepository.findByEmail(auth.getName());
        Booking booking = (Booking) session.getAttribute("booking");
        String actualPaymentMethod = (paymentMethod != null)
                ? paymentMethod
                : (String) session.getAttribute("paymentMethod");
        if (actualPaymentMethod == null) actualPaymentMethod = "cash";
        String actualPlatform = "bank".equalsIgnoreCase(actualPaymentMethod)
                ? (platform != null ? platform : (String) session.getAttribute("platform"))
                : "";
        String paymentDisplay = "bank".equalsIgnoreCase(actualPaymentMethod) ? "Chuyển khoản" : "Tiền mặt";
        booking.setBookingDate(LocalDateTime.now());
        bookingRepository.save(booking);
        Bill bill = new Bill();
        bill.setBooking(booking);
        bill.setCustomer(customer);
        bill.setInvoiceTime(LocalDateTime.now());
        bill.setPaymentStatus(1);
        bill.setTotal(totalPrice);
        bill.setPaymentMethod(paymentDisplay);
        bill.setPlatform(actualPlatform);
        billRepository.save(bill);
        List<Room> availableRooms = roomRepository.findAvailableByRoomTypeId(roomTypeId);
        if (availableRooms.size() < roomQuantity) {
            model.addAttribute("error", "Không đủ phòng trống để đặt.");
            return "/showPage404";
        }
        List<Integer> roomIds = new ArrayList<>();
        for (int i = 0; i < roomQuantity; i++) {
            Room room = availableRooms.get(i);
            room.setStatus("Đã đặt");
            roomRepository.save(room);
            BookingRoom bookingRoom = new BookingRoom();
            bookingRoom.setId(new BookingRoomID(booking.getBookingID(), room.getRoomID()));
            bookingRoom.setBooking(booking);
            bookingRoom.setRoom(room);
            bookingRoomRepository.save(bookingRoom);
            roomIds.add(room.getRoomID());
        }
        RoomType roomType = roomTypeRepository.findById(roomTypeId).orElse(null);
        emailService.sendInvoiceEmailWithQR(
                customer.getEmail(),
                bill,
                booking,
                customer,
                roomType,
                roomQuantity,
                actualPlatform,
                paymentDisplay,
                roomIds
        );
        model.addAttribute("bill", bill);
        model.addAttribute("booking", booking);
        model.addAttribute("customer", customer);
        model.addAttribute("roomType", roomType);
        model.addAttribute("roomQuantity", roomQuantity);
        return "customer/bill";
    }

    @PostMapping("/showBookingForm/payment")
    public String pay(@RequestParam Map<String, String> params, Model model, HttpSession session) {
        try {
            int roomTypeId = Integer.parseInt(params.get("roomTypeId"));
            int roomQuantity = Integer.parseInt(params.get("roomQuantity"));
            int numberOfAdult = Integer.parseInt(params.get("numberOfAdult"));
            int numberOfChild = Integer.parseInt(params.get("numberOfChild"));
            int adult = Integer.parseInt(params.get("adult"));
            int elder = Integer.parseInt(params.get("elder"));
            int teen = Integer.parseInt(params.get("teen"));
            int child = Integer.parseInt(params.get("child"));
            int underThree = Integer.parseInt(params.get("underThree"));
            int checkInHour = Integer.parseInt(params.get("checkInHour"));
            int checkOutHour = Integer.parseInt(params.get("checkOutHour"));
            float totalPrice = Float.parseFloat(params.get("totalPrice"));
            String checkInDate = params.get("checkInDate");
            String checkOutDate = params.get("checkOutDate");
            String paymentMethod = params.getOrDefault("paymentMethod", "cash");
            String bankOption = params.get("bankOption");
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Customer customer = customerRepository.findByEmail(auth.getName());
            int customerTypeId = customer.getCustomerType() != null ? customer.getCustomerType().getCustomerTypeID() : 0;
            List<Voucher> voucherList = bookingService.getAvailableVouchers(roomTypeId, customerTypeId);
            model.addAttribute("voucherList", voucherList);
            model.addAttribute("customer", customer);
            RoomType roomType = roomTypeRepository.findById(roomTypeId).orElse(null);
            Booking booking = new Booking();
            booking.setRoomType(roomType);
            booking.setRoomQuantity(roomQuantity);
            booking.setCheckInDate(LocalDateTime.parse(checkInDate + "T" + String.format("%02d", checkInHour) + ":00"));
            booking.setCheckOutDate(LocalDateTime.parse(checkOutDate + "T" + String.format("%02d", checkOutHour) + ":00"));
            booking.setAdult(adult);
            booking.setElder(elder);
            booking.setTeen(teen);
            booking.setChild(child);
            booking.setUnderThree(underThree);
            booking.setNumberOfAdult(numberOfAdult);
            booking.setNumberOfChild(numberOfChild);
            booking.setTotalPrice(totalPrice);
            model.addAttribute("booking", booking);
            session.setAttribute("booking", booking);
            session.setAttribute("paymentMethod", paymentMethod);
            session.setAttribute("bankOption", bankOption);
            return "customer/payment";
        } catch (Exception e) {
            model.addAttribute("error", "Thiếu thông tin đặt phòng. Vui lòng thử lại.");
            return "/showPage404";
        }
    }

    @PostMapping("/showBookingForm/qrpay")
    public String handleQRPay(
            @RequestParam(name = "paymentMethod", required = false, defaultValue = "cash") String paymentMethod,
            @RequestParam(name = "platform", required = false) String platform,
            HttpSession session,
            Model model) {
        Booking booking = (Booking) session.getAttribute("booking");
        session.setAttribute("paymentMethod", paymentMethod);
        session.setAttribute("platform", platform);
        if (booking == null) {
            model.addAttribute("error", "Dữ liệu đặt phòng bị thiếu. Vui lòng thử lại.");
            return "customer/qrpay";
        }
        model.addAttribute("booking", booking);
        model.addAttribute("paymentMethod", paymentMethod);
        model.addAttribute("platform", platform);
        Map<String, String> bankNames = Map.of(
                "momo", "Momo",
                "vnpay", "VNPAY",
                "vietcombank", "Vietcombank",
                "mbbank", "MB Bank",
                "techcombank", "Techcombank",
                "bidv", "BIDV"
        );
        String displayName = bankNames.getOrDefault(platform != null ? platform.toLowerCase() : "", "Chuyển khoản");
        model.addAttribute("platform", displayName);
        model.addAttribute("qrImagePath", "/images/qr-" + (platform != null ? platform : "default") + ".png");
        return "customer/qrpay";
    }

    @GetMapping("/showBookingForm/showBill")
    public String showBill(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Customer customer = customerRepository.findByEmail(auth.getName());
        if (customer == null) return "redirect:/login";
        Bill bill = billRepository.findTopByCustomer_CustomerIDOrderByBillIDDesc(customer.getCustomerID());
        if (bill == null || bill.getBooking() == null) return "redirect:/booking";
        Booking booking = bill.getBooking();
        int bookingID = booking.getBookingID();
        List<Integer> roomIDs = bookingRoomRepository.findRoomIdsByBookingID(bookingID);
        RoomType roomType = bookingRoomRepository.findByBooking_BookingID(bookingID).stream()
                .map(BookingRoom::getRoom)
                .filter(r -> r != null && r.getRoomType() != null)
                .map(Room::getRoomType)
                .findFirst()
                .orElse(null);
        model.addAttribute("bill", bill);
        model.addAttribute("booking", booking);
        model.addAttribute("customer", customer);
        model.addAttribute("roomType", roomType);
        model.addAttribute("roomQuantity", roomIDs.size());
        model.addAttribute("roomIDs", roomIDs);
        model.addAttribute("paymentMethod", bill.getPaymentMethod());
        if (!"Tiền mặt".equalsIgnoreCase(bill.getPaymentMethod())) {
            model.addAttribute("platformName", bill.getPlatform());
        }
        return "customer/bill";
    }
}