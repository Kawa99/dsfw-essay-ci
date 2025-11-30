document.addEventListener('DOMContentLoaded', function() {

    // Auto-hide success banners after 5 seconds
    const successBanner = document.querySelector('.govuk-notification-banner[style*="00703c"]');
    if (successBanner) {
        setTimeout(() => {
            successBanner.style.display = 'none';
        }, 5000);
    }

    // Scroll to error summary if present (GOV.UK bundle handles focus)
    const errorSummary = document.querySelector('.govuk-error-summary');
    if (errorSummary) {
        errorSummary.scrollIntoView({ behavior: 'smooth' });
    }

    // ---------------------------------------------------------
    // Sidebar Navigation Logic
    // ---------------------------------------------------------
    const navItems = document.querySelectorAll('.sub-navigation__item');
    const sections = document.querySelectorAll('.category-section');

    navItems.forEach(item => {
        if(item.classList.contains('nav-divider-item')) return;

        const link = item.querySelector('a');
        link.addEventListener('click', (e) => {
            e.preventDefault();

            // Update aria-current for accessibility
            navItems.forEach(nav => {
                const navLink = nav.querySelector('a');
                if(navLink) navLink.setAttribute('aria-current', 'false');
            });
            link.setAttribute('aria-current', 'page');

            // Show matching section
            const categoryId = item.dataset.categoryId;
            sections.forEach(section => {
                section.classList.remove('active');
                if (section.dataset.categoryId === categoryId) {
                    section.classList.add('active');
                }
            });
        });
    });

    // Activate first category by default
    if (navItems.length > 0) {
        const firstLink = navItems[0].querySelector('a');
        if(firstLink) firstLink.click();
    }

    // ---------------------------------------------------------
    // Inline Editing Logic
    // ---------------------------------------------------------
    function toggleEditMode(id, isEditing, type) {
        const nameSpan = document.getElementById(`${type}-name-${id}`);
        const editForm = document.getElementById(`${type}-edit-${id}`);

        if (!nameSpan || !editForm) return;

        if (isEditing) {
            nameSpan.style.display = 'none';
            editForm.style.display = 'block';
            editForm.querySelector('input')?.focus();
        } else {
            editForm.style.display = 'none';
            nameSpan.style.display = 'inline';
        }
    }

    // Category editing (in category view)
    document.querySelectorAll('.edit-category-btn').forEach(btn => {
        btn.addEventListener('click', function(e) {
            if (this.onclick) return; // Let inline onclick handle tab switching
            e.preventDefault();
            toggleEditMode(this.dataset.categoryId, true, 'cat');
        });
    });

    document.querySelectorAll('.cancel-edit-category').forEach(btn => {
        btn.addEventListener('click', () => {
            toggleEditMode(btn.dataset.categoryId, false, 'cat');
        });
    });

    // Skill editing
    document.querySelectorAll('.edit-skill-btn').forEach(btn => {
        btn.addEventListener('click', function(e) {
            e.preventDefault();
            toggleEditMode(this.dataset.skillId, true, 'skill');
        });
    });

    document.querySelectorAll('.cancel-edit-skill').forEach(btn => {
        btn.addEventListener('click', () => {
            toggleEditMode(btn.dataset.skillId, false, 'skill');
        });
    });

    // Category editing (in manage view)
    document.querySelectorAll('.edit-category-manage-btn').forEach(btn => {
        btn.addEventListener('click', function(e) {
            e.preventDefault();
            toggleEditMode(this.dataset.categoryId, true, 'cat-manage');
        });
    });

    document.querySelectorAll('.cancel-edit-category-manage').forEach(btn => {
        btn.addEventListener('click', () => {
            toggleEditMode(btn.dataset.categoryId, false, 'cat-manage');
        });
    });

    // ---------------------------------------------------------
    // Deactivation Confirmation
    // ---------------------------------------------------------
    document.querySelectorAll('.deactivate-form').forEach(form => {
        form.addEventListener('submit', function(e) {
            const itemType = this.dataset.itemType || 'item';
            if (!confirm(`Are you sure you want to deactivate this ${itemType}?\n\nThis will hide it from the user interface immediately.`)) {
                e.preventDefault();
            }
        });
    });
});