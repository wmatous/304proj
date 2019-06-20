

getCookie = function(name) {
    var value = "; " + document.cookie;
    var parts = value.split("; " + name + "=");
    if (parts.length == 2) return parts.pop().split(";").shift();
};

getInterview = function (applicantId) {
    let urlPath = 'http://localhost:6789/interview/?applicantId=' + applicantId;
    fetch(urlPath)
        .then((res) => res.json())
        .then(data => {
            populateInterviewView(data);
        })
        .catch(err => console.error(err));
};

populateInterviewView = function (interviewData) {
    var interview = interviewData[0];
    document.getElementById('i-status').innerHTML = `<h3> Status: ${interview.STATUS}</h3>`;
    document.getElementById('i-date').innerHTML = `<h3> Date: ${interview.DATE}</h3>`;
    document.getElementById('i-time').innerHTML = `<h3> Time: ${interview.TIME}</h3>`;
    document.getElementById('i-address').innerHTML = `<h3> Address: ${interview.ADDRESS}</h3>`;
    document.getElementById('viewButton').innerHTML = `
       <br>
       <a href="#" class="btn btn-outline-dark btn-md" onsubmit="viewPost(${interview.POSTINGID})" role="button">View Job Posting</a>`;
    document.getElementById('adButtons').innerHTML = `
        <script> if ($(interview.status) != "accepted" && (offer.status) != "declined") {
                    displayButtons(${interview.APPLICANTID});
                    } </script>`;
};

acceptInterview = function (applicantId) {
    let urlPath = 'http://localhost:6789/interview';
    urlPath += ('/?applicantId=' + applicantId + '&status=' + "accepted");
    fetch(urlPath,
        {method: 'POST'})
        .then((res) => res.json())
        .catch(err => console.error(err));
};

declineInterview = function (applicantId) {
    let urlPath = 'http://localhost:6789/interview';
    urlPath += ('/?applicantId=' + applicantId + '&status=' + "declined");
    fetch(urlPath,
        {method: 'POST'})
        .then((res) => res.json())
        .catch(err => console.error(err));
};

displayButtons = function (applicantId) {
    `    <button type="submit" class="btn btn-success btn-lg" onsubmit="acceptInterview(${applicantId})" role="button">
     Accept
     </button>
     <button type="submit" class="btn btn-danger btn-lg" onsubmit="declineInterview(${applicantId})" role="button">
     Decline
     </button>`
};

viewPost = function (postId) {
//copy from offerView once its done
};


window.onload = function () {
    var url = new URL(window.location.href);
    var applicantId = url.searchParams.get("applicantId");
    getInterview(applicantId);
};