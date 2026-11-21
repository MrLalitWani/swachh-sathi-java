// Swachh Sathi Frontend Application Logic
const API_BASE = window.location.origin;

const state = {
    user: null,
    token: localStorage.getItem('swachh_jwt') || null,
    role: null,
    citizenMap: null,
    workerMap: null,
    adminMap: null,
    citizenMarker: null,
    routePolyline: null
};

// API Helper
async function apiRequest(endpoint, options = {}) {
    const headers = { ...options.headers };
    if (state.token) {
        headers['Authorization'] = `Bearer ${state.token}`;
    }
    if (!(options.body instanceof FormData)) {
        headers['Content-Type'] = 'application/json';
    }

    try {
        const res = await fetch(`${API_BASE}${endpoint}`, { ...options, headers });
        if (res.status === 401) {
            logout();
            throw new Error("Session expired. Please sign in again.");
        }
        if (!res.ok) {
            const err = await res.json().catch(() => ({ message: res.statusText }));
            throw new Error(err.message || 'Request failed');
        }
        if (res.status === 204) return null;
        return await res.json();
    } catch (e) {
        console.error("API Error:", e);
        throw e;
    }
}

// Initialization on load
document.addEventListener('DOMContentLoaded', () => {
    applyTranslations();
    checkExistingAuth();
});

async function checkExistingAuth() {
    if (state.token) {
        try {
            const user = await apiRequest('/api/auth/me');
            state.user = user;
            openDashboardForRole(user.role);
        } catch (e) {
            logout();
        }
    } else {
        showRoleSelect();
    }
}

function selectRole(role) {
    state.role = role;
    openAuthModal(role);
}

function showRoleSelect() {
    document.getElementById('roleSelectSection').classList.remove('hidden');
    document.getElementById('citizenDashboard').classList.add('hidden');
    document.getElementById('workerDashboard').classList.add('hidden');
    document.getElementById('adminDashboard').classList.add('hidden');
}

function openDashboardForRole(role) {
    document.getElementById('roleSelectSection').classList.add('hidden');
    document.getElementById('authModal').classList.add('hidden');

    if (role === 'ROLE_CITIZEN') {
        document.getElementById('citizenDashboard').classList.remove('hidden');
        document.getElementById('workerDashboard').classList.add('hidden');
        document.getElementById('adminDashboard').classList.add('hidden');
        document.getElementById('citizenName').textContent = state.user.name;
        initCitizenMap();
        loadCitizenComplaints();
    } else if (role === 'ROLE_WORKER') {
        document.getElementById('workerDashboard').classList.remove('hidden');
        document.getElementById('citizenDashboard').classList.add('hidden');
        document.getElementById('adminDashboard').classList.add('hidden');
        document.getElementById('workerName').textContent = state.user.name;
        initWorkerMap();
        loadWorkerTasks();
    } else if (role === 'ROLE_ADMIN') {
        document.getElementById('adminDashboard').classList.remove('hidden');
        document.getElementById('citizenDashboard').classList.add('hidden');
        document.getElementById('workerDashboard').classList.add('hidden');
        document.getElementById('adminName').textContent = state.user.name;
        initAdminMap();
        loadAdminMetrics();
        loadAllAdminComplaints();
        loadAdminWorkers();
        loadJobApplications();
    }
}

// Auth Handlers
function openAuthModal(role) {
    const modal = document.getElementById('authModal');
    modal.classList.remove('hidden');
    document.getElementById('authRoleBadge').textContent = role.replace('ROLE_', '');
    document.getElementById('registerRoleInput').value = role;
    showLoginForm();
}

function closeAuthModal() {
    document.getElementById('authModal').classList.add('hidden');
}

function showLoginForm() {
    document.getElementById('loginFormContainer').classList.remove('hidden');
    document.getElementById('registerFormContainer').classList.add('hidden');
}

function showRegisterForm() {
    document.getElementById('loginFormContainer').classList.add('hidden');
    document.getElementById('registerFormContainer').classList.remove('hidden');
}

