function updateUserMenu() {
    const userOptions = document.getElementById("userOptions");
    userOptions.innerHTML =
      '<a type="submit" th:href="@{/showLoginPage}">Đăng nhập</a> +
       <a type="submit" th:href="@{/register/showRegisterPage}">Đăng ký</a>'
    ;
}
  document.addEventListener("DOMContentLoaded", updateUserMenu);
  