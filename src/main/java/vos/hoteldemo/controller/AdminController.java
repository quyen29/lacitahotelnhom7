package vos.hoteldemo.controller;

import jakarta.validation.Valid;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vos.hoteldemo.entity.*;
import vos.hoteldemo.service.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final RoomService roomService;
    private final RoomTypeService roomTypeService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private BillService billService;

    @Autowired
    private BookingRoomService bookingRoomService;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private FaceRecognitionService faceRecognitionService;

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

    @GetMapping("/room")
    public String showAllRoom(@RequestParam(required = false) String roomTypeName,
                              @RequestParam(required = false) String status,
                              @RequestParam(defaultValue = "0") int page,
                              Model model) {
        int size = 10;
        Page<Room> roomPage = roomService.filterRoomsPaged(roomTypeName, status, page, size);
        List<RoomType> roomTypes = roomTypeService.getAllRoomType();
        model.addAttribute("room", roomPage.getContent());
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
                               BindingResult result) {
        if (result.hasErrors()) {
            return "admin/addRoomType";
        }
        roomTypeService.save(roomType);
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

    @GetMapping("/feedback")
    public String showFeedback(Model model) {
        List<Feedback> feedbackList = feedbackService.getAllFeedback();
        model.addAttribute("feedbackList", feedbackList);
        return "admin/feedback";
    }
}