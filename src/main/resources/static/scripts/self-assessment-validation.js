function getCategoryQuestions(sectionEl) {
    return Array.from(sectionEl.querySelectorAll(".govuk-form-group"));
}

// Returns true if ANy of the radio buttons inside this question are checked.
function isQuestionAnswered(questionEl) {
    return questionEl.querySelector("input[type='radio']:checked") !== null;
}

function getCategoryState(sectionEl) {
    const questions = getCategoryQuestions(sectionEl);
    const total = questions.length;
    let answered = 0;

    questions.forEach(q => {
        if (isQuestionAnswered(q)) answered++;
    });

    if (answered === 0) return "empty";
    if (answered === total) return "complete";
    return "partial";
}

// Fetch categories by state
function findPartialCategories() {
    return Array.from(document.querySelectorAll(".category-section"))
        .filter(section => getCategoryState(section) === "partial");
}

function findEmptyCategories() {
    return Array.from(document.querySelectorAll(".category-section"))
        .filter(section => getCategoryState(section) === "empty");
}

function findCompleteCategories() {
    return Array.from(document.querySelectorAll(".category-section"))
        .filter(section => getCategoryState(section) === "complete");
}

// Highlight sidebar entries for problematic categories
function highlightSidebar(categorySections) {
    clearSidebarHighlights();

    categorySections.forEach(section => {
        const id = section.id;
        const link = document.querySelector(`.js-tab-link[data-target="${id}"]`);
        if (link) link.classList.add("sidebar-warning");
    });
}

function clearSidebarHighlights() {
    document.querySelectorAll(".sidebar-warning")
        .forEach(link => link.classList.remove("sidebar-warning"));
}

// Highlight incomplete questions inside a category
function highlightIncompleteQuestions(sectionEl) {
    clearQuestionHighlights(sectionEl);

    const questions = getCategoryQuestions(sectionEl);

    questions.forEach(question => {
        if (!isQuestionAnswered(question)) {
            question.classList.add("question-warning");
        }
    });
}

function clearQuestionHighlights(sectionEl) {
    sectionEl.querySelectorAll(".question-warning")
        .forEach(q => q.classList.remove("question-warning"));
}

// Returns list of category names (for modal messages)
function getCategoryName(sectionEl) {
    return sectionEl.querySelector("h2").innerText.trim();
}

// Clear all radio inputs inside a category-section
function clearCategory(sectionEl) {
    const radios = sectionEl.querySelectorAll("input[type='radio']");

    // Load existing saved answers from localStorage
    const saved = JSON.parse(localStorage.getItem("selfAssessment") || "{}");
    saved.answers = saved.answers || {};

    radios.forEach(r => {
        // Uncheck in the UI
        r.checked = false;

        // If this radio corresponds to a skill, remove it from saved answers
        if (r.name && r.name.startsWith("answers[")) {
            const skillId = r.name.replace("answers[", "").replace("]", "");
            delete saved.answers[skillId];
        }
    });

    // Persist the updated saved answers (so cleared answers don't come back)
    localStorage.setItem("selfAssessment", JSON.stringify(saved));

    // Remove any warning highlights from this category
    clearQuestionHighlights(sectionEl);

    // Clear sidebar warning state
    clearSidebarHighlights();
}

