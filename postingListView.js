getPosting = function (postingId) {
    let urlPath = 'http://localhost:6789/posting/' + postingId;
    fetch(urlPath)
    .then((res) => res.json())
    .then(function(data) {
        console.log(data);
        populatePostingView(data);
    })
    .catch((err) => console.error(err));
};

getAllPostings = function () {
    let urlPath = 'http://localhost:6789/postings/';
    fetch(urlPath)
    .then((res) => res.json())
    .then(function(data) {
        console.log(data);
        populatePostingView(data);
    })
    .catch((err) => console.error(err));
};

populateSkills = function (skills) {
    let innerHTML = '<h1>Skills</h1>';
    for (let i = 0; i < skills.length; i++) {
        innerHTML += "<span class='skillBox'>"+skills[i].name+"</span>";
    }
    document.getElementById('skills').innerHTML = innerHTML;
};

populatePostingView = function(postings){
    let innerHTML; //= "<div class=tbody id=tableBody>";
    for (let i = 0; i < postings.length; i++) {
        innerHTML += "<tr>"+"<div id="+i">"+"<th scope='row'>"+i+"</th>"+
        "<td>"+postings[i].postingId+"</td>"+
        "<td>"+postings[i].title+"</td>"+
        "<td>"+postings[i].active+"</td>"+
        "<td>"+postings[i].startDate+"</td>"+
        "<td>"+postings[i].address+"</td>"+
        "<td>"+postings[i].postalCode+"</td>"+
        "<td>"+postings[i].cityName+"</td>"+
        "<td>"+postings[i].state+"</td>"+
        "<td>"+postings[i].description+"</td>"+
        "<td>"+posting[i].skills+"</td>";

        innerHTML += "<td><div class='btn-group btn-group-sm' role='button'><button type='submit' class='btn btn-success btn-sm' role='button'>"+
                    "Apply"+
                "</button></div></td>";

        innerHTML += "</div>" + "</tr>";
    }
    document.getElementById('tableBody').innerHTML = innerHTML;
};

getSkills = function(postingId){
    let urlPath = 'http://localhost:6789/postingSkill/'+postingId;
    fetch(urlPath)
    .then((res) => res.json())
    .then(function(data){
        console.log(data);
        populateSkills(data);
    })
    .catch((err) => console.error(err));
};

window.onload = function(){
    var url = new URL(window.location.href);
    var accountId = url.searchParams.get("accountId");
    getAllPostings();
    getSkills(postingId);
}