class UserService {
    static async getCurrentUserInfo() {
        try {
            const response = await csrfFetch('/api/user/me');
            if (response.ok) {
                return await response.json();
            }
            throw new Error('Failed to fetch user info');
        } catch (error) {
            console.error('Get user info failed:', error);
            return null;
        }
    }

    static async updateProfile(userData) {
        try {
            const response = await csrfFetch('/api/user/me', {
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
            console.error('Update profile failed:', error);
            return { success: false, error: error.message };
        }
    }
}

async function loadUserInfo() {
    const user = await UserService.getCurrentUserInfo();
    if (user) {
        renderUserInfo(user);
    } else {
        document.getElementById('userInfo').innerHTML = `
            <div class="alert alert-warning">
                Failed to load user information. Please try again.
            </div>
        `;
    }
}

function renderUserInfo(user) {
    const container = document.getElementById('userInfo');
    if (!container) return;
    container.innerHTML = `
        <div class="card">
            <div class="card-header">
                <h5>User Information</h5>
            </div>
            <div class="card-body">
                <table class="table table-bordered">
                    <tr><th>ID</th><td>${user.id}</td></tr>
                    <tr><th>Username</th><td>${user.username}</td></tr>
                    <tr><th>Email</th><td>${user.email}</td></tr>
                    <tr><th>Age</th><td>${user.age}</td></tr>
                    <tr><th>Roles</th><td>${user.roles ? user.roles.map(r => r.name.replace('ROLE_', '')).join(', ') : ''}</td></tr>
                </table>
            </div>
        </div>
    `;
}

window.userService = { loadUserInfo };

document.addEventListener('DOMContentLoaded', function() {
    if (window.location.pathname.includes('/user')) {
        loadUserInfo();
    }
});