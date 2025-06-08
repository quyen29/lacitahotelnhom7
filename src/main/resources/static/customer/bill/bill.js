document.addEventListener('DOMContentLoaded', () => {
    initInvoice();
  });
  
  function initInvoice() {
    const overlay    = document.getElementById('invoiceOverlay');
    const form       = document.getElementById('invoiceForm');
    const btnCancel  = document.getElementById('invoiceCancel');
    const btnConfirm = document.getElementById('invoiceConfirm');
  
    const userInfo   = document.getElementById('invoiceUserInfo');
    const roomDet    = document.getElementById('invoiceRoomDetails');
    const timeDet    = document.getElementById('invoiceTimeDetails');
    const priceDet   = document.getElementById('invoiceTotalPrice');
    const peopleDet  = document.getElementById('invoiceTotalPeople');
    const payMethod  = document.getElementById('invoicePaymentMethod');
    const timeIss    = document.getElementById('invoiceTimeIssued');
    const idField    = document.getElementById('invoiceID');
  
    // Hàm global để bookingForm.js gọi
    window.showInvoice = function(data) {
      userInfo.textContent = `Tên: ${data.user.name} – CCCD: ${data.user.cccd}`;
      roomDet.innerHTML    =
        `Loại phòng: ${data.room.type} — Diện tích: ${data.room.size}<br>` +
        `Số lượng phòng: ${data.quantity}`;
      timeDet.textContent  = `Nhận phòng: ${data.checkIn} — Trả phòng: ${data.checkOut}`;
      priceDet.textContent = `${data.price.toLocaleString()} VND`;
      peopleDet.textContent= data.people;
      payMethod.value      = 'credit';
  
      const now = new Date();
      timeIss.textContent = now.toLocaleString();
      idField.textContent = 'HD' + now.getTime();
  
      overlay.style.display = 'block';
      form.style.display    = 'block';
    };
  
    btnCancel.addEventListener('click', () => {
      overlay.style.display = 'none';
      form.style.display    = 'none';
    });
  
    btnConfirm.addEventListener('click', () => {
      // TODO: gửi xác nhận lên server
      overlay.style.display = 'none';
      form.style.display    = 'none';
      alert('Thanh toán thành công!');
    });
  
    overlay.addEventListener('click', () => {
      overlay.style.display = 'none';
      form.style.display    = 'none';
    });
  }
  