// Quick 1-click Demo Logins
async function quickLogin(email, password) {
    try {
        const res = await apiRequest('/api/auth/login', {
            method: 'POST',
            body: JSON.stringify({ email, password })
        });
        state.token = res.token;
        state.user = res.user;
        localStorage.setItem('swachh_jwt', res.token);
        openDashboardForRole(res.user.role);
    } catch (e) {
        alert("Login failed: " + e.message);
    }
}

async function handleLogin(e) {
    e.preventDefault();
    const email = document.getElementById('loginEmail').value;
    const password = document.getElementById('loginPassword').value;

    try {
        const res = await apiRequest('/api/auth/login', {
            method: 'POST',
            body: JSON.stringify({ email, password })
        });
        state.token = res.token;
        state.user = res.user;
        localStorage.setItem('swachh_jwt', res.token);
        openDashboardForRole(res.user.role);
    } catch (err) {
        alert("Login error: " + err.message);
    }
}

async function handleRegister(e) {
    e.preventDefault();
    const payload = {
        name: document.getElementById('regName').value,
        email: document.getElementById('regEmail').value,
        password: document.getElementById('regPassword').value,
        role: document.getElementById('registerRoleInput').value,
        phone: document.getElementById('regPhone').value,
        address: document.getElementById('regAddress').value
    };

    try {
        const res = await apiRequest('/api/auth/register', {
            method: 'POST',
            body: JSON.stringify(payload)
        });
        state.token = res.token;
        state.user = res.user;
        localStorage.setItem('swachh_jwt', res.token);
        openDashboardForRole(res.user.role);
    } catch (err) {
        alert("Registration failed: " + err.message);
    }
}

function logout() {
    state.token = null;
    state.user = null;
    localStorage.removeItem('swachh_jwt');
    showRoleSelect();
}

// --- CITIZEN MODULE ---
function initCitizenMap() {
    if (state.citizenMap) return;
    const defaultCoords = [18.5204, 73.8567]; // Pune center
    state.citizenMap = L.map('citizenMap').setView(defaultCoords, 13);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; OpenStreetMap contributors'
    }).addTo(state.citizenMap);

    state.citizenMarker = L.marker(defaultCoords, { draggable: true }).addTo(state.citizenMap);
    updateCoordsInputs(defaultCoords[0], defaultCoords[1]);

    state.citizenMarker.on('dragend', function (e) {
        const pos = e.target.getLatLng();
        updateCoordsInputs(pos.lat, pos.lng);
    });

    state.citizenMap.on('click', function (e) {
        state.citizenMarker.setLatLng(e.latlng);
        updateCoordsInputs(e.latlng.lat, e.latlng.lng);
    });
}

function updateCoordsInputs(lat, lng) {
    document.getElementById('reportLat').value = lat.toFixed(6);
    document.getElementById('reportLng').value = lng.toFixed(6);
}

function useCurrentLocation() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(pos => {
            const lat = pos.coords.latitude;
            const lng = pos.coords.longitude;
            state.citizenMap.setView([lat, lng], 15);
            state.citizenMarker.setLatLng([lat, lng]);
            updateCoordsInputs(lat, lng);
        }, err => {
            alert("Could not get GPS location. Click directly on the map instead.");
        });
    }
}

async function handleReportSubmit(e) {
    e.preventDefault();
    const photoFile = document.getElementById('reportPhoto').files[0];
    let photoUrl = null;

    if (photoFile) {
        const formData = new FormData();
        formData.append('file', photoFile);
        try {
            const uploadRes = await apiRequest('/api/files/upload', {
                method: 'POST',
                body: formData
            });
            photoUrl = uploadRes.url;
        } catch (err) {
            alert("Photo upload failed: " + err.message);
            return;
        }
    }

    const payload = {
        wasteType: document.getElementById('reportWasteType').value,
        description: document.getElementById('reportDesc').value,
        latitude: parseFloat(document.getElementById('reportLat').value),
        longitude: parseFloat(document.getElementById('reportLng').value),
        address: document.getElementById('reportAddress').value,
        photoUrl: photoUrl
    };

    try {
        const complaint = await apiRequest('/api/complaints', {
            method: 'POST',
            body: JSON.stringify(payload)
        });
        alert(`Complaint registered successfully! Priority Assigned: ${complaint.priorityLevel} (Score: ${complaint.vicinityScore})`);
        document.getElementById('reportForm').reset();
        loadCitizenComplaints();
    } catch (err) {
        alert("Failed to submit complaint: " + err.message);
    }
}

