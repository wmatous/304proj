// displaying a grid of postings (either all postings or a subset that have been searched for)

getAllPostings = function (title, cityName, state) {
    // or do i need to combine with search here
    let urlPath = 'http://localhost:6789/allPostings/';

    if (title) {
        urlPath += ('/?title=' + title + '&cityName=' + cityName + '&state=' + state);
    }
    console.log(urlPath);
    fetch(urlPath)
        .then((res) => res.json())
        .then(data => {
            console.log(data);
            console.log("post data");
            populatePostingTable(data);
        })
        .catch(err => console.error(err));
};

getPostingsWithoutLocation = function () {
    // or do i need to combine with search here
    let urlPath = 'http://localhost:6789/postingsWithoutLocation/';
    fetch(urlPath)
        .then((res) => res.json())
        .then(data => {
            console.log(data);
            populatePostingTable(data, true);
        })
        .catch(err => console.error(err));
};

function postingTemplate(posting) {

    return `
        <tr class="postingTableRow">
            <div>
                <th scope="row"> ${posting.POSTINGID}</th>
                <td><a href="http://localhost:6789/application/"+${posting.ACCOUNTID}+${posting.POSTINGID} style="color: blue; text-decoration: underline"> ${posting.TITLE} </a></td>
                <td>${posting.ACTIVE}</td>
                <td>${posting.STARTDATE}</td>
                <td>${posting.ADDRESS}</td>
                <td>${posting.POSTALCODE}</td>
                <td>${posting.CITYNAME}</td>
                <td>${posting.STATE}</td>
                <td>${posting.DESCRIPTION}</td>
                <td>${posting.NAME}</td>
            </div>
        </tr>`;
}

function postingTemplateWithoutLocation(posting) {
    return `
        <tr class="postingTableRow">
            <div>
                <th scope="row"> ${posting.POSTINGID}</th>
                <td><a href="http://localhost:6789/application/"+${posting.ACCOUNTID}+${posting.POSTINGID} style="color: blue; text-decoration: underline"> ${posting.TITLE} </a></td>
                <td>${posting.ACTIVE}</td>
                <td>${posting.STARTDATE}</td>
                <td>${posting.DESCRIPTION}</td>
                <td>${posting.NAME}</td>
            </div>
        </tr>`;
}

updatePostingDisplay = function() {
    var hideLocationChecked = document.getElementById('hideLocationCheckBox').checked;

    if (hideLocationChecked) {
        getPostingsWithoutLocation();
    } else {
        getAllPostings();
    }
}

//postingData is array of interview json objects
//map iterates through and calls postingTemplate on each one
//join removes commas between array elements
function populatePostingTable(postingData, hideLocation) {
    if (hideLocation) {
        document.getElementById("postingTableHeaders").innerHTML =                     
                    '<th scope="col" width="3%">ID</th><th scope="col" width="7%">Title</th><th scope="col" width="5%">Active</th><th scope="col" width="7%">StartDate</th>' +
                    '<th scope="col" width="15%">Description</th><th scope="col" width="15%">Skills</th>';
        document.getElementById("postingTable").innerHTML = `${postingData.map(postingTemplateWithoutLocation).join('')}`;
    } else {
        document.getElementById("postingTableHeaders").innerHTML = 
                    '<th scope="col" width="3%">ID</th><th scope="col" width="7%">Title</th><th scope="col" width="5%">Active</th><th scope="col" width="7%">StartDate</th>' +
                    '<th scope="col" width="7%">Address</th><th scope="col" width="5%">PostalCode</th><th scope="col" width="7%">City</th><th scope="col" width="7%">State</th>' +
                    '<th scope="col" width="15%">Description</th><th scope="col" width="15%">Skills</th>'
        document.getElementById("postingTable").innerHTML = `${postingData.map(postingTemplate).join('')}`;
    }
}

window.onload = function () {
    var url = new URL(window.location.href);
    var title = url.searchParams.get("title");
    var cityName = url.searchParams.get("cityName");
    var state = url.searchParams.get("state");
    console.log(title + cityName + state);
    getAllPostings(title, cityName, state);
};