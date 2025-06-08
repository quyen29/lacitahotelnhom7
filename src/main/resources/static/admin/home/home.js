function toggleSidebar() {
    const sidebar = document.querySelector('.sidebar');
    const hamburgerHeader = document.querySelector('.header .hamburger-btn');
    const dashboard = document.querySelector('.dashboard');

    sidebar.classList.toggle('active');
    dashboard.classList.toggle('sidebar-open');

    if (sidebar.classList.contains('active')) {
        hamburgerHeader.style.display = 'none';
    } else {
        hamburgerHeader.style.display = 'flex';
    }
}