async function loadCitizenComplaints() {
    try {
        const complaints = await apiRequest('/api/complaints/my');
        const listContainer = document.getElementById('citizenComplaintsList');
        listContainer.innerHTML = '';

        if (complaints.length === 0) {
            listContainer.innerHTML = `<p class="text-slate-500 py-4 text-center">No complaints reported yet.</p>`;
            return;
        }

        complaints.forEach(c => {
            const item = document.createElement('div');
            item.className = "p-4 bg-white rounded-lg border border-slate-200 shadow-sm flex flex-col md:flex-row justify-between gap-4";
            item.innerHTML = `
                <div class="space-y-1">
                    <div class="flex items-center gap-2">
                        <span class="px-2.5 py-0.5 rounded-full text-xs font-semibold badge-${c.priorityLevel ? c.priorityLevel.toLowerCase().replace('_', '-') : 'low'}">
                            ${c.priorityLevel || 'NORMAL'}
                        </span>
                        <span class="px-2.5 py-0.5 rounded-full text-xs font-medium status-${c.status.toLowerCase().replace('_', '-')}">
                            ${c.status.replace('_', ' ')}
                        </span>
                        <span class="text-xs text-slate-400">#${c.id}</span>
                    </div>
                    <h4 class="font-semibold text-slate-800">${c.wasteType}</h4>
                    <p class="text-sm text-slate-600">${c.description}</p>
                    <p class="text-xs text-slate-400"><i class="fas fa-map-marker-alt mr-1"></i> ${c.address || `${c.latitude}, ${c.longitude}`}</p>
                </div>
                ${c.photoUrl ? `<img src="${c.photoUrl}" class="w-24 h-24 object-cover rounded-lg border" alt="Complaint photo">` : ''}
            `;
            listContainer.appendChild(item);
        });
    } catch (err) {
        console.error(err);
    }
}

// --- WORKER MODULE ---
function initWorkerMap() {
    if (state.workerMap) return;
    state.workerMap = L.map('workerMap').setView([18.5204, 73.8567], 13);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(state.workerMap);
}

async function loadWorkerTasks() {
    try {
        const tasks = await apiRequest('/api/worker/tasks');
        const container = document.getElementById('workerTasksList');
        container.innerHTML = '';
        document.getElementById('workerTaskCount').textContent = tasks.filter(t => t.status === 'ASSIGNED' || t.status === 'REPORTED').length;

        tasks.forEach(task => {
            const card = document.createElement('div');
            card.className = "p-4 bg-white rounded-lg border border-slate-200 shadow-sm flex flex-col justify-between gap-2";
            card.innerHTML = `
                <div class="flex justify-between items-start">
                    <div>
                        <span class="px-2 py-0.5 rounded text-xs font-bold badge-${task.priorityLevel ? task.priorityLevel.toLowerCase().replace('_', '-') : 'low'}">${task.priorityLevel}</span>
                        <h4 class="font-semibold text-slate-800 mt-1">${task.wasteType}</h4>
                    </div>
                    <span class="text-xs px-2 py-0.5 rounded status-${task.status.toLowerCase().replace('_', '-')}">${task.status}</span>
                </div>
                <p class="text-sm text-slate-600">${task.description}</p>
                <p class="text-xs text-slate-400"><i class="fas fa-location-dot"></i> ${task.address || `${task.latitude}, ${task.longitude}`}</p>
                ${task.status === 'ASSIGNED' || task.status === 'REPORTED' ? `
                    <button onclick="openResolveModal(${task.id})" class="mt-2 w-full py-1.5 px-3 bg-emerald-600 hover:bg-emerald-700 text-white rounded text-sm font-medium">
                        <i class="fas fa-check-circle mr-1"></i> Submit Resolution
                    </button>
                ` : `<span class="text-xs text-slate-500 italic mt-2">Awaiting Admin Verification</span>`}
            `;
            container.appendChild(card);

            // Add marker to worker map
            L.marker([task.latitude, task.longitude]).addTo(state.workerMap)
                .bindPopup(`<b>#${task.id} ${task.wasteType}</b><br>${task.description}`);
        });
    } catch (e) {
        console.error(e);
    }
}

