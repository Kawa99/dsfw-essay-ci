function toggleNewTagInput(button) {
    const container = button.nextElementSibling;
    container.style.display =
        container.style.display === "none" ? "block" : "none";
}

document.addEventListener('DOMContentLoaded', function() {

    const csrfToken = document.querySelector('meta[name="_csrf"]').content;
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

    // Auto-hide success banners after 5 seconds
    const successBanner = document.querySelector('.govuk-notification-banner[style*="00703c"]');
    if (successBanner) {
        setTimeout(() => {
            successBanner.style.display = 'none';
        }, 5000);
    }

    // Scroll to error summary if present
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

            navItems.forEach(nav => {
                const navLink = nav.querySelector('a');
                if(navLink) navLink.setAttribute('aria-current', 'false');
            });
            link.setAttribute('aria-current', 'page');

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
            editForm.querySelector('input, textarea')?.focus();
        } else {
            editForm.style.display = 'none';
            nameSpan.style.display = 'inline';
        }
    }

    document.querySelectorAll('.edit-category-btn').forEach(btn => {
        btn.addEventListener('click', function(e) {
            if (this.onclick) return;
            e.preventDefault();
            toggleEditMode(this.dataset.categoryId, true, 'cat');
        });
    });

    document.querySelectorAll('.cancel-edit-category').forEach(btn => {
        btn.addEventListener('click', () => {
            toggleEditMode(btn.dataset.categoryId, false, 'cat');
        });
    });

    document.querySelectorAll('.edit-skill-btn').forEach(btn => {
        btn.addEventListener('click', function(e) {
            e.preventDefault();
            const skillId = this.dataset.skillId;
            toggleEditMode(skillId, true, 'skill');

            // Initialize options builder for this specific form when edit is clicked
            const form = document.getElementById(`skill-edit-${skillId}`);
            if (form && !form.dataset.optionsInitialized) {
                initializeEditFormOptionsBuilder(form);
                form.dataset.optionsInitialized = 'true';
            }
        });
    });

    document.querySelectorAll('.cancel-edit-skill').forEach(btn => {
        btn.addEventListener('click', () => {
            toggleEditMode(btn.dataset.skillId, false, 'skill');
        });
    });

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

    // ---------------------------------------------------------
    // Dynamic Options Builder for Multiple Choice / Dropdown
    // ---------------------------------------------------------
    document.querySelectorAll('.add-skill-form .question-type-select').forEach(select => {
        const form = select.closest('form');
        const optionsBuilder = form.querySelector('.options-builder');
        const addOptionBtn = form.querySelector('.add-option-btn');
        const optionInput = form.querySelector('.option-input');
        const table = form.querySelector('.options-table');
        const tableBody = form.querySelector('.options-table-body');
        const noOptionsMessage = form.querySelector('.no-options-message');
        const hiddenField = form.querySelector('.options-hidden-field');


        let options = [];

        // Show/hide options builder based on question type
        select.addEventListener('change', function() {
            const selectedType = this.value;

            // ------------------------------------
            // OPTIONS BUILDER TOGGLE
            // ------------------------------------
            if (selectedType === 'MULTIPLE_CHOICE' || selectedType === 'DROPDOWN') {
                optionsBuilder.style.display = 'block';
                hiddenField.required = true;
            } else {
                optionsBuilder.style.display = 'none';
                hiddenField.required = false;
                options = [];
                updateOptionsDisplay();
            }
        });

        // Add option button click
        if (addOptionBtn) {
            addOptionBtn.addEventListener('click', function() {
                const optionText = optionInput.value.trim();

                if (optionText === '') {
                    alert('Please enter a choice');
                    return;
                }

                if (options.includes(optionText)) {
                    alert('This choice already exists');
                    return;
                }

                options.push(optionText);
                optionInput.value = '';
                optionInput.focus();
                updateOptionsDisplay();
                const recContainer = form.querySelector(".recommendation-block");
                buildOptionRecommendations(recContainer, options);
            });
        }

        // Allow Enter key to add option
        if (optionInput) {
            optionInput.addEventListener('keypress', function(e) {
                if (e.key === 'Enter') {
                    e.preventDefault();
                    addOptionBtn.click();
                }
            });
        }

        function updateOptionsDisplay() {
            hiddenField.value = options.join('\n');

            // If no options then hide table, show hint
            if (options.length === 0) {
                table.classList.add('govuk-!-display-none');
                tableBody.innerHTML = '';
                noOptionsMessage.style.display = 'block';
                return;
            }

            // Show table
            table.classList.remove('govuk-!-display-none');
            noOptionsMessage.style.display = 'none';

            // Build rows
            tableBody.innerHTML = options.map((opt, index) => `
        <tr class="govuk-table__row">
            <td class="govuk-table__cell">${index + 1}</td>
            <td class="govuk-table__cell">${opt}</td>
            <td class="govuk-table__cell">
                <button type="button"
                        class="govuk-button govuk-button--warning govuk-!-margin-bottom-0 remove-option-btn"
                        data-index="${index}">
                    Remove
                </button>
            </td>
        </tr>
    `).join('');

            // Add remove handlers
            tableBody.querySelectorAll('.remove-option-btn').forEach(btn => {
                btn.addEventListener('click', () => {
                    const index = parseInt(btn.dataset.index);
                    options.splice(index, 1);
                    updateOptionsDisplay();

                    const recContainer = form.querySelector(".recommendation-block");
                    buildOptionRecommendations(recContainer, options);
                });
            });
        }

        // Form validation
        form.addEventListener('submit', function(e) {
            const selectedType = select.value;
            if ((selectedType === 'MULTIPLE_CHOICE' || selectedType === 'DROPDOWN') && options.length === 0) {
                e.preventDefault();
                alert('Please add at least one choice for this question type');
                optionInput.focus();
            }
        });
    });

    // ---------------------------------------------------------
    // Initialize Options Builder for EDIT forms
    // ---------------------------------------------------------
    document.querySelectorAll('.edit-skill-form').forEach(form => {
        const select = form.querySelector('.question-type-select');
        const optionsBuilder = form.querySelector('.options-builder');
        const addOptionBtn = form.querySelector('.add-option-btn');
        const optionInput = form.querySelector('.option-input');
        const table = form.querySelector('.options-table');
        const tableBody = form.querySelector('.options-table-body');
        const noOptionsMessage = form.querySelector('.no-options-message');
        const hiddenField = form.querySelector('.options-hidden-field');

        // Parse existing options from hidden field
        let options = [];
        if (hiddenField && hiddenField.value.trim()) {
            options = hiddenField.value.split('\n').filter(opt => opt.trim());
        }

        // Show/hide options builder based on question type
        function updateOptionsVisibility() {
            const selectedType = select.value;
            if (selectedType === 'MULTIPLE_CHOICE' || selectedType === 'DROPDOWN') {
                optionsBuilder.style.display = 'block';
                if (hiddenField) hiddenField.required = true;
            } else {
                optionsBuilder.style.display = 'none';
                if (hiddenField) hiddenField.required = false;
            }
        }

        // ----------------------------------------------------
// INITIALIZE RECOMMENDATION BLOCK (EDIT FORM)
// ----------------------------------------------------
        const recContainer = form.querySelector(".recommendation-block");

        function updateRecommendationBlock() {
            const selectedType = select.value;

            if (selectedType === "RATING_SCALE") {
                buildRatingScaleRecommendations(recContainer);
            }
            else if (selectedType === "YES_NO") {
                buildYesNoRecommendations(recContainer);
            }
            else if (selectedType === "MULTIPLE_CHOICE" || selectedType === "DROPDOWN") {
                const optionsList = hiddenField.value.trim() ? hiddenField.value.split("\n") : [];
                buildOptionRecommendations(recContainer, optionsList);
            }
            else {
                recContainer.innerHTML = "";
                const wrapper = recContainer.closest(".recommendation-wrapper");
                wrapper.classList.remove("active");
                wrapper.classList.add("active");
            }
        }

        updateOptionsVisibility();
        updateOptionsDisplay();
        updateRecommendationBlock();

        select.addEventListener('change', updateOptionsVisibility);

        // Initialize on load
        updateOptionsVisibility();
        if (options.length > 0) {
            updateOptionsDisplay();
        }

        // Add option button click
        if (addOptionBtn) {
            addOptionBtn.addEventListener('click', function() {

                console.log('Options after re-read:', options);
                console.log('Hidden field value:', hiddenField.value);

                const optionText = optionInput.value.trim();

                if (optionText === '') {
                    return;
                }

                if (options.includes(optionText)) {
                    return;
                }

                options.push(optionText);
                optionInput.value = '';
                optionInput.focus();
                updateOptionsDisplay();
                const recContainer = form.querySelector(".recommendation-block");
                updateRecommendationBlock();
            });
        }

        // Allow Enter key to add option
        if (optionInput) {
            optionInput.addEventListener('keypress', function(e) {
                if (e.key === 'Enter') {
                    e.preventDefault();
                    addOptionBtn.click();
                }
            });
        }

        function updateOptionsDisplay() {
            hiddenField.value = options.join('\n');

            // if no options then hide table, show hint
            if (options.length === 0) {
                table.classList.add('govuk-!-display-none');
                tableBody.innerHTML = '';
                noOptionsMessage.style.display = 'block';
                return;
            }

            // Show table
            table.classList.remove('govuk-!-display-none');
            noOptionsMessage.style.display = 'none';

            // Build rows
            tableBody.innerHTML = options.map((opt, index) => `
        <tr class="govuk-table__row">
            <td class="govuk-table__cell">${index + 1}</td>
            <td class="govuk-table__cell">${opt}</td>
            <td class="govuk-table__cell">
                <button type="button"
                        class="govuk-button govuk-button--warning govuk-!-margin-bottom-0 remove-option-btn"
                        data-index="${index}">
                    Remove
                </button>
            </td>
        </tr>
    `).join('');

            // Add remove handlers
            tableBody.querySelectorAll('.remove-option-btn').forEach(btn => {
                btn.addEventListener('click', () => {
                    const recContainer = form.querySelector(".recommendation-block");
                    buildOptionRecommendations(recContainer, options);
                });
            });
        }

        // Form validation
        form.addEventListener('submit', function(e) {
            const selectedType = select.value;
            if ((selectedType === 'MULTIPLE_CHOICE' || selectedType === 'DROPDOWN') && options.length === 0) {
                e.preventDefault();
                alert('Please add at least one choice for this question type');
                optionInput.focus();
            }
        });
    });

    document.addEventListener("click", function (e) {
        if (e.target.classList.contains("create-tag-btn")) {
            const input = e.target.closest(".new-tag-input").querySelector(".new-tag-name");
            const tagName = input.value.trim();

            if (!tagName) {
                alert("Tag name cannot be empty.");
                return;
            }

            fetch("/admin/self-assessment/tags/create", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    [csrfHeader]: csrfToken
                },
                body: JSON.stringify({ name: tagName })
            })
                .then(res => {
                    if (!res.ok) throw new Error("Failed to create tag");
                    return res.json();
                })
                .then(tag => {
                    addTagCheckboxToUI(tag);
                    addTagToManageTable(tag);  // <<< NEW
                    input.value = "";
                })
                .catch(err => alert(err.message));
        }
    });

    function addTagCheckboxToUI(tag) {
        const tagLists = document.querySelectorAll(".tag-list");

        tagLists.forEach(list => {
            const wrapper = document.createElement("div");
            wrapper.innerHTML = `
            <label class="govuk-checkboxes__item">
                <input type="checkbox"
                       class="govuk-checkboxes__input tag-checkbox"
                       name="tagIds"
                       value="${tag.id}" />
                <span class="govuk-checkboxes__label">${tag.name}</span>
            </label>
        `;
            list.appendChild(wrapper);
        });
    }

    document.addEventListener("click", function (e) {

        if (e.target.classList.contains("edit-tag-btn")) {
            const row = e.target.closest("tr[data-tag-id]");
            row.querySelector(".tag-name").style.display = "none";
            row.querySelector(".tag-edit-input").style.display = "inline-block";
            e.target.style.display = "none";
            row.querySelector(".save-tag-btn").style.display = "inline-block";
        }
    });

    document.addEventListener("click", function (e) {
        if (e.target.classList.contains("save-tag-btn")) {

            const row = e.target.closest("tr[data-tag-id]");
            const id = row.getAttribute("data-tag-id");
            const newName = row.querySelector(".tag-edit-input").value.trim();

            fetch(`/admin/self-assessment/tags/${id}/edit`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    [csrfHeader]: csrfToken
                },
                body: JSON.stringify({ name: newName })
            })
                .then(res => res.json())
                .then(tag => {
                    row.querySelector(".tag-name").textContent = tag.name;

                    // restore UI
                    row.querySelector(".tag-name").style.display = "inline";
                    row.querySelector(".tag-edit-input").style.display = "none";
                    row.querySelector(".save-tag-btn").style.display = "none";
                    row.querySelector(".edit-tag-btn").style.display = "inline-block";
                })
                .catch(err => alert("Error: " + err.message));
        }
    });

    document.addEventListener("click", function (e) {
        if (e.target.classList.contains("deactivate-tag-btn")) {

            const row = e.target.closest("tr[data-tag-id]");
            const id = row.getAttribute("data-tag-id");

            fetch(`/admin/self-assessment/tags/${id}/deactivate`, {
                method: "POST",
                headers: {
                    [csrfHeader]: csrfToken
                }
            })
                .then(() => {
                    row.remove();
                    // Also remove tag from all tag-lists in skill forms
                    document.querySelectorAll(`input.tag-checkbox[value="${id}"]`)
                        .forEach(cb => cb.closest("label").remove());
                })
                .catch(err => alert("Error: " + err.message));
        }
    });

    function addTagToManageTable(tag) {
        const tbody = document.querySelector(".manage-tags-table-body");
        if (!tbody) return;

        const row = document.createElement("tr");
        row.classList.add("govuk-table__row");
        row.setAttribute("data-tag-id", tag.id);

        row.innerHTML = `
        <td class="govuk-table__cell">
            <span class="tag-name">${tag.name}</span>
            <input class="govuk-input tag-edit-input"
                   type="text"
                   value="${tag.name}"
                   style="display:none; width:200px;" />
        </td>

        <td class="govuk-table__cell">
            <button type="button"
                    class="govuk-button govuk-button--secondary edit-tag-btn"
                    style="margin-right: 10px;">
                Edit
            </button>

            <button type="button"
                    class="govuk-button save-tag-btn"
                    style="display:none; margin-right: 10px;">
                Save
            </button>

            <button type="button"
                    class="govuk-button govuk-button--warning deactivate-tag-btn">
                Deactivate
            </button>
        </td>
    `;

        tbody.appendChild(row);
    }
});

