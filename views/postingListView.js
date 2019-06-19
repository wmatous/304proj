// displaying a grid of postings (either all postings or a subset that have been searched for)

getAllPostings = function () {
    // or do i need to combine with search here
    let urlPath = 'http://localhost:6789/allPostings/';
    fetch(urlPath)
        .then((res) => res.json())
        .then(function (data) {
            console.log(data);
            populatePostingTable(data);
        })
        .catch((err) => console.error(err));
};

function postingTemplate(posting) {
    return `
        <tr class="postingTableRow">
            <div>
                <th scope="row"> ${posting.postingId}</th>
                <td><a href="http://localhost:6789/application/"+${posting.accountId}+${posting.postingId} style="color: blue; text-decoration: underline"> ${posting.title} </a></td>
                <td>${posting.active}</td>
                <td>${posting.startDate}</td>
                <td>${posting.address}</td>
                <td>${posting.postalCode}</td>
                <td>${posting.cityName}</td>
                <td>${posting.state}</td>
                <td>${posting.description}</td>
                <td id="skills">${posting.skills}</td>
            </div>
        </tr>`;
}

//postingData is array of interview json objects
//map iterates through and calls postingTemplate on each one
//join removes commas between array elements
function populatePostingTable(postingData) {
    document.getElementById("postingTable").innerHTML = `${postingData.map(postingTemplate).join('')}`;
}

populateSkills = function (skills) {
    let innerHTML = '<h1>Skills</h1>';
    for (let i = 0; i < skills.length; i++) {
        innerHTML += "<span>" + skills[i].name + "</span>";
    }
    document.getElementById('skills').innerHTML = innerHTML;
};

getSkills = function (postingId) {
    let urlPath = 'http://localhost:6789/postingSkill/' + postingId;
    fetch(urlPath)
        .then((res) => res.json())
        .then(function (data) {
            console.log(data);
            populateSkills(data);
        })
        .catch((err) => console.error(err));
};

window.onload = function () {
    var url = new URL(window.location.href);
    getAllPostings();
    getSkills();
};