async function optimizeRoute() {
    try {
        const plan = await apiRequest('/api/worker/route-plan');
        const routeContainer = document.getElementById('optimizedRouteSection');
        routeContainer.classList.remove('hidden');
        document.getElementById('routeDistance').textContent = `${plan.estimatedTotalDistanceKm} km`;
        document.getElementById('routeStopsCount').textContent = plan.totalStops;

        const stopsList = document.getElementById('routeStopsTimeline');
        stopsList.innerHTML = '';

        if (state.routePolyline) {
            state.workerMap.removeLayer(state.routePolyline);
        }

        const latLngs = [];
        plan.stops.forEach((stop, idx) => {
            latLngs.push([stop.latitude, stop.longitude]);
            const li = document.createElement('li');
            li.className = "relative pl-6 pb-4 border-l-2 border-emerald-300 last:border-none";
            li.innerHTML = `
                <div class="absolute -left-2 top-0 w-4 h-4 rounded-full bg-emerald-600 text-white text-[10px] flex items-center justify-center font-bold">${idx + 1}</div>
                <div class="bg-slate-50 p-2.5 rounded border border-slate-200">
                    <span class="text-xs font-bold text-emerald-700">Stop ${idx + 1}: #${stop.id}</span>
                    <p class="text-sm font-semibold text-slate-800">${stop.wasteType}</p>
                    <p class="text-xs text-slate-500">${stop.address || `${stop.latitude}, ${stop.longitude}`}</p>
                </div>
            `;
            stopsList.appendChild(li);
        });

        if (latLngs.length > 1) {
            state.routePolyline = L.polyline(latLngs, { color: '#059669', weight: 4, dashArray: '6, 8' }).addTo(state.workerMap);
            state.workerMap.fitBounds(state.routePolyline.getBounds());
        }
    } catch (e) {
        alert("Failed to compute optimized route: " + e.message);
    }
}

function openResolveModal(complaintId) {
    document.getElementById('resolveComplaintId').value = complaintId;
    document.getElementById('resolveModal').classList.remove('hidden');
}

function closeResolveModal() {
    document.getElementById('resolveModal').classList.add('hidden');
}

async function handleResolveSubmit(e) {
    e.preventDefault();
    const id = document.getElementById('resolveComplaintId').value;
    const proofFile = document.getElementById('resolvePhoto').files[0];
    let proofUrl = null;

    if (proofFile) {
        const fd = new FormData();
        fd.append('file', proofFile);
        const res = await apiRequest('/api/files/upload', { method: 'POST', body: fd });
        proofUrl = res.url;
    }

    const payload = {
        proofPhotoUrl: proofUrl,
        resolutionNotes: document.getElementById('resolveNotes').value
    };

    try {
        await apiRequest(`/api/complaints/${id}/resolve`, {
            method: 'PUT',
            body: JSON.stringify(payload)
        });
        alert("Resolution proof submitted! Awaiting administrator review.");
        closeResolveModal();
        loadWorkerTasks();
    } catch (err) {
        alert("Failed to submit resolution: " + err.message);
    }
}

// --- ADMIN MODULE ---
function initAdminMap() {
    if (state.adminMap) return;
    state.adminMap = L.map('adminMap').setView([18.5204, 73.8567], 12);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(state.adminMap);
}

async function loadAdminMetrics() {
    try {
        const m = await apiRequest('/api/admin/metrics');
        document.getElementById('metricTotal').textContent = m.totalComplaints;
        document.getElementById('metricResolved').textContent = m.resolvedComplaints;
        document.getElementById('metricPending').textContent = m.pendingVerification;
        document.getElementById('metricTurnaround').textContent = `${m.averageResolutionDays} d`;
        document.getElementById('metricWorkers').textContent = m.totalWorkers;
        document.getElementById('metricCitizens').textContent = m.totalCitizens;
    } catch (e) {
        console.error(e);
    }
}

