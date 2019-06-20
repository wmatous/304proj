window.onload = function () {
    var loggedInAccount = getCookie('accountId');
    getAllOffers(loggedInAccount);
};

getCookie = function(name) {
    var value = "; " + document.cookie;
    var parts = value.split("; " + name + "=");
    if (parts.length == 2) return parts.pop().split(";").shift();
};


getAllOffers = function (accountId) {
    let urlPath = 'http://localhost:6789/allOffers/?accountId=' + accountId;
    fetch(urlPath)
        .then((res) => res.json())
        .then(data => {
            populateOfferTable(data);
        })
        .catch(err => console.error(err));
};

//offerData is array of interview json objects
//map iterates through and calls interviewTemplate on each one
//join removes commas between array elements
function populateOfferTable(offerData) {
    document.getElementById("offerTable").innerHTML = `${offerData.map(offerTemplate).join('')}`;
}

function offerTemplate(offer) {
    return `
        <tr class="offerTableRow">
            <div>
                <th scope="row">${offer.OFFERID}</th>
                <td><button type="submit" class="btn btn-link btn-sm" onsubmit="viewOffer(${offer.OFFERID})"
                role="button">View</button></td>
                <td>${offer.COMPANY}</td>
                <td>${offer.TITLE}</td>
                <td>${offer.STATUS}</td>
                <td>${offer.STARTDATE}</td>
                <td>${offer.EXPIRYDATE}</td>
            </div>
        </tr>`
}

viewOffer = function (offerId) {
//modify from offerView once its done
};