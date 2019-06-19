window.onload = function(){
    var url = new URL(window.location.href);
    var applicantId = url.searchParams.get("applicantId");
    getAllInterviews(applicantId);
};

getAllInterviews = function(applicantId){
    let urlPath = 'http://localhost:6789/interviews/'+applicantId;
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
function populateInterviewTable(interviewData){
    document.getElementById("interviewTable").innerHTML =
        `${interviewData.map(interviewTemplate).join('')}`;
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
};