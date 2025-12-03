// Password validation for team creation form
document.addEventListener('DOMContentLoaded', function() {
    const passwordInput = document.getElementById('password');
    const confirmInput = document.getElementById('passwordConfirm');
    const submitBtn = document.getElementById('submit-btn');
    const matchHint = document.getElementById('password-match-hint');
    const matchStatus = document.getElementById('match-status');

    // Check if elements exist
    if (!passwordInput || !confirmInput || !submitBtn) {
        return;
    }

    const requirements = {
        length: { regex: /.{8,}/, element: document.getElementById('req-length') },
        uppercase: { regex: /[A-Z]/, element: document.getElementById('req-uppercase') },
        lowercase: { regex: /[a-z]/, element: document.getElementById('req-lowercase') },
        number: { regex: /\d/, element: document.getElementById('req-number') },
        special: { regex: /[!@#$%^&*()_+=\/<>?;:'"\\|[\]{}~`-]/, element: document.getElementById('req-special') }
    };

    function checkPasswordStrength() {
        const password = passwordInput.value;
        let allMet = true;

        for (const [key, req] of Object.entries(requirements)) {
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
        const password = passwordInput.value;
        const confirm = confirmInput.value;

        if (confirm.length === 0) {
            matchHint.style.display = 'none';
            return false;
        }

        matchHint.style.display = 'block';

        if (password === confirm) {
            matchStatus.textContent = '✓ Passwords match';
            matchStatus.className = 'govuk-body-s govuk-!-font-weight-bold';
            matchStatus.style.color = '#00703c';
            return true;
        } else {
            matchStatus.textContent = '✗ Passwords do not match';
            matchStatus.className = 'govuk-body-s govuk-!-font-weight-bold';
            matchStatus.style.color = '#d4351c';
            return false;
        }
    }

    function updateSubmitButton() {
        const strengthMet = checkPasswordStrength();
        const passwordsMatch = checkPasswordMatch();

        submitBtn.disabled = !(strengthMet && passwordsMatch);
    }

    // Event listeners
    passwordInput.addEventListener('input', updateSubmitButton);
    confirmInput.addEventListener('input', updateSubmitButton);

    // Initialize on page load
    updateSubmitButton();
});