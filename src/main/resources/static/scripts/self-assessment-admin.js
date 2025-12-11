function toggleNewTagInput(button) {
    const container = button.nextElementSibling;
    container.style.display =
        container.style.display === "none" ? "block" : "none";
}

document.addEventListener('DOMContentLoaded', function() {

    // ---------------------------------------------------------
    // 1. General UI (Notifications, Scroll, etc)
    // ---------------------------------------------------------
    const csrfToken = document.querySelector('meta[name="_csrf"]').content;
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

    // Auto-hide success banners
    const successBanner = document.querySelector('.govuk-notification-banner[style*="00703c"]');
    if (successBanner) {
        setTimeout(() => {
            successBanner.style.display = 'none';
        }, 5000);
    }

    // Scroll to error summary
    const errorSummary = document.querySelector('.govuk-error-summary');
    if (errorSummary) {
        errorSummary.scrollIntoView({ behavior: 'smooth' });
    }

    // ---------------------------------------------------------
    // 2. Sidebar Navigation
    // ---------------------------------------------------------
    const navItems = document.querySelectorAll('.sub-navigation__item');
    const sections = document.querySelectorAll('.category-section');

    navItems.forEach(item => {
        if(item.classList.contains('nav-divider-item')) return;

        const link = item.querySelector('a');
        if(link) {
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
        }
    });

    // Activate first category by default
    if (navItems.length > 0) {
        const firstLink = navItems[0].querySelector('a');
        if(firstLink) firstLink.click();
    }

    // ---------------------------------------------------------
    // 3. Inline Editing Toggles
    // ---------------------------------------------------------
    window.toggleEditMode = function(id, isEditing, type) {
        const nameSpan = document.getElementById(`${type}-name-${id}`);
        const editForm = document.getElementById(`${type}-edit-${id}`);

        if (!nameSpan || !editForm) return;

        if (isEditing) {
            nameSpan.style.display = 'none';
            editForm.style.display = 'block';

            // Initialize form logic if not done yet
            if (type === 'skill' && !editForm.dataset.initialized) {
                initSkillForm(editForm);
                editForm.dataset.initialized = 'true';
            }
        } else {
            editForm.style.display = 'none';
            nameSpan.style.display = 'inline';
        }
    };

    // Attach click handlers for edit buttons
    document.addEventListener('click', function(e) {
        // Edit Category
        if (e.target.closest('.edit-category-btn')) {
            e.preventDefault();
            const btn = e.target.closest('.edit-category-btn');
            toggleEditMode(btn.dataset.categoryId, true, 'cat');
        }
        // Cancel Category
        if (e.target.classList.contains('cancel-edit-category')) {
            toggleEditMode(e.target.dataset.categoryId, false, 'cat');
        }
        // Edit Skill
        if (e.target.closest('.edit-skill-btn')) {
            e.preventDefault();
            const btn = e.target.closest('.edit-skill-btn');
            toggleEditMode(btn.dataset.skillId, true, 'skill');
        }
        // Cancel Skill
        if (e.target.classList.contains('cancel-edit-skill')) {
            toggleEditMode(e.target.dataset.skillId, false, 'skill');
        }
        // Manage Categories Edit
        if (e.target.closest('.edit-category-manage-btn')) {
            e.preventDefault();
            const btn = e.target.closest('.edit-category-manage-btn');
            toggleEditMode(btn.dataset.categoryId, true, 'cat-manage');
        }
        // Cancel Manage Categories
        if (e.target.classList.contains('cancel-edit-category-manage')) {
            toggleEditMode(e.target.dataset.categoryId, false, 'cat-manage');
        }
    });

    // ---------------------------------------------------------
    // 4. Form Initialization Logic
    // ---------------------------------------------------------
    function initSkillForm(form) {
        const select = form.querySelector('.question-type-select');
        const optionsBuilder = form.querySelector('.options-builder');
        const addOptionBtn = form.querySelector('.add-option-btn');
        const optionInput = form.querySelector('.option-input');
        const table = form.querySelector('.options-table');
        const tableBody = form.querySelector('.options-table-body');
        const noOptionsMessage = form.querySelector('.no-options-message');
        const hiddenField = form.querySelector('.options-hidden-field');
        const recContainer = form.querySelector(".recommendation-block");

        // Load existing options
        let options = [];
        if (hiddenField && hiddenField.value.trim()) {
            options = hiddenField.value.split('\n').filter(opt => opt.trim());
        }

        // Logic to update UI based on Question Type
        function updateUI() {
            const selectedType = select.value;

            // Toggle Options Builder
            if (selectedType === 'MULTIPLE_CHOICE' || selectedType === 'DROPDOWN') {
                if(optionsBuilder) optionsBuilder.style.display = 'block';
                if(hiddenField) hiddenField.required = true;
            } else {
                if(optionsBuilder) optionsBuilder.style.display = 'none';
                if(hiddenField) hiddenField.required = false;
            }

            // Update Recommendations Block
            if (recContainer) {
                if (selectedType === "RATING_SCALE") {
                    buildRatingScaleRecommendations(recContainer);
                } else if (selectedType === "YES_NO") {
                    buildYesNoRecommendations(recContainer);
                } else if (selectedType === "MULTIPLE_CHOICE" || selectedType === "DROPDOWN") {
                    buildOptionRecommendations(recContainer, options);
                } else {
                    recContainer.innerHTML = "";
                }

                // Trigger animation for recommendation wrapper
                const wrapper = recContainer.closest(".recommendation-wrapper");
                if(wrapper) {
                    wrapper.classList.add("active");
                }
            }
        }

        function renderOptionsTable() {
            if (!hiddenField) return;
            hiddenField.value = options.join('\n');

            if (!table || !tableBody) return;

            if (options.length === 0) {
                table.classList.add('govuk-!-display-none');
                tableBody.innerHTML = '';
                if(noOptionsMessage) noOptionsMessage.style.display = 'block';
            } else {
                table.classList.remove('govuk-!-display-none');
                if(noOptionsMessage) noOptionsMessage.style.display = 'none';

                tableBody.innerHTML = options.map((opt, index) => `
                    <tr class="govuk-table__row">
                        <td class="govuk-table__cell">${index + 1}</td>
                        <td class="govuk-table__cell">${opt}</td>
                        <td class="govuk-table__cell">
                            <button type="button" class="govuk-button govuk-button--warning govuk-!-margin-bottom-0 remove-option-btn" data-index="${index}">Remove</button>
                        </td>
                    </tr>
                `).join('');

                // Re-attach remove listeners
                tableBody.querySelectorAll('.remove-option-btn').forEach(btn => {
                    btn.addEventListener('click', () => {
                        const idx = parseInt(btn.dataset.index);
                        options.splice(idx, 1);
                        renderOptionsTable();
                        updateUI();
                    });
                });
            }
        }

        // Event Listeners
        select.addEventListener('change', updateUI);

        if (addOptionBtn && optionInput) {
            const addOption = () => {
                const text = optionInput.value.trim();
                if (!text) return alert('Please enter a choice');
                if (options.includes(text)) return alert('Choice already exists');

                options.push(text);
                optionInput.value = '';
                renderOptionsTable();
                updateUI();
                optionInput.focus();
            };

            addOptionBtn.addEventListener('click', addOption);
            optionInput.addEventListener('keypress', (e) => {
                if (e.key === 'Enter') { e.preventDefault(); addOption(); }
            });
        }

        // Initial Run
        renderOptionsTable();
        updateUI();
    }

    // Initialize the "Add Skill" form immediately
    const addSkillForm = document.querySelector('.add-skill-form');
    if (addSkillForm) {
        initSkillForm(addSkillForm);
    }

    // ---------------------------------------------------------
    // 5. Deactivation Logic
    // ---------------------------------------------------------
    document.querySelectorAll('.deactivate-form').forEach(form => {
        form.addEventListener('submit', function(e) {
            const itemType = this.dataset.itemType || 'item';
            if (!confirm(`Are you sure you want to deactivate this ${itemType}? It will be hidden immediately.`)) {
                e.preventDefault();
            }
        });
    });

    // ---------------------------------------------------------
    // 6. Tag Management Logic
    // ---------------------------------------------------------
    // Create Tag
    document.addEventListener("click", function (e) {
        if (e.target.classList.contains("create-tag-btn")) {
            const container = e.target.closest(".new-tag-input");
            const input = container.querySelector(".new-tag-name");
            const tagName = input.value.trim();

            if (!tagName) return alert("Tag name cannot be empty.");

            fetch("/admin/self-assessment/tags/create", {
                method: "POST",
                headers: { "Content-Type": "application/json", [csrfHeader]: csrfToken },
                body: JSON.stringify({ name: tagName })
            })
                .then(res => { if(!res.ok) throw new Error("Failed"); return res.json(); })
                .then(tag => {
                    addTagToUI(tag);
                    input.value = "";
                    // Hide input box again
                    container.style.display = "none";
                })
                .catch(err => alert(err.message));
        }
    });

    // Toggle Tag Edit/Save
    document.addEventListener("click", function (e) {
        if (e.target.classList.contains("edit-tag-btn")) {
            const row = e.target.closest("tr");
            row.querySelector(".tag-name").style.display = "none";
            row.querySelector(".tag-edit-input").style.display = "inline-block";
            e.target.style.display = "none";
            row.querySelector(".save-tag-btn").style.display = "inline-block";
        }

        if (e.target.classList.contains("save-tag-btn")) {
            const row = e.target.closest("tr");
            const id = row.dataset.tagId;
            const newName = row.querySelector(".tag-edit-input").value.trim();

            fetch(`/admin/self-assessment/tags/${id}/edit`, {
                method: "POST",
                headers: { "Content-Type": "application/json", [csrfHeader]: csrfToken },
                body: JSON.stringify({ name: newName })
            })
                .then(res => res.json())
                .then(tag => {
                    row.querySelector(".tag-name").textContent = tag.name;
                    row.querySelector(".tag-name").style.display = "inline";
                    row.querySelector(".tag-edit-input").style.display = "none";
                    row.querySelector(".save-tag-btn").style.display = "none";
                    row.querySelector(".edit-tag-btn").style.display = "inline-block";
                });
        }

        if (e.target.classList.contains("deactivate-tag-btn")) {
            if(!confirm("Deactivate this tag?")) return;
            const row = e.target.closest("tr");
            const id = row.dataset.tagId;
            fetch(`/admin/self-assessment/tags/${id}/deactivate`, {
                method: "POST",
                headers: { [csrfHeader]: csrfToken }
            }).then(() => row.remove());
        }
    });

    function addTagToUI(tag) {
        // Add to checkbox lists
        document.querySelectorAll(".tag-list").forEach(list => {
            const div = document.createElement("div");
            div.innerHTML = `
                <label class="govuk-checkboxes__item">
                    <input type="checkbox" class="govuk-checkboxes__input tag-checkbox" name="tagIds" value="${tag.id}" />
                    <span class="govuk-checkboxes__label">${tag.name}</span>
                </label>`;
            list.appendChild(div);
        });

        // Add to manage table
        const tbody = document.querySelector(".manage-tags-table-body");
        if (tbody) {
            const row = document.createElement("tr");
            row.className = "govuk-table__row";
            row.dataset.tagId = tag.id;
            row.innerHTML = `
                <td class="govuk-table__cell">
                    <span class="tag-name">${tag.name}</span>
                    <input class="govuk-input tag-edit-input" type="text" value="${tag.name}" style="display:none; width: 200px;" />
                </td>
                <td class="govuk-table__cell">
                    <button type="button" class="govuk-button govuk-button--secondary edit-tag-btn" style="margin-right: 10px;">Edit</button>
                    <button type="button" class="govuk-button save-tag-btn" style="display:none; margin-right: 10px;">Save</button>
                    <button type="button" class="govuk-button govuk-button--warning deactivate-tag-btn">Deactivate</button>
                </td>`;
            tbody.appendChild(row);
        }
    }
});

