package vos.hoteldemo.controller;

import jakarta.validation.Valid;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vos.hoteldemo.dao.*;
import vos.hoteldemo.entity.*;
import vos.hoteldemo.service.*;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final RoomService roomService;
    private final RoomTypeService roomTypeService;

    @Autowired
    private final AdminService adminService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerTypeRepository customerTypeRepository;

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private final BillService billService;

    @Autowired
    private final BookingRoomService bookingRoomService;

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private VoucherCustomerRoomRepository voucherCustomerRoomRepository;

    @Autowired
    private LostItemService lostItemService;

    @Autowired
    private final FeedbackService feedbackService;

    @Autowired
    private final FaceRecognitionService faceRecognitionService;

    public AdminController(RoomService roomService, RoomTypeService roomTypeService, AdminService adminService, BillService billService, BookingRoomService bookingRoomService, FeedbackService feedbackService, FaceRecognitionService faceRecognitionService) {
        this.roomService = roomService;
        this.roomTypeService = roomTypeService;
        this.adminService = adminService;
        this.billService = billService;
        this.bookingRoomService = bookingRoomService;
        this.feedbackService = feedbackService;
        this.faceRecognitionService = faceRecognitionService;
    }

    @GetMapping()
    public String showLoginAdminPage() {
        return "admin/loginAdmin";
    }

    @GetMapping("/infoAdmin")
    public String showInfoAdmin(Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Admin admin = adminService.findByEmail(email);
        model.addAttribute("admin", admin);
        return "admin/infoAdmin";
    }

    @GetMapping("/home")
    public String showAdminHomePage(Model model) {
        model.addAttribute("totalRooms", roomService.countAllRooms());
        model.addAttribute("availableRooms", roomService.countByStatus("Trống"));
        model.addAttribute("bookedAndInUseRooms", roomService.countByStatuses(List.of("Đã đặt", "Đang sử dụng")));
        model.addAttribute("currentGuests", bookingRoomService.countTotalPeopleInHotel());
        model.addAttribute("monthlyRevenue", billService.getMonthlyRevenue(LocalDate.now()));
        Map<String, Float> revenueMap = billService.getRevenueLast13Months();
        model.addAttribute("chartLabels", revenueMap.keySet());
        model.addAttribute("chartData", revenueMap.values());
        return "admin/homeAdmin";
    }

    @GetMapping("/customerManagement")
    public String manageCustomer(Model model) {
        model.addAttribute("customers", customerService.getCustomerSummaryData());
        return "admin/customerManagement";
    }

    @GetMapping("/customerManagement/edit/{id}")
    public String editCustomer(@PathVariable("id") int id, Model model) {
        Customer customer = customerService.findById(id);
        model.addAttribute("customer", customer);
        List<CustomerType> customerTypeList = customerTypeRepository.findAll();
        model.addAttribute("customerTypeList", customerTypeList);

        return "admin/customerEdit";
    }

    @PostMapping("/customerManagement/update")
    public String updateCustomer(@RequestParam("customerID") int customerID,
                                 @RequestParam("customerTypeID") int customerTypeID) {

        Customer customer = customerService.findById(customerID);
        CustomerType customerType = customerTypeRepository.findById(customerTypeID).orElse(null);
        if (customer != null && customerType != null) {
            customer.setCustomerType(customerType);
            customerService.save(customer);
        }
        return "redirect:/admin/customerManagement";
    }

    @GetMapping("/room")
    public String showAllRoom(@RequestParam(required = false) String roomTypeName,
                              @RequestParam(required = false) String status,
                              @RequestParam(defaultValue = "0") int page,
                              Model model) {
        int size = 5;
        Page<Room> roomPage = roomService.filterRoomsPaged(roomTypeName, status, page, size);
        List<Room> rooms = roomPage.getContent();
        for (Room r : rooms) {
            RoomType rt = r.getRoomType();
            Float adultPrice = priceRepository.findPriceByRoomTypeIdAndAgeId(rt.getRoomTypeID(), 4);
            rt.setPrice(adultPrice);
        }
        List<RoomType> roomTypes = roomTypeService.getAllRoomType();
        model.addAttribute("room", rooms);
        model.addAttribute("roomTypes", roomTypes);
        model.addAttribute("selectedRoomType", roomTypeName);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", roomPage.getTotalPages());
        return "admin/room";
    }

    @GetMapping("/findRoom")
    public String showFilteredRoom(
            @RequestParam String roomTypeName,
            @RequestParam String status,
            Model model) {
        List<Room> room = roomService.filterRooms(roomTypeName, status);
        List<RoomType> roomTypes = roomTypeService.getAllRoomType();
        model.addAttribute("room", room);
        model.addAttribute("roomTypes", roomTypes);
        model.addAttribute("selectedRoomType", roomTypeName);
        model.addAttribute("selectedStatus", status);
        return "admin/findRoom";
    }

    @GetMapping("/room/addRoom")
    public String add(Model model) {
        Room room = new Room();
        room.setRoomType(new RoomType());
        List<RoomType> roomType = roomTypeService.getAllRoomType();
        model.addAttribute("room", room);
        model.addAttribute("roomType", roomType);
        return "admin/addRoom";
    }

    @GetMapping("/room/updateRoom")
    public String updateRoom(@RequestParam("roomID") Integer roomID,
                             Model model) {
        Room room = roomService.getRoomByID(roomID);
        String currentStatus = room.getStatus();
        List<String> allowedStatuses = List.of("Trống", "Bảo trì");
        List<String> otherStatuses = allowedStatuses.stream()
                .filter(status -> !status.equals(currentStatus))
                .toList();
        model.addAttribute("roomID", roomID);
        model.addAttribute("currentStatus", currentStatus);
        model.addAttribute("otherStatuses", otherStatuses);
        return "admin/updateRoom";
    }

    @PostMapping("/room/updateRoom/save")
    public String saveUpdatedRoom(@RequestParam("roomID") Integer roomID,
                                  @RequestParam("newStatus") String newStatus) {
        Room room = roomService.getRoomByID(roomID);
        room.setStatus(newStatus);
        roomService.save(room);
        return "redirect:/admin/room";
    }

    @GetMapping("/room/addRoomType")
    public String showAddRoomTypeForm(Model model) {
        model.addAttribute("roomType", new RoomType());
        return "admin/addRoomType";
    }

    @PostMapping("/room/addRoomType")
    public String saveRoomType(@ModelAttribute("roomType") @Valid RoomType roomType,
                               BindingResult result,
                               @RequestParam float price1,
                               @RequestParam float price2,
                               @RequestParam float price3,
                               @RequestParam float price4,
                               @RequestParam float price5) {
        if (result.hasErrors()) {
            return "admin/addRoomType";
        }
        roomTypeService.save(roomType);
        priceRepository.save(new Price(0, 1, roomType.getRoomTypeID(), price1));
        priceRepository.save(new Price(0, 2, roomType.getRoomTypeID(), price2));
        priceRepository.save(new Price(0, 3, roomType.getRoomTypeID(), price3));
        priceRepository.save(new Price(0, 4, roomType.getRoomTypeID(), price4));
        priceRepository.save(new Price(0, 5, roomType.getRoomTypeID(), price5));
        return "redirect:/admin/room";
    }

    @PostMapping("/room/save")
    public String save(@ModelAttribute("room") @Valid Room room, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/addRoom";
        }
        roomService.save(room);
        return "redirect:/admin/room";
    }

    @GetMapping("/roomBooked")
    public String showAllBookedRoom(
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) String roomId,
            Model model) {
        List<BookingRoom> all = bookingRoomService.getActiveRoomBooking();
        List<BookingRoom> filtered = all.stream().filter(br -> {
                    boolean matchCustomer = true;
                    boolean matchRoomId = true;
                    if (customerName != null && !customerName.isBlank()) {
                        String fullName = br.getBooking().getBill().getCustomer().getFullName().toLowerCase();
                        matchCustomer = fullName.contains(customerName.toLowerCase());
                    }
                    if (roomId != null && !roomId.isBlank()) {
                        matchRoomId = String.valueOf(br.getRoom().getRoomID()).contains(roomId);
                    }
                    return matchCustomer && matchRoomId;
                }).sorted((a, b) -> Integer.compare(b.getBooking().getBookingID(), a.getBooking().getBookingID()))
                .toList();
        model.addAttribute("bookingRooms", filtered);
        return "admin/roomBooked";
    }

    @GetMapping("/roomBooked/updateBooking")
    public String updateBooking(@RequestParam("roomID") Integer roomID,
                                @RequestParam("bookingID") Integer bookingID,
                                Model model) throws IOException {
        Room room = roomService.getRoomByID(roomID);
        BookingRoom bookingRoom = bookingRoomService.getByRoomAndBooking(roomID, bookingID);
        Booking booking = bookingRoom.getBooking();
        String checkinImageBase64 = "", checkoutImageBase64 = "", resultText = "";
        try {
            if (booking.getCheckinImage() != null && booking.getCheckoutImage() != null) {
                var checkin = booking.getCheckinImage().getBinaryStream();
                var checkout = booking.getCheckoutImage().getBinaryStream();
                System.out.println("\uD83D\uDCF1 Đang gửi ảnh đến AI server...");
                JSONObject result = faceRecognitionService.compareFaces(checkin, checkout);
                System.out.println("\uD83D\uDCE5 Kết quả từ AI server: " + result.toString());
                resultText = "Xác thực: " + (result.getBoolean("verified") ? "Đúng người" : "Không khớp")
                        + " (" + result.getDouble("similarity") + "%)";
            }
            if (booking.getCheckinImage() != null) {
                checkinImageBase64 = Base64.getEncoder().encodeToString(
                        booking.getCheckinImage().getBytes(1L, (int) booking.getCheckinImage().length()));
            }
            if (booking.getCheckoutImage() != null) {
                checkoutImageBase64 = Base64.getEncoder().encodeToString(
                        booking.getCheckoutImage().getBytes(1L, (int) booking.getCheckoutImage().length()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultText = "Không thể so sánh ảnh: " + e.getMessage();
        }
        List<String> otherStatuses = List.of("Trống", "Đã đặt", "Đang sử dụng", "Bảo trì")
                .stream().filter(s -> !s.equals(room.getStatus())).toList();
        model.addAttribute("bookingID", bookingID);
        model.addAttribute("roomID", roomID);
        model.addAttribute("currentStatus", room.getStatus());
        model.addAttribute("otherStatuses", otherStatuses);
        model.addAttribute("checkinImageBase64", checkinImageBase64);
        model.addAttribute("checkoutImageBase64", checkoutImageBase64);
        model.addAttribute("aiResult", resultText);
        return "admin/updateBooking";
    }

    @PostMapping(value = "/roomBooked/updateBooking/faceRecognize", produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> updateBookingAndFaceRecognize(
            @RequestParam("roomID") Integer roomID,
            @RequestParam("bookingID") Integer bookingID) {
        try {
            Room room = roomService.getRoomByID(roomID);
            BookingRoom bookingRoom = bookingRoomService.getByRoomAndBooking(roomID, bookingID);
            Booking booking = bookingRoom.getBooking();
            if (booking.getCheckinImage() != null && booking.getCheckoutImage() != null) {
                var checkin = booking.getCheckinImage().getBinaryStream();
                var checkout = booking.getCheckoutImage().getBinaryStream();
                JSONObject result = faceRecognitionService.compareFaces(checkin, checkout);
                return ResponseEntity.ok(result.toString());
            } else {
                return ResponseEntity.badRequest().body("{\"error\": \"Thiếu ảnh check-in hoặc check-out\"}");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/roomBooked/updateBooking/save")
    public String saveUpdatedRoomStatus(@RequestParam("roomID") Integer roomID,
                                        @RequestParam("bookingID") Integer bookingID,
                                        @RequestParam("newStatus") String newStatus,
                                        @RequestParam(value = "checkinImage", required = false) MultipartFile checkinImage,
                                        @RequestParam(value = "checkoutImage", required = false) MultipartFile checkoutImage) throws IOException {
        Room room = roomService.getRoomByID(roomID);
        BookingRoom bookingRoom = bookingRoomService.getByRoomAndBooking(roomID, bookingID);
        Booking booking = bookingRoom.getBooking();
        if (room.getStatus().equals("Đã đặt") && newStatus.equals("Đang sử dụng")) {
            if (checkinImage != null && !checkinImage.isEmpty()) {
                try {
                    booking.setCheckinImage(new javax.sql.rowset.serial.SerialBlob(checkinImage.getBytes()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (room.getStatus().equals("Đang sử dụng") && newStatus.equals("Trống")) {
            if (checkoutImage != null && !checkoutImage.isEmpty()) {
                try {
                    booking.setCheckoutImage(new javax.sql.rowset.serial.SerialBlob(checkoutImage.getBytes()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        room.setStatus(newStatus);
        roomService.save(room);
        bookingRoomService.saveBooking(booking);
        return "redirect:/admin/roomBooked";
    }

    @GetMapping("/voucher")
    public String showVoucher(Model model) {
        List<Voucher> vouchers = voucherRepository.findAll();
        List<RoomType> roomTypes = roomTypeRepository.findAll();
        List<CustomerType> customerTypes = customerTypeRepository.findAll();
        List<Map<String, Object>> displayVouchers = new ArrayList<>();
        for (Voucher v : vouchers) {
            for (VoucherCustomerRoom vcr : v.getVoucherCustomerRooms()) {
                RoomType room = vcr.getRoomType();
                CustomerType customer = vcr.getCustomerType();
                if (room == null || customer == null) continue;
                Map<String, Object> map = new HashMap<>();
                map.put("id", v.getVoucherID());
                map.put("code", v.getCode());
                map.put("discountPercent", v.getDiscountPercent());
                map.put("startDate", v.getStartDate());
                map.put("endDate", v.getEndDate());
                map.put("roomType", room.getRoomTypeName());
                map.put("customerType", customer.getCustomerTypeName());
                displayVouchers.add(map);
            }
        }
        model.addAttribute("vouchers", displayVouchers);
        model.addAttribute("roomTypes", roomTypes);
        model.addAttribute("customerTypes", customerTypes);
        return "admin/voucher";
    }

    @GetMapping("/voucher/add")
    public String showAddVoucherForm(Model model) {
        model.addAttribute("roomTypeList", roomTypeRepository.findAll());
        model.addAttribute("customerTypeList", customerTypeRepository.findAll());
        return "admin/addVoucher";
    }

    @PostMapping("/voucher/add")
    public String addVoucher(@RequestParam String code,
                             @RequestParam int discountPercent,
                             @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                             @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
                             @RequestParam int roomTypeId,
                             @RequestParam int customerTypeId) {
        Voucher voucher = new Voucher();
        voucher.setCode(code);
        voucher.setDiscountPercent(discountPercent);
        voucher.setStartDate(startDate);
        voucher.setEndDate(endDate);
        voucher = voucherRepository.save(voucher);
        VoucherCustomerRoom vcr = new VoucherCustomerRoom();
        vcr.setVoucher(voucher);
        vcr.setRoomType(roomTypeRepository.findById(roomTypeId).orElse(null));
        vcr.setCustomerType(customerTypeRepository.findById(customerTypeId).orElse(null));
        voucherCustomerRoomRepository.save(vcr);
        return "redirect:/admin/voucher";
    }

    @GetMapping("/voucher/edit/{id}")
    public String editVoucher(@PathVariable int id, Model model) {
        Voucher voucher = voucherRepository.findById(id).orElseThrow();
        VoucherCustomerRoom vcr = voucher.getVoucherCustomerRooms().isEmpty()
                ? null : voucher.getVoucherCustomerRooms().get(0);
        int roomTypeId = (vcr != null && vcr.getRoomType() != null) ? vcr.getRoomType().getRoomTypeID() : -1;
        int customerTypeId = (vcr != null && vcr.getCustomerType() != null) ? vcr.getCustomerType().getCustomerTypeID() : -1;
        model.addAttribute("voucher", voucher);
        model.addAttribute("roomTypeList", roomTypeRepository.findAll());
        model.addAttribute("customerTypeList", customerTypeRepository.findAll());
        model.addAttribute("roomTypeId", roomTypeId);
        model.addAttribute("customerTypeId", customerTypeId);
        return "admin/editVoucher";
    }

    @PostMapping("/voucher/update")
    public String updateVoucher(@RequestParam int id,
                                @RequestParam String code,
                                @RequestParam int discountPercent,
                                @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
                                @RequestParam int roomTypeId,
                                @RequestParam int customerTypeId) {
        Voucher voucher = voucherRepository.findById(id).orElseThrow();
        voucher.setCode(code);
        voucher.setDiscountPercent(discountPercent);
        voucher.setStartDate(startDate);
        voucher.setEndDate(endDate);
        voucherRepository.save(voucher);
        List<VoucherCustomerRoom> vcrList = voucher.getVoucherCustomerRooms();
        VoucherCustomerRoom vcr = vcrList.isEmpty() ? new VoucherCustomerRoom() : vcrList.get(0);
        vcr.setVoucher(voucher);
        vcr.setRoomType(roomTypeRepository.findById(roomTypeId).orElse(null));
        vcr.setCustomerType(customerTypeRepository.findById(customerTypeId).orElse(null));
        voucherCustomerRoomRepository.save(vcr);
        return "redirect:/admin/voucher";
    }

    @GetMapping("/lostItem")
    public String showLostItem(Model model) {
        List<LostItem> lostItems = lostItemService.findAll();
        model.addAttribute("lostItems", lostItems);
        return "admin/lostItem";
    }

    @GetMapping("/lostItem/addLostItem")
    public String addLostItem(Model model) {
        model.addAttribute("lostItem", new LostItem());
        return "admin/addLostItem";
    }

    @PostMapping("/lostItem/save")
    public String saveLostItem(@ModelAttribute LostItem lostItem,
                               @RequestParam("imageFile") MultipartFile imageFile) {
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                Blob blob = new SerialBlob(imageFile.getBytes());
                lostItem.setImage(blob);
            } else {
                LostItem old = lostItemService.findById(lostItem.getItemID());
                if (old != null) {
                    lostItem.setImage(old.getImage());
                }
            }
            lostItemService.save(lostItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/admin/lostItem";
    }

    @GetMapping("/lostItem/image/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> showImage(@PathVariable("id") int id) throws SQLException {
        LostItem item = lostItemService.findById(id);
        if (item != null && item.getImage() != null) {
            byte[] imageBytes = item.getImage().getBytes(1, (int) item.getImage().length());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/lostItem/edit/{id}")
    public String editLostItem(@PathVariable("id") int id, Model model) {
        LostItem lostItem = lostItemService.findById(id);
        model.addAttribute("lostItem", lostItem);
        return "admin/addLostItem";
    }

    @GetMapping("/feedback")
    public String showFeedback(Model model) {
        List<Feedback> feedbackList = feedbackService.getAllFeedback();
        model.addAttribute("feedbackList", feedbackList);
        return "admin/feedback";
    }
}