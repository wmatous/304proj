populateInterviewView = function(interviewData){
    var interview = interviewData[0];
    document.getElementById('i-status').innerHTML = `<h3> Status: ${interview.status}</h3>`;
    document.getElementById('i-date').innerHTML = `<h3> Date: ${interview.date}</h3>`;
    document.getElementById('i-time').innerHTML = `<h3> Time: ${interview.time}</h3>`;
    document.getElementById('i-address').innerHTML = `<h3> Address: ${interview.address}</h3>`;
};

getInterview = function(applicantId){
    let urlPath = 'http://localhost:6789/interview/'+applicantId;
    fetch(urlPath)
        .then((res) => res.json())
        .then(data => {
            populateInterviewTable(data);
            populateInterviewView(data);
        })
        .catch(err => console.error(err));
};

function interviewTemplate(interview){
    return `
        <tr class="interviewTableRow">
            <div>
                <th scope="row">${interview.id}</th>
                <td>${interview.company}</td>
                <td>${interview.position}</td>
                <td>${interview.status}</td>
                <td>${interview.date}</td>
                <td>${interview.time}</td>
                <td>${interview.address}</td>
            </div>
        </tr>`
}

//interviewData is array of interview json objects
//map iterates through and calls interviewTemplate on each one
//join removes commas between array elements
function populateInterviewTable(interviewData){
    document.getElementById("interviewTable").innerHTML =
        `${interviewData.map(interviewTemplate).join('')}`;
}

window.onload = function(){
    var url = new URL(window.location.href);
    var applicantId = url.searchParams.get("applicantId");
    getInterview(applicantId);
};
