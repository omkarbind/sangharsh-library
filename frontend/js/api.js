// ============================================================
// api.js - Centralized API communication layer
// Sangharsh K Library - Greater Noida
// ============================================================

const API_BASE = 'http://localhost:8080/api';

// ---------- Auth helpers ----------
function getToken() { return localStorage.getItem('token'); }
function getUser()  { return JSON.parse(localStorage.getItem('user') || 'null'); }
function isAdmin()  { return getUser()?.role === 'ADMIN'; }
function isLoggedIn() { return !!getToken(); }

function saveAuth(data) {
    localStorage.setItem('token', data.token);
    localStorage.setItem('user', JSON.stringify(data));
}

function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    window.location.href = '../index.html';
}

// ---------- Core fetch wrapper ----------
async function apiFetch(endpoint, options = {}) {
    const token = getToken();
    const headers = { 'Content-Type': 'application/json', ...options.headers };
    if (token) headers['Authorization'] = `Bearer ${token}`;

    const res = await fetch(`${API_BASE}${endpoint}`, { ...options, headers });
    const json = await res.json();

    if (!res.ok || !json.success) {
        throw new Error(json.message || 'Something went wrong');
    }
    return json.data;
}

// ---------- Auth APIs ----------
const AuthAPI = {
    register: (body) => apiFetch('/auth/register', { method: 'POST', body: JSON.stringify(body) }),
    login:    (body) => apiFetch('/auth/login',    { method: 'POST', body: JSON.stringify(body) }),
};

// ---------- Seat APIs ----------
const SeatAPI = {
    available: (date, start, end) =>
        apiFetch(`/seats/available?date=${date}&startTime=${start}&endTime=${end}`),
    all:    ()      => apiFetch('/seats/all'),
    add:    (body)  => apiFetch('/seats', { method: 'POST', body: JSON.stringify(body) }),
    toggle: (id)    => apiFetch(`/seats/${id}/toggle`, { method: 'PUT' }),
    delete: (id)    => apiFetch(`/seats/${id}`, { method: 'DELETE' }),
};

// ---------- Booking APIs ----------
const BookingAPI = {
    create:         (body) => apiFetch('/bookings',       { method: 'POST', body: JSON.stringify(body) }),
    myBookings:     ()     => apiFetch('/bookings/my'),
    allBookings:    ()     => apiFetch('/bookings/all'),
    cancel:         (id)   => apiFetch(`/bookings/${id}/cancel`, { method: 'PUT' }),
    confirmPayment: (id)   => apiFetch(`/bookings/${id}/pay`,    { method: 'PUT' }),
};

// ---------- Pricing APIs ----------
const PricingAPI = {
    current: () => apiFetch('/pricing/current'),
    set:     (body) => apiFetch('/admin/pricing', { method: 'POST', body: JSON.stringify(body) }),
};

// ---------- Admin APIs ----------
const AdminAPI = {
    dashboard:    ()     => apiFetch('/admin/dashboard'),
    users:        ()     => apiFetch('/admin/users'),
    toggleUser:   (id)   => apiFetch(`/admin/users/${id}/toggle`, { method: 'PUT' }),
};

// ---------- UI Utilities ----------
function showToast(msg, type = 'success') {
    const existing = document.querySelector('.toast');
    if (existing) existing.remove();
    const t = document.createElement('div');
    t.className = `toast toast-${type}`;
    t.textContent = msg;
    document.body.appendChild(t);
    setTimeout(() => t.classList.add('show'), 10);
    setTimeout(() => { t.classList.remove('show'); setTimeout(() => t.remove(), 300); }, 3000);
}

function formatCurrency(amount) {
    return '₹' + parseFloat(amount).toFixed(2);
}

function formatDate(dateStr) {
    return new Date(dateStr).toLocaleDateString('en-IN', { day:'2-digit', month:'short', year:'numeric' });
}

function formatDateTime(dtStr) {
    return new Date(dtStr).toLocaleString('en-IN');
}

function requireAuth() {
    if (!isLoggedIn()) { window.location.href = '../index.html'; }
}

function requireAdmin() {
    if (!isLoggedIn() || !isAdmin()) { window.location.href = '../index.html'; }
}

function statusBadge(status) {
    const map = {
        CONFIRMED: 'badge-green',
        CANCELLED: 'badge-red',
        COMPLETED: 'badge-blue',
        PENDING:   'badge-yellow',
        PAID:      'badge-green',
        REFUNDED:  'badge-orange',
    };
    return `<span class="badge ${map[status] || 'badge-gray'}">${status}</span>`;
}
