// ============================================================
// layout.js - Renders shared sidebar + topbar for all pages
// ============================================================

function renderLayout(activePage, pageTitle) {
    const user = getUser();
    if (!user) { window.location.href = '../index.html'; return; }

    const isAdminUser = user.role === 'ADMIN';

    const userNav = `
        <a class="nav-item ${activePage==='dashboard'?'active':''}" href="dashboard.html">
            <span class="nav-icon">🏠</span> Dashboard
        </a>
        <a class="nav-item ${activePage==='book'?'active':''}" href="book-seat.html">
            <span class="nav-icon">🪑</span> Book a Seat
        </a>
        <a class="nav-item ${activePage==='history'?'active':''}" href="my-bookings.html">
            <span class="nav-icon">📋</span> My Bookings
        </a>`;

    const adminNav = `
        <a class="nav-item ${activePage==='admin'?'active':''}" href="admin-dashboard.html">
            <span class="nav-icon">📊</span> Dashboard
        </a>
        <a class="nav-item ${activePage==='seats'?'active':''}" href="admin-seats.html">
            <span class="nav-icon">🪑</span> Manage Seats
        </a>
        <a class="nav-item ${activePage==='bookings'?'active':''}" href="admin-bookings.html">
            <span class="nav-icon">📋</span> All Bookings
        </a>
        <a class="nav-item ${activePage==='users'?'active':''}" href="admin-users.html">
            <span class="nav-icon">👥</span> Users
        </a>
        <a class="nav-item ${activePage==='pricing'?'active':''}" href="admin-pricing.html">
            <span class="nav-icon">💰</span> Pricing
        </a>`;

    const initials = user.fullName.split(' ').map(n => n[0]).join('').toUpperCase().slice(0,2);

    document.body.innerHTML = `
        <div class="sidebar-overlay" id="sidebarOverlay" onclick="closeSidebar()"></div>
        <div class="page-wrapper">
            <aside class="sidebar" id="sidebar">
                <div class="sidebar-logo">
                    <div class="lib-name">Sangharsh K Library</div>
                    <div class="lib-loc">📍 Greater Noida, UP</div>
                </div>
                <nav>
                    ${isAdminUser ? adminNav : userNav}
                </nav>
                <div class="sidebar-footer">
                    ${isAdminUser ? '🔑 Admin Panel' : '👤 Member Portal'}<br/>
                    v1.0.0
                </div>
            </aside>
            <div class="main-content">
                <div class="topbar">
                    <div style="display:flex;align-items:center;gap:12px">
                        <button class="hamburger" onclick="toggleSidebar()">☰</button>
                        <span class="topbar-title">${pageTitle}</span>
                    </div>
                    <div class="topbar-user">
                        <div class="avatar">${initials}</div>
                        <span>${user.fullName}</span>
                        <button class="btn-logout" onclick="logout()">Logout</button>
                    </div>
                </div>
                <div class="content-area" id="contentArea"></div>
            </div>
        </div>` + document.body.innerHTML;
}

function toggleSidebar() {
    document.getElementById('sidebar').classList.toggle('open');
    document.getElementById('sidebarOverlay').classList.toggle('open');
}
function closeSidebar() {
    document.getElementById('sidebar').classList.remove('open');
    document.getElementById('sidebarOverlay').classList.remove('open');
}
