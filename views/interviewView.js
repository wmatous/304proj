

getCookie = function(name) {
    var value = "; " + document.cookie;
    var parts = value.split("; " + name + "=");
    if (parts.length == 2) return parts.pop().split(";").shift();
};

getInterview = function (applicationId) {
    let urlPath = 'http://localhost:6789/interview/?applicationId=' + applicationId;
    fetch(urlPath)
        .then((res) => res.json())
        .then(data => {
            console.log(data);
            populateInterviewView(data);
        })
        .catch(err => console.error(err));
};

populateInterviewView = function (interviewData) {
    var interview = interviewData[0];
    document.getElementById('i-status').innerHTML = `<h3> Status: ${interview.STATUS}</h3>`;
    document.getElementById('i-date').innerHTML = `<h3> Date: ${interview.INTDATE}</h3>`;
    document.getElementById('i-address').innerHTML = `<h3> Address: ${interview.ADDRESS}</h3>`;
    if (interview.STATUS != "accepted" && interview.STATUS != "declined" && getCookie('accountId') == interview.APPLICANTID){
        document.getElementById('buttons').innerHTML = `
        <button class="btn btn-success btn-lg" role="button" onclick='acceptInterview(${interview.INTERVIEWID})' >Accept</button>
        <button  class="btn btn-danger btn-lg" role="button" onclick='declineInterview(${interview.INTERVIEWID})' >Decline</button>`;
    }
    
    document.getElementById('i-position').innerHTML = `
    <h2><a href="#jobInfo" data-toggle="collapse">Position Information</a></h2>
            <div id="jobInfo" class="collapse-in">
                <div class="jumbotron">
                    <h4> ${interview.TITLE} </h4>
                    <p class="lead">Information about your potential future employment.</p>
                    <hr class="my-4">
                    <div class="container">
                        <div class="row">
                            <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                                Posting Identification Number: ${interview.POSTINGID}
                                <br> Description: ${interview.DESCRIPTION}
                            </div>
                        </div>
                    </div>
                </div>
            </div>`;

    document.getElementById('i-company').innerHTML = `
            <h2><a href="#companyInfo" data-toggle="collapse">Company Information</a></h2>
            <div id="companyInfo" class="collapse-in">
                <div class="jumbotron">
                    <h4> ${interview.COMPANYNAME} </h4>
                    <p class="lead">Information about your potential future employer.</p>
                    <hr class="my-4">
                    Number of Employees: ${interview.COMPANYSIZE}
                    <br> Industry: ${interview.COMPANYINDUSTRY}
                    <br> Address: ${interview.ADDRESS}
                    <br> Email Address: ${interview.COMPANYEMAIL}
                </div>
            </div>`;
};

acceptInterview = function (applicationId) {
    let urlPath = 'http://localhost:6789/interview';
    urlPath += ('/?applicationId=' + applicationId + '&status=' + "accepted");
    fetch(urlPath,
        {method: 'POST'})
        .then((res) => res.json())
        .then(data => console.log(data))
        .catch(err => console.error(err));
};

declineInterview = function (applicationId) {
    let urlPath = 'http://localhost:6789/interview';
    urlPath += ('/?applicationId=' + applicationId + '&status=' + "declined");
    fetch(urlPath,
        {method: 'POST'})
        .then((res) => res.json())
        .then(data => console.log(data))
        .catch(err => console.error(err));
};

displayButtons = function (applicationId) {
    `    <button type="submit" class="btn btn-success btn-lg" onsubmit="acceptInterview(${applicationId})" role="button">
     Accept
     </button>
     <button type="submit" class="btn btn-danger btn-lg" onsubmit="declineInterview(${applicationId})" role="button">
     Decline
     </button>`
};

window.onload = function () {
    var url = new URL(window.location.href);
    var applicationId = url.searchParams.get("applicationId");
    getInterview(applicationId);
};