let activeAdminComplaints = [];
let availableWorkers = [];

async function loadAdminWorkers() {
    try {
        availableWorkers = await apiRequest('/api/admin/workers');
    } catch (e) {
        console.error(e);
    }
}

async function loadAllAdminComplaints() {
    try {
        activeAdminComplaints = await apiRequest('/api/complaints');
        renderAdminComplaints(activeAdminComplaints);

        // Populate markers on citywide map
        activeAdminComplaints.forEach(c => {
            const color = c.status === 'RESOLVED' ? '#10b981' : (c.priorityLevel === 'CRITICAL' ? '#ef4444' : '#f59e0b');
            L.circleMarker([c.latitude, c.longitude], { color: color, radius: 7 }).addTo(state.adminMap)
                .bindPopup(`<b>#${c.id} ${c.wasteType}</b><br>Status: ${c.status}<br>Priority: ${c.priorityLevel}`);
        });
    } catch (e) {
        console.error(e);
    }
}

function renderAdminComplaints(list) {
    const tbody = document.getElementById('adminComplaintsTbody');
    tbody.innerHTML = '';

    list.forEach(c => {
        const tr = document.createElement('tr');
        tr.className = "border-b hover:bg-slate-50";
        tr.innerHTML = `
            <td class="p-3 font-semibold">#${c.id}</td>
            <td class="p-3">${c.wasteType}</td>
            <td class="p-3 text-xs"><span class="px-2 py-0.5 rounded badge-${c.priorityLevel ? c.priorityLevel.toLowerCase().replace('_', '-') : 'low'}">${c.priorityLevel}</span></td>
            <td class="p-3 text-xs"><span class="px-2 py-0.5 rounded status-${c.status.toLowerCase().replace('_', '-')}">${c.status}</span></td>
            <td class="p-3 text-sm">${c.assignedWorker ? c.assignedWorker.name : '<span class="text-amber-600 font-medium">Unassigned</span>'}</td>
            <td class="p-3 space-x-1">
                ${c.status !== 'RESOLVED' ? `
                    <button onclick="openAssignModal(${c.id})" class="px-2 py-1 text-xs bg-blue-600 hover:bg-blue-700 text-white rounded">Assign</button>
                ` : ''}
                ${c.status === 'PENDING_VERIFICATION' ? `
                    <button onclick="openVerifyModal(${c.id})" class="px-2 py-1 text-xs bg-purple-600 hover:bg-purple-700 text-white rounded">Verify</button>
                ` : ''}
            </td>
        `;
        tbody.appendChild(tr);
    });
}

function openAssignModal(complaintId) {
    document.getElementById('assignComplaintId').value = complaintId;
    const select = document.getElementById('assignWorkerSelect');
    select.innerHTML = '<option value="">-- Choose Worker --</option>';
    availableWorkers.forEach(w => {
        select.innerHTML += `<option value="${w.id}">${w.name} (${w.phone || w.email})</option>`;
    });
    document.getElementById('assignModal').classList.remove('hidden');
}

function closeAssignModal() {
    document.getElementById('assignModal').classList.add('hidden');
}

async function handleAssignSubmit(e) {
    e.preventDefault();
    const complaintId = document.getElementById('assignComplaintId').value;
    const workerId = document.getElementById('assignWorkerSelect').value;

    try {
        await apiRequest(`/api/complaints/${complaintId}/assign`, {
            method: 'PUT',
            body: JSON.stringify({ workerId: parseInt(workerId) })
        });
        alert("Worker assigned successfully!");
        closeAssignModal();
        loadAllAdminComplaints();
        loadAdminMetrics();
    } catch (err) {
        alert("Failed to assign worker: " + err.message);
    }
}

