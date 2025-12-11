// ----------------------
// MOCK DATA
// ----------------------
const mockMemberData = {
    name: "Test Person",
    role: "Senior Developer",
    categories: [
        { label: "Communication", score: 4.0, comment: "Excellent stakeholder management" },
        { label: "Security", score: 2.0, comment: "Requires ISO training" },
        { label: "Digital Literacy", score: 5.0, comment: "Leading the department" },
        { label: "Teamwork", score: 3.0, comment: "Consistent contributor" },
        { label: "Problem Solving", score: 4.0, comment: "Strong analytical skills" }
    ]
};

const mockTeamAverages = [
    { label: "Communication", score: 3.0 },
    { label: "Security", score: 2.0 },
    { label: "Digital Literacy", score: 4.0 },
    { label: "Teamwork", score: 3.0 },
    { label: "Problem Solving", score: 3.0 }
];

// GDS Standard Colors
const GDS_BLUE = "#1d70b8";
const GDS_GREY = "#b1b4b6";
const GDS_BLACK = "#0b0c0c";
const GDS_RED = "#d4351c";
const GDS_GREEN = "#00703c";
const GDS_YELLOW = "#ffdd00";

// Helper to get tag class
function getTagHtml(score) {
    let color = "govuk-tag--red";
    let text = "Action";

    if (score >= 4.5) { color = "govuk-tag--blue"; text = "Expert"; }
    else if (score >= 3.5) { color = "govuk-tag--green"; text = "Good"; }
    else if (score >= 2.5) { color = "govuk-tag--yellow"; text = "Review"; }

    return `<strong class="govuk-tag ${color}">${text}</strong>`;
}

// ----------------------
// RENDER MEMBER RESULTS PAGE
// ----------------------
if (document.getElementById("memberRadarChart")) {

    document.getElementById("member-name-heading").innerText = mockMemberData.name;

    // Create the radar chart
    new Chart(document.getElementById("memberRadarChart"), {
        type: 'radar',
        data: {
            labels: mockMemberData.categories.map(c => c.label),
            datasets: [
                {
                    label: "Individual",
                    data: mockMemberData.categories.map(c => c.score),
                    backgroundColor: "rgba(29, 112, 184, 0.2)", // Blue transparent
                    borderColor: GDS_BLUE,
                    borderWidth: 2,
                    pointBackgroundColor: GDS_BLUE
                },
                {
                    label: "Team Average",
                    data: mockTeamAverages.map(c => c.score),
                    backgroundColor: "transparent",
                    borderColor: GDS_GREY,
                    borderWidth: 2,
                    borderDash: [5, 5], // Dotted line for benchmark
                    pointRadius: 0
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                r: {
                    angleLines: { color: '#e5e6e7' },
                    grid: { color: '#e5e6e7' },
                    pointLabels: {
                        font: { size: 12, family: "GDS Transport, arial, sans-serif" },
                        color: GDS_BLACK
                    },
                    suggestedMin: 0,
                    suggestedMax: 5,
                    ticks: { display: false, stepSize: 1 }
                }
            },
            plugins: {
                legend: { display: false } // We built a custom HTML legend
            }
        }
    });

    // Create Summary List
    const container = document.getElementById("member-score-list");

    mockMemberData.categories.forEach(cat => {
        container.innerHTML += `
            <div class="govuk-summary-list__row">
                <dt class="govuk-summary-list__key">
                    ${cat.label}
                </dt>
                <dd class="govuk-summary-list__value">
                    ${cat.score} / 5.0
                    <div class="govuk-body-s govuk-!-margin-bottom-0 text-grey">${cat.comment}</div>
                </dd>
                <dd class="govuk-summary-list__actions">
                    ${getTagHtml(cat.score)}
                </dd>
            </div>
        `;
    });
}

// ----------------------
// RENDER TEAM RESULTS PAGE
// ----------------------
if (document.getElementById("teamRadarChart")) {

    new Chart(document.getElementById("teamRadarChart"), {
        type: 'radar',
        data: {
            labels: mockTeamAverages.map(c => c.label),
            datasets: [{
                label: "Team Average",
                data: mockTeamAverages.map(c => c.score),
                backgroundColor: "rgba(29, 112, 184, 0.2)",
                borderColor: GDS_BLUE,
                borderWidth: 2,
                pointBackgroundColor: GDS_BLUE
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                r: {
                    angleLines: { color: '#e5e6e7' },
                    grid: { color: '#e5e6e7' },
                    pointLabels: {
                        font: { size: 12, family: "GDS Transport, arial, sans-serif" },
                        color: GDS_BLACK
                    },
                    suggestedMin: 0,
                    suggestedMax: 5,
                    ticks: { display: false, stepSize: 1 }
                }
            },
            plugins: { legend: { display: false } }
        }
    });

    const tableBody = document.getElementById("team-score-details-table");

    mockTeamAverages.forEach(cat => {
        tableBody.innerHTML += `
            <tr class="govuk-table__row">
                <th scope="row" class="govuk-table__header">${cat.label}</th>
                <td class="govuk-table__cell govuk-table__cell--numeric">${cat.score}</td>
                <td class="govuk-table__cell govuk-table__cell--numeric">
                    ${getTagHtml(cat.score)}
                </td>
            </tr>
        `;
    });
}