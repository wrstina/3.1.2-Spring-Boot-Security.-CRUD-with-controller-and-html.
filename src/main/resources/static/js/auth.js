class AuthService {
    static async checkAuth() {
        try {
            const response = await csrfFetch('/api/auth/check');
            return await response.json();
        } catch (error) {
            console.error('Auth check failed:', error);
            return { authenticated: false };
        }
    }

    static async getCurrentUser() {
        try {
            const response = await csrfFetch('/api/auth/user');
            if (response.ok) {
                return await response.json();
            }
            return null;
        } catch (error) {
            console.error('Get current user failed:', error);
            return null;
        }
    }

    static async logout() {
        try {
            await csrfFetch('/api/auth/logout', { method: 'POST' });
            window.location.href = '/login';
        } catch (error) {
            console.error('Logout failed:', error);
        }
    }

    static async isAdmin() {
        try {
            const response = await csrfFetch('/api/auth/is-admin');
            const data = await response.json();
            return data.isAdmin;
        } catch (error) {
            console.error('Admin check failed:', error);
            return false;
        }
    }
}

