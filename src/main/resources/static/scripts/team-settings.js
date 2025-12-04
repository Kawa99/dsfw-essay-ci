// Team Name Edit Functions
function enableEdit(teamId, field) {
    const displayEl = document.getElementById(`team-${field}-display-${teamId}`);
    const btnEl = document.getElementById(`edit-${field}-btn-${teamId}`);
    const editEl = document.getElementById(`team-${field}-edit-${teamId}`);

    if (displayEl) displayEl.style.display = 'none';
    if (btnEl) btnEl.style.display = 'none';
    if (editEl) editEl.style.display = 'block';
}

function cancelEdit(teamId, field) {
    const displayEl = document.getElementById(`team-${field}-display-${teamId}`);
    const btnEl = document.getElementById(`edit-${field}-btn-${teamId}`);
    const editEl = document.getElementById(`team-${field}-edit-${teamId}`);
    const errorEl = document.getElementById(`team-${field}-error-${teamId}`);

    if (editEl) editEl.style.display = 'none';
    if (displayEl) displayEl.style.display = 'inline-block';
    if (btnEl) btnEl.style.display = 'inline';
    if (errorEl) errorEl.style.display = 'none';
}

function enableEditTeamName(teamId) { enableEdit(teamId, 'name'); }
function cancelEditTeamName(teamId) { cancelEdit(teamId, 'name'); }

function saveTeamField(teamId, field, value, emptyMessage) {
    const csrfToken = document.querySelector('meta[name="_csrf"]').content;
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

    fetch(`/teams/${teamId}/${field}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            [csrfHeader]: csrfToken
        },
        body: `new${field.charAt(0).toUpperCase() + field.slice(1)}=` + encodeURIComponent(value)
    })
        .then(response => response.json())
        .then(data => {
            if (data.error) {
                showError(teamId, field, data.error);
            } else {
                const displayEl = document.getElementById(`team-${field}-display-${teamId}`);
                const finalText = data[`new${field.charAt(0).toUpperCase() + field.slice(1)}`] || emptyMessage;

                if (displayEl) displayEl.textContent = finalText;

                cancelEdit(teamId, field);
                showSuccessBanner(`Team ${field} updated successfully`);
            }
        })
        .catch(() => {
            showError(teamId, field, `An error occurred while updating the team ${field}`);
        });
}

// Team Description Edit Functions
function enableEditTeamDescription(teamId) { enableEdit(teamId, 'description'); }
function cancelEditTeamDescription(teamId) { cancelEdit(teamId, 'description'); }

function saveTeamName(teamId) {
    const input = document.getElementById(`team-name-input-${teamId}`);
    const value = input ? input.value.trim() : '';

    if (!value) {
        showError(teamId, 'name', 'Team name cannot be empty');
        return;
    }

    saveTeamField(teamId, 'name', value, '');
}

function saveTeamDescription(teamId) {
    const input = document.getElementById(`team-description-input-${teamId}`);
    const value = input ? input.value.trim() : '';

    saveTeamField(teamId, 'description', value, 'No description provided');
}

function validatePasswordField(value, fieldName) {
    if (!value || value.trim().length === 0) {
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
    if (!/\d/.test(password)) {
        return 'Password must contain at least one number';
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
        number: {
            regex: /\d/,
            element: document.getElementById('req-number-' + teamId)
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

function showErrorBanner(message) {
    const banner = document.createElement('div');
    banner.className = 'govuk-notification-banner govuk-notification-banner--error';
    banner.setAttribute('role', 'alert');

    banner.innerHTML =
        '<div class="govuk-notification-banner__header">' +
        '<h2 class="govuk-notification-banner__title">Error</h2>' +
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

// Initialize password validation and wire up Change Password buttons
document.addEventListener('DOMContentLoaded', function () {
    // Find all Change Password buttons on the page
    const buttons = document.querySelectorAll('button[id^="change-password-btn-"]');

    buttons.forEach(button => {
        const teamId = button.getAttribute('data-team-id');
        if (!teamId) {
            return;
        }
        // Initialise real-time validation for this team's password fields
        initPasswordValidation(teamId);

        // Wire up the click handler to call changePassword with the correct teamId
        button.addEventListener('click', function () {
            changePassword(teamId);
        });
    });
});

document.addEventListener("DOMContentLoaded", () => {
    const removeButtons = document.querySelectorAll(".remove-member-btn");

    removeButtons.forEach(btn => {
        btn.addEventListener("click", async () => {

            const userId = btn.getAttribute("data-user-id");
            const teamId = btn.getAttribute("data-team-id");

            if (!confirm("Are you sure you want to remove this team member?")) {
                return;
            }

            try {
                const csrfToken = document.querySelector('meta[name="_csrf"]').content;
                const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

                const response = await fetch(`/manager/team/${teamId}/remove/${userId}`, {
                    method: "DELETE",
                    headers: {
                        "X-Requested-With": "XMLHttpRequest",
                        [csrfHeader]: csrfToken
                    }
                });


                const result = await response.json();

                if (response.ok) {
                    const row = btn.closest("tr");
                    if (row) row.remove();

                    showSuccessBanner("Team member removed successfully.");
                } else {
                    showErrorBanner(result.error || "Failed to remove member.");
                }

            } catch (error) {
                console.error("Error removing member:", error);
                alert("An unexpected error occurred.");
            }
        });
    });
});
