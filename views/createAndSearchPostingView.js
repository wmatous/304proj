// for a single posting on a page (when creating a posting or searching for a posting)

getPosting = function (postingId) {
    let urlPath = 'http://localhost:6789/posting/' + postingId;
    fetch(urlPath)
        .then((res) => res.json())
        .then(function (data) {
            console.log(data);
            populateSinglePostingView(data);
        })
        .catch((err) => console.error(err));
};

populateSinglePostingView = function (postings) {
    var posting = postings[0];
    document.getElementById('postingId').value = posting.postingId;
    document.getElementById('title').value = posting.title;
    document.getElementById('active').value = posting.active;
    document.getElementById('startDate').value = posting.startDate;
    document.getElementById('address').value = posting.address;
    document.getElementById('postalCode').value = posting.postalCode;
    document.getElementById('cityName').value = posting.cityName;
    document.getElementById('state').value = posting.state;
    document.getElementById('description').value = posting.description;
    document.getElementById('skills').value = posting.skills;
};

updatePosting = function () {
    const postingId = document.getElementById('postingId').value;
    const title = document.getElementById('title').value;
    const active = document.getElementById('active').value;
    const startDate = document.getElementById('startDate').value;
    const address = document.getElementById('address').value;
    const postalCode = document.getElementById('postalCode').value;

    // !!! how do we update this? / do I need to? 
    const cityName = document.getElementById('cityName').value;
    const state = document.getElementById('state').value;
    const description = document.getElementById('description').value;
    const skills = document.getElementById('skills').value;

    //const accountId = document.getElementById('accountId').value;
    // don't think i need to update account ID / shouldn't even be possible?

    let urlPath = 'http://localhost:6789/posting';
    urlPath += ('/?postingId=' + postingId + '&title=' + title + '&active=' + active + '&startDate=' + startDate + '&address=' + address + '&postalCode=' + postalCode + '&cityName=' + cityName + '&state=' + state + '&description=' + description + '&skills=' + skills);

    fetch(urlPath,
        {method: 'PUT'})
        .then((res) => res.json())
        .then((data) => console.log(data))
        .catch((err) => console.error(err));
};

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

searchPostings = function () {
    const title = document.getElementById('title').value;
    const cityName = document.getElementById('cityName').value;
    const state = document.getElementById('state').value;
    const skills = document.getElementById('skills').value;

    let urlPath = 'http://localhost:6789/searchPostings';
    urlPath += ('/?title=' + title + '&cityName=' + cityName + '&state=' + state + '&skills=' + skills);

    fetch(urlPath,
        {method: 'GET'})
        .then((res) => res.json())
        .then((data) => console.log(data))
        .catch((err) => console.error(err));
};


window.onload = function () {
    var url = new URL(window.location.href);
    var postingId = url.searchParams.get("postingId");
    getPosting(postingId);
};
