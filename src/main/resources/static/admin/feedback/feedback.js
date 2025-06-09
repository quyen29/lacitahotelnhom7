document.addEventListener('DOMContentLoaded', function () {
    const rowsPerPage = 5;
    const table = document.getElementById('feedback-table');
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

    displayPage(0);
});