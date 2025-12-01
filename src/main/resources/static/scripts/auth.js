function togglePasswordVisibility(inputId) {
    const input = document.getElementById(inputId);
    const icon = input.parentElement.querySelector('button i');

    if (input.type === "password") {
        input.type = "text";
        icon.classList.replace('fa-eye', 'fa-eye-slash');
    } else {
        input.type = "password";
        icon.classList.replace('fa-eye-slash', 'fa-eye');
    }
}

function checkPassword() {
    const passwordInput = document.getElementById('reg-password');
    if (!passwordInput) return;

    const feedbackText = document.getElementById('length-feedback');
    const submitBtn = document.getElementById('reg-submit-btn');

    const minLength = passwordInput.getAttribute('minlength') || 9;
    const isValid = passwordInput.value.length >= minLength;

    if (isValid) {
        feedbackText.className = 'text-xs flex items-center gap-1 transition-colors duration-300 text-green-600';
        feedbackText.innerHTML = '<i class="fa-solid fa-check"></i> Length requirement met';

        passwordInput.classList.remove('border-red-500', 'focus:ring-red-500');
        passwordInput.classList.add('border-green-500', 'focus:ring-green-500');

        submitBtn.classList.remove('opacity-50', 'cursor-not-allowed');
        submitBtn.disabled = false;
    } else {
        feedbackText.className = 'text-xs flex items-center gap-1 transition-colors duration-300 text-red-500';
        feedbackText.innerHTML = `<i class="fa-solid fa-circle text-[6px]"></i> Must be ${minLength} characters or more`;

        passwordInput.classList.remove('border-green-500', 'focus:ring-green-500');
        passwordInput.classList.add('border-red-500', 'focus:ring-red-500');

        submitBtn.classList.add('opacity-50', 'cursor-not-allowed');
        submitBtn.disabled = true;
    }
}

document.addEventListener('DOMContentLoaded', function() {
    checkPassword();
});