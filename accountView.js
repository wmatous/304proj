populateAccountView = function(accounts){
    var account = accounts[0];
    document.getElementById('name').value = account.name;
    document.getElementById('email').value = account.email;
    document.getElementById('accountId').value = account.accountId;
    document.getElementById('postalCode').value = account.postalCode;
    if (account.size){
        document.getElementById('companyView').style.visibility = 'visible';
        document.getElementById('size').value = account.size;
        document.getElementById('industry').value = account.industry;
    } else {
        document.getElementById('individualView').style.visibility = 'visible';
        document.getElementById('age').value = account.age;
        document.getElementById('status').value = account.status;
    }
}

updateAccount = function(){
    const name = document.getElementById('name').value;
    const email = document.getElementById('email').value;
    const postalCode = document.getElementById('postalCode').value;
    const accountId = document.getElementById('accountId').value;
    let cmpSize, cmpIndustry, indStatus, indAge;
    if (document.getElementById('companyView')){
        cmpSize = document.getElementById('cmpSize').value;
        cmpIndustry = document.getElementById('cmpIndustry').value;
    } else {
        indStatus = document.getElementById('cmpInduindStatusstry').value;
        indAge = document.getElementById('indAge').value;
    }

    let urlPath = 'http://localhost:6789/account';
    urlPath += ('/?name='+name+'&email='+email+'&postalCode='+postalCode +'&accountId='+accountId);
    if (cmpSize){
        urlPath+= ('&size='+cmpSize+'&industry='+cmpIndustry);
    } else {
        urlPath+= ('&status='+indStatus+'&age='+indAge);
    }

    fetch(urlPath, 
        {method:'PUT'})
    .then((res) => res.json())
    .then(data => console.log(data))
    .catch(err => console.error(err));
}

getAccount = function(accountId){
    let urlPath = 'http://localhost:6789/account/?accountId='+accountId;
    fetch(urlPath)
    .then((res) => res.json())
    .then(data => {
        console.log(data);
        populateAccountView(data);
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

getSkills = function(accountId){
    let urlPath = 'http://localhost:6789/skill/'+accountId;
    fetch(urlPath)
    .then((res) => res.json())
    .then(data => {
        console.log(data);
        populateSkills(data);
    })
    .catch(err => console.error(err));

}

populateEndorsements = function(endorsements){
    document.getElementById('endorseCount').innerHTML = "Endorsed by" + endorsements[0].count;
}

getEndorsements = function(accountId){
    let urlPath = 'http://localhost:6789/endorsement/'+accountId;
    fetch(urlPath)
    .then((res) => res.json())
    .then(data => {
        console.log(data);
        populateEndorsements(data);
    })
    .catch(err => console.error(err));

}

window.onload = function(){
    var url = new URL(window.location.href);
    var accountId = url.searchParams.get("accountId");
    getAccount(accountId);
    getSkills(accountId);
    getEndorsements(accountId);
}
