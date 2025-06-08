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
const paymentMethodSelect = document.getElementById('paymentMethod');
const paymentSuccessForm = document.getElementById('paymentSuccessForm');
const successCloseButton = document.getElementById('successCloseButton');

// Đặt ngày mặc định
const today = new Date();
const tomorrow = new Date(today);
tomorrow.setDate(tomorrow.getDate() + 1);

const formatDate = (date) => {
  return date.toISOString().split('T')[0];
};

checkInDate.value = formatDate(today);
checkOutDate.value = formatDate(tomorrow);
checkInHour.value = '14';
checkOutHour.value = '12';

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

bookButtons.forEach(button => {
  button.addEventListener('click', () => {
    currentRoomPrice = parseInt(button.getAttribute('data-price'));
    currentRoomAvailable = parseInt(button.getAttribute('data-available'));
    const roomKey = button.getAttribute('data-room');

    switch(roomKey) {
      case 'standard': currentRoomType = "Standard"; currentRoomSize = "35 m<sup>2</sup>"; break;
      case 'deluxe': currentRoomType = "Deluxe"; currentRoomSize = "45 m<sup>2</sup>"; break;
      case 'suite': currentRoomType = "Suite"; currentRoomSize = "60 m<sup>2</sup>"; break;
      case 'family': currentRoomType = "Family"; currentRoomSize = "55 m<sup>2</sup>"; break;
    }

    totalPeople.setAttribute("max", roomMaxPersons[roomKey]);
    if (parseInt(totalPeople.value) > roomMaxPersons[roomKey]) {
      totalPeople.value = roomMaxPersons[roomKey];
    }

    roomDetails.innerHTML = `Loại phòng: ${currentRoomType} - Diện tích: ${currentRoomSize}`;
    updateRoomQuantityOptions(currentRoomAvailable);
    calculatePrice();

    bookingForm.style.display = 'block';
    overlay.style.display = 'block';
  });
});

cancelButton.addEventListener('click', () => {
  bookingForm.style.display = 'none';
  overlay.style.display = 'none';
});

const calculatePrice = () => {
  const inDate = new Date(checkInDate.value);
  const outDate = new Date(checkOutDate.value);
  inDate.setHours(parseInt(checkInHour.value), 0, 0, 0);
  outDate.setHours(parseInt(checkOutHour.value), 0, 0, 0);
  const quantity = parseInt(roomQuantity.value);

  if (outDate > inDate) {
    const diffDays = Math.ceil((outDate - inDate) / (1000 * 60 * 60 * 24));
    const totalPrice = diffDays * currentRoomPrice * quantity;
    priceDisplay.textContent = `Tổng giá: ${totalPrice.toLocaleString()} VND (${diffDays} đêm, ${quantity} phòng)`;
  } else {
    priceDisplay.textContent = 'Vui lòng chọn ngày hợp lệ';
  }
};

[checkInDate, checkOutDate, checkInHour, checkOutHour, roomQuantity].forEach(el =>
  el.addEventListener('change', calculatePrice)
);

document.getElementById('payButton').addEventListener('click', () => {
  const maxPeople = parseInt(totalPeople.getAttribute("max"));
  if (parseInt(totalPeople.value) > maxPeople) {
    alert(`Số lượng người không được vượt quá ${maxPeople}`);
    return;
  }

  const quantity = parseInt(roomQuantity.value);
  const inDate = new Date(checkInDate.value);
  const outDate = new Date(checkOutDate.value);
  inDate.setHours(parseInt(checkInHour.value));
  outDate.setHours(parseInt(checkOutHour.value));
  const diffDays = Math.ceil((outDate - inDate) / (1000 * 60 * 60 * 24));
  const totalPrice = diffDays * currentRoomPrice * quantity;

  invoiceRoomDetails.innerHTML = `Loại phòng: ${currentRoomType} - Diện tích: ${currentRoomSize} <br>Số lượng phòng: ${quantity}`;
  invoiceTimeDetails.textContent = `Nhận phòng: ${checkInDate.value} ${checkInHour.value}:00 - Trả phòng: ${checkOutDate.value} ${checkOutHour.value}:00`;
  invoiceTotalPrice.textContent = `${totalPrice.toLocaleString()} VND`;
  invoiceTotalPeople.textContent = totalPeople.value;
  invoiceUserInfo.textContent = `Thông tin khách sẽ được cập nhật sau`;

  const currentTime = new Date();
  invoiceTimeIssued.textContent = currentTime.toLocaleString();
  invoiceIDField.textContent = 'HD' + currentTime.getTime();

  bookingForm.style.display = 'none';
  invoiceForm.style.display = 'block';
});

invoiceCancelButton.addEventListener('click', () => {
  invoiceForm.style.display = 'none';
  overlay.style.display = 'none';
});

invoiceConfirmButton.addEventListener('click', () => {
  invoiceForm.style.display = 'none';
  paymentSuccessForm.style.display = 'block';
});

successCloseButton.addEventListener('click', () => {
  paymentSuccessForm.style.display = 'none';
  overlay.style.display = 'none';
});

overlay.addEventListener('click', () => {
  bookingForm.style.display = 'none';
  invoiceForm.style.display = 'none';
  paymentSuccessForm.style.display = 'none';
  overlay.style.display = 'none';
});