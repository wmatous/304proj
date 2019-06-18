populatePostingView = function(postings){
    var posting = posting[0];
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
    // !!! both of these
    document.getElementById('accountId').value = posting.accountId;

    // if (account.size){
    //     document.getElementById('companyView').style.visibility = 'visible';
    //     document.getElementById('size').value = account.size;
    //     document.getElementById('industry').value = account.industry;
    // } else {
    //     document.getElementById('individualView').style.visibility = 'visible';
    //     document.getElementById('age').value = account.age;
    //     document.getElementById('status').value = account.status;
    // }
}

updatePosting = function(){
    const postingId = document.getElementById('postingId').value;
    const title = document.getElementById('title').value;
    const active = document.getElementById('active').value;
    const startDate = document.getElementById('startDate').value;
    const address = document.getElementById('address').value;
    const postalCode = document.getElementById('postalCode').value;
    const cityName = document.getElementById('cityName').value = posting.cityName;
    const state = document.getElementById('state').value = posting.state;
    // !!! how do we update this? / do I need to?

    const skills = document.getElementById('skills').value = posting.skills;
    // !!
    const description = document.getElementById('description').value;
    const accountId = document.getElementById('accountId').value;
    //let cmpSize, cmpIndustry, indStatus, indAge;
    // if (document.getElementById('companyView')){
    //     cmpSize = document.getElementById('cmpSize').value;
    //     cmpIndustry = document.getElementById('cmpIndustry').value;
    // } else {
    //     indStatus = document.getElementById('cmpInduindStatusstry').value;
    //     indAge = document.getElementById('indAge').value;
    // }

    let urlPath = 'http://localhost:6789/posting';
    urlPath += ('/?postingId='+postingId+'&title='+title+'&active='+active+'&startDate='+startDate+'&address='+address+'&postalCode='+postalCode+'&description='+description+'&accountId='+accountId);
    // if (cmpSize){
    //     urlPath+= ('&size='+cmpSize+'&industry='+cmpIndustry);
    // } else {
    //     urlPath+= ('&status='+indStatus+'&age='+indAge);
    // }

    fetch(urlPath, 
        {method:'PUT'})
    .then((res) => res.json())
    .then(data => console.log(data))
    .catch(err => console.error(err));
}

getPosting = function(postingId){
    let urlPath = 'http://localhost:6789/posting/'+postingId;
    fetch(urlPath)
    .then((res) => res.json())
    .then(data => {
        console.log(data);
        populatePostingView(data);
    })
    .catch(err => console.error(err));
}



populateSkills = function(skills){
    let innerHTML = '<h1>Skills</h1>';
    for (let i = 0; i < skills.length; i++){
        innerHTML += "<span class='skillBox'>"+skills[i].name+"</span>";
    }
    document.getElementById('skills').innerHTML = innerHTML;
}

getSkills = function(postingId){
    let urlPath = 'http://localhost:6789/postingSkill/'+postingId;
    fetch(urlPath)
    .then((res) => res.json())
    .then(data => {
        console.log(data);
        populateSkills(data);
    })
    .catch(err => console.error(err));

}

window.onload = function(){
    var url = new URL(window.location.href);
    var postingId = url.searchParams.get("postingId");
    getPosting(postingId);
    getSkills(postingId);
}