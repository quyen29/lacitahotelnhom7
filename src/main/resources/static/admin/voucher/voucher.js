let rowsPerPage = 7;
let currentPage = 1;

function searchVoucher() {
    const roomFilter = document.getElementById("search-room").value.toLowerCase();
    const customerFilter = document.getElementById("search-customer").value.toLowerCase();
    const rows = Array.from(document.querySelectorAll("#voucher-table tr"));

    rows.forEach(row => {
        const room = row.children[5].textContent.toLowerCase();
        const customer = row.children[6].textContent.toLowerCase();

        const matchRoom = roomFilter === "all" || room.includes(roomFilter);
        const matchCustomer = customerFilter === "all" || customer.includes(customerFilter);

        // Gán class để phân trang sau
        if (matchRoom && matchCustomer) {
            row.classList.remove("filtered-out");
        } else {
            row.classList.add("filtered-out");
        }
    });

    currentPage = 1;
    paginate();
}

function paginate() {
    const rows = Array.from(document.querySelectorAll("#voucher-table tr"));
    const visibleRows = rows.filter(row => !row.classList.contains("filtered-out"));
    const totalPages = Math.ceil(visibleRows.length / rowsPerPage);

    rows.forEach(row => {
        row.style.display = "none"; // Ẩn hết
    });

    visibleRows.forEach((row, index) => {
        if (index >= (currentPage - 1) * rowsPerPage && index < currentPage * rowsPerPage) {
            row.style.display = ""; // Chỉ hiển thị dòng trong trang hiện tại
        }
    });

    renderPagination(totalPages);
}

function renderPagination(totalPages) {
    const container = document.getElementById("pagination");
    container.innerHTML = "";

    for (let i = 1; i <= totalPages; i++) {
        const btn = document.createElement("button");
        btn.textContent = i;
        btn.className = i === currentPage ? "active" : "";
        btn.onclick = () => {
            currentPage = i;
            paginate();
        };
        container.appendChild(btn);
    }
}

window.onload = () => searchVoucher();
