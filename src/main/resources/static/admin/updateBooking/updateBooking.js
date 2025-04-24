document.addEventListener("DOMContentLoaded", function () {
    const checkinInput = document.getElementById("checkinImage");
    const checkoutInput = document.getElementById("checkoutImage");
    const currentStatus = document.getElementById("currentStatus").value;
    const newStatusSelect = document.getElementById("newStatus");
    const resultSpan = document.querySelector(".ai-result span");
    const roomID = new URLSearchParams(window.location.search).get("roomID");
    const bookingID = new URLSearchParams(window.location.search).get("bookingID");
    let checkinFile = null;
    let checkoutFile = null;

    function isValidImageType(file) {
        const allowedTypes = ["image/jpeg", "image/png"];
        return file && allowedTypes.includes(file.type);
    }

    function previewImage(event, imageId, filenameId, type) {
        const file = event.target.files[0];
        const imageElement = document.getElementById(imageId);
        const filenameElement = document.getElementById(filenameId);
        if (file && isValidImageType(file)) {
            imageElement.src = URL.createObjectURL(file);
            imageElement.style.display = "block";
            filenameElement.textContent = file.name;
            if (type === "checkin") checkinFile = file;
            if (type === "checkout") {
                checkoutFile = file;
                triggerFaceRecognition();
            }
        } else {
            imageElement.src = "";
            imageElement.style.display = "none";
            filenameElement.textContent = "Chưa chọn ảnh";
            if (file) {
                alert("Chỉ cho phép ảnh JPG hoặc PNG.");
                event.target.value = "";
            }
        }
    }

    function triggerFaceRecognition() {
        const selectedStatus = newStatusSelect.value;
        const isCheckoutPhase = currentStatus === "Đang sử dụng" && selectedStatus === "Trống";
        if (isCheckoutPhase && checkoutFile) {
            const formData = new FormData();
            formData.append("roomID", roomID);
            formData.append("bookingID", bookingID);
            formData.append("image2", checkoutFile);
            resultSpan.textContent = "🔄 Đang nhận diện khuôn mặt...";
            fetch("http://127.0.0.1:5000/verify-use-checkin-from-db", {
                method: "POST",
                body: formData
            })
            .then(res => res.json())
            .then(data => {
                if (data.verified !== undefined) {
                    resultSpan.textContent = `✅ Kết quả: ${data.verified ? "Khớp khuôn mặt" : "Không khớp"} (${data.similarity}%)`;
                    const backendForm = new FormData();
                    backendForm.append("roomID", roomID);
                    backendForm.append("bookingID", bookingID);
                    fetch(`/admin/roomBooked/updateBooking/faceRecognize`, {
                        method: "POST",
                        body: backendForm
                    });
                } else {
                    resultSpan.textContent = `❌ Lỗi AI: ${data.error || "Không xác định"}`;
                }
            })
            .catch(() => {
                resultSpan.textContent = "❌ Không thể kết nối Flask.";
            });
        }
    }
    checkinInput.addEventListener("change", function (event) {
        previewImage(event, "checkinPreview", "checkinFilename", "checkin");
    });
    checkoutInput.addEventListener("change", function (event) {
        previewImage(event, "checkoutPreview", "checkoutFilename", "checkout");
    });

    function updateUploadFields() {
        const selectedStatus = newStatusSelect.value;
        if (currentStatus === "Đã đặt" && selectedStatus === "Đang sử dụng") {
            checkinInput.disabled = false;
            checkoutInput.disabled = true;
        } else if (currentStatus === "Đang sử dụng" && selectedStatus === "Trống") {
            checkinInput.disabled = true;
            checkoutInput.disabled = false;
        } else {
            checkinInput.disabled = true;
            checkoutInput.disabled = true;
        }
        checkinFile = null;
        checkoutFile = null;
        checkinInput.value = "";
        checkoutInput.value = "";
        resultSpan.textContent = "";
    }

    updateUploadFields();
    newStatusSelect.addEventListener("change", updateUploadFields);
});