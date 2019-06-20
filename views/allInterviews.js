getCookie = function (name) {
    var value = "; " + document.cookie;
    var parts = value.split("; " + name + "=");
    if (parts.length == 2) return parts.pop().split(";").shift();
};

getAllInterviews = function (accountId) {
    let urlPath = 'http://localhost:6789/allInterviews/?accountId=' + accountId;
    fetch(urlPath)
        .then((res) => res.json())
        .then(data => {
            populateInterviewTable(data);
        })
        .catch(err => console.error(err));
};

//interviewData is array of interview json objects
//map iterates through and calls interviewTemplate on each one
//join removes commas between array elements
function populateInterviewTable(interviewData) {
    document.getElementById("interviewTable").innerHTML =
        `${interviewData.map(interviewTemplate).join('')}`;
}

function interviewTemplate(interview) {
    return `
        <tr class="interviewTableRow">
            <div>
                <th scope="row">${interview.INTERVIEWID}</th>
                <td><button type="submit" class="btn btn-link btn-sm" onsubmit="viewInterview(${interview.INTERVIEWID})"
                role="button">View</button></td>
                <td>${interview.COMPANY}</td>
                <td>${interview.TITLE}</td>
                <td>${interview.STATUS}</td>
                <td>${interview.INTDATE}</td>
                <td>${interview.ADDRESS}</td>
            </div>
        </tr>`
}

viewInterview = function (applicationId) {
    window.location = '/'+window.location.pathname.split('/')[1]+'/interviewView.html?applicationId='+applicationId;
};

window.onload = function () {
    var loggedInAccount = getCookie('accountId');
    getAllInterviews(loggedInAccount);
};