// ---------------------------------------------------------
// RECOMMENDATION FIELD GENERATORS (Option A)
// ---------------------------------------------------------

function buildRatingScaleRecommendations(container) {
    container.innerHTML = `
        <div class="govuk-form-group">
            <label class="govuk-label">Low score recommendation</label>
            <textarea class="govuk-textarea" name="rec_LOW" rows="2"></textarea>
        </div>

        <div class="govuk-form-group">
            <label class="govuk-label">Medium score recommendation</label>
            <textarea class="govuk-textarea" name="rec_MEDIUM" rows="2"></textarea>
        </div>

        <div class="govuk-form-group">
            <label class="govuk-label">High score recommendation</label>
            <textarea class="govuk-textarea" name="rec_HIGH" rows="2"></textarea>
        </div>
    `;
}

function buildYesNoRecommendations(container) {
    container.innerHTML = `
        <div class="govuk-form-group">
            <label class="govuk-label">YES recommendation</label>
            <textarea class="govuk-textarea" name="rec_YES" rows="2"></textarea>
        </div>

        <div class="govuk-form-group">
            <label class="govuk-label">NO recommendation</label>
            <textarea class="govuk-textarea" name="rec_NO" rows="2"></textarea>
        </div>
    `;
}

function buildOptionRecommendations(container, optionsArray) {
    container.innerHTML = "";

    if (!optionsArray || optionsArray.length === 0) {
        container.innerHTML = `<p class="govuk-hint">Add options first to create recommendations.</p>`;
        return;
    }

    optionsArray.forEach(option => {
        const safeKey = option.replace(/\s+/g, "_"); // convert spaces to underscores

        const block = document.createElement("div");
        block.classList.add("govuk-form-group");
        block.innerHTML = `
            <label class="govuk-label">Recommendation for: <strong>${option}</strong></label>
            <textarea class="govuk-textarea" name="rec_${safeKey}" rows="2"></textarea>
        `;

        container.appendChild(block);
    });
}

