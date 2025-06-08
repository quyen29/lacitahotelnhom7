document.addEventListener("DOMContentLoaded", function () {
  const storedUser = localStorage.getItem("registeredUser");
  const isLoggedIn = localStorage.getItem("isLoggedIn") === "true";
  const userData = (storedUser && isLoggedIn) ? JSON.parse(storedUser) : {
    fullname: "",
    phone: "",
    cccd: "",
    dob: "",
    email: "",
    gender: ""
  };

  document.getElementById("fullname").value = userData.fullname;
  document.getElementById("phone").value = userData.phone;
  document.getElementById("id").value = userData.cccd;
  document.getElementById("dob").value = userData.dob;
  document.getElementById("email").value = userData.email;
  document.getElementById("gender").value = userData.gender;

  populateBookingHistory();

  const editBtn = document.getElementById("edit-btn");
  const saveBtn = document.getElementById("save-btn");
  const cancelBtn = document.getElementById("cancel-btn");
  const formInputs = document.querySelectorAll("#profile-form .form-control");

  editBtn.addEventListener("click", () => {
    formInputs.forEach(input => input.disabled = false);
    editBtn.style.display = "none";
    saveBtn.style.display = "inline-block";
    cancelBtn.style.display = "inline-block";
  });

  saveBtn.addEventListener("click", () => {
    userData.fullname = document.getElementById("fullname").value;
    userData.phone = document.getElementById("phone").value;
    userData.cccd = document.getElementById("id").value;
    userData.dob = document.getElementById("dob").value;
    userData.email = document.getElementById("email").value;
    userData.gender = document.getElementById("gender").value;

    if (isLoggedIn) {
      localStorage.setItem("registeredUser", JSON.stringify(userData));
    }

    formInputs.forEach(input => input.disabled = true);
    editBtn.style.display = "inline-block";
    saveBtn.style.display = "none";
    cancelBtn.style.display = "none";
    alert("Thông tin cá nhân đã được cập nhật!");
  });

  cancelBtn.addEventListener("click", () => {
    document.getElementById("fullname").value = userData.fullname;
    document.getElementById("phone").value = userData.phone;
    document.getElementById("id").value = userData.cccd;
    document.getElementById("dob").value = userData.dob;
    document.getElementById("email").value = userData.email;
    document.getElementById("gender").value = userData.gender;
    formInputs.forEach(input => input.disabled = true);
    editBtn.style.display = "inline-block";
    saveBtn.style.display = "none";
    cancelBtn.style.display = "none";
  });
});

let bookingHistory = [];

function populateBookingHistory() {
  const bookingList = document.getElementById("booking-list");
  bookingList.innerHTML = "";

  if (bookingHistory.length === 0) {
    bookingList.innerHTML = `<tr><td colspan="9" style="text-align:center;">Chưa có lịch sử đặt phòng</td></tr>`;
    return;
  }

  bookingHistory.forEach((booking, index) => {
    const totalPeople = booking.adults + booking.children;
    const row = document.createElement("tr");
    row.innerHTML = `
      <td>${booking.roomType}</td>
      <td>${booking.area}</td>
      <td>${booking.invoice}</td>
      <td>${booking.status}</td>
      <td>${booking.checkInDate}</td>
      <td>${booking.checkOutDate}</td>
      <td>${totalPeople}</td>
      <td>${booking.price}</td>
      <td>
        ${booking.feedback
          ? `<button class="btn" onclick="viewFeedback(${index})">Xem phản hồi</button>`
          : `<button class="btn" onclick="openFeedbackModal(${index})">Gửi phản hồi</button>`}
      </td>`;
    bookingList.appendChild(row);
  });
}

let currentBookingIndex = null;

function openFeedbackModal(index) {
  currentBookingIndex = index;
  document.getElementById("feedbackOverlay").style.display = "block";
  document.getElementById("feedbackModal").style.display = "block";
  document.getElementById("feedbackText").value = "";
  document.getElementById("feedbackText").focus();
}

function closeFeedbackModal() {
  document.getElementById("feedbackOverlay").style.display = "none";
  document.getElementById("feedbackModal").style.display = "none";
  currentBookingIndex = null;
}

function submitFeedback() {
  const feedback = document.getElementById("feedbackText").value.trim();
  if (!feedback) {
    alert("Vui lòng nhập phản hồi!");
    return;
  }
  if (currentBookingIndex !== null) {
    bookingHistory[currentBookingIndex].feedback = feedback;
    alert("Phản hồi đã được gửi!");
    closeFeedbackModal();
    populateBookingHistory();
  }
}

function viewFeedback(index) {
  if (bookingHistory[index].feedback) {
    alert("Phản hồi: " + bookingHistory[index].feedback);
  }
}

document.getElementById("feedbackOverlay").addEventListener("click", closeFeedbackModal);
