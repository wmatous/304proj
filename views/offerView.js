populateOfferView = function(offerData){
    var offer = offerData[0];
    document.getElementById('o-status').innerHTML = `<h3> Status: ${offer.status}</h3>`;
    document.getElementById('o-details').innerHTML = `
        <h4>Details</h4>
          <p class="lead">Information about this job offer</p>
          <hr class="my-4">
          OfferID: ${offer.offerId}
          <br> Employment Type: ${offer.type}
          <br> Start Date: ${offer.startDate}
          ////iff there terminating = true display endDate else dont.
          <br> Monetary Compensation: $ ${offer.compensation} per hour
          <br> <br>
          <div class="btn-group btn-group-lg" role="button">
              <button type="submit" class="btn btn-success btn-lg" role="button">
                  Accept
              </button>
              <button type="submit" class="btn btn-danger btn-lg" role="button">
                  Decline
              </button>
          </div>`;
    document.getElementById('o-offerId').innerHTML = `<h3> Status: ${offer.status}</h3>`;
    document.getElementById('o-hours').innerHTML = `<h3> Status: ${offer.status}</h3>`;
    document.getElementById('o-compensation').innerHTML = `<h3> Status: ${offer.status}</h3>`;
    document.getElementById('o-terminating').innerHTML = `<h3> Status: ${offer.status}</h3>`;
    document.getElementById('o-startDate').innerHTML = `<h3> Status: ${offer.status}</h3>`;
    document.getElementById('o-endDate').innerHTML = `<h3> Status: ${offer.status}</h3>`;
    document.getElementById('o-expiryDate').innerHTML = `<h3> Expiry Date: ${offer.expiryDate}</h3>`;
};

getOffer = function(offerId){
    let urlPath = 'http://localhost:6789/offer/'+offerId;
    fetch(urlPath)
        .then((res) => res.json())
        .then(data => {
            populateOfferTable(data);
            populateOfferView(data);
        })
        .catch(err => console.error(err));
};

function offerTemplate(offer){
    return `
        <tr class="offerTableRow">
            <div>
                <th scope="row">${offer.offerId}</th>
                <td>${offer.company}</td>
                <td>${offer.position}</td>
                <td>${offer.status}</td>
                <td>${offer.startDate}</td>
                <td>${offer.expiryDate}</td>
            </div>
        </tr>`
}

//offerData is array of interview json objects
//map iterates through and calls interviewTemplate on each one
//join removes commas between array elements
function populateOfferTable(offerData) {
    document.getElementById("offerTable").innerHTML = `${offerData.map(offerTemplate).join('')}`;
}

window.onload = function(){
    var url = new URL(window.location.href);
    var offerId = url.searchParams.get("offerId");
    getOffer(offerId);
};