// ---------------------------------------------------------
// NEW RECOMMENDATION GENERATORS (Updated)
// ---------------------------------------------------------

function buildRatingScaleRecommendations(container) {
    container.innerHTML = `
        <h4 class="govuk-heading-m govuk-!-margin-bottom-2">Provide resources to the question below</h4>
        <p class="govuk-body govuk-!-margin-bottom-6">
            Add learning resources for users based on their confidence score. 
            The appropriate link will be shared depending on the score chosen for that question.
        </p>
        
        <div class="govuk-form-group resource-group" style="background-color: #f3f2f1; padding: 15px; border-left: 5px solid #1d70b8; margin-bottom: 20px;">
            <h3 class="govuk-heading-s govuk-!-margin-bottom-1">Beginner Resources</h3>
            <span class="govuk-hint">Displayed for confidence scores of <strong>1 or 2</strong>.</span>
            <div class="url-container" id="container-BEGINNER">
                <input class="govuk-input govuk-!-margin-bottom-2" name="rec_BEGINNER" type="url" placeholder="https://..." autocomplete="off">
            </div>
            <button type="button" class="govuk-button govuk-button--secondary govuk-!-margin-bottom-0 add-link-btn" data-target="container-BEGINNER" data-name="rec_BEGINNER">
                Add another beginner link
            </button>
        </div>

        <div class="govuk-form-group resource-group" style="background-color: #f3f2f1; padding: 15px; border-left: 5px solid #1d70b8; margin-bottom: 20px;">
            <h3 class="govuk-heading-s govuk-!-margin-bottom-1">Intermediate Resources</h3>
            <span class="govuk-hint">Displayed for confidence score of <strong>3</strong>.</span>
            <div class="url-container" id="container-INTERMEDIATE">
                <input class="govuk-input govuk-!-margin-bottom-2" name="rec_INTERMEDIATE" type="url" placeholder="https://..." autocomplete="off">
            </div>
            <button type="button" class="govuk-button govuk-button--secondary govuk-!-margin-bottom-0 add-link-btn" data-target="container-INTERMEDIATE" data-name="rec_INTERMEDIATE">
                Add another intermediate link
            </button>
        </div>

        <div class="govuk-form-group resource-group" style="background-color: #f3f2f1; padding: 15px; border-left: 5px solid #1d70b8; margin-bottom: 20px;">
            <h3 class="govuk-heading-s govuk-!-margin-bottom-1">Advanced Resources</h3>
            <span class="govuk-hint">Displayed for confidence scores of <strong>4 or 5</strong>.</span>
            <div class="url-container" id="container-ADVANCED">
                <input class="govuk-input govuk-!-margin-bottom-2" name="rec_ADVANCED" type="url" placeholder="https://..." autocomplete="off">
            </div>
            <button type="button" class="govuk-button govuk-button--secondary govuk-!-margin-bottom-0 add-link-btn" data-target="container-ADVANCED" data-name="rec_ADVANCED">
                Add another advanced link
            </button>
        </div>
    `;

    // Attach event listeners for "Add Link" buttons
    const addButtons = container.querySelectorAll('.add-link-btn');
    addButtons.forEach(btn => {
        btn.addEventListener('click', function() {
            const targetId = this.dataset.target;
            const inputName = this.dataset.name;
            const targetContainer = container.querySelector(`#${targetId}`);

            // Create wrapper for input + remove button
            const wrapper = document.createElement('div');
            wrapper.className = "govuk-!-margin-bottom-2";
            wrapper.style.display = "flex";
            wrapper.style.gap = "10px";

            // Create input
            const input = document.createElement('input');
            input.className = "govuk-input";
            input.type = "url";
            input.name = inputName;
            input.placeholder = "https://...";
            input.autocomplete = "off";

            // Create remove button
            const removeBtn = document.createElement('button');
            removeBtn.type = "button";
            removeBtn.className = "govuk-button govuk-button--warning govuk-!-margin-bottom-0";
            removeBtn.innerText = "Remove";
            removeBtn.onclick = function() { wrapper.remove(); };

            wrapper.appendChild(input);
            wrapper.appendChild(removeBtn);
            targetContainer.appendChild(wrapper);
            input.focus();
        });
    });
}

