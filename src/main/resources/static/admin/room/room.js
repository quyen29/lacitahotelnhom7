// Hàm mở modal
function openModal(modalId, id = null) {
    document.getElementById(modalId).style.display = 'flex';
  }

  // Hàm đóng modal
  function closeModal(modalId) {
    document.getElementById(modalId).style.display = 'none';
  }

  // Đóng modal khi click bên ngoài
  window.onclick = function(event) {
    var modals = document.getElementsByClassName('modal');
    for (var i = 0; i < modals.length; i++) {
      if (event.target == modals[i]) {
        modals[i].style.display = 'none';
      }
    }
  }
