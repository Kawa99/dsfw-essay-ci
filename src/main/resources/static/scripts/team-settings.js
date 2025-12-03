// Team Name Edit Functions
function enableEditTeamName(teamId) {
    const displayEl = document.getElementById('team-name-display-' + teamId);
    const btnEl = document.getElementById('edit-name-btn-' + teamId);
    const editEl = document.getElementById('team-name-edit-' + teamId);

    if (displayEl) displayEl.style.display = 'none';
    if (btnEl) btnEl.style.display = 'none';
    if (editEl) editEl.style.display = 'block';
}

function cancelEditTeamName(teamId) {
    const displayEl = document.getElementById('team-name-display-' + teamId);
    const btnEl = document.getElementById('edit-name-btn-' + teamId);
    const editEl = document.getElementById('team-name-edit-' + teamId);
    const errorEl = document.getElementById('team-name-error-' + teamId);

    if (editEl) editEl.style.display = 'none';
    if (displayEl) displayEl.style.display = 'inline-block';
    if (btnEl) btnEl.style.display = 'inline';
    if (errorEl) errorEl.style.display = 'none';
}

function saveTeamName(teamId) {
    const inputEl = document.getElementById('team-name-input-' + teamId);
    const newName = inputEl ? inputEl.value.trim() : '';

    if (!newName) {
        showError(teamId, 'name', 'Team name cannot be empty');
        return;
    }

    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    fetch('/teams/' + teamId + '/name', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            [csrfHeader]: csrfToken
        },
        body: 'newName=' + encodeURIComponent(newName)
    })
        .then(response => response.json())
        .then(data => {
            if (data.error) {
                showError(teamId, 'name', data.error);
            } else {
                const displayEl = document.getElementById('team-name-display-' + teamId);
                if (displayEl) displayEl.textContent = data.newName;
                cancelEditTeamName(teamId);
                showSuccessBanner('Team name updated successfully');
            }
        })
        .catch(error => {
            showError(teamId, 'name', 'An error occurred while updating the team name');
        });
}

// Team Description Edit Functions
function enableEditTeamDescription(teamId) {
    const displayEl = document.getElementById('team-description-display-' + teamId);
    const btnEl = document.getElementById('edit-description-btn-' + teamId);
    const editEl = document.getElementById('team-description-edit-' + teamId);

    if (displayEl) displayEl.style.display = 'none';
    if (btnEl) btnEl.style.display = 'none';
    if (editEl) editEl.style.display = 'block';
}

function cancelEditTeamDescription(teamId) {
    const displayEl = document.getElementById('team-description-display-' + teamId);
    const btnEl = document.getElementById('edit-description-btn-' + teamId);
    const editEl = document.getElementById('team-description-edit-' + teamId);
    const errorEl = document.getElementById('team-description-error-' + teamId);

    if (editEl) editEl.style.display = 'none';
    if (displayEl) displayEl.style.display = 'inline-block';
    if (btnEl) btnEl.style.display = 'inline';
    if (errorEl) errorEl.style.display = 'none';
}

function saveTeamDescription(teamId) {
    const inputEl = document.getElementById('team-description-input-' + teamId);
    const newDescription = inputEl ? inputEl.value.trim() : '';

    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    fetch('/teams/' + teamId + '/description', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            [csrfHeader]: csrfToken
        },
        body: 'newDescription=' + encodeURIComponent(newDescription)
    })
        .then(response => response.json())
        .then(data => {
            if (data.error) {
                showError(teamId, 'description', data.error);
            } else {
                const displayText = data.newDescription || 'No description provided';
                const displayEl = document.getElementById('team-description-display-' + teamId);
                if (displayEl) displayEl.textContent = displayText;
                cancelEditTeamDescription(teamId);
                showSuccessBanner('Team description updated successfully');
            }
        })
        .catch(error => {
            showError(teamId, 'description', 'An error occurred while updating the team description');
        });
}