document.addEventListener("DOMContentLoaded", () => {

    const tabLinks = document.querySelectorAll(".js-tab-link");
    const sections = document.querySelectorAll(".category-section");
    const nextButtons = document.querySelectorAll(".js-btn-next");

    function switchTab(targetId) {
        sections.forEach(section => {
            section.classList.add("js-hidden");
        });

        const targetSection = document.getElementById(targetId);
        if (targetSection) {
            targetSection.classList.remove("js-hidden");
            window.scrollTo({ top: 0, behavior: 'smooth' });
        }

        tabLinks.forEach(link => {
            if (link.getAttribute("data-target") === targetId) {
                link.setAttribute("aria-current", "page");
            } else {
                link.setAttribute("aria-current", "false");
            }
        });
        localStorage.setItem("selfAssessment.activeTab", targetId);
    }

    // Progres bar update
    function updateProgress() {
        const allQuestions = document.querySelectorAll(".govuk-form-group").length;
        const answered = document.querySelectorAll("input[type='radio']:checked").length;

        const percent = Math.round((answered / allQuestions) * 100);

        const bar = document.getElementById("progress-bar");
        const text = document.getElementById("progress-text");

        bar.style.width = percent + "%";
        text.textContent = `Progress: ${percent}% complete`;
    }

    // call on load
    updateProgress();

    // call whenever an answer changes
    document.querySelectorAll("input[type='radio']").forEach(r => {
        r.addEventListener("change", updateProgress);
    });



    // auto-saving answers
    document.querySelectorAll("input[type='radio']").forEach(radio => {
        radio.addEventListener("change", () => {
            const skillId = radio.name.replace("answers[", "").replace("]", "");
            const value = radio.value;

            const saved = JSON.parse(localStorage.getItem("selfAssessment") || "{}");

            saved.answers = saved.answers || {};
            saved.answers[skillId] = value;

            localStorage.setItem("selfAssessment", JSON.stringify(saved));
        });
    });

    // restores saved answers
    const savedData = JSON.parse(localStorage.getItem("selfAssessment") || "{}");

    if (savedData.answers) {
        Object.entries(savedData.answers).forEach(([skillId, value]) => {
            const radio = document.querySelector(`input[name="answers[${skillId}]"][value="${value}"]`);
            if (radio) radio.checked = true;
        });

        updateProgress();
    }

    // Sidebar click event
    tabLinks.forEach(link => {
        link.addEventListener("click", function (e) {
            e.preventDefault();
            const targetId = this.getAttribute("data-target");
            switchTab(targetId);
        });
    });

    // "Continue" button navigation
    nextButtons.forEach(btn => {
        btn.addEventListener("click", function () {
            const nextId = this.getAttribute("data-next-target");

            const currentSection = this.closest(".category-section");

            if (currentSection && typeof getCategoryState === "function") {
                const state = getCategoryState(currentSection);

                if (state === "partial") {
                    const catName = (typeof getCategoryName === "function")
                        ? getCategoryName(currentSection)
                        : "This category";

                    window.alert(
                        catName + " is incomplete.\n\n" +
                        "You can continue to the next section, but remember to either " +
                        "complete or clear this category before submitting."
                    );
                }
            }

            switchTab(nextId);
        });
    });

    // restores the active tab
    const savedTab = localStorage.getItem("selfAssessment.activeTab");
    if (savedTab) {
        switchTab(savedTab);
    }

    const prevButtons = document.querySelectorAll(".js-btn-prev");

    prevButtons.forEach(btn => {
        btn.addEventListener("click", function () {
            const prevId = this.getAttribute("data-prev-target");
            switchTab(prevId);
        });
    });

    const clearButtons = document.querySelectorAll(".clear-category-btn");

    clearButtons.forEach(btn => {
        btn.addEventListener("click", function() {
            const catId = this.getAttribute("data-category-id");
            const sectionEl = document.querySelector(`#cat-${catId}`);

            if (!sectionEl) return;

            const confirmClear = window.confirm(
                "Are you sure you want to clear all answers in this category?"
            );

            if (confirmClear) {
                clearCategory(sectionEl);
                updateProgress();
            }
        });
    });

    const form = document.getElementById("self-assessment-form");
    const errorSummary = document.getElementById("error-summary");
    const errorList = document.getElementById("error-summary-list");

    if (form) {
        form.addEventListener("submit", function (e) {

            // Clear server-side errors on re-validation
            if (!errorSummary.classList.contains("govuk-!-display-none")) {
                errorList.innerHTML = "";
            }

            clearSidebarHighlights();
            errorList.innerHTML = "";
            errorSummary.classList.add("govuk-!-display-none");

            const partialCategories = findPartialCategories();
            const completeCategories = findCompleteCategories();

            let errors = [];

            // Partial categories are invalid
            if (partialCategories.length > 0) {
                partialCategories.forEach(section => {
                    highlightIncompleteQuestions(section);
                    const catName = getCategoryName(section);
                    errors.push(`${catName} is incomplete`);
                });

                highlightSidebar(partialCategories);
            }

            // At least one complete category required
            if (completeCategories.length === 0) {
                errors.push("You must complete at least one category before submitting.");
            }

            // If no errors then allow submit
            if (errors.length === 0) {
                localStorage.removeItem("selfAssessment");
                localStorage.removeItem("selfAssessment.activeTab");
                return;
            }

            // Otherwise block submit
            e.preventDefault();

            errors.forEach(msg => {
                const li = document.createElement("li");
                li.innerText = msg;
                errorList.appendChild(li);
            });

            errorSummary.classList.remove("govuk-!-display-none");

            errorSummary.focus();
            window.scrollTo({ top: 0, behavior: "smooth" });
        });
    }

    if (errorSummary && errorSummary.dataset.serverError === "true") {
        const firstErrorLink = document.querySelector(".sub-navigation__link.sidebar-warning");

        if (firstErrorLink) {
            const targetId = firstErrorLink.getAttribute("data-target");
            const targetSection = document.getElementById(targetId);

            if (targetSection) {
                document.querySelectorAll(".category-section")
                    .forEach(sec => sec.classList.add("js-hidden"));

                targetSection.classList.remove("js-hidden");
            }

            document.querySelectorAll(".sub-navigation__link")
                .forEach(link => link.setAttribute("aria-current", "false"));

            firstErrorLink.setAttribute("aria-current", "page");

            errorSummary.focus();
            window.scrollTo({ top: 0, behavior: "smooth" });
        }
    }

    document.querySelectorAll(".question-warning fieldset")
        .forEach(fs => fs.classList.add("question-warning"));
});
