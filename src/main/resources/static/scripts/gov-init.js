document.body.className = document.body.className
    ? document.body.className + ' js-enabled govuk-frontend-supported'
    : 'js-enabled govuk-frontend-supported';

if (window.GOVUKFrontend && typeof GOVUKFrontend.initAll === "function") {
    GOVUKFrontend.initAll();
}