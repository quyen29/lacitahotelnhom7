let currentPage = 1;
const rowsPerPage = 7;

function normalize(str) {
    return str.normalize("NFD").replace(/[\u0300-\u036f]/g, "").toLowerCase();
}

function searchCustomer() {
    const idSearch = document.getElementById("search-id").value.trim();
    const nameSearch = normalize(document.getElementById("search-name").value.trim());

    const rows = document.querySelectorAll("#customer-table tr");
    rows.forEach(row => row.style.display = "none");

    let filtered = Array.from(rows).filter(row => {
        const id = row.cells[0].textContent.trim();
        const name = normalize(row.cells[1].textContent.trim());
        const matchId = idSearch === "" || id.includes(idSearch);
        const matchName = nameSearch === "" || name.includes(nameSearch);
        return matchId && matchName;
    });

    paginate(filtered);
}

function paginate(filteredRows) {
    const totalPages = Math.ceil(filteredRows.length / rowsPerPage);
    filteredRows.forEach((row, index) => {
        row.style.display = (index >= (currentPage - 1) * rowsPerPage && index < currentPage * rowsPerPage) ? "" : "none";
    });

    renderPagination(filteredRows.length, totalPages);
}

function renderPagination(totalRows, totalPages) {
    let container = document.getElementById("pagination");
    if (!container) {
        container = document.createElement("div");
        container.id = "pagination";
        document.querySelector(".content-section").appendChild(container);
    }
    container.innerHTML = "";

    for (let i = 1; i <= totalPages; i++) {
        const btn = document.createElement("button");
        btn.textContent = i;
        btn.onclick = () => {
            currentPage = i;
            searchCustomer();
        };
        if (i === currentPage) btn.classList.add("active");
        container.appendChild(btn);
    }
}

window.onload = () => searchCustomer();