function openVerifyModal(complaintId) {
    const c = activeAdminComplaints.find(item => item.id === complaintId);
    if (!c) return;
    document.getElementById('verifyComplaintId').value = complaintId;
    document.getElementById('verifyOriginalPhoto').src = c.photoUrl || '';
    document.getElementById('verifyProofPhoto').src = c.proofPhotoUrl || '';
    document.getElementById('verifyNotesText').textContent = c.resolutionNotes || 'No notes submitted.';
    document.getElementById('verifyModal').classList.remove('hidden');
}

function closeVerifyModal() {
    document.getElementById('verifyModal').classList.add('hidden');
}

async function submitVerification(approved) {
    const id = document.getElementById('verifyComplaintId').value;
    const feedback = document.getElementById('verifyFeedback').value;

    try {
        await apiRequest(`/api/complaints/${id}/verify`, {
            method: 'PUT',
            body: JSON.stringify({ approved, feedback })
        });
        alert(approved ? "Complaint resolution verified and closed!" : "Complaint rejected and returned to worker.");
        closeVerifyModal();
        loadAllAdminComplaints();
        loadAdminMetrics();
    } catch (err) {
        alert("Verification action failed: " + err.message);
    }
}

// --- JOB APPLICATIONS ---
async function loadJobApplications() {
    try {
        const apps = await apiRequest('/api/admin/jobs');
        const container = document.getElementById('jobAppsList');
        container.innerHTML = '';

        if (apps.length === 0) {
            container.innerHTML = `<p class="text-slate-500 py-3 text-center">No job applications submitted.</p>`;
            return;
        }

        apps.forEach(app => {
            const card = document.createElement('div');
            card.className = "p-3 bg-white rounded border border-slate-200 flex justify-between items-center";
            card.innerHTML = `
                <div>
                    <h5 class="font-bold text-slate-800">${app.applicantName}</h5>
                    <p class="text-xs text-slate-600"><i class="fas fa-envelope mr-1"></i>${app.email} | <i class="fas fa-phone mr-1"></i>${app.phone}</p>
                    <p class="text-xs text-slate-500">Exp: ${app.experienceYears || 0} yrs | Status: <span class="font-bold">${app.status}</span></p>
                </div>
                ${app.status === 'PENDING' ? `
                    <div class="space-x-1">
                        <button onclick="reviewJob(${app.id}, 'APPROVED')" class="px-2.5 py-1 text-xs bg-emerald-600 hover:bg-emerald-700 text-white rounded">Approve</button>
                        <button onclick="reviewJob(${app.id}, 'REJECTED')" class="px-2.5 py-1 text-xs bg-red-600 hover:bg-red-700 text-white rounded">Reject</button>
                    </div>
                ` : `<span class="text-xs font-semibold px-2 py-1 rounded bg-slate-100 text-slate-600">${app.status}</span>`}
            `;
            container.appendChild(card);
        });
    } catch (e) {
        console.error(e);
    }
}

async function reviewJob(id, status) {
    try {
        await apiRequest(`/api/admin/jobs/${id}/review`, {
            method: 'PUT',
            body: JSON.stringify({ status, adminNotes: `Reviewed by admin: ${status}` })
        });
        alert(`Application ${status.toLowerCase()}!`);
        loadJobApplications();
        loadAdminWorkers();
    } catch (err) {
        alert("Action failed: " + err.message);
    }
}

function openJobModal() {
    document.getElementById('jobModal').classList.remove('hidden');
}

function closeJobModal() {
    document.getElementById('jobModal').classList.add('hidden');
}

async function handleJobSubmit(e) {
    e.preventDefault();
    const payload = {
        applicantName: document.getElementById('jobName').value,
        email: document.getElementById('jobEmail').value,
        phone: document.getElementById('jobPhone').value,
        address: document.getElementById('jobAddress').value,
        experienceYears: parseInt(document.getElementById('jobExp').value) || 0
    };

    try {
        await apiRequest('/api/jobs/apply', {
            method: 'POST',
            body: JSON.stringify(payload)
        });
        alert("Application submitted successfully! Our municipal team will review it.");
        document.getElementById('jobForm').reset();
        closeJobModal();
    } catch (err) {
        alert("Failed to submit application: " + err.message);
    }
}
