function togglePasswordVisibility(inputId, labelId) {
    const input = document.getElementById(inputId);
    if (!input) return;

    const isHidden = input.type === "password";
    input.type = isHidden ? "text" : "password";

    const label = labelId ? document.getElementById(labelId) : null;
    if (label) {
        label.textContent = isHidden ? "Hide password" : "Show password";
    }
}

function checkPassword() {
    const passwordInput = document.getElementById("reg-password");
    const feedbackText = document.getElementById("length-feedback");
    const submitBtn = document.getElementById("reg-submit-btn");

    if (!passwordInput || !feedbackText || !submitBtn) return;

    const minLength = Number(passwordInput.getAttribute("minlength")) || 9;
    const isValid = passwordInput.value.trim().length >= minLength;

    if (isValid) {
        feedbackText.className = "govuk-hint govuk-!-margin-top-1 govuk-!-margin-bottom-0";
        feedbackText.textContent = "Password length looks good.";
        passwordInput.classList.remove("govuk-input--error");
        submitBtn.disabled = false;
        submitBtn.removeAttribute("aria-disabled");
    } else {
        feedbackText.className = "govuk-error-message govuk-!-margin-top-1";
        feedbackText.innerHTML = '<span class="govuk-visually-hidden">Error:</span> Must be at least ' + minLength + " characters.";
        passwordInput.classList.add("govuk-input--error");
        submitBtn.disabled = true;
        submitBtn.setAttribute("aria-disabled", "true");
    }
}

document.addEventListener("DOMContentLoaded", checkPassword);
