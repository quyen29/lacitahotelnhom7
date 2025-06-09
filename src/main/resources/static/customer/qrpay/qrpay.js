document.addEventListener("DOMContentLoaded", function () {
  const userIcon = document.querySelector(".user-icon");
  const userOptions = document.getElementById("userOptions");

  userIcon?.addEventListener("click", () => {
    userOptions.classList.toggle("show");
  });

  // Ẩn dropdown nếu click ra ngoài
  document.addEventListener("click", function (e) {
    if (!e.target.closest(".user-menu")) {
      userOptions?.classList.remove("show");
    }
  });
});
