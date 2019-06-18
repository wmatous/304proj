populateInterviewView = function(interviews){
    var interview = interviews[0];
    document.getElementById('status').value = interview.status;
    document.getElementById('date').value = interview.date;
    document.getElementById('time').value = interview.time;
    document.getElementById('address').value = interview.address;
};

getInterview = function(applicantId){
    let urlPath = 'http://localhost:6789/interview/'+applicantId;
    fetch(urlPath)
        .then((res) => res.json())
        .then(data => {
            console.log(data);
            populateInterviewView(data);
        })
        .catch(err => console.error(err));
};

window.onload = function(){
    var url = new URL(window.location.href);
    var applicantId = url.searchParams.get("applicantId");
    getInterview(applicantId);
};