function buildYesNoRecommendations(container) {
    container.innerHTML = `
        <h4 class="govuk-heading-s govuk-!-margin-bottom-2">Resource Recommendations</h4>
        <div class="govuk-form-group">
            <label class="govuk-label">If user answers <strong>YES</strong></label>
            <input class="govuk-input" name="rec_YES" type="url" placeholder="https://...">
        </div>
        <div class="govuk-form-group">
            <label class="govuk-label">If user answers <strong>NO</strong></label>
            <input class="govuk-input" name="rec_NO" type="url" placeholder="https://...">
        </div>
    `;
}

function buildOptionRecommendations(container, optionsArray) {
    container.innerHTML = `<h4 class="govuk-heading-s govuk-!-margin-bottom-2">Resource Recommendations</h4>`;

    if (!optionsArray || optionsArray.length === 0) {
        container.innerHTML += `<p class="govuk-hint">Add choices above to create recommendations.</p>`;
        return;
    }

    optionsArray.forEach(option => {
        const safeKey = option.replace(/[^a-zA-Z0-9-_]/g, "_");

        const block = document.createElement("div");
        block.classList.add("govuk-form-group");
        block.innerHTML = `
            <label class="govuk-label">If user selects: <strong>${option}</strong></label>
            <input class="govuk-input" name="rec_${safeKey}" type="url" placeholder="https://...">
        `;
        container.appendChild(block);
    });
}