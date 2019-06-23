// displaying a grid of postings (either all postings or a subset that have been searched for)

getAllPostings = function () {
    // or do i need to combine with search here
    let urlPath = 'http://localhost:6789/allPostings/';
    fetch(urlPath)
        .then((res) => res.json())
        .then(data => {
            console.log(data);
            console.log("post data");
            populatePostingTable(data);
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

//postingData is array of interview json objects
//map iterates through and calls postingTemplate on each one
//join removes commas between array elements
function populatePostingTable(postingData) {
    document.getElementById("postingTable").innerHTML = `${postingData.map(postingTemplate).join('')}`;
}

window.onload = function () {
    var url = new URL(window.location.href);
    getAllPostings();
};