var usingOldIE = $.browser.msie && parseInt($.browser.version) < 9;
if(!usingOldIE) {
    $("head").append("<link rel=\"shortcut icon\" href=\"{@docRoot}/favicon.ico?v=cafebabe\">");
    $("head", window.parent.document).append("<link rel=\"shortcut icon\" href=\"{@docRoot}/favicon.ico?v=cafebabe\">");
    hljs.initHighlightingOnLoad();
    $("pre.code").css("font-size", "0.9em");
    injectProjectVersionForJavadocJDK6("Mockito ${project.version} API",
        "em#mockito-version-header-javadoc7-header",
        "em#mockito-version-header-javadoc7-footer");
}

if ('serviceWorker' in navigator && window.location.protocol === 'https') {
    var toast = document.getElementById('sw-toast');
    if (!toast) {
        toast = document.createElement('div');
        toast.id = 'sw-toast';
        toast.show = function(message) {
            toast.textContent = message;
            setTimeout(function() {
                toast.classList.remove('shown')
            }, 3000);
            toast.classList.add('shown');
        };
        document.body.appendChild(toast);
        toast.classList.add('hidden');
    }
    // Show a toast with a service-worker-related update.
    window.showToast = function(message) {
        toast.show(message);
    };

    navigator.serviceWorker.register('service-worker.js')
        .then(function(registration) {
            registration.onupdatefound = function() {
                // The updatefound event implies that registration.installing is set; see
                // https://slightlyoff.github.io/ServiceWorker/spec/service_worker/index.html#service-worker-container-updatefound-event
                var installingWorker = registration.installing;
                installingWorker.onstatechange = function() {
                    switch (installingWorker.state) {
                        case 'installed':
                            if (!navigator.serviceWorker.controller) {
                                toast.show('Service Worker installed! Pages you view are cached for offline use.');
                            }
                            break;
                        case 'redundant':
                            throw Error('The installing service worker became redundant.');
                    }
                };
            };
        }).catch(function(e) {
            console.error('Service worker registration failed:', e);
            // toast.show('Service worker registration failed');
        });
}

// Check to see if the service worker controlling the page at initial load
// has become redundant, since this implies there's a new service worker with fresh content.
if (navigator.serviceWorker && navigator.serviceWorker.controller) {
    navigator.serviceWorker.controller.onstatechange = function(event) {
        if (event.target.state === 'redundant') {
            toast.show('Site updated. Refresh this page to see the latest content.');
        }
    };
}
