populateOfferView = function(offerData){
    var offer = offerData[0];
    document.getElementById('o-status').innerHTML = `<h3> Status: ${offer.status}</h3>`;
    document.getElementById('o-expiryDate').innerHTML = `<h3> Expires: ${offer.status}</h3>`;
    document.getElementById('o-details').innerHTML = `
        <h4>Details</h4>
          <p class="lead">Information about this job offer</p>
          <hr class="my-4">
          OfferID: ${offer.offerId}
          <br> Employment Type: ${offer.type}
          <br> Start Date: ${offer.startDate}
          <br> 
          ////iff there terminating = true display endDate else dont.
          <br> Hours: ${offer.hours}
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
    document.getElementById('companyInfo').innerHTML = `
        <div class="jumbotron">
            <h4> Company-Name-Here </h4>
            <p class="lead">Information about your potential future employer.</p>
            <hr class="my-4">
            Number of Employees:
            <br> Industry:
            <br> Address:
            <br> Email Address:
        </div>`;
    document.getElementById('jobInfo').innerHTML = `
        <div class="jumbotron">
            <h4> Position-Name-Here </h4>
            <p class="lead">Information about your potential future employment.</p>
            <hr class="my-4">
            <div class="container">
                <div class="row">
                    <div class="col-lg-9 col-md-9 col-sm-12 col-xs-12">
                        Posting Identification Number:
                        <br> Description:
                    </div>
                    <div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
                        <a href="#" class="btn btn-outline-dark btn-lg" role="button">View Job Posting</a>
                    </div>
                </div>
            </div>
        </div>`;
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

