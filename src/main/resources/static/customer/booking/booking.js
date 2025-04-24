// Giả lập trạng thái đăng nhập và thông tin người dùng
let isLoggedIn = false; // Thay đổi thành true nếu người dùng đã đăng nhập
let loggedInUser = {
  name: "Nguyễn Văn A",
  cccd: "123456789"
};

// Định nghĩa số người tối đa cho từng loại phòng
const roomMaxPersons = {
  'standard': 3,
  'deluxe': 4,
  'suite': 4,
  'family': 5
};

// Biến lưu thông tin phòng hiện tại
let currentRoomPrice = 600000;
let currentRoomType = "Standard";
let currentRoomSize = "35 m<sup>2</sup>";
let currentRoomAvailable = 10;

// Lấy các phần tử cần thiết
const bookButtons = document.querySelectorAll('.book-button');
const bookingForm = document.getElementById('bookingForm');
const overlay = document.getElementById('overlay');
const cancelButton = document.getElementById('cancelButton');
const checkInDate = document.getElementById('checkInDate');
const checkOutDate = document.getElementById('checkOutDate');
const checkInHour = document.getElementById('checkInHour');
const checkOutHour = document.getElementById('checkOutHour');
const priceDisplay = document.getElementById('priceDisplay');
const roomDetails = document.getElementById('roomDetails');
const roomQuantity = document.getElementById('roomQuantity');
const roomAvailability = document.getElementById('roomAvailability');
const totalPeople = document.getElementById('totalPeople');

// Các phần tử của form hóa đơn
const invoiceForm = document.getElementById('invoiceForm');
const invoiceUserInfo = document.getElementById('invoiceUserInfo');
const invoiceRoomDetails = document.getElementById('invoiceRoomDetails');
const invoiceTimeDetails = document.getElementById('invoiceTimeDetails');
const invoiceTotalPrice = document.getElementById('invoiceTotalPrice');
const invoiceTotalPeople = document.getElementById('invoiceTotalPeople');
const invoiceTimeIssued = document.getElementById('invoiceTimeIssued');
const invoiceIDField = document.getElementById('invoiceID');
const invoiceCancelButton = document.getElementById('invoiceCancelButton');
const invoiceConfirmButton = document.getElementById('invoiceConfirmButton');

// Phương thức thanh toán sẽ được lấy từ invoice form (select có id="paymentMethod")
const paymentMethodSelect = document.getElementById('paymentMethod');

// Form thông báo thanh toán thành công
const paymentSuccessForm = document.getElementById('paymentSuccessForm');
const successCloseButton = document.getElementById('successCloseButton');

// Đặt ngày mặc định (hôm nay và ngày mai)
const today = new Date();
const tomorrow = new Date(today);
tomorrow.setDate(tomorrow.getDate() + 1);

const formatDate = (date) => {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
};

checkInDate.value = formatDate(today);
checkOutDate.value = formatDate(tomorrow);

// Giờ nhận và trả phòng mặc định
checkInHour.value = '14';
checkOutHour.value = '12';

// Cập nhật số lượng phòng có thể đặt dựa trên số phòng trống
function updateRoomQuantityOptions(available) {
  roomQuantity.innerHTML = '';
  for (let i = 1; i <= available; i++) {
    const option = document.createElement('option');
    option.value = i;
    option.textContent = i;
    roomQuantity.appendChild(option);
  }
  roomAvailability.textContent = `Phòng trống: ${available}`;
}

// Hiển thị form đặt phòng cho từng phòng
bookButtons.forEach(button => {
  button.addEventListener('click', () => {
    currentRoomPrice = parseInt(button.getAttribute('data-price'));
    currentRoomAvailable = parseInt(button.getAttribute('data-available'));
    
    // Nếu số phòng trống bằng 0 thì không cho phép đặt
    if (currentRoomAvailable === 0) {
      alert('Không còn phòng trống');
      return;
    }
    
    // Xác định loại phòng và diện tích theo dữ liệu button
    const roomKey = button.getAttribute('data-room');
    switch(roomKey) {
      case 'standard':
        currentRoomType = "Standard";
        currentRoomSize = "35 m<sup>2</sup>";
        break;
      case 'deluxe':
        currentRoomType = "Deluxe";
        currentRoomSize = "45 m<sup>2</sup>";
        break;
      case 'suite':
        currentRoomType = "Suite";
        currentRoomSize = "60 m<sup>2</sup>";
        break;
      case 'family':
        currentRoomType = "Family";
        currentRoomSize = "55 m<sup>2</sup>";
        break;
    }
    
    // Cập nhật thuộc tính max cho trường số lượng người theo roomMaxPersons
    totalPeople.setAttribute("max", roomMaxPersons[roomKey]);
    if (parseInt(totalPeople.value) > roomMaxPersons[roomKey]) {
      totalPeople.value = roomMaxPersons[roomKey];
    }
    
    // Cập nhật thông tin phòng (innerHTML để render thẻ sup)
    roomDetails.innerHTML = `Loại phòng: ${currentRoomType} - Diện tích: ${currentRoomSize}`;
    updateRoomQuantityOptions(currentRoomAvailable);
    calculatePrice();
    
    bookingForm.style.display = 'block';
    overlay.style.display = 'block';
  });
});

// Ẩn form đặt phòng
cancelButton.addEventListener('click', () => {
  bookingForm.style.display = 'none';
  overlay.style.display = 'none';
});

