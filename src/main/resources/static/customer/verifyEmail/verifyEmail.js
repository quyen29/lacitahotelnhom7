function resendCode() {
    fetch('/resend-code', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': document.querySelector('input[name="_csrf"]').value
        }
    })
    .then(response => {
        if (response.ok) {
            alert("Mã xác thực đã được gửi lại email.");
        } else {
            alert("Không thể gửi lại mã. Vui lòng thử lại sau.");
        }
    })
    .catch(error => {
        console.error("Lỗi khi gửi lại mã:", error);
        alert("Đã xảy ra lỗi khi gửi lại mã.");
    });
}
