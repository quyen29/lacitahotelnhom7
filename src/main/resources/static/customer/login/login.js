// Cập nhật menu người dùng (không dùng localStorage)
function updateUserMenu() {
    const userOptions = document.getElementById("userOptions");
    userOptions.innerHTML = `
        <a type="submit" th:href="@{/showLoginPage}">Đăng nhập</a>
        <a type="submit" th:href="@{/register/showRegisterPage}">Đăng ký</a>
    `;
  }
  document.addEventListener("DOMContentLoaded", updateUserMenu);
  
  function login(e) {
    e.preventDefault();
    const loginEmail = document.getElementById("loginEmail").value.trim();
    const loginPassword = document.getElementById("loginPassword").value.trim();
    let isValid = true;
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!loginEmail || !emailRegex.test(loginEmail)) {
      document.getElementById("loginEmailError").style.display = "block";
      isValid = false;
    } else {
      document.getElementById("loginEmailError").style.display = "none";
    }
    if (!loginPassword) {
      document.getElementById("loginPasswordError").style.display = "block";
      isValid = false;
    } else {
      document.getElementById("loginPasswordError").style.display = "none";
    }
    if (isValid) {
      // Kiểm tra giả định: nếu email và mật khẩu khớp với giá trị mẫu thì đăng nhập thành công
      if (loginEmail === "user@example.com" && loginPassword === "123456") {
        alert("Đăng nhập thành công!");
        window.location.href = "../Home/Home.html";
      } else {
        alert("Sai email hoặc mật khẩu!");
      }
    }
  }
  
  function showForgotPasswordForm() {
    document.getElementById("forgotPasswordOverlay").style.display = "block";
    document.getElementById("forgotPasswordForm").style.display = "block";
    document.getElementById("forgotEmail").focus();
  }
  
  function closeForgotPasswordForm() {
    document.getElementById("forgotPasswordOverlay").style.display = "none";
    document.getElementById("forgotPasswordForm").style.display = "none";
    document.getElementById("forgotEmail").value = "";
    document.getElementById("forgotEmailError").style.display = "none";
  }
  
  function forgotPassword(e) {
    e.preventDefault();
    const forgotEmail = document.getElementById("forgotEmail").value.trim();
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!forgotEmail || !emailRegex.test(forgotEmail)) {
      document.getElementById("forgotEmailError").style.display = "block";
      return;
    } else {
      document.getElementById("forgotEmailError").style.display = "none";
    }
    // Kiểm tra giả định: nếu email nhập vào khớp với giá trị mẫu thì gửi hướng dẫn đặt lại mật khẩu
    if (forgotEmail === "user@example.com") {
      alert("Hướng dẫn đặt lại mật khẩu đã được gửi tới email của bạn!");
      closeForgotPasswordForm();
    } else {
      alert("Không tìm thấy tài khoản với email này!");
    }
  }
  