// Password validation helpers
function validatePasswordField(value, fieldName) {
    if (!value) {
        return 'Please enter ' + fieldName;
    }
    return null;
}

function validatePasswordStrength(password) {
    if (password.length < 8) {
        return 'Password must be at least 8 characters';
    }

    if (!/[A-Z]/.test(password)) {
        return 'Password must contain at least one uppercase letter';
    }

    if (!/[a-z]/.test(password)) {
        return 'Password must contain at least one lowercase letter';
    }

    if (!/[!@#$%^&*()_+{}\[\]'"|/?.,><]/.test(password)) {
        return 'Password must contain at least one special character';
    }

    return null;
}

function validatePasswordMatch(newPass, confirmPass) {
    if (newPass !== confirmPass) {
        return 'New password and confirmation do not match';
    }
    return null;
}

// Password change function
function changePassword(teamId) {
    const currentPassEl = document.getElementById('current-password-' + teamId);
    const newPassEl = document.getElementById('new-password-' + teamId);
    const confirmPassEl = document.getElementById('confirm-password-' + teamId);
    const errorEl = document.getElementById('password-error-' + teamId);

    const currentPassword = currentPassEl ? currentPassEl.value : '';
    const newPassword = newPassEl ? newPassEl.value : '';
    const confirmPassword = confirmPassEl ? confirmPassEl.value : '';

    if (errorEl) errorEl.style.display = 'none';

    // Validation
    let error = validatePasswordField(currentPassword, 'your current password');
    if (!error) error = validatePasswordField(newPassword, 'a new password');
    if (!error) error = validatePasswordField(confirmPassword, 'password confirmation');
    if (!error) error = validatePasswordMatch(newPassword, confirmPassword);
    if (!error) error = validatePasswordStrength(newPassword);

    if (error) {
        if (errorEl) {
            errorEl.textContent = error;
            errorEl.style.display = 'block';
        }
        return;
    }

    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    fetch('/teams/' + teamId + '/password', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            [csrfHeader]: csrfToken
        },
        body: 'currentPassword=' + encodeURIComponent(currentPassword) +
            '&newPassword=' + encodeURIComponent(newPassword) +
            '&confirmPassword=' + encodeURIComponent(confirmPassword)
    })
        .then(response => response.json())
        .then(data => {
            if (data.error) {
                if (errorEl) {
                    errorEl.textContent = data.error;
                    errorEl.style.display = 'block';
                }
            } else {
                // Clear form
                if (currentPassEl) currentPassEl.value = '';
                if (newPassEl) newPassEl.value = '';
                if (confirmPassEl) confirmPassEl.value = '';

                showSuccessBanner('Team password changed successfully. New members will need to use the new password to join.');

                // Close details element
                const detailsEl = document.querySelector('.govuk-details');
                if (detailsEl) detailsEl.removeAttribute('open');
            }
        })
        .catch(error => {
            if (errorEl) {
                errorEl.textContent = 'An error occurred while changing the password';
                errorEl.style.display = 'block';
            }
        });
}

