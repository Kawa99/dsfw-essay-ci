document.addEventListener('DOMContentLoaded', function() {
    // Creates popup messages to show success/error messages to users
    function showAlert(message, type) {
        const alertBox = document.getElementById('alertBox');
        if (!alertBox) return;

        alertBox.textContent = message;
        alertBox.className = `alert alert-${type}`;
        alertBox.style.display = 'block';
        setTimeout(() => {
            alertBox.style.display = 'none';
        }, 3000);
    }

    // Handles clicking between different category sections
    const navItems = document.querySelectorAll('.nav-item');
    const sections = document.querySelectorAll('.category-section');

    navItems.forEach(item => {
        item.addEventListener('click', () => {
            // Removes active class from all navigation items
            navItems.forEach(nav => nav.classList.remove('active'));
            // Adds active class to the clicked item
            item.classList.add('active');

            // Shows the content section that matches the clicked nav item
            const categoryId = item.dataset.categoryId;
            sections.forEach(section => {
                section.classList.remove('active');
                if (section.dataset.categoryId === categoryId) {
                    section.classList.add('active');
                }
            });
        });
    });

    // Show first category by default if exists
    if (navItems.length > 0) {
        navItems[0].click();
    }

    // Handle edit category inline
    document.querySelectorAll('.edit-category-btn').forEach(btn => {
        btn.addEventListener('click', function() {
            const categoryId = this.dataset.categoryId;
            const nameSpan = document.getElementById(`cat-name-${categoryId}`);
            const editForm = document.getElementById(`cat-edit-${categoryId}`);

            nameSpan.style.display = 'none';
            editForm.style.display = 'inline';
            editForm.querySelector('input').focus();
        });
    });

    // Cancel edit category
    document.querySelectorAll('.cancel-edit-category').forEach(btn => {
        btn.addEventListener('click', function(e) {
            e.preventDefault();
            const categoryId = this.dataset.categoryId;
            const nameSpan = document.getElementById(`cat-name-${categoryId}`);
            const editForm = document.getElementById(`cat-edit-${categoryId}`);

            editForm.style.display = 'none';
            nameSpan.style.display = 'inline';
        });
    });

    // Handle edit skill inline
    document.querySelectorAll('.edit-skill-btn').forEach(btn => {
        btn.addEventListener('click', function() {
            const skillId = this.dataset.skillId;
            const nameSpan = document.getElementById(`skill-name-${skillId}`);
            const editForm = document.getElementById(`skill-edit-${skillId}`);

            nameSpan.style.display = 'none';
            editForm.style.display = 'inline';
            editForm.querySelector('input').focus();
        });
    });

    // Cancel edit skill
    document.querySelectorAll('.cancel-edit-skill').forEach(btn => {
        btn.addEventListener('click', function(e) {
            e.preventDefault();
            const skillId = this.dataset.skillId;
            const nameSpan = document.getElementById(`skill-name-${skillId}`);
            const editForm = document.getElementById(`skill-edit-${skillId}`);

            editForm.style.display = 'none';
            nameSpan.style.display = 'inline';
        });
    });

    // Confirmation for deactivate actions
    document.querySelectorAll('.deactivate-form').forEach(form => {
        form.addEventListener('submit', function(e) {
            const itemType = this.dataset.itemType || 'item';
            if (!confirm(`Are you sure you want to deactivate this ${itemType}?`)) {
                e.preventDefault();
            }
        });
    });
});