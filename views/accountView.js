populateAccountView = function (accounts) {
    var account = accounts[0];
    document.getElementById('name').value = account.NAME;
    document.getElementById('email').value = account.EMAIL;
    document.getElementById('accountId').value = account.ACCOUNTID;
    document.getElementById('postalCode').value = account.POSTALCODE;
    if (account.CSIZE) {
        document.getElementById('companyView').style.visibility = 'visible';
        document.getElementById('size').value = account.SIZE;
        document.getElementById('industry').value = account.INDUSTRY;
    } else {
        document.getElementById('individualView').style.visibility = 'visible';
        document.getElementById('age').value = account.AGE;
        document.getElementById('status').value = account.STATUS;
    }
    
};

updateAccount = function () {
    const name = document.getElementById('name').value;
    const email = document.getElementById('email').value;
    const postalCode = document.getElementById('postalCode').value;
    const accountId = document.getElementById('accountId').value;
    let cmpSize, cmpIndustry, indStatus, indAge;
    if (document.getElementById('companyView').style.visibility == 'visible') {
        cmpSize = document.getElementById('cmpSize').value;
        cmpIndustry = document.getElementById('cmpIndustry').value;
    } else {
        indStatus = document.getElementById('status').value;
        indAge = document.getElementById('age').value;
    }

    let urlPath = 'http://localhost:6789/account';
    urlPath += ('/?name=' + name + '&email=' + email + '&postalCode=' + postalCode + '&accountId=' + accountId);
    if (cmpSize) {
        urlPath += ('&size=' + cmpSize + '&industry=' + cmpIndustry);
    } else {
        urlPath += ('&status=' + indStatus + '&age=' + indAge);
    }
    fetch(urlPath,
        {method: 'POST'})
        .then((res) => res.json())
        .then(data => console.log(data))
        .catch(err => console.error(err));
};

getAccount = function (accountId) {
    let urlPath = 'http://localhost:6789/account/?accountId=' + accountId;
    fetch(urlPath)
        .then((res) => res.json())
        .then(data => {
            console.log(data);
            populateAccountView(data);
        })
        .catch(err => console.error(err));
};

populateSkills = function (skills) {
    let innerHTML = `<h1>Skills</h1>`;
    for (let i = 0; i < skills.length; i++) {
        innerHTML += `<span class='skillBox'>${skills[i].NAME}</span>`;
    }
    document.getElementById('skills').innerHTML = innerHTML;
};

getSkills = function (accountId) {
    let urlPath = 'http://localhost:6789/skill/?accountId=' + accountId;
    fetch(urlPath)
        .then((res) => res.json())
        .then(data => {
            console.log(data);
            populateSkills(data);
        })
        .catch(err => console.error(err));

};

populateEndorsements = function (endorsements) {
    document.getElementById('endorseCount').innerHTML = "Endorsed by" + endorsements[0].COUNT;
};

getEndorsements = function (accountId) {
    let urlPath = 'http://localhost:6789/endorsement/?accountId=' + accountId;
    fetch(urlPath)
        .then((res) => res.json())
        .then(data => {
            console.log(data);
            populateEndorsements(data);
        })
        .catch(err => console.error(err));
};
populateRecommended = function (recs) {
};

getRecommended = function (loggedInAccount) {
    let urlPath = 'http://localhost:6789/recommended/?accountId=' + loggedInAccount;
    fetch(urlPath)
        .then((res) => res.json())
        .then(data => {
            console.log(data);
            populateRecommended(data);
        })
        .catch(err => console.error(err));
};

getCookie = function (name) {
    var value = "; " + document.cookie;
    var parts = value.split("; " + name + "=");
    if (parts.length == 2) return parts.pop().split(";").shift();
};

window.onload = function () {
    var url = new URL(window.location.href);
    var accountId = url.searchParams.get("accountId");
    getAccount(accountId);
    getSkills(accountId);
    getEndorsements(accountId);
    var loggedInAccount = getCookie('accountId');
    if (loggedInAccount == accountId) {
        document.getElementById("saveButton").hidden = false;
        getRecommended(loggedInAccount);
    }

};
