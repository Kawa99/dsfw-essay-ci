function togglePasswordVisibility(inputId) {
    const input = document.getElementById(inputId);
    const icon = input.parentElement.querySelector('i.fa-eye') || input.parentElement.querySelector('i.fa-eye-slash');

    if (input.type === "password") {
        input.type = "text";
        icon.classList.remove('fa-eye');
        icon.classList.add('fa-eye-slash');
    } else {
        input.type = "password";
        icon.classList.remove('fa-eye-slash');
        icon.classList.add('fa-eye');
    }
}

/**
 * Validates the password length and updates the UI feedback.
 */
function checkPassword() {
    const passwordInput = document.getElementById('reg-password');
    // If we are on the login page, this element won't exist, so we return.
    if (!passwordInput) return;

    const feedbackText = document.getElementById('length-feedback');
    const submitBtn = document.getElementById('reg-submit-btn');
    const password = passwordInput.value;

    // Condition: Must be STRICTLY greater than 8 characters
    const isValid = password.length > 8;

    if (isValid) {
        // Visual Success State
        feedbackText.classList.remove('text-gray-500', 'text-red-500');
        feedbackText.classList.add('text-green-600');
        feedbackText.innerHTML = '<i class="fa-solid fa-check"></i> Length requirement met';

        passwordInput.classList.remove('focus:ring-indigo-500', 'border-red-500', 'focus:ring-red-500');
        passwordInput.classList.add('border-green-500', 'focus:ring-green-500');

        submitBtn.classList.remove('opacity-50', 'cursor-not-allowed');
        submitBtn.disabled = false;
    } else {
        // Visual Error/Default State
        if (password.length > 0) {
            feedbackText.classList.remove('text-gray-500', 'text-green-600');
            feedbackText.classList.add('text-red-500');
            passwordInput.classList.add('border-red-500', 'focus:ring-red-500');
            passwordInput.classList.remove('focus:ring-indigo-500', 'border-green-500', 'focus:ring-green-500');
        } else {
            feedbackText.classList.remove('text-red-500', 'text-green-600');
            feedbackText.classList.add('text-gray-500');
            passwordInput.classList.remove('border-red-500', 'focus:ring-red-500', 'border-green-500', 'focus:ring-green-500');
            passwordInput.classList.add('focus:ring-indigo-500');
        }

        feedbackText.innerHTML = `<i class="fa-solid fa-circle text-[6px]"></i> Must be more than 8 characters (Current: ${password.length})`;

        submitBtn.classList.add('opacity-50', 'cursor-not-allowed');
        submitBtn.disabled = true;
    }
}

/**
 * Prevents form submission if password is too short.
 */
function handleRegister(e) {
    const password = document.getElementById('reg-password').value;
    if(password.length <= 8) {
        e.preventDefault();
        alert("Password must be more than 8 characters!");
    }
}

// Initialize validation on load
document.addEventListener('DOMContentLoaded', function() {
    checkPassword();
});