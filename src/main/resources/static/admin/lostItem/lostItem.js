function normalize(str) {
    return str
        .toLowerCase()
        .normalize("NFD")
        .replace(/[\u0300-\u036f]/g, "");
}

function searchLostItem() {
    const nameFilter = normalize(document.getElementById("search-name").value.trim());
    const roomFilter = document.getElementById("search-room").value.trim();
    const rows = Array.from(document.querySelectorAll("#lost-item-table tr"));

    rows.forEach(row => {
        const name = normalize(row.dataset.name || "");
        const room = row.dataset.room || "";

        const matchName = !nameFilter || name.includes(nameFilter);
        const matchRoom = !roomFilter || room.includes(roomFilter);

        row.style.display = matchName && matchRoom ? "" : "none";
    });

    paginate(10);
}

document.addEventListener("DOMContentLoaded", () => {
    const rowsPerPage = 5;
    const table = document.getElementById('lost-item-table');
    const rows = table.getElementsByTagName('tr');
    const pagination = document.getElementById('pagination');

    function displayPage(page) {
        const start = page * rowsPerPage;
        const end = start + rowsPerPage;

        for (let i = 0; i < rows.length; i++) {
            rows[i].style.display = (i >= start && i < end) ? '' : 'none';
        }

        renderPagination(page);
    }

    function renderPagination(currentPage) {
        const totalPages = Math.ceil(rows.length / rowsPerPage);
        pagination.innerHTML = '';

        for (let i = 0; i < totalPages; i++) {
            const btn = document.createElement('button');
            btn.innerText = i + 1;
            btn.className = (i === currentPage) ? 'active' : '';
            btn.addEventListener('click', () => displayPage(i));
            pagination.appendChild(btn);
        }
    }

    // Initial call
    displayPage(0);
});
