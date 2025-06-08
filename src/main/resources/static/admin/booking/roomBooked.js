document.addEventListener("DOMContentLoaded", function () {
  const qrBtn = document.getElementById("qrBtn");
  const qrReaderContainer = document.getElementById("qr-reader-container");
  const roomIdInput = document.getElementById("roomId");
  const searchForm = document.getElementById("searchForm");
  const qrCloseBtn = document.getElementById("qr-close-btn");
  let html5QrCode;

  function extractRoomIdFromQR(text) {
    const cleanText = text.normalize("NFD").replace(/[\u0300-\u036f]/g, "");
    const lines = cleanText.split("\n");
    for (let line of lines) {
      if (line.toLowerCase().includes("id phong") || line.toLowerCase().includes("room id")) {
        const match = line.match(/\d+/);
        if (match) {
          return match[0];
        }
      }
    }
    return null;
  }

  qrBtn.addEventListener("click", () => {
    qrReaderContainer.style.display = "flex";
    if (!html5QrCode) {
      html5QrCode = new Html5Qrcode("qr-reader");
    }
    html5QrCode
      .start(
        { facingMode: "environment" },
        { fps: 10, qrbox: 250 },
        (decodedText) => {
          console.log("QR Detected:", decodedText);
          const roomId = extractRoomIdFromQR(decodedText);
          if (roomId) {
            roomIdInput.value = roomId;
            html5QrCode.stop().then(() => {
              qrReaderContainer.style.display = "none";
              searchForm.submit();
            });
          } else {
            alert("Không tìm thấy ID phòng trong mã QR!");
          }
        },
        (errorMsg) => {
          console.warn("QR scan error:", errorMsg);
        }
      )
      .catch((err) => {
        console.error("Unable to start QR scanner", err);
      });
  });

  qrCloseBtn.addEventListener("click", () => {
    if (html5QrCode) {
      html5QrCode
        .stop()
        .then(() => {
          qrReaderContainer.style.display = "none";
        })
        .catch((err) => {
          console.error("Stop failed", err);
        });
    }
  });
});
