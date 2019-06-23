// for a single posting on a page (when creating a posting or searching for a posting)

// getPosting = function (postingId) {
//     let urlPath = 'http://localhost:6789/posting/?postingId=' + postingId;
//     fetch(urlPath)
//         .then((res) => res.json())
//         .then(function (data) {
//             console.log(data);
//             populateSinglePostingView(data);
//         })
//         .catch((err) => console.error(err));
// };

// populateSinglePostingView = function (postings) {
//     var posting = postings[0];
//     document.getElementById('postingId').value = posting.POSTINGID;
//     document.getElementById('title').value = posting.TITLE;
//     document.getElementById('active').value = posting.ACTIVE;
//     document.getElementById('startDate').value = posting.STARTDATE;
//     document.getElementById('address').value = posting.ADDRESS;
//     document.getElementById('postalCode').value = posting.POSTALCODE;
//     document.getElementById('cityName').value = posting.CITYNAME;
//     document.getElementById('state').value = posting.STATE;
//     document.getElementById('description').value = posting.DESCRIPTION;
//     document.getElementById('skills').value = posting.NAME;
// };

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
        {method: 'POST'})
        .then((res) => res.json())
        .then((data) => console.log(data))
        .catch((err) => console.error(err));
};


searchPostings = function () {
    const title = document.getElementById('title').value;
    const cityName = document.getElementById('cityName').value;
    const state = document.getElementById('state').value;

    console.log(title + cityName + state);

    let urlPath = 'http://localhost:6789/searchPostings/';
    urlPath += ('/?title=' + title + '&cityName=' + cityName + '&state=' + state);
    // if (title) {
    //     urlPath += ('&title=' + title);
    // }
    // if (cityName) {
    //     urlPath += ('&cityName=' + cityName);
    // }
    // if (state) {
    //     urlPath += ('&state=' + state);
    // }

    console.log("fetching data");
    fetch(urlPath,
        {method: 'GET'})
        .then((res) => res.json())
        .then((data) => console.log(data))
        .catch((err) => console.error(err));
};


window.onload = function () {
    var url = new URL(window.location.href);
    var postingId = Math.floor(Math.random);
    document.getElementById('postingId').value = postingId;
    //getPosting(postingId);
};
