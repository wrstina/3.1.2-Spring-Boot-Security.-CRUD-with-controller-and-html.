class AdminService {
    static async getAllUsers() {
        try {
            const response = await csrfFetch('/api/admin/user');
            if (response.ok) {
                return await response.json();
            }
            throw new Error('Failed to fetch users');
        } catch (error) {
            console.error('Get users failed:', error);
            showError('Failed to load users');
            return [];
        }
    }

    static async createUser(userData) {
        try {
            const response = await csrfFetch('/api/admin/user', {
                method: 'POST',
                body: userData
            });

            const result = await response.json();
            if (response.ok) {
                return { success: true, data: result };
            } else {
                return { success: false, error: result };
            }
        } catch (error) {
            console.error('Create user failed:', error);
            return { success: false, error: error.message };
        }
    }

    static async updateUser(id, userData) {
        try {
            const response = await csrfFetch(`/api/admin/user/${id}`, {
                method: 'PUT',
                body: userData
            });

            const result = await response.json();
            if (response.ok) {
                return { success: true, data: result };
            } else {
                return { success: false, error: result };
            }
        } catch (error) {
            console.error('Update user failed:', error);
            return { success: false, error: error.message };
        }
    }

    static async deleteUser(id) {
        try {
            const response = await csrfFetch(`/api/admin/user/${id}`, {
                method: 'DELETE'
            });

            const result = await response.json();
            if (response.ok) {
                return { success: true, data: result };
            } else {
                return { success: false, error: result };
            }
        } catch (error) {
            console.error('Delete user failed:', error);
            return { success: false, error: error.message };
        }
    }
}

class AdminManager {
    static async loadUsers() {
        const users = await AdminService.getAllUsers();
        this.renderUsersTable(users);
    }

    static renderUsersTable(users) {
        const container = document.getElementById('usersTable');
        if (!container) return;

        container.innerHTML = `
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Username</th>
                        <th>Email</th>
                        <th>Age</th>
                        <th>Roles</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody id="usersTableBody">
                    ${users.map(user => `
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.username}</td>
                            <td>${user.email}</td>
                            <td>${user.age}</td>
                            <td>${user.roles ? user.roles.map(role => role.name.replace('ROLE_', '')).join(', ') : ''}</td>
                            <td>
                                <button class="btn btn-warning btn-sm" onclick="AdminManager.openEditModal(${user.id})">Edit</button>
                                <button class="btn btn-danger btn-sm" onclick="AdminManager.openDeleteModal(${user.id})">Delete</button>
                            </td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        `;
    }

    static async openEditModal(userId) {
        const users = await AdminService.getAllUsers();
        const user = users.find(u => u.id === userId);
        if (user) {
            alert(`Edit user: ${user.username}`);
            console.log('Edit user:', user);
        }
    }

    static async openDeleteModal(userId) {
        const users = await AdminService.getAllUsers();
        const user = users.find(u => u.id === userId);

        if (user && confirm(`Are you sure you want to delete user: ${user.username}?`)) {
            const result = await AdminService.deleteUser(userId);
            if (result.success) {
                showSuccess('User deleted successfully');
                await AdminManager.loadUsers();
            } else {
                showError(result.error);
            }
        }
    }

    static openAddModal() {
        alert('Open add user modal');
    }
}

function showSuccess(message) {
    const alertDiv = document.createElement('div');
    alertDiv.className = 'alert alert-success alert-dismissible fade show';
    alertDiv.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    document.querySelector('.container').prepend(alertDiv);
    setTimeout(() => alertDiv.remove(), 3000);
}

function showError(message) {
    const alertDiv = document.createElement('div');
    alertDiv.className = 'alert alert-danger alert-dismissible fade show';
    alertDiv.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    document.querySelector('.container').prepend(alertDiv);
    setTimeout(() => alertDiv.remove(), 5000);
}

window.adminManager = AdminManager;

document.addEventListener('DOMContentLoaded', function() {
    if (window.location.pathname.includes('/admin')) {
        AdminManager.loadUsers();
    }
});