// Hàm tính tổng giá
const calculatePrice = () => {
  const inDate = new Date(checkInDate.value);
  const outDate = new Date(checkOutDate.value);
  
  inDate.setHours(parseInt(checkInHour.value), 0, 0, 0);
  outDate.setHours(parseInt(checkOutHour.value), 0, 0, 0);
  
  const quantity = parseInt(roomQuantity.value);
  
  if (inDate && outDate && outDate > inDate) {
    const diffTime = Math.abs(outDate - inDate);
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    const totalPriceValue = diffDays * currentRoomPrice * quantity;
    
    priceDisplay.textContent = `Tổng giá: ${totalPriceValue.toLocaleString()} VND (${diffDays} đêm, ${quantity} phòng)`;
  } else {
    priceDisplay.textContent = 'Vui lòng chọn ngày hợp lệ';
  }
};

// Lắng nghe thay đổi ngày, giờ, số phòng để tính lại giá
checkInDate.addEventListener('change', calculatePrice);
checkOutDate.addEventListener('change', calculatePrice);
checkInHour.addEventListener('change', calculatePrice);
checkOutHour.addEventListener('change', calculatePrice);
roomQuantity.addEventListener('change', calculatePrice);

// Khi bấm thanh toán từ form đặt phòng
document.getElementById('payButton').addEventListener('click', () => {
  // Kiểm tra số lượng người không vượt quá max
  const maxPeople = parseInt(totalPeople.getAttribute("max"));
  if (parseInt(totalPeople.value) > maxPeople) {
    alert(`Số lượng người không được vượt quá ${maxPeople}`);
    return;
  }
  
  // Tính toán lại giá
  const selectedQuantity = parseInt(roomQuantity.value);
  const inDate = new Date(checkInDate.value);
  inDate.setHours(parseInt(checkInHour.value), 0, 0, 0);
  const outDate = new Date(checkOutDate.value);
  outDate.setHours(parseInt(checkOutHour.value), 0, 0, 0);
  const diffTime = Math.abs(outDate - inDate);
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
  const totalPriceValue = diffDays * currentRoomPrice * selectedQuantity;
  
  // Chuẩn bị thông tin hóa đơn
  invoiceRoomDetails.innerHTML = `Loại phòng: ${currentRoomType} - Diện tích: ${currentRoomSize} <br>Số lượng phòng: ${selectedQuantity}`;
  invoiceTimeDetails.textContent = `Nhận phòng: ${checkInDate.value} ${checkInHour.value}:00 - Trả phòng: ${checkOutDate.value} ${checkOutHour.value}:00`;
  invoiceTotalPrice.textContent = `${totalPriceValue.toLocaleString()} VND`;
  invoiceTotalPeople.textContent = totalPeople.value;
  invoiceUserInfo.textContent = `Tên: ${loggedInUser.name} - CCCD: ${loggedInUser.cccd}`;
  
  const currentTime = new Date();
  invoiceTimeIssued.textContent = currentTime.toLocaleString();
  invoiceIDField.textContent = 'HD' + currentTime.getTime();
  
  /* ===== THÊM CODE TÍCH HỢP BACKEND Ở ĐÂY =====
     Ví dụ: Gửi dữ liệu đặt phòng (thông tin phòng, thời gian, số lượng, người dùng) lên API server.
     fetch('https://example.com/api/book', {
       method: 'POST',
       headers: { 'Content-Type': 'application/json' },
       body: JSON.stringify({
         roomType: currentRoomType,
         roomSize: currentRoomSize,
         quantity: selectedQuantity,
         checkIn: checkInDate.value,
         checkOut: checkOutDate.value,
         totalPeople: totalPeople.value,
         totalPrice: totalPriceValue,
         user: loggedInUser
       })
     }).then(response => {
       // Xử lý response từ server nếu cần
     }).catch(error => {
       console.error('Lỗi khi gửi dữ liệu đặt phòng:', error);
     });
  ============================================ */
  
  // Ẩn form đặt phòng và hiển thị form hóa đơn
  bookingForm.style.display = 'none';
  invoiceForm.style.display = 'block';
});

// Xử lý nút hủy trong form hóa đơn
invoiceCancelButton.addEventListener('click', () => {
  invoiceForm.style.display = 'none';
  overlay.style.display = 'none';
});

// Xử lý nút xác nhận trong form hóa đơn
invoiceConfirmButton.addEventListener('click', () => {
  // Lấy lựa chọn phương thức thanh toán từ form
  const paymentMethod = paymentMethodSelect.options[paymentMethodSelect.selectedIndex].text;
  
  /* ===== THÊM CODE TÍCH HỢP BACKEND Ở ĐÂY =====
     Ví dụ: Gửi thông tin thanh toán đến backend để xử lý giao dịch.
     fetch('https://example.com/api/payment', {
       method: 'POST',
       headers: { 'Content-Type': 'application/json' },
       body: JSON.stringify({
         paymentMethod: paymentMethod,
         invoiceID: invoiceIDField.textContent,
         // Các thông tin cần thiết khác...
       })
     }).then(response => {
       // Xử lý response từ server nếu cần
     }).catch(error => {
       console.error('Lỗi khi xử lý thanh toán:', error);
     });
  ============================================ */
  
  // Ẩn form hóa đơn và hiển thị form thanh toán thành công
  invoiceForm.style.display = 'none';
  paymentSuccessForm.style.display = 'block';
});

// Đóng form thanh toán thành công
successCloseButton.addEventListener('click', () => {
  paymentSuccessForm.style.display = 'none';
  overlay.style.display = 'none';
});

// Đóng các form nếu click vào overlay
overlay.addEventListener('click', () => {
  bookingForm.style.display = 'none';
  invoiceForm.style.display = 'none';
  paymentSuccessForm.style.display = 'none';
  overlay.style.display = 'none';
});
