(function () {
    const tokenTag = document.querySelector('meta[name="_csrf"]');
    const headerTag = document.querySelector('meta[name="_csrf_header"]');

    function readCookie(name) {
        const m = document.cookie.match(new RegExp('(^| )' + name + '=([^;]+)'));
        return m ? decodeURIComponent(m[2]) : null;
    }

    const csrfToken = tokenTag?.content || readCookie('XSRF-TOKEN');
    const csrfHeaderName = headerTag?.content || 'X-CSRF-TOKEN';

    // Обёртка над fetch: для POST/PUT/PATCH/DELETE добавляет заголовок с токеном
    window.csrfFetch = function (url, options = {}) {
        const method = (options.method || 'GET').toUpperCase();
        const needHeader = ['POST', 'PUT', 'PATCH', 'DELETE'].includes(method);

        const headers = new Headers(options.headers || {});
        if (needHeader && csrfToken) {
            headers.set(csrfHeaderName, csrfToken);
        }
        if (options.body && typeof options.body === 'object' && !headers.has('Content-Type')) {
            headers.set('Content-Type', 'application/json');
            options.body = JSON.stringify(options.body);
        }
        return fetch(url, { ...options, headers });
    };
})();