// Real-time password validation for change password form
function initPasswordValidation(teamId) {
    const newPassEl = document.getElementById('new-password-' + teamId);
    const confirmPassEl = document.getElementById('confirm-password-' + teamId);
    const currentPassEl = document.getElementById('current-password-' + teamId);
    const submitBtn = document.getElementById('change-password-btn-' + teamId);
    const matchHint = document.getElementById('password-match-hint-' + teamId);
    const matchStatus = document.getElementById('match-status-' + teamId);

    if (!newPassEl || !confirmPassEl || !submitBtn) {
        return;
    }

    const requirements = {
        length: {
            regex: /.{8,}/,
            element: document.getElementById('req-length-' + teamId)
        },
        uppercase: {
            regex: /[A-Z]/,
            element: document.getElementById('req-uppercase-' + teamId)
        },
        lowercase: {
            regex: /[a-z]/,
            element: document.getElementById('req-lowercase-' + teamId)
        },
        special: {
            regex: /[!@#$%^&*()_+{}\[\]'"|/?.,><]/,
            element: document.getElementById('req-special-' + teamId)
        }
    };

    function checkPasswordStrength() {
        const password = newPassEl.value;
        let allMet = true;

        for (const key in requirements) {
            const req = requirements[key];
            const met = req.regex.test(password);

            if (met) {
                req.element.innerHTML = '✓ ' + req.element.textContent.replace('✓ ', '');
                req.element.className = 'govuk-body-s govuk-!-font-weight-bold';
                req.element.style.color = '#00703c';
            } else {
                req.element.innerHTML = req.element.textContent.replace('✓ ', '');
                req.element.className = 'govuk-body-s';
                req.element.style.color = '#505a5f';
                allMet = false;
            }
        }

        return allMet;
    }

    function checkPasswordMatch() {
        const newPassword = newPassEl.value;
        const confirmPassword = confirmPassEl.value;

        if (confirmPassword.length === 0) {
            if (matchHint) matchHint.style.display = 'none';
            return false;
        }

        if (matchHint) matchHint.style.display = 'block';

        if (newPassword === confirmPassword) {
            if (matchStatus) {
                matchStatus.textContent = '✓ Passwords match';
                matchStatus.className = 'govuk-body-s govuk-!-font-weight-bold';
                matchStatus.style.color = '#00703c';
            }
            return true;
        } else {
            if (matchStatus) {
                matchStatus.textContent = '✗ Passwords do not match';
                matchStatus.className = 'govuk-body-s govuk-!-font-weight-bold';
                matchStatus.style.color = '#d4351c';
            }
            return false;
        }
    }

    function updateSubmitButton() {
        const currentPassword = currentPassEl ? currentPassEl.value : '';
        const strengthMet = checkPasswordStrength();
        const passwordsMatch = checkPasswordMatch();
        const hasCurrentPassword = currentPassword.length > 0;

        if (submitBtn) {
            submitBtn.disabled = !(strengthMet && passwordsMatch && hasCurrentPassword);
        }
    }

    newPassEl.addEventListener('input', updateSubmitButton);
    confirmPassEl.addEventListener('input', updateSubmitButton);
    if (currentPassEl) {
        currentPassEl.addEventListener('input', updateSubmitButton);
    }

    updateSubmitButton();
}

// Shared utility functions
function showError(teamId, field, message) {
    const errorEl = document.getElementById('team-' + field + '-error-' + teamId);
    if (errorEl) {
        errorEl.textContent = message;
        errorEl.style.display = 'block';
    }
}

function showSuccessBanner(message) {
    const banner = document.createElement('div');
    banner.className = 'govuk-notification-banner govuk-notification-banner--success';
    banner.setAttribute('role', 'alert');
    banner.innerHTML =
        '<div class="govuk-notification-banner__header">' +
        '<h2 class="govuk-notification-banner__title">Success</h2>' +
        '</div>' +
        '<div class="govuk-notification-banner__content">' +
        '<h3 class="govuk-notification-banner__heading">' + message + '</h3>' +
        '</div>';

    const mainContent = document.querySelector('.govuk-main-wrapper');
    if (mainContent) {
        mainContent.insertBefore(banner, mainContent.firstChild);
        setTimeout(() => banner.remove(), 5000);
    }
}

// Initialize password validation when page loads and details is opened
document.addEventListener('DOMContentLoaded', function() {
    const detailsElement = document.querySelector('.govuk-details');

    if (detailsElement) {
        // Get team ID from any element with data-team-id
        const teamIdEl = document.querySelector('[data-team-id]');
        const teamId = teamIdEl ? teamIdEl.getAttribute('data-team-id') : null;

        if (teamId) {
            // Initialize when details is opened
            detailsElement.addEventListener('toggle', function() {
                if (this.open) {
                    initPasswordValidation(teamId);
                }
            });

            // Also initialize if details is already open on page load
            if (detailsElement.open) {
                initPasswordValidation(teamId);
            }
        }
    }
});