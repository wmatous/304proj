window.onload = function () {
    var url = new URL(window.location.href);
    var offerId = url.searchParams.get("offerId");
    getAllOffers(offerId);
};

getAllOffers = function (offerId) {
    let urlPath = 'http://localhost:6789/offers/' + offerId;
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
                <th scope="row">${offer.offerId}</th>
                <td><button type="submit" class="btn btn-link btn-sm" onsubmit="viewOffer(${offer.offerId})"
                role="button">View</button></td>
                <td>${offer.company}</td>
                <td>${offer.position}</td>
                <td>${offer.status}</td>
                <td>${offer.startDate}</td>
                <td>${offer.expiryDate}</td>
            </div>
        </tr>`
}

viewOffer = function (offerId) {
//modify from offerView once its done
};